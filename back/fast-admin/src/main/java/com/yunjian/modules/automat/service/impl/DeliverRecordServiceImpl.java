package com.yunjian.modules.automat.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.Query;
import com.yunjian.modules.automat.dao.DeliverRecordMapper;
import com.yunjian.modules.automat.dao.WxAccountMapper;
import com.yunjian.modules.automat.entity.DeliverRecordEntity;
import com.yunjian.modules.automat.entity.DeviceEntity;
import com.yunjian.modules.automat.service.DeliverRecordService;
import com.yunjian.modules.automat.service.RegionService;
import com.yunjian.modules.automat.service.WxAccountService;
import com.yunjian.modules.automat.util.DistanceHepler;
import com.yunjian.modules.automat.util.StringUtil;
import com.yunjian.modules.automat.vo.DeliverRecordExt;
import com.yunjian.modules.automat.vo.DeliveryParamVo;
import com.yunjian.modules.automat.vo.DeviceExt;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-16
 */
@Service("deliverRecordService")
public class DeliverRecordServiceImpl extends ServiceImpl<DeliverRecordMapper, DeliverRecordEntity> implements DeliverRecordService {

	@Resource
	private DeliverRecordMapper deliverRecordMapper;
	
	@Resource
	private WxAccountMapper wxAccountMapper;
	
	@Autowired
	private RegionService regionService;
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String deviceCode = StringUtil.object2String(params.get("deviceCode"));
		String phoneNo = StringUtil.object2String(params.get("phoneNo"));
		IPage<DeliverRecordEntity> page = this.page(
				new Query<DeliverRecordEntity>().getPage(params),
				new QueryWrapper<DeliverRecordEntity>()
					.ge(StringUtils.isNotBlank(deviceCode),"device_code", deviceCode)
					.like(StringUtils.isNotBlank(phoneNo), "phone_no", phoneNo)
					.last("order by create_time desc")
			);
		
		PageUtils pu = new PageUtils(page);
		
		List<DeliverRecordExt> recordExtList = Lists.newArrayList();
		List<DeliverRecordEntity> list = (List<DeliverRecordEntity>) pu.getList();
		for(DeliverRecordEntity record : list) {
			DeliverRecordExt ext = new DeliverRecordExt();
			BeanUtils.copyProperties(record, ext);
			String nickName = wxAccountMapper.getNameByPhone(record.getPhoneNo());
			ext.setDeliverName(nickName);
			recordExtList.add(ext);
		}
		
		pu.setList(recordExtList);
		return pu;
	}

	@Override
	public List<DeliverRecordExt> findByPhoneNo(DeliveryParamVo params) {
		QueryWrapper<DeliverRecordEntity> queryWrapper = new QueryWrapper<DeliverRecordEntity>();
		queryWrapper.eq("phone_no", params.getPhoneNo());
		List<DeliverRecordExt> list = deliverRecordMapper.findByPhoneNo(params.getPhoneNo(),params.getIndex());
		
		for(DeliverRecordExt d : list) {
			if(d != null) {
				String provinceName = regionService.getRegionName(d.getDevice().getProvince());
				String cityName = regionService.getRegionName(d.getDevice().getCity());
				String distributeName = regionService.getRegionName(d.getDevice().getDistribute());
				d.getDevice().setProvinceName(provinceName);
				d.getDevice().setCityName(cityName);
				d.getDevice().setDistributeName(distributeName);
				d.getDevice().setDistance(DistanceHepler.distance(params.getLng(),params.getLat(), 
						d.getDevice().getLng().doubleValue(), d.getDevice().getLat().doubleValue()));
			}else {
				DeviceExt device = new DeviceExt();
				device.setDeviceCode("该设备已经停用或更改了设备编号");
				d.setDevice(device);
			}
		}
		
		return list;
	}

}
