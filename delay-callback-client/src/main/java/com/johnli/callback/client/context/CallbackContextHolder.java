package com.johnli.callback.client.context;

import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.johnli.callback.client.DelayCallback;
import com.johnli.callback.client.facade.impl.DelayCallbackClientFacadeImpl;
import com.johnli.callback.common.CallbackException;
import com.johnli.callback.facade.DelayCallbackClientFacade;
import com.johnli.callback.facade.DelayCallbackServerFacade;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;

/**
 * @author johnli  2018-08-07 14:10
 */
public class CallbackContextHolder {
    private static ApplicationContext applicationContext;

    private static final HashMap<String, DelayCallback> delayCallbackMap = new HashMap<>();

    private static DelayCallbackServerFacade delayCallbackServerFacade;

    private static DelayCallbackClientFacade delayCallbackClientFacade = new DelayCallbackClientFacadeImpl();

    private static String group;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        CallbackContextHolder.applicationContext = applicationContext;
    }

    public static HashMap<String, DelayCallback> getDelayCallbackMap() {
        return delayCallbackMap;
    }

    public static DelayCallbackServerFacade getDelayCallbackServerFacade() {
        if (delayCallbackServerFacade != null) {
            return delayCallbackServerFacade;
        }
        synchronized (CallbackContextHolder.class) {
            if (delayCallbackServerFacade == null) {
                delayCallbackServerFacade = initDelayCallbackClientFacade();
            }
        }

        return delayCallbackServerFacade;
    }


    public static DelayCallbackClientFacade getDelayCallbackClientFacade() {
        return delayCallbackClientFacade;
    }

    public static String getGroup() {
        return group;
    }

    public static void setGroup(String group) {
        CallbackContextHolder.group = group;
    }

    private static DelayCallbackServerFacade initDelayCallbackClientFacade() {
        ReferenceBean<DelayCallbackServerFacade> referenceBean = new ReferenceBean<>();
        referenceBean.setApplicationContext(applicationContext);
        referenceBean.setInterface(DelayCallbackServerFacade.class);

        try {
            referenceBean.afterPropertiesSet();
            return referenceBean.get();
        } catch (Exception e) {
            throw new CallbackException("can not init DelayCallbackServerFacade", e);
        }
    }
}
