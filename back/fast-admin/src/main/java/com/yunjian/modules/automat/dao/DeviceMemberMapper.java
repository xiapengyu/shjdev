package com.yunjian.modules.automat.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunjian.modules.automat.entity.DeviceMemberEntity;

/**
 * <p>
 * 预警设备与会员的关系 Mapper 接口
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-18
 */
@Mapper
public interface DeviceMemberMapper extends BaseMapper<DeviceMemberEntity> {

}
