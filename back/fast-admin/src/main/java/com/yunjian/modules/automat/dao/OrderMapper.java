package com.yunjian.modules.automat.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunjian.modules.automat.entity.OrderEntity;
import com.yunjian.modules.automat.vo.DeviceRankVo;
import com.yunjian.modules.automat.vo.OrderExt;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-16
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrderEntity> {

	List<OrderExt> findByPhoneNo(String phoneNo);

	int deleteByOrderId(String orderId);

	OrderEntity findOrder(OrderEntity order);
	
	List<DeviceRankVo> queryHomeDeviceRankPage(@Param("offset") int offset, @Param("limit") int limit);

	int queryHomeDeviceRankCount();
	
	List<OrderEntity> queryOrderByOrderNo(@Param("orderNo") String orderNo);

}