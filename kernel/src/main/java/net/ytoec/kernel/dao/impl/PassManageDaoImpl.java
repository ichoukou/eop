package net.ytoec.kernel.dao.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.PassManageDao;
import net.ytoec.kernel.dataobject.AttentionMail;
import net.ytoec.kernel.dataobject.ReportIssue;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.WarnUpOper;
import net.ytoec.kernel.mapper.PassManageMapper;

@Repository
public class PassManageDaoImpl implements PassManageDao {

	private Logger logger=Logger.getLogger(PassManageDaoImpl.class);
	
	@Inject
	private PassManageMapper mapper;
	
	@Override
	public void addReportIssue(ReportIssue ri) {
		
		logger.info("## add report issue info ##");
		
		try {
			mapper.addReportIssue(ri);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
	}

	@Override
	public void addAttentionMail(AttentionMail att) {
		logger.info("## add attention mail info ##");
		
		try {
			mapper.addAttentionMail(att);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		
	}

	@Override
	public List searchReportIssueList(Map params) {
		List<ReportIssue> list = null;

        try {
            list = (List<ReportIssue>) mapper.getReportIssueList(params);
        } catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
        }
        return list;
	}

	@Override
	public List getOperByIssueId(Map params) {
		List<WarnUpOper> list = null;

        try {
            list = (List<WarnUpOper>) mapper.getOperList(params);
        } catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
        }
        return list;
	}

	@Override
	public boolean sendToSite(WarnUpOper oper) {
		boolean b = false;
		try{
			this.mapper.addWarnUpOper(oper);
			b = true;
		}catch(Exception e){
			 logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			 return b;
		}
		return b;
	}

	@Override
	public boolean editReportIssue(ReportIssue report) {
		boolean b = false;
		try{
			this.mapper.editReportIssue(report);
			b = true;
		}catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			 return b;
		}
		return b;
	}
	
	
}


