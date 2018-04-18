package com.ymkj.cms.biz.api.service.channel;

import java.util.List;

import com.ymkj.base.core.biz.api.message.PageResponse;
import com.ymkj.base.core.biz.api.message.Response;
import com.ymkj.cms.biz.api.vo.request.channel.ReqAppFormVO;
import com.ymkj.cms.biz.api.vo.request.channel.ReqBatchNumVo;
import com.ymkj.cms.biz.api.vo.request.channel.ReqFileBatchNumVo;
import com.ymkj.cms.biz.api.vo.request.channel.ReqdealEsignatureVo;
import com.ymkj.cms.biz.api.vo.response.channel.LoanApplyDetailVo;
import com.ymkj.cms.biz.api.vo.response.channel.ResAppBookVo;
import com.ymkj.cms.biz.api.vo.response.channel.ResAppFormVO;
import com.ymkj.cms.biz.api.vo.response.channel.ResRepaymentExpVo;

/**
 * @author YM10189
 * @date 2017年5月8日
 * @Description:划拨申请书 interface
 */
public interface IAppBookManageExecuter {
	
	/**
	 * 申请书查询
	 * @param reqAppFormVo
	 * @return
	 */
	PageResponse<ResAppFormVO> listPages(ReqAppFormVO reqAppFormVo);
	
	/**
	 * 划拨申请书签章
	 * @param requestVo
	 * @return
	 */
	Response<byte[]> dealEsignature(ReqdealEsignatureVo requestVo);
	
	/**
	 * 划拨申请书导入
	 * @param requestVo
	 * @return
	 */
	Response<String> importAppBook(ReqdealEsignatureVo requestVo);
	
	
	/**
	 * 划拨申请书xls导出
	 * @param requestVo
	 * @return
	 */
	Response<ResAppBookVo> findAppBookPdfXls(ReqBatchNumVo requestVo);
	
	
	/**
	 * 放款申请明细明细查询
	 * @param requestVo
	 * @return
	 */
	Response<List<LoanApplyDetailVo>> findLoanAppBookXls(ReqBatchNumVo requestVo);
	
	/**
	 * 还款计划导出
	 * @param requestVo
	 * @return
	 */
	public Response<List<ResRepaymentExpVo>> exportLoanRepayment(ReqBatchNumVo requestVo);
	
	/**
	 * 文件下载批次号
	 * @param paraMap
	 * @return
	 */
	public Response<String> findRequestFileBatchNum(ReqFileBatchNumVo requestVo);
	
	
	/**
	 * 是否上传过申请书
	 * @param batchNum
	 * @return
	 */
	public Response<Boolean> isUploadApplyBook(ReqAppFormVO req);
	
	/**
	 * 是否为当天第一次下载
	 * @param paraMap
	 * @return
	 */
	public Response<Boolean> isCurrentDayDownloadFirst(ReqAppFormVO req);


}
