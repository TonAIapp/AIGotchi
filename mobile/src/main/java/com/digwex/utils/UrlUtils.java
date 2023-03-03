package com.digwex.utils;

import java.net.MalformedURLException;
import java.net.URL;


public class UrlUtils {
    private static URL createCorrectUrl(String st) {
        if (!st.contains("://"))
            st = "http://" + st;

        try {
            return new URL(st);
        } catch (MalformedURLException e) {
            //e.printStackTrace();
        }
        return null;
    }

    public static String tryParseUrl(String st) {
        if (!st.contains("://"))
            st = "http://" + st;
        try {
            return new URL(st).toString();
        } catch (MalformedURLException ignored) {
            //e.printStackTrace();
        }
        return null;
    }
}
