package com.erm.project.ees.util;

import java.io.InputStream;
import java.net.URL;

public class ResourceHelper {

    private ResourceHelper() {
        super();
    }

    public static URL resource(final String path) {
        if(path != null) {
            URL url = ResourceHelper.class.getClass().getResource(path);
            return url;
        }
        return ResourceHelper.class.getClass().getResource("");
    }

    public static InputStream resourceAsStream(final String path) {
        if(path != null) {
            InputStream inputStream = ResourceHelper.class.getClass().getResourceAsStream(path);
            return inputStream;
        }
        return ResourceHelper.class.getClass().getResourceAsStream("");
    }

    public static String dir() {
        return System.getProperty("user.dir");
    }
}