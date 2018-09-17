package com.johnli.callback.controller;

import com.johnli.callback.service.CallbackDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author johnli 2018-09-17 16:13
 */

@RestController
public class CallbackTestController {
    @Autowired
    private CallbackDemoService callbackDemoService;

    @RequestMapping(value = "/test")
    public String listCount() {
        return callbackDemoService.test();
    }
}