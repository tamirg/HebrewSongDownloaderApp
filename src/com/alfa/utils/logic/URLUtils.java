package com.alfa.utils.logic;

import java.net.URI;
import java.net.URL;

/**
 * Created by Tamir on 11/02/14.
 */
public class URLUtils {
    public static URI parseUrl(String s) throws Exception {
        URL u = new URL(s);
        return new URI(
                u.getProtocol(),
                u.getAuthority(),
                u.getPath(),
                u.getQuery(),
                u.getRef());
    }
}
