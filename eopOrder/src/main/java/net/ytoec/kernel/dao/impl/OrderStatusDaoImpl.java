package net.ytoec.kernel.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import net.ytoec.kernel.action.remote.xml.UpdateInfo;
import net.ytoec.kernel.dao.OrderStatusDao;
import net.ytoec.kernel.dataobject.SendTaskToTB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * 批量更新 create by wangmindong create timer 2013-03-25
 * 
 */
@Repository
public class OrderStatusDaoImpl extends SimpleJdbcDaoSupport implements
		OrderStatusDao {

	private static Logger logger = LoggerFactory.getLogger(OrderStatusDaoImpl.class);

	@Resource(name = "jdbcTemplate")
	public void setSuperJdbcTemplate(JdbcTemplate jdbcTemplate) {
		super.setJdbcTemplate(jdbcTemplate);
	}

	@Override
	public boolean batchModifyOrdStatus(List<UpdateInfo> list) {
		boolean flag = false;
		logger.error("==========批量更新order表开始==========");
		long t1 = System.currentTimeMillis();
		try {
			/*
			 * for (int i = 0; i < list.size(); i++) { StringBuffer sql = new
			 * StringBuffer();
			 * sql.append("UPDATE EC_CORE_ORDER o SET  o.update_time=now()"); if
			 * (list.get(i).getRemark() != null &&
			 * !list.get(i).getRemark().equals("")) { sql.append(",o.remark = "
			 * + "'"+list.get(i).getRemark()+"'"); } if (list.get(i).getStatus()
			 * != null && !list.get(i).getStatus().equals("")) {
			 * sql.append(",o.status = " + "'"+list.get(i).getStatus()+"'"); }
			 * if (list.get(i).getAcceptTime() != null &&
			 * !list.get(i).getAcceptTime().equals("")) {
			 * sql.append(",o.accept_time = " +
			 * "'"+list.get(i).getAcceptTime()+"'"); } if (list.get(i).getType()
			 * != null && !list.get(i).getType().equals("")) {
			 * sql.append(",o.type = " + "'"+list.get(i).getType()+"'"); } if
			 * (list.get(i).getMailNo() != null &&
			 * !list.get(i).getMailNo().equals("")) { sql.append(",o.mailNo = "
			 * + "'"+list.get(i).getMailNo()+"'");
			 * 
			 * } sql.append(" where o.tx_logistic_id = " +
			 * "'"+list.get(i).getTxLogisticID()+"'").append(" and ")
			 * .append(" o.client_id = " + "'"+list.get(i).getClientId()+"'");
			 * //BeanPropertySqlParameterSource[] beanSources = list.toArray(new
			 * BeanPropertySqlParameterSource[list.size()]);
			 * //this.getSimpleJdbcTemplate().batchUpdate(sql.toString(),
			 * SqlParameterSourceUtils.createBatch(beanSources));
			 * //this.getSimpleJdbcTemplate().update(sql.toString()); flag =
			 * true; }
			 */

			String sqlBatch = "UPDATE EC_CORE_ORDER o SET  o.update_time=now(),o.remark = :remark,o.status = :status,o.accept_time = :acceptTime,o.type = :type,o.mailNo = :mailNo "
					+ "where o.tx_logistic_id = :txLogisticID ";
			this.getSimpleJdbcTemplate().batchUpdate(sqlBatch,
					SqlParameterSourceUtils.createBatch(list.toArray()));
			flag = true;
			logger.error("此次批量更新order表共" + list.size() + "条数据,用时："
					+ (System.currentTimeMillis() - t1));
		} catch (Exception e) {
			logger.error("批量更新order表订单状态失败...........", e);
		}
		logger.error("==========批量更新order表结束==========");
		return flag;
	}

	/**
	 * 问题件订单状态修改
	 */
	@Override
	public boolean batchUpdateOrderStatusByMailNo(List<UpdateInfo> list) {
		boolean result = false;
		logger.error("==========批量更新ec_core_questionnaire表开始==========");
		long t1 = System.currentTimeMillis();
		try {
			/*
			 * for(int i =0;i<list.size();i++){ String sql =
			 * "update ec_core_questionnaire q SET  q.order_status= " +
			 * ""+"'"+list.get(i).getOrderStatus()+"'"+
			 * " where q.mail_no = "+"'"+list.get(i).getMailNo()+"'";
			 * this.getSimpleJdbcTemplate().update(sql.toString()); }
			 */
			String sql = "update ec_core_questionnaire q SET  q.order_status= :orderStatus where q.mail_no = :mailNo ";
			this.getSimpleJdbcTemplate().batchUpdate(sql,
					SqlParameterSourceUtils.createBatch(list.toArray()));
			result = true;
			logger.error("此次批量更新ec_core_questionnaire表共" + list.size()
					+ "条数据,用时：" + (System.currentTimeMillis() - t1));
		} catch (Exception e) {
			logger.error("批量更新ec_core_questionnaire表订单状态失败...........", e);
		}
		logger.error("==========批量更新ec_core_questionnaire表结束==========");
		return result;
	}

	/**
	 * 批量添加发送淘宝任务记录 ec_core_send_taskToTB
	 */
	@Override
	public boolean batchAddSendTaskToTB(List<SendTaskToTB> list) {
		boolean result = false;
		logger.error("==========批量添加ec_core_send_taskToTB表开始==========");
		long t1 = System.currentTimeMillis();
		try {
			String sql = "insert into  EC_CORE_SEND_TaskToTB (request_url,order_id,last_send_time,client_id,request_params,"
					+ "remark,task_flag_id,task_flag,tx_logistic_id) values (?,?,now(),?,?,?,?,?,?)";

			this.getSimpleJdbcTemplate().batchUpdate(sql.toString(),
					SqlParameterSourceUtils.createBatch(list.toArray()));
			result = true;
		} catch (Exception e) {
			logger.error("批量添加ec_core_send_taskToTB表订单状态失败...........", e);
			e.printStackTrace();
		}
		logger.error("批量添加完成...........");
		long t2 = System.currentTimeMillis();
		long t = t2 - t1;
		logger.info("此次批量添加ec_core_send_taskToTB表共" + list.size() + "条数据,用时："
				+ t);
		logger.error("==========批量添加ec_core_send_taskToTB表结束==========");
		return result;
	}
}
