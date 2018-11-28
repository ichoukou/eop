package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.MailNoInfo;
import net.ytoec.kernel.dataobject.UserWaybillInfo;
import net.ytoec.kernel.dataobject.ZebraSurfacebill;

/**
 * 电子面单dao实现类
 * 
 * @date 2013-5-24
 * @author huangtianfu
 * 
 * @param <T>
 */
public interface ZebraSurfacebillDao<T> {
	/**
	 * @作者：罗典
	 * @时间：2013-10-23
	 * @描述：根据商家编码更新一定数量面单数据状态
	 * @参数：customerCode 商家编码,quantity 需下载数量
	 * @返回：影响行数
	 * */
	public int batchUpdateStatebyQuantity(String customerCode,int quantity,String sequence);
	/**
	 * @作者：罗典
	 * @时间：2013-10-23
	 * @描述：统计商家面单数据
	 * @参数：customerCodeList 商家编码集合
	 * @返回: List<UserWaybillInfo> 面单统计信息集合
	 * */
	public List<UserWaybillInfo> countWaybillInfo(List<String> customerCodeList);
	
	/**
	 * @作者：罗典
	 * @时间：2013-10-23
	 * @描述：根据序列号查询面单集合
	 * @参数：customerCode 商家编码，sequence 序列号
	 * @返回: List<String> 面单信息集合
	 * */
	public List<String> queryWaybillBySequence(String customerCode,int sequence);
	
	/**
	 * @作者：罗典
	 * @时间：2013-08-28
	 * @描述：保存面单数据
	 * @参数：mailNoInfo：面单信息
	 * @返回: 影响行数
	 * */
	public int insertSurfaceBill(MailNoInfo mailNoInfo);
	
	/**
	 * @作者：罗典
	 * @时间：2013-08-28
	 * @描述：批量保存面单数据
	 * @参数：mailNoList：面单集合
	 * @返回：出错的面单集合
	 * */
	public int batchInsert(List<MailNoInfo> mailNoList);
	
	/**
	 * @作者：罗典
	 * @时间：2013-08-28
	 * @描述：删除商家一定数量的回单
	 * @参数：customerCode 商家编码, limit 数量
	 * @返回：删除的数量
	 * */
	public int deleteUploadBillByLimit(String customerCode,int limit);
	
	/**
	 * @作者：罗典
	 * @时间：2013-08-28
	 * @描述：查询商家一定数量的面单
	 * @参数：customerCode 商家编码, limit 数量
	 * @返回：面单号集合
	 * */
	public List<String> querySurfaceBillListByLimit(String customerCode,int limit);
	
	/**
	 * @作者：罗典
	 * @时间：2013-08-28
	 * @描述：根据序列号修改面单状态
	 * @参数：sequence 序列，isUse 状态
	 * @返回：面单号集合
	 * */
	public int updateStateBySequence(String sequence,int isUse);
	
	Integer insertBill(ZebraSurfacebill entity);

	int updateByPrimaryKeySelective(ZebraSurfacebill zebraSurfacebill);

	/**
	 * 查找该用户当前最大版本号
	 * 
	 * @param userName
	 * @return
	 */
	String selectVersionNo(String userName);

	/**
	 * 查找该用户对应的数量
	 * 
	 * @param userName
	 * @return
	 */
	Integer selectCounts(String userName);

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
	 * 批量更新面单使用状态
	 * 
	 * @return
	 */
	int batchUpdateUseState(Map<String, Object> params);

	/**
	 * 批量更新面单打印状态
	 * 
	 * @return
	 */
	int batchUpdatePrintState(Map<String, Object> params);

	/**
	 * 根据商家code查询商家电子面单数量
	 * 
	 * @param customerCode
	 *            商家代码
	 * @param state
	 * @return
	 */
	int selectCountsByState(String customerCode, int state);

	// 查询面单总数
	int selectAllCountsBycustomerCode(String customerCode);

	/**
	 * 批量插入电子面单号到面单回传表
	 * 
	 * @param params
	 * @return
	 */
	int batchInsert(Map<String, Object> params);

	/**
	 * 批量删除电子面单号到面单回传表
	 * 
	 * @param params
	 * @return
	 */
	int batchDelete(Map<String, Object> params);

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
	 * 验证请求序列号是否合法
	 * 
	 * @param customerCode
	 *            商家代码
	 * @param sequence
	 *            请求序列号
	 * @return
	 */
	String validateSequence(String customerCode, String sequence);

	/**
	 * 根据sequence查询回传表
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
	 * 查询要删除该商家上传到回传表counts数据的面单号
	 * 
	 * @param customerCode
	 * @param counts
	 * @return
	 */
	List<String> selectUploadSurceBillByCustomerCodeAndCounts(
			String customerCode, int counts);

}
