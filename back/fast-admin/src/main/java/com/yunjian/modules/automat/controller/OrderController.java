package com.yunjian.modules.automat.controller;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.R;
import com.yunjian.modules.automat.entity.GoodsEntity;
import com.yunjian.modules.automat.entity.OrderDetailEntity;
import com.yunjian.modules.automat.entity.OrderEntity;
import com.yunjian.modules.automat.entity.WxAccountEntity;
import com.yunjian.modules.automat.service.GoodsService;
import com.yunjian.modules.automat.service.OrderDetailService;
import com.yunjian.modules.automat.service.OrderService;
import com.yunjian.modules.automat.util.DateUtil;
import com.yunjian.modules.automat.vo.OrderDetailExtDto;
import com.yunjian.modules.automat.vo.OrderExt;
import com.yunjian.modules.automat.vo.TendencyPairVo;
import com.yunjian.modules.sys.controller.AbstractController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-16
 */
@Api(tags = "订单相关接口")
@RestController
@RequestMapping("/automat/order")
public class OrderController extends AbstractController {
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderDetailService orderDetailService;
	
	@Autowired
	private GoodsService goodsService;
	
	@GetMapping("/list")
	@RequiresPermissions("order:list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = orderService.queryPage(params);
		return R.ok().put("page", page);
	}
	
	@ApiOperation(value = "根据orderId查询订单信息")
	@RequestMapping(value = "/getOrderById", method = RequestMethod.POST)
	public R getOrderById(@RequestBody OrderEntity order){
		logger.info("查询订单信息>>>[{}]", order.getOrderId());
		QueryWrapper<OrderEntity> orderQuery = new QueryWrapper<OrderEntity>();
		orderQuery.eq("order_id", order.getOrderId());
		OrderEntity o = this.orderService.getOne(orderQuery);
		logger.info("查询订单信息>>>[{}]", o);
		return R.ok().put("orderInfo", o);
	}
	
	@ApiOperation(value = "根据orderCode查询订单信息")
	@RequestMapping(value = "/getOrderByCode", method = RequestMethod.POST)
	public R getOrderByCode(@RequestBody OrderEntity order){
		logger.info("查询订单信息>>>[{}]", order.getOrderId());
		QueryWrapper<OrderEntity> orderQuery = new QueryWrapper<OrderEntity>();
		orderQuery.eq("order_code", order.getOrderCode());
		OrderEntity o = this.orderService.getOne(orderQuery);
		logger.info("查询订单信息>>>[{}]", o);
		return R.ok().put("orderInfo", o);
	}
	
	@ApiOperation(value = "支付订单")
	@RequestMapping(value = "/payOrder", method = RequestMethod.POST)
	public R payOrder(@RequestBody OrderExt order) {
		logger.info("submit order bill>>[{}]", order);
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		return orderService.payOrder(order, request);
	}
	
	@ApiOperation(value = "支付订单缴费回调")
	@RequestMapping(value = "/weixinPayOrderNotify", method = RequestMethod.POST)
	public R weixinPayOrderNotify(HttpServletRequest request, HttpServletResponse response) {
		return orderService.weixinPayOrderNotify(request, response);
	}
	
	@ApiOperation(value = "用户提现")
	@RequestMapping(value = "/getCash", method = RequestMethod.POST)
	public R getCash(@RequestBody WxAccountEntity account) {
		return orderService.getCash(account);
	}
	
	@ApiOperation(value = "会员的订单列表")
	@ApiImplicitParams({
	    @ApiImplicitParam(name="phoneNo",value="手机号码", required=true, dataType="String"),
	})
	@GetMapping("/findByPhoneNo")
	public List<OrderExt> findByPhoneNo(String phoneNo){
		List<OrderExt> list = orderService.findByPhoneNo(phoneNo);
		return list;
	}
	
	@ApiOperation(value = "查询1个订单信息")
	@GetMapping("/findOrder")
	public R findOrder(Integer orderId){
		OrderEntity order = new OrderEntity();
		order.setOrderId(orderId);
		OrderEntity o = orderService.findOrder(order);
		
		QueryWrapper<OrderDetailEntity> query = new QueryWrapper<OrderDetailEntity>();
		query.eq("order_id", orderId).orderByDesc("create_time");
		List<OrderDetailEntity> list = this.orderDetailService.list(query);
		List<OrderDetailExtDto> resultList = new ArrayList<OrderDetailExtDto>();
		
		List<GoodsEntity> goodsList = goodsService.list();
		
		for(OrderDetailEntity detail : list) {
			OrderDetailExtDto dto = new OrderDetailExtDto();
			for(GoodsEntity g : goodsList) {
				if(detail.getGoodsId() == g.getGoodsId()) {
					BeanUtils.copyProperties(detail, dto);
					if(detail.getGoodsId() == g.getGoodsId()) {
						dto.setImgPath(g.getImgPath());
					}
				}
			}
			resultList.add(dto);
		}
		
		return R.ok().put("order", o).put("details", resultList);
	}
	
	@ApiOperation(value = "删除订单")
	@ApiImplicitParams({
	    @ApiImplicitParam(name="orderId",value="订单号", required=true, dataType="String"),
	})
	@GetMapping("/deleteByOrderId")
	public boolean deleteByPhoneNo(String orderId){
		boolean result = orderService.deleteByOrderId(orderId);
		return result;
	}
	
	@ApiOperation(value = "查询订单详情列表")
	@GetMapping("/orderDetailsById/{id}")
	public R orderDetailsById(@PathVariable("id") Integer orderId){
		QueryWrapper<OrderEntity> orderQuery = new QueryWrapper<OrderEntity>();
		orderQuery.eq("order_id", orderId);
		OrderEntity order = this.orderService.getOne(orderQuery);
		
		QueryWrapper<OrderDetailEntity> query = new QueryWrapper<OrderDetailEntity>();
		query.eq("order_id", orderId);
		List<OrderDetailEntity> list = this.orderDetailService.list(query);
		List<OrderDetailExtDto> resultList = new ArrayList<OrderDetailExtDto>();
		
		List<GoodsEntity> goodsList = goodsService.list();
		for(OrderDetailEntity detail : list) {
			OrderDetailExtDto dto = new OrderDetailExtDto();
			for(GoodsEntity g : goodsList) {
				BeanUtils.copyProperties(detail, dto);
				if(detail.getGoodsId() == g.getGoodsId()) {
					dto.setImgPath(g.getImgPath());
				}
			}
			resultList.add(dto);
		}
		return R.ok().put("order", order).put("dicData", resultList);
	}
	
	@ApiOperation(value = "查询订单详情列表")
	@GetMapping("/orderTendency/{id}")
	public R orderTendency(@PathVariable("id") Integer type){
		QueryWrapper<OrderEntity> query = new QueryWrapper<OrderEntity>();
		query.in("pay_status", Arrays.asList(1,2,3,5,6));
		List<OrderEntity> records = orderService.list(query);
		List<String> dateArray = new ArrayList<String>();
		List<Double> amountArray = new ArrayList<Double>();
		List<Integer> timesArray = new ArrayList<Integer>();
		int totalTimes = 0;
		double totalAmount = 0;
		
		for(OrderEntity record : records) {
			if(record.getPayTime() != null) {
				totalTimes++;
				totalAmount = totalAmount + record.getTotalAmount().doubleValue();
			}
		}
		totalAmount = this.format(totalAmount);
		if(type == 1) {
			List<TendencyPairVo> days = DateUtil.getLast30DayList();
			for(TendencyPairVo day : days) {
				for(OrderEntity record : records) {
					if(record.getPayTime() != null && record.getPayTime().compareTo(day.getStartTime()) >= 0 
							&& record.getPayTime().compareTo(day.getEndTime()) < 0) {
						day.setTimes(day.getTimes() + 1);
						day.setAmount(day.getAmount() + record.getTotalAmount().doubleValue());
					}
				}
				timesArray.add(day.getTimes());
				amountArray.add(this.format(day.getAmount()));
				dateArray.add(day.getDate());
			}
		}else if(type == 2) {
			List<TendencyPairVo> months = DateUtil.getLast12MonthList();
			for(TendencyPairVo month : months) {
				for(OrderEntity record : records) {
					if(record.getPayTime() != null && record.getPayTime().compareTo(month.getStartTime()) >= 0 
							&& record.getPayTime().compareTo(month.getEndTime()) < 0) {
						month.setTimes(month.getTimes() + 1);
						month.setAmount(month.getAmount() + record.getTotalAmount().doubleValue());
					}
				}
				timesArray.add(month.getTimes());
				amountArray.add(this.format(month.getAmount()));
				dateArray.add(month.getDate());
			}
		}else if(type == 3) {
			List<TendencyPairVo> years = DateUtil.getLastYearList();
			for(TendencyPairVo year : years) {
				for(OrderEntity record : records) {
					if(record.getPayTime() != null && record.getPayTime().compareTo(year.getStartTime()) >= 0 
							&& record.getPayTime().compareTo(year.getEndTime()) < 0) {
						year.setTimes(year.getTimes() + 1);
						year.setAmount(year.getAmount() + record.getTotalAmount().doubleValue());
					}
				}
				timesArray.add(year.getTimes());
				amountArray.add(this.format(year.getAmount()));
				dateArray.add(year.getDate());
			}
		}
		return R.ok().put("timesArray", timesArray).put("amountArray", amountArray).put("dateArray", dateArray)
				.put("totalTimes", totalTimes).put("totalAmount", totalAmount);
	}
	
	private Double format(Double amount) {
		DecimalFormat decimalFormat=new DecimalFormat(".00");
		amount = Double.parseDouble(decimalFormat.format(amount));
		return amount;
	}
	
}

