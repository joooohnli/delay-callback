package com.johnli.callback.client;


import com.johnli.callback.request.CallbackRequest;

/**
 * <p>注意：若以<font color="red">匿名内部类</font>方式实现，方法体中禁止包含<font color="red">外部方法的局部变量</font>
 *
 * @author johnli  2018-08-06 14:49
 */
public interface DelayCallback {
    /**
     * 定义该回调别名。须保证应用内唯一
     *
     * @return 非空串
     */
    String alias();

    /**
     * 回调处理
     *
     * @param request 注册回调时的参数对象
     * @return true，完成回调；false，表示回调失败，将进行重试（如有可重试次数）
     */
    boolean onCallback(CallbackRequest request);
}
