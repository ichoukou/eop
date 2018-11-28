package net.ytoec.kernel.dto;

import java.sql.Date;

/**
 * 关于重量更新的order
 * 
 * @author Administrator
 * 
 */

public class OrderWeightUpdateDTO {

	// order的Id
	private Integer id;
	// 运单号
	private String mailNo;
	
	private String txLogisticId;

	private Float weight;

	private Date partitiondate;

	public OrderWeightUpdateDTO() {
		super();
	}

	public OrderWeightUpdateDTO(Integer id, String mailNo, Float weight, String txLogisticId) {
		super();
		this.id = id;
		this.mailNo = mailNo;
		this.weight = weight;
		this.txLogisticId = txLogisticId;
	}

	public String getTxLogisticId() {
		return txLogisticId;
	}

	public void setTxLogisticId(String txLogisticId) {
		this.txLogisticId = txLogisticId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public Date getPartitiondate() {
		return partitiondate;
	}

	public void setPartitiondate(Date partitiondate) {
		this.partitiondate = partitiondate;
	}

}
