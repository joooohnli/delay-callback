package com.johnli.callback.server.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.johnli.callback.facade.CallbackProcessFacade;
import com.johnli.callback.server.dao.po.CallbackIdPO;
import com.johnli.callback.server.service.DistributeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author johnli  2018-08-10 18:18
 */
@Service
public class DistributeServiceImpl implements DistributeService {

    @Reference(async = true, sent = false)
    private CallbackProcessFacade callbackProcessFacade;

    @Override
    public void distribute(List<CallbackIdPO> callbackIdPOS) {
        for (CallbackIdPO callbackIdPO : callbackIdPOS) {
            // distribute task through asynchronous dubbo invocation
            callbackProcessFacade.process(callbackIdPO.getUid());
        }
    }


}
