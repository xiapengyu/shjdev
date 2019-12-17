package com.yunjian.modules.automat.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.Query;
import com.yunjian.modules.automat.dao.AdMapper;
import com.yunjian.modules.automat.entity.AdEntity;
import com.yunjian.modules.automat.service.AdService;

@Service("adService")
public class AdServiceImpl extends ServiceImpl<AdMapper, AdEntity> implements AdService {
	
	@Resource
	private AdMapper adMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String startTime = (String)params.get("startTime");
        String endTime = (String)params.get("endTime");
        IPage<AdEntity> page = this.page(
                new Query<AdEntity>().getPage(params),
                new QueryWrapper<AdEntity>()
                .ge(StringUtils.isNotBlank(startTime),"create_time", startTime)
				.le(StringUtils.isNotBlank(endTime),"create_time", endTime)
				.orderByDesc("create_time")
        );
        return new PageUtils(page);
    }

	@Override
	public void statusChange(AdEntity ad) {
		UpdateWrapper<AdEntity> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("status", ad.getStatus());
		this.update(ad, updateWrapper);
	}

	@Override
	public List<AdEntity> adListForDevice(List<Integer> ids) {
		return adMapper.adListForDevice(ids);
	}
    
}
