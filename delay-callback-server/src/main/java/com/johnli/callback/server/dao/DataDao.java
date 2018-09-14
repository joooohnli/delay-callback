package com.johnli.callback.server.dao;

import com.johnli.callback.server.dao.po.CallbackDetailPO;
import com.johnli.callback.server.dao.po.CallbackIdPO;
import com.johnli.callback.server.dao.po.LockPO;
import com.johnli.callback.server.dao.po.PageResult;

import java.util.Date;
import java.util.List;

/**
 * @author johnli  2018-08-10 12:01
 */
public interface DataDao {
    List<CallbackIdPO> listIdsByRange(Date start, Date end, boolean desc, int offset, int count);

    boolean add(CallbackDetailPO po);

    boolean delete(String group, String uid);

    boolean update(CallbackDetailPO po);

    boolean markFailure(CallbackDetailPO po);

    CallbackDetailPO get(String uid);

    List<String> listGroup();

    int count(String group);

    int countFailure(String group);

    CallbackDetailPO getFailure(String group, String uid);

    boolean deleteFailure(String group, String uid);

    PageResult<CallbackDetailPO> listFailures(String group, int offset, int count);

    LockPO acquireLock(String uid, long acquireTimeout, long lockExpireTime);

    boolean releaseLock(LockPO lockPO);
}
