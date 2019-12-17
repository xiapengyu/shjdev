package com.yunjian.modules.app.controller;

import com.yunjian.common.utils.R;
import com.yunjian.common.validator.ValidatorUtils;
import com.yunjian.modules.app.entity.UserEntity;
import com.yunjian.modules.app.form.RegisterForm;
import com.yunjian.modules.app.service.UserService;
import com.yunjian.modules.automat.util.StrUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 注册
 *
 *
 */
@RestController
@RequestMapping("/app")
@Api("APP注册接口")
public class AppRegisterController {
    @Autowired
    private UserService userService;
    
    @Value("${user.sign.key}")
	private String userSignKey = "";

    @PostMapping("register")
    @ApiOperation("注册")
    public R register(@RequestBody RegisterForm form){
        //表单校验
        ValidatorUtils.validateEntity(form);

        UserEntity user = new UserEntity();
        user.setMobile(form.getMobile());
        user.setUsername(form.getMobile());
        user.setAccount(form.getMobile());
        user.setPassword(DigestUtils.sha256Hex(form.getPassword()));
        user.setCreateTime(new Date());
        user.setAmount(0f);
        String sign = StrUtil.createAccountSign(form.getMobile(), new BigDecimal(0), userSignKey);
        user.setSign(sign);
        userService.save(user);

        return R.ok();
    }
}
