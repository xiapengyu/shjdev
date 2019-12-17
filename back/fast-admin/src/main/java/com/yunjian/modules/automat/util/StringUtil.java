package com.yunjian.modules.automat.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StringUtil {
	
	private static Logger logger = LoggerFactory.getLogger(StringUtil.class);

	public static String object2String(Object obj) {
		if (obj == null) {
			return "";
		} else {
			return obj.toString();
		}
	}
}
