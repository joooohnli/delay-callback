package com.johnli.callback.server.job;

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
 * @author johnli  2018-08-10 18:17
 */
@Component
public class CompensationJob extends AbstractJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompensationJob.class);

    @Autowired
    private DataService dataService;

    @Autowired
    private DistributeService distributeService;


    @Scheduled(fixedRate = JobConstant.COMPENSATION_JOB_INTERVAL_MILLSEC, initialDelay = JobConstant.JOB_DELAY_MILLSEC)
    @Async("compensationJobExecutor")
    @Override
    public void scheduled() {
        if (!isOn()) {
            return;
        }

        ExecuteTemplate.execute(new AbstractExecutor() {
            @Override
            public void execute() {
                Date now = ContextHolder.getSysContext().getCurrentTime();
                Date start = DateUtils.addHours(now, -JobConstant.MAX_COMPENSATION_HOURS);
                Date end = DateUtils.addMilliseconds(now, -JobConstant.SCAN_START_GAP_MILLSEC);
                List<CallbackIdPO> callbackIdPOS = dataService.listIdsByRange(start, end, true, 0, callbackProperties.getMaxBatchCount());


                if (CollectionUtils.isEmpty(callbackIdPOS)) {
                    return;
                }
                LOGGER.info("CompensationJob start:{},end:{},size:{}", start, now, callbackIdPOS.size());

                distributeService.distribute(callbackIdPOS);
            }

            @Override
            public void dealException(Exception e) {
                LOGGER.error("CompensationJob error", e);
            }
        });
    }
}
