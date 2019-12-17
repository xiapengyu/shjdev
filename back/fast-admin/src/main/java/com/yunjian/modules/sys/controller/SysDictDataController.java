package com.yunjian.modules.sys.controller;


import com.yunjian.common.utils.PageUtils;
import com.yunjian.modules.sys.entity.SysDictDataEntity;
import com.yunjian.modules.sys.service.SysDictDataService;
import com.yunjian.common.annotation.SysLog;
import com.yunjian.common.utils.R;
import com.yunjian.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 系统配置信息
 * 
 * @author chenshun
 *
 * @date 2016年12月4日 下午6:55:53
 */
@RestController
@RequestMapping("/sys/dictData")
public class SysDictDataController extends AbstractController {
	@Autowired
	private SysDictDataService sysDictDataService;


	/**
	 * 所有字典列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:dictData:list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = sysDictDataService.queryPage(params);
		return R.ok().put("page", page);
	}

	/**
	 * 字典信息
	 */
	@GetMapping("/info/{id}")
	@RequiresPermissions("sys:dictData:info")
	public R info(@PathVariable("id") Long id){
		SysDictDataEntity dictData = sysDictDataService.getById(id);
		return R.ok().put("dictData", dictData);
	}


	/**
	 * 保存配置
	 */
	@SysLog("保存字典")
	@PostMapping("/save")
	@RequiresPermissions("sys:dictData:save")
	public R save(@RequestBody SysDictDataEntity dictData){
		ValidatorUtils.validateEntity(dictData);
		sysDictDataService.saveOrUpdate(dictData);
		return R.ok();
	}

	/**
	 * 修改配置
	 */
	@SysLog("修改字典")
	@PostMapping("/update")
	@RequiresPermissions("sys:dictData:update")
	public R update(@RequestBody SysDictDataEntity dictData){
		ValidatorUtils.validateEntity(dictData);
		sysDictDataService.saveOrUpdate(dictData);
		return R.ok();
	}


	/**
	 * 删除配置
	 */
	@SysLog("删除字典")
	@PostMapping("/delete")
	@RequiresPermissions("sys:dictData:delete")
	public R delete(@RequestBody Long[] ids){
		sysDictDataService.removeByIds(Arrays.asList(ids));

		return R.ok();
	}
}
