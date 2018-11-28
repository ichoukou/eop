package net.ytoec.kernel.action.remote;

import net.ytoec.kernel.action.common.BatchQueryRequestProcessor;

/**
 * 
 * 订单查询(批量).
 * 
 */
public class BatchQueryRequest   {
	private String logisticProviderId;
	private String mailNo;

	private BatchQueryResponse batchQueryResponse = new BatchQueryResponse();

	private BatchQueryRequestProcessor batchQueryRequestProcessor = new BatchQueryRequestProcessor();

	public BatchQueryRequest() {

	}

	public BatchQueryRequest(String xmlFragment) {

	}

	public BatchQueryRequest toObject(String xmlFragment) {
		BatchQueryRequest batchQueryRequest = batchQueryRequestProcessor
				.parse(xmlFragment);
		return batchQueryRequest;
	}

	public String getLogisticProviderId() {
		return logisticProviderId;
	}

	public void setLogisticProviderId(String logisticProviderId) {
		this.logisticProviderId = logisticProviderId;
	}

	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	public BatchQueryResponse getBatchQueryResponse() {
		return batchQueryResponse;
	}

	public void setBatchQueryResponse(BatchQueryResponse batchQueryResponse) {
		this.batchQueryResponse = batchQueryResponse;
	}

}
