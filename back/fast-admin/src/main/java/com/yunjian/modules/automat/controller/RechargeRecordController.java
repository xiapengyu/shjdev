package com.yunjian.modules.automat.controller;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.R;
import com.yunjian.common.validator.ValidatorUtils;
import com.yunjian.common.validator.group.AddGroup;
import com.yunjian.modules.automat.entity.RechargeRecordEntity;
import com.yunjian.modules.automat.service.RechargeRecordService;
import com.yunjian.modules.automat.util.DateUtil;
import com.yunjian.modules.automat.vo.RechargeReqDto;
import com.yunjian.modules.automat.vo.TendencyPairVo;
import com.yunjian.modules.sys.controller.AbstractController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 充值记录
 *
 * @author xiapengyu
 * @since 2019-06-04
 */
@Api(tags = "充值记录接口")
@RestController
@RequestMapping("/automat/rechargeRecord")
public class RechargeRecordController extends AbstractController {
	
	@Autowired
	RechargeRecordService rechargeRecordService;
	
	/**
	 * 分页查询充值记录
	 * @param params
	 * @return
	 */
	@GetMapping("/list")
	@RequiresPermissions("operate:recharge:list")
	public R rechargeRecordList(@RequestParam Map<String, Object> params) {
		PageUtils page = rechargeRecordService.queryPage(params);
		return R.ok().put("page", page);
	}
	
	@ApiOperation(value = "会员充值支付")
	@RequestMapping(value = "/recharge", method = RequestMethod.POST)
	public R recharge(@RequestBody RechargeReqDto requestDto) {
		logger.info("用户开始充值>>[{}]", requestDto);
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		return rechargeRecordService.recharge(requestDto, request);
	}
	
	@ApiOperation(value = "会员充值缴费回调")
	@RequestMapping(value = "/weixinRechargeNotify", method = RequestMethod.POST)
	public Object rechargeNotify(HttpServletRequest request, HttpServletResponse response) {
		return rechargeRecordService.weixinRechargeNotify(request, response);
	}
	
	/**
	 * 保存充值记录
	 * @param donateRuleEntity
	 * @return
	 */
	@PostMapping("/save")
	@RequiresPermissions("operate:rechargeRecord:save")
	public R saveRechargeRecord(@RequestBody RechargeRecordEntity rechargeRecordEntity) {
		ValidatorUtils.validateEntity(rechargeRecordEntity, AddGroup.class);
		rechargeRecordService.saveRechargeRecord(rechargeRecordEntity);
		return R.ok();
	}
	
	/**
	 * 更新充值记录状态
	 * @param donateRuleEntity
	 * @return
	 */
	@PostMapping("/update")
	@RequiresPermissions("operate:RechargeRecord:update")
	public R updateRechargeRecord(@RequestBody RechargeRecordEntity rechargeRecordEntity) {
		ValidatorUtils.validateEntity(rechargeRecordEntity, AddGroup.class);
		rechargeRecordService.updateRechargeRecord(rechargeRecordEntity);
		return R.ok();
	}
	
	/**
	 * 查询充值详情
	 * @param donateRuleEntity
	 * @return
	 */
	@GetMapping("/info")
	@RequiresPermissions("operate:RechargeRecord:info")
	public R updateRechargeRecord(@PathVariable("recordId") Long recordId) {
		RechargeRecordEntity info = rechargeRecordService.getById(recordId);
		return R.ok().put("record", info);
	}
	
	@ApiOperation(value = "个人充值记录列表")
	@GetMapping("/findByPhoneNo")
	public List<RechargeRecordEntity> findByPhoneNo(String phoneNo){
		return rechargeRecordService.findByPhoneNo(phoneNo);
	}
	
	@ApiOperation(value = "查询充值订单详情列表")
	@GetMapping("/rechargeTendency/{id}")
	public R rechargeTendency(@PathVariable("id") Integer type){
		List<RechargeRecordEntity> records = rechargeRecordService.list();
		List<String> dateArray = new ArrayList<String>();
		List<Double> amountArray = new ArrayList<Double>();
		List<Integer> timesArray = new ArrayList<Integer>();
		int totalTimes = 0;
		double totalAmount = 0;
		
		for(RechargeRecordEntity record : records) {
			if(record.getPayTime() != null) {
				totalTimes++;
				totalAmount = totalAmount + record.getCash().doubleValue();
			}
		}
		totalAmount = this.format(totalAmount);
		if(type == 1) {
			List<TendencyPairVo> days = DateUtil.getLast30DayList();
			for(TendencyPairVo day : days) {
				for(RechargeRecordEntity record : records) {
					if(record.getPayTime() != null && record.getPayTime().compareTo(day.getStartTime()) >= 0 
							&& record.getPayTime().compareTo(day.getEndTime()) < 0) {
						day.setTimes(day.getTimes() + 1);
						day.setAmount(day.getAmount() + record.getCash().doubleValue());
					}
				}
				timesArray.add(day.getTimes());
				amountArray.add(this.format(day.getAmount()));
				dateArray.add(day.getDate());
			}
		}else if(type == 2) {
			List<TendencyPairVo> months = DateUtil.getLast12MonthList();
			for(TendencyPairVo month : months) {
				for(RechargeRecordEntity record : records) {
					if(record.getPayTime() != null && record.getPayTime().compareTo(month.getStartTime()) >= 0 
							&& record.getPayTime().compareTo(month.getEndTime()) < 0) {
						month.setTimes(month.getTimes() + 1);
						month.setAmount(month.getAmount() + record.getCash().doubleValue());
					}
				}
				timesArray.add(month.getTimes());
				amountArray.add(this.format(month.getAmount()));
				dateArray.add(month.getDate());
			}
		}else if(type == 3) {
			List<TendencyPairVo> years = DateUtil.getLastYearList();
			for(TendencyPairVo year : years) {
				for(RechargeRecordEntity record : records) {
					if(record.getPayTime() != null && record.getPayTime().compareTo(year.getStartTime()) >= 0 
							&& record.getPayTime().compareTo(year.getEndTime()) < 0) {
						year.setTimes(year.getTimes() + 1);
						year.setAmount(year.getAmount() + record.getCash().doubleValue());
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

