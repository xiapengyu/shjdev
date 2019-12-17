

package com.yunjian.modules.app.service;


import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.modules.app.entity.UserEntity;
import com.yunjian.modules.app.form.LoginForm;

/**
 * 用户
 *
 *
 */
public interface UserService extends IService<UserEntity> {

	UserEntity queryByMobile(String mobile);
	
	/**
	 * 用户登录
	 * @param form    登录表单
	 * @return        返回用户ID
	 */
	long login(LoginForm form);
	
	PageUtils queryPage(Map<String, Object> params);
	
	PageUtils queryDeliverPage(Map<String, Object> params);
}
