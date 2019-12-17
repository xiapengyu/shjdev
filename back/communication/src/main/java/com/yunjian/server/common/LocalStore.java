package com.yunjian.server.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;

public class LocalStore {
	
	private static volatile LocalStore instance;
	
	@Setter @Getter
	private Map<String,ChannelHandlerContext> ctxMap = new ConcurrentHashMap<>(); // <deviceCode,ctx>
	
	@Setter @Getter
	private Map<String, Integer> timeoutMap = new ConcurrentHashMap<>(); // <deviceCode,timeoutCounter<Integer>>
	
	private LocalStore() {}
	
	public static LocalStore getInstance() {
		if (instance == null) {
            synchronized (LocalStore.class) {
                if (instance == null) {
                	instance = new LocalStore();
                }
            }
        }
        return instance;
	}

	public void addCtx(String key, ChannelHandlerContext ctx) {
		ctxMap.put(key, ctx);
	}
	
	/**
	 * 记录超时次数
	 * @param remoteAddr
	 */
	public void addTimeoutCount(String deviceCode) {
		if (timeoutMap.containsKey(deviceCode)) {
			int count = timeoutMap.get(deviceCode);
			timeoutMap.put(deviceCode, count + 1);
		} else {
			timeoutMap.put(deviceCode, 1);
		}
	}
	
	/**
	 * 获取超时次数
	 * @param remoteAddr
	 * @return
	 */
	public int getTimeoutCount(String deviceCode) {
		return timeoutMap.get(deviceCode);
	}
	
	/**
	 * 资源回收
	 * 
	 * @param remoteAddr
	 */
	public void recycling(String deviceCode) {
		ctxMap.remove(deviceCode);
		timeoutMap.remove(deviceCode);
	}
	
	public String getDeviceCode(ChannelHandlerContext ctx) {
		for (String key : ctxMap.keySet()) {
			ChannelHandlerContext cacheCtx = ctxMap.get(key);
			if (cacheCtx.equals(ctx)) {
				return key;
			}
		}
		return null;
	}
	
	public ChannelHandlerContext getCtx(String key) {
		return ctxMap.get(key);
	}

}
