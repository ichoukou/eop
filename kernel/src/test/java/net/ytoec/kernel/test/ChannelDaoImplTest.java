package net.ytoec.kernel.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.dao.ChannelDao;
import net.ytoec.kernel.dataobject.Channel;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

/**
 * 
 * @author ChenRen
 * @date 2011-7-19
 */
@ContextConfiguration("classpath*:applicationContext-*.xml")
public class ChannelDaoImplTest extends AbstractJUnit38SpringContextTests {

	@Inject
	private ChannelDao<Channel> dao;

	/**
	 * 为了让测试不在DB中保留测试数据，且测试的几个方法都是操作的一条数据；
	 * 这里设置一个全局Id保存当前表中最大的Id值(Id自增，一般情况下执行add方法后，再次得到的tempId为当前新增的数据)
	 */
	private static int tempId = -1;
	
	/*
	 * TODO 测试bug! 
	 * 1. 执行任何一个方法, 执行结果都为true; 
	 * 但是控制台会打印错误信息：java.lang.Exception: DEBUG STACK TRACE for PoolBackedDataSource.close().
	 * 在mapper指定字段的javaType也报错. 
	 * 2. 执行edit()会同时修改DB中的create_time, 直接用脚本在本机客户端执行也会这样
	 */

	@Test
	public void testAdd() {
		Channel channel = new Channel();
		channel.setKey("yto. add.");
		channel.setValue("test. add!");
		channel.setIpAddress("www.yto.net.cn");
		dao.addChannel(channel);
	}

	@Test
	public void testGetChannelById() {
		Map map = new HashMap();
		List<Channel> list = dao.getAllChannel(map);
		for (Channel channel : list) {
			
			// 取当前表中最大的Id值
			if(tempId < channel.getId()) {
				tempId = channel.getId();
				continue;
			}
		} // for
		
		Channel chn = dao.getChannelById(tempId);
		System.out.println(chn);
	}

	@Test
	public void testChannelList() {
		Map map = new HashMap();
		List<Channel> chnList = dao.getAllChannel(map);
		System.out.println(chnList.size());
		System.out.println(chnList);
	}

	@Test
	public void testEditChannel() {
		Channel chn = dao.getChannelById(tempId);
		System.out.println(chn);
		
		if(chn == null) return;

		chn.setKey("yto edit!");
		chn.setValue("test. edit!");
		dao.editChannel(chn);
		System.out.println(dao.getChannelById(tempId));
	}

	@Test
	public void testRemove() {
		Channel channel = dao.getChannelById(tempId);
		if(channel == null) return;
		
		System.out.println(dao.removeChannel(channel));
	}
}
