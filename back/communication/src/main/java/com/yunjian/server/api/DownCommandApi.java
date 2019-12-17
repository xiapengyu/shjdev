package com.yunjian.server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yunjian.server.api.vo.CartOutParamsVo;
import com.yunjian.server.api.vo.CommonParamVo;
import com.yunjian.server.api.vo.ConfigLightParamVo;
import com.yunjian.server.api.vo.ConfigTempParamVo;
import com.yunjian.server.api.vo.GoodOutParamsVo;
import com.yunjian.server.api.vo.PullLogParamVo;
import com.yunjian.server.api.vo.SendAdParamVo;
import com.yunjian.server.api.vo.UpgradeParamVo;
import com.yunjian.server.service.DownService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/** 下行接口 **/
@Slf4j
@RestController
@ResponseBody
@Api(tags = "指令下发接口")
public class DownCommandApi {
	
	@Autowired
	private DownService downService;
	
	@ApiOperation(value = "远程重启设备")
	@ApiImplicitParams({
	    @ApiImplicitParam(name="deviceCode",value="设备编号", required=true, dataType="String"),
	})
	@RequestMapping(value="/tcp/reboot",method = RequestMethod.POST)
	public String rebootDevice(@RequestBody CommonParamVo commonParam) {
		boolean b = downService.rebootDevice(commonParam.getDeviceCode());
		return b?"success":"error:设备离线";
	}
	
	@ApiOperation(value = "远程升级设备APP")
	@RequestMapping(value="/tcp/upgrade",method = RequestMethod.POST)
	public String upgradeDevice(@RequestBody UpgradeParamVo param) {
		return downService.updateDevice(param)?"success":"error:设备离线";
	}
	
	@ApiOperation(value = "远程拉取日志")
	@RequestMapping(value="/tcp/logs",method = RequestMethod.POST)
	public String pullLogs(@RequestBody PullLogParamVo param) {
		return downService.pullLogs(param)?"success":"error:设备离线";
	}
	
	@ApiOperation(value = "远程设置柜子温度")
	@RequestMapping(value="/tcp/temp",method = RequestMethod.POST)
	public String configTemp(@RequestBody ConfigTempParamVo param) {
		return downService.configTemp(param)?"success":"error:设备离线";
	}
	
	@ApiOperation(value = "远程设置柜子灯光")
	@RequestMapping(value="/tcp/light",method = RequestMethod.POST)
	public String configLight(@RequestBody ConfigLightParamVo param) {
		return downService.configLight(param) ? "success":":error:设备离线";
	}
	
	@ApiOperation(value = "远程设置密码")
	@RequestMapping(value="/tcp/setpass",method = RequestMethod.POST)
	public String configPassword(@RequestBody CommonParamVo param) {
		return downService.configPassword(param) ? "success":"error:设备离线";
	}
	
	@ApiOperation(value = "下发广告")
	@RequestMapping(value="/tcp/ad",method = RequestMethod.POST)
	public String sendAd(@RequestBody SendAdParamVo sendAdParamVo) {
		return downService.sendAd(sendAdParamVo)?"success":"error";
	}
	
	@ApiOperation(value = "商品出货")
	@RequestMapping(value="/tcp/goodsOut",method = RequestMethod.POST)
	public String goodsOut(@RequestBody GoodOutParamsVo param) {
		log.info("接收出货参数：{}", param.toString());
		downService.goodsOut(param);
		return "success";
	}
	
	@ApiOperation(value = "商品出货")
	@RequestMapping(value="/tcp/cartOut",method = RequestMethod.POST)
	public String cartOut(@RequestBody CartOutParamsVo param) {
		log.info("接收出货参数：{}", param.toString());
		downService.cartOut(param);
		return "success";
	}
	

}
