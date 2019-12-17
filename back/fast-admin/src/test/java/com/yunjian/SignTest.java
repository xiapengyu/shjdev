package com.yunjian;

import java.math.BigDecimal;

public class SignTest {

	public static void main(String[] args) {
		String sign = createAccountSign("13411922990", BigDecimal.valueOf(0.02), "EGDBGFDS5612768UGV90");
		System.out.println(sign);
	}
	
	public static String createAccountSign(String phone, BigDecimal userAmount, String userSignKey) {
		StringBuilder sb = new StringBuilder(phone);
		//余额取整加密，以防小数位数不一致导致签名不一致
		sb.append((userAmount.multiply(new BigDecimal(100))).setScale( 0, BigDecimal.ROUND_UP ).longValue()).append(userSignKey);
		try {
			String sign = MD5Util.md5Sign(sb.toString());
			return sign;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
