package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.UnlikeFreight;


public interface UnlikeFreightService<T> {


	boolean addUnlikeFreight(T unlikeFreight);
	
	boolean editUnlikeFreight(T unlikeFreight);
	
	boolean deleteUnlikeFreight(T unlikeFreight);
	
	public UnlikeFreight getUnlikeFreightById(int id);
	
	public UnlikeFreight getUnlikeFreightByMailNo(String mailNo);

	public List<T> getUnlikeFreightList(Map map, Pagination pagination,
			boolean flag);
	public List<T> getUnlikeFreightListbymailNo(String sr, Pagination pagination,
			boolean flag);
	public UnlikeFreight getUnlikeFreightListbySR(String sr, Pagination pagination,
			boolean flag);
	public UnlikeFreight getUnlikeFreightListbySR(String sr);
}
