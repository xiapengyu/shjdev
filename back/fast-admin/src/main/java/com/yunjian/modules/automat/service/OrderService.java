package com.yunjian.modules.automat.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.R;
import com.yunjian.modules.automat.entity.FeedbackEntity;
import com.yunjian.modules.automat.entity.OrderEntity;
import com.yunjian.modules.automat.entity.WxAccountEntity;
import com.yunjian.modules.automat.vo.OrderExt;
import com.yunjian.modules.automat.vo.UploadCartOutVo;
import com.yunjian.modules.automat.vo.UploadGoodsOutVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-16
 */
public interface OrderService extends IService<OrderEntity> {
	
	PageUtils queryPage(Map<String, Object> params);
	
	void saveGoods(OrderEntity donateRule);

	List<OrderExt> findByPhoneNo(String phoneNo);

	boolean deleteByOrderId(String orderId);

	OrderEntity findOrder(OrderEntity order);
	
	int dayOrderCount();
	
	int weekOrderCount();

	PageUtils queryHomeDeviceRankPage(Map<String, Object> params);
	
	R payOrder(OrderExt order, HttpServletRequest request);

	R weixinPayOrderNotify(HttpServletRequest request, HttpServletResponse response);

	R refund(FeedbackEntity entity, String phone, String orderCode);

	R getCash(WxAccountEntity account);

	void uploadGoodsOut(UploadGoodsOutVo param);
	void uploadCartOut(UploadCartOutVo param);

//	R createOrder(OrderExt order);
	
}
