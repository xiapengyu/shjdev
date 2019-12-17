package com.yunjian.modules.automat.service;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.modules.automat.entity.DonateRuleEntity;

/**
 * 赠送规则 服务类
 * @author xiapengyu
 * @since 2019-06-03
 */
public interface DonateRuleService extends IService<DonateRuleEntity> {

	PageUtils queryPage(Map<String, Object> params);
	
	/**
	 * 保存用户
	 */
	void saveDonateRule(DonateRuleEntity donateRule);
	
	/**
	 * 修改用户
	 */
	void updateDonateRule(DonateRuleEntity donateRule);
	
	/**
	 * 删除用户
	 */
	void deleteDonateRule(int[] donateRuleIds);

}
