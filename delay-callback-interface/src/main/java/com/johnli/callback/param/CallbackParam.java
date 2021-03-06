package com.johnli.callback.param;

import com.johnli.callback.common.CallbackException;
import com.johnli.callback.common.Validator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * @author johnli  2018-08-06 13:52
 */
public class CallbackParam implements Serializable, Validator {
    private static final long serialVersionUID = -6562599855623010686L;
    /**
     * arguments list
     */
    private List<String> args;
    /**
     * seconds to delay
     */
    private int delaySeconds;
    /**
     * retry strategy
     */
    private RetryStrategy retryStrategy;
    /**
     * idempotent key<p></p>
     * if this value is not empty, use it to ensure the registration is idempotent when same args under same alias are registered more than once;
     * otherwise, will register callbacks for times.
     */
    private String idempotentKey;


    @Override
    public void validate() throws CallbackException {
        if (delaySeconds <= 0) {
            throw new CallbackException("delaySeconds can not less than 1");
        }
        if (retryStrategy == null) {
            throw new CallbackException("retryStrategy can not be null");
        }

        retryStrategy.validate();
    }

    private CallbackParam() {
    }


    public CallbackParam(List<String> args, int delaySeconds, String idempotentKey, RetryStrategy retryStrategy) {
        this.args = args;
        this.delaySeconds = delaySeconds;
        this.retryStrategy = retryStrategy;
        this.idempotentKey = idempotentKey;
    }

    public CallbackParam(List<String> args, int delaySeconds, String idempotentKey) {
        this(args, delaySeconds, idempotentKey, new RetryStrategy());
    }

    public CallbackParam(List<String> args, int delaySeconds) {
        this(args, delaySeconds, null);
    }

    public boolean needIdempotent() {
        return StringUtils.isNoneEmpty(idempotentKey);
    }

    public List<String> getArgs() {
        return args;
    }

    public CallbackParam setArgs(List<String> args) {
        this.args = args;
        return this;
    }

    public int getDelaySeconds() {
        return delaySeconds;
    }

    public CallbackParam setDelaySeconds(int delaySeconds) {
        this.delaySeconds = delaySeconds;
        return this;
    }

    public RetryStrategy getRetryStrategy() {
        return retryStrategy;
    }

    public CallbackParam setRetryStrategy(RetryStrategy retryStrategy) {
        this.retryStrategy = retryStrategy;
        return this;
    }

    public String getIdempotentKey() {
        return idempotentKey;
    }

    public CallbackParam setIdempotentKey(String idempotentKey) {
        this.idempotentKey = idempotentKey;
        return this;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
