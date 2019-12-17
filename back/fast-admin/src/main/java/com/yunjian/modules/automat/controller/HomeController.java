package com.yunjian.modules.automat.controller;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.R;
import com.yunjian.modules.automat.dao.OrderDetailMapper;
import com.yunjian.modules.automat.entity.OrderEntity;
import com.yunjian.modules.automat.service.DeviceService;
import com.yunjian.modules.automat.service.OrderService;
import com.yunjian.modules.automat.util.DateUtil;
import com.yunjian.modules.automat.vo.HomeVo;
import com.yunjian.modules.automat.vo.Pair;
import com.yunjian.modules.sys.controller.AbstractController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 用户 前端控制器
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-16
 */
@Api(tags = "管理后台首页相关接口")
@RestController
@RequestMapping("/automat/home")
public class HomeController extends AbstractController {
	
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderDetailMapper orderDetailMapper;
	
	@GetMapping("/data")
	public R list(@RequestParam Map<String, Object> params){
		HomeVo home = new HomeVo();
		home.setDeviceCount(deviceService.countTotal());
		home.setDayOrderCount(orderService.dayOrderCount());
		home.setWeekOrderCount(orderService.weekOrderCount());
		QueryWrapper<OrderEntity> query = new QueryWrapper<OrderEntity>();
		query.in("pay_status", Arrays.asList(1,2,3,5,6));
		List<OrderEntity> list = orderService.list(query);
		List<String> ids = new ArrayList<String>();
		for(OrderEntity item : list) {
			if(!ids.contains(item.getPhoneNo())) {
				ids.add(item.getPhoneNo());
			}
		}
		home.setCosumeUserCount(ids.size());
		return R.ok().put("panelData", home);
	}
	
	@ApiOperation(value = "查询首页订单趋势图")
	@GetMapping("/orderTendency")
	public R orderTendency(){
		String period = "Day";
		List<String> orderLineDate = new ArrayList<String>();
		List<Integer> orderLineData = new ArrayList<Integer>();
		if("Day".equals(period)) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.HOUR, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			calendar.add(Calendar.DAY_OF_YEAR, -7);
			Date start = calendar.getTime();
			for(int i=0;i<7;i++) {
				orderLineDate.add(DateUtil.formatDate(start));
				Date end = DateUtil.addOneDay(start);
				QueryWrapper<OrderEntity> query = new QueryWrapper<OrderEntity>();
				query.ge("create_time", start).lt("create_time", end)
				.in("pay_status", Arrays.asList(1,2,3,5,6));
				int count = orderService.count(query);
				orderLineData.add(count);
				start = end;
			}
		}else if("Week".equals(period)) {
			
		}else if("Month".equals(period)) {
			
		}
		
		return R.ok().put("orderLineDate", orderLineDate)
				.put("orderLineData", orderLineData);
	}
	
	@ApiOperation(value = "查询首页柱状图数据")
	@GetMapping("/orderBarData")
	public R orderBarData(){
		QueryWrapper<OrderEntity> query = new QueryWrapper<OrderEntity>();
		query.in("pay_status", Arrays.asList(1,2,3,5,6));
		//订单总数
		int orderCount = orderService.count(query);
		//营收总金额
		float totalAmount = 0;
		List<OrderEntity> orderList = orderService.list(query);
		for(OrderEntity entity : orderList) {
			totalAmount += entity.getTotalAmount();
		}
		DecimalFormat decimalFormat=new DecimalFormat(".00");
		totalAmount = Float.parseFloat(decimalFormat.format(totalAmount));
		
		QueryWrapper<OrderEntity> queryWrapper = new QueryWrapper<OrderEntity>();
		queryWrapper.in("pay_status", Arrays.asList(1,2,3,5));
		Calendar calendar = Calendar.getInstance();
		Date end = calendar.getTime();
		calendar.add(Calendar.MONDAY, -1);
		Date start = calendar.getTime();
		queryWrapper.ge("create_time", start).lt("create_time", end);
		//最近一月订单数
		int monthOrderCount = orderService.count(queryWrapper);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("orderCount", orderCount);
		result.put("totalAmount", totalAmount);
		result.put("monthOrderCount", monthOrderCount);
		return R.ok().put("result", result);
	}
	
	@ApiOperation(value = "查询首页饼状图数据")
	@GetMapping("/goodsPieData")
	public R goodsPieData(){
		List<Pair> list = orderDetailMapper.goodsAmountOrder();
		List<String> goodsNameList = new ArrayList<String>();
		for(Pair item : list) {
			goodsNameList.add(item.getName());
		}
		return R.ok().put("names", goodsNameList).put("resultList", list);
	}
	
	@ApiOperation(value = "查询首页设备营收排名数据")
	@GetMapping("/deviceAmountRankData")
	public R deviceAmountRankData(@RequestParam Map<String, Object> params){
		PageUtils page = orderService.queryHomeDeviceRankPage(params);
		return R.ok().put("page", page);
	}
}

