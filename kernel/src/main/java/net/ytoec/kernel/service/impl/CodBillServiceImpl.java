package net.ytoec.kernel.service.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.action.bill.BillDetailDto;
import net.ytoec.kernel.action.bill.TStlConfirmLetterCod;
import net.ytoec.kernel.dao.CodBillDao;
import net.ytoec.kernel.dataobject.ConfirmLetterDto;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.CodBillService;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 代收货款实现类
 * @author wmd
 * 2013-03-06
 * @param <T>
 */
@Service
@Transactional
public class CodBillServiceImpl<T> implements CodBillService<T>{

	private static Logger logger=Logger.getLogger(CodBillServiceImpl.class);
	
	@Inject
	private CodBillDao<T> codBillDao;
	
	@Override
	public Integer getTotalCodBill(Map<String, Object> params) {
		return codBillDao.searchTotalCod(params);
	}

	@Override
	public List<T> getCodBillDtails(Map<String, Object> params) {
		return codBillDao.searchCodDatils(params);
	}
	
	@Override
	public List<BillDetailDto> searchCodBillDtails(Map<String,Object> params){
		return codBillDao.findCodDatils(params);
	}
	@Override
	public List<ConfirmLetterDto> getConfirmLetterCods(Map<String, Object> params) {
		return codBillDao.getConfirmLetterCods(params);
	}

	@Override
	public String buildExportCodBills(String waybillNos) {
		List<BillDetailDto> billList = codBillDao.getBillListByMailNos(waybillNos);
//		返回内容字符串
		StringBuffer sbCsvStr = new StringBuffer();
		String defaultSheet = "运单号, 揽件日期, 发件时间, 签收日期, 代收金额, 发件网点名称, 快件状态, 确认状态, 支付状态, 支付时间, 差异信息, 收款类型 ";
		sbCsvStr.append(defaultSheet);
		for (BillDetailDto billDetailDto : billList) {
			sbCsvStr.append("\r\n").append(billDetailDto.getWaybillNo()).append(",");
			sbCsvStr.append(billDetailDto.getCollectInTime() == null ? "" : billDetailDto.getCollectInTime()).append(",");
			sbCsvStr.append(billDetailDto.getSendTime() == null ? "" : billDetailDto.getSendTime()).append(",");
			sbCsvStr.append(billDetailDto.getSignoffTime() == null ? "" : billDetailDto.getSignoffTime()).append(",");
			sbCsvStr.append(billDetailDto.getCodMoney() == null ? "" : billDetailDto.getCodMoney()).append(",");
			sbCsvStr.append(billDetailDto.getSendOrgName() == null ? "" : billDetailDto.getSendOrgName()).append(",");
//			快件状态 1:已签收 0:未签收
			if(billDetailDto.getSignoffFlg() != null ){
				if(billDetailDto.getSignoffFlg().equals("1")){
					sbCsvStr.append("已签收").append(",");
				}else if(billDetailDto.getSignoffFlg().equals("0")){
					sbCsvStr.append("未签收").append(",");
				}else{
					sbCsvStr.append("").append(",");
				}
			}else{
				sbCsvStr.append("").append(",");
			}
//			确认状态 1：已确认 2：未确认
			if(billDetailDto.getCustomerConfirmFlg() != null ){
				if(billDetailDto.getCustomerConfirmFlg().equals("1")){
					sbCsvStr.append("已确认").append(",");
				}else if(billDetailDto.getCustomerConfirmFlg().equals("0")){
					sbCsvStr.append("未确认").append(",");
				}else{
					sbCsvStr.append("").append(",");
				}
			}else{
				sbCsvStr.append("").append(",");
			}
//			sbCsvStr.append(billDetailDto.getCustomerPayFlg() == null ? "" : (billDetailDto.getCustomerPayFlg() == "1" ? "已支付":"未支付" )).append(",");
//			支付状态 1：已支付 2：未支付
			if(billDetailDto.getCustomerPayFlg() != null ){
				if(billDetailDto.getCustomerPayFlg().equals("1")){
					sbCsvStr.append("已支付").append(",");
				}else if(billDetailDto.getCustomerPayFlg().equals("0")){
					sbCsvStr.append("未支付").append(",");
				}else{
					sbCsvStr.append("").append(",");
				}
			}else{
				sbCsvStr.append("").append(",");
			}
			sbCsvStr.append(billDetailDto.getCustomerPayTime() == null ? "" : billDetailDto.getCustomerPayTime()).append(",");
			if(billDetailDto.getConfirmFlg() == null){
				sbCsvStr.append("").append(",");
			}else if(billDetailDto.getConfirmFlg().equalsIgnoreCase("4")){
				sbCsvStr.append("已驳回").append(",");
			}else if(billDetailDto.getConfirmFlg().equalsIgnoreCase("6")){
				sbCsvStr.append("处理中").append(",");
			}else if(billDetailDto.getConfirmFlg().equalsIgnoreCase("7")){
				sbCsvStr.append("已驳回").append(",");
			}else if(billDetailDto.getConfirmFlg().equalsIgnoreCase("8")){
				sbCsvStr.append("已处理").append(",");
			}else if(billDetailDto.getConfirmFlg().equalsIgnoreCase("3")){
				sbCsvStr.append("已上报").append(",");
			}else{
				sbCsvStr.append("无差异").append(",");
			}
//			sbCsvStr.append(billDetailDto.getConfirmFlg() == null ? "" : billDetailDto.getConfirmFlg()).append(",");
			if(billDetailDto.getChargeType() != null){
				if(billDetailDto.getChargeType().equals("0")){
					sbCsvStr.append("现金").append(",");
				}
				if(billDetailDto.getChargeType().equals("2")){
					sbCsvStr.append("刷卡").append(",");
				}
			}else{
				sbCsvStr.append("").append(",");
			}
		}
		
		return sbCsvStr.toString();
	}
	
	@Override
	public String billBuildExportCodBills(String wayBillNos) {
		List<BillDetailDto> billList = codBillDao.getBillListByMailNos(wayBillNos);
//		返回内容字符串
		StringBuffer sbCsvStr = new StringBuffer();
		String defaultSheet = "运单号码, 揽件日期, 代收金额, 发件网点名称, 快件状态, 差异状态 ";
		sbCsvStr.append(defaultSheet);
		for (BillDetailDto billDetailDto : billList) {
			sbCsvStr.append("\r\n").append(billDetailDto.getWaybillNo()).append(",");
			sbCsvStr.append(billDetailDto.getCollectInTime() == null ? "" : billDetailDto.getCollectInTime()).append(",");
//			sbCsvStr.append(billDetailDto.getSignoffTime() == null ? "" : billDetailDto.getSignoffTime()).append(",");
			sbCsvStr.append(billDetailDto.getCodMoneyReal() == null ? "" : billDetailDto.getCodMoneyReal()).append(",");
			sbCsvStr.append(billDetailDto.getSendOrgName() == null ? "" : billDetailDto.getSendOrgName()).append(",");
			sbCsvStr.append(billDetailDto.getSignoffFlg() == null ? "" :  billDetailDto.getStrWaybillStatus()).append(",");
			if(billDetailDto.getConfirmFlg().equals("")){
				sbCsvStr.append("").append(",");
			}else if(billDetailDto.getConfirmFlg().equals("0") || billDetailDto.getConfirmFlg().equals("1") || billDetailDto.getConfirmFlg().equals("2") || billDetailDto.getConfirmFlg().equals("5")){
				sbCsvStr.append("待确认").append(",");
			}else if(billDetailDto.getConfirmFlg().equals("3")){
				sbCsvStr.append("已上报").append(",");
			}else if(billDetailDto.getConfirmFlg().equals("6")){
				sbCsvStr.append("处理中").append(",");
			}else if(billDetailDto.getConfirmFlg().equals("4") || billDetailDto.getConfirmFlg().equals("7")){
				sbCsvStr.append("被驳回").append(",");
			}else if(billDetailDto.getConfirmFlg().equals("8")){
				sbCsvStr.append("已处理").append(",");
			}
		}
		
		return sbCsvStr.toString();
	}

	@Override
	public Double getCODCount(Map<String, Object> params) {
		return codBillDao.getCODCount(params);
	}

	@Override
	public String getCODCounts(List<BillDetailDto> list) {
		return codBillDao.getCODCounts(list);
	}

	@Override
	public void batchConfirmBillDetails(String wayBillNos, User currentUser) {
		codBillDao.batchConfirmBillDetails(wayBillNos, currentUser);
	}

	@Override
	public boolean saveDiff(TStlConfirmLetterCod confirmLetter) {
		return codBillDao.saveDiff(confirmLetter);
	}

	@Override
	public void updateStlBillDetail(String waybillNo) {
		 codBillDao.updateStlBillDetail(waybillNo);
	}

	/*
	 * 查看总公司驳回理由
	 * (non-Javadoc)
	 * @see net.ytoec.kernel.service.CodBillService#getCompanyUndoReason(java.lang.String)
	 */
	@Override
	public String getCompanyUndoReason(String wayBillNo) {
		// TODO Auto-generated method stub
		return codBillDao.getCompanyUndoReason(wayBillNo);
	}
	
	/*
	 * 查看总公司驳回理由
	 * (non-Javadoc)
	 * @see net.ytoec.kernel.service.CodBillService#getCompanyUndoReason(java.lang.String)
	 */
	@Override
	public String getSubcompanyUndoReason(String wayBillNo) {
		// TODO Auto-generated method stub
		return codBillDao.getSubCompanyUndoReason(wayBillNo);
	}

	@Override
	public int repeatSubmit(String waybillNo) {
		// TODO Auto-generated method stub
		return codBillDao.repeatSubmit(waybillNo);
	}
	
}
