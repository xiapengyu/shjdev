package com.yunjian.modules.automat.service.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.Query;
import com.yunjian.modules.automat.dao.QrCodeMapper;
import com.yunjian.modules.automat.entity.QrCodeEntity;
import com.yunjian.modules.automat.service.QrCodeService;
import com.yunjian.modules.automat.util.StringUtil;

@Service("qrCodeService")
public class QrCodeServiceImpl extends ServiceImpl<QrCodeMapper, QrCodeEntity> implements QrCodeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
    	String startTime = StringUtil.object2String(params.get("startTime"));
		String endTime = StringUtil.object2String(params.get("endTime"));
    	String batchNo = StringUtil.object2String(params.get("batchNo"));
    	
    	IPage<QrCodeEntity> page = this.page(
				new Query<QrCodeEntity>().getPage(params),
				new QueryWrapper<QrCodeEntity>()
				.ge(StringUtils.isNotBlank(startTime),"create_time", startTime)
				.le(StringUtils.isNotBlank(endTime),"create_time", endTime)
                .eq(StringUtils.isNotBlank(batchNo), "batch_no", batchNo)
                .orderByDesc("create_time")
        );
        return new PageUtils(page);
    }
    
}
