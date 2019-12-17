package com.yunjian.modules.automat.util;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UUIDUtil {
	private static Logger log = LoggerFactory.getLogger(UUIDUtil.class);

  private UUIDUtil() {}

  public static String getUUID() {
    return StringUtils.replace(UUID.randomUUID().toString(), "-", "");
  }
  
  public static void main(String[] arg) {
		for(int i = 0;i<5;i++) {
			log.info(StringUtils.replace(UUID.randomUUID().toString(), "-", ""));
		}
		
	}
}
