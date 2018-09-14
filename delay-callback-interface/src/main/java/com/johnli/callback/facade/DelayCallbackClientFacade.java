package com.johnli.callback.facade;

import com.johnli.callback.request.CallbackRequest;
import com.johnli.callback.result.CallbackResult;

/**
 * @author johnli  2018-08-06 18:52
 */
public interface DelayCallbackClientFacade {
    CallbackResult callback(CallbackRequest callbackRequest);
}
