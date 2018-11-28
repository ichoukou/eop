package net.ytoec.kernel.dataobject;
import java.io.Serializable;
import java.util.Date;

/**
 * 买家的搜索器(每个卖家针对每个短信类型，只允许创建5个)
 * @author Administrator
 *
 */
public class SMSBuyersSearch implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1608077773455413295L;
	
	private int id;     
	
	private String searchName;     	//搜索器名字
	
	private String searchCondition; //搜索器内容
	
	private Date updateTime;     	//修改时间
	
	private int updateUserId;   	//修改人
	
	private Date createTime;     	//创建时间
	
	private int createUserId;   	//创建人
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getSearchName() {
		return searchName;
	}
	
	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
	
	public String getSearchCondition() {
		return searchCondition;
	}
	
	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}
	
	public Date getUpdateTime() {
		return updateTime;
	}
	
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public int getUpdateUserId() {
		return updateUserId;
	}
	
	public void setUpdateUserId(int updateUserId) {
		this.updateUserId = updateUserId;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public int getCreateUserId() {
		return createUserId;
	}
	
	public void setCreateUserId(int createUserId) {
		this.createUserId = createUserId;
	}
	
	

}
