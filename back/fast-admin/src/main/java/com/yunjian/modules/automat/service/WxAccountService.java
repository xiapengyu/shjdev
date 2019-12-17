package com.yunjian.modules.automat.service;

import com.yunjian.common.utils.PageUtils;
import com.yunjian.modules.automat.entity.WxAccountEntity;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 微信账号 服务类
 * </p>
 *
 * @author xiapengyu
 * @since 2019-07-08
 */
public interface WxAccountService extends IService<WxAccountEntity> {

	boolean updateAccount(WxAccountEntity account);

	PageUtils queryPage(Map<String, Object> params);

	PageUtils queryMgmtPage(Map<String, Object> params);

	void getSmsMessage(String phoneNo);

}
