package com.johnli.callback.server.autoconfigure;

import com.johnli.callback.server.autoconfigure.property.CallbackProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author johnli  2018-08-10 18:21
 */
@Configuration
@EnableAsync
@EnableConfigurationProperties(value = {CallbackProperties.class})
public class AsyncAutoConfiguration {
    @Autowired
    private CallbackProperties callbackProperties;

    @Bean("scanJobExecutor")
    public Executor scanJobExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(callbackProperties.getScanPool().getCorePoolSize());
        executor.setMaxPoolSize(callbackProperties.getScanPool().getMaxPoolSize());
        executor.setQueueCapacity(callbackProperties.getScanPool().getQueueCapacity());
        executor.setKeepAliveSeconds(callbackProperties.getScanPool().getKeepAliveSeconds());
        executor.setAllowCoreThreadTimeOut(callbackProperties.getScanPool().isAllowCoreThreadTimeOut());
        executor.initialize();

        return executor;
    }

    @Bean("compensationJobExecutor")
    public Executor compensationJobExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(callbackProperties.getCompensationPool().getCorePoolSize());
        executor.setMaxPoolSize(callbackProperties.getCompensationPool().getMaxPoolSize());
        executor.setQueueCapacity(callbackProperties.getCompensationPool().getQueueCapacity());
        executor.setKeepAliveSeconds(callbackProperties.getCompensationPool().getKeepAliveSeconds());
        executor.setAllowCoreThreadTimeOut(callbackProperties.getCompensationPool().isAllowCoreThreadTimeOut());
        executor.initialize();

        return executor;
    }

    @Bean("monitorJobExecutor")
    public Executor monitorJobExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(callbackProperties.getMonitorPool().getCorePoolSize());
        executor.setMaxPoolSize(callbackProperties.getMonitorPool().getMaxPoolSize());
        executor.setQueueCapacity(callbackProperties.getMonitorPool().getQueueCapacity());
        executor.setKeepAliveSeconds(callbackProperties.getMonitorPool().getKeepAliveSeconds());
        executor.setAllowCoreThreadTimeOut(callbackProperties.getMonitorPool().isAllowCoreThreadTimeOut());
        executor.initialize();

        return executor;
    }
}
