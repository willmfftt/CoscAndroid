package com.cosc.bandfanapp.util;

/**
 * @author William Moffitt
 * @version 1.0 10/28/15
 */
public class ErrorCode {

    public enum Code {
        USER_INCOMPLETE,
        USER_GENERIC_ERROR,
        USERNAME_INVALID,
        PASSWORD_INVALID,
        GENERIC_ERROR
    }

    public Code errorCode;

    public ErrorCode(Code code) {
        errorCode = code;
    }

    public static Code getCode(int code) {
        switch (code) {
            case 0:
                return Code.USER_INCOMPLETE;
            case 1:
                return Code.USER_GENERIC_ERROR;
            case 2:
                return Code.USERNAME_INVALID;
            case 3:
                return Code.PASSWORD_INVALID;
            default:
                return Code.GENERIC_ERROR;
        }
    }

}
