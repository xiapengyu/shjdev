package com.yunjian.api.controller;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.Maps;
import com.yunjian.api.dto.LoginDto;
import com.yunjian.common.utils.R;
import com.yunjian.modules.automat.entity.VerifyCodeEntity;
import com.yunjian.modules.automat.entity.WxAccountEntity;
import com.yunjian.modules.automat.service.VerifyCodeService;
import com.yunjian.modules.automat.service.WxAccountService;
import com.yunjian.modules.automat.util.DateUtil;
import com.yunjian.modules.automat.util.HttpUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(tags = "微信登录接口")
@RequestMapping("/wx")
public class WxLoginController {

	@Autowired
	private WxAccountService wxAccountService;

	@Resource
	private RestTemplate restTemplate;
	@Autowired
	private VerifyCodeService verifyCodeService;

	@Value("${wx.applet.appid}")
	private String appid;

	@Value("${wx.applet.appsecret}")
	private String appSecret;

	@ApiOperation(value = "小程序登录")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "jsCode", value = "wx.login()返回的res", required = true, dataType = "String"), })
	@PostMapping("/login")
	public String wxLogin(String jsCode, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String code2SessionUrl = "https://api.weixin.qq.com/sns/jscode2session";
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("appid", appid);
		params.add("secret", appSecret);
		params.add("js_code", jsCode);
		params.add("grant_type", "authorization_code");
		URI code2Session = HttpUtil.getURIwithParams(code2SessionUrl, params);
		String result = restTemplate
				.exchange(code2Session, HttpMethod.GET, new HttpEntity<String>(new HttpHeaders()), String.class)
				.getBody();
		session.setAttribute("result", result);
		return result;
	}

	@ApiOperation(value = "保存用户信息")
	@PostMapping("/saveAccountInfo")
	public String saveAccountInfo(@RequestBody WxAccountEntity account) {
		log.info("保存用户信息>>[{}]", account);
		account.setCreateTime(new Date());
		boolean b = wxAccountService.saveOrUpdate(account);
		return b ? "success" : "error";
	}

	@ApiOperation(value = "更新用户信息,openId为必须")
	@PostMapping("/updateAccountInfo")
	public R updateAccountInfo(@RequestBody LoginDto account) {
		log.info("更新保存用户信息>>[{}]", account);
		if(StringUtils.isEmpty(account.getPhoneNo())) {
			return R.error("请输入手机号");
		}
		if(StringUtils.isEmpty(account.getVerifyCode())) {
			return R.error("请输入验证码");
		}
		// 验证码校验
		QueryWrapper<VerifyCodeEntity> entityQuery = new QueryWrapper<VerifyCodeEntity>();
		entityQuery.eq("phone", account.getPhoneNo()).orderByDesc("create_time");
		List<VerifyCodeEntity> codes = verifyCodeService.list(entityQuery);
		if (codes != null && codes.size() > 0) {
			VerifyCodeEntity codeEntity = codes.get(0);
			if (DateUtil.get2DateMinute(codeEntity.getCreateTime(), new Date()) > 5 || codeEntity.getIsRead() == 1) {
				return R.error("验证码无效，请重新获取");
			} else {
				if (codeEntity.getCode().equalsIgnoreCase(account.getVerifyCode())) {
					//将验证码置为无效
					UpdateWrapper<VerifyCodeEntity> entityUpdate = new UpdateWrapper<VerifyCodeEntity>();
					entityUpdate.eq("id", codeEntity.getId());
					VerifyCodeEntity record = new VerifyCodeEntity();
					record.setIsRead(1);
					verifyCodeService.update(record, entityUpdate);
					//更新用户信息
					boolean b = wxAccountService.updateAccount(account);
					return b ? R.ok("success") : R.error("error");
				} else {
					R.error("验证码错误");
				}
			}
		} else {
			return R.error("验证码无效，请重新获取");
		}
		return R.error();
	}

	@ApiOperation(value = "根据openId查用户信息（用于登录前校验）")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "openId", value = "微信的openId", required = true, dataType = "String"), })
	@GetMapping("/queryAccountInfo")
	public Map<String, Object> queryAccountInfo(String openId) {
		QueryWrapper<WxAccountEntity> qw = new QueryWrapper<WxAccountEntity>();
		qw.eq("wx_open_id", openId);
		WxAccountEntity account = wxAccountService.getOne(qw);
		Map<String, Object> map = Maps.newHashMap();
		if (account == null) {
			map.put("result", "none");
		} else {
			map.put("result", account);
		}
		return map;
	}

	@ApiOperation(value = "查询是否存在该手机号")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "openId", value = "微信的openId", required = true, dataType = "String"), })
	@GetMapping("/queryPhoneNo")
	public Map<String, Object> queryPhoneNo(String phoneNo) {
		QueryWrapper<WxAccountEntity> qw = new QueryWrapper<WxAccountEntity>();
		qw.eq("phone_no", phoneNo);
		int count = wxAccountService.count(qw);
		Map<String, Object> map = Maps.newHashMap();
		if (count == 0) {
			map.put("result", 0);
		} else {
			map.put("result", 1);
		}
		return map;
	}

}
