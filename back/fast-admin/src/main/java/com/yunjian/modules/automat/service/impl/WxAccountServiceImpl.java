package com.yunjian.modules.automat.service.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.Query;
import com.yunjian.modules.automat.dao.WxAccountMapper;
import com.yunjian.modules.automat.entity.WxAccountEntity;
import com.yunjian.modules.automat.service.WxAccountService;

/**
 * <p>
 * 微信账号 服务实现类
 * </p>
 *
 * @author xiapengyu
 * @since 2019-07-08
 */
@Service("wxAccountService")
public class WxAccountServiceImpl extends ServiceImpl<WxAccountMapper, WxAccountEntity> implements WxAccountService {
	
	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String phoneNo = (String) params.get("phoneNo");
		String nickName = (String) params.get("nickName");
		String type = (String) params.get("type");
		IPage<WxAccountEntity> page = this.page(new Query<WxAccountEntity>().getPage(params),
				new QueryWrapper<WxAccountEntity>()
				.eq(StringUtils.isNotBlank(type), "type", Integer.parseInt(type))
				.like(StringUtils.isNotBlank(phoneNo), "phone_no", phoneNo)
				.like(StringUtils.isNotBlank(nickName), "nick_name", nickName)
				.orderByDesc("create_time"));
		return new PageUtils(page);
	}
	
	@Override
	public PageUtils queryMgmtPage(Map<String, Object> params) {
		String phoneNo = (String) params.get("phoneNo");
		String nickName = (String) params.get("nickName");
		IPage<WxAccountEntity> page = this.page(new Query<WxAccountEntity>().getPage(params),
				new QueryWrapper<WxAccountEntity>()
				.like(StringUtils.isNotBlank(phoneNo), "phone_no", phoneNo)
				.like(StringUtils.isNotBlank(nickName), "nick_name", nickName)
				.orderByDesc("create_time"));
		return new PageUtils(page);
	}

	@Override
	public boolean updateAccount(WxAccountEntity account) {
		account.setAmount(null);
		account.setDonate(null);
		account.setIntegral(null);
		UpdateWrapper<WxAccountEntity> uw = new UpdateWrapper<WxAccountEntity>();
    	uw.eq("wx_open_id",account.getWxOpenId());
    	if(account.getPhoneNo() != null) {
    		uw.set("phone_no", account.getPhoneNo());
    	}
    	if(account.getPassword() !=null ) {
    		uw.set("password", account.getPassword());
    	}
    	if(account.getIsNoPassword() !=null ) {
    		uw.set("is_no_password", account.getIsNoPassword());
    	}
    	if(account.getIsTick() != null) {
    		uw.set("is_tick", account.getIsTick());
    	}
    	if(account.getSessionKey() != null) {
    		uw.set("session_key", account.getSessionKey());
    	}
    	if(account.getHeadPic() != null) {
    		uw.set("head_pic", account.getHeadPic());
    	}
    	if(account.getGender() != null) {
    		uw.set("gender", account.getGender());
    	}
    	if(account.getType() != null) {
    		uw.set("type", account.getType());
    	}
    	if(account.getAmount() != null) {
    		uw.set("amount", account.getAmount());
    	}
    	if(account.getDonate() != null) {
    		uw.set("donate", account.getDonate());
    	}
    	if(account.getIntegral() != null) {
    		uw.set("integral", account.getIntegral());
    	}
    	if(account.getIdentityNo() != null) {
    		uw.set("identity_no", account.getIdentityNo());
    	}
    	
		return update(uw);
	}

	@Override
	public void getSmsMessage(String phoneNo) {
		
	}

}
