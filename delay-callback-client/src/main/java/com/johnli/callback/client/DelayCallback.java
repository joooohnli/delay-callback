package com.johnli.callback.client;


import com.johnli.callback.request.CallbackRequest;

/**
 * <p>Note：if the implementation is realised through <font color="red">anonymous inner class</font>, it's method can not include
 * <font color="red">local variables of outer class</font>.
 *
 * @author johnli  2018-08-06 14:49
 */
public interface DelayCallback {
    /**
     * define alias of callback, you should make sure it is unique in your current application.
     *
     * @return not empty
     */
    String alias();

    /**
     * dealing with callback
     *
     * @param request params registered
     * @return true: callback complete;
     * false: callback failed, will retry if has available retry times.
     */
    boolean onCallback(CallbackRequest request);
}
