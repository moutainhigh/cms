package com.ymkj.cms.biz.facade.master;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.ymkj.base.core.biz.api.message.PageResponse;
import com.ymkj.base.core.biz.common.PageBean;
import com.ymkj.base.core.biz.common.PageParam;
import com.ymkj.base.core.common.excption.CoreErrorCode;
import com.ymkj.base.core.common.utils.BeanKit;
import com.ymkj.cms.biz.api.service.master.IBMSLoanReviewExecuter;
import com.ymkj.cms.biz.api.vo.request.master.ReqBMSLoanReviewVO;
import com.ymkj.cms.biz.api.vo.response.master.ResBMSLoanReviewVO;
import com.ymkj.cms.biz.entity.master.BMSLoanReview;
import com.ymkj.cms.biz.exception.BizException;
import com.ymkj.cms.biz.service.master.IBMSLoanReviewService;
@Service
public class BMSLoanReviewExecuter implements IBMSLoanReviewExecuter {

	@Autowired
	private IBMSLoanReviewService BMSLoanReviewService;

	@Override
	public PageResponse<ResBMSLoanReviewVO> listPage(ReqBMSLoanReviewVO reqBMSLoanReviewVO) {
		PageResponse<ResBMSLoanReviewVO> response = new PageResponse<ResBMSLoanReviewVO>();
		// 参数校验
		if (reqBMSLoanReviewVO.getPageNum() == 0 || reqBMSLoanReviewVO.getPageSize() == 0) {
			throw new BizException(CoreErrorCode.PARAM_ERROR, new Object[] { "pageNum | pageSize" });
		}
		try {
			// 构造请求参数
			/*reqBMSLoanReviewVO.setIsDeleted((long) 0);*/
			PageParam pageParam = new PageParam(reqBMSLoanReviewVO.getPageNum(), reqBMSLoanReviewVO.getPageSize());
		
			Map<String, Object> paramMap = BeanKit.bean2map(reqBMSLoanReviewVO);
			
			
			// 调用业务逻辑,得到list集合
			PageBean<BMSLoanReview> pageBean = BMSLoanReviewService.listPage(pageParam, paramMap);
			// 构造响应参数
			List<ResBMSLoanReviewVO> records = new ArrayList<ResBMSLoanReviewVO>();
			List<BMSLoanReview> Entitys = pageBean.getRecords();
			for (BMSLoanReview Entity : Entitys) {
				ResBMSLoanReviewVO resDemoVO = new ResBMSLoanReviewVO();
				BeanUtils.copyProperties(Entity, resDemoVO);
				records.add(resDemoVO);
			}
			// 忽略 复制的字段
			BeanUtils.copyProperties(pageBean, response, "records");
			response.setRecords(records);

		} catch (Exception e) {
			// 抛出自定义异常
			/*System.out.println(e);*/
			throw new BizException(CoreErrorCode.SYSTEM_ERROR, e);
		}
		return response;
	
	}

}
