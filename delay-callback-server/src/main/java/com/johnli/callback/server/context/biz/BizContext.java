package com.johnli.callback.server.context.biz;


import com.johnli.callback.server.dao.po.CallbackDetailPO;

/**
 * @author johnli  2018-08-30 17:44
 */
public class BizContext {
    private CallbackDetailPO detailPO;

    public CallbackDetailPO getDetailPO() {
        return detailPO;
    }

    public BizContext setDetailPO(CallbackDetailPO detailPO) {
        this.detailPO = detailPO;
        return this;
    }
}
