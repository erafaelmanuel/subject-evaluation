package io.ermdev.ees.dao.conn;

import io.ermdev.ees.util.ConnectionHelper;

import java.sql.*;
import java.util.logging.Logger;

public class DbManager {

    private Logger logger = Logger.getLogger(DbManager.class.getSimpleName());

    private DbUserLibrary dbUserLibrary;

    private boolean status;

    private Connection connection;

    public DbManager() {
        dbUserLibrary = ConnectionHelper.getUserLibrary();
        status = connect(dbUserLibrary);
    }

    public DbManager(DbUserLibrary dbUserLibrary) {
        this.dbUserLibrary = dbUserLibrary;
        status = connect(dbUserLibrary);
    }

    public boolean getConnectionStatus() {
        return status;
    }

    public Connection getConnection() {
        return connection;
    }

    public DbUserLibrary getDbUserLibrary() {
        return dbUserLibrary;
    }

    public boolean connect(DbUserLibrary dbUserLibrary) {
        this.dbUserLibrary = dbUserLibrary;
        return connect();
    }

    public boolean connect() {
        try {
            String url = dbUserLibrary.getConnectionUrl();
            String username = dbUserLibrary.getUsername();
            String password = dbUserLibrary.getPassword();
            Class.forName(dbUserLibrary.getDriver());

            connection = DriverManager.getConnection(url, username, password);
            connection.createStatement().executeUpdate("use " + dbUserLibrary.getCatalog());

            return true;
        } catch (ClassNotFoundException e) {
            logger.warning("ClassNotFoundException");
            connection = null;
            return false;
        } catch (SQLException e) {
            logger.warning(e.getMessage());
            connection = null;
            return false;

        } catch (Exception e) {
            logger.warning("Exception");
            connection = null;
            return false;
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            logger.warning("SQLException");
        }
    }
}
