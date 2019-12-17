package com.yunjian.common.utils;

public class ByteUtils {

	/**
	 * 16进制字符串转byte数组
	 * @param hex
	 * @return
	 */
	public static byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	private static byte toByte(char c) {
		byte b = (byte) "0123456789abcdef".indexOf(c);
		return b;
	}
	
	/**
	 * byte转二进制字符串
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
	    * @param src byte数组  
	    * @param offset 从数组的第offset位开始  
	    * @return int数值  
	    */    
	public static int bytesToInt(byte[] src, int offset) {  
	    int value;    
	    value = (int) ((src[offset] & 0xFF)   
	            | ((src[offset+1] & 0xFF)<<8)   
	            | ((src[offset+2] & 0xFF)<<16)   
	            | ((src[offset+3] & 0xFF)<<24));  
	    return value;  
	}  
	  
	 /**  
	    * byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序。和intToBytes2（）配套使用 
	    */  
	public static int bytesToInt2(byte[] src, int offset) {  
	    int value;    
	    value = (int) ( ((src[offset] & 0xFF)<<24)  
	            |((src[offset+1] & 0xFF)<<16)  
	            |((src[offset+2] & 0xFF)<<8)  
	            |(src[offset+3] & 0xFF));  
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
	
	/** 获取指令异或值
     * @param datas
     * @return
     */
    public static int getBCC(byte[] datas) {
        int temp = datas[0];              // 此处首位取1是因为本协议中第一个数据不参数异或校验，转为int防止结果出现溢出变成负数

        for (int i = 1; i < datas.length-1; i++) {
            int iData;
            if (datas[i] < 0) {
                iData = datas[i] & 0xff;      // 变为正数计算
            } else {
                iData = datas[i];
            }
            if (temp < 0) {
                temp = temp & 0xff;          // 变为正数
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

}
