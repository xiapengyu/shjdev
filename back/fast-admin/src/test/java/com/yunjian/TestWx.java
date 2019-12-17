package com.yunjian;

import java.net.URI;
import java.text.MessageFormat;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.zxing.qrcode.encoder.QRCode;
import com.yunjian.modules.automat.util.HttpUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestWx {
	
	@Resource
    private RestTemplate restTemplate;

	@Test
	public void testLogin() {
		String jsCode = "061xzm7l1R7ETm02Cg9l19Vh7l1xzm7p";
		String appid = "wx13d17f8e85711067";
		String code2SessionUrl = "https://api.weixin.qq.com/sns/jscode2session";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("appid", appid);
//        params.add("secret", appSecret);
        params.add("js_code", jsCode);
        params.add("grant_type", "authorization_code");
        URI code2Session = HttpUtil.getURIwithParams(code2SessionUrl, params);
        String result = restTemplate.exchange(code2Session, HttpMethod.GET, new HttpEntity<String>(new HttpHeaders()), String.class).getBody();
        System.out.println(result);
	}
	
	@Test
	public void qrcodeTest() {
		String qrUrl = MessageFormat.format("123{0},123{1}", "AAA", "BBB");
		System.out.println(qrUrl);
	}

}
