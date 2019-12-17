package com.yunjian.modules.automat.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yunjian.common.utils.R;
import com.yunjian.modules.automat.entity.DeviceEntity;
import com.yunjian.modules.automat.entity.OrderEntity;
import com.yunjian.modules.automat.service.DeviceService;
import com.yunjian.modules.automat.service.GoodsAisleService;
import com.yunjian.modules.automat.service.OrderService;
import com.yunjian.modules.automat.util.JsonUtil;
import com.yunjian.modules.automat.vo.UploadCartOutVo;
import com.yunjian.modules.automat.vo.UploadGoodsOutVo;
import com.yunjian.modules.sys.controller.AbstractController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "设备APP访问接口")
@RestController
@RequestMapping("/app")
public class AppController extends AbstractController{
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private GoodsAisleService goodsAisleService;
	@Autowired
	private OrderService orderService;
	
	@ApiOperation(value = "扫码取货上行接口")
	@GetMapping("/sweepCode")
	public R SweepCode(String orderNo, String deviceCode, String sweepDeviceCode){
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			logger.info("请求地址>>>【{},请求参数>>>【{}】", request.getRequestURL(), request.getQueryString());
			//校验设备是否正常
			QueryWrapper<DeviceEntity> deviceQuery = new QueryWrapper<DeviceEntity>();
			deviceQuery.eq("device_code", sweepDeviceCode);
			DeviceEntity device = deviceService.getOne(deviceQuery);
			if(device == null) {
				logger.error("请求非法，设备不存在");
				return R.ok().put("result",3);      //请求非法
			}
			//检查订单是否正常
			QueryWrapper<OrderEntity> orderQuery = new QueryWrapper<OrderEntity>();
			orderQuery.eq("order_no", orderNo);
			OrderEntity order = orderService.getOne(orderQuery);
			if(order == null ) {
				logger.error("请求非法，没有查询到对应的订单");
				return R.ok().put("result",3); 
			}
			/*else if(order.getPayStatus() == 6 && order.getOutSuccess() == order.getGoodsAmount()) {
				logger.error("该订单已出完货");
				return R.ok().put("result",4); 
			}*/
			int result = goodsAisleService.sweepCode(order, deviceCode, sweepDeviceCode);
			if(result == 2) {
				logger.info("设备缺货");
			}else if(result == 4) {
				logger.error("请求非法，该订单已取货");
			}else {
				logger.info("正常出货");
			}
			return R.ok().put("result",result);
		} catch (Exception e) {
			logger.error("请求错误", e);
			return R.error();
		}
	}
	
	@ApiOperation(value = "扫码取货结果上行接口")
	@RequestMapping(value="/uploadGoodsResult",method = RequestMethod.POST)
	public R uploadGoodsResult(@RequestBody UploadGoodsOutVo param){
		logger.info("扫码取货结果上行接口>>[{}]", JsonUtil.toJsonString(param));
		orderService.uploadGoodsOut(param);
		return R.ok();
	}
	
	@ApiOperation(value = "扫码取货结果上行接口")
	@RequestMapping(value="/uploadCartResult",method = RequestMethod.POST)
	public R uploadGoodsResult(@RequestBody UploadCartOutVo param){
		logger.info("扫码取货结果上行接口>>[{}]", JsonUtil.toJsonString(param));
		orderService.uploadCartOut(param);
		return R.ok();
	}

}
