package net.ytoec.kernel.timer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.dataobject.Questionnaire;
import net.ytoec.kernel.dto.DtoBranch;
import net.ytoec.kernel.service.MailService;
import net.ytoec.kernel.service.QuestionnaireService;
import net.ytoec.kernel.util.ConfigUtilSingle;
import net.ytoec.kernel.util.DateUtil;
import net.ytoec.kernel.util.JDBCUtilSingle;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.QuartzJobBean;


/**
 * 用来处理问题单同步：从金刚数据库内读取数据 同步到核心平台数据库中
 * <tr>
 * RomoteTask 由spring 配置调度的频率
 * <tr>
 * 
 * @author mgl
 */
public class QuestionnaireTimer extends QuartzJobBean {

	private static Logger logger = LoggerFactory.getLogger(QuestionnaireTimer.class);
	private QuestionnaireService<Questionnaire> questionnaireService;

	/*
	 * 2011-12-08/ChenRen private static final String MAIL_TYPE="TYPE"; private
	 * static final String MAIL_NO="MAILNO"; private static final String
	 * DEAL_TIME="TIME"; private static final String PROVINCE="BPROV"; private
	 * static final String CITY="BCITY"; private static final String
	 * COUNTRY="BDISTRICT"; private static final String ADDRESS="BADDRESS";
	 * private static final String BRANCK_ID="CODE"; private static final String
	 * VIP_NAME="SNAME"; private static final String VIP_PHONE="SPHONE"; private
	 * static final String BUY_USERNAME="BNAME"; private static final String
	 * BUY_USERPHONE="BPHONE"; private static final String
	 * FEEDBACK_INFO="STATUS";
	 */

	/**
	 * 对象表名<br>
	 * 在配置文件中配置(kernel.properties)，由spring注入
	 */
	private String tablename;
	/**
	 * timer的启动时间间隔<br>
	 * 在配置文件中配置(kernel.properties)，由spring注入
	 */
	private int period;

	/** 线程启动标识 */
	private static boolean isRunning = false;
	private static String dateFormat = "yyyy-MM-dd HH:mm:ss";

	private MailService<Mail> mailService;
	private String receiver = "yto_yitong1@163.com";
	/** 问题id */
	private static final String ISSUE_ID = "id";
	/** 问题描述 */
	private static final String ISSUE_DESC = "issue_desc";
	/** 问题状态 */
	private static final String ISSUE_STATUS = "status";
	/** 问题类型 */
	private static final String ISSUE_TYPE = "issue_type";
	/** 揽收网点Id */
	private static final String BRANCK_ID = "source_org_code";
	/** 上报人 */
	private static final String ISSUE_CREATE_USER_TEXT = "create_user_name";
	/** 上报时间 */
	private static final String ISSUE_CREATE_TIME = "create_time";
	/** 接收网点 */
	private static final String REC_BRANCK_TEXT = "rec_org_name";
	/** 上报网点编码 */
	private static final String REPORT_BRANCK_CODE = "org_code";
	private static final String MAIL_NO = "waybill_no";
	/** 运单类型 */
	private static final String MAIL_TYPE = "exp_type";
	/** 图片地址 */
	private static final String IMG1 = "IMG1";
	/** 图片地址 */
	private static final String IMG2 = "IMG2";
	/** 图片地址 */
	private static final String IMG3 = "IMG3";
	/** 图片地址 */
	private static final String IMG4 = "IMG4";
	private static final String PHYSICAL_NAME = "PHYSICAL_NAME";
	private static int threadNum;
    private ThreadPoolTaskExecutor addThreadPoolTaskExecutor;
    List<Questionnaire> sublist=null;
    private List<List<Questionnaire>> subQuestionnaires = new ArrayList<List<Questionnaire>>();
	/**
	 * 邮件模版
	 */
	private static String mailContentTemp = "<HTML><HEAD><META HTTP-EQUIV=CONTENT-TYPE CONTENT=\"TEXT/HTML; CHARSET=UTF-8\"><STYLE>.tab-1{padding-left:32px; white-space:pre;}</STYLE><META CONTENT=\"MSHTML 6.00.2900.3492\" NAME=GENERATOR></HEAD>"
			+ "<BODY STYLE=\"BORDER-WIDTH:0; MARGIN:12PX;\"><DIV><DIV STYLE=\"LINE-HEIGHT:1.7;COLOR:#000000;FONT-SIZE:14PX;FONT-FAMILY:ARIAL\">"
			+ "<DIV> ${mailContent}</DIV>"
			+ "<DIV>系统地址：<A HREF=\"http://ec.yto56.net.cn\" TARGET=\"_BLANK\">http://ec.yto56.net.cn</A></DIV>"
			+ "<BR>--------------------------------------------------------------------------------------"
			+ "</BODY></HTML>";

	@Override
	public void executeInternal(JobExecutionContext context)
	throws JobExecutionException {
		Connection con = null;
		PreparedStatement ps = null;
		// PreparedStatement delps = null;
		ResultSet rs = null;
		if(isRunning){
			logger.error("问题件还在更新中");
		}
		if (!isRunning) {
			try {
				isRunning = true;
				tablename = ConfigUtilSingle.getInstance()
						.getQuestionnaireIssue();
				period = ConfigUtilSingle.getInstance()
						.getQuestionnairePeriod();
				if (StringUtils.isEmpty(tablename)) {
					logger.error("问题件表名为空! 请检查配置文件是否配置或spring是否注入成功!");
					isRunning = false;
					return;
				}
				// 获取缓存中上次存的启动开始时间
				String strStarttime = Resource
						.getQsnConfig(Resource.QSN_NEXTSTARTTIME);
				logger.error("strStarttime=="+strStarttime);
				// 开始时间
				Date startDate = DateUtil.valueof(strStarttime, dateFormat);
				Date lastEndDate = startDate;
				lastEndDate = DateUtil.add(lastEndDate, Calendar.SECOND,
						-10000 / 1000);
				strStarttime = DateUtil.format(lastEndDate, dateFormat);
				// 如果发现上次同步时间与当前时间之差大于2倍的period，则提升本次同步跨度的2倍，保证同步的延迟在一个合理的范围
				if (DateUtil.millisecondsInterval(new Date(), startDate) > period * 3) {
					//period = period * 3;
				} else if (DateUtil.millisecondsInterval(new Date(), startDate) < period) {
					return;
				}
				String partition = "PAR_"
						+ DateUtil.format(startDate, "yyyy_MM_dd");

				Calendar cal = Calendar.getInstance();
				cal.setTime(startDate);
				cal.add(Calendar.SECOND, period / 1000); // 毫秒/1000
				// 结束时间
				Date endDate = cal.getTime();

				endDate = DateUtil.verifyDayInterval(startDate, endDate);

				final String strEndtime = DateUtil.format(endDate, dateFormat);

				logger.error("开始执行问题件同步");
				long t1 = System.currentTimeMillis();
				con = JDBCUtilSingle.getInstance().getConnection();
				String sql = "select * from " + tablename + " where modify_time between to_date('"
						+ strStarttime + "', 'yyyy-MM-dd HH24:mi:ss')"
						+ " and to_date('" + strEndtime
						+ "', 'yyyy-MM-dd HH24:mi:ss')";
				logger.error("问题件同步, SQL:" + sql);
				ps = con.prepareStatement(sql);
				// delps =
				// con.prepareStatement("delete from "+tablename+" where waybill_no=?");

				rs = ps.executeQuery();
				long tt1 = System.currentTimeMillis();
				/*logger.error("同步问题件->金刚检索数据耗时:"
						+ (System.currentTimeMillis() - t1));*/
				

				/*if (!rs.next()) {
					// 如果结果集为空，直接更新缓存
					Resource.updateQsnConfig(Resource.QSN_NEXTSTARTTIME,
							strEndtime);
					logger.error("问题件同步, 没有结果集。不更新问题件, 直接更新缓存时间。");
					isRunning = false;
					return;
				}*/
				/**
				 * 每30分钟会重新build一次Resource.autoNotifyUserMap;
				 * lastNotifyUserMapTime记录的是上次更新Resource.autoNotifyUserMap的时间
				 */

				long t2 = System.currentTimeMillis();
				CountDownLatch cdl = new CountDownLatch(threadNum);
				//List<Questionnaire> list=getQuestionnaireList(rs);
				int num = getQuestionnaireList(rs);
				logger.error("同步问题件->"+" 金刚检索数据耗时"+(tt1-t1)+" 检索结果集解析耗时："+(System.currentTimeMillis()-t2)+" 总条数："+num);
				/*if(list==null){
	            	isRunning = false;
	            	return;
	            }*/
				if(num<=0){
					// 如果结果集为空，直接更新缓存
					Resource.updateQsnConfig(Resource.QSN_NEXTSTARTTIME,
							strEndtime);
					logger.error("问题件同步, 没有结果集。不更新问题件, 直接更新缓存时间。");
					isRunning = false;
					return;
				}
				
				/*SynQuestionnaireThread[] threads = new SynQuestionnaireThread[threadNum];
				for (int i = 0; i < threads.length; i++) {
					SynQuestionnaireThread synQuestionnaireThread = new SynQuestionnaireThread(
							subQuestionnaires.get(i), cdl,strEndtime);

					// 设置线程名称
					synQuestionnaireThread.setName("quesub" + i);
					threads[i] = synQuestionnaireThread;
					logger.error("thread领取问题件" + i + ",size:"
							+ subQuestionnaires.get(i).size());

					// 执行线程
					threads[i].start();
				}*/
				for(int i=0;i<threadNum;i++){
					SynQuestionnaireThread synQuestionnaireThread = new SynQuestionnaireThread(
							subQuestionnaires.get(i), cdl,strEndtime);
					// 设置线程名称
					synQuestionnaireThread.setName("quesub" + i);
					
					logger.error("thread领取问题件" + i + ",size:"
							+ subQuestionnaires.get(i).size());
					addThreadPoolTaskExecutor.execute(synQuestionnaireThread);
				}

				// 等待所有子线程执行完毕
				cdl.await(); 
				Resource.updateQsnConfig(Resource.QSN_NEXTSTARTTIME, strEndtime);
				//this.getQuestionnaireFromJIinGang(rs, strEndtime);

				logger.error("同步问题件->易通数据库操作耗时为:"
						+ (System.currentTimeMillis() - t2));
				logger.error("同步问题件->处理此次数据总耗时为:"
						+ (System.currentTimeMillis() - t1));
				logger.error("-------------------问题件同步结束----------------------");
			} catch (Exception e) {
				logger.error("问题件同步出错!", e);
				StackTraceElement ex = e.getStackTrace()[0];
				Mail mail = new Mail();
				mail.setSubject("QuestionnaireTimer出现异常！");
				mail.setSendToMail(this.receiver);
				mail.setContent(mailContentTemp.replace(
						"${mailContent}",
						"Timer监控出现异常！\n异常类型：" + e.getClass() + "\n异常行数："
								+ ex.getLineNumber()));
				mailService.sendMail(mail);
				isRunning = false;
			} finally {
				JDBCUtilSingle.free(rs, ps, con);
				isRunning = false;
			}
		}
	}
	
	private class SynQuestionnaireThread extends Thread {

		private List<Questionnaire> questionnaires;

		private CountDownLatch threadsSignal;
		
		private String strEndtime;

		SynQuestionnaireThread(List<Questionnaire> questionnaires,
				CountDownLatch threadsSignal,String strEndtime) {
			this.questionnaires = questionnaires;
			this.threadsSignal = threadsSignal;
			this.strEndtime = strEndtime;
		}

		@Override
		public void run() {
			try {
				if (questionnaires.size() > 0) {
					for (int i = 0; i < questionnaires.size(); i++) {
						Questionnaire questionnaire = questionnaires.get(i);
						//long t = System.currentTimeMillis();
						questionnaireService.syncQuestionnaire(questionnaire, strEndtime); 
						//logger.error(" 持久化问题件到数据 每条数据耗时(包括自动通知客户)："+(System.currentTimeMillis()-t)+" ms");
					}
				}

			} catch (Exception e) {
				logger.error("SendOrderCreateThread error", e);
			} finally {
				// 线程结束时计数器减1
				threadsSignal.countDown();
			}
		}
	}
	public int getQuestionnaireList(ResultSet rs) throws Exception{
		logger.error("金刚取得的问题件数据开始处理");
		String branchId = "";
		//List<Questionnaire> questionnaireList=new ArrayList<Questionnaire>();
		for (int i = 0; i < threadNum; i++) {
			List<Questionnaire> tempQuestionnaireList = new ArrayList<Questionnaire>();
			subQuestionnaires.add(tempQuestionnaireList);
		}
		int num = 0;
		int mod = 0;
		while (rs.next()) {
			branchId = rs.getString(BRANCK_ID);
			if (StringUtils.isNotBlank(branchId)) {
				try
				{
					//logger.error("处理从金刚取得的问题件数据 "+num);
					long t = System.currentTimeMillis();
					DtoBranch branch = Resource.getDtoBranchByCode(branchId);
					logger.error(" 从金刚获取的每条数据 处理缓存和从数据库中取得 耗时："+(System.currentTimeMillis()-t));
					if (branch == null) {
						logger.error("branchId：" + branchId + ",DtoBranch is null ");
						continue;
					}
					// 网点未激活, 跳过更新
					if (!"1".equals(branch.getStatus())) {
						logger.info("问题件同步. 揽件网点不存在或者账号 状态不是激活状态, 跳过更新!"
								+ " 参数信息[运单号:" + rs.getString(MAIL_NO) + ";"
								+ " 网点编码:" + branchId + ";" + " 网点状态:"
								+ branch.getStatus() + "]");
						continue;
					}

					Questionnaire qsn = new Questionnaire();

					qsn.setBranchId(branchId);
					qsn.setBranckText(branch.getText());
					// 设置问题件处理时间为当前时间，网点在查询的时候按这个字段查询。处理后更新次字段。
					// 上述逻辑修正：余建江说 同步过来的处理时间就写 上报时间 2012-05-10
					qsn.setDealTime(rs.getTimestamp(ISSUE_CREATE_TIME));
					// 设置分区时间
					qsn.setPartitionDate(rs.getDate(ISSUE_CREATE_TIME));
					qsn.setMailType(rs.getString(MAIL_TYPE)); // 运单状态
					qsn.setMailNO(rs.getString(MAIL_NO));
					qsn.setFeedbackInfo(rs.getString(ISSUE_TYPE));
					qsn.setIssueId(rs.getString(ISSUE_ID));
					qsn.setIssueDesc(rs.getString(ISSUE_DESC));
					qsn.setIssueStatus(rs.getString(ISSUE_STATUS));
					qsn.setDealStatus("1"); // 未通知
					qsn.setIssueCreateUserText(rs.getString(ISSUE_CREATE_USER_TEXT));
					qsn.setIssueCreateTime(rs.getTimestamp(ISSUE_CREATE_TIME));
					qsn.setRecBranckText(rs.getString(REC_BRANCK_TEXT));
					qsn.setReportBranckCode(rs.getString(REPORT_BRANCK_CODE)); // 上报网点code
					qsn.setReportBranckText(Resource.getDtoBranchByCode(
							rs.getString(REPORT_BRANCK_CODE)).getText());// 上报网点名称
					//questionnaireList.add(qsn);
					mod = num % threadNum;
					subQuestionnaires.get(mod).add(qsn);
					num++;
				}catch(Exception e){
					logger.error(" 处理从金刚取得的问题件出错：",e);
					throw e;
				}
			}
		}
		return num;
	}
	/*
	public List<Questionnaire> getQuestionnaireList(ResultSet rs) throws Exception{
		logger.error("金刚取得的问题件数据开始处理");
		String branchId = "";
		List<Questionnaire> questionnaireList=new ArrayList<Questionnaire>();
		for (int i = 0; i < threadNum; i++) {
			List<Questionnaire> tempQuestionnaireList = new ArrayList<Questionnaire>();
			subQuestionnaires.add(tempQuestionnaireList);
		}
		int num = 0;
		int mod = 0;
		while (rs.next()) {
			branchId = rs.getString(BRANCK_ID);
			if (StringUtils.isNotBlank(branchId)) {
				try
				{
					//logger.error("处理从金刚取得的问题件数据 "+num);
					long t = System.currentTimeMillis();
					DtoBranch branch = Resource.getDtoBranchByCode(branchId);
					logger.error(" 从金刚获取的每条数据 处理缓存和从数据库中取得 耗时："+(System.currentTimeMillis()-t));
					if (branch == null) {
						logger.error("branchId：" + branchId + ",DtoBranch is null ");
						continue;
					}
					// 网点未激活, 跳过更新
					if (!"1".equals(branch.getStatus())) {
						logger.info("问题件同步. 揽件网点不存在或者账号 状态不是激活状态, 跳过更新!"
								+ " 参数信息[运单号:" + rs.getString(MAIL_NO) + ";"
								+ " 网点编码:" + branchId + ";" + " 网点状态:"
								+ branch.getStatus() + "]");
						continue;
					}

					Questionnaire qsn = new Questionnaire();

					qsn.setBranchId(branchId);
					qsn.setBranckText(branch.getText());
					// 设置问题件处理时间为当前时间，网点在查询的时候按这个字段查询。处理后更新次字段。
					// 上述逻辑修正：余建江说 同步过来的处理时间就写 上报时间 2012-05-10
					qsn.setDealTime(rs.getTimestamp(ISSUE_CREATE_TIME));
					// 设置分区时间
					qsn.setPartitionDate(rs.getDate(ISSUE_CREATE_TIME));
					qsn.setMailType(rs.getString(MAIL_TYPE)); // 运单状态
					qsn.setMailNO(rs.getString(MAIL_NO));
					qsn.setFeedbackInfo(rs.getString(ISSUE_TYPE));
					qsn.setIssueId(rs.getString(ISSUE_ID));
					qsn.setIssueDesc(rs.getString(ISSUE_DESC));
					qsn.setIssueStatus(rs.getString(ISSUE_STATUS));
					qsn.setDealStatus("1"); // 未通知
					qsn.setIssueCreateUserText(rs.getString(ISSUE_CREATE_USER_TEXT));
					qsn.setIssueCreateTime(rs.getTimestamp(ISSUE_CREATE_TIME));
					qsn.setRecBranckText(rs.getString(REC_BRANCK_TEXT));
					qsn.setReportBranckCode(rs.getString(REPORT_BRANCK_CODE)); // 上报网点code
					qsn.setReportBranckText(Resource.getDtoBranchByCode(
							rs.getString(REPORT_BRANCK_CODE)).getText());// 上报网点名称
					//questionnaireList.add(qsn);
					mod = num % threadNum;
					subQuestionnaires.get(mod).add(qsn);
					num++;
				}catch(Exception e){
					logger.error(" 处理从金刚取得的问题件出错：",e);
					throw e;
				}
			}
		}
		return questionnaireList;
	}*/

	/**
	 * 问题件同步 ->问题件持久化
	 * 
	 * @param rs
	 * @param strEndtime
	 * @throws SQLException
	 */
	public void getQuestionnaireFromJIinGang(ResultSet rs, String strEndtime)
			throws SQLException {
		String branchId = "";
		Questionnaire qsn = null;
		while (rs.next()) {
			branchId = rs.getString(BRANCK_ID);
			if (StringUtils.isNotBlank(branchId)) {

				DtoBranch branch = Resource.getDtoBranchByCode(branchId);
				if (branch == null) {
					logger.error("branchId：" + branchId + ",DtoBranch is null ");
					continue;
				}
				// 网点未激活, 跳过更新
				if (!"1".equals(branch.getStatus())) {
					logger.info("问题件同步. 揽件网点不存在或者账号 状态不是激活状态, 跳过更新!"
							+ " 参数信息[运单号:" + rs.getString(MAIL_NO) + ";"
							+ " 网点编码:" + branchId + ";" + " 网点状态:"
							+ branch.getStatus() + "]");
					continue;
				}

				qsn = new Questionnaire();

				qsn.setBranchId(branchId);
				qsn.setBranckText(branch.getText());
				// 设置问题件处理时间为当前时间，网点在查询的时候按这个字段查询。处理后更新次字段。
				// 上述逻辑修正：余建江说 同步过来的处理时间就写 上报时间 2012-05-10
				qsn.setDealTime(rs.getTimestamp(ISSUE_CREATE_TIME));
				// 设置分区时间
				qsn.setPartitionDate(rs.getDate(ISSUE_CREATE_TIME));
				qsn.setMailType(rs.getString(MAIL_TYPE)); // 运单状态
				qsn.setMailNO(rs.getString(MAIL_NO));
				qsn.setFeedbackInfo(rs.getString(ISSUE_TYPE));
				qsn.setIssueId(rs.getString(ISSUE_ID));
				qsn.setIssueDesc(rs.getString(ISSUE_DESC));
				qsn.setIssueStatus(rs.getString(ISSUE_STATUS));
				qsn.setDealStatus("1"); // 未通知
				qsn.setIssueCreateUserText(rs.getString(ISSUE_CREATE_USER_TEXT));
				qsn.setIssueCreateTime(rs.getTimestamp(ISSUE_CREATE_TIME));
				qsn.setRecBranckText(rs.getString(REC_BRANCK_TEXT));
				qsn.setReportBranckCode(rs.getString(REPORT_BRANCK_CODE)); // 上报网点code
				qsn.setReportBranckText(Resource.getDtoBranchByCode(
						rs.getString(REPORT_BRANCK_CODE)).getText());// 上报网点名称

				// 同步问题件图片:需要从金刚库中查找PHYSICAL_NAME
				/*
				 * String tablename =
				 * ConfigUtilSingle.getInstance().getQuestionnaireFile();
				 * if(StringUtils.isNotEmpty(rs.getString(IMG1))){ Connection
				 * con = null; PreparedStatement ps = null; ResultSet result =
				 * null; con = JDBCUtilSingle.getInstance().getConnection();
				 * try{ String sql = "select * from " + tablename +
				 * " where ID="+rs.getString(IMG1); ps =
				 * con.prepareStatement(sql); result = ps.executeQuery();
				 * if(!result.next()){ return; } else{ while(result.next()){
				 * String value = result.getString(PHYSICAL_NAME);
				 * if(value.contains("/yto")) value = value.replace("/yto", "");
				 * qsn.setIMG1(value); break; } } } catch(Exception e){
				 * logger.error("查找问题件图片出错："+e); } finally{
				 * JDBCUtilSingle.free(result, ps, con); } }
				 * if(StringUtils.isNotEmpty(rs.getString(IMG2))){ Connection
				 * con = null; PreparedStatement ps = null; ResultSet result =
				 * null; con = JDBCUtilSingle.getInstance().getConnection();
				 * try{ String sql = "select * from " + tablename +
				 * " where ID="+rs.getString(IMG2); ps =
				 * con.prepareStatement(sql); result = ps.executeQuery();
				 * if(!result.next()){ return; } else{ while(result.next()){
				 * String value = result.getString(PHYSICAL_NAME);
				 * if(value.contains("/yto")) value = value.replace("/yto", "");
				 * qsn.setIMG2(value); break; } } } catch(Exception e){
				 * logger.error("查找问题件图片出错："+e); } finally{
				 * JDBCUtilSingle.free(result, ps, con); } }
				 * if(StringUtils.isNotEmpty(rs.getString(IMG3))){ Connection
				 * con = null; PreparedStatement ps = null; ResultSet result =
				 * null; con = JDBCUtilSingle.getInstance().getConnection();
				 * try{ String sql = "select * from " + tablename +
				 * " where ID="+rs.getString(IMG3); ps =
				 * con.prepareStatement(sql); result = ps.executeQuery();
				 * if(!result.next()){ return; } else{ while(result.next()){
				 * String value = result.getString(PHYSICAL_NAME);
				 * if(value.contains("/yto")) value = value.replace("/yto", "");
				 * qsn.setIMG3(value); break; } } } catch(Exception e){
				 * logger.error("查找问题件图片出错："+e); } finally{
				 * JDBCUtilSingle.free(result, ps, con); } }
				 * if(StringUtils.isNotEmpty(rs.getString(IMG4))){ Connection
				 * con = null; PreparedStatement ps = null; ResultSet result =
				 * null; con = JDBCUtilSingle.getInstance().getConnection();
				 * try{ String sql = "select * from " + tablename +
				 * " where ID="+rs.getString(IMG4); ps =
				 * con.prepareStatement(sql); result = ps.executeQuery();
				 * if(!result.next()){ return; } else{ while(result.next()){
				 * String value = result.getString(PHYSICAL_NAME);
				 * if(value.contains("/yto")) value = value.replace("/yto", "");
				 * qsn.setIMG4(value); break; } } } catch(Exception e){
				 * logger.error("查找问题件图片出错："+e); } finally{
				 * JDBCUtilSingle.free(result, ps, con); } }
				 */

				questionnaireService.syncQuestionnaire(qsn, strEndtime);
			}
		}
		Resource.updateQsnConfig(Resource.QSN_NEXTSTARTTIME, strEndtime);
		logger.error("-------------------问题件同步执行完毕----------------------");

	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public MailService<Mail> getMailService() {
		return mailService;
	}

	public void setMailService(MailService<Mail> mailService) {
		this.mailService = mailService;
	}

	public QuestionnaireService<Questionnaire> getQuestionnaireService() {
		return questionnaireService;
	}

	public void setQuestionnaireService(
			QuestionnaireService<Questionnaire> questionnaireService) {
		this.questionnaireService = questionnaireService;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}
	public ThreadPoolTaskExecutor getAddThreadPoolTaskExecutor() {
		return addThreadPoolTaskExecutor;
	}

	public void setAddThreadPoolTaskExecutor(
			ThreadPoolTaskExecutor addThreadPoolTaskExecutor) {
		this.addThreadPoolTaskExecutor = addThreadPoolTaskExecutor;
	}

	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		QuestionnaireTimer.threadNum = threadNum;
	}
}
