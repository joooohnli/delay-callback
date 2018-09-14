package com.johnli.callback.server.log.digest;

/**
 * @author johnli  2018-08-30 17:08
 */
public class RegisterDigestLog extends DigestLogInfo {
    private String uid;
    private String alias;
    private String errorMsg;
    private boolean success;
    private int delaySeconds;

    public String getUid() {
        return uid;
    }

    public RegisterDigestLog setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public RegisterDigestLog setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public RegisterDigestLog setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public RegisterDigestLog setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public int getDelaySeconds() {
        return delaySeconds;
    }

    public RegisterDigestLog setDelaySeconds(int delaySeconds) {
        this.delaySeconds = delaySeconds;
        return this;
    }
}
