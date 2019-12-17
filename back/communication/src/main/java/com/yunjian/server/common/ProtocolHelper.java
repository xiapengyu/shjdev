package com.yunjian.server.common;

import com.yunjian.server.dto.ProtocolBody;

public class ProtocolHelper {
	
	public static ProtocolBody respHeartbeat() {
		ProtocolBody body = new ProtocolBody();
		body.setCardCode((byte)0x02);
		body.setType((byte)0x10);
		body.setLength((short)7);
		body.setCtrlCode((byte)0x01);
		body.setEnd((short)3338);
		return body;
	}
	
	public static ProtocolBody respDeviceCode() {
		ProtocolBody body = new ProtocolBody();
		body.setCardCode((byte)0x02);
		body.setType((byte)0x20);
		body.setLength((short)7);
		body.setCtrlCode((byte)0x01);
		body.setEnd((short)3338);
		return body;
	}
	
	public static ProtocolBody reboot() {
		ProtocolBody body = new ProtocolBody();
		body.setCardCode((byte)0x02);
		body.setType((byte)0x30);
		body.setLength((short)7);
		body.setCtrlCode((byte)0x02);
		body.setEnd((short)3338);
		return body;
	}
	
	public static ProtocolBody configPassword(String password) {
		byte[] passwordByte = password.getBytes();
		ProtocolBody body = new ProtocolBody();
		body.setCardCode((byte)0x02);
		body.setType((byte)0x40);
		body.setLength((short)(7 + passwordByte.length));
		body.setCtrlCode((byte)0x03);
		body.setData(passwordByte);
		body.setEnd((short)3338);
		return body;
	}
	
	public static ProtocolBody configTestPassword(String password) {
		byte[] passwordByte = password.getBytes();
		ProtocolBody body = new ProtocolBody();
		body.setCardCode((byte)0x02);
		body.setType((byte)0x40);
		body.setLength((short)(7 + passwordByte.length));
		body.setCtrlCode((byte)0x05);
		body.setData(passwordByte);
		body.setEnd((short)3338);
		return body;
	}

}
