package com.yunjian.server.service;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.yunjian.server.common.Constant;
import com.yunjian.server.common.DataParser;
import com.yunjian.server.common.LocalStore;
import com.yunjian.server.common.ProtocolHelper;
import com.yunjian.server.common.RestTemplateComponent;
import com.yunjian.server.dto.DeviceStatusDto;
import com.yunjian.server.dto.GoodsOutRespDto;
import com.yunjian.server.dto.ProtocolBody;
import com.yunjian.server.dto.SendAdRespDto;
import com.yunjian.server.dto.SetDeviceLightRespDto;
import com.yunjian.server.dto.SetDeviceTempRespDto;
import com.yunjian.server.util.ByteUtils;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CtrlService {
	
	@Autowired
	private RestTemplateComponent restTemplateComponent;
	
	@Value("${device.online.switch.api}")
	private String onlineSwitchApi;
	
	@Value("${device.password.save.api}")
	private String passwordSaveApi;
	
	@Value("${device.upload.goods.result}")
	private String uploadGoodsResultApi;

	/**
	 * 接收设备上报的机器编号
	 * @param ctx
	 * @param body
	 * @throws Exception
	 */
	public void handleReceiveDeviceCode(ChannelHandlerContext ctx, ProtocolBody body) throws Exception {
		String data = new String(body.getData(),"UTF-8").trim();
		log.info("数据域: {}", data);
		if(data == null || "".equals(data)) {
			log.error("接收设备上报的机器编号为空，无法完成上报");
			return;
		}
		LocalStore.getInstance().getCtxMap().put(data, ctx);

		//返回贞
		ctx.writeAndFlush(ProtocolHelper.respDeviceCode());
		log.info("响应设备({})[上报机器编号]:  {}", data,Constant.DEVICE_CODE_RESP);

		Map<String, Object> params = Maps.newHashMap();
		params.put("deviceCode", data);
		params.put("onlineStatus", 0);
		String result = restTemplateComponent.restTemplatePOST(params, onlineSwitchApi);
		log.info("向平台上报在线，结果：{}", result);
	}

	public void handleHeartbeat(ChannelHandlerContext ctx,String deviceCode) {
		ctx.writeAndFlush(ProtocolHelper.respHeartbeat());
		log.info("响应设备({})[心跳]:  {}", deviceCode, ByteUtils.toHexString(Constant.HEARTBEAT_RESP));
	}

	/**
	 * 处理设备上报的状态
	 * @param ctx
	 * @param body
	 */
	public void handleDeviceStatus(ChannelHandlerContext ctx, ProtocolBody body) {
		DeviceStatusDto deviceStatus =  DataParser.parseDeviceStatus(body.getData());
		log.info("解析出设备({})状态信息：{}" ,body.getDeviceCode(), deviceStatus.toString());

	}

	public void handleRebootResult(ProtocolBody body) {
		short result = ByteUtils.getShort(body.getData(), 0);
		log.info("解析出设备({})重启结果：{}" ,body.getDeviceCode(), result);
	}

	public void handleTempConfigResult(ProtocolBody body) {
		SetDeviceTempRespDto dto = DataParser.parseTempConfigResult(body.getData());
		log.info("解析出设备({})远程设置温度结果：{}" ,body.getDeviceCode(), dto);
	}

	public void handleLightConfigResult(ProtocolBody body) {
		SetDeviceLightRespDto dto = DataParser.parseLightConfigResult(body.getData());
		log.info("解析出设备({})远程设置灯光结果：{}" , body.getDeviceCode(), dto);
	}

	public void handleSendAdResult(ProtocolBody body) {
		SendAdRespDto dto = DataParser.parseSendAdResult(body.getData());
		log.info("解析出下发广告结果：{}" , dto);
	}

	public void handleGoodsOutResult(ProtocolBody body) {
		GoodsOutRespDto dto = DataParser.parseGoodsOutResult(body.getData());
		log.info("解析出货响应结果并：{}" , dto);
		
//已经由app直接上报至平台，不在此上报出货结果
//		int goodsResult = 0;
//		if(dto.getTradeType()==(byte)0x03) {
//			goodsResult = 0;
//		}else {
//			goodsResult = 1;
//		}
//		Map<String, Object> params = Maps.newHashMap();
//		params.put("result", goodsResult);
//		params.put("orderNoChanged", dto.getOrderNoChanged());
//		params.put("deviceCode", dto.getDeviceCode());
//		String result = restTemplateComponent.restTemplatePOST(params, uploadGoodsResultApi);
//		log.info("向平台上报管理页密码，结果：{}", result);
	}

	public void handlePasswordResult(ProtocolBody body) {
		try {
			String data = new String(body.getData(),"utf-8");
			log.info("解析出设置管理页密码上传的结果：{}" , data);
			
			String pass[] = data.split(",");
			Map<String, Object> params = Maps.newHashMap();
			params.put("deviceCode", pass[0].trim());
			params.put("password", pass[1]);
			params.put("passwordType", 0);
			String result = restTemplateComponent.restTemplatePOST(params, passwordSaveApi);
			log.info("向平台上报管理页密码，结果：{}", result);
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(),e);
		}
	}

	public void handleTestPasswordResult(ProtocolBody body) {
		try {
			String data = new String(body.getData(),"utf-8");
			log.info("解析出设置测试页密码上传的结果：{}" , data);
			
			String pass[] = data.split(",");
			Map<String, Object> params = Maps.newHashMap();
			params.put("deviceCode", pass[0].trim());
			params.put("password", pass[1]);
			params.put("passwordType", 1);
			String result = restTemplateComponent.restTemplatePOST(params, passwordSaveApi);
			log.info("向平台上报测试页密码，结果：{}", result);
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(),e);
		}
	}

}
