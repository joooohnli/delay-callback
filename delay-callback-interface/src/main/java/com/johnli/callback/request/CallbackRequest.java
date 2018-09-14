package com.johnli.callback.request;

import com.johnli.callback.common.CallbackException;
import com.johnli.callback.common.Validator;
import com.johnli.callback.param.CallbackParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * @author johnli  2018-08-08 10:14
 */
public class CallbackRequest implements Serializable, Validator {
    private static final long serialVersionUID = -8945707933369604900L;

    private CallbackParam callbackParam;

    private String alias;

    /**
     * 唯一id
     */
    private String uid;
    /**
     * 0.表示正常回调
     * >0.表示第n次重试
     */
    private int retryTh;

    @Override
    public void validate() throws CallbackException {
        if (callbackParam == null) {
            throw new CallbackException("callbackParam can not be null");
        }
        if (StringUtils.isBlank(alias)) {
            throw new CallbackException("alias can not be empty");
        }
        if (StringUtils.isBlank(uid)) {
            throw new CallbackException("uid can not be empty");
        }
        callbackParam.validate();
    }

    public String getUid() {
        return uid;
    }

    public CallbackRequest setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public int getRetryTh() {
        return retryTh;
    }

    public CallbackRequest setRetryTh(int retryTh) {
        this.retryTh = retryTh;
        return this;
    }

    public CallbackParam getCallbackParam() {
        return callbackParam;
    }

    public CallbackRequest setCallbackParam(CallbackParam callbackParam) {
        this.callbackParam = callbackParam;
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public CallbackRequest setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
