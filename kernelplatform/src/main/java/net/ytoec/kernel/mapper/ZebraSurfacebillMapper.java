package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.MailNoInfo;
import net.ytoec.kernel.dataobject.UserWaybillInfo;
import net.ytoec.kernel.dataobject.ZebraSurfacebill;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

public interface ZebraSurfacebillMapper<T extends ZebraSurfacebill> extends
		BaseSqlMapper<T> {
	public int batchUpdateStatebyQuantity(Map<String,Object> map);
	
	public List<String> queryWaybillBySequence(Map<String,Object> map);
	
	public List<UserWaybillInfo> countWaybillInfo(List<String> customerCodeList);
	
	public int insertSurfaceBill(MailNoInfo mailNoInfo);
	
	public int batchInsertSurfaceBill(List<MailNoInfo> mailNoList);
	
	public int updateStateBySequence(Map<String,Object> map);
	
	public List<String> querySurfaceBillListByLimit(Map<String,Object> map);

	public int deleteUploadBillByLimit(Map<String,Object> map);
	ZebraSurfacebill getZebraSurfacebillMinnoNoUsed(String userName);

	void edit(ZebraSurfacebill entity);

	Integer insertBill(ZebraSurfacebill entity);

	/**
	 * 根据电子面单号码获取发送次数
	 * 
	 * @param waybillNo
	 * @return
	 */
	int getSendTimes(String waybillNos);

	/**
	 * 查找当前用户使用的最大版本号
	 * 
	 * @param userName
	 * @return
	 */
	String selectVersionNo(String userName);

	Integer selectCounts(String userName);

	List<ZebraSurfacebill> selectZebraSurfacebillsByCustomerCode(
			Map<String, Object> params);

	int updateByPrimaryKeySelective(ZebraSurfacebill zebraSurfacebill);

	int batchUpdateUseState(Map<String, Object> params);

	int batchUpdatePrintState(Map<String, Object> params);
 
	//查询面单总数
	int selectAllCounts(String customerCode);

	int batchInsert(Map<String, Object> params);

	int batchDelete(Map<String, Object> params);

	int selectCountsByCustomerCode(String customerCode);

	List<String> selectByCustomerCodeAndCounts(Map<String, Object> params);

	int generateSequence(Map<String, Object> params);

	int selectSequence();

	String selectSequenceByKey(Map<String, String> params);

	List<String> selectUploadSurfacebillsBySenquence(String sequence);

	int selectUsedCountsByCustomerCode(String customerCode);

	int selectCountsByState(Map<String, Object> params);

	List<String> selectUploadSurceBillByCustomerCodeAndCounts(Map<String, Object> params);

}
