package io.erm.ees.util;

import io.erm.ees.dao.conn.DbUserLibrary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;

public class ConnectionHelper {

    public static DbUserLibrary getUserLibrary() {
        try {
            InputStream inputStream = ResourceHelper.resourceAsStreamWithBasePath("connection.properties");
            Properties properties = new Properties();
            properties.load(inputStream);

            DbUserLibrary dbUserLibrary = new DbUserLibrary();
            dbUserLibrary.setDriver(properties.getProperty("db.driver"));
            dbUserLibrary.setHost(properties.getProperty("db.host"));
            dbUserLibrary.setPort(properties.getProperty("db.port"));
            dbUserLibrary.setUsername(properties.getProperty("db.user"));
            dbUserLibrary.setPassword(properties.getProperty("db.password"));
            dbUserLibrary.setCatalog(properties.getProperty("db.catalog"));
            dbUserLibrary.setProtocol(properties.getProperty("db.protocol"));
            return dbUserLibrary;
        } catch (IOException e) {
            e.printStackTrace();
            return new DbUserLibrary();
        }
    }

    public static void setUserLibrary(DbUserLibrary dbUserLibrary) {
        try {
            File file = new File(ResourceHelper.resourceWithBasePath("connection.properties").toURI().getPath());
            FileOutputStream fos = new FileOutputStream(file);

            Properties properties = new Properties();
            properties.put("db.driver", dbUserLibrary.getDriver());
            properties.put("db.host", dbUserLibrary.getHost());
            properties.put("db.port", dbUserLibrary.getPort());
            properties.put("db.user", dbUserLibrary.getUsername());
            properties.put("db.password", dbUserLibrary.getPassword());
            properties.put("db.catalog", dbUserLibrary.getCatalog());
            properties.put("db.protocol", dbUserLibrary.getProtocol());

            properties.store(fos, "");
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
