package net.ytoec.kernel.service;

import java.util.List;

import net.ytoec.kernel.dataobject.Channel;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.dataobject.ZebraPartern;

/**
 * 商家信息管理
 * 
 * @author John
 * 
 */
public interface SellerInfoService {
	
	// 根据商家编码查密钥
	String searchParternBysellerUserCode(String sellerUserCode);

	// 根据商家名称查密钥
	String searchParternBysellerUserName(String sellerUserName);

	// 根据商家编码和名称查密钥
	String findParternByUserCodeAndShopName(String shopName,
			String userCode);

	// 根据商家编码查用户
	List<User> searchUserByUserCode(String sellerUserCode);

	// 根据商家名称查用户
	User searchUserByUserName(String sellerUserName);

	// 根据商家编码和名称查用户
	User findUserByUserCodeAndShopName(String shopName,
			String userCode);
	//查询当前网点下商家所有信息
	List<UserThread> findUserAll(String userName);
	int findUserIdByUserCode(String userCode);
	
	//生成密钥
	void updateByPrimaryKeySelective(Channel zebraPartern);
	void inserteByPrimaryKeySelective(Channel zebraPartern);
	
	//生成账号
	void updateOrInsertUser(User user);
	void insertUser(User user);
	String findSiteCodeByUserCode(String userCode);
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
