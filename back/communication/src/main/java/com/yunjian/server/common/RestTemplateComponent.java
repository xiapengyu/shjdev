package com.yunjian.server.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

@Component
public class RestTemplateComponent {
	private static final Logger logger = LoggerFactory.getLogger(RestTemplateComponent.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	public String restTemplatePOST( Object data , String url) {
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		String jsonStr = JSONObject.toJSONString(data);
		HttpEntity<String> formEntity = new HttpEntity<String>(jsonStr, headers);
		String result = restTemplate.postForObject(url, formEntity, String.class);
		logger.info("平台的响应结果为：{}", result);
		return result;
	}

}
