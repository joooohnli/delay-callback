package com.johnli.callback.server.service;


import com.johnli.callback.server.dao.po.CallbackIdPO;

import java.util.List;

/**
 * @author johnli  2018-08-10 10:19
 */
public interface DistributeService {
    void distribute(List<CallbackIdPO> callbackIdPOS);
}
