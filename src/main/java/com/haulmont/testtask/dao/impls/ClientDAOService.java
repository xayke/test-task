package com.haulmont.testtask.dao.impls;

import com.haulmont.testtask.dao.DAOService;
import com.haulmont.testtask.dao.DaoServiceException;
import com.haulmont.testtask.database.DatabaseService;
import com.haulmont.testtask.domain.Bank;
import com.haulmont.testtask.domain.Client;
import com.haulmont.testtask.domain.Credit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientDAOService extends BankDependentDaoService<Client> implements DAOService<Client> {
    private static ClientDAOService INSTANCE;
    private static final Logger LOGGER = LogManager.getLogger(ClientDAOService.class);

    private final DatabaseService service;

    private ClientDAOService() {
        service = DatabaseService.getInstance();
    }

    public static ClientDAOService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClientDAOService();
        }
        return INSTANCE;
    }

    @Override
    public Client findById(UUID id) throws DaoServiceException {
        Client result = null;
        LOGGER.info("Finding client with id({}) in database...", id);
        try (Connection connection = service.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM client WHERE id = ?")) {
            statement.setString(1, id.toString());
            ResultSet rs = statement.executeQuery();
            rs.next();
            result = getFromResultSet(rs);
            LOGGER.info("Client with id({}) have been found", id);
        } catch (SQLException e) {
            throw new DaoServiceException("Can't acquire client from database", e);
        }
        return result;
    }

    @Override
    public List<Client> findAll() throws DaoServiceException {
        List<Client> result = null;
        LOGGER.info("Acquiring all clients from database...");
        try (Connection connection = service.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM client")) {
            ResultSet rs = statement.executeQuery();
            result = new ArrayList<>();
            while (rs.next()) {
                Client client = getFromResultSet(rs);
                result.add(client);
            }
            LOGGER.info("Clients have been found");
        } catch (SQLException e) {
            throw new DaoServiceException("Can't acquire clients from database", e);
        }
        return result;
    }

    @Override
    public List<Client> findAllWhereBankId(UUID bankId) {
        List<Client> result = null;
        LOGGER.info("Acquiring all clients where bank_id ({})from database...", bankId);
        try (Connection connection = service.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM client WHERE bank_id = ?;")) {
            statement.setString(1, bankId.toString());
            ResultSet rs = statement.executeQuery();
            result = new ArrayList<>();
            while (rs.next()) {
                Client credit = getFromResultSet(rs);
                result.add(credit);
            }
            LOGGER.info("Clients where bank_id ({}) has been found", bankId);
        } catch (SQLException e) {
            throw new DaoServiceException("Can't acquire clients from database", e);
        }
        return result;
    }

    @Override
    public void save(Client client, UUID bankId) throws DaoServiceException {
        LOGGER.info("Saving client({} {}) in database...", client.getFirstname(), client.getLastname());

        String query = "INSERT INTO client(id, bank_id, firstname, lastname, patronymic, phone, email, passport)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection connection = service.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            initStatementParams(client, bankId, statement);
            statement.execute();

            LOGGER.info("Client ({} {}) have been saved", client.getFirstname(), client.getLastname());
        } catch (SQLException e) {
            throw new DaoServiceException("Can't save client in database", e);
        }
    }

    @Override
    public void save(Client client) throws DaoServiceException {
        throw new DaoServiceException("Bank ID required for saving client");
    }

    @Override
    public void delete(Client client) throws DaoServiceException {
        LOGGER.info("Deleting client ({} {}) from database...", client.getFirstname(), client.getLastname());

        String query = "DELETE FROM client " +
                "WHERE id = ?;";

        try (Connection connection = service.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, client.getId().toString()); // WHERE id = client.id
            statement.execute();

            LOGGER.info("Client's ({} {}) data have been deleted", client.getFirstname(), client.getLastname());
        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key no action"))
                throw new DaoServiceException("Can't delete  client's data in database. " +
                        "Client have credit suggestions.", e);
            throw new DaoServiceException("Can't delete client's data in database", e);
        }
    }

    @Override
    public void update(Client client, UUID bankId) throws DaoServiceException {
        LOGGER.info("Updating client's data in database...");

        String query = "UPDATE client " +
                "SET id = ?, bank_id = ?, firstname = ?, lastname = ?, patronymic = ?, phone = ?, email = ?, passport = ? " +
                "WHERE id = ?;";

        try (Connection connection = service.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            initStatementParams(client, bankId, statement);
            statement.setString(9, client.getId().toString()); // WHERE id = client.id
            statement.executeUpdate();

            LOGGER.info("Client's data have been updated");
        } catch (SQLException e) {
            throw new DaoServiceException("Can't update client's data in database", e);
        }
    }

    @Override
    public void update(Client client) throws DaoServiceException {
        throw new DaoServiceException("Bank ID required for saving client");
    }

    private Client getFromResultSet(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(UUID.fromString(rs.getString("id")));
        client.setFirstname(rs.getString("firstname"));
        client.setLastname(rs.getString("lastname"));
        if (rs.getString("patronymic") != "NULL") client.setPatronymic(rs.getString("patronymic"));
        client.setPhone(rs.getString("phone"));
        if (rs.getString("email") != "NULL") client.setEmail(rs.getString("email"));
        client.setPassport(rs.getString("passport"));
        return client;
    }

    private void initStatementParams(Client client, UUID bankId, PreparedStatement statement) throws SQLException {
        statement.setString(1, client.getId().toString());
        statement.setString(2, bankId.toString());
        statement.setString(3, client.getFirstname());
        statement.setString(4, client.getLastname());
        if (client.getPatronymic() != null) {
            statement.setString(5, client.getPatronymic());
        } else {
            statement.setNull(5, Types.VARCHAR);
        }

        statement.setString(6, client.getPhone());
        if (client.getEmail() != null) {
            statement.setString(7, client.getEmail());
        } else {
            statement.setNull(7, Types.VARCHAR);
        }
        statement.setString(8, client.getPassport());
    }
}
