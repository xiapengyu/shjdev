package com.yunjian.modules.automat.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunjian.modules.automat.entity.DeviceWarnEntity;

import java.util.List;

/**
 * <p>
 * 预警管理 Mapper 接口
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-18
 */
@Mapper
public interface DeviceWarnMapper extends BaseMapper<DeviceWarnEntity> {

	List<DeviceWarnEntity> findDeviceWarningByPhoneNo(String phoneNo);

}
