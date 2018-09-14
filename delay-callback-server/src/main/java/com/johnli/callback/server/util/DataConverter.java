package com.johnli.callback.server.util;

import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSON;
import com.johnli.callback.request.CallbackRequest;
import com.johnli.callback.request.RegisterRequest;
import com.johnli.callback.server.dao.po.CallbackDetailPO;

/**
 * @author johnli  2018-08-10 11:56
 */
public class DataConverter {

    public static CallbackDetailPO convert2PO(RegisterRequest request) {
        CallbackDetailPO po = new CallbackDetailPO();
        po.setAlias(request.getAlias());
        po.setCallbackParam(request.getCallbackParam());
        po.setGroup(request.getGroup());
        po.setDubboContext(JSON.toJSONString(RpcContext.getContext()));

        return po;
    }

    public static CallbackRequest convert2CallbackRequest(CallbackDetailPO detailPO) {
        CallbackRequest callbackRequest = new CallbackRequest();
        callbackRequest.setCallbackParam(detailPO.getCallbackParam());
        callbackRequest.setRetryTh(detailPO.getRetryTh());
        callbackRequest.setUid(detailPO.getUid());
        callbackRequest.setAlias(detailPO.getAlias());

        return callbackRequest;
    }
}
