package com.johnli.callback.server.service.impl;

import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.johnli.callback.common.CallbackException;
import com.johnli.callback.facade.DelayCallbackClientFacade;
import com.johnli.callback.server.context.ContextHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author johnli  2018-08-09 17:08
 */
public class CallbackClientFactory {
    private static Map<String, DelayCallbackClientFacade> facadeConcurrentHashMap = new HashMap<>();

    //todo load all groups' references on startup
    public static DelayCallbackClientFacade getCallbackClientFacade(String group) {
        DelayCallbackClientFacade delayCallbackClientFacade = facadeConcurrentHashMap.get(group);
        if (delayCallbackClientFacade != null) {
            return delayCallbackClientFacade;
        }

        synchronized (CallbackClientFactory.class) {
            if (facadeConcurrentHashMap.get(group) == null) {
                DelayCallbackClientFacade init = init(group);
                facadeConcurrentHashMap.put(group, init);
            }
        }

        return facadeConcurrentHashMap.get(group);
    }

    private static DelayCallbackClientFacade init(String group) {
        ReferenceBean<DelayCallbackClientFacade> referenceBean = new ReferenceBean<>();
        referenceBean.setApplicationContext(ContextHolder.getApplicationContext());
        referenceBean.setInterface(DelayCallbackClientFacade.class);
        referenceBean.setGroup(group);

        DelayCallbackClientFacade delayCallbackClientFacade = null;
        try {
            referenceBean.afterPropertiesSet();
            delayCallbackClientFacade = referenceBean.get();
        } catch (Exception e) {
            throw new CallbackException("can not init reference for group:" + group, e);
        }

        if (delayCallbackClientFacade == null) {
            throw new CallbackException("can not init reference(null) for group:" + group);
        }

        return delayCallbackClientFacade;
    }
}
