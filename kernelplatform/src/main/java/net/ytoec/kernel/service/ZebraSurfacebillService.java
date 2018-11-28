package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.ytoec.kernel.dataobject.UserWaybillInfo;
import net.ytoec.kernel.dataobject.ZebraSequence;
import net.ytoec.kernel.dataobject.ZebraSurfacebill;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public interface ZebraSurfacebillService {
	/**
	 * @作者：罗典
	 * @描述：面单界面excel导出
	 * @时间: 2013-10-23
	 * @参数：customerCode 商家代码,quantity 导出数量,customerName 商家名称
	 * @返回：面单号集合Excel
	 * */
	public HSSFWorkbook exportWaybills(ZebraSequence zebraSequence,String customerName)throws Exception;
	
	/**
	 * @作者：罗典
	 * @描述：统计商家的面单信息
	 * @时间：2013-10-23
	 * @参数：customerCodeList 商家编码集合
	 * @返回: List<UserWaybillInfo> 商家运单统计信息
	 * */
	public List<UserWaybillInfo> countWaybillInfo(List<String> customerCodeList);
	
	/**
	 * @作者：罗典
	 * @时间：2013-09-25
	 * @描述：易通提供金刚系统用来获取金刚面单数据
	 * @参数：http用户请求消息
	 * @返回：响应消息 
	 * */
	public void receiveWaybill(HttpServletRequest request) throws Exception;
	/**
	 * 查找该用户对应的最大版本号
	 * 
	 * @param userName
	 * @return
	 */
	String selectVersionNo(String userName);

	Integer selectCounts(String userName);

	boolean insertBill(ZebraSurfacebill entity);

	/**
	 * 从金刚同步电子面单到易通的定时任务
	 */
	void waybillTimer();

	/**
	 * 根据商家代码查询电子面单
	 * 
	 * @param customerCode
	 *            商家代码
	 * @param counts
	 *            易通需要补给仓配通的电子面单数量
	 * @param state
	 * @return
	 */
	List<ZebraSurfacebill> selectZebraSurfacebillsByCustomerCode(
			String customerCode, int counts, int state);

	/**
	 * 批量更新电子面单的下发状态
	 * 
	 * @param waybillNos
	 * 
	 *            仓配通回传的电子面单号列表
	 * @return
	 */
	int batchUpdateUseState(List<String> waybillNos, int state);

	/**
	 * 批量更新电子面单的打印状态
	 * 
	 * @param waybillNos
	 *            仓配通回传的电子面单号列表
	 * @return
	 */
	int batchUpdatePrintState(List<Map<String, String>> waybillNos);

	/**
	 * 根据使用状态查询未使用面单数
	 */
	int selectCountsByState(String customerCode, int state);

	/**
	 * 查询面单总数
	 * 
	 * @param customerCode
	 *            商家代码
	 * @return
	 */
	public int selectAllCount(String customerCode);

	/**
	 * 批量插入电子面单号到面单回传表
	 * 
	 * @param waybillNos
	 *            仓配通回传的电子面单号列表
	 * @param customerCode
	 *            商家代码
	 */
	int addBatch(List<Map<String, String>> waybillNos, String customerCode);

	/**
	 * 批量删除回传的电子面单
	 * 
	 * @param waybillNos
	 *            仓配通回传的电子面单号列表
	 * @param customerCode
	 *            商家代码
	 * @return
	 */
	int batchDelete(List<String> waybillNos, String customerCode);

	/**
	 * 查询仓配通回传电子面单表，得到需要要补仓配通的电子面单数量
	 * 
	 * @param customerCode
	 *            商家代码
	 * @return
	 */
	int selectCountsByCustomerCode(String customerCode);

	/**
	 * 易通给仓配通的某商家下发多少条电子面单，<br>
	 * 先查询出要相应删除的回传电子面单中某商家的多少条数据
	 * 
	 * @param counts
	 *            删除的数量
	 * @param customerCode
	 *            商家代码
	 * @return
	 */
	List<String> selectByCustomerCodeAndCounts(int counts, String customerCode);

	/**
	 * 生成获取电子面单请求序列
	 * 
	 * @param customerCode
	 *            商家代码
	 * @param parternCode
	 *            商家密钥
	 * @return 新生成的序列
	 */
	int insertSequence(String customerCode, String parternCode);

	/**
	 * 验证请求序列号是否合法
	 * 
	 * @param customerCode
	 *            商家代码
	 * @param sequence
	 *            请求序列号
	 * @return
	 */
	boolean validateSequence(String customerCode, String sequence);

	/**
	 * 通过sequence查询回传面单
	 * 
	 * @param sequence
	 *            请求序列号
	 * @return
	 */
	List<String> selectUploadSurfacebillsBySenquence(String sequence);

	/**
	 * 查询已经下发给商家的面单数
	 * 
	 * @param customerCode
	 * @return
	 */
	int selectUsedCountsByCustomerCode(String customerCode);

	/**
	 * 删除该商家上传到回传表的deleteCounts数据, 并更新预警值
	 * 
	 * @param customerCode
	 * @param deleteCounts
	 * @param customerWarnHistoryValue
	 */
	void removeUploadSurfacebillAndUpdateWarnHistoryValue(String customerCode,
			int deleteCounts, int customerWarnHistoryValue);

	/**
	 * 提供电子面单同步接口
	 * 
	 * @param request
	 */
	void synWaybill(HttpServletRequest request)throws Exception;

}
