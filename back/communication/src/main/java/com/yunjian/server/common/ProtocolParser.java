package com.yunjian.server.common;

import com.yunjian.server.dto.StatusCode2;
import com.yunjian.server.dto.StatusCode3;
import com.yunjian.server.dto.VersionSupportCode1;
import com.yunjian.server.util.ByteUtils;

public class ProtocolParser {
	
	/**
	 * 包装状态码第二个字节
	 * @param data
	 * @return
	 */
	public static StatusCode2 packagingStatusCode2(byte data) {
		StatusCode2 code2 = new StatusCode2();
		String binStr = ByteUtils.byteToBinaryString(data);
		code2.setBit0(Integer.parseInt(""+binStr.charAt(0)));
		code2.setBit1(Integer.parseInt(""+binStr.charAt(1)));
		code2.setBit2(Integer.parseInt(""+binStr.charAt(2)));
		code2.setBit3(Integer.parseInt(""+binStr.charAt(3)));
		code2.setBit4(Integer.parseInt(""+binStr.charAt(4)));
		code2.setBit5(Integer.parseInt(""+binStr.charAt(5)));
		code2.setBit6(Integer.parseInt(""+binStr.charAt(6)));
		code2.setBit7(Integer.parseInt(""+binStr.charAt(7)));
		return code2;
	}

	/**
	 * 包装状态码第三个字节
	 * @param data
	 * @return
	 */
	public static StatusCode3 packagingStatusCode3(byte data) {
		StatusCode3 code3 = new StatusCode3();
		String binStr = ByteUtils.byteToBinaryString(data);
		code3.setBit0(Integer.parseInt(""+binStr.charAt(0)));
		code3.setBit1(Integer.parseInt(""+binStr.charAt(1)));
		code3.setBit2(Integer.parseInt(""+binStr.charAt(2)));
		code3.setBit3(Integer.parseInt(""+binStr.charAt(3)));
		code3.setBit4(Integer.parseInt(""+binStr.charAt(4)));
		code3.setBit5(Integer.parseInt(""+binStr.charAt(5)));
		code3.setBit6(Integer.parseInt(""+binStr.charAt(6)));
		code3.setBit7(Integer.parseInt(""+binStr.charAt(7)));
		return code3;
	}
	
	/**
	 * 包装状态码第三个字节
	 * @param data
	 * @return
	 */
	public static VersionSupportCode1 packagingSupportCode1(byte data) {
		VersionSupportCode1 code1 = new VersionSupportCode1();
		String binStr = ByteUtils.byteToBinaryString(data);
		code1.setBit0(Integer.parseInt(""+binStr.charAt(0)));
		code1.setBit1(Integer.parseInt(""+binStr.charAt(1)));
		code1.setBit2(Integer.parseInt(""+binStr.charAt(2)));
		code1.setBit3(Integer.parseInt(""+binStr.charAt(3)));
		code1.setBit4(Integer.parseInt(""+binStr.charAt(4)));
		code1.setBit5(Integer.parseInt(""+binStr.charAt(5)));
		code1.setBit6(Integer.parseInt(""+binStr.charAt(6)));
		code1.setBit7(Integer.parseInt(""+binStr.charAt(7)));
		return code1;
	}

}
