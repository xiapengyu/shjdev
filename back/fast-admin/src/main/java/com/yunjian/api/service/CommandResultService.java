package com.yunjian.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.yunjian.api.constant.CommandEnum;
import com.yunjian.api.constant.ExeStatusEnum;
import com.yunjian.api.dto.SendFinishDto;
import com.yunjian.api.dto.StatusCodeDto;
import com.yunjian.api.dto.VersionDto;
import com.yunjian.common.utils.ByteUtils;

@Service("commandResultService")
public class CommandResultService {
	
	private Logger logger = LoggerFactory.getLogger(CommandResultService.class);

	/**
	 * 处理各个指令的响应结果
	 */
	public void handleResult(Object result, CommandEnum commandEnum) {
		switch (commandEnum) {
		case MECHINE_CTRL_RESULT:  //对应 电机控制指令  0x30 
			handleCtrlResult(result);
			break;
		case MECHINE_CTRL_INFRARED_DETECTION_RESULT:  //对应 电机控制+红外检测指令  0x31
			handleCtrlInfraredDetectionResult(result);
			break;
		case QUERY_STATUS_RESULT: //对应 状态查询指令  0x32 
			handleStatusResult((StatusCodeDto)result);
			break;
		case UPLOAD_EVENT_RESULT: //对应 按键仓门事件上报 0xB3
			handleEventResult(result);
			break;
		case SET_ADDRESS_RESULT:  //对应 地址设置指令  0x35 
			handleAddressResult(result);
			break;
		case READ_VERSION_RESULT:  //对应 读版本指令 0xb6
			handleVersionInfo((VersionDto)result);
			break;
		case SOFTWARE_RESET_RESULT:  //对应 软件复位指令 0xb8
			handleSoftwareResetResult(result);
			break;
		case UPLOAD_START_RESULT:  //对应 开机上报指令 0xB8
			handleStartResult(result);
			break;
		case CAGE_ASSEMBLY_CTRL_RESULT: //对应 升降台控制指令  0x39 
			handleCageAssemblyCtrlResult(result);
			break;
		case SECURITY_DOOR_CTRL_RESULT: //对应 防盗门控制指令  0x3A 
			handleSecurityDoorCtrlResult(result);
			break;
		case CABIN_DOOR_LOCK_RESULT:  //对应 仓门锁控制指令  0x3B 
			handleCabinDoorLoclResult(result);
			break;
		case CANCEL_CURRENT_OPT_RESULT: //对应 取消当前操作指令  0x3F 
			handleCancelResult(result);
			break;
		case READ_TEMP_RESULT:  //对应 读取温度  0x40 
			//uploadRemp(body.getData());
			break;
		case SOFTWARE_UPGRADE_RESULT: //对应软件升级  0x7F 
			uploadSoftwareUpgradeResult(result);
			break;
		default:
			break;
		}
		
	}
	
	/**
	 * 处理软件升级结果
	 * @param result
	 */
	private void uploadSoftwareUpgradeResult(Object result) {
		if("00000001".equals(result.toString())) { //0x01 开始传输的结果
			logger.info("软件升级：开始传输");
			
		}else if("00000010".equals(result.toString())) {  //0x02 正在传输
			logger.info("软件升级：正在传输");
		}else if("00000100".equals(result.toString())) {  //0x03 开始升级
			logger.info("软件升级：开始升级");
		}else { //传输完成
			logger.info("软件升级：传输完成");
			SendFinishDto finish = (SendFinishDto) result;
			// TODO
		}
		
		
	}

	/**
	 * @param result 仓门锁控制指令响应结果
	 */
	private void handleCancelResult(Object result) {
		logger.info("收到【取消当前操作指令(0x3F)】的响应结果：{}" , result.toString());
		//TODO 结果需推送给小程序JSON
	}
	
	/**
	 * @param result 仓门锁控制指令响应结果
	 */
	private void handleCabinDoorLoclResult(Object result) {
		logger.info("收到【仓门锁控制指令(0x3B)】的响应结果：{}" , result.toString());
		//TODO 结果需推送给小程序JSON
	}
	
	/**
	 * @param result 防盗门控制指令响应结果
	 */
	private void handleSecurityDoorCtrlResult(Object result) {
		logger.info("收到【 防盗门控制指令(0x3A)】的响应结果：{}" , result.toString());
		//TODO 结果需推送给小程序JSON
	}
	/**
	 * @param result 升降台控制指令响应结果
	 */
	private void handleCageAssemblyCtrlResult(Object result) {
		logger.info("收到【升降台控制指令(0x39)】的响应结果：{}" , result.toString());
		//TODO 结果需推送给小程序JSON
	}

	/**
	 * @param result 开机指令响应结果
	 */
	private void handleStartResult(Object result) {
		logger.info("收到【 开机上报指令(0xB8)】的响应结果：{}" , result.toString());
		//TODO 结果需推送给小程序JSON
	}

	/**
	 * 软件复位指令结果
	 * @param result
	 */
	private void handleSoftwareResetResult(Object result) {
		logger.info("收到【 软件复位指令(0x38)】的响应结果：{}" , result.toString());
		//TODO 结果需推送给小程序JSON
	}

	/**
	 * @param result (VersionDto) 读版本指令结果
	 */
	private void handleVersionInfo(VersionDto result) {
		logger.info("收到【 读版本指令(0x36)】的响应结果：{}" , result.toString());
		//TODO 结果需推送给小程序JSON
	}

	/**
	 * @param result 1字节  地址设置 指令结果
	 */
	private void handleAddressResult(Object result) {
		byte[] resu = ByteUtils.binStrToByteArr((String)result);
		ExeStatusEnum statusEnum = ExeStatusEnum.getEnumByNo(resu[0]);
		logger.info("收到【地址设置指令 (0x35)】的响应结果：{}" , statusEnum.getName());
		//TODO 结果需推送给小程序
	}

	
	/**
	 * @param result 1字节 按键/仓门事件 指令结果
	 */
	private void handleEventResult(Object result) {
		byte[] resu = ByteUtils.binStrToByteArr((String)result);
		ExeStatusEnum statusEnum = ExeStatusEnum.getEnumByNo(resu[0]);
		logger.info("收到【按键/仓门事件上报 (0xB3)】的响应结果：{}" , statusEnum.getName());
		//TODO 结果需推送给小程序
	}

	/**
	 * @param result (StatusCodeDto) 状态查询指令结果
	 */
	private void handleStatusResult(StatusCodeDto result) {
		logger.info("收到【 状态查询指令(0x32)】的响应结果：{}" , result.toString());
		//TODO 结果需推送给小程序JSON
	}

	/**
	 * @param result 1字节 电机控制+红外检测指令结果
	 */
	private void handleCtrlInfraredDetectionResult(Object result) { 
		byte[] resu = ByteUtils.binStrToByteArr((String)result);
		ExeStatusEnum statusEnum = ExeStatusEnum.getEnumByNo(resu[0]);
		logger.info("收到【 电机控制+红外检测指令(0x31)】的响应结果：{}" , statusEnum.getName());
		//TODO 结果需推送给小程序
	}

	/**
	 * @param result 1字节 电机控制指令响应结果
	 */
	private void handleCtrlResult(Object result) {
		byte[] resu = ByteUtils.binStrToByteArr((String)result);
		ExeStatusEnum statusEnum = ExeStatusEnum.getEnumByNo(resu[0]);
		logger.info("收到【电机控制指令(0x30)】的响应结果：{}" , statusEnum.getName());
		//TODO 结果需推送给小程序
	}

}
