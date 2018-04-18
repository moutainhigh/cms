package com.ymkj.cms.web.boss.controller.master;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ymkj.base.core.common.excption.CoreErrorCode;
import com.ymkj.base.core.web.controller.BaseController;
import com.ymkj.base.core.web.result.PageResult;
import com.ymkj.cms.biz.api.vo.request.master.ReqBMSTmParameterVO;
import com.ymkj.cms.biz.api.vo.response.master.ResBMSTmParameterVO;
import com.ymkj.cms.web.boss.common.ResponsePage;
import com.ymkj.cms.web.boss.exception.BusinessException;
import com.ymkj.cms.web.boss.service.master.ITmParameterService;

@Controller
@RequestMapping("tmParameter")
public class TmParameterController extends BaseController {

	@Autowired
	private ITmParameterService tmParameterService;

	@RequestMapping("main")
	public String view() {
		return "master/tmParameter/tmParameterList";
	}

	/**
	 * 返回json类型的处理方法 1. 第一步 参数校验 (第一种方式) 2. 第二步 业务调用 3. 第三步 结果处理
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "listPage")
	@ResponseBody
	public ResponsePage<ResBMSTmParameterVO> listPage(ReqBMSTmParameterVO reqDemoVO) {
		if (reqDemoVO.getPageNum() == 0 || reqDemoVO.getPageSize() == 0) {
			// 数组参数 必须 与 错误枚举消息中的 占位符 个数 一致
			throw new BusinessException(CoreErrorCode.REQUEST_PARAM_ERROR, new Object[] { "pageNum | pageSize" });
		}
		// 必须 设置请求编码
		reqDemoVO.setSysCode("1111111111");
		PageResult<ResBMSTmParameterVO> pageResult = tmParameterService.listPage(reqDemoVO);
		ResponsePage<ResBMSTmParameterVO> pageList = new ResponsePage<ResBMSTmParameterVO>();
		pageList.setTotal(pageResult.getTotalCount());
		pageList.setRows(pageResult.getRecords());
		return pageList;
	}

	@RequestMapping(value = "addTmParameter")
	@ResponseBody
	public Map<String, Object> addTmParameterMethod(ReqBMSTmParameterVO reqDemoVO) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		// 调用新增接口
		boolean addSuccess = tmParameterService.addTmParameter(reqDemoVO);
		hashMap.put("isSuccess", addSuccess ? true : false);
		return hashMap;
	}

	@RequestMapping(value = "deleteTmParameter")
	@ResponseBody
	public Map<String, Object> deleteTmParameterMethod(ReqBMSTmParameterVO reqDemoVO) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		reqDemoVO.setIsDelete((long) 1);
		boolean addSuccess = tmParameterService.updateTmParameter(reqDemoVO);
		hashMap.put("isSuccess", addSuccess ? true : false);
		return hashMap;
	}

	@RequestMapping(value = "updateTmParameterInit")
	@ResponseBody
	public Map<String, Object> updateTmParameterInit(ReqBMSTmParameterVO reqDemoVO) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		// 调用查询接口接口
		ResBMSTmParameterVO vo = tmParameterService.findById(reqDemoVO.getId());
		hashMap.put("info", vo);
		return hashMap;
	}

	@RequestMapping(value = "updateTmParameter")
	@ResponseBody
	public Map<String, Object> updateTmParameter(ReqBMSTmParameterVO reqDemoVO) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		boolean addSuccess = tmParameterService.updateTmParameter(reqDemoVO);
		hashMap.put("isSuccess", addSuccess ? true : false);
		return hashMap;
	}
}
