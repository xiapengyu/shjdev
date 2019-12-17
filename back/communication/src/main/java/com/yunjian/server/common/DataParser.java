package com.yunjian.server.common;

import java.io.UnsupportedEncodingException;

import com.yunjian.server.dto.DeviceStatusDto;
import com.yunjian.server.dto.GoodsOutRespDto;
import com.yunjian.server.dto.SendAdRespDto;
import com.yunjian.server.dto.SetDeviceLightRespDto;
import com.yunjian.server.dto.SetDeviceTempRespDto;
import com.yunjian.server.util.ByteUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class DataParser {

	/**
	 * 解析设备状态
	 * @param data
	 * @return
	 */
	public static DeviceStatusDto parseDeviceStatus(byte[] data) {
		ByteBuf byteBuf = Unpooled.copiedBuffer(data); 
		DeviceStatusDto dto = new DeviceStatusDto();
		
		byte[] lockerNoBytes = new byte[2];
		byteBuf.readBytes(lockerNoBytes);
		dto.setLockerNo( ByteUtils.getShort(lockerNoBytes, 0) );
		
		byte[] tempBytes = new byte[2];
		byteBuf.readBytes(tempBytes);
		dto.setTemp( ByteUtils.getShort(tempBytes, 0) );
		
		byte[] lampBytes = new byte[2];
		byteBuf.readBytes(lampBytes);
		dto.setLamp( ByteUtils.getShort(lampBytes, 0) );
		
		byte[] defogStatusBytes = new byte[2];
		byteBuf.readBytes(defogStatusBytes);
		dto.setDefogStatus(ByteUtils.getShort(defogStatusBytes, 0));
		
		byte[] doorStatusBytes = new byte[2];
		byteBuf.readBytes(doorStatusBytes);
		dto.setDoorStatus(ByteUtils.getShort(doorStatusBytes, 0));
		
		byte[] faultCodeBytes = new byte[2];
		byteBuf.readBytes(faultCodeBytes);
		dto.setFaultCode(ByteUtils.getShort(faultCodeBytes, 0));
		
		byte[] faultTimeBytes = new byte[2];
		byteBuf.readBytes(faultTimeBytes);
		dto.setFaultTime( ByteUtils.getShort(faultTimeBytes, 0) );
		
		byte[] tempCtrlStatusBytes = new byte[2];
		byteBuf.readBytes(tempCtrlStatusBytes);
		dto.setTempCtrlStatus(  ByteUtils.getShort(tempCtrlStatusBytes, 0) );
		
		byte[] versionBytes = new byte[2];
		byteBuf.readBytes(versionBytes);
		dto.setVersion( ByteUtils.getShort(versionBytes, 0) );
		
		byte[] settedTempBytes = new byte[2];
		byteBuf.readBytes(settedTempBytes);
		dto.setSettedTemp( ByteUtils.getShort(settedTempBytes, 0) );
		
		dto.setSignal( byteBuf.readByte() );
		
		byte[] appVersionBytes = new byte[2];
		byteBuf.readBytes(appVersionBytes);
		dto.setAppVersion( ByteUtils.getShort(appVersionBytes, 0) );
		
		byte[] deviceCodeBytes = new byte[50];
		byteBuf.readBytes(deviceCodeBytes);
		try {
			String deviceCode = new String(deviceCodeBytes,"utf-8").trim();
			dto.setDeviceCode( deviceCode );
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byteBuf.release();
		return dto;
	}

	/**
	 * 解析温度设置结果
	 * @param data
	 * @return
	 */
	public static SetDeviceTempRespDto parseTempConfigResult(byte[] data) {
		ByteBuf byteBuf = Unpooled.copiedBuffer(data); 
		SetDeviceTempRespDto dto = new SetDeviceTempRespDto();
		byte[] deviceCodeBytes = new byte[50];
		byteBuf.readBytes(deviceCodeBytes);
		String deviceCode;
		try {
			deviceCode = new String(deviceCodeBytes,"utf-8").trim();
			dto.setDeviceCode( deviceCode );
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		dto.setResult(byteBuf.readByte());
		byteBuf.release();
		return dto;
	}

	public static SetDeviceLightRespDto parseLightConfigResult(byte[] data) {
		ByteBuf byteBuf = Unpooled.copiedBuffer(data); 
		SetDeviceLightRespDto dto = new SetDeviceLightRespDto();
		byte[] deviceCodeBytes = new byte[50];
		byteBuf.readBytes(deviceCodeBytes);
		String deviceCode;
		try {
			deviceCode = new String(deviceCodeBytes,"utf-8").trim();
			dto.setDeviceCode( deviceCode );
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		dto.setResult(byteBuf.readByte());
		byteBuf.release();
		return dto;
	}

	public static SendAdRespDto parseSendAdResult(byte[] data) {
		ByteBuf byteBuf = Unpooled.copiedBuffer(data); 
		SendAdRespDto dto = new SendAdRespDto();
		byte[] deviceCodeBytes = new byte[50];
		byteBuf.readBytes(deviceCodeBytes);
		String deviceCode;
		try {
			deviceCode = new String(deviceCodeBytes,"utf-8").trim();
			dto.setDeviceCode( deviceCode );
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		dto.setResult(byteBuf.readByte());
		byteBuf.release();
		return dto;
	}

	public static GoodsOutRespDto parseGoodsOutResult(byte[] data) {
		ByteBuf byteBuf = Unpooled.copiedBuffer(data); 
		GoodsOutRespDto dto = new GoodsOutRespDto();
		dto.setTradeType(byteBuf.readByte());
		
		byte[] orderNoChangedByte = new byte[2];
		byteBuf.readBytes(orderNoChangedByte);
		dto.setOrderNoChanged( ByteUtils.getShort(orderNoChangedByte, 0) );
		dto.setFlag( byteBuf.readByte() );
		
		byte[] timeByte = new byte[7];
		byteBuf.readBytes(timeByte);
		
		byte[] deviceCode = new byte[50];
		byteBuf.readBytes(deviceCode);
		try {
			dto.setTime( ByteUtils.bcd2Str(timeByte) );
			dto.setDeviceCode( new String(deviceCode,"utf-8").trim() );
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return dto;
	}

}
