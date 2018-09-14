package com.johnli.callback.server.autoconfigure.property;

/**
 * @author johnli  2018-08-09 18:51
 */
public class JedisProperties {
    private String host;

    private int port;

    private int maxTotal;

    private int maxIdle;

    private int maxWaitMillis;

    private int timeout;

    private String password;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(int maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public int getTimeout() {
        return timeout;
    }

    public JedisProperties setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public JedisProperties setPassword(String password) {
        this.password = password;
        return this;
    }
}

