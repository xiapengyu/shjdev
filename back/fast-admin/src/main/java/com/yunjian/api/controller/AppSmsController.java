package com.yunjian.api.controller;


import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.yunjian.common.utils.R;
import com.yunjian.modules.automat.entity.VerifyCodeEntity;
import com.yunjian.modules.automat.service.VerifyCodeService;
import com.yunjian.modules.automat.util.StrUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * APP测试接口
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 15:47
 */
@RestController
@RequestMapping("/app")
@Api("APP 短信接口")
public class AppSmsController {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
    
    @Value("${aliyun.sms.isEnabled}")
	private String isEnabled;
    
    @Value("${aliyun.sms.accessKeyId}")
	private String accessKeyId;
    
    @Value("${aliyun.sms.secret}")
	private String secret;
    
    @Value("${aliyun.sms.tempCode}")
	private String tempCode;
    
    @Value("${aliyun.sms.signName}")
	private String SignName;
    
    @Autowired
    private VerifyCodeService verifyCodeService;
    

    @GetMapping("getSms/{mobile}")
    @ApiOperation("获取短信验证码")
    public R getSms(@PathVariable("mobile") String mobile){
        if (StringUtils.isBlank(mobile)) {
            return R.error("手机号不可为空！");
        }

        String verifyCode = StrUtil.randomCode();
        logger.info("【{}】发送验证码【{}】", mobile, verifyCode);
//        String verifyCode = "234567";
//        if (aliyunSmsProperties.isEnabled()) {
            DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, secret);
            IAcsClient client = new DefaultAcsClient(profile);
            CommonRequest request = new CommonRequest();
            request.setMethod(MethodType.POST);
            request.setDomain("dysmsapi.aliyuncs.com");
            request.setVersion("2017-05-25");
            request.setAction("SendSms");
            request.putQueryParameter("RegionId", "cn-hangzhou");
            request.putQueryParameter("PhoneNumbers", mobile);
            request.putQueryParameter("SignName", SignName);
            request.putQueryParameter("TemplateCode", tempCode);
            request.putQueryParameter("TemplateParam", "{\"code\":\""+verifyCode+"\"}");

            try {
                CommonResponse response = client.getCommonResponse(request);
                logger.info("返回结果>>>[{}]", response.toString());
                
                JSONObject rest = JSON.parseObject(response.getData());
                logger.info("短信发送结果>>[{}]", rest);
//                if(map.get("Message").toString().equals("OK")) {
                if(rest.getString("Message").equals("OK")){
                    // 五分钟有效
//                  long expire = 60 * 5;
//                  redisUtils.set(key,verifyCode,expire);
                    //保存短信数据
                    VerifyCodeEntity entity = new VerifyCodeEntity();
                    entity.setCode(verifyCode);
                    entity.setPhone(mobile);
                    entity.setIsRead(0);
                    entity.setCreateTime(new Date());
                    verifyCodeService.saveOrUpdate(entity);
                    return R.ok();
                }
                return R.error("短信发送失败");
            }catch (Exception e) {
                logger.error("短信发送失败", e);
                return R.error("短信发送失败" + e.getMessage());
            }
            
//        } else {
//            String key = RedisKeys.getSmsCodeKey(mobile);
//            // 五分钟有效
//            long expire = 60 * 5;
//            redisUtils.set(key,verifyCode,expire);
//            return R.ok().put("data", verifyCode);
//        }
    }

}
