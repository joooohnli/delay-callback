package com.johnli.callback.server.service.impl;

import com.johnli.callback.param.ErrorCode;
import com.johnli.callback.param.ErrorCodeEnum;
import com.johnli.callback.request.CallbackRequest;
import com.johnli.callback.result.CallbackResult;
import com.johnli.callback.server.context.ContextHolder;
import com.johnli.callback.server.dao.po.CallbackDetailPO;
import com.johnli.callback.server.service.CalculatorService;
import com.johnli.callback.server.service.CallbackService;
import com.johnli.callback.server.service.DataService;
import com.johnli.callback.server.util.DataConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.johnli.callback.param.ErrorCodeEnum.CALLBACK_EXCEPTION;
import static com.johnli.callback.param.ErrorCodeEnum.DB_EXCEPTION;


/**
 * @author johnli  2018-08-09 17:04
 */
@Service
public class CallbackServiceImpl implements CallbackService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CallbackServiceImpl.class);

    @Autowired
    private DataService dataService;

    @Autowired
    private CalculatorService calculatorService;

    @Override
    public CallbackResult callback(CallbackDetailPO callbackDetailPO) {

        callbackDetailPO.setRetryTh(callbackDetailPO.getRetryTh() + 1);

        CallbackResult callbackResult;
        try {
            callbackResult = doRemoteCallback(callbackDetailPO);
        } catch (Exception e) {
            LOGGER.error("invoke remote callback error", e);
            callbackResult = new CallbackResult().setErrorCode(new ErrorCode(CALLBACK_EXCEPTION).setMsg(e.getMessage()));
        }

        callbackDetailPO.setExecuted(true);

        if (callbackResult.isSuccess()) {
            boolean delete = dataService.delete(callbackDetailPO.getUid());

            if (!delete) {
                callbackResult.setErrorCode(new ErrorCode(DB_EXCEPTION).setMsg("can not mark success"));
                LOGGER.error("{},po:{}", callbackResult.getErrorCode().getMsg(), callbackDetailPO);
                return callbackResult;
            }

        } else {

            // system error will not count retry times.
            if (!(callbackResult.getErrorCode().getCode() == ErrorCodeEnum.CALLBACK_BIZ_FAILED.getCode()
                    || callbackResult.getErrorCode().getCode() == ErrorCodeEnum.CALLBACK_BIZ_EXCEPTION.getCode())) {
                callbackDetailPO.setRetryTh(callbackDetailPO.getRetryTh() - 1);
            }

            if (calculatorService.canRetry(callbackDetailPO)) {
                Date nextExecTime = calculatorService.calculateNextExecTime(ContextHolder.getSysContext().getCurrentTime(), callbackDetailPO);
                callbackDetailPO.setNextExecTime(nextExecTime);


                callbackDetailPO.setLastError(callbackResult.getErrorCode().getMsg());
                boolean update = dataService.update(callbackDetailPO);
                if (!update) {
                    callbackResult.setErrorCode(new ErrorCode(DB_EXCEPTION).setMsg("can not update record"));
                    LOGGER.error("{},po:{}", callbackResult.getErrorCode().getMsg(), callbackDetailPO);
                    return callbackResult;
                }

            } else {
                boolean markFailure = dataService.markFailure(callbackDetailPO);
                if (!markFailure) {
                    callbackResult.setErrorCode(new ErrorCode(DB_EXCEPTION).setMsg("can not mark failure"));
                    LOGGER.error("{},po:{}", callbackResult.getErrorCode().getMsg(), callbackDetailPO);
                    return callbackResult;
                }
            }
        }

        return callbackResult;
    }


    private CallbackResult doRemoteCallback(CallbackDetailPO detailPO) {
        CallbackRequest callbackRequest = DataConverter.convert2CallbackRequest(detailPO);

        // may inject dubbo context here


        return CallbackClientFactory.getCallbackClientFacade(detailPO.getGroup()).callback(callbackRequest);
    }
}
