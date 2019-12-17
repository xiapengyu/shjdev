package com.yunjian.modules.automat.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.Query;
import com.yunjian.common.utils.R;
import com.yunjian.modules.automat.dao.DeviceMemberMapper;
import com.yunjian.modules.automat.dao.GoodsAisleMapper;
import com.yunjian.modules.automat.dao.GoodsMapper;
import com.yunjian.modules.automat.entity.DeliverDetailEntity;
import com.yunjian.modules.automat.entity.DeliverGoodsEntity;
import com.yunjian.modules.automat.entity.DeliverRecordEntity;
import com.yunjian.modules.automat.entity.DeviceEntity;
import com.yunjian.modules.automat.entity.DeviceMemberEntity;
import com.yunjian.modules.automat.entity.DeviceWarnEntity;
import com.yunjian.modules.automat.entity.GoodsAisleEntity;
import com.yunjian.modules.automat.entity.GoodsEntity;
import com.yunjian.modules.automat.entity.WxAccountEntity;
import com.yunjian.modules.automat.service.DeliverDetailService;
import com.yunjian.modules.automat.service.DeliverGoodsService;
import com.yunjian.modules.automat.service.DeliverRecordService;
import com.yunjian.modules.automat.service.DeviceService;
import com.yunjian.modules.automat.service.DeviceWarnService;
import com.yunjian.modules.automat.service.GoodsAisleService;
import com.yunjian.modules.automat.service.GoodsService;
import com.yunjian.modules.automat.service.RegionService;
import com.yunjian.modules.automat.service.WxAccountService;
import com.yunjian.modules.automat.util.StringUtil;
import com.yunjian.modules.automat.vo.DeviceExt;
import com.yunjian.modules.automat.vo.GoodsExt;
import com.yunjian.modules.automat.vo.ReplenishmentVo;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-16
 */
@Service("goodsService")
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, GoodsEntity> implements GoodsService {

	@Resource
	private GoodsMapper goodsMapper;
	
	@Resource
	private DeviceMemberMapper deviceMemberMapper;
	
	@Autowired
	private GoodsAisleService goodsAisleService;

	@Autowired
	private DeviceService deviceService;

	@Autowired
	private DeviceWarnService deviceWarnService;

	@Autowired
	private WxAccountService wxAccountService;
	
	@Autowired
	private RegionService regionService;
	
	@Autowired
	private DeliverRecordService deliverRecordService;
	
	@Autowired
	private DeliverGoodsService deliverGoodsService;
	
	@Autowired
	private DeliverDetailService deliverDetailService;
	
	@Value("${locker.primary.total-number}")
	private Integer primaryTotalNumber;
	
	@Value("${locker.second.total-number}")
	private Integer secondTotalNumber;
	
	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String goodsName = StringUtil.object2String(params.get("goodsName"));
		IPage<GoodsEntity> page = this.page(
				new Query<GoodsEntity>().getPage(params),
				new QueryWrapper<GoodsEntity>()
					.like(StringUtils.isNotBlank(goodsName),"goods_name", goodsName)
					.orderByDesc("create_time")
			);
			return new PageUtils(page);
	}

	@Override
	public void saveGoods(GoodsEntity goodsEntity) {
		this.save(goodsEntity);
	}

	@Override
	public List<GoodsExt> findGoodsByDeviceId(String deviceCode) {
		List<GoodsExt> goodsList = goodsMapper.stockoutGoodsList(deviceCode);
		for(int i=0; i<goodsList.size(); i++){
			goodsList.get(i).setNeedQuantity(6 - goodsList.get(i).getGoodsQuantity());
		}
		return goodsList;
	}

	@Override
	public List<GoodsEntity> deliveryGoodsList(Integer deliverId) {
		return goodsMapper.deliveryGoodsList(deliverId);
	}

	@Override
	public void updateGoods(GoodsEntity goodsEntity) {
		UpdateWrapper<GoodsEntity> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("goods_id", goodsEntity.getGoodsId());
		this.update(goodsEntity, updateWrapper);
	}

	@Override
	public List<GoodsExt> goodsList(String deviceCode) {
		return goodsMapper.goodsList(deviceCode);
	}

	@Override
	public R stockoutGoodsList(String deviceCode) {
		List<GoodsExt> goodsList = goodsMapper.stockoutGoodsList(deviceCode);
		for(int i=0; i<goodsList.size(); i++){
			goodsList.get(i).setNeedQuantity(6 - goodsList.get(i).getGoodsQuantity());
		}
		R r = R.ok().put("goodsList",goodsList);
		QueryWrapper<DeviceEntity> qw = new QueryWrapper<>();
		qw.eq("device_code",deviceCode);
		DeviceEntity device = deviceService.getOne(qw);
		
		DeviceExt deviceExt = new DeviceExt();
		BeanUtils.copyProperties(device, deviceExt);
		deviceExt.setProvinceName(regionService.getRegionName(deviceExt.getProvince()));
		deviceExt.setCityName(regionService.getRegionName(deviceExt.getCity()));
		deviceExt.setDistributeName(regionService.getRegionName(deviceExt.getDistribute()));
		r.put("device",deviceExt);

		QueryWrapper<WxAccountEntity> qw2 = new QueryWrapper<>();
		qw2.eq("type",1);
		List<WxAccountEntity> accountList = wxAccountService.list(qw2);
		r.put("accountList",accountList);
		return r;
	}

	@Override
	public boolean replenishment(ReplenishmentVo params) {
		List<GoodsAisleEntity> aisleList = null;
		//查出缺货的货道
		QueryWrapper<GoodsAisleEntity> aisleQw1 = new QueryWrapper<>();
		aisleQw1.eq("device_code", params.getDeviceCode());
		aisleQw1.le("goods_quantity", 2);  //  goods_quantity<=2
		
		int upNumber = 0;
		int downNumber = 0;
		int needQuantity = 0;
		try {
			int aisleGoodsTotalNumber = 0;    //货道满货量
			//补货
			List<GoodsAisleEntity> disposedList = Lists.newArrayList();
			if(params.getDetail()!=null && params.getDetail().size() > 0) {
				List<Integer> goodsIds = params.getDetail().stream().map(g -> g.getGoodsId()).collect(Collectors.toList());
				aisleQw1.in("goods_id", goodsIds);
				aisleList = goodsAisleService.list(aisleQw1);
				for(ReplenishmentVo.Detail detail : params.getDetail()) {
					int currentQuantity = detail.getGoodsQuantity();
					for(GoodsAisleEntity aisle : aisleList) {
						if(aisle.getLockerNo() == 0) {
							aisleGoodsTotalNumber = primaryTotalNumber;
						}else {
							aisleGoodsTotalNumber = secondTotalNumber;
						}
						if(detail.getGoodsId() == aisle.getGoodsId()) {
							//needQuantity = aisleGoodsTotalNumber - aisle.getGoodsQuantity();
							upNumber += currentQuantity;
							aisle.setGoodsQuantity(aisle.getGoodsQuantity() + currentQuantity);
							UpdateWrapper<GoodsAisleEntity> aisleUw = new UpdateWrapper<GoodsAisleEntity>();
							aisleUw.eq("aisle_id", aisle.getAisleId());
							goodsAisleService.update(aisle, aisleUw);
							disposedList.add(aisle);
						}
					}
				}
			}else {//一键补货
				aisleList = goodsAisleService.list(aisleQw1);
				for(GoodsAisleEntity aisle : aisleList) {
					if(aisle.getLockerNo() == 0) {
						aisleGoodsTotalNumber = primaryTotalNumber;
					}else {
						aisleGoodsTotalNumber = secondTotalNumber;
					}
					needQuantity = aisleGoodsTotalNumber - aisle.getGoodsQuantity();
					upNumber += needQuantity;
					aisle.setGoodsQuantity(aisleGoodsTotalNumber);
					UpdateWrapper<GoodsAisleEntity> aisleUw = new UpdateWrapper<GoodsAisleEntity>();
					aisleUw.eq("aisle_id", aisle.getAisleId());
					goodsAisleService.update(aisle, aisleUw);
					disposedList.add(aisle);
				}
				
			}
			
			//保存补货记录
			DeliverRecordEntity deliverRecord = new DeliverRecordEntity();
			deliverRecord.setPhoneNo(params.getPhoneNo());
			deliverRecord.setDeviceCode(params.getDeviceCode());
			deliverRecord.setDownAmount(downNumber);
			deliverRecord.setUpAmount(upNumber);
			deliverRecord.setTotalAmount( upNumber + downNumber);
			deliverRecord.setCreateTime(new Date());
			deliverRecordService.save(deliverRecord);
			
			//保存补货记录-商品关系
			for(GoodsAisleEntity aisle : disposedList) {
				//保存补货详细信息
				QueryWrapper<GoodsEntity> goodsQw = new QueryWrapper<>();
				goodsQw.eq("goods_id", aisle.getGoodsId());
				GoodsEntity goods = goodsMapper.selectOne(goodsQw);
				DeliverDetailEntity detail = new DeliverDetailEntity();
				detail.setGoodsName(goods.getGoodsName());
				detail.setUnit(goods.getUnit());
				detail.setAmount(aisle.getGoodsQuantity());
				detail.setGoodsPrice(goods.getGoodsPrice().floatValue());
				detail.setTotalPrice(goods.getGoodsPrice().floatValue() * (float)aisle.getGoodsQuantity());
				detail.setDeviceCode(params.getDeviceCode());
				detail.setCreateTime(new Date());
				detail.setRecordId(deliverRecord.getId());
				deliverDetailService.save(detail);

				//保存补货记录-商品关系信息
				QueryWrapper<DeliverGoodsEntity> dgWrapper = new QueryWrapper<>();
				dgWrapper.eq("deliver_id", deliverRecord.getId());
				dgWrapper.eq("goods_id", aisle.getGoodsId());
				int existCount = deliverGoodsService.count(dgWrapper);
				if(existCount == 0) {
					DeliverGoodsEntity deliverGoods = new DeliverGoodsEntity();
					deliverGoods.setDeliverId(deliverRecord.getId());
					deliverGoods.setGoodsId(aisle.getGoodsId());
					deliverGoodsService.save(deliverGoods);
				}
			}
			
			//判断该设备是否仍缺货,不缺货则删除设备-配送员关联关系
			int count = goodsAisleService.stockoutAisle(params.getDeviceCode());
			if(count == 0) {
				QueryWrapper<DeviceMemberEntity> qw = new QueryWrapper<>();
				qw.eq("device_code", params.getDeviceCode());
				qw.eq("phone_no", params.getPhoneNo());
				deviceMemberMapper.delete(qw);

				//删除预警的设备
				QueryWrapper<DeviceWarnEntity> deviceWarnQw = new QueryWrapper<>();
				deviceWarnQw.eq("device_code", params.getDeviceCode());
				deviceWarnService.remove(deviceWarnQw);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return false;
		}
		
		return true;
	}

}
