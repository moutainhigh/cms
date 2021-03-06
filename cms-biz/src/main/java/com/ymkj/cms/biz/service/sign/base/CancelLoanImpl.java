package com.ymkj.cms.biz.service.sign.base;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bstek.uflo.model.task.Task;
import com.bstek.uflo.service.TaskOpinion;
import com.ymkj.base.core.biz.api.message.Response;
import com.ymkj.base.core.biz.common.Validate;
import com.ymkj.base.core.common.excption.CoreErrorCode;
import com.ymkj.base.core.common.http.HttpResponse;
import com.ymkj.cms.biz.api.enums.EnumConstants;
import com.ymkj.cms.biz.api.vo.request.sign.ReqLoanContractSignVo;
import com.ymkj.cms.biz.api.vo.response.sign.ResLoanContractSignVo;
import com.ymkj.cms.biz.common.sign.BaseSign;
import com.ymkj.cms.biz.common.util.DateUtil;
import com.ymkj.cms.biz.common.util.JsonUtils;
import com.ymkj.cms.biz.common.util.StringUtils;
import com.ymkj.cms.biz.dao.apply.LoanExtDao;
import com.ymkj.cms.biz.dao.sign.ILoanBaseEntityDao;
import com.ymkj.cms.biz.entity.apply.LoanExtEntity;
import com.ymkj.cms.biz.entity.master.BMSSysLoanLog;
import com.ymkj.cms.biz.entity.sign.BMSLoanBaseEntity;
import com.ymkj.cms.biz.entity.sign.HttpContractReturnEntity;
import com.ymkj.cms.biz.exception.BizErrorCode;
import com.ymkj.cms.biz.exception.BizException;
import com.ymkj.cms.biz.service.http.ICoreHttpService;
import com.ymkj.cms.biz.service.master.IBMSSysLoanLogService;
import com.ymkj.cms.biz.service.master.IBMSSysLogService;
import com.ymkj.cms.biz.service.process.ITaskService;
import com.ymkj.cms.biz.service.sign.ILoanBaseEntityService;

/**
 * 签约环节     取消
 * 
 * @author YM10138
 *
 */
@Service
public class CancelLoanImpl extends BaseSign<ResLoanContractSignVo> {

	@Autowired
	private ILoanBaseEntityService loanBaseEntityService;
	@Autowired
	private ITaskService taskService;
	@Autowired
	private LoanExtDao loanExtDao;
	@Autowired
	private IBMSSysLoanLogService ibmsSysLoanLogService;
	@Autowired
	private IBMSSysLogService ibmsSysLogService;
	@Autowired
	private ILoanBaseEntityDao loanBaseEntityDao;
	@Autowired
	private ICoreHttpService coreHttpService;

	@Override
	public boolean before(ReqLoanContractSignVo reqLoanContractSignVo, Response<ResLoanContractSignVo> res) {
		// 参数校验
		@SuppressWarnings("unchecked")
		Response<Object> validateResponse = Validate.getInstance().validate(reqLoanContractSignVo);
		if (!validateResponse.isSuccess()) {
			setError(new BizException(CoreErrorCode.REQUEST_PARAM_ISBLANK,validateResponse.getRepMsg()), res);
			return false;
		}
		if (StringUtils.isBlank(reqLoanContractSignVo.getLoanNo())) {
			setError(new BizException(CoreErrorCode.REQUEST_PARAM_ISNULL,new Object[] { "loanNo" }), res);
			return false;
		}
		if (reqLoanContractSignVo.getId() == null) {
			setError(new BizException(CoreErrorCode.REQUEST_PARAM_ISNULL,new Object[] { "id" }), res);
			return false;
		}
		if (StringUtils.isBlank(reqLoanContractSignVo.getFirstLevleReasons())) {
			setError(new BizException(CoreErrorCode.REQUEST_PARAM_ISNULL,new Object[] { "firstLevleReasons" }), res);
			return false;
		}
		if (StringUtils.isBlank(reqLoanContractSignVo.getFirstLevleReasonsCode())) {
			setError(new BizException(CoreErrorCode.REQUEST_PARAM_ISNULL,new Object[] { "firstLevleReasonsCode" }), res);
			return false;
		}
		if ("0".equals(reqLoanContractSignVo.getTwoLevleReasons())) {
			reqLoanContractSignVo.setTwoLevleReasons(null);
		}
		if ("0".equals(reqLoanContractSignVo.getTwoLevleReasonsCode())) {
			reqLoanContractSignVo.setTwoLevleReasonsCode(null);
		}
		BMSLoanBaseEntity bmsLoanBaseEntity1 = loanBaseEntityService.findByloanBaseId(String.valueOf(reqLoanContractSignVo.getId()));
		// 推送过银行id 不为空 赋给请求vo做为操作是否做核心操作判断
		if (!StringUtils.isBlank(bmsLoanBaseEntity1.getApplyBankNameId())) {
			reqLoanContractSignVo.setApplyBankNameId(Integer .valueOf(bmsLoanBaseEntity1.getApplyBankNameId()));
		}
		// 业务校验
		if (bmsLoanBaseEntity1.getSignCode() != null) {
			// 当为系统操作时不校验
			if (EnumConstants.BMS_SYSTEM_CODE.equals(reqLoanContractSignVo.getServiceCode())) {

			} else if (!reqLoanContractSignVo.getServiceCode().equals(bmsLoanBaseEntity1.getSignCode())) {
				setError(new BizException(BizErrorCode.OPRATEUSER_EOERR,
						"当前签约客服异常，请刷新"),res);
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean execute(ReqLoanContractSignVo reqLoanContractSignVo, Response<ResLoanContractSignVo> res) {
		
		try {
			//更新借款系统借款状态
			BMSLoanBaseEntity  loanBaseEntity = new BMSLoanBaseEntity(reqLoanContractSignVo.getId(),
					EnumConstants.RtfState.HTQY.getValue(),EnumConstants.RtfNodeState.HTQYCANCEL.getValue(),reqLoanContractSignVo.getVersion());
			loanBaseEntity.setSignEndDate(DateUtil.defaultFormatDate(new Date()));
			loanBaseEntity.setStatus(EnumConstants.LoanStatus.CANCEL.getValue());
			//签约超时取消判断
			if(EnumConstants.cancelFirstLevleReasonCode.超签约时效自动取消.getValue().equals(reqLoanContractSignVo.getFirstLevleReasons())){
				loanBaseEntity.setRtfNodeState(EnumConstants.RtfNodeState.QYCSCANCEL.getValue());
			} else {
				loanBaseEntity.setRtfNodeState(EnumConstants.RtfNodeState.HTQYCANCEL.getValue());
			}
			long count=  loanBaseEntityDao.update(loanBaseEntity);//签约结束时间
			if(count !=1){
				throw new BizException(BizErrorCode.DB_UPDATE_RESULT_0, "更新失败，请刷新页面");
			}
			//调用接口更新合同
			if(reqLoanContractSignVo.getApplyBankNameId() != null){//推送成功调用
				reqLoanContractSignVo.setState(EnumConstants.CONTRACT_CANCEL);
				boolean flag = updateCoreLoanState(reqLoanContractSignVo, res);
				if(!flag){
					return false;
				}
			}
			//记一二级原因
			LoanExtEntity loanExtEntity = new LoanExtEntity();
			loanExtEntity.setLoanBaseId(reqLoanContractSignVo.getId());
			loanExtEntity.setPrimaryReason(reqLoanContractSignVo.getFirstLevleReasons());
			loanExtEntity.setSecodeReason(reqLoanContractSignVo.getTwoLevleReasons());
			loanExtEntity.setFirstLevleReasonsCode(reqLoanContractSignVo.getFirstLevleReasonsCode());
			loanExtEntity.setTwoLevleReasonsCode(reqLoanContractSignVo.getTwoLevleReasonsCode());
			loanExtEntity.setModifier(reqLoanContractSignVo.getServiceCode());
			loanExtEntity.setModifiedTime(new Date());
			loanExtDao.update(loanExtEntity);
			//借款日志
			BMSSysLoanLog loanLog= new BMSSysLoanLog();
			loanLog.setOperationModule(EnumConstants.OptionModule.CONTRACT_SIGN.getValue());
			loanLog.setLoanNo(reqLoanContractSignVo.getLoanNo());
			loanLog.setLoanBaseId(reqLoanContractSignVo.getId());
			loanLog.setOperationTime(new Date());
			loanLog.setOperator(reqLoanContractSignVo.getServiceName());
			loanLog.setOperatorCode(reqLoanContractSignVo.getServiceCode());
			loanLog.setStatus(EnumConstants.LoanStatus.CANCEL.getValue());
			loanLog.setRtfState(EnumConstants.RtfState.HTQY.getValue());
			//签约超时取消判断
			if(EnumConstants.cancelFirstLevleReasonCode.超签约时效自动取消.getValue().equals(reqLoanContractSignVo.getFirstLevleReasons())){
				loanLog.setRtfNodeState(EnumConstants.RtfNodeState.QYCSCANCEL.getValue());
			} else {
				loanLog.setRtfNodeState(EnumConstants.RtfNodeState.HTQYCANCEL.getValue());
			}
			loanLog.setOperationType(EnumConstants.OptionType.CANCEL.getValue());
			loanLog.setFirstLevleReasons(reqLoanContractSignVo.getFirstLevleReasons());
			loanLog.setTwoLevleReasons(reqLoanContractSignVo.getTwoLevleReasons());
			loanLog.setFirstLevleReasonsCode(reqLoanContractSignVo.getFirstLevleReasonsCode());
			loanLog.setTwoLevleReasonsCode(reqLoanContractSignVo.getTwoLevleReasonsCode());
			loanLog.setRemark(reqLoanContractSignVo.getRemark());
			ibmsSysLoanLogService.saveOrUpdate(loanLog);
			//系统日志
			ibmsSysLogService.recordSysLog(reqLoanContractSignVo, "合同签约|合同签约|取消|cancel","ILoanContractSignExecuter","cancelLoan");
			
		} catch (BizException e) {
			throw new BizException(CoreErrorCode.UNKNOWN_ERROR, e.getMessage() + e.getArguments()[0]);
		}
		
		return true;
	}

	@Override
	public boolean after(ReqLoanContractSignVo reqLoanContractSignVo, Response<ResLoanContractSignVo> res) {
		Task task = taskService.findTaskByLoanBaseId(String .valueOf(reqLoanContractSignVo.getId()));

		if (task != null && !EnumConstants.WF_KS.equals(task.getTaskName())) {// 开启流程后调用
			// 完成当前任务
			try {
				TaskOpinion option = new TaskOpinion("取消");
				taskService.completeTaskByLoanBaseId( reqLoanContractSignVo.getId(), EnumConstants.WFPH_JJQX, option);
			} catch (Exception e) {
				setError(new BizException(BizErrorCode.UFLOWORKFLOW_EOERR, e.getMessage()), res);
				return false;
			}
		}
		return true;
	}
	
	//更新核心系统借款状态
	private boolean  updateCoreLoanState(ReqLoanContractSignVo reqLoanContractSignVo, Response<ResLoanContractSignVo> res){
		Map<String,Object> loanDataMap  = new HashMap<String,Object>();
		
		//构造 LoanNo 和 借款状态useCode 和 state
		BMSLoanBaseEntity bmsLoanBaseEntity = new BMSLoanBaseEntity(reqLoanContractSignVo.getLoanNo(),
				reqLoanContractSignVo.getServiceCode(),reqLoanContractSignVo.getState());
		
		loanDataMap.put("loanNo", bmsLoanBaseEntity.getLoanNo());
		loanDataMap.put("userCode",bmsLoanBaseEntity.getUserCode());
		loanDataMap.put("stateCode",bmsLoanBaseEntity.getState());
		//调用核心更新借款状态
		HttpResponse httpResponse = coreHttpService.updateCoreLoanState(loanDataMap);
		
		HttpContractReturnEntity contractReturnEntity= JsonUtils.decode(httpResponse.getContent(), HttpContractReturnEntity.class);
		if(!"000000".equals(contractReturnEntity.getCode())){//是否请求成功
			setError(new BizException(BizErrorCode.FACADE_RESPONSE_FAIL, "接口调用返回失败消息"), res);
			return false;
		}else{
			return true;
		}
		
	}

}
