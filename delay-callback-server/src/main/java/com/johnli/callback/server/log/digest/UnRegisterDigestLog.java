package com.johnli.callback.server.log.digest;

/**
 * @author johnli  2018-08-30 17:08
 */
public class UnRegisterDigestLog extends DigestLogInfo {
    private String uid;
    private String errorMsg;
    private String alias;
    private boolean success;

    public String getUid() {
        return uid;
    }

    public UnRegisterDigestLog setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public UnRegisterDigestLog setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public UnRegisterDigestLog setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public UnRegisterDigestLog setSuccess(boolean success) {
        this.success = success;
        return this;
    }

}
