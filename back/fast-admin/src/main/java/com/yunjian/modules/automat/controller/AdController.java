package com.yunjian.modules.automat.controller;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import com.yunjian.common.annotation.SysLog;
import com.yunjian.common.exception.RRException;
import com.yunjian.common.utils.DateUtils;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.R;
import com.yunjian.common.validator.ValidatorUtils;
import com.yunjian.common.validator.group.AddGroup;
import com.yunjian.modules.automat.entity.AdEntity;
import com.yunjian.modules.automat.entity.DeviceEntity;
import com.yunjian.modules.automat.service.AdService;
import com.yunjian.modules.automat.service.DeviceService;
import com.yunjian.modules.automat.vo.SendAdParamVo;
import com.yunjian.modules.sys.controller.AbstractController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 广告管理
 */
@RestController
@RequestMapping("/automat/ad")
@Api(tags = "广告接口")
public class AdController extends AbstractController {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private AdService adService;
	@Autowired
	private DeviceService deviceService;

	@Value("${oss.endpoint}")
	private String endpoint;
	@Value("${oss.accessKeyId}")
	private String accessKeyId;
	@Value("${oss.accessKeySecret}")
	private String accessKeySecret;
	@Value("${oss.bucketName}")
	private String bucketName;
	@Value("${oss.publicUrl}")
	private String publicUrl;

	@Autowired
	private RestTemplate restTemplate;
	@Value("${communication.base.path}")
	private String communicationBase;
	@Value("${communication.sendAdUrl}")
	private String sendAdUrl;

	// 文件存储目录
	private String filedir = "automat/";

	private String logDir = "log/";

	@ApiOperation(value = "设备端请求广告")
	@ApiImplicitParams({ @ApiImplicitParam(name = "ids", value = "广告id列表", required = true, dataType = "Integer"), })
	@PostMapping("/adList")
	public R adListForDevice(@RequestBody List<Integer> ids) {
		R r = R.ok().put("adList", adService.adListForDevice(ids));
		return r;
	}

	/**
	 * 所有广告列表
	 */
	@GetMapping("/list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = adService.queryPage(params);
		return R.ok().put("page", page);
	}

	@GetMapping("/querySendAdList")
	public R querySendAdList() {
		try {
			QueryWrapper<AdEntity> adQuery = new QueryWrapper<AdEntity>();
			adQuery.eq("status", 1).orderByDesc("create_time");
			List<AdEntity> adList = adService.list(adQuery);
			
			QueryWrapper<DeviceEntity> deviceQuery = new QueryWrapper<DeviceEntity>();
			deviceQuery.eq("online_status", 0);
			List<DeviceEntity> deviceList = deviceService.list(deviceQuery);
			/*List<DeviceEntity> deviceList = deviceService.list();*/
			return R.ok().put("adList", adList).put("deviceList", deviceList);
		} catch (Exception e) {
			return R.error("查询数据失败");
		}
	}

	@ApiOperation(value = "下发广告到设备")
	@RequestMapping(value = "/sendAdd", method = RequestMethod.POST)
	public R sendAdd(@RequestBody SendAdParamVo param) {
		try {
			List<Integer> adIdList = param.getAdIdList();
			List<String> deviceCodeList = param.getDeviceCodeList();
			if (adIdList == null || adIdList.isEmpty()) {
				return R.error("没有选择广告");
			} else if (deviceCodeList == null || deviceCodeList.isEmpty()) {
				return R.error("没有选择设备");
			} else {
				logger.info("开始下发广告");
				String result = sendAd(param);
				return R.ok().put("result", result);
			}
		} catch (Exception e) {
			logger.error("下发广告到设备失败>>>[{}]", e);
			return R.error("下发广告到设备失败");
		}
	}

	private String sendAd(SendAdParamVo param) {
		logger.info("发送广告给设备[{}],下行出货指令：[{}]", param.getAdIdList(), param.getDeviceCodeList());
		Map<String, Object> params = Maps.newHashMap();
		params.put("adIdList", param.getAdIdList());
		params.put("deviceCodeList", param.getDeviceCodeList());

		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		String jsonStr = JSONObject.toJSONString(params);
		HttpEntity<String> formEntity = new HttpEntity<String>(jsonStr, headers);
		String result = restTemplate.postForObject(communicationBase + sendAdUrl, formEntity, String.class);
		logger.info("平台的响应结果为：{}", result);
		return result;
	}

	/**
	 * 广告信息
	 */
	@GetMapping("/info/{id}")
	public R info(@PathVariable("id") Long id) {
		QueryWrapper<AdEntity> query = new QueryWrapper<AdEntity>();
		query.eq("ad_id", id);
		AdEntity data = adService.getOne(query);
		return R.ok().put("ad", data);
	}

	/**
	 * 保存配置
	 */
	@ApiOperation(value = "保存广告")
	@PostMapping("/save")
	public R save(@RequestBody AdEntity data) {
		logger.info("保存广告>>>[{}]", data);
		data.setCreateTime(new Date());
		adService.save(data);
		return R.ok();
	}

	@ApiOperation(value = "上传广告文件")
	@PostMapping("/uploadAdFile")
	public R uploadGoodsImg(@RequestParam("file") MultipartFile file) throws Exception {
		if (file.isEmpty()) {
			throw new RRException("上传文件不能为空");
		}
		/*
		 * if (file.getSize() > 1024 * 1024 * 100) { return R.error("图片超过100M，请重新上传"); }
		 */

		String originalFilename = file.getOriginalFilename();
		String extend = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
		String fileName = UUID.randomUUID().toString().replaceAll("-", "") + extend;
		try {
			InputStream inputStream = file.getInputStream();
			this.uploadFile2OSS(inputStream, fileName);
		} catch (Exception e) {
			return R.error("上传失败");
		}
		String url = publicUrl + filedir + fileName;
		logger.info("文件url>>>[{}]", url);
		return R.ok().put("url", url);
	}

	@ApiOperation(value = "上传日志文件")
	@PostMapping("/uploadLogFile")
	public R uploadLogFile(@RequestParam("file") MultipartFile file) throws Exception {
		logger.info("开始上传日志文件......");
		if (file.isEmpty()) {
			throw new RRException("上传文件不能为空");
		}
		/*
		 * if (file.getSize() > 1024 * 1024 * 100) { return R.error("图片超过100M，请重新上传"); }
		 */

		String originalFilename = file.getOriginalFilename();
		String extend = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
		String timestamp = System.currentTimeMillis() + "";
		String name = "LOG_" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + "_" + timestamp.substring(8);
		// String fileName = UUID.randomUUID().toString().replaceAll("-", "") + extend;
		String fileName = name + extend;
		logger.info(new Date() + "日志文件上传目录>>>【{}】", fileName);
		try {
			InputStream inputStream = file.getInputStream();
			this.uploadFile2OSS(inputStream, fileName, logDir);
		} catch (Exception e) {
			return R.error("上传失败");
		}
		String url = publicUrl + logDir + fileName;
		logger.info("日志文件全路径>>>[{}]", url);
		return R.ok().put("url", url);
	}

	@ApiOperation(value = "下载OSS文件")
	@PostMapping("/downFile")
	public void downFile(@RequestBody Map<String, Object> param) {
		try {
			String url = param.get("url").toString();

			// OSSClient client= new OSSClient(endpoint, accessKeyId,
			// accessKeySecret).getObject(new GetObjectRequest(bucketName, url), new
			// File(path));

			OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

			ObjectMetadata objectMetadata = ossClient.getObject(new GetObjectRequest(bucketName, url),
					new File("D:\\logs"));
			System.out.println(objectMetadata);
			/*
			 * DownloadFileRequest downloadFileRequest = new DownloadFileRequest(bucketName,
			 * "LOG_20190816160255_993.txt", url, 10000); DownloadFileResult result =
			 * ossClient.downloadFile(downloadFileRequest); ObjectMetadata objectMetadata =
			 * result.getObjectMetadata();
			 */
		} catch (Exception e) {
			logger.error("下载OSS文件失败", e);
		}
	}

	/**
	 * 修改配置
	 */
	@SysLog("修改广告")
	@PostMapping("/update")
	public R update(@RequestBody AdEntity adData) {
		QueryWrapper<AdEntity> query = new QueryWrapper<AdEntity>();
		query.eq("ad_id", adData.getAdId());
		adService.update(adData, query);
		return R.ok();
	}

	/**
	 * 启用禁用
	 */
	@SysLog("启用禁用广告")
	@PostMapping("/statusChange")
	@RequiresPermissions("automat:ad:statusChange")
	public R statusChange(@RequestBody AdEntity adData) {
		ValidatorUtils.validateEntity(adData, AddGroup.class);
		adService.statusChange(adData);
		return R.ok();
	}

	/**
	 * 删除配置
	 */
	@SysLog("删除广告")
	@PostMapping("/delete")
	@RequiresPermissions("automat:ad:delete")
	public R delete(@RequestBody Long[] ids) {
		adService.removeByIds(Arrays.asList(ids));
		return R.ok();
	}

	/**
	 * 上传图片获取fileUrl
	 * 
	 * @param instream
	 * @param fileName
	 * @return
	 */
	public String uploadFile2OSS(InputStream instream, String fileName) {
		String ret = "";
		try {
			// 创建上传Object的Metadata
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentLength(instream.available());
			objectMetadata.setCacheControl("no-cache");
			objectMetadata.setHeader("Pragma", "no-cache");
			objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
			objectMetadata.setContentDisposition("inline;filename=" + fileName);
			// 上传文件
			OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
			PutObjectResult putResult = ossClient.putObject(bucketName, filedir + fileName, instream, objectMetadata);
			ret = putResult.getETag();
		} catch (IOException e) {
			logger.error("上传文件失败>>>[{}]", e);
		} finally {
			try {
				if (instream != null) {
					instream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	/**
	 * 上传图片获取fileUrl
	 * 
	 * @param instream
	 * @param fileName
	 * @return
	 */
	public String uploadFile2OSS(InputStream instream, String fileName, String logDir) {
		String ret = "";
		try {
			// 创建上传Object的Metadata
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentLength(instream.available());
			objectMetadata.setCacheControl("no-cache");
			objectMetadata.setHeader("Pragma", "no-cache");
			objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
			objectMetadata.setContentDisposition("inline;filename=" + fileName);
			// 上传文件
			OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
			PutObjectResult putResult = ossClient.putObject(bucketName, logDir + fileName, instream, objectMetadata);
			ret = putResult.getETag();
		} catch (IOException e) {
			logger.error("上传文件失败>>>[{}]", e);
		} finally {
			try {
				if (instream != null) {
					instream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.info(new Date() + "日志文件上传目录>>>【{}】", ret);
		return ret;
	}

	public String getcontentType(String FilenameExtension) {
		if (FilenameExtension.equalsIgnoreCase(".bmp")) {
			return "image/bmp";
		}
		if (FilenameExtension.equalsIgnoreCase(".gif")) {
			return "image/gif";
		}
		if (FilenameExtension.equalsIgnoreCase(".jpeg") || FilenameExtension.equalsIgnoreCase(".jpg")
				|| FilenameExtension.equalsIgnoreCase(".png")) {
			return "image/jpeg";
		}
		if (FilenameExtension.equalsIgnoreCase(".html")) {
			return "text/html";
		}
		if (FilenameExtension.equalsIgnoreCase(".txt")) {
			return "text/plain";
		}
		if (FilenameExtension.equalsIgnoreCase(".vsd")) {
			return "application/vnd.visio";
		}
		if (FilenameExtension.equalsIgnoreCase(".pptx") || FilenameExtension.equalsIgnoreCase(".ppt")) {
			return "application/vnd.ms-powerpoint";
		}
		if (FilenameExtension.equalsIgnoreCase(".docx") || FilenameExtension.equalsIgnoreCase(".doc")) {
			return "application/msword";
		}
		if (FilenameExtension.equalsIgnoreCase(".xml")) {
			return "text/xml";
		}
		return "image/jpeg";
	}
	
	@ApiOperation(value = "上传日志文件")
	@PostMapping("/uploadLogFile2")
	public void uploadLogsFile(HttpServletRequest request, HttpServletResponse response) {
		try {
			logger.info("开始上传日志文件......");
			InputStream is = request.getInputStream();
			DataInputStream dis = new DataInputStream(is);
			String result = "";
			try {
				result = saveFile(dis);
			} catch (Exception e) {
				logger.error("upload error", e);
			}
			request.getSession().invalidate();
			response.setContentType("text/html;charset=UTF-8");
			ObjectOutputStream dos = new ObjectOutputStream(response.getOutputStream());
			dos.writeObject(result);
			dos.flush();
			dos.close();
			dis.close();
			is.close();
		} catch (IOException e) {
			logger.error("上传文件失败", e);
		}
	}
	
	private String saveFile(DataInputStream dis) {
		String timestamp = System.currentTimeMillis() + "";
		String name = "LOG_" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + "_" + timestamp.substring(8) + ".txt";
        String fileurl = "/data/shj/api/" + name;
        File file = new File(fileurl);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
            	logger.error("saveFile error", e);
            }
        }
        FileOutputStream fps = null;
        try {
            fps = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int length = -1;
        try {
            while ((length = dis.read(buffer)) != -1) {
                fps.write(buffer, 0, length);
            }
        } catch (IOException e) {
        	logger.error("saveFile error", e);
        }
        try {
            fps.flush();
            fps.close();
        } catch (IOException e) {
        	logger.error("saveFile error", e);
        }
        
        //开始从本地服务器上传到oss
        try {
        	InputStream inputStream = new FileInputStream(file);
			//InputStream inputStream = file.getInputStream();
			this.uploadFile2OSS(inputStream, name, logDir);
		} catch (Exception e) {
			logger.error("上传失败", e);
			return "fail";
		}
		String url = publicUrl + filedir + name;
		logger.info("日志完整路径>>>[{}]", url);
		file.delete();
        return url;
    }
}
