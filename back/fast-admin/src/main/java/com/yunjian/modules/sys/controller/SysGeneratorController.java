
package com.yunjian.modules.sys.controller;

import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.R;
import com.yunjian.modules.sys.service.SysGeneratorService;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 代码生成器
 * 
 * @author Mark sunlightcs@gmail.com
 */
@Controller
@RequestMapping("/sys/generator")
public class SysGeneratorController {
	@Autowired
	private SysGeneratorService sysGeneratorService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("sys:generator:list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils pageUtil = sysGeneratorService.queryList(params);
		
		return R.ok().put("page", pageUtil);
	}
	
	/**
	 * 生成代码
	 */
	@RequestMapping("/code")
	@RequiresPermissions("sys:generator:code")
	public void code(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException{
		String tables = params.get("tables");
		params.remove("tables");
		byte[] data = sysGeneratorService.generatorCode(tables.split(","),params);
		response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"fast.zip\"");
        response.addHeader("Content-Length", "" + data.length);  
        response.setContentType("application/octet-stream; charset=UTF-8");  
  
        IOUtils.write(data, response.getOutputStream());  
	}
}
