package com.johnli.callback;

import com.johnli.callback.param.CallbackParam;
import com.johnli.callback.server.dao.DataDao;
import com.johnli.callback.server.dao.po.CallbackDetailPO;
import com.johnli.callback.server.dao.po.LockPO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author johnli  2018-08-10 15:52
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class DataDaoTest {
    @Autowired
    private DataDao dataDao;

    @Test
    public void testCRUD() {
        String uid = "123456";
        dataDao.delete("dubbo-provider-demo", uid);

        CallbackDetailPO callbackDetailPO = new CallbackDetailPO();
        ArrayList<String> args = new ArrayList<>();
        args.add("hello");
        args.add("world");
        CallbackParam callbackParam = new CallbackParam(args, 10);
        callbackDetailPO.setUid(uid)
                .setGroup("test")
                .setAlias("alias")
                .setCreateTime(new Date())
                .setModifyTime(new Date())
                .setNextExecTime(new Date())
                .setLastError("extra")
                .setCallbackParam(callbackParam);

        boolean add = dataDao.add(callbackDetailPO);
        Assert.assertTrue(add);
        CallbackDetailPO callbackDetailPO1 = dataDao.get(callbackDetailPO.getUid());
        System.out.println("callbackDetailPO1" + callbackDetailPO1);
        Assert.assertNotNull(callbackDetailPO1);

        callbackDetailPO.setRetryTh(2);
        boolean update = dataDao.update(callbackDetailPO);
        Assert.assertTrue(update);
        CallbackDetailPO callbackDetailPO2 = dataDao.get(callbackDetailPO.getUid());
        System.out.println("callbackDetailPO2" + callbackDetailPO2);
        Assert.assertNotNull(callbackDetailPO2);
    }

    @Test
    public void testLock() throws InterruptedException {
        String lockKey = "123456";
        LockPO lockPO = dataDao.acquireLock(lockKey, 0, 3000);
        Assert.assertNotNull(lockPO);

        LockPO lockPO1 = dataDao.acquireLock(lockKey, 0, 3000);
        Assert.assertNull(lockPO1);

        Thread.sleep(3001);

        LockPO lockPO2 = dataDao.acquireLock(lockKey, 0, 3000);
        Assert.assertNotNull(lockPO2);

        boolean suc = dataDao.releaseLock(lockPO2);
        Assert.assertTrue(suc);

        LockPO lockPO3 = dataDao.acquireLock(lockKey, 0, 3000);
        Assert.assertNotNull(lockPO3);
        dataDao.releaseLock(lockPO3);
    }

}
