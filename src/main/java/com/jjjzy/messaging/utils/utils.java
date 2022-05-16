package com.jjjzy.messaging.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class utils {
    public static String md5(String input) {
        String md5 = null;

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(input.getBytes(), 0, input.length());
            md5 = new BigInteger(1, digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }

    public static String generateToken(){
        String result = RandomStringUtils.random(64, true, true);
        return result;
    }
}
