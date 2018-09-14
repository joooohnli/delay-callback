package com.johnli.callback.facade;

import com.johnli.callback.request.RegisterRequest;
import com.johnli.callback.request.UnRegisterRequest;
import com.johnli.callback.result.RegisterResult;
import com.johnli.callback.result.UnRegisterResult;

/**
 * @author johnli  2018-08-07 17:14
 */
public interface DelayCallbackServerFacade {
    RegisterResult register(RegisterRequest request);

    UnRegisterResult unRegister(UnRegisterRequest request);
}
