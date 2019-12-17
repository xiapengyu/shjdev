package com.yunjian.modules.automat.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.Query;
import com.yunjian.common.utils.R;
import com.yunjian.modules.automat.dao.RechargeRecordMapper;
import com.yunjian.modules.automat.entity.DonateRecordEntity;
import com.yunjian.modules.automat.entity.DonateRuleEntity;
import com.yunjian.modules.automat.entity.IntegralEntity;
import com.yunjian.modules.automat.entity.RechargeRecordEntity;
import com.yunjian.modules.automat.entity.WxAccountEntity;
import com.yunjian.modules.automat.entity.WxConfigEntity;
import com.yunjian.modules.automat.exception.PaymentGatewayException;
import com.yunjian.modules.automat.service.DonateRecordService;
import com.yunjian.modules.automat.service.DonateRuleService;
import com.yunjian.modules.automat.service.IntegralService;
import com.yunjian.modules.automat.service.RechargeRecordService;
import com.yunjian.modules.automat.service.SignatureService;
import com.yunjian.modules.automat.service.WxAccountService;
import com.yunjian.modules.automat.service.WxConfigService;
import com.yunjian.modules.automat.util.IpUtil;
import com.yunjian.modules.automat.util.JsonUtil;
import com.yunjian.modules.automat.util.StrUtil;
import com.yunjian.modules.automat.util.StringUtil;
import com.yunjian.modules.automat.util.UUIDUtil;
import com.yunjian.modules.automat.vo.RechargeReqDto;

/**
 * <p>
 * 充值记录 服务实现类
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-18
 */
@Service("rechargeRecordService")
public class RechargeRecordServiceImpl extends ServiceImpl<RechargeRecordMapper, RechargeRecordEntity>
		implements RechargeRecordService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private DonateRuleService donateRuleServiceImpl;
	@Autowired
	private SignatureService signatureService;
	@Autowired
	private WxAccountService wxAccountService;
	@Autowired
	private WxConfigService wxConfigService;
	@Autowired
	private IntegralService integralService;
	@Autowired
	private DonateRecordService donateRecordService;

	@Value("${user.sign.key}")
	private String userSignKey = "";

	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String phoneNo = StringUtil.object2String(params.get("phoneNo"));
		String startTime = StringUtil.object2String(params.get("startTime"));
		String endTime = StringUtil.object2String(params.get("endTime"));
		IPage<RechargeRecordEntity> page = this.page(new Query<RechargeRecordEntity>().getPage(params),
				new QueryWrapper<RechargeRecordEntity>()
						.ge(StringUtils.isNotBlank(startTime), "pay_time", startTime)
						.le(StringUtils.isNotBlank(endTime), "pay_time", endTime)
						.like(StringUtils.isNotBlank(phoneNo), "phone_no", phoneNo)
						.orderByDesc("create_time"));
		return new PageUtils(page);
	}

	@Override
	@Transactional
	public void saveRechargeRecord(RechargeRecordEntity rechargeRecordEntity) {
		this.save(rechargeRecordEntity);
	}

	@Override
	@Transactional
	public void updateRechargeRecord(RechargeRecordEntity rechargeRecordEntity) {
		this.updateById(rechargeRecordEntity);
	}

	@Override
	public List<RechargeRecordEntity> findByPhoneNo(String phoneNo) {
		QueryWrapper<RechargeRecordEntity> qw = new QueryWrapper<RechargeRecordEntity>();
		qw.eq("phone_no", phoneNo).orderByDesc("create_time");
		return list(qw);
	}

	@Override
	@Transactional
	public R recharge(RechargeReqDto requestDto, HttpServletRequest request) {
		requestDto.setTerminalIp(IpUtil.getIpAddr(request));
		logger.info("用户充值请求参数>>>[{}]", requestDto);
		QueryWrapper<WxAccountEntity> queryWrapper = new QueryWrapper<WxAccountEntity>();
		queryWrapper.eq("phone_no", requestDto.getMobile());
		WxAccountEntity user = wxAccountService.getOne(queryWrapper);

		// 校验账户签名
//		if (!StringUtils.isEmpty(user.getSign()) && !StrUtil.checkAccountSign(user, userSignKey)) {
//			logger.warn("recharge account sign error>>[{}]", user);
//			return R.error("用户签名错误");
//		}

		// 生成充值订单
		RechargeRecordEntity rechargeRecord = new RechargeRecordEntity();
		rechargeRecord.setCash(BigDecimal.valueOf(Double.parseDouble(requestDto.getAmount())));
		// 充值赠送查询
		QueryWrapper<DonateRuleEntity> query = new QueryWrapper<DonateRuleEntity>();
		query.eq("enable", 1);
		Date now = new Date();
		query.le("start_time", now);
		query.ge("end_time", now);
		query.orderByDesc("recharge_money");
		List<DonateRuleEntity> rules = donateRuleServiceImpl.list(query);
		if (rules != null && !rules.isEmpty()) {
			BigDecimal donate = BigDecimal.valueOf(0);
			for(DonateRuleEntity item : rules) {
				if(item.getRechargeMoney().compareTo(rechargeRecord.getCash()) <= 0) {
					donate = item.getDonateMoney();
					break;
				}
			}
			rechargeRecord.setDonateMoney(donate);
		} else {
			rechargeRecord.setDonateMoney(new BigDecimal(0));
		}
		rechargeRecord.setTotalMoney(rechargeRecord.getCash().add(rechargeRecord.getDonateMoney()));
		int integral = (rechargeRecord.getCash().intValue() < 1) ? 1 : rechargeRecord.getCash().intValue();
		rechargeRecord.setCreateTime(new Date());
		rechargeRecord.setPhoneNo(user.getPhoneNo());
		rechargeRecord.setIntegral(integral);
		rechargeRecord.setPayTime(null);
		rechargeRecord.setStatus(0);
		rechargeRecord.setVenderOrderNo(getTradeNo());
		rechargeRecord.setWechatOrderNo(null);
		this.saveOrUpdate(rechargeRecord);
		logger.info("插入充值记录>>[{}]", rechargeRecord);

		// 微信统一下单接口
		QueryWrapper<RechargeRecordEntity> wrapper = new QueryWrapper<RechargeRecordEntity>();
		wrapper.eq("vender_order_no", rechargeRecord.getVenderOrderNo());
		RechargeRecordEntity record = this.getOne(wrapper);

		try {
			WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
			WxConfigEntity config = wxConfigService.list().get(0);
			orderRequest.setAppid(config.getAppId());
			orderRequest.setMchId(config.getMchId());
			orderRequest.setOpenid(requestDto.getOpenid());
			orderRequest.setNotifyUrl(config.getRechargeNotify());
			orderRequest.setSign(getSign(record));
			orderRequest.setSignType("MD5");
			orderRequest.setNonceStr(UUIDUtil.getUUID());
			orderRequest.setTradeType(requestDto.getTradeType());
			orderRequest.setOutTradeNo(record.getVenderOrderNo());
			orderRequest.setBody("会员充值");
			int fee = record.getCash().multiply(new BigDecimal(100)).intValue();
			orderRequest.setTotalFee(fee);
			orderRequest.setSpbillCreateIp(request.getRemoteAddr());
			logger.info("调用微信统一下单接口>>>[{}]", JsonUtil.toJsonString(orderRequest));

			WxPayConfig payConfig = new WxPayConfig();
			payConfig.setAppId(config.getAppId());
			payConfig.setMchId(config.getMchId());
			payConfig.setNotifyUrl(config.getRechargeNotify());
			payConfig.setMchKey(config.getMchKey());
			payConfig.setSignType("MD5");
			WxPayService wxPayService = new WxPayServiceImpl();
			wxPayService.setConfig(payConfig);
			WxPayMpOrderResult result = wxPayService.createOrder(orderRequest);
			logger.info("调用微信统一下单接口响应>>>[{}]", JsonUtil.toJsonString(result));
			return R.ok("返回结果正常").put("result", result);
		} catch (Exception e) {
			logger.info("weixin pre pay error", e);
			return R.error("微信支付失败");
		}
	}

	private String getTradeNo() {
		StringBuilder sb = new StringBuilder("CS");
		sb.append("weixin");
		String str = UUIDUtil.getUUID().replaceAll("-", "");
		sb.append(str.substring(8));
		return sb.toString();
	}

	/**
	 * @return 签名字符串
	 */
	@SuppressWarnings("rawtypes")
	private String getSign(RechargeRecordEntity bill) {
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

	@Override
	@Transactional
	public Object weixinRechargeNotify(HttpServletRequest request, HttpServletResponse response) {
		String xmlResult = null;
        try {
            xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            logger.info("微信回调结果>>[{}]", xmlResult);
        } catch (IOException e) {
            e.printStackTrace();
            return WxPayNotifyResponse.fail(e.getMessage());
        }
		
		WxPayOrderNotifyResult result = null;
		try {
			WxConfigEntity config = wxConfigService.list().get(0);
			WxPayConfig payConfig = new WxPayConfig();
			payConfig.setAppId(config.getAppId());
			payConfig.setMchId(config.getMchId());
			payConfig.setNotifyUrl(config.getRechargeNotify());
			payConfig.setMchKey(config.getMchKey());
			payConfig.setSignType("MD5");
			WxPayService wxPayService = new WxPayServiceImpl();
			wxPayService.setConfig(payConfig);
			result = wxPayService.parseOrderNotifyResult(xmlResult);
			logger.info("微信支付回调结果>>>[{}]", result);
			if (WxPayConstants.ResultCode.SUCCESS.equalsIgnoreCase(result.getResultCode())) {
				logger.info("微信支付成功回调结果>>>[{}]", xmlResult);
				// 更新订单信息
				String transactionId = result.getTransactionId();
				String totalFee = BaseWxPayResult.fenToYuan(result.getTotalFee());
				QueryWrapper<RechargeRecordEntity> wrapper = new QueryWrapper<RechargeRecordEntity>();
				wrapper.eq("vender_order_no", result.getOutTradeNo());
				RechargeRecordEntity record = this.getOne(wrapper);
				if (record == null) {
					return WxPayNotifyResponse.fail("充值订单不存在");
				}
				if(record.getStatus() == 1) {
					return WxPayNotifyResponse.success("订单已处理");
				}
				if(!totalFee.equals(record.getCash().toString())) {
					return WxPayNotifyResponse.fail("订单金额不匹配");
				}
				record.setWechatOrderNo(transactionId);
				record.setStatus(1);
				record.setPayTime(new Date());
				this.saveOrUpdate(record);

				// 修改用户余额、积分信息
				QueryWrapper<WxAccountEntity> queryWrapper = new QueryWrapper<WxAccountEntity>();
				queryWrapper.eq("phone_no", record.getPhoneNo());
				WxAccountEntity user = wxAccountService.getOne(queryWrapper);
				user.setIntegral(user.getIntegral() + record.getIntegral());
				user.setAmount(user.getAmount().add(record.getCash()));
				user.setDonate(user.getDonate().add(record.getDonateMoney()));
				user.setSign(StrUtil.createAccountSign(user.getPhoneNo(), user.getAmount(), userSignKey));
				wxAccountService.saveOrUpdate(user);
				IntegralEntity integral = new IntegralEntity();
				integral.setPhoneNo(record.getPhoneNo());
				integral.setScore(record.getIntegral());
				integral.setMoney(record.getCash().floatValue());
				integral.setType(1);
				integral.setSource(record.getRechargeId());
				integral.setCreateTime(new Date());
				integralService.save(integral);
				
				//增加赠送记录
				DonateRecordEntity donateRecord = new DonateRecordEntity();
				donateRecord.setType(1);
				donateRecord.setPhoneNo(record.getPhoneNo());
				donateRecord.setRechargeMoney(record.getCash());
				donateRecord.setDonateMoney(record.getDonateMoney());
				donateRecord.setDonateTime(new Date());
				donateRecord.setWechatOrderNo(record.getWechatOrderNo());
				donateRecordService.save(donateRecord);
			} else if (WxPayConstants.ResultCode.FAIL.equalsIgnoreCase(result.getReturnCode())) {
				logger.error("微信支付失败回调结果>>>[{}]", xmlResult);
				throw new WxPayException("微信通知支付失败！");
			}
		} catch (WxPayException e) {
			logger.info("微信充值回调失败", e);
			return WxPayNotifyResponse.fail("微信充值回调失败");
		}
		return WxPayNotifyResponse.success("回调通知成功");
	}

}
