package com.johnli.callback.server.log.digest;

import java.util.Date;

/**
 * @author johnli  2018-08-30 17:08
 */
public class CallbackDigestLog extends DigestLogInfo {
    private String uid;
    private String alias;
    private Date nextExecTime;
    private Integer errorCode;
    private String errorMsg;
    private boolean success;

    public String getUid() {
        return uid;
    }

    public CallbackDigestLog setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public CallbackDigestLog setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public Date getNextExecTime() {
        return nextExecTime;
    }

    public CallbackDigestLog setNextExecTime(Date nextExecTime) {
        this.nextExecTime = nextExecTime;
        return this;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public CallbackDigestLog setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public CallbackDigestLog setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public CallbackDigestLog setSuccess(boolean success) {
        this.success = success;
        return this;
    }

}
