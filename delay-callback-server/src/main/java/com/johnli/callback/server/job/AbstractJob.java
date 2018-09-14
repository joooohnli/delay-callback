package com.johnli.callback.server.job;

import com.johnli.callback.server.autoconfigure.property.CallbackProperties;
import com.johnli.callback.server.context.ContextHolder;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author johnli  2018-08-22 14:25
 */
public abstract class AbstractJob {
    @Autowired
    protected CallbackProperties callbackProperties;


    protected boolean isOn() {
        if (!callbackProperties.isCanRunJob()) {
            return false;
        }
        if (!callbackProperties.isLeaderMode()) {
            return true;
        }
        return ContextHolder.isLeader();
    }

    abstract void scheduled();
}
