package com.johnli.callback.server.log.digest;

/**
 * @author johnli  2018-08-30 18:48
 */
public class MonitorCountDigestLog extends DigestLogInfo {
    private int count;
    private int failedCount;

    public int getFailedCount() {
        return failedCount;
    }

    public MonitorCountDigestLog setFailedCount(int failedCount) {
        this.failedCount = failedCount;
        return this;
    }

    public int getCount() {
        return count;
    }

    public MonitorCountDigestLog setCount(int count) {
        this.count = count;
        return this;
    }
}
