package com.johnli.callback.service;

import com.johnli.callback.client.DelayCallback;
import com.johnli.callback.client.DelayCallbackHelper;
import com.johnli.callback.param.CallbackParam;
import com.johnli.callback.request.CallbackRequest;
import com.johnli.callback.result.RegisterResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author johnli  2018-09-17 15:53
 */
@Service
public class CallbackDemoService {
    public String test() {
        // params that will be delivered while callback
        List<String> params = new ArrayList<>();
        params.add("hello");
        params.add("world");

        // do register
        RegisterResult register = DelayCallbackHelper.register(new CallbackParam(params, 10), new DelayCallback() {
            @Override
            public String alias() {
                return "callback01";
            }

            @Override
            public boolean onCallback(CallbackRequest request) {
                // do whatever you want here

                return true;
            }
        });

        if (register.isSuccess()) {
            System.out.println("register successfully");

            // you can cancel before the callback being invoked.
            //DelayCallbackHelper.unRegister(register.getUid());
        }

        return register.getUid();
    }
}
