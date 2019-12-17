package com.yunjian.modules.automat.service.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.Query;
import com.yunjian.modules.automat.dao.WorkOrderMapper;
import com.yunjian.modules.automat.entity.DonateRecordEntity;
import com.yunjian.modules.automat.entity.DonateRuleEntity;
import com.yunjian.modules.automat.entity.WorkOrderEntity;
import com.yunjian.modules.automat.service.WorkOrderService;
import com.yunjian.modules.automat.util.StringUtil;

@Service("workOrderService")
public class WorkOrderServiceImpl extends ServiceImpl<WorkOrderMapper, WorkOrderEntity> implements WorkOrderService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String startTime = StringUtil.object2String(params.get("startTime"));
        String endTime = StringUtil.object2String(params.get("endTime"));
        Long feedbackType = (Long)params.get("feedbackType");
        IPage<WorkOrderEntity> page = this.page(
        		new Query<WorkOrderEntity>().getPage(params),
				new QueryWrapper<WorkOrderEntity>()
                        .ge(StringUtils.isNotBlank(startTime),"start_time", startTime)
                        .le(StringUtils.isNotBlank(endTime),"end_time", endTime)
                        .eq(feedbackType != null,"feedback_type", feedbackType)
        );
        return new PageUtils(page);
    }

	@Override
	@Transactional
	public void updateWorkOrder(WorkOrderEntity dictData) {
		UpdateWrapper<WorkOrderEntity> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("work_id", dictData.getWorkId());
		this.update(dictData, updateWrapper);
	}
    
}
