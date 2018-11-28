package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.AttentionMail;
import net.ytoec.kernel.dataobject.ReportIssue;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.WarnUpOper;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

public interface PassManageMapper  extends BaseSqlMapper {

	public void addReportIssue(ReportIssue ri);

	public void addAttentionMail(AttentionMail att);

	public User getUserState(String site);

	public List<ReportIssue> getAllUserMap(Map params);

	public List<ReportIssue> getReportIssueList(Map params);

	public List<WarnUpOper> getOperList(Map params);

	public void addWarnUpOper(WarnUpOper oper);

	public void editReportIssue(ReportIssue report);
	
}
