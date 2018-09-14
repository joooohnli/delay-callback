package com.johnli.callback.server.template;

/**
 * @author johnli  2018-08-13 17:58
 */
public class ExecuteTemplate {

    public static void execute(Executor rExecutor) {
        try {
            rExecutor.before();

            rExecutor.execute();

        } catch (Exception e) {
            rExecutor.dealException(e);
        } finally {
            rExecutor.after();
        }
    }

}
