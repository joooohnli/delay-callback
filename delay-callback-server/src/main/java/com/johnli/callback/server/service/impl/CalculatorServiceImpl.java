package com.johnli.callback.server.service.impl;

import com.johnli.callback.common.CallbackException;
import com.johnli.callback.param.RetryStrategy;
import com.johnli.callback.server.dao.po.CallbackDetailPO;
import com.johnli.callback.server.service.CalculatorService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author johnli  2018-08-29 16:13
 */
@Service
public class CalculatorServiceImpl implements CalculatorService {

    private static final int DEFAULT_RETRY_INTERVAL_SEC = 30;

    @Override
    public boolean canRetry(CallbackDetailPO detailPO) {
        if (detailPO.getRetryTh() == CallbackDetailPO.RETRY_TH_NOT_EXECUTE) {
            return true;
        }

        return detailPO.getCallbackParam().getRetryStrategy().canRetry(detailPO.getRetryTh());
    }

    @Override
    public Date calculateNextExecTime(Date exeTime, CallbackDetailPO detailPO) {
        if (detailPO.getRetryTh() < CallbackDetailPO.RETRY_TH_NOT_EXECUTE) {
            throw new CallbackException("detailPO.getRetryTh can not < " + CallbackDetailPO.RETRY_TH_NOT_EXECUTE);
        }

        if (detailPO.getRetryTh() == CallbackDetailPO.RETRY_TH_NOT_EXECUTE) {
            // never being executed
            if (!detailPO.isExecuted()) {
                return DateUtils.addSeconds(exeTime, detailPO.getCallbackParam().getDelaySeconds());
            }
            // biz set no retry
            if (detailPO.getCallbackParam().getRetryStrategy().getTimes() == RetryStrategy.TIMES_NO_RETRY) {
                return DateUtils.addSeconds(exeTime, DEFAULT_RETRY_INTERVAL_SEC);
            }
            // first retry
            return detailPO.getCallbackParam().getRetryStrategy().calculateNextRetryTime(exeTime, 0);
        }

        // N th retry
        return detailPO.getCallbackParam().getRetryStrategy().calculateNextRetryTime(exeTime, detailPO.getRetryTh());
    }
}
