package com.johnli.callback.server.constant;

/**
 * @author johnli  2018-08-13 14:52
 */
public class JobConstant {

    /**
     * interval of scanning job
     */
    public static final int SCAN_JOB_INTERVAL_MILLSEC = 500;
    /**
     * interval of compensation job
     */
    public static final int COMPENSATION_JOB_INTERVAL_MILLSEC = 1000;
    /**
     * interval of monitor job
     */
    public static final int MONITOR_JOB_INTERVAL_MILLSEC = 60000;

    /**
     * interval for fist run of all jobs
     */
    public static final int JOB_DELAY_MILLSEC = 10000;

    /**
     * gap of head of the scanning time slot
     */
    public static final int SCAN_START_GAP_MILLSEC = SCAN_JOB_INTERVAL_MILLSEC;
    /**
     * gap of tail of the scanning time slot
     */
    public static final int SCAN_END_GAP_MILLSEC = SCAN_JOB_INTERVAL_MILLSEC / 2;
    /**
     * max gap of compensation job's scanning time slot. unit: hour
     */
    public static final int MAX_COMPENSATION_HOURS = 240;

}
