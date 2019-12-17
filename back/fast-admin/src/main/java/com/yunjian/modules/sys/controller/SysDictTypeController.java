package com.yunjian.modules.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.modules.sys.entity.SysDictDataEntity;
import com.yunjian.modules.sys.entity.SysDictTypeEntity;
import com.yunjian.modules.sys.service.SysDictDataService;
import com.yunjian.modules.sys.service.SysDictTypeService;
import com.yunjian.common.annotation.SysLog;
import com.yunjian.common.utils.R;
import com.yunjian.common.validator.ValidatorUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * 系统配置信息
 * 
 * @author chenshun
 *
 * @date 2016年12月4日 下午6:55:53
 */
@RestController
@RequestMapping("/sys/dictType")
public class SysDictTypeController extends AbstractController {
	@Autowired
	private SysDictTypeService sysDictTypeService;
	@Autowired
	private SysDictDataService sysDictDataService;


	/**
	 * 所有分类列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:dictType:list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = sysDictTypeService.queryPage(params);
		return R.ok().put("page", page);
	}

	/**
	 * 分类信息
	 */
	@GetMapping("/info/{id}")
	@RequiresPermissions("sys:dictType:info")
	public R info(@PathVariable("id") Long id){
		SysDictTypeEntity dictType = sysDictTypeService.getById(id);
		return R.ok().put("dictType", dictType);
	}


	/**
	 * 保存配置
	 */
	@SysLog("保存字典类型")
	@PostMapping("/save")
	@RequiresPermissions("sys:dictType:save")
	public R save(@RequestBody SysDictTypeEntity dictType){
		ValidatorUtils.validateEntity(dictType);
		sysDictTypeService.saveOrUpdate(dictType);
		return R.ok();
	}

	/**
	 * 修改配置
	 */
	@SysLog("修改字典类型")
	@PostMapping("/update")
	@RequiresPermissions("sys:dictType:update")
	public R update(@RequestBody SysDictTypeEntity dictType){
		ValidatorUtils.validateEntity(dictType);
		sysDictTypeService.saveOrUpdate(dictType);
		return R.ok();
	}


	/**
	 * 删除配置
	 */
	@SysLog("删除字典类型")
	@PostMapping("/delete/{dictId}")
	@RequiresPermissions("sys:dictType:delete")
	public R delete(@PathVariable("dictId") long dictId){
		SysDictTypeEntity dictType  = sysDictTypeService.getById(dictId);
		String type = dictType.getDictType();

		int dataCount = sysDictDataService.count(new QueryWrapper<SysDictDataEntity>().like(StringUtils.isNotBlank(type), "dict_type", type));
		if(dataCount > 0){
			return R.error("请先删除子数据");
		}
		sysDictTypeService.removeById(dictId);
		return R.ok();
	}
}
