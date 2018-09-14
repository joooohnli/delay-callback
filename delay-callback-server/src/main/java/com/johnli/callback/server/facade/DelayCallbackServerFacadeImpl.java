package com.johnli.callback.server.facade;

import com.alibaba.dubbo.config.annotation.Service;
import com.johnli.callback.common.CallbackException;
import com.johnli.callback.facade.DelayCallbackServerFacade;
import com.johnli.callback.request.RegisterRequest;
import com.johnli.callback.request.UnRegisterRequest;
import com.johnli.callback.result.RegisterResult;
import com.johnli.callback.result.UnRegisterResult;
import com.johnli.callback.server.context.ContextHolder;
import com.johnli.callback.server.context.biz.BizContext;
import com.johnli.callback.server.dao.po.CallbackDetailPO;
import com.johnli.callback.server.log.digest.DigestLogInfo;
import com.johnli.callback.server.log.digest.RegisterDigestLog;
import com.johnli.callback.server.log.digest.UnRegisterDigestLog;
import com.johnli.callback.server.service.RegisterService;
import com.johnli.callback.server.template.AbstractExecutor;
import com.johnli.callback.server.template.ExecuteTemplate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author johnli  2018-08-06 13:53
 */
@Service
public class DelayCallbackServerFacadeImpl implements DelayCallbackServerFacade {
    private static final Logger LOGGER = LoggerFactory.getLogger(DelayCallbackServerFacadeImpl.class);

    @Autowired
    private RegisterService registerService;

    @Override
    public RegisterResult register(RegisterRequest request) {
        RegisterResult registerResult = new RegisterResult();

        ExecuteTemplate.execute(new AbstractExecutor<RegisterRequest, RegisterResult>(request, registerResult) {
            @Override
            public void execute() {
                if (request == null) {
                    throw new CallbackException("request can not be null");
                }
                request.validate();

                String uid = registerService.register(request);
                if (StringUtils.isNoneEmpty(uid)) {
                    result.setSuccess(true);
                    result.setUid(uid);
                }
            }

            @Override
            public void dealException(Exception e) {
                result.setUid(null);
                if (e instanceof CallbackException) {
                    result.setErrorMsg(e.getMessage());
                } else {
                    result.setErrorMsg("system error");
                    LOGGER.error("register error", e);
                }
            }

            @Override
            public DigestLogInfo composeDigestLog() {
                return new RegisterDigestLog()
                        .setErrorMsg(result.getErrorMsg())
                        .setAlias(request.getAlias())
                        .setDelaySeconds(request.getCallbackParam().getDelaySeconds())
                        .setSuccess(result.isSuccess())
                        .setUid(result.getUid())
                        .setBiz("register")
                        .setGroup(request.getGroup());
            }
        });

        return registerResult;
    }

    @Override
    public UnRegisterResult unRegister(UnRegisterRequest request) {
        UnRegisterResult unRegisterResult = new UnRegisterResult();

        ExecuteTemplate.execute(new AbstractExecutor<UnRegisterRequest, UnRegisterResult>(request, unRegisterResult) {

            @Override
            public void execute() {
                if (request == null) {
                    throw new CallbackException("request can not be null");
                }
                request.validate();

                boolean unRegister = registerService.unRegister(request.getUid());
                result.setSuccess(unRegister);
            }

            @Override
            public void dealException(Exception e) {
                result.setSuccess(false);
                if (e instanceof CallbackException) {
                    result.setErrorMsg(e.getMessage());
                } else {
                    result.setErrorMsg("system error");
                    LOGGER.error("unRegister error", e);
                }
            }

            @Override
            public DigestLogInfo composeDigestLog() {
                BizContext bizContext = ContextHolder.getBizContext();
                CallbackDetailPO detailPO = bizContext == null ? null : bizContext.getDetailPO();
                return new UnRegisterDigestLog()
                        .setUid(request.getUid())
                        .setSuccess(result.isSuccess())
                        .setErrorMsg(result.getErrorMsg())
                        .setAlias(detailPO == null ? null : detailPO.getAlias())
                        .setBiz("unRegister")
                        .setGroup(detailPO == null ? null : detailPO.getGroup());
            }

        });
        return unRegisterResult;
    }

}
