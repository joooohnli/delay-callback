package com.johnli.callback.server.context;

import com.johnli.callback.common.CallbackException;
import com.johnli.callback.server.context.biz.BizContext;
import org.springframework.context.ApplicationContext;

/**
 * @author johnli  2018-08-09 17:22
 */
public class ContextHolder {
    private static ApplicationContext applicationContext;

    private static boolean leader;

    private static ThreadLocal<SysContext> sysContextThreadLocal = new InheritableThreadLocal<SysContext>() {

        @Override
        protected SysContext childValue(SysContext parentValue) {
            try {
                return (SysContext) parentValue.clone();
            } catch (CloneNotSupportedException e) {
                throw new CallbackException("can not copy SysContext from parent thread", e);
            }
        }
    };

    private static ThreadLocal<BizContext> bizContextThreadLocal = new InheritableThreadLocal<>();

    public static <T extends BizContext> T getBizContext() {
        return (T) bizContextThreadLocal.get();
    }

    public static void setBizContext(BizContext bizContext) {
        bizContextThreadLocal.set(bizContext);
    }

    public static SysContext getSysContext() {
        if (sysContextThreadLocal == null) {
            throw new CallbackException("sysContextThreadLocal is null");
        }

        return sysContextThreadLocal.get();
    }

    public static void setCallbackContext(SysContext sysContext) {
        sysContextThreadLocal.set(sysContext);
    }

    public static void removeContext() {
        SysContext sysContext = sysContextThreadLocal.get();
        if (sysContext != null) {
            sysContext.remove();
        }
        sysContextThreadLocal.remove();
        bizContextThreadLocal.remove();
    }


    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        ContextHolder.applicationContext = applicationContext;
    }

    public static boolean isLeader() {
        return leader;
    }

    public static void setLeader(boolean leader) {
        ContextHolder.leader = leader;
    }
}
