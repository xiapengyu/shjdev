package com.yunjian.modules.automat.job;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yunjian.modules.automat.entity.AdEntity;
import com.yunjian.modules.automat.service.AdService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CheckAdExpireJob {
	
	@Autowired
	private AdService adService; 
	
	@Scheduled(cron = "0 0/1 * * * ?")
    private void configureTasks() {
		QueryWrapper<AdEntity> query = new QueryWrapper<AdEntity>();
		query.eq("status", 1);
		List<AdEntity> adList = adService.list(query);
		Date now = new Date();
    	if(adList.size()>0) {
    		for(AdEntity ad : adList) {
    			if(now.after(ad.getEndTime())) {
    				UpdateWrapper<AdEntity> update = new UpdateWrapper<AdEntity>();
    				update.eq("ad_id", ad.getAdId());
    				ad.setStatus(0);
    				adService.update(ad, update);
    			}
    		}
    	}
    	log.info("定时检测广告生效时间，如果失效则置为禁用...");
    }

}
