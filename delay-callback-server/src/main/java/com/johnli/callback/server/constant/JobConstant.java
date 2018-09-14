package com.johnli.callback.server.constant;

/**
 * @author johnli  2018-08-13 14:52
 */
public class JobConstant {

    /**
     * 扫描任务间隔
     */
    public static final int SCAN_JOB_GAP_MILLSEC = 500;
    /**
     * 补偿任务间隔
     */
    public static final int COMPENSATION_JOB_GAP_MILLSEC = 1000;
    /**
     * 监控任务间隔
     */
    public static final int MONITOR_JOB_GAP_MILLSEC = 60000;

    /**
     * 任务首次执行延时
     */
    public static final int JOB_DELAY_MILLSEC = 10000;

    /**
     * 扫描任务开始间隔
     */
    public static final int SCAN_START_GAP_MILLSEC = SCAN_JOB_GAP_MILLSEC;
    /**
     * 扫描任务结束间隔
     */
    public static final int SCAN_END_GAP_MILLSEC = SCAN_JOB_GAP_MILLSEC / 2;
    /**
     * 补偿任务最长时间范围，单位小时
     */
    public static final int MAX_COMPENSATION_HOURS = 240;

}
