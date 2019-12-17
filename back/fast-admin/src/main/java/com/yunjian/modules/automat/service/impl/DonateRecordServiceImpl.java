package com.yunjian.modules.automat.service.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.Query;
import com.yunjian.modules.automat.dao.DonateRecordMapper;
import com.yunjian.modules.automat.entity.DonateRecordEntity;
import com.yunjian.modules.automat.service.DonateRecordService;
import com.yunjian.modules.automat.util.StringUtil;

/**
 * <p>
 * 赠送记录 服务实现类
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-03
 */
@Service("donateRecordService")
public class DonateRecordServiceImpl extends ServiceImpl<DonateRecordMapper, DonateRecordEntity> implements DonateRecordService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String startTime = StringUtil.object2String(params.get("startTime"));
		String endTime = StringUtil.object2String(params.get("endTime"));
		String phoneNo = StringUtil.object2String(params.get("phoneNo"));
		String wechatOrderNo = StringUtil.object2String(params.get("wechatOrderNo"));
		IPage<DonateRecordEntity> page = this.page(
				new Query<DonateRecordEntity>().getPage(params),
				new QueryWrapper<DonateRecordEntity>()
					.ge(StringUtils.isNotBlank(startTime),"donate_time", startTime)
					.le(StringUtils.isNotBlank(endTime),"donate_time", endTime)
					.like(StringUtils.isNotBlank(phoneNo), "phone_no", phoneNo)
					.like(StringUtils.isNotBlank(wechatOrderNo), "wechat_order_no", wechatOrderNo)
					.orderByDesc("donate_time")
			);
			return new PageUtils(page);
	}
	

}
