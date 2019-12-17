package com.yunjian;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class MyGenerator2 {
	
	/**
	 * 代码文件生成路径（根据自己需要修改）
	 */
	public static String outPutPath = "D:\\logs";
	
	/**
	 * 默认模块名（可不改）
	 */
	public static String moduleName = "automat";
	/**
	 * 表名（根据表名修改）
	 */
	public static String tableName = "tb_verify_code";
	public static String dataBaseUserName = "shj";
	public static String dataBasePassword = "Shj123";
	public static String dataBaseUrl = "jdbc:mysql://119.23.52.9:3306/shj?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false";

	public static void main(String[] args) {
		shell(moduleName, tableName);
	}

	private static void shell(String moduleName, String tableName) {
		AutoGenerator mpg = new AutoGenerator().setGlobalConfig(
				//全局配置
				new GlobalConfig().setOutputDir(outPutPath)
				.setFileOverride(true)
				.setActiveRecord(true)
				.setEnableCache(false)
				.setBaseResultMap(true)
				.setBaseColumnList(false)
				.setMapperName("%sMapper")
				.setXmlName("%sMapper")
				.setServiceName("%sService")
				.setServiceImplName("%sServiceImpl")
				.setControllerName("%sController")
				.setEntityName("%sEntity")
				.setAuthor("laizhiwen")
		).setDataSource(
				//数据源配置
				new DataSourceConfig()
				.setDbType(DbType.MYSQL)
				.setDriverName("com.mysql.jdbc.Driver")
				.setUsername(dataBaseUserName)
				.setPassword(dataBasePassword)
				.setUrl(dataBaseUrl)
		).setStrategy(
				//策略配置
				new StrategyConfig()
				// strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
				.setTablePrefix(new String[] { "tb_" })// 此处可以修改为您的表前缀
				.setNaming(NamingStrategy.underline_to_camel)// 表名生成策略
				.setInclude(new String[] { tableName }) // 需要生成的表
				// 自定义实体父类
				// strategy.setSuperEntityClass("com.spf.model.Entity");
				// 自定义 mapper 父类
				.setSuperMapperClass("com.baomidou.mybatisplus.core.mapper.BaseMapper")
				// 自定义 service 父类
				.setSuperServiceClass("com.baomidou.mybatisplus.extension.service.IService")
				// 自定义 service 实现类父类
				.setSuperServiceImplClass("com.baomidou.mybatisplus.extension.service.impl.ServiceImpl")
				// 自定义 controller 父类
				.setSuperControllerClass("com.yunjian.modules.sys.controller.AbstractController")
		).setPackageInfo(
				new PackageConfig()
				.setParent("com.yunjian.modules")
				.setModuleName(moduleName)
				.setController("controller")
				.setEntity("entity")
				.setMapper("dao")
				.setService("service")
				.setServiceImpl("service.impl")
		);

		InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
            	Map<String, Object> map = new HashMap<String, Object>();
				map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
				map.put("mainPath", "com.yunjian");
				map.put("moduleName", moduleName);
				this.setMap(map);
            }
        };
        
		List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
		focList.add(new FileOutConfig("/template/Dao.java.vm") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				return "/dao" + tableInfo.getEntityName() + "Mapper.java";
			}
		});
		focList.add(new FileOutConfig("/template/Dao.xml.vm") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				return "/src/main/resources/mapper/automat/" + tableInfo.getEntityName() + "Mapper.xml";
			}
		});
		focList.add(new FileOutConfig("/template/Service.java.vm") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				return "/service" + tableInfo.getEntityName() + "Service.java";
			}
		});
		focList.add(new FileOutConfig("/template/ServiceImpl.java.vm") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				return "/service/impl" + tableInfo.getEntityName() + "ServiceImpl.java";
			}
		});
		focList.add(new FileOutConfig("/template/Controller.java.vm") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				return "/controller" + tableInfo.getEntityName() + "Controller.java";
			}
		});
		focList.add(new FileOutConfig("/template/Entity.java.vm") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				return "/entity" + tableInfo.getEntityName() + "Entity.java";
			}
		});
		cfg.setFileOutConfigList(focList);
		mpg.setCfg(cfg);

		// 执行生成
		mpg.execute();

		// 打印注入设置
		// System.out.println(mpg.getCfg().getMap().get("abc"));
	}

}
