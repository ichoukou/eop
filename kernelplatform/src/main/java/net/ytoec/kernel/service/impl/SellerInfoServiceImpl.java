package net.ytoec.kernel.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.constains.CachePrefixConstant;
import net.ytoec.kernel.dao.SellerInfoDao;
import net.ytoec.kernel.dataobject.Channel;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.dataobject.ZebraPartern;
import net.ytoec.kernel.service.MemcacheService;
import net.ytoec.kernel.service.SellerInfoService;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商家service实现类
 * 
 * @author John
 * 
 */
@Service
@Transactional
public class SellerInfoServiceImpl implements SellerInfoService {
	@Inject
	private SellerInfoDao<User> sellerInfoDao;

	@Inject
	private MemcacheService<Channel> memcacheService;

	@Override
	public String searchParternBysellerUserCode(String sellerUserCode) {

		String s = sellerInfoDao.findParternBySellerUserCode(sellerUserCode);
		return s;
	}

	@Override
	public String searchParternBysellerUserName(String sellerUserName) {
		String s = sellerInfoDao.findParternBySellerUserName(sellerUserName);
		return s;
	}

	@Override
	public String findParternByUserCodeAndShopName(String shopName,
			String userCode) {
		String s = sellerInfoDao.findParternByUserCodeAndShopName(shopName,
				userCode);
		return s;
	}

	@Override
	public List<User> searchUserByUserCode(String sellerUserCode) {
		List list = new ArrayList<User>();
		list = sellerInfoDao.findUserBySellerUserCode(sellerUserCode);
		return list;
	}

	@Override
	public User searchUserByUserName(String sellerUserName) {
		User user = new User();
		user = sellerInfoDao.findUserBySellerUserName(sellerUserName);
		return user;
	}

	@Override
	public User findUserByUserCodeAndShopName(String shopName, String userCode) {
		User user = new User();
		user = sellerInfoDao.findUserByUserCodeAndShopName(shopName, userCode);
		return user;
	}

	@Override
	public List<UserThread> findUserAll(String userName) {
		return sellerInfoDao.findUserAll(userName);
	}

	@Override
	public void updateByPrimaryKeySelective(Channel zebraPartern) {
		sellerInfoDao.updateByPrimaryKeySelective(zebraPartern);
		memcacheService.add(
				CachePrefixConstant.CHANNEL
						+ StringUtils.upperCase(zebraPartern.getClientId()),
				zebraPartern);

	}

	@Override
	public void updateOrInsertUser(User user) {
		sellerInfoDao.createUser(user);
	}

	@Override
	public void inserteByPrimaryKeySelective(Channel zebraPartern) {
		sellerInfoDao.insertByPrimaryKeySelective(zebraPartern);
		memcacheService.add(
				CachePrefixConstant.CHANNEL
						+ StringUtils.upperCase(zebraPartern.getClientId()),
				zebraPartern);

	}

	@Override
	public void insertUser(User user) {
		sellerInfoDao.insertUser(user);

	}

	@Override
	public int findUserIdByUserCode(String userCode) {

		return sellerInfoDao.findUserIdByUserCode(userCode);
	}

	@Override
	public String findUserNameByUserCode(String userCode) {

		return sellerInfoDao.findUserNameByUserCode(userCode);
	}

	@Override
	public String findUserCodeByUserCode(String userCode) {

		return sellerInfoDao.findUserCodeByUserCode(userCode);
	}

	@Override
	public String findEmailByUserName(String userName) {
		return sellerInfoDao.findEmailByUserName(userName);
	}

	@Override
	public String findSiteCodeByUserCode(String userCode) {

		return sellerInfoDao.findSiteCodeByUserCode(userCode);
	}

	@Override
	public String findSiteCodeByUserCodeFromUT(String userCode) {
		return sellerInfoDao.findSiteCodeByUserCodeFromUT(userCode);
	}

	@Override
	public String findIdByUserName(String UserName) {
		return sellerInfoDao.findIdByUserName(UserName);
	}

	@Override
	public void updateTaobaoEncodeKey(User user) {
		sellerInfoDao.updateTaobaoEncodeKey(user);

	}

	@Override
	public String findUserNameByUserCodeFromUser(String userCode) {
		return sellerInfoDao.findUserNameByUserCodeFromUser(userCode);
	}

	@Override
	public String findParternByUserCode(String customerCode) {
		return sellerInfoDao.findParternByUserCode(customerCode);
	}

	@Override
	public void insertByCustomerCodeFromPartern(ZebraPartern zebraPartern) {
		sellerInfoDao.insertByCustomerCodeFromPartern(zebraPartern);

	}

	@Override
	public void updateByCustomerCodeFromPartern(ZebraPartern zebraPartern) {
		sellerInfoDao.updateByCustomerCodeFromPartern(zebraPartern);

	}

}
