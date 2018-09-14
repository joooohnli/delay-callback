package com.johnli.callback.server.service;


import com.johnli.callback.server.dao.po.CallbackDetailPO;

import java.util.Date;

/**
 * @author johnli  2018-08-29 16:11
 */
public interface CalculatorService {

    boolean canRetry(CallbackDetailPO detailPO);

    Date calculateNextExecTime(Date exeTime, CallbackDetailPO detailPO);
}
