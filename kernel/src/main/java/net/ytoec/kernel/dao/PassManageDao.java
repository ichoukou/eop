package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.AttentionMail;
import net.ytoec.kernel.dataobject.ReportIssue;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.WarnUpOper;

public interface PassManageDao<T> {
	
	public abstract void addReportIssue(ReportIssue ri);
	
	public abstract void addAttentionMail(AttentionMail att);

	public abstract List<ReportIssue> searchReportIssueList(
			Map<String, Object> params);

	public abstract List<WarnUpOper> getOperByIssueId(Map<String, Object> params);

	public abstract boolean sendToSite(WarnUpOper oper);

	public abstract boolean editReportIssue(ReportIssue report);

}
