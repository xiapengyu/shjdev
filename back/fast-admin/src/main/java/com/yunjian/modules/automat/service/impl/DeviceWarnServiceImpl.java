package com.yunjian.modules.automat.service.impl;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.yunjian.modules.automat.service.RegionService;
import com.yunjian.modules.automat.util.DistanceHepler;
import com.yunjian.modules.automat.vo.DeviceExt;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.Query;
import com.yunjian.modules.automat.dao.DeviceWarnMapper;
import com.yunjian.modules.automat.entity.DeviceEntity;
import com.yunjian.modules.automat.entity.DeviceWarnEntity;
import com.yunjian.modules.automat.service.DeviceService;
import com.yunjian.modules.automat.service.DeviceWarnService;

/**
 * <p>
 * 预警管理 服务实现类
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-18
 */
@Service("deviceWarningService")
public class DeviceWarnServiceImpl extends ServiceImpl<DeviceWarnMapper, DeviceWarnEntity> implements DeviceWarnService {
	
	@Autowired
	private DeviceService deviceService;

	@Autowired
	private DeviceWarnMapper deviceWarnMapper;

	@Autowired
	private RegionService regionService;
	
	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		QueryWrapper<DeviceWarnEntity> qw = new QueryWrapper<>();
		if(StringUtils.isNotEmpty((String) params.get("deviceCode"))) {
			qw.eq("device_code", (String) params.get("deviceCode"));
		}else {
			if(StringUtils.isNotEmpty((String) params.get("province"))) {
				qw.eq("province", (String) params.get("province"));
			}
			if(StringUtils.isNotEmpty((String) params.get("city"))) {
				qw.eq("city", (String) params.get("city"));
			}
			if(StringUtils.isNotEmpty((String) params.get("distribute"))) {
				qw.eq("distribute", (String) params.get("distribute"));
			}
		}
        IPage<DeviceWarnEntity> page = this.page(
                new Query<DeviceWarnEntity>().getPage(params),
                qw
        );
        return new PageUtils(page);
    }
	
	@Override
	public List<DeviceExt> findDeviceWarningByPhoneNo(String phoneNo, Double lng, Double lat) {
		List<DeviceWarnEntity> dlist = deviceWarnMapper.findDeviceWarningByPhoneNo(phoneNo);
		List<DeviceExt> extList = Lists.newArrayList();
		for(DeviceWarnEntity d:dlist){
			DeviceExt deviceExt = new DeviceExt();
			BeanUtils.copyProperties(d,deviceExt);
			String provinceName = regionService.getRegionName(d.getProvince());
			String cityName = regionService.getRegionName(d.getCity());
			String distributeName = regionService.getRegionName(d.getDistribute());
			deviceExt.setProvinceName(provinceName);
			deviceExt.setCityName(cityName);
			deviceExt.setDistributeName(distributeName);
			deviceExt.setDistance(DistanceHepler.distance(d.getLng().doubleValue(),d.getLat().doubleValue(),lng,lat));
			extList.add(deviceExt);
		}
		return extList;
	}

	@Override
	public void saveWarnDevice(List<String> deviceCodes) {
		QueryWrapper<DeviceEntity> qw = new QueryWrapper<DeviceEntity>();
		qw.in("device_code", deviceCodes);
		qw.eq("status", 0);
		List<DeviceEntity> list = deviceService.list(qw);
		for(DeviceEntity d : list) {
			QueryWrapper<DeviceWarnEntity> qwe = new QueryWrapper<DeviceWarnEntity>();
			qwe.eq("device_code", d.getDeviceCode());
			int num = count(qwe);
			if(num == 0) {
				DeviceWarnEntity entity = new DeviceWarnEntity();
				BeanUtils.copyProperties(d, entity);
				save(entity);
			}
		}
	}

}
