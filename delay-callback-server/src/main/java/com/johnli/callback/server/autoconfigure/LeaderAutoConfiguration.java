package com.johnli.callback.server.autoconfigure;

import com.johnli.callback.server.autoconfigure.property.CallbackProperties;
import com.johnli.callback.server.context.ContextHolder;
import com.johnli.callback.server.util.OSUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

/**
 * @author johnli  2018-08-09 18:51
 */
@Configuration
@EnableConfigurationProperties(CallbackProperties.class)
public class LeaderAutoConfiguration implements InitializingBean, DisposableBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(LeaderAutoConfiguration.class);
    private static final String PATH = "/delay/callback/leader";

    @Autowired
    private CuratorFramework curatorClient;

    private LeaderLatch leaderLatch;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (leaderLatch != null) {
            return;
        }
        leaderLatch = new LeaderLatch(curatorClient,
                PATH,
                OSUtil.getServerIp() + "_" + UUID.randomUUID().toString());
        leaderLatch.addListener(new LeaderLatchListener() {
            @Override
            public void isLeader() {
                ContextHolder.setLeader(true);
                LOGGER.info("im leader");
            }

            @Override
            public void notLeader() {
                ContextHolder.setLeader(false);
                LOGGER.info("im not leader");
            }
        });
        leaderLatch.start();
    }

    @Override
    public void destroy() throws Exception {
        if (leaderLatch != null) {
            leaderLatch.close();
        }
    }
}