package com.johnli.callback.server.dao.po;


import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;

/**
 * @author johnli  2018-08-23 10:38
 */
public class PageResult<T> {
    private int cursor;
    private List<T> results;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public int getCursor() {
        return cursor;
    }

    public PageResult<T> setCursor(int cursor) {
        this.cursor = cursor;
        return this;
    }

    public List<T> getResults() {
        return results;
    }

    public PageResult<T> setResults(List<T> results) {
        this.results = results;
        return this;
    }
}
