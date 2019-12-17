package com.yunjian.api.controller;

import java.util.List;

import com.yunjian.modules.automat.vo.DeviceExt;
import com.yunjian.modules.automat.vo.GoodsExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yunjian.modules.automat.entity.DeviceWarnEntity;
import com.yunjian.modules.automat.entity.GoodsEntity;
import com.yunjian.modules.automat.service.DeviceWarnService;
import com.yunjian.modules.automat.service.GoodsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "配送员相关接口")
public class DeliverymanController {

	@Autowired
	private DeviceWarnService deviceWarningService;
	@Autowired
	private GoodsService goodsService;
	
	@ApiOperation(value = "【配送员首页】指派给我的配送")
	@ApiImplicitParams({
	    @ApiImplicitParam(name="phoneNo",value="用户id", required=true, dataType="String"),
	    @ApiImplicitParam(name="lng",value="经度", required=true, dataType="Double"),
	    @ApiImplicitParam(name="lat",value="纬度", required=true, dataType="Double"),
	})
	@RequestMapping(value="/client/deliveryList",method = RequestMethod.GET)
	public List<DeviceExt> clientDeliveryList(String phoneNo,Double lng, Double lat) {
		return deviceWarningService.findDeviceWarningByPhoneNo(phoneNo,lng, lat);
	}
	
	@ApiOperation(value = "【配送员】售货机详情(缺货)")
	@ApiImplicitParams({
	    @ApiImplicitParam(name="deviceCode",value="设备Code", required=true, dataType="String"),
	})
	@RequestMapping(value="/client/goodsList",method = RequestMethod.GET)
	public List<GoodsExt> goodsList(String deviceCode) {
		return goodsService.findGoodsByDeviceId(deviceCode);
	}

}
