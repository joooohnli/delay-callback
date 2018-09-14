package com.johnli.callback.server.autoconfigure;

import com.johnli.callback.server.context.ContextHolder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * @author johnli  2018-08-07 17:52
 */
@Configuration
public class ApplicationContextAutoConfigure implements InitializingBean {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        ContextHolder.setApplicationContext(applicationContext);
    }
}
