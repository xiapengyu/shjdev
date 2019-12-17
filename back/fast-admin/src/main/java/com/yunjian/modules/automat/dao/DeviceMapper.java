package com.yunjian.modules.automat.dao;

import com.yunjian.modules.automat.entity.DeviceEntity;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author laizhiwen
 * @since 2019-06-22
 */
@Mapper
public interface DeviceMapper extends BaseMapper<DeviceEntity> {

	DeviceEntity queryByAisleId(Integer aisleId);

}
