package com.yunjian.modules.automat.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yunjian.common.utils.R;
import com.yunjian.modules.automat.entity.RegionEntity;
import com.yunjian.modules.automat.service.RegionService;
import com.yunjian.modules.sys.controller.AbstractController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author laizhiwen
 * @since 2019-06-22
 */
@RestController
@RequestMapping("/automat/region")
public class RegionController extends AbstractController {
	
	@Autowired
	private RegionService regionService;
	
	@GetMapping(value="queryAll")
	public List<RegionEntity> queryAll(){
		return regionService.list();
	}
	
	@GetMapping(value="queryRegion")
	public R queryRegion(Integer regionId){
		QueryWrapper<RegionEntity> qw = new QueryWrapper<RegionEntity>();
		qw.eq("parent_id", regionId);
		return R.ok().put("regionList",regionService.list(qw));
	}
	
	@GetMapping(value="provinceList")
	public R queryRegion(){
		QueryWrapper<RegionEntity> qw = new QueryWrapper<RegionEntity>();
		qw.eq("level_type", 2);
		return R.ok().put("provinceList",regionService.list(qw));
	}

}

