package com.yunjian.modules.automat.service.impl;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.Query;
import com.yunjian.modules.automat.dao.DonateRuleMapper;
import com.yunjian.modules.automat.entity.DonateRuleEntity;
import com.yunjian.modules.automat.service.DonateRuleService;
import com.yunjian.modules.automat.util.StringUtil;

/**
 * 赠送规则 服务实现类
 *
 * @author xiapengyu
 * @since 2019-06-03
 */
@Service("donateRuleService")
public class DonateRuleServiceImpl extends ServiceImpl<DonateRuleMapper, DonateRuleEntity> implements DonateRuleService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String startTime = StringUtil.object2String(params.get("startTime"));
		String endTime = StringUtil.object2String(params.get("endTime"));
		IPage<DonateRuleEntity> page = this.page(
				new Query<DonateRuleEntity>().getPage(params),
				new QueryWrapper<DonateRuleEntity>()
					.ge(StringUtils.isNotBlank(startTime),"create_time", startTime)
					.le(StringUtils.isNotBlank(endTime),"create_time", endTime)
					.orderByDesc("create_time")
			);
			return new PageUtils(page);
	}

	@Override
	@Transactional
	public void saveDonateRule(DonateRuleEntity donateRule) {
		this.save(donateRule);
	}

	@Override
	@Transactional
	public void updateDonateRule(DonateRuleEntity donateRule) {
		UpdateWrapper<DonateRuleEntity> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("donate_id", donateRule.getDonateId());
		this.update(donateRule, updateWrapper);
	}
	
	@Override
	@Transactional
	public void deleteDonateRule(int[] donateRuleIds) {
		this.removeByIds(Arrays.asList(donateRuleIds));
	}
	
}
