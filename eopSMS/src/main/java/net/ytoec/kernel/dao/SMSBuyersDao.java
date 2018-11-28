package net.ytoec.kernel.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import net.ytoec.kernel.dataobject.SMSBuyers;

/**
 * 会员管理dao
 * @author
 *
 * @param <T>
 */
public interface SMSBuyersDao<T> {

	/**
	 * 根据登录帐号获取会员信息
	 * @param buyerAccount
	 * @param userId 卖家的主帐号ID
	 * @return
	 */
	public SMSBuyers getSMSBuyersByBuyerAccount(String buyerAccount, Integer userId);
	  /**
	* 根据查询条件获取会员数量
	* @param params
	*        userIds 			List<Integer> 	(此处如果为子帐号，请传入主帐号的ID)和关联店铺主帐号的Id
	*	     theLastTradeTime   (String类型，上次交易时间)
	*		 theLastMarketTime	(上次营销活动时间)
	*		 userGrade 			(普通会员 ：0， 高级会员 ：1， VIP会员：2，至尊VIP会员 ：３，所有会员 ： 4)
	*		 tradeCountMin 		(交易量最小值)
	*		 tradeCountMax 		(交易量最大值)
	*		 tradeAmountMin 	(交易额最小值)
	*		 tradeAmountMax 	(交易额最大值)
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
	* 根据查询条件获取会员信息
	* @param params
	*        userIds 			List<Integer> 	(此处如果为子帐号，请传入主帐号的ID)和关联店铺主帐号的Id
	*	     theLastTradeTime   (String类型，上次交易时间)
	*		 theLastMarketTime	(上次营销活动时间)
	*		 userGrade 			(普通会员 ：0， 高级会员 ：1， VIP会员：2，至尊VIP会员 ：３，所有会员 ： 4)
	*		 tradeCountMin 		(交易量最小值)
	*		 tradeCountMax 		(交易量最大值)
	*		 tradeAmountMin 	(交易额最小值)
	*		 tradeAmountMax 	(交易额最大值)
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
	 *  从淘宝获取订单下来时需要更新会员的信息
	 * 	1、累加交易额
	 *	2、交易量 + 1
	 *	3、更新上一次的交易时间
	 * @param tradeAmount       (本次订单的交易额)
	 * @param userId 卖家的主帐号id
	 * @return
	 */
	public Boolean  updateInfoByTaobao(Double tradeAmount, Date theLastTradeTime, Integer userId);
	
	/**
	 * 批量删除会员
	 * @param idLists   需要删除会员的ids
	 * @return
	 */
	public boolean delBatchSMSBuyers(List<Integer> idLists);
	
	/**
	 * 添加会员
	 * @param buyers
	 * @return
	 */
	public boolean addSMSBuyers(T buyers);

	/**
	 * 删除会员
	 * @param id
	 * @return
	 */
	public boolean delSMSBuyersById(Integer id);

	/**
	 * 修改会员
	 * @param buyers
	 * @return
	 */
	public boolean editSMSBuyers(T buyers);

	/**
	 * 根据ID查询会员
	 * @param id
	 * @return
	 */
	public Object getSMSBuyersById(Integer id);
	
	/**
	 * 会员批量导入
	 * @param list
	 * @return
	 * @throws DataAccessException
	 */
	public boolean addSMSBuyersList(List<T> list) throws DataAccessException;
	
	/**
	 * 根据条件统计查询此帐号等级数量
	 * @param params
	*        userIds 			List<Integer> 	(此处如果为子帐号，请传入主帐号的ID)和关联店铺主帐号的Id
	*		 tradeCountMin 		(交易量最小值)
	*		 tradeCountMax 		(交易量最大值)
	*		 tradeAmountMin 	(交易额最小值)
	*		 tradeAmountMax 	(交易额最大值)
	 * @return 
	 */
	public Integer getCountByUserGrade(Map<String, Object> params);
	
	/**
	 * 验证旺旺唯一性
	 * @param buyerAccount
	 * @return
	 */
	public SMSBuyers checkWW(String buyerAccount);
	
}
