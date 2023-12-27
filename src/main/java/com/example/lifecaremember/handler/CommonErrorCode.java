package com.example.lifecaremember.handler;

import lombok.Getter;

@Getter
public enum CommonErrorCode {
    // 400
    BAD_REQUEST(400, "BAD_REQUEST", "Bad request"),

    SYSTEM_ERROR(400, "SYSTEM_ERROR", "An error occurred"),
    USERNAME_NOT_FOUND (400, "USERNAME_NOT_FOUND", "Username not found"),
    INVALID_USERNAME_PASSWORD (400, "INVALID_USERNAME_PASSWORD", "Invalid username or password"),

    INVALID_PASSWORD(400, "INVALID_PASSWORD", "Invalid password"),
    INVALID_EMAIL(400, "INVALID_EMAIL", "Invalid email"),
    INVALID_FROM_TO_DATE(400, "INVALID_FROM_TO_DATE", "Invalid from date or to date"),
    MEMBER_ID_EXISTS(400, "MEMBER_ID_ALREADY_EXISTS", "Member id already exists");

    private  int status;
    private final String code;
    private final String message;

    CommonErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    CommonErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
