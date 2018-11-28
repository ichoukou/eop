package com.ytoec.cms.bean;

import java.util.Comparator;

public class ColumnComparator implements Comparator<Column> {


	@Override
	public int compare(Column column1, Column column2) {
		// TODO Auto-generated method stub
		boolean condition1 = column1.getRootId() > column2.getRootId();
		boolean condition2 = column1.getRootId() == column2.getRootId() && column1.getLevel() > column2.getLevel();
		boolean condition3 = column1.getRootId() == column2.getRootId() && column1.getLevel() == column2.getLevel() && column1.getSeq() > column2.getSeq();
		
		if(condition1 || condition2 || condition3){
			  return 1;
		}else{
			  return 0;
		} 
	}

}
