package com.yunjian.modules.automat.dao;


import java.util.List;

import com.yunjian.modules.automat.vo.GoodsExt;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunjian.modules.automat.entity.GoodsEntity;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-16
 */
@Mapper
public interface GoodsMapper extends BaseMapper<GoodsEntity> {

	List<GoodsEntity> deliveryGoodsList(Integer deliverId);

    List<GoodsExt> goodsList(String deviceCode);

    List<GoodsExt> stockoutGoodsList(String deviceCode);
}
