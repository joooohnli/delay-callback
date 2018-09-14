package com.johnli.callback.server.service;

import com.johnli.callback.server.dao.po.CallbackDetailPO;
import com.johnli.callback.server.dao.po.CallbackIdPO;
import com.johnli.callback.server.dao.po.PageResult;

import java.util.Date;
import java.util.List;

/**
 * @author johnli  2018-08-10 10:08
 */
public interface DataService {
    List<CallbackIdPO> listIdsByRange(Date start, Date end, boolean desc, int offset, int count);

    boolean add(CallbackDetailPO po);

    boolean delete(String uid);

    boolean markFailure(CallbackDetailPO po);

    boolean update(CallbackDetailPO po);

    CallbackDetailPO get(String uid);

    List<String> listGroup();

    int count(String group);

    int countFailure(String group);

    CallbackDetailPO getFailure(String group, String uid);

    boolean deleteFailure(String group, String uid);

    PageResult<CallbackDetailPO> listFailures(String group, int offset, int count);

    <R> R accessWithinLock(String uid, Accessor<R> accessor);

    interface Accessor<R> {
        R access(boolean canAccess);
    }
}
