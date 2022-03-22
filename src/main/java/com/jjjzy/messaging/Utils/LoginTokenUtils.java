package com.jjjzy.messaging.Utils;

import org.apache.commons.lang3.RandomStringUtils;

public class LoginTokenUtils {
    public static String generateToken(){
        String result = RandomStringUtils.random(64, true, true);
        return result;
    }
}
