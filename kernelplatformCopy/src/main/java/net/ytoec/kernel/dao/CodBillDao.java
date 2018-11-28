package net.ytoec.kernel.dao;

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
public interface CodBillDao<T> {

	public Integer searchTotalCod(Map<String,Object> params);
	public List<T> searchCodDatils(Map<String,Object> params);
	public List<BillDetailDto> findCodDatils(Map<String,Object> params);
	public Double getCODCount(Map<String,Object> params);
	public String getCODCounts(List<BillDetailDto> list);
	public List<ConfirmLetterDto> getConfirmLetterCods(Map<String,Object> params);
	/*
	 * 根据运单号查询订单
	 */
	public List<BillDetailDto> getBillListByMailNos(String waybillNos);
	public void batchConfirmBillDetails(String wayBillNos, User currentUser);
	/***
	 * 上报差异
	 */
	public boolean saveDiff(TStlConfirmLetterCod confirmLetter);
	
	/***
	 * 防重复上报差异
	 */
	public int repeatSubmit(String waybillNo);
	/***
	 * 修改运单号对应的账单明细
	 */
	public void updateStlBillDetail(String waybillNo);
	
	
	/*
	 * chenfeng
	 */
	public List<T> searchCodBillDatils(Map<String,Object> params);
	
	/*
	 * 查看总公司驳回理由
	 */
	public String getCompanyUndoReason(String waybillNo);
	
	/*
	 * 查看分公司驳回理由
	 */
	public String getSubCompanyUndoReason(String waybillNo);
	
	
}
