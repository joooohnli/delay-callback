package com.johnli.callback.server.job;

import com.johnli.callback.server.constant.JobConstant;
import com.johnli.callback.server.log.digest.DigestLogInfo;
import com.johnli.callback.server.log.digest.MonitorCountDigestLog;
import com.johnli.callback.server.service.DataService;
import com.johnli.callback.server.template.AbstractExecutor;
import com.johnli.callback.server.template.ExecuteTemplate;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * @author johnli  2018-08-10 18:11
 */
@Component
public class MonitorJob extends AbstractJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorJob.class);

    @Autowired
    private DataService dataService;

    @Scheduled(fixedRate = JobConstant.MONITOR_JOB_INTERVAL_MILLSEC, initialDelay = JobConstant.JOB_DELAY_MILLSEC)
    @Async("monitorJobExecutor")
    @Override
    public void scheduled() {
        if (!isOn()) {
            return;
        }

        List<DigestLogInfo> logInfos = new ArrayList<>();

        ExecuteTemplate.execute(new AbstractExecutor() {

            @Override
            public void execute() {
                List<String> groups = dataService.listGroup();
                if (CollectionUtils.isEmpty(groups)) {
                    return;
                }

                for (String group : groups) {
                    int count = dataService.count(group);
                    int countFailure = dataService.countFailure(group);

                    DigestLogInfo log = new MonitorCountDigestLog()
                            .setCount(count)
                            .setFailedCount(countFailure)
                            .setGroup(group)
                            .setBiz("monitorCount");
                    logInfos.add(log);
                }
            }

            @Override
            public List<DigestLogInfo> composeDigestLogs() {
                return logInfos;
            }

            @Override
            public void dealException(Exception e) {
                LOGGER.error("MonitorJob error", e);
            }
        });

    }
}
