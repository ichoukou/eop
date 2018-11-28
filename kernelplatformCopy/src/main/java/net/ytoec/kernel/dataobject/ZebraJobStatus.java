package net.ytoec.kernel.dataobject;

/**
 * Spring Job 状态类
 * 
 * @author Adair
 * @since 2013-03-17
 */
public class ZebraJobStatus {

	/**
	 * job 名称
	 */
	private String jobName;

	/**
	 * job 状态
	 */
	private int status;

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
