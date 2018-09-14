package com.johnli.callback.server.service;

import com.johnli.callback.result.CallbackResult;
import com.johnli.callback.server.dao.po.CallbackDetailPO;

/**
 * @author johnli  2018-08-09 17:03
 */
public interface CallbackService {
    CallbackResult callback(CallbackDetailPO detailPO);
}
