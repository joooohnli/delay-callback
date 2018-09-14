package com.johnli.callback.server.dao.po;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @author johnli  2018-08-10 17:35
 */
public class CallbackIdPO implements Serializable{
    private static final long serialVersionUID = 3450826657265607041L;

    private  String uid;

    private Date nextExecTime;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public String getUid() {
        return uid;
    }

    public CallbackIdPO setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public Date getNextExecTime() {
        return nextExecTime;
    }

    public CallbackIdPO setNextExecTime(Date nextExecTime) {
        this.nextExecTime = nextExecTime;
        return this;
    }
}
