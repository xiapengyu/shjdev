package com.yunjian.modules.automat.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
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
import com.yunjian.modules.automat.entity.FeedbackEntity;
import com.yunjian.modules.automat.entity.FeedbackImgEntity;
import com.yunjian.modules.automat.entity.GoodsEntity;
import com.yunjian.modules.automat.entity.OrderDetailEntity;
import com.yunjian.modules.automat.entity.OrderEntity;
import com.yunjian.modules.automat.service.FeedbackImgService;
import com.yunjian.modules.automat.service.FeedbackService;
import com.yunjian.modules.automat.service.GoodsService;
import com.yunjian.modules.automat.service.OrderDetailService;
import com.yunjian.modules.automat.service.OrderService;
import com.yunjian.modules.automat.util.DateUtil;
import com.yunjian.modules.automat.vo.FeedBackDto;
import com.yunjian.modules.automat.vo.FeedBackOrder;
import com.yunjian.modules.automat.vo.OrderDetailExtDto;
import com.yunjian.modules.sys.controller.AbstractController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author laizhiwen
 * @since 2019-07-12
 */
@Api(tags = "意见反馈相关接口")
@RestController
@RequestMapping("/automat/feedback")
public class FeedbackController extends AbstractController {

	@Autowired
	private FeedbackService feedbackService;
	@Autowired
	private FeedbackImgService feedbackImgService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderDetailService orderDetailService;
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

	@ApiOperation(value = "查询单个反馈信息")
	@GetMapping("/info/{feedBackId}")
	public R info(@PathVariable("feedBackId") Integer feedBackId) {
		QueryWrapper<FeedbackEntity> query = new QueryWrapper<FeedbackEntity>();
		query.eq("feedBack_id", feedBackId);
		FeedbackEntity entity = feedbackService.getOne(query);

		FeedBackDto dto = new FeedBackDto();
		BeanUtils.copyProperties(entity, dto);

		QueryWrapper<FeedbackImgEntity> imageQuery = new QueryWrapper<FeedbackImgEntity>();
		imageQuery.eq("feedBack_id", feedBackId);
		List<FeedbackImgEntity> images = feedbackImgService.list(imageQuery);
		List<String> imgPaths = new ArrayList<String>();

		for (FeedbackImgEntity image : images) {
			imgPaths.add(image.getImgPath());
		}
		dto.setImageList(imgPaths);
		return R.ok().put("entity", dto);
	}
	
	@ApiOperation(value = "查询用户反馈列表")
	@RequestMapping(value = "/queryFeedbacks", method = RequestMethod.POST)
	public R queryFeedbacks(@RequestBody FeedBackDto dto) {
		logger.info("查询用户反馈列表>>>[{}]", dto);
		QueryWrapper<FeedbackEntity> query = new QueryWrapper<FeedbackEntity>();
		query.eq("phone", dto.getPhone());
		query.eq("type", 1);
		query.orderByDesc("create_time");
		List<FeedbackEntity> feedBackList = feedbackService.list(query);
		return R.ok().put("result", feedBackList);
	}

	@ApiOperation(value = "查询用户订单列表")
	@RequestMapping(value = "/queryFeedbackOrders", method = RequestMethod.POST)
	public R queryFeedbackOrders(@RequestBody FeedBackDto dto) {
		List<FeedBackOrder> result = new ArrayList<FeedBackOrder>();
		logger.info("查询用户订单列表>>>[{}]", dto);
		QueryWrapper<OrderEntity> queryWrapper = new QueryWrapper<OrderEntity>();
		queryWrapper.eq("phone_no", dto.getPhone());
		queryWrapper.in("pay_status", dto.getPayStatusList());
		queryWrapper.orderByDesc("create_time");
		List<OrderEntity> list = orderService.list(queryWrapper);
		if(list == null || list.size() == 0) {
			/* return R.error("没用符合条件的订单"); */
			return R.error();
		}
		List<Integer> detailIds = new ArrayList<Integer>();
		for(OrderEntity entity : list) {
			detailIds.add(entity.getOrderId());
		}
		QueryWrapper<OrderDetailEntity> detailQuery = new QueryWrapper<OrderDetailEntity>();
		detailQuery.in("order_id", detailIds);
		List<OrderDetailEntity> detailList = orderDetailService.list(detailQuery);
		
		List<Integer> goodsIds = new ArrayList<Integer>();
		for(OrderDetailEntity item : detailList) {
			goodsIds.add(item.getGoodsId());
		}
		QueryWrapper<GoodsEntity> goodsQuery = new QueryWrapper<GoodsEntity>();
		goodsQuery.in("goods_id", goodsIds);
		List<GoodsEntity> goodsList = goodsService.list(goodsQuery);
		
		for(OrderEntity order : list) {
			FeedBackOrder feedBackOrder = new FeedBackOrder();
			BeanUtils.copyProperties(order, feedBackOrder);
			QueryWrapper<FeedbackEntity> query = new QueryWrapper<FeedbackEntity>();
			query.eq("order_code", order.getOrderCode());
			List<FeedbackEntity> feedBackList = feedbackService.list(query);
			String reason = "";
			if(feedBackList != null && !feedBackList.isEmpty()) {
				reason = feedBackList.get(0).getReason();
			}
			feedBackOrder.setReason(reason);
			
			for(OrderDetailEntity detail : detailList) {
				OrderDetailExtDto detailExt = new OrderDetailExtDto();
				if(order.getOrderId() - detail.getOrderId() == 0) {
					BeanUtils.copyProperties(detail, detailExt);
					for(GoodsEntity item : goodsList) {
						if(detail.getGoodsId() == item.getGoodsId()) {
							detailExt.setImgPath(item.getImgPath());
						}
					}
					feedBackOrder.getDetails().add(detailExt);
				}
			}
			result.add(feedBackOrder);
		}
		logger.info("返回结果>>[{}]", result.size());
		return R.ok().put("orderList", result);
	}

	@ApiOperation(value = "新增意见反馈")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public R saveFeedBack(@RequestBody FeedBackDto dto) {
		logger.info("新增意见反馈>>>[{}]", dto);
		try {
			if (dto.getOrderCode() != null) {
				QueryWrapper<OrderEntity> queryWrapper = new QueryWrapper<OrderEntity>();
				queryWrapper.eq("order_code", dto.getOrderCode());
				OrderEntity order = orderService.getOne(queryWrapper);
				if(order == null) {
					return R.error("订单不存在");
				}
				if (dto.getType() == 2 && order.getPayStatus() == 0) {
					return R.error("当前订单未支付，无法退款");
				}
				if (dto.getType() == 2 && order.getPayTime() != null && DateUtil.getDistanceMins(new Date(), order.getPayTime()) > 60 * 24 * 15) {
					return R.error("当前订单结束时间超过30天，无法退款");
				}
				QueryWrapper<FeedbackEntity> query = new QueryWrapper<FeedbackEntity>();
				query.eq("order_code", dto.getOrderCode());
				if (feedbackService.list(query) != null && !feedbackService.list(query).isEmpty()) {
					for(FeedbackEntity item : feedbackService.list(query)) {
						if(dto.getType() == 2 && item.getStatus() == 1 && item.getType() == 2) {
							return R.error("订单已提交退款申请，请勿重复提交");
						}
						if(dto.getType() == 1 && item.getStatus() == 1 && item.getType() == 1) {
							return R.error("订单已提交意见反馈，请勿重复提交");
						}
					}
				}
				//退款反馈需要将订单状态改为退款中
				if((dto.getType() - 2) == 0) {
					order.setArefundTime(new Date());
					order.setPayStatus(2);	//退款中
				}
				logger.info("修改订单状态>>>[{}]", order);
				orderService.saveOrUpdate(order);
			}
			feedbackService.saveFeedBack(dto);
		} catch (Exception e) {
			logger.error("保存意见反馈错误 >>>[{}]", e);
			return R.error("订单不存在");
		}
		return R.ok();
	}

	@ApiOperation(value = "标记处理反馈单")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public R updateFeedBack(@RequestBody FeedBackDto dto) {
		logger.info("标记处理反馈单>>>[{}]", dto);
		FeedbackEntity entity = new FeedbackEntity();
		BeanUtils.copyProperties(dto, entity);
		entity.setUpdateUser(getUser().getUsername());
		if (dto.getOperation().equalsIgnoreCase("reject")) {
			entity.setStatus(3); // 被驳回
			entity.setUpdateTime(new Date());
			feedbackService.saveOrUpdate(entity);
			//订单状态改为被驳回
			QueryWrapper<OrderEntity> query = new QueryWrapper<OrderEntity>();
			query.eq("order_code", dto.getOrderCode());
			OrderEntity order = orderService.getOne(query);
			order.setPayStatus(3);	//被驳回
			order.setRejectTime(new Date());
			orderService.saveOrUpdate(order);
		} else if (dto.getOperation().equalsIgnoreCase("update")) {
			entity.setStatus(2); // 已处理
			entity.setUpdateTime(new Date());
			feedbackService.saveOrUpdate(entity);
		} else if (dto.getOperation().equalsIgnoreCase("refund")) {
			return orderService.refund(entity, dto.getPhone(), dto.getOrderCode());
		}
		return R.ok();
	}

	@ApiOperation(value = "分页查询反馈单列表")
	@GetMapping("/list")
	@RequiresPermissions("feedback:list")
	public R list(@RequestParam Map<String, Object> params) {
		logger.info("分页查询反馈单列表>>>[{}]", params);
		PageUtils page = feedbackService.queryPage(params);
		return R.ok().put("page", page);
	}
	
	@ApiOperation(value = "分页查询用户反馈单列表")
	@RequestMapping(value = "/queryFeedBack", method = RequestMethod.POST)
	public R appList(@RequestBody Map<String, Object> params) {
		logger.info("分页查询反馈单列表>>>[{}]", params);
		PageUtils page = feedbackService.queryUserFeedBack(params);
		return R.ok().put("page", page);
	}
	
	@ApiOperation(value = "删除意见反馈")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public R delete(@RequestBody Map<String, Object> params) {
		logger.info("删除意见反馈>>>[{}]", params);
		String id = params.get("feedbackId").toString();
		QueryWrapper<FeedbackEntity> query = new QueryWrapper<FeedbackEntity>();
		query.eq("feedback_id", Integer.parseInt(id));
		FeedbackEntity entity = feedbackService.getOne(query);
		if(entity != null) {
			feedbackService.remove(query);
		}
		return R.ok();
	}
	
	@ApiOperation(value = "取消退款申请")
	@RequestMapping(value = "/cancelRefund", method = RequestMethod.POST)
	public R cancelRefund(@RequestBody Map<String, Object> params) {
		logger.info("取消退款申请>>>[{}]", params);
		String orderId = params.get("orderId").toString();
		
		//反馈订单的状态退款中改为已支付
		QueryWrapper<OrderEntity> orderQuery = new QueryWrapper<OrderEntity>();
		orderQuery.eq("order_id", Integer.parseInt(orderId));
		OrderEntity order = orderService.getOne(orderQuery);
		if(order != null) {
			//修改订单状态
			if(order.getPayStatus() != 2) {
				return R.error("只有退款中的订单才可以取消退款");
			}else {
				order.setPayStatus(1);
				orderService.saveOrUpdate(order);
			}
			//如果有未处理的退款反馈单，删除
			QueryWrapper<FeedbackEntity> query = new QueryWrapper<FeedbackEntity>();
			query.eq("order_code", order.getOrderCode());
			query.eq("status", 1);
			FeedbackEntity entity = feedbackService.getOne(query);
			if(entity != null) {
				//删除反馈
				feedbackService.remove(query);
			}
		}else {
			return R.error("订单信息不存在");
		}
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

}
