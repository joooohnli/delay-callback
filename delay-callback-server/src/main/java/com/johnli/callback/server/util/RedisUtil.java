package com.johnli.callback.server.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.UUID;

/**
 * @author johnli  2018-08-10 10:09
 */
public class RedisUtil {
    /**
     * @param conn
     * @param lockKey
     * @param acquireTimeout <=0:none block. unit:ms
     * @param lockExpireTime unit:ms
     * @return
     */
    public static String acquireLock(Jedis conn, String lockKey, long acquireTimeout, long lockExpireTime) {
        String identifier = UUID.randomUUID().toString();
        // none block
        if (acquireTimeout <= 0) {
            if ("OK".equalsIgnoreCase(conn.set(lockKey, identifier, "NX", "PX", lockExpireTime))) {
                return identifier;
            }
            if (conn.ttl(lockKey) == -1) {
                conn.expire(lockKey, (int) lockExpireTime / 1000);
            }
            return null;
        }
        long end = System.currentTimeMillis() + acquireTimeout;
        while (System.currentTimeMillis() < end) {
            if ("OK".equalsIgnoreCase(conn.set(lockKey, identifier, "NX", "PX", lockExpireTime))) {
                return identifier;
            }
            if (conn.ttl(lockKey) == -1) {
                conn.expire(lockKey, (int) lockExpireTime / 1000);
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        // null indicates that the lock was not acquired
        return null;
    }

    public static boolean releaseLock(Jedis conn, String lockKey, String identifier) {
        while (true) {
            conn.watch(lockKey);
            //check current thread still holds lock
            if (identifier.equals(conn.get(lockKey))) {
                Transaction trans = conn.multi();
                // release lock
                trans.del(lockKey);
                List<Object> results = trans.exec();
                if (results == null|| results.size()==0) {
                    continue;
                }
                return true;
            }
            conn.unwatch();
            break;
        }
        return false;
    }

}
