package com.johnli.callback.server.dao.impl;

import com.alibaba.fastjson.JSON;
import com.johnli.callback.server.context.ContextHolder;
import com.johnli.callback.server.dao.DataDao;
import com.johnli.callback.server.dao.po.CallbackDetailPO;
import com.johnli.callback.server.dao.po.CallbackIdPO;
import com.johnli.callback.server.dao.po.LockPO;
import com.johnli.callback.server.dao.po.PageResult;
import com.johnli.callback.server.util.RedisUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.*;

import java.util.*;

/**
 * @author johnli  2018-08-10 12:03
 */
@Repository
public class DataDaoImpl implements DataDao {
    private static final String KEY_TASK_ID = "delay:callback:id";
    private static final String KEY_TASK_DETAIL = "delay:callback:detail";
    private static final String KEY_TASK_COUNT_PREFIX = "delay:callback:count:";
    private static final String KEY_FAILURE_PREFIX = "delay:callback:failure:";
    private static final String LOCK_PREFIX = "delay:callback:lock:";

    @Override
    public List<CallbackIdPO> listIdsByRange(Date start, Date end, boolean desc, int offset, int count) {
        Jedis jedis = ContextHolder.getSysContext().getJedis();
        long startTime = start == null ? 0 : start.getTime();
        long endTime = end == null ? 0 : end.getTime();

        Set<Tuple> exec;
        if (desc) {
            exec = jedis.zrevrangeByScoreWithScores(KEY_TASK_ID, endTime, startTime, offset, count);
        } else {
            exec = jedis.zrangeByScoreWithScores(KEY_TASK_ID, startTime, endTime, offset, count);
        }
        if (exec == null) {
            return null;
        }
        List<CallbackIdPO> pos = new ArrayList<>();
        for (Tuple tuple : exec) {
            CallbackIdPO po = new CallbackIdPO();
            po.setUid(tuple.getElement());
            po.setNextExecTime(new Date((long) tuple.getScore()));
            pos.add(po);
        }
        return pos;
    }

    @Override
    public boolean add(CallbackDetailPO po) {
        Jedis jedis = ContextHolder.getSysContext().getJedis();

        Transaction multi = jedis.multi();
        multi.zadd(KEY_TASK_ID, po.getNextExecTime().getTime(), po.getUid());
        multi.hset(KEY_TASK_DETAIL, po.getUid(), JSON.toJSONString(po));
        multi.incr(getCountKey(po.getGroup()));
        List<Object> exec = multi.exec();


        if (exec == null) {
            return false;
        }

        for (Object o : exec) {
            long ret = (long) o;
            if (ret <= 0) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean markFailure(CallbackDetailPO po) {
        Jedis jedis = ContextHolder.getSysContext().getJedis();

        //todo optimize: use lua
//        ScanParams scanParams = new ScanParams();
//        scanParams.match(po.getUid());
//        ScanResult<Tuple> result = jedis.zscan(KEY_TASK_ID, "0", scanParams);
//
//        if (result.getResult().size() <= 0) {
//            return false;
//        }

        Transaction multi = jedis.multi();
        multi.zrem(KEY_TASK_ID, po.getUid());
        multi.hdel(KEY_TASK_DETAIL, po.getUid());
        multi.decr(getCountKey(po.getGroup()));
        multi.hset(getFailureKey(po.getGroup()), po.getUid(), JSON.toJSONString(po));
        List<Object> exec = multi.exec();

        if (exec == null) {
            return false;
        }

        return true;
    }

    @Override
    public List<String> listGroup() {
        Jedis jedis = ContextHolder.getSysContext().getJedis();
        Set<String> keys = jedis.keys(KEY_TASK_COUNT_PREFIX + "*");

        List<String> groups = new ArrayList<>();
        if (CollectionUtils.isEmpty(keys)) {
            return groups;
        }

        for (String key : keys) {
            groups.add(getGroupFromCountKey(key));
        }
        return groups;
    }

    @Override
    public int count(String group) {
        Jedis jedis = ContextHolder.getSysContext().getJedis();
        String count = jedis.get(getCountKey(group));
        return Integer.valueOf(count);
    }

    @Override
    public int countFailure(String group) {
        Jedis jedis = ContextHolder.getSysContext().getJedis();
        Long count = jedis.hlen(getFailureKey(group));
        return Math.toIntExact(count);
    }

    @Override
    public boolean delete(String group, String uid) {
        Jedis jedis = ContextHolder.getSysContext().getJedis();

        //todo optimize: use lua
//        ScanParams scanParams = new ScanParams();
//        scanParams.match(uid);
//        ScanResult<Tuple> result = jedis.zscan(KEY_TASK_ID, "0", scanParams);
//
//        if (result.getResult().size() <= 0) {
//            return false;
//        }

        Transaction multi = jedis.multi();
        multi.zrem(KEY_TASK_ID, uid);
        multi.hdel(KEY_TASK_DETAIL, uid);
        multi.decr(getCountKey(group));
        List<Object> exec = multi.exec();


        // todo result
        if (exec == null) {
            return false;
        }

        return true;
    }

    @Override
    public boolean update(CallbackDetailPO po) {
        Jedis jedis = ContextHolder.getSysContext().getJedis();
        Transaction multi = jedis.multi();
        multi.zrem(KEY_TASK_ID, po.getUid());
        multi.zadd(KEY_TASK_ID, po.getNextExecTime().getTime(), po.getUid());
        multi.hset(KEY_TASK_DETAIL, po.getUid(), JSON.toJSONString(po));
        List<Object> exec = multi.exec();

        if (exec == null) {
            return false;
        }

        return true;
    }

    @Override
    public PageResult<CallbackDetailPO> listFailures(String group, int offset, int count) {
        Jedis jedis = ContextHolder.getSysContext().getJedis();
        ScanResult<Map.Entry<String, String>> scanResult = jedis.hscan(getFailureKey(group), String.valueOf(offset), new ScanParams().count(count));

        PageResult<CallbackDetailPO> callbackDetailPOPageResult = new PageResult<>();
//       scan complete while scanResult.getStringCursor() == 0
        callbackDetailPOPageResult.setCursor(Integer.valueOf(scanResult.getStringCursor()));

        List<CallbackDetailPO> callbackDetailPOS = new ArrayList<>();
        List<Map.Entry<String, String>> result = scanResult.getResult();
        if (CollectionUtils.isNotEmpty(result)) {
            for (Map.Entry<String, String> entry : result) {
                callbackDetailPOS.add(JSON.parseObject(entry.getValue(), CallbackDetailPO.class));
            }
        }
        callbackDetailPOPageResult.setResults(callbackDetailPOS);

        return callbackDetailPOPageResult;
    }

    @Override
    public boolean deleteFailure(String group, String uid) {
        Jedis jedis = ContextHolder.getSysContext().getJedis();
        Long hdel = jedis.hdel(getFailureKey(group), uid);
        return hdel > 0;
    }

    @Override
    public CallbackDetailPO getFailure(String group, String uid) {
        Jedis jedis = ContextHolder.getSysContext().getJedis();
        String hget = jedis.hget(getFailureKey(group), uid);
        if (hget == null) {
            return null;
        }

        return JSON.parseObject(hget, CallbackDetailPO.class);
    }

    @Override
    public CallbackDetailPO get(String uid) {
        Jedis jedis = ContextHolder.getSysContext().getJedis();
        String hget = jedis.hget(KEY_TASK_DETAIL, uid);
        if (hget == null) {
            return null;
        }

        return JSON.parseObject(hget, CallbackDetailPO.class);
    }

    @Override
    public LockPO acquireLock(String uid, long acquireTimeout, long lockExpireTime) {
        Jedis jedis = ContextHolder.getSysContext().getJedis();
        String identifier = RedisUtil.acquireLock(jedis, getLockKey(uid), acquireTimeout, lockExpireTime);
        if (identifier == null) {
            return null;
        }
        LockPO lockPO = new LockPO();
        lockPO.setIdentifier(identifier);
        lockPO.setUid(uid);
        lockPO.setAcquireTimeout(acquireTimeout);
        lockPO.setLockExpireTime(lockExpireTime);

        return lockPO;
    }

    @Override
    public boolean releaseLock(LockPO lockPO) {
        Jedis jedis = ContextHolder.getSysContext().getJedis();
        return RedisUtil.releaseLock(jedis, getLockKey(lockPO.getUid()), lockPO.getIdentifier());
    }

    private static String getCountKey(String group) {
        return KEY_TASK_COUNT_PREFIX + group;
    }

    private static String getGroupFromCountKey(String countKey) {
        return StringUtils.substringAfterLast(countKey, KEY_TASK_COUNT_PREFIX);
    }

    private static String getFailureKey(String group) {
        return KEY_FAILURE_PREFIX + group;
    }

    private static String getLockKey(String uid) {
        return LOCK_PREFIX + uid;
    }
}
