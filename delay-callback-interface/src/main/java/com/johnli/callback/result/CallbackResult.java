package com.johnli.callback.result;

import com.johnli.callback.param.ErrorCode;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * @author johnli  2018-08-08 10:23
 */
public class CallbackResult implements Serializable {
    private static final long serialVersionUID = -1667934918643842844L;

    private boolean success;

    private ErrorCode errorCode;


    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public boolean isSuccess() {
        return success;
    }

    public CallbackResult setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public CallbackResult setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
        return this;
    }
}
