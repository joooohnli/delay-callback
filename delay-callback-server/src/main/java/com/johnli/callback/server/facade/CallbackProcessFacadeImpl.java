package com.johnli.callback.server.facade;

import com.alibaba.dubbo.config.annotation.Service;
import com.johnli.callback.facade.CallbackProcessFacade;
import com.johnli.callback.param.ErrorCode;
import com.johnli.callback.param.ErrorCodeEnum;
import com.johnli.callback.result.CallbackResult;
import com.johnli.callback.server.constant.JobConstant;
import com.johnli.callback.server.context.ContextHolder;
import com.johnli.callback.server.context.biz.BizContext;
import com.johnli.callback.server.dao.po.CallbackDetailPO;
import com.johnli.callback.server.log.digest.CallbackDigestLog;
import com.johnli.callback.server.log.digest.DigestLogInfo;
import com.johnli.callback.server.service.CallbackService;
import com.johnli.callback.server.service.DataService;
import com.johnli.callback.server.template.AbstractExecutor;
import com.johnli.callback.server.template.ExecuteTemplate;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;


/**
 * @author johnli  2018-08-22 15:15
 */
@Service
public class CallbackProcessFacadeImpl implements CallbackProcessFacade {
    private static final Logger LOGGER = LoggerFactory.getLogger(CallbackProcessFacadeImpl.class);

    @Autowired
    private DataService dataService;

    @Autowired
    private CallbackService callbackService;

    @Override
    public void process(String uid) {
        ExecuteTemplate.execute(new AbstractExecutor<String, CallbackResult>(uid, new CallbackResult()) {

            @Override
            public void execute() {
                CallbackResult result = doProcess(uid);
                this.result.setSuccess(result.isSuccess());
                this.result.setErrorCode(result.getErrorCode());
            }

            @Override
            public void dealException(Exception e) {
                LOGGER.error("process error", e);
            }

            @Override
            public DigestLogInfo composeDigestLog() {
                BizContext bizContext = ContextHolder.getBizContext();
                CallbackDetailPO detailPO = bizContext == null ? null : bizContext.getDetailPO();

                return new CallbackDigestLog()
                        .setUid(uid)
                        .setErrorCode(result.getErrorCode() == null ? null : result.getErrorCode().getCode())
                        .setErrorMsg(result.getErrorCode() == null ? null : result.getErrorCode().getMsg())
                        .setNextExecTime(detailPO == null ? null : detailPO.getNextExecTime())
                        .setAlias(detailPO == null ? null : detailPO.getAlias())
                        .setSuccess(result.isSuccess())
                        .setBiz("callback")
                        .setGroup(detailPO == null ? null : detailPO.getGroup());
            }

        });
    }

    private CallbackResult doProcess(String uid) {
        return dataService.accessWithinLock(uid, canAccess -> {
            CallbackResult callbackResult = new CallbackResult();
            if (!canAccess) {
                return callbackResult.setErrorCode(new ErrorCode(ErrorCodeEnum.CALLBACK_EXCEPTION).setMsg("can not acquire lock"));
            }

            // check if record exists
            CallbackDetailPO callbackDetailPO = dataService.get(uid);
            if (callbackDetailPO == null) {
                return callbackResult.setErrorCode(new ErrorCode(ErrorCodeEnum.CALLBACK_EXCEPTION).setMsg("record not exists"));
            }

            ContextHolder.getBizContext().setDetailPO(callbackDetailPO);

            // validate po
            callbackDetailPO.validate();

            // check execute time
            Date jobEndTime = DateUtils.addMilliseconds(ContextHolder.getSysContext().getCurrentTime(), JobConstant.SCAN_END_GAP_MILLSEC);
            if (callbackDetailPO.getNextExecTime().after(jobEndTime)) {
                return callbackResult.setErrorCode(new ErrorCode(ErrorCodeEnum.CALLBACK_EXCEPTION).setMsg("execution time is not up"));
            }

            return callbackService.callback(callbackDetailPO);
        });

    }
}
