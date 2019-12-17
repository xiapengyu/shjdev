package com.yunjian.server.service;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.yunjian.server.api.vo.CartOutParamsVo;
import com.yunjian.server.api.vo.CommonParamVo;
import com.yunjian.server.api.vo.ConfigLightParamVo;
import com.yunjian.server.api.vo.ConfigTempParamVo;
import com.yunjian.server.api.vo.GoodOutParamsVo;
import com.yunjian.server.api.vo.PullLogParamVo;
import com.yunjian.server.api.vo.SendAdParamVo;
import com.yunjian.server.api.vo.UpgradeParamVo;
import com.yunjian.server.common.Constant;
import com.yunjian.server.common.LocalStore;
import com.yunjian.server.common.ProtocolHelper;
import com.yunjian.server.common.RestTemplateComponent;
import com.yunjian.server.dto.ProtocolBody;
import com.yunjian.server.util.ByteUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("downService")
public class DownService {
	
	@Autowired
	private RestTemplateComponent restTemplateComponent;
	
	@Value("${device.password.save.api}")
	private String passwordSaveApi;
	
	public boolean rebootDevice(String deviceCode) {
		try {
			ChannelHandlerContext ctx = LocalStore.getInstance().getCtx(deviceCode);
			ctx.writeAndFlush( ProtocolHelper.reboot() );
			log.info("向设备({})下发重启指令:{}", deviceCode, ByteUtils.toHexString(Constant.REBOOT_DEVICE) );
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return false;
		}
		
		return true;
	}

	public boolean updateDevice(UpgradeParamVo param) {
		String data = param.getAppType() + "," + param.getAppVersion() + "," + param.getMd5();
		log.info("收到App版本信息：{}", data);
		
		byte [] appTypeByte = data.getBytes();
		
		ChannelHandlerContext ctx = null;
		ProtocolBody body = new ProtocolBody();
		body.setCardCode((byte)0x02);
		body.setType((byte)0x30);
		body.setLength((short)(appTypeByte.length+7));
		body.setCtrlCode((byte)0x03);
		body.setData(appTypeByte);
		body.setEnd((short)3338);
		
		try {
			if(param.getDeviceCodeList().isEmpty()) {
				//全量升级
				for(Map.Entry<String,ChannelHandlerContext> entry : LocalStore.getInstance().getCtxMap().entrySet()) {
					ctx = entry.getValue();
					ctx.writeAndFlush(body);
					log.info("向设备({})下发升级APP指令:{}", entry.getKey() , body.toString());
				}
			}else {
				for(String deviceCode : param.getDeviceCodeList()) {
					ctx = LocalStore.getInstance().getCtx(deviceCode);
					ctx.writeAndFlush(body);
					log.info("向设备({})下发升级APP指令:{}", deviceCode , body.toString());
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return false;
		}
		return true;
	}

	public boolean pullLogs(PullLogParamVo param) {
		byte[] type = {(byte)param.getLogType()};
		ChannelHandlerContext ctx = null;
		ProtocolBody body = new ProtocolBody();
		body.setCardCode((byte)0x02);
		body.setType((byte)0x30);
		body.setLength((short)8);
		body.setCtrlCode((byte)0x04);
		body.setData(type);
		body.setEnd((short)3338);
		try {
			for(String deviceCode : param.getDeviceCodeList()) {
				ctx = LocalStore.getInstance().getCtx(deviceCode);
				ctx.writeAndFlush(body);
				log.info("向设备({})下发拉取日志指令:{}", deviceCode , body.toString());
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return false;
		}
		return true;
	}

	public boolean configTemp(ConfigTempParamVo param) {
		byte sign = (byte)param.getSign();
		byte lockerNo = (byte)param.getLockerNo();
		byte temp = (byte)param.getTemp();
		byte [] data = {sign,lockerNo,temp};
		
		ProtocolBody body = new ProtocolBody();
		body.setCardCode((byte)0x02);
		body.setType((byte)0x40);
		body.setLength((short)10);
		body.setCtrlCode((byte)0x01);
		body.setData(data);
		body.setEnd((short)3338);
		try {
			ChannelHandlerContext ctx = LocalStore.getInstance().getCtx(param.getDeviceCode());
			ctx.writeAndFlush(body);
			log.info("向设备({})下发远程设置温度指令:{}", param.getDeviceCode(),body.toString());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return false;
		}
		return true;
	}

	public boolean configLight(ConfigLightParamVo param) {
		byte lockerNo = (byte)param.getLockerNo();
		byte level = (byte)param.getLevel();
		byte [] data = {lockerNo, level};
		ProtocolBody body = new ProtocolBody();
		body.setCardCode((byte)0x02);
		body.setType((byte)0x40);
		body.setLength((short)9);
		body.setCtrlCode((byte)0x02);
		body.setData(data);
		body.setEnd((short)3338);
		try {
			ChannelHandlerContext ctx = LocalStore.getInstance().getCtx(param.getDeviceCode());
			ctx.writeAndFlush(body);
			log.info("向设备({})下发远程设置灯光指令:{}", param.getDeviceCode(),body.toString());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return false;
		}
		return true;
	}

	public boolean configPassword(CommonParamVo param) {
		
		try {
			ChannelHandlerContext ctx = LocalStore.getInstance().getCtx(param.getDeviceCode());
			ProtocolBody body = null;
			
			Map<String, Object> params = Maps.newHashMap();
			params.put("deviceCode", param.getDeviceCode());
			if(StringUtils.isNotBlank(param.getPassword())) {
				body = ProtocolHelper.configPassword(param.getPassword());
				ctx.writeAndFlush( body );
				log.info("向设备({})下发远程设置管理页密码指令:{},密码：{}", param.getDeviceCode(), body.toString(), new String(body.getData(),"utf-8"));
				params.put("password", param.getPassword());
				params.put("passwordType", 0);
				String result = restTemplateComponent.restTemplatePOST(params, passwordSaveApi);
				log.info("向平台上报密码(类型：0)，结果：{}",result);
			}
			if(StringUtils.isNotBlank(param.getTestPassword())) {
				body = ProtocolHelper.configTestPassword(param.getTestPassword());
				ctx.writeAndFlush( body );
				log.info("向设备({})下发远程设置测试页密码指令:{},密码：{}", param.getDeviceCode(), body.toString(), new String(body.getData(),"utf-8"));
				params.put("password", param.getTestPassword());
				params.put("passwordType", 1);
				String result = restTemplateComponent.restTemplatePOST(params, passwordSaveApi);
				log.info("向平台上报密码(类型：1)，结果：{}",result);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return false;
		}
		return true;
	}
	
	public boolean sendAd(SendAdParamVo sendAdParamVo) {
		ProtocolBody body = new ProtocolBody();
		body.setCardCode((byte)0x02);
		body.setType((byte)0x50);
		body.setCtrlCode((byte)0x01);
		body.setEnd((short)3338);
		
		ChannelHandlerContext ctx = null;
		for(String deviceCode : sendAdParamVo.getDeviceCodeList()) {
			String idsStr = StringUtils.join(sendAdParamVo.getAdIdList(), ",");
			body.setData(idsStr.getBytes());
			body.setLength((short)(idsStr.getBytes().length + 7));
			ctx = LocalStore.getInstance().getCtx(deviceCode);
			if(ctx!=null) {
				ctx.writeAndFlush(body);
				log.info("向设备({})下发广告指令:{}", deviceCode , body.toString());
			}else {
				log.info("设备{}不在线。", deviceCode);
				continue;
			}
		}
		return true;
	}

	public void cartOut(CartOutParamsVo param) {
		String[] lockerNos = param.getLockerNos().split(",");
		String[] aisles = param.getAisles().split(",");
//		byte lockerNo = (byte)param.getLockerNo();
//		byte aisle = (byte)param.getAisle();
		byte goodsCount = (byte)param.getGoodsCount();
		byte[] orderNo = param.getOrderNo().getBytes();  //14字节
		byte[] time = ByteUtils.str2Bcd(param.getTime());        //7字节的bcd码
		
		//以下各数值顺序不能变
		byte[] data = new byte[param.getGoodsCount()*2+4];
		int index = 0;
		data[index++] = goodsCount;
		for(int i=0;i<param.getGoodsCount();i++) {
			data[index] = (byte)Integer.parseInt(lockerNos[i]);
			data[param.getGoodsCount()+index] = (byte)Integer.parseInt(aisles[i]);
			index++;
		}
		index += param.getGoodsCount();
		data[index++] = (byte)0x00;  //联动数量
		data[index++] = (byte)0x00;  //执行时间，默认
		data[index++] = (byte)0x00;  //点击转动方向，默认
//		System.arraycopy(orderNo, 0, data, 5, orderNo.length);
//		System.arraycopy(time, 0, data, 19, time.length);
		ByteBuf buf = Unpooled.buffer(data.length+21);
		buf.writeBytes(data);
		buf.writeBytes(orderNo);
		buf.writeBytes(time);

		byte[] dataAll = new byte[data.length+21];
		buf.readBytes(dataAll);
		buf.release();
		//以上各数值顺序不能变
		
		ProtocolBody body = new ProtocolBody();
		body.setCardCode((byte)0x02);
		body.setType((byte)0x70);
		body.setLength((short)(data.length+21+7));
		body.setCtrlCode((byte)0x01);
		body.setData(dataAll);
		body.setEnd((short)3338);
		ChannelHandlerContext ctx = LocalStore.getInstance().getCtx(param.getDeviceCode());
		ctx.writeAndFlush(body);
		log.info("向设备({})下发出货指令:{}", param.getDeviceCode(),body.toString());
		
	}
	//dinggengting modify by 20190905 废丢单个货道出货的逻辑
	public void goodsOut(GoodOutParamsVo param) {
		byte lockerNo = (byte)param.getLockerNo();
		byte aisle = (byte)param.getAisle();
//		byte goodsCount = (byte)param.getGoodsCount();
		byte goodsCount = 0;//modify dgt 联动数据改为默认0，而不是传商品数量
		byte[] orderNo = param.getOrderNo().getBytes();  //14字节
		byte[] time = ByteUtils.str2Bcd(param.getTime());        //7字节的bcd码
		
		//以下各数值顺序不能变
		byte[] data = new byte[5];
		data[0] = lockerNo;
		data[1] = aisle;
		data[2] = goodsCount;
		data[3] = (byte)0x00;  //执行时间，默认
		data[4] = (byte)0x00;  //点击转动方向，默认
//		System.arraycopy(orderNo, 0, data, 5, orderNo.length);
//		System.arraycopy(time, 0, data, 19, time.length);
		ByteBuf buf = Unpooled.buffer(26);
		buf.writeBytes(data);
		buf.writeBytes(orderNo);
		buf.writeBytes(time);
		
		byte[] dataAll = new byte[26];
		buf.readBytes(dataAll);
		buf.release();
		//以上各数值顺序不能变
		
		ProtocolBody body = new ProtocolBody();
		body.setCardCode((byte)0x02);
		body.setType((byte)0x60);
		body.setLength((short)33);
		body.setCtrlCode((byte)0x01);
		body.setData(dataAll);
		body.setEnd((short)3338);
		ChannelHandlerContext ctx = LocalStore.getInstance().getCtx(param.getDeviceCode());
		ctx.writeAndFlush(body);
		log.info("向设备({})下发出货指令:{}", param.getDeviceCode(),body.toString());
	}

}
