package com.yunjian.modules.automat.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.R;
import com.yunjian.modules.automat.entity.GoodsEntity;
import com.yunjian.modules.automat.vo.GoodsExt;
import com.yunjian.modules.automat.vo.ReplenishmentVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-16
 */
public interface GoodsService extends IService<GoodsEntity> {
	
	PageUtils queryPage(Map<String, Object> params);
	
	void saveGoods(GoodsEntity donateRule);

	List<GoodsExt> findGoodsByDeviceId(String deviceId);

	List<GoodsEntity> deliveryGoodsList(Integer deliverId);

	void updateGoods(GoodsEntity goodsEntity);

    List<GoodsExt> goodsList(String deviceCode);

	R stockoutGoodsList(String deviceCode);

	boolean replenishment(ReplenishmentVo params);
}
