package com.yunjian.server.tcp;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.yunjian.server.common.Constant;
import com.yunjian.server.common.LocalStore;
import com.yunjian.server.common.MsgTypeEnum;
import com.yunjian.server.common.RestTemplateComponent;
import com.yunjian.server.dto.ProtocolBody;
import com.yunjian.server.service.CtrlService;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * TCP服务处理类.
 */
@Slf4j
@Component
@Scope("prototype")
@Sharable
public class TCPServerHandler extends ChannelHandlerAdapter {
	
	@Autowired
	private CtrlService ctrlService;
	
	@Autowired
	private RestTemplateComponent restTemplateComponent;
	
	@Value("${device.online.switch.api}")
	private String onlineSwitchApi;
	
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		log.info("设备({})与命令路由服务建立tcp通讯成功", ctx.channel().remoteAddress().toString());
		super.channelRegistered(ctx);
	}

	/**
	 * 收发消息样例
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ProtocolBody body = (ProtocolBody)msg;
		parseData(body);

		String deviceCode = null;
		if(!(body.getType()==(byte)0x20 && body.getCtrlCode()==(byte)0x01)) {
			deviceCode = LocalStore.getInstance().getDeviceCode(ctx);
			body.setDeviceCode(deviceCode);
		}
		
		if(body.getType()==(byte)0x20 && body.getCtrlCode()==(byte)0x01) {
			ctrlService.handleReceiveDeviceCode(ctx,body);
		}else if(body.getType()==(byte)0x10 && body.getCtrlCode()==(byte)0x01) {
			ctrlService.handleHeartbeat(ctx,deviceCode);
		}else if(body.getType()==(byte)0x30 && body.getCtrlCode()==(byte)0x01) {
			ctrlService.handleDeviceStatus(ctx,body);
		}else if(body.getType()==(byte)0x30 && body.getCtrlCode()==(byte)0x02) {
			ctrlService.handleRebootResult(body);
		}else if(body.getType()==(byte)0x40 && body.getCtrlCode()==(byte)0x01) {
			ctrlService.handleTempConfigResult(body);
		}else if(body.getType()==(byte)0x40 && body.getCtrlCode()==(byte)0x02) {
			ctrlService.handleLightConfigResult(body);
		}else if(body.getType()==(byte)0x50 && body.getCtrlCode()==(byte)0x01) {
			ctrlService.handleSendAdResult(body);
		}else if(body.getType()==(byte)0x60 && body.getCtrlCode()==(byte)0x01) {
			ctrlService.handleGoodsOutResult(body);
		}else if(body.getType()==(byte)0x40 && body.getCtrlCode()==(byte)0x03) {
			ctrlService.handlePasswordResult(body);
		}else if(body.getType()==(byte)0x40 && body.getCtrlCode()==(byte)0x05) {
			ctrlService.handleTestPasswordResult(body);
		}
		
	}
	
	/**
	 * 解析数据，打印必要日志，方便日后排查问题
	 */
	private void parseData(ProtocolBody body) {
		String name = MsgTypeEnum.getNameByNo(body.getMsgType());
		log.info("解析出[{}]指令(帧类型+功能码)：{}", name, body.getMsgType());
	}
	
	/**
	 * 事件回调函数，如：读消息超时触发
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;
			switch (e.state()) {
			case READER_IDLE:
				handleReaderIdle(ctx);
				break;
			case WRITER_IDLE:
				// handleWriterIdle(ctx); //预留
				break;
			case ALL_IDLE:
				// handleAllIdle(ctx); //预留
				break;
			default:
				break;
			}

		}

		super.userEventTriggered(ctx, evt);
	}
	
	protected void handleReaderIdle(ChannelHandlerContext ctx) throws Exception {
		String deviceCode = LocalStore.getInstance().getDeviceCode(ctx);
		if(deviceCode == null) return;
		log.info("{}读空闲事件被触发...", deviceCode);
		LocalStore.getInstance().addTimeoutCount(deviceCode);

		int count = LocalStore.getInstance().getTimeoutCount(deviceCode);
		log.info("已经{}个周期没有读取到到({})信息！马上发出心跳消息...", count, deviceCode);
		if (count >= Constant.HEARTBEAT_SYCLE_TIME) {
			log.info("已经{}个周期没有收到连接{}的信息！关闭该连接并上报该设备离线", Constant.HEARTBEAT_SYCLE_TIME, deviceCode);
			ctx.close();
			return;
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		String deviceCode = LocalStore.getInstance().getDeviceCode(ctx);
		log.info("与设备({})通讯发生异常, 异常: {}", deviceCode, cause.getLocalizedMessage());
		ctx.close(); 
		super.exceptionCaught(ctx, cause);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		String deviceCode = LocalStore.getInstance().getDeviceCode(ctx);
		super.channelInactive(ctx);
		
		log.info("设备({})与服务器失去活跃,上报离线并移除该连接", deviceCode);
		Map<String, Object> params = Maps.newHashMap();
		params.put("deviceCode", deviceCode);
		params.put("onlineStatus", 1);
		String result = restTemplateComponent.restTemplatePOST(params, onlineSwitchApi);
		log.info("向平台上报离线，结果：{}", result);
		
		LocalStore.getInstance().recycling(deviceCode);
	}

}
