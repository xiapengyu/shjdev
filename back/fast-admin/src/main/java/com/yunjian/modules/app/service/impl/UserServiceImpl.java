
package com.yunjian.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunjian.modules.app.entity.UserEntity;
import com.yunjian.common.exception.RRException;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.Query;
import com.yunjian.common.validator.Assert;
import com.yunjian.modules.app.dao.UserDao;
import com.yunjian.modules.app.form.LoginForm;
import com.yunjian.modules.app.service.UserService;
import com.yunjian.modules.automat.entity.AdEntity;

import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

	@Override
	public UserEntity queryByMobile(String mobile) {
		return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("mobile", mobile));
	}

	@Override
	public long login(LoginForm form) {
		UserEntity user = queryByMobile(form.getMobile());
		Assert.isNull(user, "手机号或密码错误");

		// 密码错误
		if (!user.getPassword().equals(DigestUtils.sha256Hex(form.getPassword()))) {
			throw new RRException("手机号或密码错误");
		}

		return user.getUserId();
	}

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String account = (String) params.get("account");
		String username = (String) params.get("username");
		String mobile = (String) params.get("mobile");
		IPage<UserEntity> page = this.page(new Query<UserEntity>().getPage(params),
				new QueryWrapper<UserEntity>().like(StringUtils.isNotBlank(account), "account", account)
						.like(StringUtils.isNotBlank(username), "username", username)
						.like(StringUtils.isNotBlank(mobile), "mobile", mobile));
		return new PageUtils(page);
	}

	@Override
	public PageUtils queryDeliverPage(Map<String, Object> params) {
		String account = (String) params.get("account");
		String username = (String) params.get("username");
		String mobile = (String) params.get("mobile");

		QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("is_deliver", 1);

		IPage<UserEntity> page = this.page(new Query<UserEntity>().getPage(params),
				queryWrapper.like(StringUtils.isNotBlank(account), "account", account)
						.like(StringUtils.isNotBlank(username), "username", username)
						.like(StringUtils.isNotBlank(mobile), "mobile", mobile));
		return new PageUtils(page);
	}
}
