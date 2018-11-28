package net.ytoec.kernel.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.dao.SMSInfoDao;
import net.ytoec.kernel.dataobject.FilterResult;
import net.ytoec.kernel.dataobject.FilterRule;
import net.ytoec.kernel.search.service.BranchSearchService;
import net.ytoec.kernel.service.FilterRuleService;
import net.ytoec.kernel.service.SMSInfoService;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

/**
 * 测试
 * 
 * @author ChenRen
 * @date 2011-7-22
 */
@ContextConfiguration("classpath*:applicationContext*.xml")
public class BranchSearchServiceImplTest extends AbstractJUnit38SpringContextTests {


	@Inject
    private BranchSearchService branchSearchService;
	@Inject
	private SMSInfoService smsInfoService;
	@Inject
	private SMSInfoDao smsInfoDao;
	@Inject
	private FilterRuleService filterRuleService;
	
    @Test
    public void testCommitBranchData() throws Exception {

        //branchSearchService.commitBranchData("http://127.0.0.1:8083/solr/branchcore");
    	/*Map params = new HashMap();
    	params.put("buyMobile", "18221959339");
    	params.put("status", "GOT");
    	params.put("mailNo", "9000000001");
    	System.out.println(smsInfoDao.isNotSendByParams(params));
    	*/
    	List<FilterRule> filters = filterRuleService.getFilterRulesByType(FilterRule.TYPE_ISSUEREPORT);
    	for(FilterRule filter:filters) {
    		System.out.println(filter.getWords());
    	}
		FilterResult filter = filterRuleService.filter("共产党",FilterRule.TYPE_ISSUEREPORT);
		System.out.println(filter.isvalid());
	}

}
