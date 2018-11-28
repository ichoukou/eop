package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.action.bill.BillDetailDto;
import net.ytoec.kernel.action.bill.TStlConfirmLetterCod;
import net.ytoec.kernel.dataobject.ConfirmLetterDto;
import net.ytoec.kernel.dataobject.User;

/**
 * 代收货款
 * @author wmd
 * 2013-03-06
 * @param <T>
 */
public interface CodBillService<T> {

	/**
	 * 查询未确认账单总数
	 * @author chenfeng
	 * @return
	 */
	public Integer getTotalCodBill(Map<String,Object> params);
	/**
	 * 综合查询
	 * @author chenfeng
	 * @return
	 */
	public List<T> getCodBillDtails(Map<String,Object> params);
	/**
	 * 查询未确认账单明细
	 * @author dunjie
	 * @return
	 */
	public List<BillDetailDto> searchCodBillDtails(Map<String,Object> params);
	/**
	 * 查询未确认账单总金额
	 * @author chenfeng
	 * @return
	 */
	public Double getCODCount(Map<String,Object> params);
	/**
	 * 累加未确认账单总金额
	 * @author chenfeng
	 * @return
	 */
	public String getCODCounts(List<BillDetailDto> list);
	/**
	 * w未确认差异
	 * @author dunjie
	 * @param conditions
	 * @return
	 */
	public List<ConfirmLetterDto> getConfirmLetterCods(Map<String, Object> params);
	/**
	 * @author dunjie
	 * 根据运单号构建导出数据[未确认账单明细导出]
	 */
	public String billBuildExportCodBills(String wayBillNos);
	/**
	 * @author dunjie
	 * 根据运单号确认客户账单[未确认账单明细确认账单]
	 */
	public void batchConfirmBillDetails(String wayBillNos,User currentUser);
	/***
	 * 上报差异
	 */
	public boolean saveDiff(TStlConfirmLetterCod confirmLetter);
	/***
	 * 防重复上报差异
	 */
	public int repeatSubmit(String waybillNo);
	/***
	 * 修改该运单号对应的账单明细
	 */
	public void updateStlBillDetail(String waybillNo);
	
	/**
	 * 根据运单号构建导出数据
	 */
	public String buildExportCodBills(String wayBillNos);
	
	/*
	 * 查看总公司驳回理由
	 */
	public String getCompanyUndoReason(String wayBillNo);
	
	/*
	 * 查看总公司驳回理由
	 */
	public String getSubcompanyUndoReason(String wayBillNo);
	
	
	
}
