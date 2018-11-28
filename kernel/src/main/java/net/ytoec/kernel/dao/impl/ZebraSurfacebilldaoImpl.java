package net.ytoec.kernel.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.ZebraSurfacebillDao;
import net.ytoec.kernel.dataobject.ZebraSurfacebill;
import net.ytoec.kernel.mapper.ZebraSurfacebillMapper;

import org.apache.log4j.Logger;
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

	private static Logger logger = Logger
			.getLogger(ZebraSurfacebilldaoImpl.class);

	@Inject
	private ZebraSurfacebillMapper<ZebraSurfacebill> mapper;

	/**
	 * 查询得到当前用户所有的面单信息集合
	 */
	@Override
	public List<ZebraSurfacebill> findZebraSurfacebillsByCurrentUser(
			String userId) {
		List<ZebraSurfacebill> waybillst = null;
		try {
			waybillst = mapper.findZebraSurfacebillsByCurrentUser(userId);
			for (int i = 0; i < waybillst.size(); i++) {
				ZebraSurfacebill waybill = waybillst.get(i);
				waybill.setWaybillNo(new String(Md5Encryption
						.decryptBASE64(waybill.getWaybillNo())));
			}
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return waybillst;
	}

	@Override
	public String selectVersionNo(String userName) {
		return mapper.selectVersionNo(userName);
	}

	// 所有已经发送的面单信息
	@Override
	public Integer selectCounts(String userName) {
		return mapper.selectCounts(userName);
	}

	@Override
	public List<ZebraSurfacebill> findZebraSurfacebillListBySend(int times) {
		return mapper.findZebraSurfacebillListBySend(times);
	}

	@Override
	public ZebraSurfacebill getZebraSurfacebillMinnoNoUsed(String userName) {
		ZebraSurfacebill waybill = null;
		try {
			waybill = mapper.getZebraSurfacebillMinnoNoUsed(userName);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return waybill;
	}

	@Override
	public void edit(ZebraSurfacebill entity) {
		try {
			mapper.edit(entity);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
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
	public int getSendTimes(String waybillNo) {
		int number = 0;
		try {
			number = mapper.getSendTimes(waybillNo);
		} catch (Exception e) {
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
