package com.johnli.callback.server.template;

import com.johnli.callback.common.CallbackException;
import com.johnli.callback.server.constant.LogConstant;
import com.johnli.callback.server.context.ContextHolder;
import com.johnli.callback.server.context.SysContext;
import com.johnli.callback.server.context.biz.BizContext;
import com.johnli.callback.server.log.digest.DigestLogInfo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author johnli  2018-08-13 18:08
 */
public abstract class AbstractExecutor<REQ, RET> implements Executor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractExecutor.class);
    private static final Logger DIGEST_LOGGER = LoggerFactory.getLogger(LogConstant.DIGEST_LOGGER);


    protected REQ request;
    protected RET result;

    public AbstractExecutor() {
    }

    public AbstractExecutor(REQ request, RET result) {
        this.request = request;
        this.result = result;
    }

    @Override
    public void before() {
        if (ContextHolder.getSysContext() == null) {
            // inherited from parent thread
            //todo central time
            SysContext sysContext = new SysContext();
            sysContext.setCurrentTime(new Date());
            JedisPool jedisPool = ContextHolder.getApplicationContext().getBean(JedisPool.class);
            if (jedisPool == null) {
                throw new CallbackException("jedisPool is null");
            }
            sysContext.setJedisPool(jedisPool);

            ContextHolder.setCallbackContext(sysContext);
        }

        if (ContextHolder.getBizContext() == null) {
            ContextHolder.setBizContext(initBizContext());
        }
    }

    @Override
    public void dealException(Exception e) {
        LOGGER.error("AbstractExecutor exception", e);
    }

    @Override
    public void after() {
        try {
            List<DigestLogInfo> logInfos = composeDigestLogs();
            if (CollectionUtils.isNotEmpty(logInfos)) {
                Date now = new Date();
                for (DigestLogInfo digestLogInfo : logInfos) {
                    if (digestLogInfo != null) {
                        digestLogInfo.setCost(now.getTime() - ContextHolder.getSysContext().getCurrentTime().getTime());
                        digest(digestLogInfo);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("digest log error", e);
        }

        ContextHolder.removeContext();
    }

    protected BizContext initBizContext() {
        return new BizContext();
    }

    public DigestLogInfo composeDigestLog() {
        return null;
    }

    public List<DigestLogInfo> composeDigestLogs() {
        if (composeDigestLog() == null) {
            return null;
        }
        List<DigestLogInfo> logInfos = new ArrayList<>();
        logInfos.add(composeDigestLog());
        return logInfos;
    }


    private void digest(DigestLogInfo logInfo) {
        // can collect log by filebeat
        DIGEST_LOGGER.info(logInfo.toString());

        // may also report log here
    }
}
