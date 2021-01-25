package com.haulmont.testtask.dao.impls;

import com.haulmont.testtask.dao.DAOService;
import com.haulmont.testtask.dao.DaoServiceException;
import com.haulmont.testtask.database.DatabaseService;
import com.haulmont.testtask.domain.Payment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PaymentDAOService implements DAOService<Payment> {
    private static PaymentDAOService INSTANCE;
    private static final Logger LOGGER = LogManager.getLogger(PaymentDAOService.class);

    private final DatabaseService service;

    private PaymentDAOService() {
        service = DatabaseService.getInstance();
    }

    public static PaymentDAOService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PaymentDAOService();
        }
        return INSTANCE;
    }

    @Override
    public Payment findById(UUID id) throws DaoServiceException {
        Payment result = null;
        LOGGER.info("Finding payment with id({}) in database...", id);
        try (Connection connection = service.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM payment WHERE id = ?")) {
            statement.setString(1, id.toString());
            ResultSet rs = statement.executeQuery();
            rs.next();
            result = getFromResultSet(rs);
            LOGGER.info("Payment with id({}) has been found", id);
        } catch (SQLException e) {
            throw new DaoServiceException("Can't acquire payment from database", e);
        } catch (ParseException e) {
            throw new DaoServiceException("Can't parse date");
        }
        return result;
    }

    @Override
    public List<Payment> findAll() throws DaoServiceException {
        List<Payment> result = null;
        LOGGER.info("Acquiring all payments from database...");
        try (Connection connection = service.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM payment")) {
            ResultSet rs = statement.executeQuery();
            result = new ArrayList<>();
            while (rs.next()) {
                Payment payment = getFromResultSet(rs);
                result.add(payment);
            }
            LOGGER.info("Payments has been found");
        } catch (SQLException e) {
            throw new DaoServiceException("Can't acquire payments from database", e);
        } catch (ParseException e) {
            throw new DaoServiceException("Can't parse date");
        }
        return result;
    }

    public List<Payment> findAll(UUID suggestionId) throws DaoServiceException {
        List<Payment> result = null;
        LOGGER.info("Acquiring all payments of credit suggestion ({}) from database...",
                suggestionId.toString());

        try (Connection connection = service.getConnection();
             PreparedStatement statement = connection.
                     prepareStatement("SELECT * FROM payment WHERE CREDIT_SUGG_ID = ?")) {

            statement.setString(1, suggestionId.toString());
            ResultSet rs = statement.executeQuery();

            result = new ArrayList<>();
            while (rs.next()) {
                Payment payment = getFromResultSet(rs);
                result.add(payment);
            }

            LOGGER.info("Payments of credit suggestion ({}) has been found", suggestionId.toString());
        } catch (SQLException e) {
            throw new DaoServiceException("Can't acquire payments of credit suggestion from database", e);
        } catch (ParseException e) {
            throw new DaoServiceException("Can't parse date");
        }
        return result;
    }

    @Override
    public void save(Payment payment) throws DaoServiceException {
        LOGGER.info("Saving payment in database...");

        String query = String.format("INSERT INTO payment(pay_date, full_amount, principal_amount, interest_amount, " +
                "credit_sugg_id, id)" +
                " VALUES (?, ?, ?, ?, ?, ?);");

        try (Connection connection = service.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            initStatementParams(payment, statement);
            statement.execute();

            LOGGER.info("Payment has been saved");
        } catch (SQLException e) {
            throw new DaoServiceException("Can't save payment in database", e);
        }
    }

    @Override
    public void delete(Payment payment) throws DaoServiceException {
        LOGGER.info("Deleting payment from database...");

        String query = String.format("DELETE FROM payment " +
                "WHERE id = ?;");

        try (Connection connection = service.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, payment.getId().toString()); // WHERE id = payment.id
            statement.execute();

            LOGGER.info("Payment's data has been deleted");
        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key no action"))
                throw new DaoServiceException("Can't delete  payment's data in database. " +
                        "Payment related to credit suggestion.", e);
            throw new DaoServiceException("Can't delete payment's data in database", e);
        }
    }

    @Override
    public void update(Payment payment) throws DaoServiceException {
        LOGGER.info("Updating payment's data in database...");

        String query = String.format("UPDATE payment " +
                "SET pay_date = ?, full_amount = ?, principal_amount = ?, " +
                "interest_amount = ?, credit_sugg_id = ? WHERE id = ?;");

        try (Connection connection = service.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            initStatementParams(payment, statement);
            statement.executeUpdate();

            LOGGER.info("Payment's data has been updated");
        } catch (SQLException e) {
            throw new DaoServiceException("Can't update payment's data in database", e);
        }
    }

    private Payment getFromResultSet(ResultSet rs) throws SQLException, ParseException {
        Payment payment = new Payment();

        payment.setId(UUID.fromString(rs.getString("id")));
        payment.setDate(rs.getString("pay_date"));
        payment.setFullAmount(rs.getString("full_amount"));
        payment.setPrincipalAmount(rs.getString("principal_amount"));
        payment.setInterestAmount(rs.getString("interest_amount"));
        payment.setSuggestionId(UUID.fromString(rs.getString("credit_sugg_id")));

        return payment;
    }

    private void initStatementParams(Payment payment, PreparedStatement statement) throws SQLException {
        statement.setString(1, payment.getFormattedStringDate());
        statement.setString(2, payment.getFullAmount());
        statement.setString(3, payment.getPrincipalAmount());
        statement.setString(4, payment.getInterestAmount());
        statement.setString(5, payment.getSuggestionId().toString());
        statement.setString(6, payment.getId().toString());
    }
}
