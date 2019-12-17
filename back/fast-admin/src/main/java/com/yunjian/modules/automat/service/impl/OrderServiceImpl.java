package com.yunjian.modules.automat.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.binarywang.wxpay.bean.entpay.EntPayRequest;
import com.github.binarywang.wxpay.bean.entpay.EntPayResult;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.EntPayService;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.EntPayServiceImpl;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.yunjian.common.utils.DateUtils;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.Query;
import com.yunjian.common.utils.R;
import com.yunjian.modules.automat.dao.OrderMapper;
import com.yunjian.modules.automat.entity.FeedbackEntity;
import com.yunjian.modules.automat.entity.GoodsEntity;
import com.yunjian.modules.automat.entity.IntegralEntity;
import com.yunjian.modules.automat.entity.OrderDetailEntity;
import com.yunjian.modules.automat.entity.OrderEntity;
import com.yunjian.modules.automat.entity.WxAccountEntity;
import com.yunjian.modules.automat.entity.WxConfigEntity;
import com.yunjian.modules.automat.exception.PaymentGatewayException;
import com.yunjian.modules.automat.service.FeedbackService;
import com.yunjian.modules.automat.service.GoodsAisleService;
import com.yunjian.modules.automat.service.GoodsService;
import com.yunjian.modules.automat.service.IntegralService;
import com.yunjian.modules.automat.service.OrderDetailService;
import com.yunjian.modules.automat.service.OrderService;
import com.yunjian.modules.automat.service.SignatureService;
import com.yunjian.modules.automat.service.WxAccountService;
import com.yunjian.modules.automat.service.WxConfigService;
import com.yunjian.modules.automat.util.JsonUtil;
import com.yunjian.modules.automat.util.QRCodeUtil;
import com.yunjian.modules.automat.util.StrUtil;
import com.yunjian.modules.automat.util.UUIDUtil;
import com.yunjian.modules.automat.vo.DeviceRankVo;
import com.yunjian.modules.automat.vo.GoodsDto;
import com.yunjian.modules.automat.vo.OrderExt;
import com.yunjian.modules.automat.vo.UploadCartOutVo;
import com.yunjian.modules.automat.vo.UploadGoodsOutVo;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-16
 */
@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderEntity> implements OrderService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderDetailService orderDetailService;
	@Autowired
	private SignatureService signatureService;
	@Autowired
	private WxConfigService wxConfigService;
	@Autowired
	private WxAccountService wxAccountService;
	@Autowired
	private FeedbackService feedbackService;
	@Autowired
	private IntegralService integralService;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private GoodsAisleService goodsAisleService;

	@Value("${user.sign.key}")
	private String userSignKey = "";

	@Value("${qr.code.prefix}")
	private String prefix = "";

	@Value("${wechat.pay.keyPath}")
	private String keyPath = "";

	@Value("${oss.endpoint}")
	private String endpoint;
	@Value("${oss.accessKeyId}")
	private String accessKeyId;
	@Value("${oss.accessKeySecret}")
	private String accessKeySecret;
	@Value("${oss.bucketName}")
	private String bucketName;
	@Value("${oss.publicUrl}")
	private String publicUrl;
	// 文件存储目录
	private String filedir = "automat/";

	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String orderCode = (String) params.get("orderCode");
		String phoneNo = (String) params.get("phoneNo");
		String startTime = (String) params.get("startTime");
		String endTime = (String) params.get("endTime");
		IPage<OrderEntity> page = this.page(new Query<OrderEntity>().getPage(params),
				new QueryWrapper<OrderEntity>().like(StringUtils.isNotBlank(orderCode), "order_code", orderCode)
						.like(StringUtils.isNotBlank(phoneNo), "phone_no", phoneNo)
						.ge(StringUtils.isNotBlank(startTime), "create_time", startTime)
						.le(StringUtils.isNotBlank(endTime), "create_time", endTime).orderByDesc("create_time"));
		return new PageUtils(page);
	}

	@Override
	public void saveGoods(OrderEntity donateRule) {

	}

	@Override
	public List<OrderExt> findByPhoneNo(String phoneNo) {
		return orderMapper.findByPhoneNo(phoneNo);
	}

	@Override
	public boolean deleteByOrderId(String orderId) {
		int n = orderMapper.deleteByOrderId(orderId);
		return n > 0 ? true : false;
	}

	@Override
	public OrderEntity findOrder(OrderEntity order) {
		return orderMapper.findOrder(order);
	}

	@Override
	public int dayOrderCount() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date date = calendar.getTime();

		QueryWrapper<OrderEntity> query = new QueryWrapper<OrderEntity>();
		query.ge("create_time", date).in("pay_status", Arrays.asList(1, 2, 3, 5, 6));
		return this.count(query);
	}

	@Override
	public int weekOrderCount() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_YEAR, -7);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date date = calendar.getTime();

		QueryWrapper<OrderEntity> query = new QueryWrapper<OrderEntity>();
		query.ge("create_time", date).in("pay_status", Arrays.asList(1, 2, 3, 5, 6));
		return this.count(query);
	}

	@Override
	public PageUtils queryHomeDeviceRankPage(Map<String, Object> params) {
		int page = Integer.parseInt(params.get("page").toString());
		int limit = Integer.parseInt(params.get("limit").toString());
		int offset = limit * (page - 1);
		List<DeviceRankVo> list = orderMapper.queryHomeDeviceRankPage(offset, limit);
		int totalCount = orderMapper.queryHomeDeviceRankCount();
		PageUtils pageResult = new PageUtils(list, totalCount, limit, page);
		return pageResult;
	}

	/**
	 * 通过微信支付订单
	 */
	@Override
	public R payOrder(OrderExt order, HttpServletRequest request) {
		WxPayMpOrderResult result = null;
		int orderId = 0;
		String qrcode = "";
		try {
			if (order.getPayWay() == 2) { // 钱包支付
				QueryWrapper<WxAccountEntity> queryWrapper = new QueryWrapper<WxAccountEntity>();
				queryWrapper.eq("phone_no", order.getMobile());
				WxAccountEntity user = wxAccountService.list(queryWrapper).get(0);
				BigDecimal totalAmount = new BigDecimal(0); // 总金额
				for (GoodsDto g : order.getGoodsList()) {
					totalAmount = totalAmount.add(g.getUnitPrice().multiply(BigDecimal.valueOf(g.getAmount())));
				}
				if (user.getAmount().doubleValue() < totalAmount.doubleValue()) {
					return R.error("钱包余额不足");
				} else {
					// 创建支付订单
					OrderEntity createOrder = createYueOrder(order, user);
					logger.info("订单[{}]生成二维码地址>>>[{}]", createOrder.getOrderId(), createOrder.getQrCodeImage());
					qrcode = createOrder.getQrCodeImage();
					orderId = createOrder.getOrderId();
					// 更新用户信息
					QueryWrapper<OrderEntity> orderQuery = new QueryWrapper<OrderEntity>();
					orderQuery.eq("order_no", createOrder.getOrderNo());
					OrderEntity o = this.getOne(orderQuery);
					user.setAmount(user.getAmount().subtract(new BigDecimal(o.getTotalAmount() + "")));
					user.setDonate(user.getDonate().subtract(new BigDecimal(o.getDonateAmount() + "")));
					user.setIntegral(user.getIntegral() + o.getIntegral());
					wxAccountService.saveOrUpdate(user);

					IntegralEntity integral = new IntegralEntity();
					integral.setPhoneNo(user.getPhoneNo());
					integral.setScore(o.getIntegral());
					integral.setMoney(o.getTotalAmount());
					integral.setType(2);
					integral.setSource(o.getOrderId());
					integral.setCreateTime(new Date());
					integralService.save(integral);
				}
			} else { // 微信支付
				// 创建支付订单
				OrderEntity createOrder = createWxOrder(order);
				logger.info("订单[{}]生成二维码地址>>>[{}]", createOrder.getOrderId(), createOrder.getQrCodeImage());
				qrcode = createOrder.getQrCodeImage();
				orderId = createOrder.getOrderId();
				QueryWrapper<OrderEntity> queryWrapper = new QueryWrapper<OrderEntity>();
				queryWrapper.eq("order_no", createOrder.getOrderNo());
				OrderEntity o = this.getOne(queryWrapper);
				WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
				WxConfigEntity config = wxConfigService.list().get(0);
				orderRequest.setAppid(config.getAppId());
				orderRequest.setMchId(config.getMchId());
				orderRequest.setNotifyUrl(config.getOrderNotify());
				orderRequest.setSign(getSign(o));
				orderRequest.setSignType("MD5");
				orderRequest.setNonceStr(UUIDUtil.getUUID());
				orderRequest.setTradeType("JSAPI");
				orderRequest.setOpenid(order.getOpenId());
				orderRequest.setOutTradeNo(o.getOrderNo());
				orderRequest.setBody("支付订单");
				int fee = (int) (o.getTotalAmount() * 100);
				orderRequest.setTotalFee(fee);
				orderRequest.setSpbillCreateIp(request.getRemoteAddr());
				logger.info("send create weixin order request>>>[{}]", JsonUtil.toJsonString(orderRequest));
				WxPayConfig payConfig = new WxPayConfig();
				payConfig.setAppId(config.getAppId());
				payConfig.setMchId(config.getMchId());
				payConfig.setNotifyUrl(config.getOrderNotify());
				payConfig.setMchKey(config.getMchKey());
				WxPayService wxPayService = new WxPayServiceImpl();
				wxPayService.setConfig(payConfig);
				result = wxPayService.createOrder(orderRequest);
				logger.info("create order weixin response>>>[{}]", JsonUtil.toJsonString(result));
			}
		} catch (Exception e) {
			logger.info("weixin pre pay error", e);
			return R.error("微信支付失败");
		}
		return R.ok("返回结果正常").put("result", result).put("orderId", orderId).put("qrCodeImg", qrcode);
	}

	@Override
	public R weixinPayOrderNotify(HttpServletRequest request, HttpServletResponse response) {
		String xmlResult = null;
		try {
			xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
		} catch (IOException e) {
			logger.info("weinxin pay notify error", e);
			return R.error("微信支付回调失败");
		}
		WxPayOrderNotifyResult result = null;
		try {
			WxConfigEntity config = wxConfigService.list().get(0);
			WxPayConfig payConfig = new WxPayConfig();
			payConfig.setAppId(config.getAppId());
			payConfig.setMchId(config.getMchId());
			payConfig.setNotifyUrl(config.getOrderNotify());
			payConfig.setMchKey(config.getMchKey());
			payConfig.setSignType("MD5");
			WxPayService wxPayService = new WxPayServiceImpl();
			wxPayService.setConfig(payConfig);
			result = wxPayService.parseOrderNotifyResult(xmlResult);
			logger.info("weixin platform notify result>>>[{}]", result);
			if (WxPayConstants.ResultCode.SUCCESS.equalsIgnoreCase(result.getResultCode())) {
				logger.info("微信支付成功回调>>[{}]", xmlResult);
				// 更新订单信息
				String tradeNo = result.getOutTradeNo();
				String transactionId = result.getTransactionId();
				String totalFee = BaseWxPayResult.fenToYuan(result.getTotalFee());
				QueryWrapper<OrderEntity> queryWrapper = new QueryWrapper<OrderEntity>();
				queryWrapper.eq("order_no", tradeNo);
				OrderEntity record = this.getOne(queryWrapper);
				if (record == null) {
					return R.error("订单不存在");
				}
				if (!totalFee.equals(record.getTotalAmount() + "")) {
					return R.error("金额不匹配");
				}
				if (record.getPayStatus() == 0) {
					record.setWeixinOrderNo(transactionId);
					record.setPayStatus(1);
					record.setPayTime(new Date());
					this.saveOrUpdate(record);

					// 修改用户积分信息
					IntegralEntity integral = new IntegralEntity();
					integral.setPhoneNo(record.getPhoneNo());
					integral.setScore(record.getIntegral());
					integral.setMoney(record.getTotalAmount());
					integral.setType(2);
					integral.setSource(record.getOrderId());
					integral.setCreateTime(new Date());
					integralService.save(integral);

					QueryWrapper<WxAccountEntity> userWrapper = new QueryWrapper<WxAccountEntity>();
					userWrapper.eq("phone_no", record.getPhoneNo());
					WxAccountEntity user = wxAccountService.getOne(userWrapper);
					user.setIntegral(user.getIntegral() + record.getIntegral());
					wxAccountService.saveOrUpdate(user);

					// 生成二维码
					String codeImage = this.createQrcodeImage(record.getQrCode());
					record.setQrCodeImage(codeImage);
					this.saveOrUpdate(record);
				}
			}
			if (WxPayConstants.ResultCode.FAIL.equalsIgnoreCase(result.getReturnCode())) {
				logger.error("微信支付失败回调>>[{}]", xmlResult);
				throw new WxPayException("微信通知支付失败！");
			}
		} catch (WxPayException e) {
			logger.info("weinxin pay notify error", e);
			return R.error("微信支付回调失败");
		}
		return R.ok();
	}

	@Override
	public R refund(FeedbackEntity entity, String phone, String deviceCode) {
		QueryWrapper<OrderEntity> queryWrapper = new QueryWrapper<OrderEntity>();
		queryWrapper.eq("order_code", deviceCode);
		OrderEntity order = this.getOne(queryWrapper);
		if (order == null) {
			return R.error("订单不存在");
		}
		if (order.getPayWay() == 1) {
			// 微信支付退款
			WxPayRefundRequest wxPayRefundRequest = new WxPayRefundRequest();
			WxConfigEntity config = wxConfigService.list().get(0);

			wxPayRefundRequest.setAppid(config.getAppId());
			wxPayRefundRequest.setMchId(config.getMchId());
			wxPayRefundRequest.setSignType("MD5");
			wxPayRefundRequest.setSign(getSign(order));
			wxPayRefundRequest.setNonceStr(UUIDUtil.getUUID());
			wxPayRefundRequest.setOutTradeNo(order.getOrderNo());
			wxPayRefundRequest.setOutRefundNo(getTradeNo());
			wxPayRefundRequest.setRefundDesc("用户退款");
			// 元转成分
			int fee = (int) (order.getTotalAmount() * 100);
			wxPayRefundRequest.setTotalFee(fee);
			wxPayRefundRequest.setRefundFee(fee);
			WxPayRefundResult wxPayRefundResult = null;
			try {
				WxPayConfig payConfig = new WxPayConfig();
				payConfig.setAppId(config.getAppId());
				payConfig.setMchId(config.getMchId());
				payConfig.setNotifyUrl(config.getRefundNotify());
				payConfig.setMchKey(config.getMchKey());
				payConfig.setKeyPath(keyPath);
				WxPayService wxPayService = new WxPayServiceImpl();
				wxPayService.setConfig(payConfig);
				wxPayRefundResult = wxPayService.refund(wxPayRefundRequest);
				logger.info("微信退款返回结果>>[{}]", JsonUtil.toJsonString(wxPayRefundResult));
			} catch (WxPayException e) {
				order.setPayStatus(5); // 退款失败
				this.saveOrUpdate(order);
				entity.setStatus(2); // 已处理
				feedbackService.saveOrUpdate(entity);
				logger.error("订单退款失败>>[{}]", e);
				return R.error("订单退款失败");
			}
			if (!wxPayRefundResult.getReturnCode().equalsIgnoreCase("SUCCESS")) {
				logger.warn("refund fail: " + wxPayRefundResult.getReturnMsg());
				order.setPayStatus(5); // 退款失败
				this.saveOrUpdate(order);
				entity.setStatus(2); // 已处理
				feedbackService.saveOrUpdate(entity);
				return R.error("订单退款失败");
			}
			if (wxPayRefundResult.getResultCode().equalsIgnoreCase("SUCCESS")) {
				String outRefundNo = wxPayRefundResult.getOutRefundNo();
				order.setPayStatus(4); // 已退款
				order.setRefundNo(outRefundNo);
				order.setRefundTime(new Date());
				order.setRefundAmount(order.getTotalAmount());
				this.saveOrUpdate(order);
				entity.setStatus(2); // 已处理
				feedbackService.saveOrUpdate(entity);
				
				//修改积分明细，因为没有定义积分赠送回收规则，前端也不展示，所以此处只保存记录,，积分暂且设置为0
				IntegralEntity integral = new IntegralEntity();
				integral.setPhoneNo(order.getPhoneNo());
				integral.setScore(0);
				integral.setMoney(order.getTotalAmount());
				integral.setType(4);
				integral.setSource(order.getOrderId());
				integral.setCreateTime(new Date());
				integralService.save(integral);
			}
		} else if (order.getPayWay() == 2) {
			// 直接修改用户余额
			QueryWrapper<WxAccountEntity> query = new QueryWrapper<WxAccountEntity>();
			query.eq("phone_no", order.getPhoneNo());
			WxAccountEntity user = wxAccountService.getOne(query);
			user.setAmount(user.getAmount().add(BigDecimal.valueOf(order.getTotalAmount())));
			wxAccountService.saveOrUpdate(user);
			order.setRefundTime(new Date());
			order.setPayStatus(4); // 已退款
			this.saveOrUpdate(order);

			entity.setStatus(2); // 已处理
			feedbackService.saveOrUpdate(entity);
			
			IntegralEntity integral = new IntegralEntity();
			integral.setPhoneNo(order.getPhoneNo());
			integral.setScore(0);
			integral.setMoney(order.getTotalAmount());
			integral.setType(4);
			integral.setSource(order.getOrderId());
			integral.setCreateTime(new Date());
			integralService.save(integral);
			/*
			 * QueryWrapper<WxAccountEntity> query = new QueryWrapper<WxAccountEntity>();
			 * query.eq("phone_no", order.getPhoneNo()); WxAccountEntity user =
			 * wxAccountService.getOne(query);
			 * 
			 * // 钱包支付退款 EntPayResult entpayResult = null; try { WxConfigEntity config =
			 * wxConfigService.list().get(0); WxPayConfig payConfig = new WxPayConfig();
			 * payConfig.setAppId(config.getAppId()); payConfig.setMchId(config.getMchId());
			 * payConfig.setNotifyUrl(config.getOrderNotify());
			 * payConfig.setMchKey(config.getMchKey()); WxPayService wxPayService = new
			 * WxPayServiceImpl(); wxPayService.setConfig(payConfig);
			 * 
			 * EntPayRequest entpayRequest = new EntPayRequest();
			 * entpayRequest.setAppid(config.getAppId());
			 * entpayRequest.setMchId(config.getMchId());
			 * entpayRequest.setOpenid(user.getWxOpenId());
			 * entpayRequest.setPartnerTradeNo(getTradeNo());
			 * entpayRequest.setCheckName("NO_CHECK"); // 元转成分 int fee = (int)
			 * (order.getTotalAmount() * 100); entpayRequest.setAmount(fee);
			 * entpayRequest.setSignType("MD5"); entpayRequest.setDescription("用户退款");
			 * EntPayService entPayService = new EntPayServiceImpl(wxPayService);
			 * entpayResult = entPayService.entPay(entpayRequest); if
			 * (!entpayResult.getResultCode().equalsIgnoreCase("SUCCESS")) {
			 * logger.warn("refund fail: " + entpayResult.getReturnMsg()); return
			 * R.error("订单退款失败"); } else { String outRefundNo =
			 * entpayResult.getPartnerTradeNo(); order.setPayStatus(4); // 已退款
			 * order.setRefundNo(outRefundNo); this.saveOrUpdate(order);
			 * entity.setStatus(2); // 已处理 feedbackService.saveOrUpdate(entity); } } catch
			 * (WxPayException e) { e.printStackTrace(); return R.error("订单退款失败"); }
			 */
		}
		return R.ok("退款申请已经成功，将在1-3个工作日内处理");
	}

	@Override
	public R getCash(WxAccountEntity account) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();

		QueryWrapper<WxAccountEntity> query = new QueryWrapper<WxAccountEntity>();
		query.eq("phone_no", account.getPhoneNo());
		WxAccountEntity user = wxAccountService.getOne(query);

		// 商户余额转账到个人微信账户
		EntPayResult entpayResult = null;
		try {
			WxConfigEntity config = wxConfigService.list().get(0);
			WxPayConfig payConfig = new WxPayConfig();
			payConfig.setAppId(config.getAppId());
			payConfig.setMchId(config.getMchId());
			payConfig.setNotifyUrl(config.getOrderNotify());
			payConfig.setMchKey(config.getMchKey());
			payConfig.setKeyPath(keyPath);
			WxPayService wxPayService = new WxPayServiceImpl();
			wxPayService.setConfig(payConfig);

			EntPayRequest entpayRequest = new EntPayRequest();
			entpayRequest.setAppid(config.getAppId());
			entpayRequest.setMchId(config.getMchId());
			entpayRequest.setOpenid(user.getWxOpenId());
			entpayRequest.setPartnerTradeNo(getTradeNo());
			entpayRequest.setSpbillCreateIp(request.getRemoteAddr());
			entpayRequest.setCheckName("NO_CHECK");
			// 元转成分
			int fee = (int) (user.getAmount().doubleValue() * 100);
			entpayRequest.setAmount(fee);
			entpayRequest.setSignType("MD5");
			entpayRequest.setDescription("用户提现");
			EntPayService entPayService = new EntPayServiceImpl(wxPayService);
			entpayResult = entPayService.entPay(entpayRequest);
			if (!entpayResult.getResultCode().equalsIgnoreCase("SUCCESS")) {
				logger.warn("refund fail: " + entpayResult.getReturnMsg());
				return R.error("用户提现失败");
			} else {
				user.setAmount(user.getAmount().subtract(user.getAmount()));
				user.setSign(StrUtil.createAccountSign(user.getPhoneNo(), user.getAmount(), userSignKey));
				wxAccountService.saveOrUpdate(user);
				//未定义积分规则，先暂时置为0
				IntegralEntity integral = new IntegralEntity();
				integral.setPhoneNo(user.getPhoneNo());
				integral.setScore(0);
				integral.setMoney(user.getAmount().floatValue());
				integral.setType(3);
				integral.setSource(0);
				integral.setCreateTime(new Date());
				integralService.save(integral);
			}
		} catch (WxPayException e) {
			e.printStackTrace();
			return R.error("用户提现失败");
		}
		return R.ok();
	}

	/**
	 * 创建支付订单
	 * 
	 * @param order
	 */
	@Transactional
	private OrderEntity createWxOrder(OrderExt order) {
		String qrUrl = "";
		OrderEntity entity = new OrderEntity();
		try {
			List<GoodsDto> goods = order.getGoodsList();
			int goodsAmount = 0; // 总数量
			BigDecimal totalAmount = new BigDecimal(0); // 总金额
			for (GoodsDto g : goods) {
				totalAmount = totalAmount.add(g.getUnitPrice().multiply(BigDecimal.valueOf(g.getAmount())));
				goodsAmount += g.getAmount();
			}
			entity.setDeviceCode(order.getDeviceCode());
			entity.setCreateTime(new Date());
			entity.setGoodsAmount(goodsAmount);
			entity.setTotalAmount(totalAmount.floatValue());
			entity.setIntegral((totalAmount.doubleValue() < 1) ? 1 : totalAmount.intValue());
			entity.setOrderNo(getTradeNo());
			entity.setWeixinOrderNo("");
			entity.setPayStatus(0);
			entity.setPayTime(null);
			entity.setPhoneNo(order.getMobile());
			entity.setPayWay(order.getPayWay()); // 支付方式1微信支付 2钱包支付
			qrUrl = MessageFormat.format(prefix, entity.getOrderNo(), entity.getDeviceCode());
			logger.info("订单【{}】二维码内容>>>【{}】", entity.getOrderNo(), qrUrl);
			entity.setQrCode(qrUrl);
			String codeImage = this.createQrcodeImage(qrUrl);
			entity.setQrCodeImage(codeImage);
			entity.setOrderCode(createOrderCode());
			this.saveOrUpdate(entity);

			QueryWrapper<OrderEntity> queryWrapper = new QueryWrapper<OrderEntity>();
			queryWrapper.eq("order_no", entity.getOrderNo());
			OrderEntity o = this.getOne(queryWrapper);

			List<GoodsEntity> goodsList = goodsService.list();
			try {
				List<OrderDetailEntity> detailList = new ArrayList<OrderDetailEntity>();
				for (GoodsDto g : goods) {
					OrderDetailEntity detail = new OrderDetailEntity();
					detail.setOrderId(o.getOrderId());
					detail.setGoodsId(g.getGoodsId());
					detail.setGoodsAmount(g.getAmount());
					detail.setGoodsPrice(g.getUnitPrice().multiply(BigDecimal.valueOf(g.getAmount())).floatValue());
					for (GoodsEntity item : goodsList) {
						if (g.getGoodsId() == item.getGoodsId()) {
							detail.setGoodsName(item.getGoodsName());
						}
					}
					detail.setCreateTime(new Date());
					detailList.add(detail);
				}
				orderDetailService.saveBatch(detailList);
			} catch (Exception e) {
				this.remove(queryWrapper);
				logger.error("订单详情创建失败>>[{}]", e);
				return null;
			}
		} catch (Exception e) {
			logger.error("订单创建失败>>[{}]", e);
			return null;
		}
		return entity;
	}

	@Transactional
	private OrderEntity createYueOrder(OrderExt order, WxAccountEntity user) {
		OrderEntity entity = new OrderEntity();
		String qrCodeUrl = "";
		try {
			List<GoodsDto> goods = order.getGoodsList();
			int goodsAmount = 0; // 总数量
			BigDecimal totalAmount = new BigDecimal(0); // 总金额
			for (GoodsDto g : goods) {
				totalAmount = totalAmount.add(g.getUnitPrice().multiply(BigDecimal.valueOf(g.getAmount())));
				goodsAmount += g.getAmount();
			}
			entity.setDeviceCode(order.getDeviceCode());
			entity.setCreateTime(new Date());
			entity.setGoodsAmount(goodsAmount);

			float donate = (float) (totalAmount.floatValue() * 0.3 < user.getDonate().floatValue()
					? totalAmount.floatValue() * 0.3
					: user.getDonate().floatValue());
			float amount = totalAmount.floatValue() - donate;
			entity.setTotalAmount(amount);
			entity.setDonateAmount(donate);
			entity.setIntegral((totalAmount.doubleValue() < 1) ? 1 : totalAmount.intValue());
			entity.setOrderNo(getTradeNo());
			entity.setWeixinOrderNo("");
			entity.setPayStatus(1);
			entity.setPayTime(new Date());
			entity.setPhoneNo(order.getMobile());
			entity.setPayWay(order.getPayWay()); // 支付方式1微信支付 2钱包支付
			qrCodeUrl = MessageFormat.format(prefix, entity.getOrderNo(), entity.getDeviceCode());
			logger.info("订单【{}】二维码内容>>>【{}】", entity.getOrderNo(), qrCodeUrl);
			String codeImage = this.createQrcodeImage(qrCodeUrl);
			entity.setQrCodeImage(codeImage);
			entity.setQrCode(qrCodeUrl);
			entity.setOrderCode(createOrderCode());
			this.saveOrUpdate(entity);

			QueryWrapper<OrderEntity> queryWrapper = new QueryWrapper<OrderEntity>();
			queryWrapper.eq("order_no", entity.getOrderNo());
			OrderEntity o = this.getOne(queryWrapper);
			try {
				List<OrderDetailEntity> detailList = new ArrayList<OrderDetailEntity>();
				List<GoodsEntity> goodsList = goodsService.list();
				for (GoodsDto g : goods) {
					OrderDetailEntity detail = new OrderDetailEntity();
					detail.setOrderId(o.getOrderId());
					detail.setGoodsId(g.getGoodsId());
					detail.setGoodsAmount(g.getAmount());
					detail.setGoodsPrice(g.getUnitPrice().multiply(BigDecimal.valueOf(g.getAmount())).floatValue());
					for (GoodsEntity item : goodsList) {
						if (g.getGoodsId() == item.getGoodsId()) {
							detail.setGoodsName(item.getGoodsName());
						}
					}
					detail.setCreateTime(new Date());
					detailList.add(detail);
				}
				orderDetailService.saveBatch(detailList);
			} catch (Exception e) {
				this.remove(queryWrapper);
				logger.error("订单详情创建失败>>[{}]", e);
				return entity;
			}
		} catch (Exception e) {
			logger.error("订单创建失败>>[{}]", e);
			return entity;
		}
		return entity;
	}

	private String getTradeNo() {
		StringBuilder sb = new StringBuilder("CS");
		sb.append("weixin");
		String str = UUIDUtil.getUUID().replaceAll("-", "");
		sb.append(str.substring(8));
		return sb.toString();
	}

	private String createQrcodeImage(String content) {
		String fileName = UUID.randomUUID().toString().replaceAll("-", "") + ".png";
		String qrCodeUrl = "";
		try {
			InputStream in = QRCodeUtil.createQRCode(content, 1500, fileName);
			qrCodeUrl = publicUrl + filedir + fileName;
			this.uploadFile2OSS(in, fileName);
		} catch (Exception e) {
			logger.error("生成二维码出错>>[{}]", e);
			return "";
		}
		return qrCodeUrl;
	}

	/**
	 * 上传图片获取fileUrl
	 * 
	 * @param instream
	 * @param fileName
	 * @return
	 */
	public String uploadFile2OSS(InputStream instream, String fileName) {
		String ret = "";
		try {
			// 创建上传Object的Metadata
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentLength(instream.available());
			objectMetadata.setCacheControl("no-cache");
			objectMetadata.setHeader("Pragma", "no-cache");
			objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
			objectMetadata.setContentDisposition("inline; filename=" + fileName);
			// 上传文件
			OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
			PutObjectResult putResult = ossClient.putObject(bucketName, filedir + fileName, instream, objectMetadata);
			ret = putResult.getETag();
		} catch (IOException e) {
			logger.error("订单创建失败>>[{}]", e);
			return "";
		} finally {
			try {
				if (instream != null) {
					instream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	/**
	 * @return 签名字符串
	 */
	@SuppressWarnings("rawtypes")
	private String getSign(OrderEntity bill) {
		String billForWeiXinJsonString;
		try {
			billForWeiXinJsonString = objectMapper.writeValueAsString(bill);
		} catch (JsonProcessingException e) {
			logger.info(e.getMessage());
			throw new PaymentGatewayException("transform error!");
		}
		logger.info("billForWeiXinJsonString:>>>[{}]", bill);
		Map signatureMap = JsonUtil.toMap(billForWeiXinJsonString);
		return signatureService.weixinSignature(signatureMap);
	}

	public String getcontentType(String FilenameExtension) {
		if (FilenameExtension.equalsIgnoreCase(".bmp")) {
			return "image/bmp";
		}
		if (FilenameExtension.equalsIgnoreCase(".gif")) {
			return "image/gif";
		}
		if (FilenameExtension.equalsIgnoreCase(".jpeg") || FilenameExtension.equalsIgnoreCase(".jpg")
				|| FilenameExtension.equalsIgnoreCase(".png")) {
			return "image/jpeg";
		}
		if (FilenameExtension.equalsIgnoreCase(".html")) {
			return "text/html";
		}
		if (FilenameExtension.equalsIgnoreCase(".txt")) {
			return "text/plain";
		}
		if (FilenameExtension.equalsIgnoreCase(".vsd")) {
			return "application/vnd.visio";
		}
		if (FilenameExtension.equalsIgnoreCase(".pptx") || FilenameExtension.equalsIgnoreCase(".ppt")) {
			return "application/vnd.ms-powerpoint";
		}
		if (FilenameExtension.equalsIgnoreCase(".docx") || FilenameExtension.equalsIgnoreCase(".doc")) {
			return "application/msword";
		}
		if (FilenameExtension.equalsIgnoreCase(".xml")) {
			return "text/xml";
		}
		return "image/jpeg";
	}

	private String createOrderCode() {
		StringBuilder sb = new StringBuilder();
		String date = DateUtils.format(new Date(), "yyyyMMddHHmmss");
		sb.append("XF").append(date);
		Random random = new Random();
		sb.append(random.nextInt(9000) + 1000);
		return sb.toString();
	}
	
	public static void main(String[] args) {
		StringBuilder sb = new StringBuilder();
		String date = DateUtils.format(new Date(), "yyyyMMddHHmmss");
		sb.append("XF").append(date);
		Random random = new Random();
		sb.append(random.nextInt(9000) + 1000);
		System.out.println(sb.toString());
	}

	@Override
	@Transactional
	public void uploadGoodsOut(UploadGoodsOutVo param) {
		try {
			List<OrderEntity> list = orderMapper.queryOrderByOrderNo(param.getOrderNo());
			if(list != null && !list.isEmpty()) {
				OrderEntity order = list.get(0);
				if(param.getResult().equals("1")) {
					int success = order.getOutSuccess() + 1;
					order.setOutSuccess(success);
//					if(success == order.getGoodsAmount()) {
//						order.setPayStatus(6);
//					}
				}else if(param.getResult().equals("0")) {
					order.setOutFail(order.getOutFail() + 1);
					//恢复货道库存 和 订单状态
					// dinggengting modify 20190827
//					QueryWrapper<GoodsAisleEntity> query = new QueryWrapper<GoodsAisleEntity>();
//					query.eq("device_code", param.getDeviceCode()).eq("locker_no", param.getLockerNo()).eq("aisle_no", param.getAisle());
//					List<GoodsAisleEntity> aisleList = goodsAisleService.list(query);
//					if(aisleList != null && !list.isEmpty()) {
//						GoodsAisleEntity aisle = goodsAisleService.list(query).get(0);
//						aisle.setGoodsQuantity(aisle.getGoodsQuantity() + 1);
//						goodsAisleService.saveOrUpdate(aisle);
//						order.setPayStatus(1);
//					}else {
//						logger.error("货道不存在>>>[{}]", param);
//					}
				}
				this.saveOrUpdate(order);
			
			}
		} catch (Exception e) {
			logger.error("出货结果同步操作失败",  e);
		}
	}
	@Override
	@Transactional
	public void uploadCartOut(UploadCartOutVo param) {
		try {
			List<OrderEntity> list = orderMapper.queryOrderByOrderNo(param.getOrderNo());
			if(list != null && !list.isEmpty()) {
				OrderEntity order = list.get(0);
				String[] results = param.getResult().split(";");
				int success = 0;
				int error = 0;
				for(String result :results) {
					if(result == null || result.trim().length()==0) {
						continue;
					}
					String[] objs = result.trim().split(",");
					if(objs[2].trim().equals("1")) {
						success ++;
					}else {
						error ++;
						logger.error("扫码取货执行结果，机器码{}>>订单号{},柜子号{},货道{},错误码{}", param.getDeviceCode(),param.getOrderNo(),objs[0],objs[1],objs[3]);
					}
				}
				if(success > 0 || error > 0) {
					order.setOutSuccess(order.getOutSuccess()+success);
					order.setOutFail(order.getOutFail() + error);
					this.saveOrUpdate(order);
				}
				logger.info("扫码取货执行结果{}>>成功{},失败{}", param.getOrderNo(),success,error);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("出货结果同步操作失败{}",  param.getOrderNo());
		}
	}

}
