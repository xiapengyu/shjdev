/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.yunjian.modules.sys.service;


import com.yunjian.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 代码生成器
 * 
 * @author Mark sunlightcs@gmail.com
 */
public interface SysGeneratorService {

	public PageUtils queryList(Map<String, Object> params);

	public Map<String, String> queryTable(String tableName);

	public List<Map<String, String>> queryColumns(String tableName);

	public byte[] generatorCode(String[] tableNames,Map<String,String> configMap);
}
