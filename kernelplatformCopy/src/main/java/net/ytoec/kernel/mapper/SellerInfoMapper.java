package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.Channel;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.dataobject.ZebraPartern;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * 商家信息mapper
 * 
 * @author John
 * @param <T>
 * 
 */
public interface SellerInfoMapper<T> extends BaseSqlMapper<T> {
	String findParternByUserCodeFromChannelInfo(String customerCode);

	String findParternByUserName(String shopName);

	

	List<User> findUserByUserCode(String userCode);

	User findUserByUserName(String shopName);
	
	String findParternByUserCodeAndShopName(Map<String, Object> map);
	
	User findUserByUserCodeAndShopName(Map<String, Object> map);

	// 查询当前网点下商家所有信息
	List<UserThread> findUserAll(String userName);
	//生成密钥
	void updateByCustomerCode(Channel zebraPartern);
	void insertByCustomerCode(Channel zebraPartern);
	//生成账号
	void createUser(User user);
	void insertUser(User user);
	int findUserIdByUserCode(String userCode);
	
	String findSiteCodeByUserCode(String userCode);
	
	String findUserNameByUserCode(String UserCode);
	String findUserCodeByUserCode(String UserCode);
	String findEmailByUserName(String userName);
	String findSiteCodeByUserCodeFromUT(String userCode);
	String findIdByUserName(String userName);
	void updateTaobaoEncodeKey(User user);
	String findUserNameByUserCodeFromUser(String userCode);
	
	//查询密钥表
	String findParternByUserCode(String customerCode);
	//向密钥表插入记录
	void insertByCustomerCodeFromPartern(ZebraPartern zebraPartern); 
	//密钥表更新记录
	void updateByCustomerCodeFromPartern(ZebraPartern zebraPartern);
}
