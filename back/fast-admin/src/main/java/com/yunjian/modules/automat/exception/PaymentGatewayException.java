package com.yunjian.modules.automat.exception;

/**
 * 缴费网关自定义异常类
 *
 * @since 2018-03-01
 */
public class PaymentGatewayException extends CommonException {

    /**
     * @param message 错误信息
     */
    public PaymentGatewayException(String message) {
        super(message);
    }

    /**
     * @param code    错误码
     * @param message 错误信息
     */
    public PaymentGatewayException(String code, String message) {
        super(code, message);
    }
}
