package com.yunjian.modules.automat.job;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yunjian.modules.automat.service.DeviceWarnService;
import com.yunjian.modules.automat.service.GoodsAisleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CheckDeviceWarnJob {
	
	@Autowired
	private GoodsAisleService goodsAisleService; 
	
	@Autowired
	private DeviceWarnService deviceWarnService; 
	
    @Scheduled(cron = "0 0/1 * * * ?")
    private void configureTasks() {
    	List<String> deviceCodes =  goodsAisleService.queryWarnDeviceCodes();
    	if(deviceCodes.size()>0) {
    		deviceWarnService.saveWarnDevice(deviceCodes);
    	}
    	log.info("保存预警设备信息...");
    }
}
