package com.johnli.callback.client.utils;

import java.util.Optional;

public class EnvUtils {

    /**
     * 推断启动main函数所在的class
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
     * 推断启动main函数所在的class的package
     *
     * @return
     */
    public static String deduceMainClassPackage() {
        return Optional.ofNullable(deduceMainClass()).map((c) -> c.getPackage().getName()).orElse("");
    }
}
