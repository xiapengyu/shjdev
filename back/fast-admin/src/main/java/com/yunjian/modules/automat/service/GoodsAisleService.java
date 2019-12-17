package com.yunjian.modules.automat.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.R;
import com.yunjian.modules.automat.entity.GoodsAisleEntity;
import com.yunjian.modules.automat.entity.OrderEntity;
import com.yunjian.modules.automat.vo.AisleExt;

/**
 * <p>
 * 货道信息管理表 服务类
 * </p>
 *
 * @author laizhiwen
 * @since 2019-07-01
 */
public interface GoodsAisleService extends IService<GoodsAisleEntity> {
	PageUtils queryPage(Map<String, Object> params) throws IllegalAccessException, InvocationTargetException;

	List<String> queryWarnDeviceCodes();

	Integer sweepCode(OrderEntity order, String deviceCode, String sweepDeviceCode);

	R saveAisle(GoodsAisleEntity aisle);

	void checkStockout(String deviceCode);

	List<AisleExt> queryTaskAisle(String phoneNo);

	int stockoutAisle(String deviceCode);

}
