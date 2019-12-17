package com.yunjian.modules.automat.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.modules.automat.entity.DeliverRecordEntity;
import com.yunjian.modules.automat.vo.DeliverRecordExt;
import com.yunjian.modules.automat.vo.DeliveryParamVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-16
 */
public interface DeliverRecordService extends IService<DeliverRecordEntity> {
	
	PageUtils queryPage(Map<String, Object> params);
	
	List<DeliverRecordExt> findByPhoneNo(DeliveryParamVo params);
}
