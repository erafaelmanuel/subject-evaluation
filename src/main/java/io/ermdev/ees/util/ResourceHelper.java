package io.ermdev.ees.util;

import java.io.InputStream;
import java.net.URL;

public class ResourceHelper {

    public static final String BASE_PATH = "/";

    private ResourceHelper() {
        super();
    }

    public static URL resource(final String path) {
        if (path != null)
           return ResourceHelper.class.getClass().getResource(path);
        return ResourceHelper.class.getClass().getResource("");
    }

    public static URL resourceWithBasePath(final String path) {
        if (path != null)
            return ResourceHelper.class.getClass().getResource(BASE_PATH + path);
        return ResourceHelper.class.getClass().getResource("");
    }

    public static InputStream resourceAsStream(final String path) {
        if (path != null)
            return ResourceHelper.class.getClass().getResourceAsStream(path);
        return ResourceHelper.class.getClass().getResourceAsStream("");
    }

    public static InputStream resourceAsStreamWithBasePath(final String path) {
        if (path != null)
            return ResourceHelper.class.getClass().getResourceAsStream(BASE_PATH + path);
        return ResourceHelper.class.getClass().getResourceAsStream("");
    }

    public static String dir() {
        return System.getProperty("user.dir");
    }

    public static String dirWithBasePath() {
        return System.getProperty("user.dir") + BASE_PATH;
    }
}
