package com.yunjian.modules.automat.controller;

import java.util.Date;
import java.util.Map;

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
import com.yunjian.common.annotation.SysLog;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.R;
import com.yunjian.modules.automat.entity.DistributorEntity;
import com.yunjian.modules.automat.service.DistributorService;
import com.yunjian.modules.sys.controller.AbstractController;

import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 经销商信息 前端控制器
 * </p>
 *
 * @author laizhiwen
 * @since 2019-08-16
 */
@RestController
@RequestMapping("/automat/distributor")
public class DistributorController extends AbstractController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DistributorService distributorService;
	
	@GetMapping("/list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = distributorService.queryPage(params);
		return R.ok().put("page", page);
	}
	
	@GetMapping("/info/{id}")
	public R distributorInfo(@PathVariable("id") Long id) {
		QueryWrapper<DistributorEntity> queryWrapper = new QueryWrapper<DistributorEntity>();
		queryWrapper.eq("id", id);
		DistributorEntity result = distributorService.getOne(queryWrapper);
		logger.info("查询经销商信息>>>[{}]", result);
		return R.ok().put("distributor", result);
	}
	
	/**
	 * 保存配置
	 */
	@ApiOperation(value = "保存经销商信息")
	@PostMapping("/save")
	public R save(@RequestBody DistributorEntity data){
		logger.info("保存经销商信息>>>[{}]", data);
		data.setCreateTime(new Date());
		distributorService.save(data);
		return R.ok();
	}
	
	/**
	 * 修改配置
	 */
	@SysLog("修改经销商信息")
	@PostMapping("/update")
	public R update(@RequestBody DistributorEntity adData){
		QueryWrapper<DistributorEntity> query = new QueryWrapper<DistributorEntity>();
		query.eq("id", adData.getId());
		distributorService.update(adData, query);
		return R.ok();
	}
	
	/**
	 * 删除经销商信息
	 */
	@SysLog("删除经销商信息")
	@PostMapping("/delete")
	public R delete(@RequestBody DistributorEntity adData){
		QueryWrapper<DistributorEntity> query = new QueryWrapper<DistributorEntity>();
		query.eq("id", adData.getId());
		distributorService.remove(query);
		return R.ok();
	}
}

