package com.johnli.callback.server.service.impl;

import com.johnli.callback.common.CallbackException;
import com.johnli.callback.request.RegisterRequest;
import com.johnli.callback.server.context.ContextHolder;
import com.johnli.callback.server.context.biz.BizContext;
import com.johnli.callback.server.dao.po.CallbackDetailPO;
import com.johnli.callback.server.service.CalculatorService;
import com.johnli.callback.server.service.DataService;
import com.johnli.callback.server.service.RegisterService;
import com.johnli.callback.server.util.DataConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

/**
 * @author johnli  2018-08-10 11:32
 */
@Service
public class RegisterServiceImpl implements RegisterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterServiceImpl.class);

    @Autowired
    private DataService dataService;

    @Autowired
    private CalculatorService calculatorService;

    @Override
    public String register(RegisterRequest request) {
        String uid = generateUid(request.getGroup(),
                request.getAlias(),
                request.getCallbackParam().getArgs(),
                request.getCallbackParam().getIdempotentKey());

        if (request.getCallbackParam().needIdempotent()) {
            if (dataService.get(uid) != null) {
                return uid;
            }
        }

        CallbackDetailPO callbackDetailPO = DataConverter.convert2PO(request);
        callbackDetailPO.setUid(uid);
        callbackDetailPO.setNextExecTime(calculatorService.calculateNextExecTime(
                ContextHolder.getSysContext().getCurrentTime(),
                callbackDetailPO));

        boolean add = dataService.add(callbackDetailPO);

        if (add) {
            return uid;
        }

        return null;
    }

    @Override
    public boolean unRegister(String uid) {
        return dataService.accessWithinLock(uid, canAccess -> {
            if (!canAccess) {
                throw new CallbackException("callback is being invoked");
            }
            CallbackDetailPO detailPO = dataService.get(uid);
            if (detailPO == null) {
                return true;
            }
            BizContext bizContext = ContextHolder.getBizContext();
            bizContext.setDetailPO(detailPO);
            //todo distinguish if has been invoked
//            if (dataService.get(uid) == null) {
//                if (dataService.getFailure(uid) == null) {
//                    throw new CallbackException("uid do not exist");
//                }
//                else {
//                    throw new CallbackException("uid do not exist");
//                }
//            }
            return dataService.delete(uid);
        });
    }

    @Override
    public String generateUid(String group, String alias, List<String> args, String idempotentKey) {
        String keys = composeKeys(group, alias, args);
        if (StringUtils.isEmpty(idempotentKey)) {
            keys += UUID.randomUUID();
        } else {
            keys += idempotentKey;
        }
        return DigestUtils.md5DigestAsHex(keys.getBytes());
    }

    private String composeKeys(String group, String alias, List<String> args) {
        String sb = group + alias;

//        if (CollectionUtils.isNotEmpty(args)) {
//            for (String arg : args) {
//                sb.append(arg);
//            }
//        }

        return sb;
    }
}
