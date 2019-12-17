package com.yunjian.modules.automat.service.impl;

import com.yunjian.modules.automat.entity.RegionEntity;
import com.yunjian.modules.automat.dao.RegionMapper;
import com.yunjian.modules.automat.service.RegionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author laizhiwen
 * @since 2019-06-22
 */
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, RegionEntity> implements RegionService {
	
	@Override
	public List<RegionEntity> getProvinceList(){
		QueryWrapper<RegionEntity> qw = new QueryWrapper<RegionEntity>();
		qw.eq("level_type", 2);
		List<RegionEntity> provinceList = list(qw);
		return provinceList;
	}
	
	@Override
	public List<RegionEntity> getRegionList(Integer parentId){
		QueryWrapper<RegionEntity> qw = new QueryWrapper<RegionEntity>();
		qw.eq("parent_id", parentId);
		List<RegionEntity> list = list(qw);
		return list;
	}

	@Override
	public String getRegionName(Integer id) {
		RegionEntity r = getById(id);
		return r!=null?r.getName():null;
	}
}
