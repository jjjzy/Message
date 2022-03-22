package com.jjjzy.messaging.Enums;

public enum Status {
    OK(1000, "Successful"),
    PASSWORDS_NOT_MATCH(2000, "Passwords don't match"),
    USERNAME_EXISTS(2001, "Username exists."),
    WRONG_VALIDATION_CODE(2002, "Wrong validation code"),
    USER_NOT_EXISTS(2003, "User not exists."),
    WRONG_PASSWORD(2004, "Password is wrong, try again");

    private int code;
    private String message;

    Status(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}