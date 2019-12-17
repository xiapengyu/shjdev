package com.yunjian;

import com.yunjian.modules.automat.util.QRCodeUtil;

public class QRCodeTest {
	
	public static void main(String[] args) {
		try {
			QRCodeUtil.createQRCode("http://localhost:8080/fast-admin?deviceCode=123&orderId=123", 1500, "D:\\logs2\\123.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/*String content = QRCodeUtil.parseQRCode("D:\\logs\\order.png");
		System.out.println(content);*/
	}

}
