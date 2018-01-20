package io.ermdev.ees.legacy.dao.conn;

import java.util.logging.Logger;

public class DbUserLibrary {

    private Logger logger = Logger.getLogger(DbUserLibrary.class.getSimpleName());

    private String host;
    private String port;
    private String username;
    private String password;
    private String driver;
    private String catalog;
    private String protocol;

    public DbUserLibrary() {
        super();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getConnectionUrl() {
        try {
            String url = protocol
                    .concat("://")
                    .concat(host)
                    .concat(":")
                    .concat(port)
                    .concat("/")
                    .concat(catalog);
            return url;
        } catch (NullPointerException e) {
            logger.info("NullPointerException");
            return "";
        }
    }

    @Override
    public String toString() {
        return getConnectionUrl().concat("?username=").concat(username).concat("&password=").concat(password);
    }
}
