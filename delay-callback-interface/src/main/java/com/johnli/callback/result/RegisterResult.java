package com.johnli.callback.result;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * @author johnli  2018-08-08 10:21
 */
public class RegisterResult implements Serializable {
    private static final long serialVersionUID = 7238236792896686786L;

    private boolean success;

    private String uid;

    private String errorMsg;

    public boolean isSuccess() {
        return success;
    }

    public RegisterResult setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getUid() {
        return uid;
    }

    public RegisterResult setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public RegisterResult setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
