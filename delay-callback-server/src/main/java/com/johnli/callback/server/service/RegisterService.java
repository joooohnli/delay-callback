package com.johnli.callback.server.service;

import com.johnli.callback.request.RegisterRequest;

import java.util.List;

/**
 * @author johnli  2018-08-09 18:30
 */
public interface RegisterService {

    String register(RegisterRequest request);

    boolean unRegister(String uid);

    String generateUid(String group, String alias, List<String> args, String idempotentKey);

}
