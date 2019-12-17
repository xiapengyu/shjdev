package com.yunjian.modules.automat.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.Query;
import com.yunjian.modules.automat.dao.AdvertisementMapper;
import com.yunjian.modules.automat.entity.AdvertisementEntity;
import com.yunjian.modules.automat.service.AdvertisementService;

@Service("advertisementService")
public class AdvertisementServiceImpl extends ServiceImpl<AdvertisementMapper, AdvertisementEntity> implements AdvertisementService {

	@Autowired
	private AdvertisementMapper advertisementMapper;
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String startTime = (String)params.get("startTime");
        String endTime = (String)params.get("endTime");

        IPage<AdvertisementEntity> page = this.page(
                new Query<AdvertisementEntity>().getPage(params),
                new QueryWrapper<AdvertisementEntity>()
                        .ge("start_time", startTime)
                        .le("end_time", endTime)
        );

        return new PageUtils(page);
    }

	@Override
	public void statusChange(Integer status) {
		advertisementMapper.statusChange(status);
	}
    
}
