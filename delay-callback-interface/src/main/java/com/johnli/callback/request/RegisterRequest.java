package com.johnli.callback.request;

import com.johnli.callback.common.CallbackException;
import com.johnli.callback.common.Validator;
import com.johnli.callback.param.CallbackParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * @author johnli  2018-08-07 17:19
 */
public class RegisterRequest implements Serializable, Validator {

    private static final long serialVersionUID = 7598049553897088159L;

    private CallbackParam callbackParam;

    private String group;

    private String alias;

    @Override
    public void validate() throws CallbackException {
        if (StringUtils.isBlank(group)) {
            throw new CallbackException("group can not be empty");
        }
        if (StringUtils.isBlank(alias)) {
            throw new CallbackException("alias can not be empty");
        }
        if (callbackParam == null) {
            throw new CallbackException("callbackParam can not be null");
        }

        callbackParam.validate();

    }

    public CallbackParam getCallbackParam() {
        return callbackParam;
    }

    public RegisterRequest setCallbackParam(CallbackParam callbackParam) {
        this.callbackParam = callbackParam;
        return this;
    }

    public String getGroup() {
        return group;
    }

    public RegisterRequest setGroup(String group) {
        this.group = group;
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public RegisterRequest setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
