package com.yunjian.modules.automat.service;

import com.yunjian.modules.automat.entity.RegionEntity;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author laizhiwen
 * @since 2019-06-22
 */
public interface RegionService extends IService<RegionEntity> {

	List<RegionEntity> getProvinceList();

	List<RegionEntity> getRegionList(Integer province);

	String getRegionName(Integer id);

}
