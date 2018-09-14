package com.johnli.callback.server.autoconfigure;

import com.johnli.callback.server.autoconfigure.property.CallbackProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author johnli  2018-08-09 18:51
 */
@Configuration
@EnableConfigurationProperties(CallbackProperties.class)
public class JedisAutoConfiguration {

    @Autowired
    private CallbackProperties callbackProperties;

    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(callbackProperties.getJedis().getMaxTotal());
        config.setMaxIdle(callbackProperties.getJedis().getMaxIdle());
        config.setMaxWaitMillis(callbackProperties.getJedis().getMaxWaitMillis());
        config.setBlockWhenExhausted(false);

        if (StringUtils.isBlank(callbackProperties.getJedis().getPassword())) {
            return new JedisPool(config,
                    callbackProperties.getJedis().getHost(),
                    callbackProperties.getJedis().getPort(),
                    callbackProperties.getJedis().getTimeout());
        }
        return new JedisPool(config,
                callbackProperties.getJedis().getHost(),
                callbackProperties.getJedis().getPort(),
                callbackProperties.getJedis().getTimeout(),
                callbackProperties.getJedis().getPassword());
    }

}