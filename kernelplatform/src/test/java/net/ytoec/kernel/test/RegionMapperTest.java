package net.ytoec.kernel.test;

import java.util.List;

import net.ytoec.kernel.dataobject.Region;
import net.ytoec.kernel.mapper.RegionMapper;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class RegionMapperTest {
	private static RegionMapper<Region> mapper;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"classpath*:applicationContext-*.xml");
		Thread.sleep(2000);
		mapper = (RegionMapper) ctx.getBean("regionMapper");
	}

	@Test
	public void testAdd() {
//		Region reg = new Region();
//		reg.setRegionName("aaa-ccc");
//		reg.setRegionNumber("340600");
//		reg.setParentId(19);
//		reg.setRemark("sss");
//		mapper.add(reg);
	}
	
//	@Test
//	public void testGet() {
//		List<Region> list = mapper.getAllRegion();
//		for(Region reg : list){
//			System.out.println(mapper.get(reg));
//		}
//	}
//	
//	@Test
//	public void testGetByParentId(){
//		List<Region> tempList = mapper.getAllRegion();
//		int parentId = 0;
//		for(Region reg : tempList){
//			parentId = (parentId < reg.getId())? reg.getId() : parentId;
//		}
//		List<Region> list = mapper.getRegionListByParentId(parentId);
//		System.out.println(list.size());
//	}
//
//	@Test
//	public void testEdit() {
//		List<Region> list = mapper.getAllRegion();
//		for(int i=0; i<list.size(); i++){
//			list.get(i).setRegionName("aaa_"+i);
//			list.get(i).setRegionNumber(i+"_");
//			list.get(i).setRemark("remark_"+i);
//			mapper.edit(list.get(i));
//		}
//	}
	
//	@Test
//	public void testRemove() {
//		List<Region> list = mapper.getAllRegion();
//		for(Region reg : list){
//			mapper.remove(reg);
//		}
//	}

}
