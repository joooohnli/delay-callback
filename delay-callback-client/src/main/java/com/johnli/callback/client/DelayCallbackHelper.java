package com.johnli.callback.client;

import com.johnli.callback.common.CallbackException;
import com.johnli.callback.param.CallbackParam;
import com.johnli.callback.request.RegisterRequest;
import com.johnli.callback.request.UnRegisterRequest;
import com.johnli.callback.result.RegisterResult;
import com.johnli.callback.result.UnRegisterResult;
import com.johnli.callback.client.context.CallbackContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author johnli  2018-08-06 14:48
 */
public class DelayCallbackHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DelayCallbackHelper.class);

    /**
     * @param callbackParam params
     * @param delayCallback implementation of callback logic
     * @return unique id
     */
    public static RegisterResult register(CallbackParam callbackParam, DelayCallback delayCallback) {
        RegisterResult registerResult = new RegisterResult();

        if (delayCallback == null) {
            registerResult.setErrorMsg("delayCallback is null");
            return registerResult;
        }

        RegisterRequest registerRequest = new RegisterRequest();

        try {
            registerRequest.setCallbackParam(callbackParam);
            registerRequest.setGroup(CallbackContextHolder.getGroup());
            registerRequest.setAlias(delayCallback.alias());

            registerRequest.validate();

            if (!CallbackContextHolder.getDelayCallbackMap().containsKey(delayCallback.alias())) {
                throw new CallbackException("delayCallback.alias has not been inited");
            }

            LOGGER.info("start to register callback,alias:{},args:{},delaySeconds:{}",
                    registerRequest.getAlias(),
                    registerRequest.getCallbackParam().getArgs(),
                    registerRequest.getCallbackParam().getDelaySeconds());

            return CallbackContextHolder.getDelayCallbackServerFacade().register(registerRequest);
        } catch (Exception e) {
            LOGGER.error("register delay callback error,registerRequest:{}", registerRequest, e);

            registerResult.setErrorMsg(e.getMessage());
            return registerResult;
        }
    }

    /**
     * cancel registration
     *
     * @param uid unique id
     * @return
     */
    public static UnRegisterResult unRegister(String uid) {
        UnRegisterResult unRegisterResult = new UnRegisterResult();
        if (StringUtils.isBlank(uid)) {
            unRegisterResult.setErrorMsg("uid is empty");
            return unRegisterResult;
        }

        LOGGER.info("start to unRegister callback,uid:{}", uid);

        try {
            UnRegisterRequest unRegisterRequest = new UnRegisterRequest();
            unRegisterRequest.setUid(uid);
            return CallbackContextHolder.getDelayCallbackServerFacade().unRegister(unRegisterRequest);
        } catch (Exception e) {
            LOGGER.error("unRegister delay callback error,uid:{}", uid, e);

            unRegisterResult.setErrorMsg(e.getMessage());
            return unRegisterResult;
        }
    }


}
