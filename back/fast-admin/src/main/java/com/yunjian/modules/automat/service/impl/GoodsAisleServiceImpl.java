package com.yunjian.modules.automat.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.Query;
import com.yunjian.common.utils.R;
import com.yunjian.modules.automat.dao.GoodsAisleMapper;
import com.yunjian.modules.automat.dao.OrderMapper;
import com.yunjian.modules.automat.entity.DeviceWarnEntity;
import com.yunjian.modules.automat.entity.GoodsAisleEntity;
import com.yunjian.modules.automat.entity.GoodsEntity;
import com.yunjian.modules.automat.entity.OrderDetailEntity;
import com.yunjian.modules.automat.entity.OrderEntity;
import com.yunjian.modules.automat.service.DeviceWarnService;
import com.yunjian.modules.automat.service.GoodsAisleService;
import com.yunjian.modules.automat.service.GoodsService;
import com.yunjian.modules.automat.service.OrderDetailService;
import com.yunjian.modules.automat.service.OrderService;
import com.yunjian.modules.automat.util.StringUtil;
import com.yunjian.modules.automat.vo.AisleExt;

/**
 * <p>
 * 货道信息管理表 服务实现类
 * </p>
 *
 * @author laizhiwen
 * @since 2019-07-01
 */
@Service("goodsAisleService")
public class GoodsAisleServiceImpl extends ServiceImpl<GoodsAisleMapper, GoodsAisleEntity>
		implements GoodsAisleService {
	
	private Logger logger = LoggerFactory.getLogger(GoodsAisleServiceImpl.class);

	@Autowired
	private GoodsService goodsService;
	@Autowired
	private DeviceWarnService deviceWarnService;
	
	@Resource
	private GoodsAisleMapper goodsAisleMapper;
	@Resource
	private OrderMapper orderMapper;
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderDetailService orderDetailService;
	@Autowired
	private GoodsAisleService goodsAisleService;
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${communication.base.path}")
	private String communicationBase;
	
	@Value("${communication.goodsOutUrl}")
	private String goodsOutUrl;
	@Value("${communication.cartOutUrl}")
	private String cartOutUrl;
	
	@Value("${locker.primary.total-number}")
	private Integer primaryTotalNumber;
	
	@Value("${locker.second.total-number}")
	private Integer secondTotalNumber;

	@SuppressWarnings("unchecked")
	@Override
	public PageUtils queryPage(Map<String, Object> params) throws IllegalAccessException, InvocationTargetException {
		String deviceCode = StringUtil.object2String(params.get("deviceCode"));
		QueryWrapper<GoodsAisleEntity> qwAisle = new QueryWrapper<GoodsAisleEntity>();
		if (StringUtils.isNotEmpty(deviceCode)) {
			qwAisle.eq("device_code", deviceCode);
		}
		IPage<GoodsAisleEntity> page = this.page(new Query<GoodsAisleEntity>().getPage(params), qwAisle);
		PageUtils pageData = new PageUtils(page);
		List<GoodsAisleEntity> list = (List<GoodsAisleEntity>) pageData.getList();
		List<AisleExt> listExt = Lists.newArrayList();
		for (GoodsAisleEntity aisle : list) {
			AisleExt aisleExt = new AisleExt();
			BeanUtils.copyProperties(aisleExt, aisle);
			QueryWrapper<GoodsEntity> qw = new QueryWrapper<GoodsEntity>();
			qw.eq("goods_id", aisle.getGoodsId());
			GoodsEntity goods = goodsService.getOne(qw);
			aisleExt.setGoods(goods);
			listExt.add(aisleExt);
		}
		pageData.setList(listExt);
		return pageData;
	}

	@Override
	public List<String> queryWarnDeviceCodes() {
		return goodsAisleMapper.queryWarnDeviceCodes();
	}

	@Override
	@Transactional
	public synchronized Integer sweepCode(OrderEntity order, String deviceCode, String sweepDeviceCode) {
		logger.info("订单[{}],扫码出货>>[{}]", order.getOrderNo(), sweepDeviceCode);
		QueryWrapper<OrderDetailEntity> detailQuery = new QueryWrapper<OrderDetailEntity>();
		detailQuery.eq("order_id", order.getOrderId());
		List<OrderDetailEntity> details = orderDetailService.list(detailQuery);
		logger.info("已获取订单详情，开始出货>>[{}]", details);
		QueryWrapper<GoodsAisleEntity> aisleQuery = new QueryWrapper<GoodsAisleEntity>();
		aisleQuery.eq("device_code", sweepDeviceCode.trim());
		List<GoodsAisleEntity> aisleList = goodsAisleService.list(aisleQuery);
		// 正常出货(正常订单只有一条detail)
		OrderDetailEntity detail = details.get(0);
		//等待出货的数量
		int goodsAmount = order.getGoodsAmount() - order.getOutSuccess();	//待出货商品数量
		//货柜商品总数量
		int remainAmount = 0;
		for (GoodsAisleEntity aisle : aisleList) {
			if (aisle.getGoodsId() == detail.getGoodsId()) {
				remainAmount += aisle.getGoodsQuantity();
			}
		}
		if (remainAmount < goodsAmount) {
			return 2; // 缺货
		}else {
			//订单状态改为已出货
			UpdateWrapper<OrderEntity> orderUpdate = new UpdateWrapper<OrderEntity>();
			orderUpdate.eq("order_id", order.getOrderId());
			orderUpdate.eq("pay_status", "1");
			OrderEntity o = new OrderEntity();
			o.setPayStatus(6);	//已出货
			o.setSweepTime(new Date());  //扫码取货时间
			if(!orderService.update(o, orderUpdate)){
				return 4;
			}
			int pikeupConut = 0;//已出货数量
			StringBuffer sblockerNos = new StringBuffer();//柜子号
			StringBuffer sbaisles = new StringBuffer();//货道号
			for (GoodsAisleEntity aisle : aisleList) {
				int aisleGoodsAmount = aisle.getGoodsQuantity();	//当前货道商品数量
//				int waitAmount = order.getGoodsAmount() - order.getOutSuccess();  //待出货商品数
				if (aisle.getGoodsId() == detail.getGoodsId() && aisleGoodsAmount > 0) {
					int localCount = 0;//本次循环取货数量
					for(int i=0; i<aisleGoodsAmount && pikeupConut<goodsAmount; i++) {
						// 下发出货指令
						pikeupConut ++;
						localCount ++;
//						sendGoodsOut(aisle, 0, order.getOrderNo());
						sblockerNos.append(aisle.getLockerNo()).append(",");
						sbaisles.append(aisle.getAisleNo()).append(",");
//						try {
//							Thread.sleep(10);//休眠 10ms
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						// 更新货道商品库存数量
//						aisleGoodsAmount = aisleGoodsAmount -1;
//						waitAmount = waitAmount - 1;
						
					}
					if(localCount > 0) {
						GoodsAisleEntity updateGoods = new GoodsAisleEntity();
						updateGoods.setGoodsQuantity(aisleGoodsAmount-localCount);
						updateGoods.setAisleId(aisle.getAisleId());
						UpdateWrapper<GoodsAisleEntity> updateWhere = new UpdateWrapper<GoodsAisleEntity>();
						updateWhere.eq("aisle_id", aisle.getAisleId());
						this.update(updateGoods, updateWhere);
					}
					
					logger.info("订单【{}】货道【{}】下发出货指令,本次订单数量【{}】,当前商品数量【{}】,已经出货【{}】", order.getOrderNo(),aisle, goodsAmount,aisleGoodsAmount, localCount);
				}
			}
			if(sblockerNos.length()>0) {
				sendCartOut(sweepDeviceCode.trim(),order.getOrderNo(),goodsAmount,sblockerNos.substring(0,sblockerNos.length()-1),sbaisles.substring(0,sbaisles.length()-1));
			}
			return 1;
		}
	}
	
	private String sendCartOut(String deviceCode,String orderNo, int goodsAmount, String lockerNos, String aisles) {
		logger.info("发送指令给设备{},订单号{},数量{}下行出货指令：{}--{}",deviceCode, orderNo, goodsAmount,lockerNos,aisles);
		Map<String,String> params = Maps.newHashMap();
		params.put("deviceCode", deviceCode);
		params.put("lockerNos", lockerNos);
		params.put("aisles", aisles);
		params.put("goodsCount", goodsAmount+"");
		params.put("orderNo", orderNo);
		params.put("time", new Date().getTime() + "");
		
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		String jsonStr = JSONObject.toJSONString(params);
		HttpEntity<String> formEntity = new HttpEntity<String>(jsonStr, headers);
		String result = restTemplate.postForObject(communicationBase + cartOutUrl, formEntity, String.class);
		logger.info("平台的响应结果为：{}", result);
		return result;
	}

	private String sendGoodsOut(GoodsAisleEntity aisle, int amount, String orderNo) {
		logger.info("发送指令给设备{},订单号{},下行出货指令：{}",aisle.getDeviceCode() , orderNo, amount);
		Map<String,String> params = Maps.newHashMap();
		params.put("deviceCode", aisle.getDeviceCode());
		params.put("lockerNo", aisle.getLockerNo()+"");
		params.put("aisle", aisle.getAisleNo()+"");
		params.put("goodsCount", amount+"");
		params.put("orderNo", orderNo);
		params.put("time", new Date().getTime() + "");
		
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		String jsonStr = JSONObject.toJSONString(params);
		HttpEntity<String> formEntity = new HttpEntity<String>(jsonStr, headers);
		String result = restTemplate.postForObject(communicationBase + goodsOutUrl, formEntity, String.class);
		logger.info("平台的响应结果为：{}", result);
		return result;
	}
	
	@SuppressWarnings("static-access")
	@Override
	public R saveAisle(GoodsAisleEntity aisle) {
		R r = null;
		QueryWrapper<GoodsAisleEntity> qw = new QueryWrapper<>();
		qw.eq("device_code", aisle.getDeviceCode());
		qw.eq("locker_no", aisle.getLockerNo());
		qw.eq("aisle_no", aisle.getAisleNo());
		GoodsAisleEntity a = this.getOne(qw);
		if(a != null) {
			r = R.error("设备：" + aisle.getDeviceCode() + " 柜号：" + aisle.getLockerNo() + " 货道号：" + aisle.getAisleNo() + " 已存在");
			return r;
		}
		save(aisle);
		r = r.ok();
		return r;
	}
	
	@Override
	public void checkStockout(String deviceCode) {
		QueryWrapper<GoodsAisleEntity> qw = new QueryWrapper<>();
		qw.eq("device_code", deviceCode);
		qw.le("goods_quantity", 2);
		int count = count(qw);
		if(count==0) {
			QueryWrapper<DeviceWarnEntity> qw2 = new QueryWrapper<>();
			qw.eq("device_code", deviceCode);
			deviceWarnService.remove(qw2);
		}
	}

	@Override
	public List<AisleExt> queryTaskAisle(String phoneNo) {
		List<AisleExt> list = goodsAisleMapper.queryTaskAisle(phoneNo);
		int totalNumber = 0;
		for(AisleExt a : list) {
			if(a.getLockerNo()==0) {
				totalNumber = primaryTotalNumber;
			}else {
				totalNumber = secondTotalNumber;
			}
			a.setGoodsQuantity(totalNumber - a.getGoodsQuantity());
		}
		return list;
	}

	@Override
	public int stockoutAisle(String deviceCode) {
		return goodsAisleMapper.stockoutAisle(deviceCode);
	}

}
