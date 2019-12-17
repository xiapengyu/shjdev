package com.yunjian.modules.automat.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunjian.modules.automat.entity.WxAccountEntity;

import net.sf.json.JSONObject;

public class StrUtil {
	
	private static Logger logger = LoggerFactory.getLogger(StrUtil.class);
	
	public static String obj2String(Object obj) {
		if(obj == null ) {
			return "";
		}else {
			return obj.toString();
		}
	}
	
	public static String createAccountSign(String phone, BigDecimal userAmount, String userSignKey) {
		StringBuilder sb = new StringBuilder(phone);
		//余额取整加密，以防小数位数不一致导致签名不一致
		sb.append((userAmount.multiply(new BigDecimal(100))).setScale( 0, BigDecimal.ROUND_UP ).longValue()).append(userSignKey);
		try {
			String sign = MD5Util.md5Sign(sb.toString());
			return sign;
		} catch (Exception e) {
			logger.error("生成用户签名失败>>[{}]", e.getMessage());
		}
		return null;
	}
	
	public static boolean checkAccountSign(WxAccountEntity user, String userSignKey) {
		boolean flag = false;
		StringBuilder sb = new StringBuilder(user.getPhoneNo());
		//余额取整加密，以防小数位数不一致导致签名不一致
		sb.append(BigDecimal.valueOf(user.getAmount().doubleValue() * 100).setScale( 0, BigDecimal.ROUND_UP ).longValue()).append(userSignKey);
		try {
			String sign = MD5Util.md5Sign(sb.toString());
			logger.info("用户sign=" + user.getPhoneNo() + "; 校验sign=" + sign);
			flag = sign.equals(user.getSign());
		} catch (Exception e) {
			logger.error("生成用户签名失败>>[{}]", e.getMessage());
		}
		return flag;
	}
	
	public static String getJson2Xml(JSONObject json) {
        Iterator<String> it = json.keys();
        StringBuilder sb = new StringBuilder();
        String key = "";
        String value = "";
        sb.append("<xml>");
        while (it.hasNext()) {
            key = it.next();
            value = json.optString(key);
            if (!"null".equalsIgnoreCase(value)) {
                try {
                    JSONObject jsonSon = JSONObject.fromObject(value);
                    sb.append("<").append(key).append(">");
                    sb.append(getJson2Xml(jsonSon));
                    sb.append(sb.append("</").append(key).append(">"));
                } catch (Exception e) {
                    sb.append("<").append(key).append(">").append(value).append("</").append(key).append(">");
                }
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }
	
	public static Map<String, Object> transferXMLtoMap(String xmlString) {
        Map<String, Object> newMap = new HashMap<>();
        xmlString = xmlString.replaceAll("<\\!\\[CDATA\\[", "");
        xmlString = xmlString.replaceAll("\\]\\]>", "");
        xmlString = xmlString.replaceAll("</xml>", "");
        xmlString = xmlString.replaceAll("<xml>", "");
        logger.debug("=====>xmlString: " + xmlString);

        Pattern p = Pattern.compile("<([^</]+)>([^<]*)</([^</]+)>");
        Matcher m = p.matcher(xmlString);

        while (m.find()) {
            newMap.put(m.group(1), m.group(2));
        }

        logger.debug("====newMap: " + newMap);

        return newMap;
    }
	
	public static Map<String, Object> transferXMLtoMap(String xmlString, Map<String, Object> paramMap) {

        xmlString = xmlString.replaceAll("<\\!\\[CDATA\\[", "");
        xmlString = xmlString.replaceAll("\\]\\]>", "");
        xmlString = xmlString.replaceAll("</xml>", "");
        xmlString = xmlString.replaceAll("<xml>", "");
        logger.debug("=====>xmlString: " + xmlString);

        Pattern p = Pattern.compile("<([^</]+)>([^<]*)</([^</]+)>");
        Matcher m = p.matcher(xmlString);

        while (m.find()) {
            paramMap.put(m.group(1), m.group(2));
        }

        logger.debug("====paramMap: " + paramMap);
        return paramMap;
    }
	
	public static String randomCode() {
		int code = (int)((Math.random()*9+1)*100000);
		return code + "";
	}

}
