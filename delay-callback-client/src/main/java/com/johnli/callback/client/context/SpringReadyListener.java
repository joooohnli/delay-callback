package com.johnli.callback.client.context;

import com.alibaba.dubbo.config.spring.ServiceBean;
import com.johnli.callback.client.DelayCallback;
import com.johnli.callback.client.utils.EnvUtils;
import com.johnli.callback.common.CallbackException;
import com.johnli.callback.facade.DelayCallbackClientFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author johnli  2018-08-07 13:59
 */
public class SpringReadyListener implements ApplicationListener<ContextRefreshedEvent> {
    private static final AtomicBoolean overrided = new AtomicBoolean(false);

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringReadyListener.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!overrided.compareAndSet(false, true)) {
            return;
        }

        LOGGER.info("init delay callback env start...");

        initGroupName();

        initCallbacks();

        registerClientFacade();

        referServerFacade();

        LOGGER.info("init delay callback env end...");
    }

    private void initGroupName() {
        String appName = CallbackContextHolder.getApplicationContext().getEnvironment().getProperty("spring.application.name");
        if (StringUtils.isBlank(appName)) {
            throw new CallbackException("Callback init error,groupName is empty");
        }
        CallbackContextHolder.setGroup(appName);
    }

    private void initCallbacks() {
        String callbackPackage = CallbackContextHolder.getApplicationContext().getEnvironment().getProperty("delay.callback.package");
        if (StringUtils.isBlank(callbackPackage)) {
            callbackPackage = EnvUtils.deduceMainClassPackage();
        }
        LOGGER.info("start to scan callbacks under:{}", callbackPackage);

        Reflections reflections = new Reflections(callbackPackage);

        Set<Class<? extends DelayCallback>> classes = reflections.getSubTypesOf(DelayCallback.class);

        LOGGER.info("start to init callbacks:{}", classes);
        if (CollectionUtils.isEmpty(classes)) {
            return;
        }
        for (Class<? extends DelayCallback> aClass : classes) {
            DelayCallback instance = null;
            try {
                instance = (DelayCallback) getInstance(aClass);

            } catch (NoSuchMethodException e) {
                throw new CallbackException("Callback init error,reason:Anonymous inner class implementation of DelayCallback can not include local variables of external method,class:" + aClass, e);
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                throw new CallbackException("Callback init error,class:" + aClass, e);
            }
            if (instance == null) {
                throw new CallbackException("Callback init error,instance is null,class:" + aClass);
            }
            String alias = instance.alias();
            if (alias == null || alias.equals("")) {
                LOGGER.error("Callback init error,alias is empty,class:" + aClass);
                continue;
            }
            HashMap<String, DelayCallback> delayCallbackMap = CallbackContextHolder.getDelayCallbackMap();
            if (delayCallbackMap.containsKey(alias)) {
                LOGGER.error("Callback init error,duplicated alias,class:" + aClass);
                continue;
            }
            delayCallbackMap.put(alias, instance);
        }
    }

    private Object getInstance(Class<?> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> enclosingClass = clazz.getEnclosingClass();
        if (enclosingClass == null) {
            String[] beanNamesForType = CallbackContextHolder.getApplicationContext().getBeanNamesForType(clazz);
            if (beanNamesForType == null || beanNamesForType.length == 0) {
                return clazz.newInstance();
            }
            return CallbackContextHolder.getApplicationContext().getBean(clazz);
        }

        Constructor<?> declaredConstructor = clazz.getDeclaredConstructor(enclosingClass);
        Object enclosingBean = getInstance(enclosingClass);
        declaredConstructor.setAccessible(true);
        return declaredConstructor.newInstance(enclosingBean);
    }

    private void registerClientFacade() {

        ServiceBean serviceBean = new ServiceBean<DelayCallbackClientFacade>();
        serviceBean.setApplicationContext(CallbackContextHolder.getApplicationContext());
        serviceBean.setInterface(DelayCallbackClientFacade.class);
        serviceBean.setGroup(CallbackContextHolder.getGroup());

        serviceBean.setRef(CallbackContextHolder.getDelayCallbackClientFacade());
        try {
            serviceBean.afterPropertiesSet();
        } catch (Exception e) {
            throw new CallbackException("Callback init error,can not export DelayCallbackClientFacade");
        }

        if (!serviceBean.isExported()) {
            serviceBean.export();
        }
    }

    private void referServerFacade() {
        // try to init
        try {
            CallbackContextHolder.getDelayCallbackServerFacade();
        } catch (Exception e) {
            LOGGER.warn("failed to try to init DelayCallbackServerFacade", e);
        }
    }
}
