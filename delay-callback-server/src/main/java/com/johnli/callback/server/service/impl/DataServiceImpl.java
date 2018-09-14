package com.johnli.callback.server.service.impl;

import com.johnli.callback.server.autoconfigure.property.CallbackProperties;
import com.johnli.callback.server.context.ContextHolder;
import com.johnli.callback.server.dao.DataDao;
import com.johnli.callback.server.dao.po.CallbackDetailPO;
import com.johnli.callback.server.dao.po.CallbackIdPO;
import com.johnli.callback.server.dao.po.LockPO;
import com.johnli.callback.server.dao.po.PageResult;
import com.johnli.callback.server.service.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author johnli  2018-08-10 12:00
 */
@Service
public class DataServiceImpl implements DataService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataServiceImpl.class);

    @Autowired
    private CallbackProperties callbackProperties;

    @Autowired
    private DataDao dataDao;

    @Override
    public List<CallbackIdPO> listIdsByRange(Date start, Date end, boolean desc, int offset, int count) {
        return dataDao.listIdsByRange(start, end, desc, offset, count);
    }

    @Override
    public boolean add(CallbackDetailPO callbackDetailPO) {
        Date now = ContextHolder.getSysContext().getCurrentTime();
        callbackDetailPO.setCreateTime(now);
        callbackDetailPO.setModifyTime(now);

        boolean add = dataDao.add(callbackDetailPO);
        LOGGER.info("add:{},result:{}", callbackDetailPO, add);
        return add;
    }

    @Override
    public boolean delete(String uid) {
        CallbackDetailPO callbackDetailPO = get(uid);
        if (callbackDetailPO == null) {
            return true;
        }
        boolean delete = dataDao.delete(callbackDetailPO.getGroup(), uid);
        LOGGER.info("delete:{},result:{}", uid, delete);
        return delete;
    }

    @Override
    public boolean markFailure(CallbackDetailPO po) {
        boolean markFailure = dataDao.markFailure(po);
        LOGGER.info("markFailure:{},result:{}", po, markFailure);
        return markFailure;
    }

    @Override
    public boolean update(CallbackDetailPO po) {
        po.setModifyTime(new Date());
        boolean update = dataDao.update(po);
        LOGGER.info("update:{},result:{}", po, update);
        return update;
    }

    @Override
    public boolean deleteFailure(String group, String uid) {
        return dataDao.deleteFailure(group, uid);
    }

    @Override
    public CallbackDetailPO getFailure(String group, String uid) {
        return dataDao.getFailure(group, uid);
    }

    @Override
    public PageResult<CallbackDetailPO> listFailures(String group, int offset, int count) {
        return dataDao.listFailures(group, offset, count);
    }

    @Override
    public CallbackDetailPO get(String uid) {
        return dataDao.get(uid);
    }

    @Override
    public List<String> listGroup() {
        return dataDao.listGroup();
    }

    @Override
    public int count(String group) {
        return dataDao.count(group);
    }

    @Override
    public int countFailure(String group) {
        return dataDao.countFailure(group);
    }

    @Override
    public <R> R accessWithinLock(String uid, Accessor<R> accessor) {
        LockPO lockPO = dataDao.acquireLock(uid, 0, callbackProperties.getLockTimeoutSec() * 1000);
        try {
            return accessor.access(lockPO != null);
        } finally {
            if (lockPO != null) {
                dataDao.releaseLock(lockPO);
            }
        }
    }
}
