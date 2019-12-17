

package com.yunjian.modules.automat.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunjian.modules.automat.entity.AdvertisementEntity;

/**
 * 广告
 */
@Mapper
public interface AdvertisementMapper extends BaseMapper<AdvertisementEntity> {

	void statusChange(Integer status);
}
