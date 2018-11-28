package net.ytoec.kernel.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import net.ytoec.kernel.dataobject.SMSBuyers;
import net.ytoec.kernel.dataobject.TaobaoTask;

public interface SMSBuyersService {
	
	/**
	 * 根据登录帐号获取会员信息
	 * @param buyerAccount
	 * @param userId 主帐号的userid
	 * @return
	 */
	public SMSBuyers getSMSBuyersByBuyerAccount(String buyerAccount, Integer userId);
	
	/**
	 * 更新淘宝号、会员信息进入DB
	 * @throws DataAccessException
	 */
	public void taobaoDataToDB(File taskFiles, TaobaoTask taobaoTask, String tempString)throws Exception;
	
	/**
	 * 根据任务列表获取订单信息的ZIP包
	 * @return
	 * @throws DataAccessException
	 */
	public boolean downZipFromTaobao()throws DataAccessException;
	
	/**
	 * 从淘宝获取订单时创建任务
	 */
	public boolean createTaskByTaobao(Integer userId, String topSession)throws DataAccessException; 
	
  /**
	* 根据查询条件获取会员信息
	* @param params
	*        userIds 			List<Integer> 	(此处如果为子帐号，请传入主帐号的ID)和关联店铺主帐号的Id
	*	     theLastTradeTime   (String类型，上次交易时间)
	*		 theLastMarketTime	(上次营销活动时间)
	*		 userGrade 			(普通会员 ：0， 高级会员 ：1， VIP会员：2，至尊VIP会员 ：３，所有会员 ： 4)
	*		 tradeCountMin2 	(交易量最小值)
	*		 tradeCountMax2 	(交易量最大值)
	*		 tradeAmountMin2 	(交易额最小值)
	*		 tradeAmountMax2 	(交易额最大值)
	*		 receiverProvince 	(省)   code
	*		 receiverCity     	(市)   code
	*		 receiverDistrict  	(区或者县)   code
	*		 receiverName       (联系人)
	*		 receiverMobile     (电话)
	*		 buyerAccount		(会员名)     注：4个byte朝上先查会员名
	*		 orderByCol			(排序字段 ：交易额：tradeAmount, 交易量: tradeCount)
    * @return List<SMSBuyers>
   */
	public List<SMSBuyers> getSMSBuyersByPamams(Map<String, Object> params);
	
	  /**
	* 根据查询条件获取会员数量
	* @param params
	*        userIds 			List<Integer> 	(此处如果为子帐号，请传入主帐号的ID)和关联店铺主帐号的Id
	*	     theLastTradeTime   (String类型，上次交易时间)
	*		 theLastMarketTime	(上次营销活动时间)
	*		 userGrade 			(普通会员 ：0， 高级会员 ：1， VIP会员：2，至尊VIP会员 ：３，所有会员 ： 4)
	*		 tradeCountMin2     (交易量最小值)
	*		 tradeCountMax2 	(交易量最大值)
	*		 tradeAmountMin2 	(交易额最小值)
	*		 tradeAmountMax2 	(交易额最大值)
	*		 receiverProvince 	(省)   code
	*		 receiverCity     	(市)   code
	*		 receiverDistrict  	(区或者县)   code
	*		 receiverName       (联系人)
	*		 receiverMobile     (电话)
	*		 buyerAccount		(会员名)     注：4个byte朝上先查会员名
	*		 orderByCol			(排序字段 ：交易额：tradeAmount, 交易量: tradeCount)
    * @return List<SMSBuyers>
   */
	public Integer getSMSBuyersCountByPamams(Map<String, Object> params);
	
	/**
	 * 根据用户等级获取用户数量
	 * @param params
	 * 		  userIds 		List<Integer> 	(此处如果为子帐号，请传入主帐号的ID)和关联店铺主帐号的Id
	 *  	  userGrade 	(普通会员 ：0， 高级会员 ：1， VIP会员：2，至尊VIP会员 ：３，所有会员 ： 4)
	 * @return
	 */
	public Integer getCountByUserGrade(Map<String, Object> params);
	
	/**
	 * 批量删除会员
	 * @param idLists   需要删除会员的ids
	 * @return
	 */
	public boolean delBatchSMSBuyers(List<Integer> idLists);
	
	/**
	 * 添加信息
	 * @param buyers
	 * @return
	 */
	public boolean addSMSBuyers(SMSBuyers buyers);
	
	/**
	 * 根据ID删除信息
	 * @param id
	 * @return
	 */
	public boolean delSMSBuyersById(Integer id);
	
	/**
	 * 修改
	 * @return
	 */
	public boolean editSMSBuyers(SMSBuyers buyers);
	
	/**
	 * 根据ID查询对象
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public SMSBuyers getSMSBuyersById(Integer id) throws Exception;
	
	/**
	 * 会员批量导入
	 * @param list
	 * @return
	 */
	public boolean addSMSBuyersList(List<SMSBuyers> list);
	/**
	 * 验证旺旺唯一性
	 * @param buyerAccount
	 * @return
	 */
	public boolean checkWW(String buyerAccount,String memberId);
	
}
