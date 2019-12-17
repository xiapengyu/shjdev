package com.yunjian.modules.automat.exception;

import org.apache.commons.lang3.StringUtils;

/**
 * Common Exception
 * 
 */
public class CommonException extends BaseException {

	/**
	 * @Field long serialVersionUID
	 */
	private static final long serialVersionUID = -4527567935254966321L;

	public CommonException(String code) {
		super(code, null, null, null);
	}

	public CommonException(String code, String message) {
		super(code, message, null, null);
	}

	public CommonException(String code, String message, Object[] values) {
		super(code, message, values, null);
	}

	public CommonException(String code, String message, Object[] values, Throwable cause) {
		super(code, message, values, cause);
	}

	public String getErrorCode() {
		String errorCode = "00099";
		String message = getMessage() == null ? getCode() : getMessage();
		if (!StringUtils.isEmpty(message)) {
			String[] messageValues = StringUtils.split(message, ":");
			if (messageValues.length > 1) {
				errorCode = messageValues[0];
			}
		}
		return errorCode;
	}
}
