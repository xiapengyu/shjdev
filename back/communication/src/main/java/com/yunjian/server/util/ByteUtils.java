package com.yunjian.server.util;

import java.nio.ByteBuffer;
import java.util.Calendar;

public class ByteUtils {

	/**
	 * 16进制字符串转byte数组
	 * 
	 * @param hex
	 * @return
	 */
	public static byte[] hexStringToByte(String hex) {
		String hexstring = hex;
		byte[] destByte = new byte[hexstring.length() / 2];
		int j = 0;
		for (int i = 0; i < destByte.length; i++) {
			byte high = (byte) (Character.digit(hexstring.charAt(j), 16) & 0xff);
			byte low = (byte) (Character.digit(hexstring.charAt(j + 1), 16) & 0xff);
			destByte[i] = (byte) (high << 4 | low);
			j += 2;
		}
		return destByte;
	}

	private static byte toByte(char c) {
		byte b = (byte) "0123456789abcdef".indexOf(c);
		return b;
	}

	/**
	 * byte转二进制字符串
	 * 
	 * @param num
	 * @return
	 */
	public static String byteToBinaryString(byte byt) {
		String bStr = Integer.toBinaryString((byt & 0xFF) + 0x100).substring(1);
		return bStr;

	}

	/**
	 * 二进制字符串转换为byte数组,每个字节以","隔开
	 **/
	public static byte[] conver2HexToByte(String hex2Str) {
		String[] temp = hex2Str.split(",");
		byte[] b = new byte[temp.length];
		for (int i = 0; i < b.length; i++) {
			b[i] = Long.valueOf(temp[i], 2).byteValue();
		}
		return b;
	}

	/**
	 * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和intToBytes（）配套使用 [大端]
	 * 
	 * @param src    byte数组
	 * @param offset 从数组的第offset位开始
	 * @return int数值
	 */
	public static int bytesToInt(byte[] src, int offset) {
		int value;
		value = (int) ((src[offset] & 0xFF) | ((src[offset + 1] & 0xFF) << 8) | ((src[offset + 2] & 0xFF) << 16)
				| ((src[offset + 3] & 0xFF) << 24));
		return value;
	}

	/**
	 * byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序。和intToBytes2（）配套使用
	 */
	public static int bytesToInt2(byte[] src, int offset) {
		int value;
		value = (int) (((src[offset] & 0xFF) << 24) | ((src[offset + 1] & 0xFF) << 16) | ((src[offset + 2] & 0xFF) << 8)
				| (src[offset + 3] & 0xFF));
		return value;
	}

	/**
	 * 二进制字符串转换为byte数组,每个字节以","隔开
	 **/
	public static byte[] binStrToByteArr(String binStr) {
		String[] temp = binStr.split(",");
		byte[] b = new byte[temp.length];
		for (int i = 0; i < b.length; i++) {
			b[i] = Long.valueOf(temp[i], 2).byteValue();
		}
		return b;
	}

	/**
	 * 将二进制转换成16进制.
	 * 
	 * @param buf defualt
	 * @return
	 */
	public static String parseByte2HexStr(byte[] buf) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 获取指令异或值
	 * 
	 * @param datas
	 * @return
	 */
	public static int getBCC(byte[] datas) {
		int temp = datas[0]; // 此处首位取1是因为本协议中第一个数据不参数异或校验，转为int防止结果出现溢出变成负数

		for (int i = 1; i < datas.length - 1; i++) {
			int iData;
			if (datas[i] < 0) {
				iData = datas[i] & 0xff; // 变为正数计算
			} else {
				iData = datas[i];
			}
			if (temp < 0) {
				temp = temp & 0xff; // 变为正数
			}
			temp ^= iData;

		}

		return temp;
	}

	public static int byteToInt(byte b) {
		// Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值
		return b & 0xFF;
	}

	/**
	 * byte数组转对应的16进制字符串
	 * 
	 * @param byteArray
	 * @return
	 */
	public static String toHexString(byte[] byteArray) {
		String str = null;
		if (byteArray != null && byteArray.length > 0) {
			StringBuffer stringBuffer = new StringBuffer(byteArray.length);
			for (byte byteChar : byteArray) {
				stringBuffer.append(String.format("%02X", byteChar));
			}
			str = stringBuffer.toString();
		}
		return str;
	}

	/**
	 * 从byte数组的index处的连续两个字节获得一个short
	 * 
	 * @param arr
	 * @param index byte数组的起始位置
	 * @return
	 */
	public static short getShort(byte[] arr, int index) {
		return (short) (0xff00 & arr[index] << 8 | (0xff & arr[index + 1]));
	}

	/**
	 * 注释：short到字节数组的转换！ 大短序
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] short2ByteBigend(short x) {
		byte high = (byte) (0x00FF & (x >> 8));// 定义第一个byte
		byte low = (byte) (0x00FF & x);// 定义第二个byte
		byte[] bytes = new byte[2];
		bytes[0] = high;
		bytes[1] = low;
		return bytes;
	}

	/**
	 * 获取6位字节的时间戳
	 * 
	 * @return
	 */
	public static byte[] get6ByteTimeStamp() {
		byte[] timeByte = new byte[6];
		Calendar a = Calendar.getInstance();
		int year = a.get(Calendar.YEAR);// 得到年
		int month = a.get(Calendar.MONTH) + 1;// 由于月份是从0开始的所以加1
		int day = a.get(Calendar.DATE);
		int hour = a.get(Calendar.HOUR_OF_DAY);
		int minute = a.get(Calendar.MINUTE);
		int second = a.get(Calendar.SECOND);
		timeByte[0] = (byte) year;
		timeByte[1] = (byte) month;
		timeByte[2] = (byte) day;
		timeByte[3] = (byte) hour;
		timeByte[4] = (byte) minute;
		timeByte[5] = (byte) second;
		return timeByte;
	}

	/**
	 * @功能: BCD码转为10进制串(阿拉伯数据)
	 * @参数: BCD码
	 * @结果: 10进制串
	 */
	public static String bcd2Str(byte[] bytes) {
		StringBuffer temp = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
			temp.append((byte) (bytes[i] & 0x0f));
		}
		return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp.toString().substring(1) : temp.toString();
	}

	/**
	 * @功能: 10进制串转为BCD码
	 * @参数: 10进制串
	 * @结果: BCD码
	 */
	public static byte[] str2Bcd(String asc) {
		int len = asc.length();
		int mod = len % 2;
		if (mod != 0) {
			asc = "0" + asc;
			len = asc.length();
		}
		byte abt[] = new byte[len];
		if (len >= 2) {
			len = len / 2;
		}
		byte bbt[] = new byte[len];
		abt = asc.getBytes();
		int j, k;
		for (int p = 0; p < asc.length() / 2; p++) {
			if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
				j = abt[2 * p] - '0';
			} else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
				j = abt[2 * p] - 'a' + 0x0a;
			} else {
				j = abt[2 * p] - 'A' + 0x0a;
			}
			if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
				k = abt[2 * p + 1] - '0';
			} else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
				k = abt[2 * p + 1] - 'a' + 0x0a;
			} else {
				k = abt[2 * p + 1] - 'A' + 0x0a;
			}
			int a = (j << 4) + k;
			byte b = (byte) a;
			bbt[p] = b;
		}
		return bbt;
	}

}
