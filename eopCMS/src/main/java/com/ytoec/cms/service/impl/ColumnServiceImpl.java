package com.ytoec.cms.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;

import org.springframework.stereotype.Service;
import javax.inject.Inject;

import com.ytoec.cms.bean.Column;
import com.ytoec.cms.bean.ColumnComparator;
import com.ytoec.cms.dao.ColumnDao;
import com.ytoec.cms.service.ColumnService;

@Service
public class ColumnServiceImpl<T extends Column> implements ColumnService<T> {
	
	@Inject
	private ColumnDao<T> columnDao;
	
	@Override
	public boolean add(T object){
		// TODO Auto-generated method stub
		return columnDao.add(object);
	}

	@Override
	public T get(T object){
		// TODO Auto-generated method stub
		return columnDao.get(object);
	}

	@Override
	public boolean edit(T object){
		// TODO Auto-generated method stub
		return columnDao.edit(object);
	}

	/**
	*物理删除(数据将从数据库中删除)
	*/
	@Override
	public boolean remove(T object){
		// TODO Auto-generated method stub
		return columnDao.remove(object);
	}

	@Override
	public List<T> getAll(Map<String, Object> param){
		// TODO Auto-generated method stub
		return columnDao.getAll(param);
	}

	@Override
	public Pagination<T> getPageList(Pagination<T> page,Map<String, Object> params){
		// TODO Auto-generated method stub
		return columnDao.getPageList(page,params);
	}

	@Override
	public T retrive(String columnCode){
		// TODO Auto-generated method stub
		return columnDao.retrive(columnCode);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> sortColumn(List<T> columnList){
		// TODO Auto-generated method stub
		if(columnList != null && columnList.size() > 1){
			Map<Integer, List<T>> tempMap = new HashMap<Integer,List<T>>();
			List<T> tempList = new ArrayList<T>();
			 for(T column:columnList){
				List<T> list = new ArrayList<T>();
				if(tempMap.containsKey(column.getRootId())){
					list = tempMap.get(column.getRootId());
				}
				list.add(column);
				tempMap.put(column.getRootId(), list);
			}
			
			for(Integer root_id:tempMap.keySet()){
				List<Column> columns = (List<Column>) tempMap.get(root_id);
				T rootColumn = this.get(root_id);
				for(Column column:columns){
					if(column.getColumnCode().equals(rootColumn.getColumnCode())){
						columns.remove(column);
						break;
					}
				}
				Collections.sort(columns, new ColumnComparator());
				rootColumn.setItems(columns);
				tempList.add(rootColumn);
			}
			Collections.sort(tempList, new ColumnComparator());
			columnList = tempList;
			 
			//Collections.sort(columnList, new ColumnComparator());
		}
		return columnList;
	}

	/**
	 * 获取当前栏目往上的栏目节点树list
	 */
	@Override
	public List<T> getColTopLineTree(Integer columnId){
		// TODO Auto-generated method stub
		List<T> list = new ArrayList<T>();
		T c = get(columnId);
		if(c != null){
			list.add(c);
			if(getColTopLineTree(c.getParentColumnId()).size() > 0){
				list.addAll(getColTopLineTree(c.getParentColumnId()));
			}
		}
		return list;
	}

	@Override
	public boolean delete(Integer columnId){
		// TODO Auto-generated method stub
		return columnDao.delete(columnId);
	}

	@Override
	public List<T> getChildList(Integer parentColId){
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("parentColumnId", parentColId);
		return columnDao.getAll(params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(Integer id) {
		// TODO Auto-generated method stub
		T col = (T) new Column();
		col.setColumnId(id);
		return get(col);
	}
}