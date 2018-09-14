package com.johnli.callback.client.facade.impl;

import com.johnli.callback.client.DelayCallback;
import com.johnli.callback.common.CallbackException;
import com.johnli.callback.facade.DelayCallbackClientFacade;
import com.johnli.callback.param.ErrorCode;
import com.johnli.callback.request.CallbackRequest;
import com.johnli.callback.result.CallbackResult;
import com.johnli.callback.client.context.CallbackContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import static com.johnli.callback.param.ErrorCodeEnum.CALLBACK_BIZ_EXCEPTION;
import static com.johnli.callback.param.ErrorCodeEnum.CALLBACK_BIZ_FAILED;


/**
 * @author johnli  2018-08-07 17:21
 */
public class DelayCallbackClientFacadeImpl implements DelayCallbackClientFacade {
    private static final Logger LOGGER = LoggerFactory.getLogger(DelayCallbackClientFacadeImpl.class);

    @Override
    public CallbackResult callback(CallbackRequest callbackRequest) {
        CallbackResult callbackResult = new CallbackResult();
        try {
            if (callbackRequest == null) {
                throw new CallbackException("callbackRequest is null");
            }
            callbackRequest.validate();

            HashMap<String, DelayCallback> delayCallbackMap = CallbackContextHolder.getDelayCallbackMap();
            DelayCallback delayCallback = delayCallbackMap.get(callbackRequest.getAlias());
            if (delayCallback == null) {
                LOGGER.error("callback error,can not find uid:{},alias:{}", callbackRequest.getUid(), callbackRequest.getAlias());
                callbackResult.setErrorCode(new ErrorCode(CALLBACK_BIZ_EXCEPTION).setMsg("alias not exists"));
                return callbackResult;
            }

            LOGGER.info("start to execute callback,uid={},alias={}", callbackRequest.getUid(), callbackRequest.getAlias());

            boolean success = delayCallback.onCallback(callbackRequest);

            if (!success) {
                callbackResult.setErrorCode(new ErrorCode(CALLBACK_BIZ_FAILED));
                return callbackResult;
            }

            callbackResult.setSuccess(true);
            return callbackResult;
        } catch (Exception e) {
            LOGGER.error("execute callback error,request:{}", callbackRequest, e);
            callbackResult.setErrorCode(new ErrorCode(CALLBACK_BIZ_EXCEPTION));
            return callbackResult;
        }
    }
}
