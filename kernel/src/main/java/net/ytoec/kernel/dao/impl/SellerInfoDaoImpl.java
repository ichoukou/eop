package net.ytoec.kernel.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.dao.SellerInfoDao;
import net.ytoec.kernel.dataobject.Channel;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.dataobject.ZebraPartern;
import net.ytoec.kernel.mapper.SellerInfoMapper;

import org.springframework.stereotype.Repository;

/**
 * 商家账号管理dao实现类
 * 
 * @author John
 * 
 * @param <T>
 */
@Repository
public class SellerInfoDaoImpl<T> implements SellerInfoDao<T> {

	@Inject
	private SellerInfoMapper<T> mapper;

	@Override
	public String findParternBySellerUserCode(String customerCode) {
		String p = mapper.findParternByUserCodeFromChannelInfo(customerCode);
		return p;
	}

	@Override
	public String findParternBySellerUserName(String sellerUserName) {
		String p = mapper.findParternByUserName(sellerUserName);
		return p;
	}

	@Override
	public String findParternByUserCodeAndShopName(String shopName,
			String userCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("shopName", shopName);
		map.put("userCode", userCode);
		String p = mapper.findParternByUserCodeAndShopName(map);
		return p;
	}

	@Override
	public List<User> findUserBySellerUserCode(String userCode) {
		List<User> list=new ArrayList<User>();
		list =mapper.findUserByUserCode(userCode);
		return list;
	}

	@Override
	public User findUserBySellerUserName(String sellerUserName) {
		User user = new User();
		user = (User) mapper.findUserByUserName(sellerUserName);
		return user;
	}

	@Override
	public User findUserByUserCodeAndShopName(String shopName,
			String userCode) {
		User user = new User();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("shopName", shopName);
		map.put("userCode", userCode);
		user = (User) mapper.findUserByUserCodeAndShopName(map);
		return user;
	}

	@Override
	public List<UserThread> findUserAll(String userName) {

		return mapper.findUserAll(userName);
	}

	@Override
	public void updateByPrimaryKeySelective(Channel zebraPartern) {
		mapper.updateByCustomerCode(zebraPartern);
		
	}

	@Override
	public void createUser(User user) {
		mapper.createUser(user);
	}

	@Override
	public void insertByPrimaryKeySelective(Channel zebraPartern) {
		mapper.insertByCustomerCode(zebraPartern);
		
	}

	@Override
	public void insertUser(User user) {
		mapper.insertUser(user);
		
	}

	@Override
	public int findUserIdByUserCode(String userCode) {
		
		return mapper.findUserIdByUserCode(userCode);
	}

	@Override
	public String findUserNameByUserCode(String userCode) {
		
		return mapper.findUserNameByUserCode(userCode);
	}

	@Override
	public String findUserCodeByUserCode(String userCode) {
		return mapper.findUserCodeByUserCode(userCode);
	}

	@Override
	public String findEmailByUserName(String userName) {
		return mapper.findEmailByUserName(userName);
	}

	@Override
	public String findSiteCodeByUserCode(String userCode) {
		
		return mapper.findSiteCodeByUserCode(userCode);
	}

	@Override
	public String findSiteCodeByUserCodeFromUT(String userCode) {
		return mapper.findSiteCodeByUserCodeFromUT(userCode);
	}

	@Override
	public String findIdByUserName(String UserName) {
		return mapper.findIdByUserName(UserName);
	}

	@Override
	public void updateTaobaoEncodeKey(User user) {
		mapper.updateTaobaoEncodeKey(user);
		
	}

	@Override
	public String findUserNameByUserCodeFromUser(String userCode) {
		return mapper.findUserNameByUserCodeFromUser(userCode);
	}

	@Override
	public String findParternByUserCode(String customerCode) {
		return mapper.findParternByUserCode(customerCode);
	}

	@Override
	public void insertByCustomerCodeFromPartern(ZebraPartern zebraPartern) {
		mapper.insertByCustomerCodeFromPartern(zebraPartern);
		
	}

	@Override
	public void updateByCustomerCodeFromPartern(ZebraPartern zebraPartern) {
		mapper.updateByCustomerCodeFromPartern(zebraPartern);
	}

}
