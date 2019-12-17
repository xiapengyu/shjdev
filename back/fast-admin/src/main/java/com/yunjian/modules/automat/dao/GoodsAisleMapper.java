package com.yunjian.modules.automat.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunjian.modules.automat.entity.GoodsAisleEntity;
import com.yunjian.modules.automat.vo.AisleExt;

/**
 * <p>
 * 货道信息管理表 Mapper 接口
 * </p>
 *
 * @author laizhiwen
 * @since 2019-07-01
 */
@Mapper
public interface GoodsAisleMapper extends BaseMapper<GoodsAisleEntity> {

	List<String> queryWarnDeviceCodes();

	void replenishmentByDeviceCode(
			@Param("deviceCode")String deviceCode, 
			@Param("goodsQuantity")Integer goodsQuantity,
			@Param("aisleId")Integer aisleId);

	int stockoutAisle(String deviceCode);

	List<AisleExt> queryTaskAisle(String phoneNo);

}
