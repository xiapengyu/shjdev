package com.yunjian.modules.automat.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunjian.modules.automat.entity.AdEntity;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-12
 */
@Mapper
public interface AdMapper extends BaseMapper<AdEntity> {

	List<AdEntity> adListForDevice(List<Integer> ids);

}
