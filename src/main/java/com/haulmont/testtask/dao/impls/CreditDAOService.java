package com.haulmont.testtask.dao.impls;

import com.haulmont.testtask.dao.DAOService;
import com.haulmont.testtask.dao.DaoServiceException;
import com.haulmont.testtask.database.DatabaseService;
import com.haulmont.testtask.domain.Credit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreditDAOService extends BankDependentDaoService<Credit> implements DAOService<Credit> {
    private static CreditDAOService INSTANCE;
    private static final Logger LOGGER = LogManager.getLogger(CreditDAOService.class);

    private final DatabaseService service;

    private CreditDAOService() {
        service = DatabaseService.getInstance();
    }

    public static CreditDAOService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CreditDAOService();
        }
        return INSTANCE;
    }

    @Override
    public Credit findById(UUID id) throws DaoServiceException {
        Credit result = null;
        LOGGER.info("Finding credit with id({}) in database...", id);
        try (Connection connection = service.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM credit WHERE id = ?")) {
            statement.setString(1, id.toString());
            ResultSet rs = statement.executeQuery();
            rs.next();
            result = getFromResultSet(rs);
            LOGGER.info("Credit with id({}) has been found", id);
        } catch (SQLException e) {
            throw new DaoServiceException("Can't acquire credit from database", e);
        }
        return result;
    }

    @Override
    public List<Credit> findAll() throws DaoServiceException {
        List<Credit> result = null;
        LOGGER.info("Acquiring all credits from database...");
        try (Connection connection = service.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM credit")) {
            ResultSet rs = statement.executeQuery();
            result = new ArrayList<>();
            while (rs.next()) {
                Credit credit = getFromResultSet(rs);
                result.add(credit);
            }
            LOGGER.info("Credits has been found");
        } catch (SQLException e) {
            throw new DaoServiceException("Can't acquire credits from database", e);
        }
        return result;
    }

    @Override
    public List<Credit> findAllWhereBankId(UUID bankId) {
        List<Credit> result = null;
        LOGGER.info("Acquiring all credits where bank_id ({})from database...", bankId);
        try (Connection connection = service.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM credit WHERE bank_id = ?;")) {
            statement.setString(1, bankId.toString());
            ResultSet rs = statement.executeQuery();
            result = new ArrayList<>();
            while (rs.next()) {
                Credit credit = getFromResultSet(rs);
                result.add(credit);
            }
            LOGGER.info("Credits where bank_id ({}) has been found", bankId);
        } catch (SQLException e) {
            throw new DaoServiceException("Can't acquire credits from database", e);
        }
        return result;
    }

    @Override
    public void save(Credit credit, UUID bankId) throws DaoServiceException {
        LOGGER.info("Saving credit in database...");

        String query = "INSERT INTO credit(bank_id, name, min_limit, max_limit, percents, id)" +
                " VALUES (?, ?, ?, ?, ?, ?);";

        try (Connection connection = service.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            initStatementParams(credit, bankId, statement);
            statement.execute();

            LOGGER.info("Credit has been saved");
        } catch (SQLException e) {
            throw new DaoServiceException("Can't save credit in database", e);
        }
    }

    @Override
    public void save(Credit credit) throws DaoServiceException {
        throw new DaoServiceException("Bank ID required for saving credit");
    }

    @Override
    public void delete(Credit credit) throws DaoServiceException {
        LOGGER.info("Deleting credit from database...");

        String query = "DELETE FROM credit WHERE id = ?;";

        try (Connection connection = service.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, credit.getId().toString()); // WHERE id = credit.id
            statement.execute();

            LOGGER.info("Credit's data has been deleted");
        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key no action"))
                throw new DaoServiceException("Can't delete  credit's data in database. " +
                        "Credit have credit suggestions.", e);
            throw new DaoServiceException("Can't delete credit's data in database", e);
        }
    }

    @Override
    public void update(Credit credit, UUID bankId) throws DaoServiceException {
        LOGGER.info("Updating credit's data in database...");

        String query = "UPDATE credit " +
                "SET bank_id = ?, name = ?, min_limit = ?, max_limit = ?, percents = ?" +
                " WHERE id = ?;";

        try (Connection connection = service.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            initStatementParams(credit, bankId, statement);
            statement.executeUpdate();

            LOGGER.info("Credit's data has been updated");
        } catch (SQLException e) {
            throw new DaoServiceException("Can't update credit's data in database", e);
        }
    }

    @Override
    public void update(Credit credit) throws DaoServiceException {
        throw new DaoServiceException("Bank ID required for updating credit");
    }

    private Credit getFromResultSet(ResultSet rs) throws SQLException {
        Credit credit = new Credit();
        credit.setId(UUID.fromString(rs.getString("id")));
        credit.setName(rs.getString("name"));
        credit.setMinLimit(rs.getString("min_limit"));
        credit.setMaxLimit(rs.getString("max_limit"));
        credit.setPercent(rs.getString("percents"));
        return credit;
    }

    private void initStatementParams(Credit credit, UUID bankId, PreparedStatement statement) throws SQLException {
        statement.setString(1, bankId.toString());
        statement.setString(2, credit.getName());
        statement.setString(3, credit.getMinLimit());
        statement.setString(4, credit.getMaxLimit());
        statement.setString(5, credit.getPercent());
        statement.setString(6, credit.getId().toString());
    }
}
