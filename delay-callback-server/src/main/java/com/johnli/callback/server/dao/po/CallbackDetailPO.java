package com.johnli.callback.server.dao.po;

import com.johnli.callback.common.CallbackException;
import com.johnli.callback.common.Validator;
import com.johnli.callback.param.CallbackParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @author johnli  2018-08-10 10:03
 */
public class CallbackDetailPO implements Serializable, Validator {
    /**
     * not being invoked
     */
    public static final int RETRY_TH_NOT_EXECUTE = -1;
    /**
     * being invoked, not being retried
     */
    public static final int RETRY_TH_EXECUTEED = 0;

    private static final long serialVersionUID = -285316621480278314L;

    private String uid;

    private CallbackParam callbackParam;

    private String group;

    private String alias;

    private Date createTime;

    private Date modifyTime;

    private Date nextExecTime;

    private String dubboContext;

    /**
     * -1：not being invoked
     * 0：invoked
     * >0：retry times
     */
    private int retryTh = RETRY_TH_NOT_EXECUTE;

    /**
     * if been executed
     */
    private boolean executed;

    private String lastError;

    public String getUid() {
        return uid;
    }

    public CallbackDetailPO setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getDubboContext() {
        return dubboContext;
    }

    public CallbackDetailPO setDubboContext(String dubboContext) {
        this.dubboContext = dubboContext;
        return this;
    }

    public Date getNextExecTime() {
        return nextExecTime;
    }

    public CallbackDetailPO setNextExecTime(Date nextExecTime) {
        this.nextExecTime = nextExecTime;
        return this;
    }

    public int getRetryTh() {
        return retryTh;
    }

    public CallbackDetailPO setRetryTh(int retryTh) {
        this.retryTh = retryTh;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public CallbackDetailPO setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public CallbackDetailPO setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }

    public CallbackParam getCallbackParam() {
        return callbackParam;
    }

    public CallbackDetailPO setCallbackParam(CallbackParam callbackParam) {
        this.callbackParam = callbackParam;
        return this;
    }

    public String getGroup() {
        return group;
    }

    public CallbackDetailPO setGroup(String group) {
        this.group = group;
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public CallbackDetailPO setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public String getLastError() {
        return lastError;
    }

    public CallbackDetailPO setLastError(String lastError) {
        this.lastError = lastError;
        return this;
    }

    public boolean isExecuted() {
        return executed;
    }

    public CallbackDetailPO setExecuted(boolean executed) {
        this.executed = executed;
        return this;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    @Override
    public void validate() throws CallbackException {
        if (StringUtils.isBlank(group)) {
            throw new CallbackException("group can not be empty");
        }
        if (StringUtils.isBlank(alias)) {
            throw new CallbackException("alias can not be empty");
        }
        if (StringUtils.isBlank(uid)) {
            throw new CallbackException("uid can not be empty");
        }
        if (createTime == null) {
            throw new CallbackException("createTime can not be null");
        }
        if (modifyTime == null) {
            throw new CallbackException("modifyTime can not be null");
        }
        if (nextExecTime == null) {
            throw new CallbackException("nextExecTime can not be null");
        }
        if (callbackParam == null) {
            throw new CallbackException("callbackParam can not be null");
        }

        callbackParam.validate();
    }
}
