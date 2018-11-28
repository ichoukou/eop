package net.ytoec.kernel.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.Channel;
import net.ytoec.kernel.mapper.ChannelMapper;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 
 * @author ChenRen
 * @date 2011-7-19
 */
public class ChannelMapperTest {

	private static ChannelMapper<Channel> mapper;
	/**
	 * 为了让测试不在DB中保留测试数据，且测试的几个方法都是操作的一条数据；
	 * 这里设置一个全局Id保存当前表中最大的Id值(Id自增，一般情况下执行add方法后，再次得到的tempId为当前新增的数据)
	 */
	private static int tempId = -1;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"classpath*:applicationContext-*.xml");
		mapper = (ChannelMapper) ctx.getBean("channelMapper");
	}

	@Test
	public void testAdd() {
		Channel channel = new Channel();
		channel.setKey("yto");
		channel.setValue("圆通速递");
		channel.setIpAddress("www.yto.net.cn");
		mapper.add(channel);
	}

	@Test
	public void testGetChannel() {
		Map map = new HashMap();
		List<Channel> list = mapper.getAllChannel(map);
		for (Channel channel : list) {

			// 取当前表中最大的Id值
			if (tempId < channel.getId()) {
				tempId = channel.getId();
				continue;
			}
		} // for

		Channel channel = new Channel();
		channel.setId(tempId);
		System.out.println(mapper.get(channel));
	}

	@Test
	public void testChannelList() {
		Map map = new HashMap();
		List<Channel> channel = mapper.getAllChannel(map);
		System.out.println(channel.size());
		System.out.println(channel);
	}

	@Test
	public void testEdit() {
		Channel channel = new Channel();
		channel.setId(tempId);
		channel = mapper.get(channel);
		System.out.println(channel);

		channel.setKey("kEdited");
		channel.setValue("vEdited");
		mapper.edit(channel);
		System.out.println(mapper.get(channel));
	}

	@Test
	public void testRemove() {
		Channel channel = new Channel();
		channel.setId(tempId);
		System.out.println(mapper.get(channel));
	}
}
