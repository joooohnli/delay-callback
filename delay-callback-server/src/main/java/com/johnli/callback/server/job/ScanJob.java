package com.johnli.callback.server.job;

import com.johnli.callback.server.autoconfigure.property.CallbackProperties;
import com.johnli.callback.server.constant.JobConstant;
import com.johnli.callback.server.context.ContextHolder;
import com.johnli.callback.server.dao.po.CallbackIdPO;
import com.johnli.callback.server.service.DataService;
import com.johnli.callback.server.service.DistributeService;
import com.johnli.callback.server.template.AbstractExecutor;
import com.johnli.callback.server.template.ExecuteTemplate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


/**
 * @author johnli  2018-08-10 18:11
 */
@Component
public class ScanJob extends AbstractJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScanJob.class);

    @Autowired
    private DataService dataService;

    @Autowired
    private DistributeService distributeService;

    @Autowired
    private CallbackProperties callbackProperties;

    @Scheduled(fixedRate = JobConstant.SCAN_JOB_GAP_MILLSEC, initialDelay = JobConstant.JOB_DELAY_MILLSEC)
    @Async("scanJobExecutor")
    @Override
    public void scheduled() {
        if (!isOn()) {
            return;
        }

        ExecuteTemplate.execute(new AbstractExecutor() {

            @Override
            public void execute() {
                Date now = ContextHolder.getSysContext().getCurrentTime();
                Date start = DateUtils.addMilliseconds(now, -JobConstant.SCAN_START_GAP_MILLSEC);
                Date end = DateUtils.addMilliseconds(now, JobConstant.SCAN_END_GAP_MILLSEC);
                List<CallbackIdPO> callbackIdPOS = dataService.listIdsByRange(start, end, false, 0, callbackProperties.getMaxBatchCount());


                if (CollectionUtils.isEmpty(callbackIdPOS)) {
                    return;
                }
                LOGGER.info("ScanJob start:{},end:{},size:{}", start, now, callbackIdPOS.size());

                distributeService.distribute(callbackIdPOS);
            }

            @Override
            public void dealException(Exception e) {
                LOGGER.error("ScanJob error", e);
            }
        });

    }
}
