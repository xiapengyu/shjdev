package com.yunjian.modules.automat.dao;

import com.yunjian.modules.automat.entity.DeliverGoodsEntity;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 配送记录与商品关系表 Mapper 接口
 * </p>
 *
 * @author laizhiwen
 * @since 2019-08-02
 */
@Mapper
public interface DeliverGoodsMapper extends BaseMapper<DeliverGoodsEntity> {

}
