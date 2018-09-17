package com.johnli.callback.param;

import com.johnli.callback.common.CallbackException;
import com.johnli.callback.common.Validator;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @author johnli  2018-08-07 15:44
 */
public class RetryStrategy implements Serializable, Validator {

    private static final long serialVersionUID = 7841902113390507815L;

    public static final int TIMES_DEFAULT = 10;
    public static final int TIMES_NO_RETRY = 0;
    public static final int TYPE_DEFAULT = 3;
    public static final int INTERVAL_DEFAULT = 10;
    public static final int MAX_INTERVAL_DEFAULT = 86400;

    /**
     * retry times
     * <p> <0.illegal
     * <p> =0.no retry
     * <p> >0.n times
     */
    private int times = TIMES_DEFAULT;
    /**
     * retry types
     * <p>base on interval
     * <p>1.fixed interval
     * <p>2.grow in multiples of interval[interval, 2*interval, 3*interval, ..., n*interval]
     * <p>3.grow exponentially with interval[interval, 2^1*interval, 2^2*interval, ... , 2^n*interval]
     */
    private int type = TYPE_DEFAULT;
    /**
     * retry interval
     * <p>unit：second
     */
    private int interval = INTERVAL_DEFAULT;
    /**
     * max retry interval
     * <p>unit：second
     */
    private int maxInterval = MAX_INTERVAL_DEFAULT;

    public RetryStrategy() {
    }


    /**
     * @param triedTh
     * @return
     */
    public boolean canRetry(int triedTh) {
        if (times == TIMES_NO_RETRY) {
            return false;
        }
        return triedTh < times;
    }

    /**
     * @param lastExecuteTime
     * @param triedTh
     * @return
     */
    public Date calculateNextRetryTime(Date lastExecuteTime, int triedTh) {
        if (triedTh < 0) {
            throw new CallbackException("calculateNextRetryTime triedTh must >=0");
        }
        if (type == 1) {
            return new Date(lastExecuteTime.getTime() + interval * 1000);
        } else if (type == 2) {
            int currInterval = (triedTh + 1) * interval;
            currInterval = currInterval < maxInterval ? currInterval : maxInterval;
            return new Date(lastExecuteTime.getTime() + currInterval * 1000);
        } else if (type == 3) {
            int currInterval = (int) (Math.pow(2, triedTh) * interval);
            currInterval = currInterval < maxInterval ? currInterval : maxInterval;
            return new Date(lastExecuteTime.getTime() + currInterval * 1000);
        } else {
            throw new CallbackException("unsupported retry type " + type);
        }
    }


    public int getTimes() {
        return times;
    }

    public RetryStrategy setTimes(int times) {
        this.times = times;
        return this;
    }

    public int getType() {
        return type;
    }

    public RetryStrategy setType(int type) {
        this.type = type;
        return this;
    }

    public int getInterval() {
        return interval;
    }

    public RetryStrategy setInterval(int interval) {
        this.interval = interval;
        return this;
    }

    public int getMaxInterval() {
        return maxInterval;
    }

    public RetryStrategy setMaxInterval(int maxInterval) {
        this.maxInterval = maxInterval;
        return this;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    @Override
    public void validate() throws CallbackException {
        if (times < 0) {
            throw new CallbackException("time must >= 0");
        }
        if (interval <= 0) {
            throw new CallbackException("interval must greater than 0");
        }
        if (type < 1 || type > 3) {
            throw new CallbackException("type must be in [1,2,3]");
        }
        if (maxInterval <= 0) {
            throw new CallbackException("maxInterval must greater than 0");
        }
        if (maxInterval < interval) {
            throw new CallbackException("maxInterval must greater than interval");
        }
    }

    public static void main(String[] args) {
        RetryStrategy retryStrategy = new RetryStrategy().setType(2);
        Date now = new Date();
        Date nextRetryTime = retryStrategy.calculateNextRetryTime(now, 0);
        System.out.println(now);
        System.out.println(nextRetryTime);
    }
}
