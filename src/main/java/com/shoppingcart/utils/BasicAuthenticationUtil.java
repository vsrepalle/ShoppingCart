package com.shoppingcart.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class BasicAuthenticationUtil {
    public static String[] getCredentials(String authorization) {
        // Authorization: Basic base64credentials
        String base64Credentials = authorization.substring("Basic".length()).trim();
        byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(credDecoded, StandardCharsets.UTF_8);
        // credentials = username:password
        final String[] values = credentials.split(":", 2);
        return values;
    }
}
