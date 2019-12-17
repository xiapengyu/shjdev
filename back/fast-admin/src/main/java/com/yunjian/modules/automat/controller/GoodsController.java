package com.yunjian.modules.automat.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yunjian.common.exception.RRException;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.R;
import com.yunjian.modules.automat.entity.GoodsEntity;
import com.yunjian.modules.automat.service.GoodsService;
import com.yunjian.modules.automat.vo.GoodsExt;
import com.yunjian.modules.automat.vo.ReplenishmentVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-16
 */
@Api(tags = "商品相关接口")
@RestController
@RequestMapping("/automat/goods")
public class GoodsController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private GoodsService goodsService;
	
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

	// 文件存储目录
	private String filedir = "automat/";

	@GetMapping("/list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = goodsService.queryPage(params);
		return R.ok().put("page", page);
	}

	@GetMapping("/info/{id}")
	public R goodsInfo(@PathVariable("id") Long id) {
		QueryWrapper<GoodsEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("goods_id", id);
		GoodsEntity dictData = goodsService.getOne(queryWrapper);
		logger.info("查询商品信息>>>[{}]", dictData);
		return R.ok().put("donateData", dictData);
	}

	@ApiOperation(value = "上传图片")
	@PostMapping("/uploadGoodsImg")
	public R uploadGoodsImg(@RequestParam("file") MultipartFile file) throws Exception {
		if (file.isEmpty()) {
			throw new RRException("上传文件不能为空");
		}
		if (file.getSize() > 1024 * 1024 * 5) {
			return R.error("图片超过5M，请重新上传");
		}
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
		return R.ok().put("url", url);
	}

	/**
	 * 新增商品
	 * 
	 * @param goodsEntity
	 * @return
	 */
	@PostMapping("/saveGoods")
	public R saveGoods(@RequestBody GoodsEntity goodsEntity) {
		logger.info("新增商品>>>[{}]", goodsEntity);
		goodsEntity.setCreateTime(new Date());
		goodsService.saveGoods(goodsEntity);
		return R.ok();
	}

	/**
	 * 修改商品
	 * 
	 * @param goodsEntity
	 * @return
	 */
	@PostMapping("/updateGoods")
	public R updateGoods(@RequestBody GoodsEntity goodsEntity) {
		logger.info("修改商品>>>[{}]", goodsEntity);
		goodsService.updateGoods(goodsEntity);
		return R.ok();
	}

	@ApiOperation(value = "【商品】详情", produces = "获取单个商品详情信息的都可以调用本接口")
	@ApiImplicitParams({ @ApiImplicitParam(name = "goodsId", value = "商品id", required = true, dataType = "String"), })
	@RequestMapping(value = "/client/goodsList", method = RequestMethod.GET)
	public GoodsEntity findById(String goodsId) {
		return goodsService.getById(goodsId);
	}

	@ApiOperation(value = "【配送记录】详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "deliverId", value = "配送记录id", required = true, dataType = "Integer"), })
	@RequestMapping(value = "/client/deliveryGoodsList", method = RequestMethod.GET)
	public List<GoodsEntity> findByDeliverId(Integer deliverId) {
		return goodsService.deliveryGoodsList(deliverId);
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
			return "";
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

	@ApiOperation(value = "【商品-小程序】点击设备显示商品列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "deviceCode", value = "设备编码", required = true, dataType = "String"), })
	@RequestMapping(value = "/goodsList", method = RequestMethod.GET)
	public List<GoodsExt> goodsList(String deviceCode) {
		return goodsService.goodsList(deviceCode);
	}

	/**
	 * 预警管理中-发送配送员弹出窗信息
	 * 
	 * @param deviceCode
	 * @return
	 */
	@RequestMapping(value = "/stockoutGoodsList/{deviceCode}", method = RequestMethod.GET)
	public R stockoutGoodsList(@PathVariable("deviceCode") String deviceCode) {
		R r = goodsService.stockoutGoodsList(deviceCode);
		return r;
	}

	@RequestMapping(value = "/allGoods", method = RequestMethod.GET)
	public R allGoods() {
		List<GoodsEntity> goodsList = goodsService.list();
		R r = R.ok().put("goodsList", goodsList);
		return r;
	}

	/**
	 * 补货接口
	 * 
	 * @return
	 */
	@ApiOperation(value = "补货接口")
	@RequestMapping(value = "/replenishment", method = RequestMethod.POST)
	public R replenishment(@RequestBody ReplenishmentVo params) {
		boolean b = goodsService.replenishment(params);
		return b?R.ok():R.error();
	}

}
