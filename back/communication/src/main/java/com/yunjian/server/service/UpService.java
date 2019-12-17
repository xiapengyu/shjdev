package com.yunjian.server.service;

import org.springframework.stereotype.Service;

@Service("upService")
public class UpService {
	
//	@Autowired
//	private HttpHelper httpHelper;
//	
//	@Value("${system.manage.host}")
//	private String systemHost;
//
//	/**
//	 *  上报响应结果
//	 * @param body
//	 * @param commandEnum 
//	 */
//	public void uploadResult(ProtocolBody body, CommandEnum commandEnum) {
//		
//		switch (commandEnum) {
//		case MECHINE_CTRL_RESULT:  //对应 电机控制指令  0x30 
//			uploadCtrlResult(body.getData(),CommandEnum.MECHINE_CTRL_RESULT.getNo());
//			break;
//		case MECHINE_CTRL_INFRARED_DETECTION_RESULT:  //对应 电机控制+红外检测指令  0x31
//			uploadCtrlInfraredDetectionResult(body.getData(),CommandEnum.MECHINE_CTRL_INFRARED_DETECTION_RESULT.getNo());
//			break;
//		case QUERY_STATUS_RESULT:  //对应 状态指令  0x32
//			uploadStatus(body.getData(),CommandEnum.QUERY_STATUS_RESULT.getNo());
//			break;
//		case UPLOAD_EVENT_RESULT: //对应 按键仓门事件上报 0xB3
//			uploadButtonOrBingateEvent(body.getData(),CommandEnum.UPLOAD_EVENT_RESULT.getNo());
//			break;
//		case SET_ADDRESS_RESULT:  //对应 地址设置指令  0x35 
//			uploadSetAddressResult(body.getData(),CommandEnum.SET_ADDRESS_RESULT.getNo());
//			break;
//		case READ_VERSION_RESULT:  //对应 读版本指令 0xb6
//			uploadVersionInfo(body.getData(),CommandEnum.READ_VERSION_RESULT.getNo());
//			break;
//		case SOFTWARE_RESET_RESULT:  //对应 软件复位指令 0xb8
//			uploadSoftwareResetResult(body.getData(),CommandEnum.SOFTWARE_RESET_RESULT.getNo());
//			break;
//		case UPLOAD_START_RESULT:  //对应 开机上报指令 0xB8
//			uploadStartResult(body.getData(),CommandEnum.UPLOAD_START_RESULT.getNo());
//			break;
//		case CAGE_ASSEMBLY_CTRL_RESULT: //对应 升降台控制指令  0x39 
//			uploadCageAssemblyCtrlResult(body.getData(), CommandEnum.CAGE_ASSEMBLY_CTRL_RESULT.getNo());
//			break;
//		case SECURITY_DOOR_CTRL_RESULT: //对应 防盗门控制指令  0x3A 
//			uploadSecurityDoorCtrlResult(body.getData(),CommandEnum.SECURITY_DOOR_CTRL_RESULT.getNo());
//			break;
//		case CABIN_DOOR_LOCK_RESULT:  //对应 仓门锁控制指令  0x3B 
//			uploadCabinDoorLoclResult(body.getData() ,CommandEnum.CABIN_DOOR_LOCK_RESULT.getNo());
//			break;
//		case CANCEL_CURRENT_OPT_RESULT: //对应 取消当前操作指令  0x3F 
//			uploadCancelResult(body.getData(), CommandEnum.CANCEL_CURRENT_OPT_RESULT.getNo());
//			break;
//		case READ_TEMP_RESULT:  //对应 读取温度  0x40 
//			uploadRemp(body.getData());
//			break;
//		case SOFTWARE_UPGRADE_RESULT: //对应软件升级  0x7F 
//			uploadSoftwareUpgradeResult(body.getData(), CommandEnum.SOFTWARE_UPGRADE_RESULT.getNo());
//			break;
//		default:
//			break;
//		}
//		
//	}
//
//	/**
//	 * 软件升级过程上报
//	 * @param data
//	 */
//	private void uploadSoftwareUpgradeResult(byte[] data,byte ctrlCode) {
//		Map<String,Object> result = Maps.newHashMap();
//		result.put("ctrlCode", ByteUtils.byteToBinaryString(ctrlCode) );
//		if(data.length==1) {  //0x01  0x02  0x04
//			result.put("result", ByteUtils.byteToBinaryString(data[0]) );
//		}else {
//			SendFinishDto finish = new SendFinishDto();
//			finish.setChecksum(ByteUtils.byteToBinaryString(data[1]));
//			finish.setForeignChecksum(ByteUtils.byteToBinaryString(data[2]));
//			result.put("result", finish);
//		}
//		httpHelper.restTemplatePOST(result, systemHost + "/command/software/upgrade/result");
//	}
//
//	/**
//	 * TODO
//	 * @param data
//	 */
//	private void uploadRemp(byte[] data) {
//		
//		
//	}
//
//	/**
//	 * 取消当前操作指令的结果上报
//	 * @param data
//	 */
//	private void uploadCancelResult(byte[] data,byte ctrlCode) {
//		uploadOneByteResult(data, ctrlCode, systemHost + "/command/cabin/door/result");  //TODOs		
//	}
//
//	/**
//	 * 仓门锁控制指令结果上报
//	 * @param data
//	 */
//	private void uploadCabinDoorLoclResult(byte[] data,byte ctrlCode) {
//		uploadOneByteResult(data, ctrlCode, systemHost + "/command/cabin/door/result");  //TODOs
//	}
//
//	/**
//	 * 防盗门控制指令结果上报
//	 * @param data
//	 */
//	private void uploadSecurityDoorCtrlResult(byte[] data,byte ctrlCode) {
//		uploadOneByteResult(data, ctrlCode, systemHost + "/command/security/door/result");  //TODO
//	}
//
//	/**
//	 * 升降台控制指令结果上报
//	 * @param data
//	 */
//	private void uploadCageAssemblyCtrlResult(byte[] data, byte ctrlCode) {
//		uploadOneByteResult(data, ctrlCode ,systemHost + "/command/cage/assembly/result");  //TODO
//	}
//
//	/**
//	 * 开机上报指令的响应结果上报
//	 * @param data
//	 */
//	private void uploadStartResult(byte[] data, byte ctrlCode) {
//		uploadOneByteResult(data, ctrlCode, systemHost + "/command/start/result");  
//	}
//
//	/**
//	 * 软件复位指令 的响应结果上报
//	 * @param data
//	 */
//	private void uploadSoftwareResetResult(byte[] data,byte ctrlCode) {
//		uploadOneByteResult(data, ctrlCode, systemHost + "/command/software/reset/result"); 
//	}
//
//	/**
//	 * 上报电机控制+红外检测指令结果
//	 * @param data
//	 */
//	private void uploadCtrlInfraredDetectionResult(byte[] data,byte ctrlCode) {
//		uploadOneByteResult(data,ctrlCode, systemHost + "/command/infrared/detection/result");
//	}
//
//	/**
//	 * 上报电机控制指令结果
//	 * @param data
//	 */
//	private void uploadCtrlResult(byte[] data,byte ctrlCode) {
//		uploadOneByteResult(data,ctrlCode, systemHost + "/command/ctrl/result");
//	}
//
//	/**
//	 * 读版本指令结果上报
//	 * @param data
//	 */
//	private void uploadVersionInfo(byte[] data,byte ctrlCode) {
//		VersionDto version = new VersionDto();
//		byte[] versionByte = new byte[4];
//		byte[] supportByte = new byte[4];
//		versionByte = Arrays.copyOfRange(data,0,4);
//		supportByte = Arrays.copyOfRange(data,5,9);
//		version.setVersion( ByteUtils.bytesToInt(versionByte,0) );
//		version.setAisleType( ByteUtils.byteToBinaryString(data[4]) );
//		version.setSupport( ProtocolParser.packagingSupportCode1(supportByte[1]) );
//		Map<String,Object> map = Maps.newHashMap();
//		map.put("result", version);
//		map.put("ctrlCode", ByteUtils.byteToBinaryString(ctrlCode) );
//		httpHelper.restTemplatePOST(version, systemHost + "/command/version/result");
//	}
//
//	/**
//	 * 地址设置结果上报
//	 * @param data
//	 */
//	private void uploadSetAddressResult(byte[] data,byte ctrlCode) {
//		uploadOneByteResult(data, ctrlCode, systemHost + "/command/setaddress/result");
//	}
//
//	/**
//	 * 按键仓门事件结果上报
//	 * @param data
//	 */
//	private void uploadButtonOrBingateEvent(byte[] data ,byte ctrlCode) {
//		uploadOneByteResult(data, ctrlCode, systemHost + "/command/button/bingate/result");
//	}
//	
//	/**
//	 * 响应结果只有一个字节的公用方法
//	 * @param ctrlCode 
//	 */
//	private void uploadOneByteResult(byte[] data,byte ctrlCode, String url) {
//		Map<String,Object> map = Maps.newHashMap();
//		map.put("result", ByteUtils.byteToBinaryString(data[0]) );
//		map.put("ctrlCode", ByteUtils.byteToBinaryString(ctrlCode) );
//		httpHelper.restTemplatePOST(map, url);
//	}
//
//	/**
//	 * 上报状态查询指令结果
//	 * @param data
//	 */
//	private void uploadStatus(byte[] data,byte ctrlCode) {
//		StatusCodeDto statusCodeDto = new StatusCodeDto();
//		statusCodeDto.setCode1( ByteUtils.byteToBinaryString(data[0]) );
//		statusCodeDto.setCode2(ProtocolParser.packagingStatusCode2(data[1]));
//		statusCodeDto.setCode3(ProtocolParser.packagingStatusCode3(data[2]));
//		Map<String,Object> map = Maps.newHashMap();
//		map.put("result", statusCodeDto);
//		map.put("ctrlCode", ByteUtils.byteToBinaryString(ctrlCode) );
//		httpHelper.restTemplatePOST(map, systemHost + "/command/status/result");
//	}

}
