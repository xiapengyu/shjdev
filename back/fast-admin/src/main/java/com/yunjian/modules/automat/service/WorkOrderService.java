package com.yunjian.modules.automat.service;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.modules.automat.entity.WorkOrderEntity;

public interface WorkOrderService extends IService<WorkOrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

	void updateWorkOrder(WorkOrderEntity dictData);

}
