package com.johnli.callback;

import com.johnli.callback.facade.DelayCallbackServerFacade;
import com.johnli.callback.param.CallbackParam;
import com.johnli.callback.param.RetryStrategy;
import com.johnli.callback.request.RegisterRequest;
import com.johnli.callback.result.RegisterResult;
import com.johnli.callback.server.context.ContextHolder;
import com.johnli.callback.server.dao.po.CallbackDetailPO;
import com.johnli.callback.server.service.DataService;
import com.johnli.callback.server.service.RegisterService;
import com.johnli.callback.server.template.AbstractExecutor;
import com.johnli.callback.server.template.ExecuteTemplate;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author johnli  2018-08-09 19:09
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class FacadeTest {

    @Autowired
    private DelayCallbackServerFacade delayCallbackServerFacade;
    @Autowired
    private DataService dataService;
    @Autowired
    private RegisterService registerService;

    public String testRegister() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setAlias("callback02");
        registerRequest.setGroup("dubbo-provider-demo2");
        List<String> args = new ArrayList<>();
        args.add("hello");
        args.add("world");
        RetryStrategy retryStrategy = new RetryStrategy();
        retryStrategy.setInterval(10);
        retryStrategy.setType(1);
        retryStrategy.setTimes(3);
        CallbackParam callbackParam = new CallbackParam(args, 3);
        callbackParam.setRetryStrategy(retryStrategy);
        registerRequest.setCallbackParam(callbackParam);

        RegisterResult register = delayCallbackServerFacade.register(registerRequest);

        return register.getUid();
    }

    /**
     * 测试任意注册时间
     *
     * @return
     */
    public String testCompensation() {
        RegisterResult registerResult = new RegisterResult();
        ExecuteTemplate.execute(new AbstractExecutor() {
            @Override
            public void execute() {
                CallbackDetailPO po = new CallbackDetailPO();
                List<String> args = new ArrayList<>();
                args.add("hello");
                args.add("world");
                RetryStrategy retryStrategy = new RetryStrategy();
                retryStrategy.setInterval(10);
                retryStrategy.setType(1);
                retryStrategy.setTimes(3);
                CallbackParam callbackParam = new CallbackParam(args, 3);
                callbackParam.setRetryStrategy(retryStrategy);

                po.setCallbackParam(callbackParam);
                po.setGroup("dubbo-provider-demo2");
                po.setAlias("callbak01");
                po.setUid(registerService.generateUid(po.getGroup(), po.getAlias(), po.getCallbackParam().getArgs(), po.getCallbackParam().getIdempotentKey()));

                po.setNextExecTime(DateUtils.addSeconds(ContextHolder.getSysContext().getCurrentTime(), -60));
                dataService.add(po);

                registerResult.setUid(po.getUid());
            }

            @Override
            public void dealException(Exception e) {

            }
        });
        return registerResult.getUid();
    }
}
