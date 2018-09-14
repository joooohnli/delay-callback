package com.johnli.callback.param;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * @author johnli  2018-08-09 16:31
 */
public class ErrorCode implements Serializable {

    private static final long serialVersionUID = -3105295977465733411L;
    private int code;
    private String desc;
    private String msg;


    private ErrorCode() {
    }

    public ErrorCode(ErrorCodeEnum codeEnum) {
        this(codeEnum, codeEnum.getDesc());
    }

    public ErrorCode(ErrorCodeEnum codeEnum, String msg) {
        this.code = codeEnum.getCode();
        this.desc = codeEnum.getDesc();
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


    public String getMsg() {
        return msg;
    }

    public ErrorCode setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}

