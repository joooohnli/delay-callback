package com.johnli.callback.client.utils;

import java.util.Optional;

/**
 * @author qiang.li5  on 2018/1/12.
 */
public class EnvUtils {

    /**
     * deduce class of main
     *
     * @return
     */
    public static Class deduceMainClass() {
        try {
            StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                if ("main".equals(stackTraceElement.getMethodName())) {
                    return Class.forName(stackTraceElement.getClassName());
                }
            }
        } catch (ClassNotFoundException ex) {
            // Swallow and continue
        }
        return null;
    }


    /**
     * deduce package of main
     *
     * @return
     */
    public static String deduceMainClassPackage() {
        return Optional.ofNullable(deduceMainClass()).map((c) -> c.getPackage().getName()).orElse("");
    }
}
