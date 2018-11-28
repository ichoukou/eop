package net.ytoec.kernel.dataobject;

import java.util.List;

public class TracesElement {

	private String logisticProviderID;
	private String mailNos;
	private List<Trace> traces;
	public String getLogisticProviderID() {
		return logisticProviderID;
	}
	public void setLogisticProviderID(String logisticProviderID) {
		this.logisticProviderID = logisticProviderID;
	}
	public String getMailNos() {
		return mailNos;
	}
	public void setMailNos(String mailNos) {
		this.mailNos = mailNos;
	}
	public List<Trace> getTraces() {
		return traces;
	}
	public void setTraces(List<Trace> traces) {
		this.traces = traces;
	}
	
}
