package com.johnli.callback.server.log.digest;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @author johnli  2018-08-17 17:25
 */
public abstract class DigestLogInfo {

    private String biz;

    private String group;
    // ms
    private long cost;

    private String traceId;

    public DigestLogInfo() {
        // may inject traceId here
    }

    public String getTraceId() {
        return traceId;
    }


    public String getGroup() {
        return group;
    }

    public DigestLogInfo setGroup(String group) {
        this.group = group;
        return this;
    }

    public String getBiz() {
        return biz;
    }

    public DigestLogInfo setBiz(String biz) {
        this.biz = biz;
        return this;
    }

    public long getCost() {
        return cost;
    }

    /**
     * set by framework automatically
     *
     * @param cost
     * @return
     */
    public DigestLogInfo setCost(long cost) {
        this.cost = cost;
        return this;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
