package net.ytoec.kernel.dao;

import java.util.List;

import net.ytoec.kernel.dataobject.Channel;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.dataobject.ZebraPartern;

/**
 * 商家信息Dao
 * 
 * @author John
 * 
 */
public interface SellerInfoDao<T> {
	String findParternBySellerUserCode(String customerCode);

	String findParternBySellerUserName(String sellerUserName);

	String findParternByUserCodeAndShopName(String shopName,
			String userCode);

	List<User> findUserBySellerUserCode(String userCode);

	User findUserBySellerUserName(String sellerUserName);

	User findUserByUserCodeAndShopName(String shopName,
			String userCode);

	List<UserThread> findUserAll(String userName);
	//生成密钥
	void updateByPrimaryKeySelective(Channel zebraPartern);
	
	void insertByPrimaryKeySelective(Channel zebraPartern);
	//生成账号
	void createUser(User user);
	
	void insertUser(User user);
	
	String findSiteCodeByUserCode(String userCode);
	
	int findUserIdByUserCode(String userCode);
	
	String findUserNameByUserCode(String userCode);
	
	String findUserCodeByUserCode(String userCode);
	
	String findEmailByUserName(String userName);
	
	String findSiteCodeByUserCodeFromUT(String userCode);
	
	String findIdByUserName(String UserName);
	
	void updateTaobaoEncodeKey(User user);
	
	String findUserNameByUserCodeFromUser(String userCode);
	
	//查询密钥表
	String findParternByUserCode(String customerCode);
	//向密钥表插入记录
	void insertByCustomerCodeFromPartern(ZebraPartern zebraPartern); 
	//密钥表更新记录
	void updateByCustomerCodeFromPartern(ZebraPartern zebraPartern);
}
