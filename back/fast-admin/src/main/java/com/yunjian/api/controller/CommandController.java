package com.yunjian.api.controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import com.yunjian.api.constant.CommandEnum;
import com.yunjian.api.dto.StatusCodeDto;
import com.yunjian.api.dto.VersionDto;
import com.yunjian.api.service.CommandResultService;
import com.yunjian.common.utils.ByteUtils;
import com.yunjian.common.utils.R;
import com.yunjian.modules.automat.entity.DeviceEntity;
import com.yunjian.modules.automat.entity.OrderEntity;
import com.yunjian.modules.automat.service.DeviceService;
import com.yunjian.modules.automat.service.OrderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 售货机指令下发
 * @author Administrator
 *
 */

@RestController
@Api(tags = "接收指令执行结果的接口")
public class CommandController {
	
	private final Logger logger = LoggerFactory.getLogger(CommandController.class);
	
	@Autowired
	private CommandResultService commandResultService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${tcp.command.api}")
	private String tcpCommandApi;
	
	/**
	 * 提供给移动端访问的指令下发接口
	 * @param deviceId 设备id
	 * @param command  指令的byte数组转成的16进制字符串
	 */
	@ApiOperation(value = "指令下发")
	@ApiImplicitParams({
	    @ApiImplicitParam(name="deviceId",value="设备编号", required=true, dataType="String"),
	    @ApiImplicitParam(name="command",value="字节数组对应的16进制字符串",required=true, dataType="String")
	})
	@RequestMapping(value = "/command/down",method=RequestMethod.POST)
	public void sendCommandToDevice(String deviceId,String command) {
		logger.info("发送指令给设备{},下行指令：{}",deviceId , command);
		
		Map<String,String> params = Maps.newHashMap();
		params.put("deviceId",deviceId);
		params.put("command",command);
		
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		String jsonStr = JSONObject.toJSONString(params);
		HttpEntity<String> formEntity = new HttpEntity<String>(jsonStr, headers);
		String result = restTemplate.postForObject(tcpCommandApi, formEntity, String.class);
		logger.info("平台的响应结果为：{}", result);
	}
	
	
	//======================= 以下为接收指令执行结果 ============================
	
	/**
	 * 电机控制指令 结果接收 
	 */
	@RequestMapping(value = "/command/ctrl/result",method=RequestMethod.POST)
	public void receiveCtrlResult(@RequestBody Map<String, Object> params) {
		handleOneByteResult(params);
	}
	
	/**
	 * 电机控制+红外检测指令 结果接收 
	 */
	@RequestMapping(value = "/command/infrared/detection/result",method=RequestMethod.POST)
	public void receiveInfraredDetectionResult(@RequestParam Map<String, Object> params) {
		handleOneByteResult(params);
	}
	
	/**
	 * 状态查询指令 结果接收 
	 */
	@PostMapping(value = "/command/status/result", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void receiveStatusResult(@RequestBody String params) {
		if(StringUtils.isNotEmpty(params)) {
			JSONObject jsonObj = JSON.parseObject(params);
			JSONObject jsonResult = jsonObj.getJSONObject("result");
			StatusCodeDto statusCodeDto = JSON.toJavaObject(jsonResult,StatusCodeDto.class);
			String ctrlCodeStr = jsonObj.getString("ctrlCode");
			byte ctrlCode =  ByteUtils.conver2HexToByte(ctrlCodeStr)[0];
			CommandEnum commandEnum = CommandEnum.getEnumByNo(ctrlCode);
			commandResultService.handleResult(statusCodeDto,commandEnum);
		}
	}
	
	/**
	 * 设置地址指令  结果接收 
	 */
	@RequestMapping(value = "/command/setaddress/result",method=RequestMethod.POST)
	public void receiveSetAddressResult(@RequestParam Map<String, Object> params) {
		handleOneByteResult(params);
	}
	
	/**
	 * 读版本指令  结果接收 
	 */
	@PostMapping(value = "/command/version/result", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void receiveVersionInfo(@RequestBody String params) {
		if(StringUtils.isNotEmpty(params)) {
			JSONObject jsonObj = JSON.parseObject(params);
			JSONObject jsonResult = jsonObj.getJSONObject("result");
			VersionDto versionDto = JSON.toJavaObject(jsonResult,VersionDto.class);
			String ctrlCodeStr = jsonObj.getString("ctrlCode");
			byte ctrlCode =  ByteUtils.conver2HexToByte(ctrlCodeStr)[0];
			CommandEnum commandEnum = CommandEnum.getEnumByNo(ctrlCode);
			commandResultService.handleResult(versionDto,commandEnum);
		}
	}
	
	/**
	 * 软件复位指令  结果接收 
	 */
	@RequestMapping(value = "/command/software/reset/result",method=RequestMethod.POST)
	public void receiveSoftwareReset(@RequestParam Map<String, Object> params) {
		handleOneByteResult(params);
	}
	
	/**
	 * 开机上报指令  结果接收 
	 */
	@RequestMapping(value = "/command/start/result",method=RequestMethod.POST)
	public void receiveStartResult(@RequestParam Map<String, Object> params) {
		handleOneByteResult(params);
	}
	
	/**
	 * 升降台控制指令  结果接收 
	 */
	@RequestMapping(value = "/command/cage/assembly/result",method=RequestMethod.POST)
	public void receiveCageAssemblyResult(@RequestParam Map<String, Object> params) {
		handleOneByteResult(params);
	}
	
	/**
	 * 升降台控制指令  结果接收 
	 */
	@RequestMapping(value = "/command/security/door/result",method=RequestMethod.POST)
	public void receiveSecurityDoorResult(@RequestParam Map<String, Object> params) {
		handleOneByteResult(params);
	}
	
	/**
	 * 升降台控制指令  结果接收 
	 */
	@RequestMapping(value = "/command/cabin/door/result",method=RequestMethod.POST)
	public void receiveCabinDoorResult(@RequestParam Map<String, Object> params) {
		handleOneByteResult(params);
	}
	
	/**
	 * 升降台控制指令  结果接收 
	 */
	@RequestMapping(value = "/command/cancle/result",method=RequestMethod.POST)
	public void receiveCancleResult(@RequestParam Map<String, Object> params) {
		handleOneByteResult(params);
	}
	
	/**
	 * 软件升级过程控制指令  结果接收 
	 */
	@RequestMapping(value = "/command/software/upgrade/result",method=RequestMethod.POST)
	public void receiveSoftwareUpgradeResult(@RequestParam Map<String, Object> params) {
		Object versionDto = params.get("result");
		String ctrlCodeStr = (String) params.get("ctrlCode");
		byte ctrlCode =  ByteUtils.conver2HexToByte(ctrlCodeStr)[0];
		CommandEnum commandEnum = CommandEnum.getEnumByNo(ctrlCode);
		commandResultService.handleResult(versionDto,commandEnum);
	}
	
	/**
	 * 接收的结果只有一个字节的公用方法
	 * @param params
	 */
	private void handleOneByteResult(Map<String, Object> params) {
		Object statusCodeDto = params.get("result");
		String ctrlCodeStr = (String) params.get("ctrlCode");
		byte ctrlCode =  ByteUtils.conver2HexToByte(ctrlCodeStr)[0];
		CommandEnum commandEnum = CommandEnum.getEnumByNo(ctrlCode);
		commandResultService.handleResult(statusCodeDto,commandEnum);
	}

}
