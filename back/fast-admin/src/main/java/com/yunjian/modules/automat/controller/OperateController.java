package com.yunjian.modules.automat.controller;


import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.R;
import com.yunjian.modules.automat.entity.DonateRuleEntity;
import com.yunjian.modules.automat.service.DonateRecordService;
import com.yunjian.modules.automat.service.DonateRuleService;
import com.yunjian.modules.sys.controller.AbstractController;

/**
 * 运营管理
 *
 * @author xiapengyu
 * @since 2019-06-03
 */
@RestController
@RequestMapping("/automat/operate")
public class OperateController extends AbstractController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	DonateRecordService donateRecordService;
	@Autowired
	DonateRuleService donateRuleService;
	
	/**
	 * 分页查询赠送记录
	 * @param params
	 * @return
	 */
	@GetMapping("/donateRecordList")
	@RequiresPermissions("operate:donaterecode:list")
	public R DonateRecordList(@RequestParam Map<String, Object> params) {
		logger.info("分页查询赠送记录>>>[{}]", params);
		PageUtils page = donateRecordService.queryPage(params);
		return R.ok().put("page", page);
	}
	
	/**
	 * 分页查询赠送规则
	 * @param params
	 * @return
	 */
	@GetMapping("/donateRuleList")
	@RequiresPermissions("operate:donateRule:list")
	public R DonateRuleList(@RequestParam Map<String, Object> params) {
		logger.info("分页查询赠送规则>>>[{}]", params);
		PageUtils page = donateRuleService.queryPage(params);
		return R.ok().put("page", page);
	}
	
	/**
	 * 查询可用的赠送规则返给app
	 * @param params
	 * @return
	 */
	@GetMapping("/enableDonateRuleList")
	public R DonateRuleList() {
		logger.info("查询可用的赠送规则返给app");
		QueryWrapper<DonateRuleEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("enable", 1);
		queryWrapper.orderByAsc("recharge_money");
		Date now = new Date();
		queryWrapper.le("start_time", now);
		queryWrapper.ge("end_time", now);
		List<DonateRuleEntity> list = donateRuleService.list(queryWrapper);
		logger.info("查询赠送规则信息>>>[{}]", list);
		return R.ok().put("rules", list);
	}
	
	/**
	 * 新增赠送规则
	 * @param donateRuleEntity
	 * @return
	 */
	@PostMapping("/saveDonateRule")
	public R saveDonateRule(@RequestBody DonateRuleEntity donateRuleEntity) {
		logger.info("新增赠送规则>>>[{}]", donateRuleEntity);
		donateRuleEntity.setCreateTime(new Date());
		donateRuleEntity.setType(1);
		donateRuleService.saveDonateRule(donateRuleEntity);
		return R.ok();
	}
	
	/**
	 * 修改赠送规则
	 * @param donateRuleEntity
	 * @return
	 */
	@PostMapping("/updateDonateRule")
	public R updateDonateRule(@RequestBody DonateRuleEntity donateRuleEntity) {
		logger.info("修改赠送规则>>>[{}]", donateRuleEntity);
		donateRuleService.updateDonateRule(donateRuleEntity);
		return R.ok();
	}
	
	/**
	 * 删除赠送规则
	 * @param donateRuleIds
	 * @return
	 */
	@GetMapping("/deleteDonateRule")
	public R delDonateRule(@RequestBody int[] donateRuleIds) {
		donateRuleService.deleteDonateRule(donateRuleIds);
		return R.ok();
	}
	
	/**
	 * 赠送规则信息
	 */
	@GetMapping("/info/{id}")
	public R info(@PathVariable("id") Long id){
		QueryWrapper<DonateRuleEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("donate_id", id);
		DonateRuleEntity dictData = donateRuleService.getOne(queryWrapper);
		logger.info("查询赠送规则信息>>>[{}]", dictData);
		return R.ok().put("donateData", dictData);
	}
}

