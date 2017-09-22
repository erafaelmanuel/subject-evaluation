package com.erm.project.ees.util;

import com.erm.project.ees.dao.conn.UserLibrary;

import java.io.*;
import java.util.Properties;

public class ConnectionHelper {

    public static final String CONNECTION_PROPERTIES = "/connection.properties";

    public static UserLibrary getUserLibrary() {
        try {
            InputStream inputStream = ResourceHelper.resourceAsStream(CONNECTION_PROPERTIES);
            Properties properties = new Properties();
            properties.load(inputStream);

            UserLibrary userLibrary = new UserLibrary();
            userLibrary.setDriver(properties.getProperty("db.driver"));
            userLibrary.setHost(properties.getProperty("db.host"));
            userLibrary.setPort(properties.getProperty("db.port"));
            userLibrary.setUsername(properties.getProperty("db.user"));
            userLibrary.setPassword(properties.getProperty("db.password"));
            userLibrary.setCatalog(properties.getProperty("db.catalog"));
            userLibrary.setProtocol(properties.getProperty("db.protocol"));

            return userLibrary;
        } catch (IOException e) {
            e.printStackTrace();
            return new UserLibrary();
        }
    }

    public static void setUserLibrary(UserLibrary userLibrary) {
        try {
            File file = new File(ResourceHelper.dir() + "/src/main/resources" + CONNECTION_PROPERTIES);
            FileOutputStream fos = new FileOutputStream(file);

            Properties properties = new Properties();
            properties.put("db.driver", userLibrary.getDriver());
            properties.put("db.host", userLibrary.getHost());
            properties.put("db.port", userLibrary.getPort());
            properties.put("db.user", userLibrary.getUsername());
            properties.put("db.password", userLibrary.getPassword());
            properties.put("db.catalog", userLibrary.getCatalog());
            properties.put("db.protocol", userLibrary.getProtocol());

            properties.store(fos, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
