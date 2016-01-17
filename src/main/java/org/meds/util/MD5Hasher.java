package org.meds.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.meds.logging.Logging;

public class MD5Hasher {

    public static String computeHash(String string) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest dm = MessageDigest.getInstance("MD5");
            dm.update(string.getBytes());
            byte[] digest = dm.digest();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
        } catch(NoSuchAlgorithmException ex) {
            Logging.Fatal.log("MD5Hasher throws NoSuchAlgorithmException: " + ex.getMessage());
            return null;
        }

        return sb.toString();
    }

    public static String computePasswordHash(String password) {
        return computeHash(computeHash(password) + "dsdarkswords");
    }
}
