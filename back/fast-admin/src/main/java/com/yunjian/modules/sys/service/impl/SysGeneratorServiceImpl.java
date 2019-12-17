package com.yunjian.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yunjian.common.utils.GenUtils;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.Query;
import com.yunjian.modules.sys.dao.GeneratorDao;
import com.yunjian.modules.sys.service.SysGeneratorService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

@Service
public class SysGeneratorServiceImpl implements SysGeneratorService {

    @Autowired
	private GeneratorDao generatorDao;

    @Override
	public PageUtils queryList(Map<String, Object> params) {
        IPage<Map<String,Object>> page = new Query<Map<String,Object>>().getPage(params);
        List<Map<String, Object>> list = generatorDao.queryList(page, params);
        page.setRecords(list);
        return new PageUtils(page);
	}
    @Override
	public Map<String, String> queryTable(String tableName) {
		return generatorDao.queryTable(tableName);
	}

    @Override
	public List<Map<String, String>> queryColumns(String tableName) {
		return generatorDao.queryColumns(tableName);
	}
    @Override
	public byte[] generatorCode(String[] tableNames,Map<String,String> configMap) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);
		for(String tableName : tableNames){
			//查询表信息
			Map<String, String> table = queryTable(tableName);
			//查询列信息
			List<Map<String, String>> columns = queryColumns(tableName);
			//生成代码
			GenUtils.generatorCode(table, columns, zip,configMap);
		}
		IOUtils.closeQuietly(zip);
		return outputStream.toByteArray();
	}
}
