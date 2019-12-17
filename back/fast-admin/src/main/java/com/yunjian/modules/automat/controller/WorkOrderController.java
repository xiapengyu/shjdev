package com.yunjian.modules.automat.controller;


import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yunjian.common.annotation.SysLog;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.R;
import com.yunjian.common.validator.ValidatorUtils;
import com.yunjian.modules.automat.entity.WorkOrderEntity;
import com.yunjian.modules.automat.service.WorkOrderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 工单管理
 */
@Api(tags = "工单相关接口")
@RestController
@RequestMapping("/automat/workOrder")
public class WorkOrderController {
	@Autowired
	private WorkOrderService workOrderService;


	/**
	 * 所有工单列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("workOrder:list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = workOrderService.queryPage(params);
		return R.ok().put("page", page);
	}

	/**
	 * 工单信息
	 */
	@GetMapping("/info/{id}")
	public R info(@PathVariable("id") Long id){
		QueryWrapper<WorkOrderEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("work_id", id);
		WorkOrderEntity dictData = workOrderService.getOne(queryWrapper);
		return R.ok().put("dictData", dictData);
	}


	/**
	 * 保存配置
	 */
	@ApiOperation(value = "提交工单信息")
	@SysLog("保存工单")
	@PostMapping("/save")
	public R save(@RequestBody WorkOrderEntity dictData){
		ValidatorUtils.validateEntity(dictData);
		workOrderService.saveOrUpdate(dictData);
		return R.ok();
	}

	/**
	 * 修改配置
	 */
	@SysLog("修改工单")
	@PostMapping("/update")
	public R update(@RequestBody WorkOrderEntity dictData){
		ValidatorUtils.validateEntity(dictData);
		workOrderService.updateWorkOrder(dictData);
		return R.ok();
	}


	/**
	 * 删除配置
	 */
	@SysLog("删除工单")
	@PostMapping("/delete")
	public R delete(@RequestBody Long[] ids){
		workOrderService.removeByIds(Arrays.asList(ids));
		return R.ok();
	}
}
