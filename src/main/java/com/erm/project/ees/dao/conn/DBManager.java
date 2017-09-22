package com.erm.project.ees.dao.conn;

import com.erm.project.ees.util.ConnectionHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DBManager {

    private Logger logger = Logger.getLogger(DBManager.class.getSimpleName());

    private UserLibrary userLibrary;

    private boolean status;

    private Connection connection;

    public DBManager() {
        userLibrary = ConnectionHelper.getUserLibrary();
        status = connect(userLibrary);
    }

    public DBManager(UserLibrary userLibrary) {
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
            logger.info("SQLException");
            connection = null;
            return false;
        } catch (Exception e) {
            logger.info("Exception");
            connection = null;
            return false;
        }
    }
}
