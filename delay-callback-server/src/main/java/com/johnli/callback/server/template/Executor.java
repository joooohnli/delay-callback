package com.johnli.callback.server.template;

/**
 * @author johnli  2018-08-13 18:07
 */
public interface Executor {
    void before();
    void execute();
    void after();
    void dealException(Exception e);
}
