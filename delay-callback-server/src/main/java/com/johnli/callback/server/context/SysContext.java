package com.johnli.callback.server.context;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Date;

/**
 * @author johnli  2018-08-14 10:42
 */
public class SysContext implements Cloneable {

    private Date currentTime;

    private JedisPool jedisPool;

    private Jedis jedis;

    public synchronized void remove() {
        if (jedis != null) {
            jedis.close();
        }
    }


    public SysContext setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
        return this;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public SysContext setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
        return this;
    }

    public Jedis getJedis() {
        if (jedis != null) {
            return jedis;
        }
        synchronized (this) {
            if (jedis == null) {
                jedis = jedisPool.getResource();
            }
        }
        return jedis;
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        SysContext sysContext = new SysContext();
        sysContext.setCurrentTime(this.currentTime);
        sysContext.setJedisPool(this.jedisPool);
        return sysContext;
    }
}
