package com.johnli.callback.client.autoconfigure;

import com.johnli.callback.client.context.CallbackContextHolder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * @author johnli  2018-08-07 17:52
 */
@Configuration
public class SpringAutoConfigure implements InitializingBean {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        CallbackContextHolder.setApplicationContext(applicationContext);
    }
}
