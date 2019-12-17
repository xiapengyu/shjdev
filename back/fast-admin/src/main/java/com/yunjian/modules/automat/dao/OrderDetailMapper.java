package com.yunjian.modules.automat.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunjian.modules.automat.entity.OrderDetailEntity;
import com.yunjian.modules.automat.vo.Pair;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-16
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetailEntity> {
	
	public List<Pair> goodsAmountOrder();
	
}
