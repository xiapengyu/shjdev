package com.yunjian.modules.automat.util;

public class PaymentBusinessConstant {

    private PaymentBusinessConstant() {

    }

    public static final String WEI_XIN = "WEIXIN";

    public static final String ALI_PAY = "ALIPAY";

    public static final String KEY = "KEY";

    public static final String SIGN_TYPE_MD5 = "MD5";

    public static final String SIGN_TYPE_HMAC = "HmacSHA256";

    public static final String SIGN_TYPE_RSA = "RSA";

    public static final String SIGN_TYPE_RSA2 = "RSA2";

    public static final String RSA_INSTANCE_NAME = "SHA1WithRSA";

    public static final String RSA2_INSTANCE_NAME = "SHA256WithRSA";

    public static final String CHARSET_UTF8 = "utf-8";

    public static final String CHARSET_GBK = "GBK";

    public static final String CHARSET = "charset";

    public static final String SIGN_TYPE = "sign_type";

    public static final String SIGN = "sign";

    public static final String SUCCESS_CODE = "200";

    public static final String FAIL_CODE = "500";

    public static final String COMMON_FRAMEWORK_SUCCESS_CODE = "00000";

    public static final String SUCCESS_CODE_DESC = "200:返回数据正常";

    public static final String FAIL_MESSAGE = "500:支付网关：创建|查询订单出现异常";

    public static final String SUCCESS_MESSAGE = "SUCCESS";

    public static final String PACKAGE = "Sign=WXPay";

    public static final String MESSAGE_UNSUPPORT_SIGN_TYPE = "Unsupported Signature Type: ";

    public static final String QUERY = "query";

    public static final String TRADE_STATUS = "trade_status";

    public static final String TRADE_STATE = "trade_state";

    public static final String SUB_CODE = "sub_code";

    public static final String ACQ_TRADE_HAS_SUCCESS = "ACQ.TRADE_HAS_SUCCESS";

    public static final String NUM_ZERO = "0.00";

    public static final String OUT_TRADE_NO = "out_trade_no";

    public static final String TRADE_NO = "trade_no";

    public static final String OUT_REQUEST_NO = "out_request_no";

    public static final String COUPON_ID = "coupon_id_";

    public static final String COUPON_TYPE = "coupon_type_";

    public static final String COUPON_FEE = "coupon_fee_";

    public static final String REFUND_COUNT = "refund_count";

    public static final String OUT_REFUND_NO = "out_refund_no_";

    public static final String REFUND_ID = "refund_id_";

    public static final String REFUND_CHANNEL = "refund_channel_";

    public static final String REFUND_FEE = "refund_fee_";

    public static final String SETTLEMENT_REFUND_FEE = "settlement_refund_fee_";

    public static final String COUPON_REFUND_FEE = "coupon_refund_fee_";

    public static final String COUPON_REFUND_COUNT = "coupon_refund_count_";

    public static final String REFUND_STATUS = "refund_status_";

    public static final String REFUND_ACCOUNT = "refund_account_";

    public static final String REFUND_RECV_ACCOUNT = "refund_recv_accout_";

    public static final String REFUND_SUCCESS_TIME = "refund_success_time_";


    public static final String ALIPAY_FUND_CHANGE = "Y";

    public static final String RESULT_CODE_SUCCESS = "SUCCESS";

    public static final String RESULT_CODE_FAIL = "FAIL";

    public static final String RETURN_CODE_ERROR = "FAIL";

    public static final String ALIPAY_RETURN_CODE_SUCCESS = "10000";

    public static final String WEIXIN_URL = "WEIXIN-URL";

    public static final String WEIXIN_REFUND_URL = "WEIXIN-REFUND-URL";

    public static final String ALIPAY_URL = "ALIPAY-URL";

    public static final String ALIPAY_APP_PRIVATE = "ALIPAY-APP-PRIVATE";

    public static final String ALIPAY_PUBLIC = "ALIPAY-PUBLIC";

    public static final String METHOD = "method";

    public static final String FORMAT = "format";

    public static final String VERSION = "version";

    public static final String JSONDEL = "\":\"";

    public static final String CREATE_METHOD = "create";

    public static final String CREATE_REFUND_METHOD = "create_refund";

    public static final String RETURN_CODE = "return_code";

    public static final String TRADE_TYPE_APP = "APP";

    public static final String GET_OPENID_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
    
    /**
     * redis key 的前缀
     */
    public final static String PAYMENT_REDIS_KEY_PRE = "payment.gateway.config.";
    /**
     *12小时
     */
    public final static long PAYMENT_REDIS_EXPIRE = 12*60*60L;

}
