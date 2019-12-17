/**
 * Copyright 2017-2025 Evergrande Group.
 */
package com.yunjian.modules.automat.service.impl;


import static com.yunjian.modules.automat.util.PaymentBusinessConstant.ALI_PAY;
import static com.yunjian.modules.automat.util.PaymentBusinessConstant.CHARSET;
import static com.yunjian.modules.automat.util.PaymentBusinessConstant.CHARSET_GBK;
import static com.yunjian.modules.automat.util.PaymentBusinessConstant.CHARSET_UTF8;
import static com.yunjian.modules.automat.util.PaymentBusinessConstant.MESSAGE_UNSUPPORT_SIGN_TYPE;
import static com.yunjian.modules.automat.util.PaymentBusinessConstant.RSA2_INSTANCE_NAME;
import static com.yunjian.modules.automat.util.PaymentBusinessConstant.RSA_INSTANCE_NAME;
import static com.yunjian.modules.automat.util.PaymentBusinessConstant.SIGN;
import static com.yunjian.modules.automat.util.PaymentBusinessConstant.SIGN_TYPE;
import static com.yunjian.modules.automat.util.PaymentBusinessConstant.SIGN_TYPE_HMAC;
import static com.yunjian.modules.automat.util.PaymentBusinessConstant.SIGN_TYPE_MD5;
import static com.yunjian.modules.automat.util.PaymentBusinessConstant.SIGN_TYPE_RSA;
import static com.yunjian.modules.automat.util.PaymentBusinessConstant.SIGN_TYPE_RSA2;
import static com.yunjian.modules.automat.util.PaymentBusinessConstant.WEI_XIN;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.yunjian.modules.automat.entity.WxConfigEntity;
import com.yunjian.modules.automat.exception.PaymentGatewayException;
import com.yunjian.modules.automat.service.SignatureService;
import com.yunjian.modules.automat.service.WxConfigService;
import com.yunjian.modules.automat.util.Base64Utils;
import com.yunjian.modules.automat.util.StreamUtil;

/**
 * 签名和验签算法
 *
 * @author gucunyang
 * @since 2018-02-08
 */
@Service("signatureService")
public class SignatureServiceImpl implements SignatureService {

    private static final Logger logger = LoggerFactory.getLogger(SignatureServiceImpl.class);
    
    @Autowired
	private WxConfigService wxConfigService;
    
    /**
     * 接收请求数据，按照微信提供的协议进行签名
     *
     * @param requestParamsMap 请求参数集合
     * @return 签名
     */
    @Override
    public String weixinSignature(Map requestParamsMap) {
        String sign = "";
        if (requestParamsMap == null) {
            logger.error("Params of method weixinSignature is null!");
            return sign;
        }
		WxConfigEntity config = wxConfigService.list().get(0);
        //微信商户密钥
        String weixinKey = config.getMchKey();
        String charset = "utf-8";
        StringBuilder content = getSignContent(requestParamsMap);
        content.append("&key=" + weixinKey);
        String signType = "MD5";
        if (SIGN_TYPE_MD5.equalsIgnoreCase(signType)) {
            sign = md5Sign(content.toString(), charset);
        } 
        return sign;
    }
    
    @Override
	public String weixinSignature(Map requestParamsMap, String weixinKey) {
        String sign = "";

        if (requestParamsMap == null) {
            logger.error("Params of method weixinSignature is null!");
            return sign;
        }
        String charset = (String) requestParamsMap.get(CHARSET);
        if (StringUtils.isEmpty(charset)) {
            charset = getCharset(WEI_XIN);
        }
        StringBuilder content = getSignContent(requestParamsMap);
        content.append("&key=" + weixinKey);
        String signType = (String) requestParamsMap.get(SIGN_TYPE);
        if (StringUtils.isEmpty(signType)) {
            signType = getSignType(WEI_XIN);
        }
        if (SIGN_TYPE_MD5.equalsIgnoreCase(signType)) {
            sign = md5Sign(content.toString(), charset);
        } else if (SIGN_TYPE_HMAC.equalsIgnoreCase(signType)) {
            sign = hmacSHA256Sign(content.toString(), weixinKey, charset);
        } else {
            logger.error(MESSAGE_UNSUPPORT_SIGN_TYPE + signType);
        }
        return sign;
    }
    
    /**
     * 接收返回数据，验证微信签名是否正确
     *
     * @param responseParamsMap 返回参数集合
     * @return 验签结果
     */
    @Override
    public boolean weixinSignatureCheck(Map responseParamsMap) {
        if (!checkParamIsEmpty(responseParamsMap)) {
            return false;
        }
        String sign = (String) responseParamsMap.get(SIGN);
        responseParamsMap.remove(SIGN);
        String signCheck = "";
		WxConfigEntity config = wxConfigService.list().get(0);
        //微信商户密钥
        String weixinKey = config.getMchKey();
        //存在sub_appid说明是服务商模式，则采用服务商的商户KEY进行签名
        if(responseParamsMap.get("sub_appid") != null && !responseParamsMap.get("sub_appid").toString().isEmpty()) {
        	signCheck = weixinSignature(responseParamsMap,weixinKey);
        }else {
        	signCheck = weixinSignature(responseParamsMap);
        }
        return sign.equals(signCheck);
    }

    /**
     * 接收请求数据，按照支付宝提供的协议进行签名
     *
     * @param requestParamsMap 请求参数集合
     * @return 签名
     */
    @Override
    public String alipaySignature(Map requestParamsMap) {
        String sign = "";

        if (requestParamsMap == null) {
            logger.error("Params of method alipaySignature is null!");
            return sign;
        }

        String content = getSignContent(requestParamsMap).toString();
        String signType = (String) requestParamsMap.get(SIGN_TYPE);
        if (StringUtils.isEmpty(signType)) {
            logger.error("sign_type is null");
            return sign;
        }
        String charset = (String) requestParamsMap.get(CHARSET);
        if (StringUtils.isEmpty(charset)) {
            charset = getCharset(ALI_PAY);
        }
        if (SIGN_TYPE_RSA2.equals(signType)) {
            sign = rsa256Sign(content, charset);
        } else if (SIGN_TYPE_RSA.equals(signType)) {
            sign = rsaSign(content, charset);
        } else {
            logger.error("Unsupported Signature Type: signType = " + signType);
        }

        return sign;
    }

    /**
     * 接收异步通知数据，验证支付宝签名是否正确
     *
     * @param responseParamsMap 返回参数集合
     * @return 验签结果
     */
    @Override
    public boolean alipaySignatureAsyCheck(Map responseParamsMap) {
        if (!checkParamIsEmpty(responseParamsMap)) {
            return false;
        }
        String sign = (String) responseParamsMap.get(SIGN);
        responseParamsMap.remove(SIGN);
        String signType = (String) responseParamsMap.get(SIGN_TYPE);
        if (StringUtils.isEmpty(signType)) {
            signType = getSignType(ALI_PAY);
        }
        responseParamsMap.remove(SIGN_TYPE);

        String content = getSignContent(responseParamsMap).toString();
        return getCheckResult(content, sign, signType);

    }

    /**
     * 接收同步返回数据，验证支付宝签名是否正确
     *
     * @param content 返回字符串
     * @param sign    签名
     * @return 验签结果
     */
    @Override
    public boolean alipaySignatureSynCheck(String content, String sign) {
        String signType = getSignType(ALI_PAY);
        return getCheckResult(content, sign, signType);
    }

    /**
     * 验证参数集合
     *
     * @param responseParamsMap 返回参数集合
     * @return 验证结果
     */
    private boolean checkParamIsEmpty(Map responseParamsMap) {
        if (responseParamsMap == null) {
            logger.error("Params of method weixinSignatureCheck is null!");
            return false;
        }

        String sign = (String) responseParamsMap.get(SIGN);
        if (StringUtils.isEmpty(sign)) {
            logger.error("sign is empty");
            return false;
        }
        return true;
    }

    /**
     * MD5签名
     *
     * @param content 验签字符串
     * @param charset 字符集
     * @return 签名
     */
    private String md5Sign(String content, String charset) {
        String sign;
        try {
            MessageDigest md5 = MessageDigest.getInstance(SIGN_TYPE_MD5);
            byte[] bytes = md5.digest(content.getBytes(charset));
            sign = encodeBytes(bytes);
        } catch (Exception e) {
            logger.error("MD5 Signature FAIL:" + e.getMessage());
            throw new PaymentGatewayException("MD5 Signature FAIL");
        }
        return sign;
    }

    /**
     * HmacSHA256签名
     *
     * @param content   验签字符串
     * @param weixinKey 微信密钥
     * @param charset   字符集
     * @return 签名
     */
    private String hmacSHA256Sign(String content, String weixinKey, String charset) {
        String sign;
        try {
            Mac hmacSHA256 = Mac.getInstance(SIGN_TYPE_HMAC);
            SecretKeySpec secretKey = new SecretKeySpec(weixinKey.getBytes(charset), SIGN_TYPE_HMAC);
            hmacSHA256.init(secretKey);
            byte[] bytes = hmacSHA256.doFinal(content.getBytes(charset));
            sign = encodeBytes(bytes);
        } catch (Exception e) {
            logger.error("HMAC-SHA256 Signature FAIL:" + e.getMessage());
            throw new PaymentGatewayException("HMAC-SHA256 Signature FAIL");
        }
        return sign;
    }

    /**
     * RSA2签名
     *
     * @param content 验签字符串
     * @param charset 字符集
     * @return 签名
     */
    private String rsa256Sign(String content, String charset) {
        String sign = null;
        try {
            PrivateKey privateK = getPrivateKey();
            Signature signature = Signature.getInstance(RSA2_INSTANCE_NAME);
            if (privateK == null) {
                return sign;
            }
            signature.initSign(privateK);
            signature.update(content.getBytes(charset));
            sign = Base64Utils.encode(signature.sign());
        } catch (Exception e) {
            logger.error("RSA2 Signature FAIL:" + e.getMessage());
            throw new PaymentGatewayException("RSA2 Signature FAIL");
        }
        return sign;
    }

    /**
     * RSA签名
     *
     * @param content 验签字符串
     * @param charset 字符集
     * @return 签名
     */
    private String rsaSign(String content, String charset) {
        String sign = null;
        try {
            Signature signature = Signature.getInstance(RSA_INSTANCE_NAME);
            PrivateKey privateK = getPrivateKey();
            if (privateK == null) {
                return sign;
            }
            signature.initSign(privateK);
            signature.update(content.getBytes(charset));
            sign = Base64Utils.encode(signature.sign());
        } catch (Exception e) {
            logger.error("RSA Signature FAIL:" + e.getMessage());
            throw new PaymentGatewayException("RSA Signature FAIL");
        }
        return sign;
    }

    /**
     * 获取待签名字符串
     *
     * @param requestParamsMap 请求参数集合
     * @return 验签字符串
     */
    private StringBuilder getSignContent(Map<String, Object> requestParamsMap) {
        StringBuilder content = new StringBuilder();
        List<String> keys = new ArrayList<>(requestParamsMap.keySet());

        Collections.sort(keys);

        int index = 0;
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = requestParamsMap.get(key) != null ? requestParamsMap.get(key).toString() : null;
            if (!StringUtils.isEmpty(value)) {
                content.append((index == 0 ? "" : "&") + key + "=" + value);
            }
            ++index;
        }
        return content;
    }

    /**
     * byte数组转换成字符串
     *
     * @param bytes 转换数组
     * @return String
     */
    private String encodeBytes(byte[] bytes) {
        StringBuilder hex = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString().toUpperCase();
    }

    /**
     * 获取privateKey
     *
     * @return privateKey
     */
    private PrivateKey getPrivateKey() {
        try {
        	//支付宝私钥
            String alipayAppPrivateKey = "";
            byte[] keyBytes = Base64Utils.decode(alipayAppPrivateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(SIGN_TYPE_RSA);
            return keyFactory.generatePrivate(pkcs8KeySpec);
        } catch (Exception e) {
            logger.error("Get Private Key FAIL:" + e.getMessage());
            throw new PaymentGatewayException("Get Private Key FAIL");
        }
    }

    /**
     * 获取字符集
     *
     * @param platform 支付平台
     * @return charset
     */
    private String getCharset(String platform) {
        String charset = "utf-8";
        if (StringUtils.isEmpty(charset)) {
            charset = ALI_PAY.equals(platform) ? CHARSET_GBK : CHARSET_UTF8;
        }
        return charset;
    }

    /**
     * 验证签名
     *
     * @param content         验签字符串
     * @param sign            签名
     * @param alipayPublicKey 支付宝公钥
     * @param charset         字符集
     * @param rsaInstanceName 签名方式
     * @return 验签结果
     */
    private boolean checkSign(String content, String sign, String alipayPublicKey,
                              String charset, String rsaInstanceName) {
        try {
            PublicKey pubKey = getPublicKeyFromX509(SIGN_TYPE_RSA,
                    new ByteArrayInputStream(alipayPublicKey.getBytes()));
            Signature signature = Signature.getInstance(rsaInstanceName);
            signature.initVerify(pubKey);
            signature.update(content.getBytes(charset));
            return signature.verify(Base64.decodeBase64(sign.getBytes()));
        } catch (Exception e) {
            logger.error("Signature Check ERROR:" + e.getMessage());
            throw new PaymentGatewayException("Signature Check ERROR");
        }
    }

    /**
     * 获取签名结果
     *
     * @param content  验签字符串
     * @param sign     签名
     * @param signType 签名方式
     * @return 验签结果
     */
    private boolean getCheckResult(String content, String sign, String signType) {
    	//支付宝公钥
        String alipayPublicKey = "";
        String charset = getCharset(ALI_PAY);
        if (SIGN_TYPE_RSA2.equals(signType)) {
            return checkSign(content, sign, alipayPublicKey, charset, RSA2_INSTANCE_NAME);
        } else if (SIGN_TYPE_RSA.equals(signType)) {
            return checkSign(content, sign, alipayPublicKey, charset, RSA_INSTANCE_NAME);
        } else {
            logger.error("Unsupported Signature Type: signType = " + signType);
            return false;
        }
    }

    /**
     * getSignTyoe
     *
     * @param platform 支付平台
     * @return 签名方式
     */
    private String getSignType(String platform) {
        String signType = "MD5";
        if (StringUtils.isEmpty(signType)) {
            signType = ALI_PAY.equals(platform) ? SIGN_TYPE_RSA2 : SIGN_TYPE_MD5;
        }
        return signType;
    }

    /**
     * getPublicKeyFromX509
     *
     * @param algorithm 签名方式
     * @param ins       InputStream
     * @return PublicKey
     */
    private PublicKey getPublicKeyFromX509(String algorithm, InputStream ins) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            StringWriter writer = new StringWriter();
            StreamUtil.io(new InputStreamReader(ins), writer);
            byte[] encodedKey = writer.toString().getBytes();
            encodedKey = Base64.decodeBase64(encodedKey);
            return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
        } catch (Exception e) {
            logger.error("Get Public Key FAIL:" + e.getMessage());
            throw new PaymentGatewayException("Get Public Key FAIL");
        }
    }

	

}
