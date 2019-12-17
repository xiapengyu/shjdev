package com.yunjian.modules.automat.controller;


import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.R;
import com.yunjian.modules.automat.entity.DeviceWarnEntity;
import com.yunjian.modules.automat.entity.RegionEntity;
import com.yunjian.modules.automat.service.DeviceWarnService;
import com.yunjian.modules.automat.service.RegionService;
import com.yunjian.modules.automat.vo.DeviceExt;
import com.yunjian.modules.sys.controller.AbstractController;

import io.swagger.annotations.Api;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 */
@Api(tags = "预警设备相关接口")
@RestController
@RequestMapping("/automat/deviceWarn")
public class DeviceWarnController extends AbstractController {
	@Autowired
	private DeviceWarnService deviceWarnService;
	
	@Autowired
	private RegionService regionService;

	/**
	 * 所有设备列表
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@GetMapping("/warnList")
	@RequiresPermissions("deviceWarn:list")
	public R list(@RequestParam Map<String, Object> params) throws IllegalAccessException, InvocationTargetException{
		List<RegionEntity> provinceList = regionService.getProvinceList();
		R r = R.ok().put("provinceList", provinceList);
		PageUtils page = deviceWarnService.queryPage(params);
		@SuppressWarnings("unchecked")
		List<DeviceWarnEntity> list = (List<DeviceWarnEntity>) page.getList();
		List<DeviceExt> listExt = Lists.newArrayList();
		for(int i=0;i<list.size();i++) {
			DeviceWarnEntity d = list.get(i);
			if(d.getStreet()==null) d.setStreet("");
			DeviceExt deviceExt = new DeviceExt();
			BeanUtils.copyProperties(deviceExt,d);
			String provinceName = regionService.getRegionName(d.getProvince());
			String cityName = regionService.getRegionName(d.getCity());
			String distributeName = regionService.getRegionName(d.getDistribute());
			deviceExt.setProvinceName(provinceName);
			deviceExt.setCityName(cityName);
			deviceExt.setDistributeName(distributeName);
			listExt.add(deviceExt);
		}
		page.setList(listExt);
		
		return r.put("page", page);
	}

}

