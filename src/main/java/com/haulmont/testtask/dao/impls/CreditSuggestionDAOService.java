package com.haulmont.testtask.dao.impls;

import com.haulmont.testtask.dao.DAOService;
import com.haulmont.testtask.dao.DaoServiceException;
import com.haulmont.testtask.database.DatabaseService;
import com.haulmont.testtask.domain.Client;
import com.haulmont.testtask.domain.Credit;
import com.haulmont.testtask.domain.CreditSuggestion;
import com.haulmont.testtask.domain.Payment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreditSuggestionDAOService implements DAOService<CreditSuggestion> {
    private static CreditSuggestionDAOService INSTANCE;
    private static final Logger LOGGER = LogManager.getLogger(CreditSuggestionDAOService.class);

    private final DatabaseService service;
    private final DAOService<Client> clientService;
    private final DAOService<Credit> creditService;
    private final PaymentDAOService paymentService;

    private CreditSuggestionDAOService() {
        service = DatabaseService.getInstance();
        clientService = ClientDAOService.getInstance();
        creditService = CreditDAOService.getInstance();
        paymentService = PaymentDAOService.getInstance();
    }

    public static CreditSuggestionDAOService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CreditSuggestionDAOService();
        }
        return INSTANCE;
    }

    @Override
    public CreditSuggestion findById(UUID id) {
        CreditSuggestion result = null;
        LOGGER.info("Finding credit suggestion with id({}) in database...", id);
        try (Connection connection = service.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM credit_suggestion " +
                     "WHERE id = ?")) {
            statement.setString(1, id.toString());
            ResultSet rs = statement.executeQuery();
            rs.next();
            result = getFromResultSet(rs);
            LOGGER.info("Credit suggestion with id({}) has been found", id);
        } catch (SQLException e) {
            throw new DaoServiceException("Can't acquire credit suggestion from database", e);
        }
        return result;
    }

    @Override
    public List<CreditSuggestion> findAll() {
        List<CreditSuggestion> result = null;
        LOGGER.info("Acquiring all credit suggestions from database...");
        try (Connection connection = service.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM credit_suggestion")) {
            ResultSet rs = statement.executeQuery();
            result = new ArrayList<>();
            while (rs.next()) {
                CreditSuggestion suggestion = getFromResultSet(rs);
                result.add(suggestion);
            }
            LOGGER.info("Credit suggestions has been found");
        } catch (SQLException e) {
            throw new DaoServiceException("Can't acquire credit suggestions from database", e);
        }
        return result;
    }

    @Override
    public void save(CreditSuggestion suggestion) {
        LOGGER.info("Saving credit suggestion in database...");

        String query = "INSERT INTO credit_suggestion(client_id, credit_id, amount, id)" +
                " VALUES (?, ?, ?, ?);";

        try (Connection connection = service.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            initStatementParams(suggestion, statement);
            statement.execute();

            LOGGER.info("Credit suggestion has been saved");
        } catch (SQLException e) {
            throw new DaoServiceException("Can't save credit suggestion in database", e);
        }
    }

    @Override
    public void delete(CreditSuggestion suggestion) {
        LOGGER.info("Deleting credit suggestion from database...");

        String query = "DELETE FROM credit_suggestion " +
                "WHERE id = ?;";

        try (Connection connection = service.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, suggestion.getId().toString()); // WHERE id = credit_suggestion.id
            statement.execute();

            LOGGER.info("Credit suggestion's data has been deleted");
        } catch (SQLException e) {
            throw new DaoServiceException("Can't delete credit's data in database", e);
        }
    }

    @Override
    public void update(CreditSuggestion suggestion) {
        LOGGER.info("Updating credit suggestion's data in database...");

        String query = "UPDATE credit_suggestion " +
                "SET client_id = ?, credit_id = ?, amount = ? " +
                "WHERE id = ?;";

        try (Connection connection = service.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            initStatementParams(suggestion, statement);
            statement.executeUpdate();

            LOGGER.info("Credit suggestion's data has been updated");
        } catch (SQLException e) {
            throw new DaoServiceException("Can't update credit suggestion's data in database", e);
        }
    }

    private CreditSuggestion getFromResultSet(ResultSet rs) throws SQLException, DaoServiceException {
        CreditSuggestion creditSuggestion = new CreditSuggestion();
        Client client = clientService.findById(UUID.fromString(rs.getString("client_id")));
        Credit credit = creditService.findById(UUID.fromString(rs.getString("credit_id")));
        List<Payment> payments = paymentService.findAll(UUID.fromString(rs.getString("id")));
        creditSuggestion.setId(UUID.fromString(rs.getString("id")));
        creditSuggestion.setClient(client);
        creditSuggestion.setCredit(credit);
        creditSuggestion.setAmount(rs.getString("amount"));
        creditSuggestion.setPaymentPlan(payments);
        return creditSuggestion;
    }

    private void initStatementParams(CreditSuggestion suggestion, PreparedStatement statement) throws SQLException {
        statement.setString(1, suggestion.getClient().getId().toString());
        statement.setString(2, suggestion.getCredit().getId().toString());
        statement.setString(3, suggestion.getAmount());
        statement.setString(4, suggestion.getId().toString());
    }
}
