package com.johnli.callback.result;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * @author johnli  2018-08-15 15:08
 */
public class UnRegisterResult implements Serializable {
    private static final long serialVersionUID = -3943054470560084215L;

    private boolean success;

    private String errorMsg;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public boolean isSuccess() {
        return success;
    }

    public UnRegisterResult setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public UnRegisterResult setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }
}
