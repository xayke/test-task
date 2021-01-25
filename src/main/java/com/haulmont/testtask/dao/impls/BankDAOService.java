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

public class BankDAOService implements DAOService<Bank> {
    private static BankDAOService INSTANCE;
    private static final Logger LOGGER = LogManager.getLogger(BankDAOService.class);

    private BankDependentDaoService<Client> clientDAOService;
    private BankDependentDaoService<Credit> creditDAOService;
    private DatabaseService service;

    public BankDAOService() {
        service = DatabaseService.getInstance();
        clientDAOService = ClientDAOService.getInstance();
        creditDAOService = CreditDAOService.getInstance();
    }

    public static BankDAOService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BankDAOService();
        }
        return INSTANCE;
    }

    @Override
    public Bank findById(UUID id) {
        Bank result = null;
        LOGGER.info("Finding bank with id({}) in database...", id);
        try (Connection connection = service.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM bank WHERE id = ?")) {
            statement.setString(1, id.toString());
            ResultSet rs = statement.executeQuery();
            rs.next();
            result = getFromResultSet(rs);
            LOGGER.info("Bank with id({}) has been found", id);
        } catch (SQLException e) {
            throw new DaoServiceException("Can't acquire bank from database", e);
        }
        return result;
    }

    public Bank findBankByClient(Client client) {
        Bank result = null;
        LOGGER.info("Finding bank by client's id({}) in database...", client.getId());
        String query = "SELECT bank.id AS id, bank.name AS name " +
                "FROM bank " +
                "LEFT JOIN client ON client.bank_id = bank.id " +
                "WHERE client.id = ?;";
        try (Connection connection = service.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, client.getId().toString());
            ResultSet rs = statement.executeQuery();
            rs.next();
            result = getFromResultSet(rs);
            LOGGER.info("Bank with client's id ({}) has been found", client.getId());
        } catch (SQLException e) {
            throw new DaoServiceException("Can't acquire bank from database", e);
        }
        return result;
    }

    public Bank findBankByCredit(Credit credit) {
        Bank result = null;
        LOGGER.info("Finding bank by credit's id({}) in database...", credit.getId());
        String query = "SELECT bank.id AS id, bank.name AS name " +
                "FROM bank " +
                "LEFT JOIN credit ON credit.bank_id = bank.id " +
                "WHERE credit.id = ?;";
        try (Connection connection = service.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, credit.getId().toString());
            ResultSet rs = statement.executeQuery();
            rs.next();
            result = getFromResultSet(rs);
            LOGGER.info("Bank with credit's id ({}) has been found", credit.getId());
        } catch (SQLException e) {
            throw new DaoServiceException("Can't acquire bank from database", e);
        }
        return result;
    }

    @Override
    public List<Bank> findAll() {
        List<Bank> result = null;
        LOGGER.info("Finding banks in database...");
        try (Connection connection = service.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM bank")) {
            ResultSet rs = statement.executeQuery();
            result = new ArrayList<>();
            while (rs.next()) {
                Bank bank = getFromResultSet(rs);
                result.add(bank);
            }
            LOGGER.info("Banks has been found");
        } catch (SQLException e) {
            throw new DaoServiceException("Can't acquire bank from database", e);
        }
        return result;
    }

    @Override
    public void save(Bank bank) {
        LOGGER.info("Saving bank in database...");

        String query = "INSERT INTO bank(name, id) VALUES (?, ?)";
        try (Connection connection = service.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            initStatementParams(bank, statement);
            statement.execute();

            LOGGER.info("Bank has been saved");
        } catch (SQLException e) {
            throw new DaoServiceException("Can't acquire bank from database", e);
        }
    }

    @Override
    public void delete(Bank bank) {
        LOGGER.info("Deleting bank from database...");

        String query = "DELETE FROM bank WHERE id = ?;";

        try (Connection connection = service.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, bank.getId().toString()); // WHERE id = bank.id
            statement.execute();

            LOGGER.info("Bank's data has been deleted");
        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key no action"))
                throw new DaoServiceException("Can't delete  bank's data in database. " +
                        "Bank have clients and/or credits.", e);
            throw new DaoServiceException("Can't delete bank's data in database", e);
        }
    }

    @Override
    public void update(Bank bank) {
        LOGGER.info("Updating bank's data in database...");

        String query = "UPDATE bank SET name = ? WHERE id = ?;";

        try (Connection connection = service.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            initStatementParams(bank, statement);
            statement.executeUpdate();

            LOGGER.info("Bank's data has been updated");
        } catch (SQLException e) {
            throw new DaoServiceException("Can't update bank's data in database", e);
        }
    }

    private Bank getFromResultSet(ResultSet rs) throws SQLException {
        Bank bank = new Bank();
        bank.setId(UUID.fromString(rs.getString("id")));
        bank.setName(rs.getString("name"));
        bank.setClients(clientDAOService.findAllWhereBankId(bank.getId()));
        bank.setCredits(creditDAOService.findAllWhereBankId(bank.getId()));
        return bank;
    }

    private void initStatementParams(Bank bank, PreparedStatement statement) throws SQLException {
        statement.setString(1, bank.getName());
        statement.setString(2, bank.getId().toString());
    }
}
