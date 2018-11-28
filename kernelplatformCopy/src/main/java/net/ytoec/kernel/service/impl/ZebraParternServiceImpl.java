/**
 * 
 */
package net.ytoec.kernel.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.action.common.XmlSender;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.ZebraParternDao;
import net.ytoec.kernel.dataobject.ZebraPartern;
import net.ytoec.kernel.service.ZebraParternService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ZebraParternServiceImpl<T extends ZebraPartern> implements
		ZebraParternService<T> {

	private static Logger logger = LoggerFactory
			.getLogger(ZebraSurfacebillServiceImpl.class);

	@Inject
	private ZebraParternDao<ZebraPartern> zebraParternDao;

	@Value("${parternSyncJgUrl}")
	private String parternSyncJgUrl;// 密钥同步到金刚地址

	@Value("${parternCode}")
	private String parternCode;// 密钥同步时，统一用一个可以密钥

	/**
	 * 密钥更新
	 */
	@Override
	public void editByCustomerCode(ZebraPartern zebraPartern) {
		ZebraPartern zp = new ZebraPartern();
		zp.setCustomerCode(zebraPartern.getCustomerCode());
		zp.setParternCode(zebraPartern.getParternCode());
		zp.setUpdateTime(zebraPartern.getUpdateTime());
		zebraParternDao.updateByCustomerCode(zebraPartern);

	}

	/**
	 * 添加密钥
	 */
	@Override
	public void insertSelective(ZebraPartern zebraPartern) {
		ZebraPartern zp = new ZebraPartern();
		zp.setCustomerCode(zebraPartern.getCustomerCode());
		zp.setParternCode(zebraPartern.getParternCode());
		zp.setUpdateTime(zebraPartern.getUpdateTime());

		zebraParternDao.insertSelective(zp);

	}

	@Override
	public void zebraParternTimer() {

	}

	/**
	 * 分页显示
	 */
	@Override
	public Pagination<ZebraPartern> findPageList(
			Pagination<ZebraPartern> pagination, Map<String, Object> params) {
		if (pagination != null) {
			pagination.setTotalRecords(zebraParternDao.selectTotal(params));
			params.put("startIndex", pagination.getStartIndex());
			params.put("pageNum", pagination.getPageNum());
			pagination.setRecords(zebraParternDao.selectPageList(params));
		}
		return pagination;
	}

	/**
	 * 删除密钥
	 */
	@Override
	public void deleteByCustomerCode(ZebraPartern zebraPartern) {
		ZebraPartern zp = new ZebraPartern();
		zp.setCustomerCode(zebraPartern.getCustomerCode());
		zebraParternDao.deleteByCustomerCode(zp);
	}

	@Override
	public String selectParternCodeByCustomerCode(String customerCode) {
		return zebraParternDao.selectParternCodeByCustomerCode(customerCode);
	}

	@Override
	public void updateParternByCustomerCode(ZebraPartern zebraPartern) {
		zebraParternDao.updateByPrimaryKeySelective(zebraPartern);

	}

	@Override
	public void parternTimer() {
		try {
			// 循环分页查询
			Map<String, Object> paramMap = new HashMap<String, Object>();
			int totals = zebraParternDao.selectTotal(paramMap);
			int startIndex = 0;
			int pageSize = 1000;
			int pageNum = totals / pageSize != 0 ? (totals % pageSize == 0 ? totals
					/ pageSize
					: totals / pageSize + 1)
					: 1;
			XmlSender xmlSender = new XmlSender();
			String xmlstring = "";
			String params = "";
			List<ZebraPartern> zebraParterns = new ArrayList<ZebraPartern>();
			for (int i = 0; i < pageNum; i++) {
				startIndex = i * pageSize;

				paramMap.put("startIndex", startIndex);
				paramMap.put("pageSize", pageSize);
				zebraParterns = zebraParternDao.selectPageList(paramMap);
				for (ZebraPartern zebraPartern : zebraParterns) {
					xmlstring = createXml(zebraPartern);
					params = "logistics_interface="
							+ encode(xmlstring, XmlSender.UTF8_CHARSET)
							+ "&"
							+ "data_digest="
							+ encode(
									Md5Encryption.MD5Encode(xmlstring
											+ parternCode),
									XmlSender.UTF8_CHARSET);

					xmlSender.setUrlString(parternSyncJgUrl);
					xmlSender.setRequestMethod(XmlSender.POST_REQUEST_METHOD);
					xmlSender.setRequestParams(params);
					xmlSender.send();
				}
				zebraParterns.clear();
			}
		} catch (Exception e) {
			logger.error("密钥上传", e);
		}
	}

	private String encode(String arg, String charset) {
		try {
			return java.net.URLEncoder.encode(arg, charset);
		} catch (UnsupportedEncodingException e) {
			logger.error(LogInfoEnum.PARSE_INVALID.getValue(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 创建发送信息
	 * 
	 * @param waybill
	 * @return
	 */
	private String createXml(ZebraPartern partern) {
		String xml = "<?xml version='1.0' encoding='UTF-8'?>"
				+ "<ParternRequest>" + "<pId>" + partern.getParternId()
				+ "</pId>" + "<customerCode>" + partern.getCustomerCode()
				+ "</customerCode>" + "<parternId>" + partern.getParternCode()
				+ "</parternId>" + "</ParternRequest>";
		return xml;
	}

}
