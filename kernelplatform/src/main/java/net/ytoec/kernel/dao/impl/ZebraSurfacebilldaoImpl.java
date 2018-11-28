package net.ytoec.kernel.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.ZebraSurfacebillDao;
import net.ytoec.kernel.dataobject.MailNoInfo;
import net.ytoec.kernel.dataobject.UserWaybillInfo;
import net.ytoec.kernel.dataobject.ZebraSurfacebill;
import net.ytoec.kernel.mapper.ZebraSurfacebillMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * 电子面单dao实现类
 * 
 * @date 2013-5-24
 * @author huangtianfu
 * 
 * @param <T>
 */
@Repository
public class ZebraSurfacebilldaoImpl<T extends ZebraSurfacebill> implements
		ZebraSurfacebillDao<T> {

	private static Logger logger = LoggerFactory
			.getLogger(ZebraSurfacebilldaoImpl.class);

	@Inject
	private ZebraSurfacebillMapper<ZebraSurfacebill> mapper;

	/**
	 * @作者：罗典
	 * @时间：2013-10-23
	 * @描述：根据商家编码更新一定数量面单数据状态
	 * @参数：customerCode 商家编码,quantity 需下载数量
	 * @返回：影响行数
	 * */
	public int batchUpdateStatebyQuantity(String customerCode,int quantity,String sequence){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("customerCode", customerCode);
		map.put("sequence", sequence);
		map.put("quantity", quantity);
		return mapper.batchUpdateStatebyQuantity(map);
	}
	
	/**
	 * @作者：罗典
	 * @时间：2013-10-23
	 * @描述：统计商家面单数据
	 * @参数：customerCodeList 商家编码集合
	 * @返回: List<UserWaybillInfo> 面单统计信息集合
	 * */
	public List<UserWaybillInfo> countWaybillInfo(List<String> customerCodeList) {
		return mapper.countWaybillInfo(customerCodeList);
	}

	/**
	 * @作者：罗典
	 * @时间：2013-10-23
	 * @描述：根据序列号查询面单集合
	 * @参数：customerCode 商家编码，sequence 序列号
	 * @返回: List<String> 面单信息集合
	 * */
	public List<String> queryWaybillBySequence(String customerCode,
			int sequence) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("customerCode", customerCode);
		map.put("sequence", sequence);
		return mapper.queryWaybillBySequence(map);
	}

	/**
	 * @作者：罗典
	 * @时间：2013-09-26
	 * @描述：保存面单数据
	 * @参数：mailNoInfo：面单信息
	 * @返回: 影响行数
	 * */
	public int insertSurfaceBill(MailNoInfo mailNoInfo) {
		return mapper.insertSurfaceBill(mailNoInfo);
	}

	/**
	 * @作者：罗典
	 * @时间：2013-09-26
	 * @描述：批量保存面单数据
	 * @参数：mailNoList：面单集合
	 * @返回：出错的面单集合
	 * */
	public int batchInsert(List<MailNoInfo> mailNoList) {
		return mapper.batchInsertSurfaceBill(mailNoList);
	}

	/**
	 * @作者：罗典
	 * @时间：2013-08-28
	 * @描述：删除商家一定数量的回单
	 * @参数：customerCode 商家编码, limit 数量
	 * @返回：删除的数量
	 * */
	@Override
	public int deleteUploadBillByLimit(String customerCode, int limit) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("customerCode", customerCode);
		map.put("limit", limit);
		return mapper.deleteUploadBillByLimit(map);
	}

	/**
	 * @作者：罗典
	 * @时间：2013-08-28
	 * @描述：查询商家一定数量的面单
	 * @参数：customerCode 商家编码, limit 数量
	 * @返回：面单号集合
	 * */
	public List<String> querySurfaceBillListByLimit(String customerCode,
			int limit) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("customerCode", customerCode);
		map.put("limit", limit);
		return mapper.querySurfaceBillListByLimit(map);
	}

	/**
	 * @作者：罗典
	 * @时间：2013-08-28
	 * @描述：根据序列号修改面单状态
	 * @参数：sequence 序列，state 状态
	 * @返回：面单号集合
	 * */
	@Override
	public int updateStateBySequence(String sequence, int isUse) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sequence", sequence);
		map.put("isUse", isUse);
		return mapper.updateStateBySequence(map);
	}

	// 所有已经发送的面单信息
	@Override
	public Integer selectCounts(String userName) {
		return mapper.selectCounts(userName);
	}

	@Override
	public Integer insertBill(ZebraSurfacebill entity) {
		int number = 0;
		try {
			number = mapper.insertBill(entity);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return number;
	}

	@Override
	public List<ZebraSurfacebill> selectZebraSurfacebillsByCustomerCode(
			String customerCode, int counts, int state) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerCode", customerCode);
		params.put("counts", counts);
		params.put("state", state);
		return mapper.selectZebraSurfacebillsByCustomerCode(params);
	}

	@Override
	public int updateByPrimaryKeySelective(ZebraSurfacebill zebraSurfacebill) {
		return mapper.updateByPrimaryKeySelective(zebraSurfacebill);
	}

	@Override
	public int batchUpdateUseState(Map<String, Object> params) {
		return mapper.batchUpdateUseState(params);
	}

	@Override
	public String selectVersionNo(String userName) {
		return mapper.selectVersionNo(userName);
	}

	@Override
	public int batchUpdatePrintState(Map<String, Object> params) {
		return mapper.batchUpdatePrintState(params);
	}

	@Override
	// 查询面单数总
	public int selectAllCountsBycustomerCode(String customerCode) {
		return mapper.selectAllCounts(customerCode);
	}

	@Override
	public int batchInsert(Map<String, Object> params) {
		return mapper.batchInsert(params);
	}

	@Override
	public int batchDelete(Map<String, Object> params) {
		return mapper.batchDelete(params);
	}

	@Override
	public int selectCountsByCustomerCode(String customerCode) {
		return mapper.selectCountsByCustomerCode(customerCode);
	}

	@Override
	public List<String> selectByCustomerCodeAndCounts(int counts,
			String customerCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerCode", customerCode);
		params.put("counts", counts);
		return mapper.selectByCustomerCodeAndCounts(params);
	}

	@Override
	public String validateSequence(String customerCode, String sequence) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("customerCode", customerCode);
		params.put("sequence", sequence);
		return mapper.selectSequenceByKey(params);
	}

	@Override
	public List<String> selectUploadSurfacebillsBySenquence(String sequence) {
		return mapper.selectUploadSurfacebillsBySenquence(sequence);
	}

	@Override
	public int selectUsedCountsByCustomerCode(String customerCode) {
		return mapper.selectUsedCountsByCustomerCode(customerCode);
	}

	@Override
	public int selectCountsByState(String customerCode, int state) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerCode", customerCode);
		params.put("state", state);
		return mapper.selectCountsByState(params);
	}

	@Override
	public List<String> selectUploadSurceBillByCustomerCodeAndCounts(
			String customerCode, int counts) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerCode", customerCode);
		params.put("counts", counts);
		return mapper.selectUploadSurceBillByCustomerCodeAndCounts(params);
	}
}
