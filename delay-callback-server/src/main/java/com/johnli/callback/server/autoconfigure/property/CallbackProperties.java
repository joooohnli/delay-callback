package com.johnli.callback.server.autoconfigure.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author johnli  2018-08-13 14:59
 */
@ConfigurationProperties(prefix = "callback")
public class CallbackProperties {
    private int maxBatchCount = 1000;
    private int lockTimeoutSec = 5;
    private JedisProperties jedis;
    private boolean canRunJob = true;
    private boolean leaderMode = true;
    private AsyncProperties scanPool;
    private AsyncProperties compensationPool;
    private AsyncProperties monitorPool;

    public boolean isLeaderMode() {
        return leaderMode;
    }

    public CallbackProperties setLeaderMode(boolean leaderMode) {
        this.leaderMode = leaderMode;
        return this;
    }

    public int getMaxBatchCount() {
        return maxBatchCount;
    }

    public CallbackProperties setMaxBatchCount(int maxBatchCount) {
        this.maxBatchCount = maxBatchCount;
        return this;
    }

    public boolean isCanRunJob() {
        return canRunJob;
    }

    public CallbackProperties setCanRunJob(boolean canRunJob) {
        this.canRunJob = canRunJob;
        return this;
    }

    public int getLockTimeoutSec() {
        return lockTimeoutSec;
    }

    public CallbackProperties setLockTimeoutSec(int lockTimeoutSec) {
        this.lockTimeoutSec = lockTimeoutSec;
        return this;
    }

    public JedisProperties getJedis() {
        return jedis;
    }

    public CallbackProperties setJedis(JedisProperties jedis) {
        this.jedis = jedis;
        return this;
    }

    public AsyncProperties getScanPool() {
        return scanPool;
    }

    public CallbackProperties setScanPool(AsyncProperties scanPool) {
        this.scanPool = scanPool;
        return this;
    }

    public AsyncProperties getCompensationPool() {
        return compensationPool;
    }

    public CallbackProperties setCompensationPool(AsyncProperties compensationPool) {
        this.compensationPool = compensationPool;
        return this;
    }

    public AsyncProperties getMonitorPool() {
        return monitorPool;
    }

    public CallbackProperties setMonitorPool(AsyncProperties monitorPool) {
        this.monitorPool = monitorPool;
        return this;
    }
}
