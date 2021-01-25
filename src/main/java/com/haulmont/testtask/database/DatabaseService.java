package com.haulmont.testtask.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hsqldb.cmdline.SqlFile;
import org.hsqldb.cmdline.SqlToolError;

import java.io.*;
import java.sql.*;

public class DatabaseService {
    private static final String DRIVER_CLASS = "org.hsqldb.jdbc.JDBCDriver";
    private static final Logger LOGGER = LogManager.getLogger(DatabaseService.class);
    private static DatabaseService INSTANCE;

    private final String URL = "jdbc:hsqldb:file:./db/bank;shutdown=true";
    private final String USER = "SA";
    private final String PASSWORD = "";

    private DatabaseService() {
        loadDriverClass();
        if (!isTablesExist()) {
            LOGGER.info("Some tables not found :(");
            createTables();
            if (isTablesExist()) {
                LOGGER.info("All tables exist");
                fillTables();
            }
        }
        if (isTablesEmpty()) {
            fillTables();
        }
        LOGGER.info("Database service initialized");
    }

    public static DatabaseService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseService();
        }
        return INSTANCE;
    }

    private void loadDriverClass() {
        try {
            LOGGER.info("HSQLDB driver class loading...");
            Class.forName(DRIVER_CLASS);
            LOGGER.info("HSQLDB driver class has been loaded");
        } catch (ClassNotFoundException e) {
            LOGGER.error("Can't load HSQLDB JDBC driver class", e);
        }
    }

    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        return connection;
    }

    private void createTables() {
        LOGGER.info("Creating tables...");
        SqlFile file = null;
        try (InputStream is = getClass().getResourceAsStream("/sql/create_tables.sql");
             Connection connection = getConnection()) {
            file = new SqlFile(new InputStreamReader(is), "create_tables", System.out,
                    "UTF-8", false, new File("."));

            file.setConnection(connection);
            file.execute();
            LOGGER.info("Tables has been created.");
        } catch (IOException e) {
            LOGGER.error("Can't read SQL script file", e);
            throw new DatabaseServiceException("Can't read SQL script file", e);
        } catch (SQLException e) {
            LOGGER.error("Can't establish database connection or error occurred in executing process");
            throw new DatabaseServiceException("Can't establish database connection or error occurred in " +
                    "executing process", e);
        } catch (SqlToolError e) {
            LOGGER.error("Can't execute SQL file script", e);
            throw new DatabaseServiceException("Can't execute SQL file script", e);
        }
    }

    private boolean isTablesExist() {
        boolean creditTableExists = false,
                clientTableExists = false,
                creditSuggestionsTableExists = false,
                paymentTableExists = false;
        LOGGER.info("Checking tables existence...");
        try {
            DatabaseMetaData md = getConnection().getMetaData();
            ResultSet rs = md.getTables(null, null, "%", null);
            while (rs.next()) {
                String res = rs.getString(3);
                if (res.equals("CLIENT")) {
                    clientTableExists = true;
                    continue;
                }
                if (res.equals("CREDIT")) {
                    creditTableExists = true;
                    continue;
                }
                if (res.equals("CREDIT_SUGGESTION")) {
                    creditSuggestionsTableExists = true;
                }
                if (res.equals("PAYMENT")) {
                    paymentTableExists = true;
                }
            }
            LOGGER.info("Tables existence checked");
        } catch (SQLException e) {
            LOGGER.error("Can't establish connection", e);
        }
        return (clientTableExists && creditTableExists && creditSuggestionsTableExists && paymentTableExists);
    }

    private boolean isTablesEmpty() {
        boolean results[] = {false, false, false, false};
        String sqls[] = {
                "SELECT COUNT(*) AS result FROM CLIENT;",
                "SELECT COUNT(*) AS result FROM CREDIT;",
                "SELECT COUNT(*) AS result FROM CREDIT_SUGGESTION;",
                "SELECT COUNT(*) AS result FROM PAYMENT;"
        };
        LOGGER.info("Checking tables on emptiness");
        for (int i = 0; i < sqls.length; i++) {
            try (Connection connection = getConnection();
                 Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery(sqls[i]);
                rs.next();
                if (rs.getInt("result") == 0) {
                    results[i] = true;
                }
            } catch (SQLException e) {
                LOGGER.error("Can't establish connection or error occurred in executing process", e);
            }
        }
        for (boolean result : results) {
            if (result) {
                LOGGER.info("At least one table is empty :(");
                return true;
            }
        }
        LOGGER.info("All tables have data :)");
        return false;
    }

    private void fillTables() {
        LOGGER.info("Filling tables with data");
        SqlFile file = null;
        try (InputStream is = getClass().getResourceAsStream("/sql/fill_tables.sql");
             Connection connection = getConnection()) {
            file = new SqlFile(new InputStreamReader(is), "create_tables", System.out,
                    "UTF-8", false, new File("."));

            file.setConnection(connection);
            file.execute();
            org.hsqldb.DatabaseManager.closeDatabases(0); // saving changes in db/bank.script file
            LOGGER.info("Tables has been filled.");
        } catch (IOException e) {
            LOGGER.error("Can't read SQL script file", e);
            throw new DatabaseServiceException("Can't read SQL script file", e);
        } catch (SQLException e) {
            LOGGER.error("Can't establish database connection or error occurred in executing process");
            throw new DatabaseServiceException("Can't establish database connection or error occurred in " +
                    "executing process", e);
        } catch (SqlToolError e) {
            LOGGER.error("Can't execute SQL file script", e);
            throw new DatabaseServiceException("Can't execute SQL file script", e);
        }
    }
}
