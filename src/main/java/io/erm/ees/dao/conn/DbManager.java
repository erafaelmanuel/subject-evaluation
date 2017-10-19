package io.erm.ees.dao.conn;

import io.erm.ees.util.ConnectionHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DbManager {

    private Logger logger = Logger.getLogger(DbManager.class.getSimpleName());

    private UserLibrary userLibrary;

    private boolean status;

    private Connection connection;

    public DbManager() {
        userLibrary = ConnectionHelper.getUserLibrary();
        status = connect(userLibrary);
    }

    public DbManager(UserLibrary userLibrary) {
        this.userLibrary = userLibrary;
        status = connect(userLibrary);
    }

    public boolean getConnectionStatus() {
        return status;
    }

    public Connection getConnection() {
        return connection;
    }

    public UserLibrary getUserLibrary() {
        return userLibrary;
    }

    public boolean connect(UserLibrary userLibrary) {
        this.userLibrary = userLibrary;
        return connect();
    }

    public boolean connect() {
        try {
            String url = userLibrary.getConnectionUrl();
            String username = userLibrary.getUsername();
            String password = userLibrary.getPassword();
            Class.forName(userLibrary.getDriver());

            connection = DriverManager.getConnection(url, username, password);
            return true;
        } catch (ClassNotFoundException e) {
            logger.info("ClassNotFoundException");
            connection = null;
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.info("SQLException");
            connection = null;
            return false;
        } catch (Exception e) {
            logger.info("Exception");
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
