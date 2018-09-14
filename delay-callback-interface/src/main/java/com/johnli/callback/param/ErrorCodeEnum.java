package com.johnli.callback.param;

/**
 * @author johnli  2018-08-29 10:07
 */
public enum ErrorCodeEnum {
    ALIAS_NOT_EXIST(1, "alias not exist"),
    CALLBACK_BIZ_FAILED(2, "callback biz failed"),
    CALLBACK_BIZ_EXCEPTION(3, "callback biz exception"),
    CALLBACK_EXCEPTION(4, "callback exception"),
    DB_EXCEPTION(5, "db exception");

    private int code;
    private String desc;


    ErrorCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
