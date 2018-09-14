package com.johnli.callback.request;

import com.johnli.callback.common.CallbackException;
import com.johnli.callback.common.Validator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * @author johnli  2018-08-20 10:29
 */
public class UnRegisterRequest implements Serializable, Validator {
    private static final long serialVersionUID = 5825796296905097177L;

    private String uid;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public String getUid() {
        return uid;
    }

    public UnRegisterRequest setUid(String uid) {
        this.uid = uid;
        return this;
    }

    @Override
    public void validate() throws CallbackException {
        if (StringUtils.isBlank(uid)) {
            throw new CallbackException("uid can not be empty");
        }
    }
}
