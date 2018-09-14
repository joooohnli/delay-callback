package com.johnli.callback.server.dao.po;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @author johnli  2018-08-10 15:21
 */
public class LockPO {
    private String uid;
    private String identifier;
    private long acquireTimeout;
    private long lockExpireTime;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public String getUid() {
        return uid;
    }

    public LockPO setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getIdentifier() {
        return identifier;
    }

    public LockPO setIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public long getAcquireTimeout() {
        return acquireTimeout;
    }

    public LockPO setAcquireTimeout(long acquireTimeout) {
        this.acquireTimeout = acquireTimeout;
        return this;
    }

    public long getLockExpireTime() {
        return lockExpireTime;
    }

    public LockPO setLockExpireTime(long lockExpireTime) {
        this.lockExpireTime = lockExpireTime;
        return this;
    }
}
