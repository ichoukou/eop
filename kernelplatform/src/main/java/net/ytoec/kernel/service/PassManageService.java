package net.ytoec.kernel.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.AttentionMail;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.Questionnaire;
import net.ytoec.kernel.dataobject.ReportIssue;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.WarnUpOper;
import net.ytoec.kernel.dataobject.WarnValue;
import net.ytoec.kernel.dto.DtoQuestion;
import net.ytoec.kernel.dto.PassIssueDTO;
import net.ytoec.kernel.search.dto.EccoreSearchResultDTO;
import net.ytoec.kernel.search.service.EccoreSearchService;

/**
 * @author yangqh
 *
 * @param <T>
 */
public interface PassManageService{

	
	public abstract boolean addReportIssue(ReportIssue ri);

	public abstract boolean addAttentionMail(AttentionMail att);

	public abstract List<ReportIssue> searchReportIssueList(
			Map<String, Object> params, Pagination pagination, boolean b);

	public abstract List<WarnUpOper> getOperByIssueId(Map<String, Object> params);

	public abstract boolean sendToSite(WarnUpOper oper);

	public abstract boolean updateReportIssue(ReportIssue report);

	/**
	 * 上报问题件，若成功则选择添加我的关注
	 * @param queryCondition
	 * @param currentUser
	 * @return
	 * @throws Exception 时间转换异常
	 */
	public abstract boolean reportIssue(String queryCondition, HashMap<String,Object> params,UserService<User> userService) throws Exception;

	
	public abstract boolean sendMsg(WarnUpOper oper,String flag);

	/**
	 * 查询搜索引擎返回数据构造PassIssueDTO对象集合
	 * @param solrEccoreUrl
	 * @param pagination1
	 * @param eccoreSearchService
	 * @param questionnaireService 
	 * @param orderService 
	 * @param userService 
	 * @return
	 */
	public abstract List<PassIssueDTO> searPassIssue(String solrEccoreUrl,
			Pagination<EccoreSearchResultDTO> pagination1,
			List<User> shopNames,Integer userId
			,List<WarnValue> warnValueList,List<String>  bindString)throws Exception;
	
	/**
	 * 查询数量
	 * @param solrEccoreUrl
	 * @param pagination1
	 * @param eccoreSearchService
	 * @param questionnaireService 
	 * @param orderService 
	 * @param userService 
	 * @return
	 */
	public abstract Integer searPassIssueByTotalCount(String solrEccoreUrl,
			Pagination<EccoreSearchResultDTO> pagination1)throws Exception;

}
