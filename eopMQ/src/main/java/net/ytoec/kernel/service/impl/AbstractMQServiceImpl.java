package net.ytoec.kernel.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import net.ytoec.kernel.common.Constants;
import net.ytoec.kernel.service.MQService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.MQSimpleConnectionManager;
import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.headers.MQHeaderIterator;

public abstract class AbstractMQServiceImpl implements MQService {
	private static Logger log = LoggerFactory
			.getLogger(AbstractMQServiceImpl.class);
	//private static int DEFAULT_WAITINTERVAL = 10 * 60 * 1000; // 取消息默认最长时间（10分钟）
	private static int DEFAULT_WAITINTERVAL = 30*1000;//取消息时间修改成30秒
	@Autowired
	private MQSimpleConnectionManager mqSimpleConnectionManager;// 连接池管理对象

	private String qmName;// 队列管理器名称
	private Hashtable<String, Object> qmParams;// 连接参数

	private String onlineSendQueueName;
	private String offlineSendQueueName;
	private String jingangSendQueueName;
	private String onlineReceiveQueueName;
	private String offlineReceiveQueueName;
	private String jingangReceiveQueueName;
	private int maxBackoutCount = 5;
	private int deadMaxBackoutCount = 10;
	private String onlineBackoutQueueName;
	private String offlineBackoutQueueName;
	private String jingangBackoutQueueName;
	private String deadQueueName;
	private String jingangDeadQueueName;
	
	
	//4Solr
	private String solrReceiveQueueName;
	private String solrSendQueueName;
	// solr订单创建
	private String solrAddReceiveQueueName;
	private String solrAddSendQueueName;
	private String solrOrderBackoutQueueName;

	private int waitInterval = DEFAULT_WAITINTERVAL;// 读取消息最大等待时间

	private int sendOpenOptions = MQConstants.MQOO_OUTPUT
			| MQConstants.MQOO_FAIL_IF_QUIESCING;// 发送消息打开的选项
	private int receiveOpenOptions = MQConstants.MQOO_INPUT_SHARED
			| MQConstants.MQOO_FAIL_IF_QUIESCING;// 接受消息打开的选项
	
	private int mqlimit;// 一次从mq取的条数
	/**
	 * 得到一个QM实例，由连接池管理的
	 * 
	 * @return
	 * @throws MQException
	 */
	private MQQueueManager getQM() throws MQException {
		return new MQQueueManager(qmName, qmParams, mqSimpleConnectionManager);
	}

	/**
	 * 关闭队列
	 * 
	 * @param queue
	 */
	private void closeQueue(MQQueue queue) {
		if (null != queue) {
			try {
				queue.close();
			} catch (MQException e) {
				try {
					log.error("close queue error!queue name:" + queue.getName());
				} catch (MQException e1) {
					log.error("get queue name error!");
				}
			}
		}
	}

	/**
	 * 断开QM连接。如果是连接池中获取的，则放回到池中
	 * 
	 * @param qm
	 */
	private void disconnectQM(MQQueueManager qm) {
		if (null != qm) {
			try {
				qm.disconnect();
			} catch (MQException e) {
				try {
					log.error("disconnect qm error!qm name:" + qm.getName());
				} catch (MQException e1) {
					log.error("get qm name error!", e1);
				}
			}
		}
	}

	@Override
	public boolean send(String msg, String objType) throws Exception {
		boolean flg = true;
		MQQueueManager qm = null;

		try {
			qm = getQM();
			MQQueue queue = qm.accessQueue(judgeObjectType(objType),
					sendOpenOptions);
			MQPutMessageOptions pmo = new MQPutMessageOptions();

			MQMessage outMsg = constructMsg(msg);
			
			// 队列优先级设置
			outMsg.priority = judgePriority(objType);
			
			queue.put(outMsg, pmo);// 放入消息

			qm.commit();
			closeQueue(queue);
		} catch (Exception e) {
			flg = false;
			throw e;
		} finally {
			disconnectQM(qm);
		}
		return flg;
	}

	@Override
	public void send(Collection<String> msgs, String objType) throws Exception {
		MQQueueManager qm = null;
		try {
			qm = getQM();
			MQQueue queue = qm.accessQueue(judgeObjectType(objType),
					sendOpenOptions);
			MQPutMessageOptions pmo = new MQPutMessageOptions();
			for (String msg : msgs) {
				MQMessage outMsg = constructMsg(msg);
				queue.put(outMsg, pmo);
			}
			qm.commit();
			closeQueue(queue);
		} catch (Exception e) {
			throw e;
		} finally {
			disconnectQM(qm);
		}
	}

	/**
	 * 构建消息对象
	 * 
	 * @param msg
	 * @return
	 * @throws IOException
	 */
	private MQMessage constructMsg(String msg) throws IOException {
		MQMessage outMsg = new MQMessage();
		outMsg.format = MQConstants.MQFMT_STRING;
		outMsg.characterSet = 1208;
		outMsg.writeString(msg);
		return outMsg;
	}
	
	@Override
	public void receive4SolrAddBack(String objType) throws Exception{
		log.error("receive4SolrAddBack MQ 开始运行");
		MQQueueManager qm = null;
		try
		{
			long now = System.currentTimeMillis();
			qm = getQM();
			MQQueue queue = qm
					.accessQueue(solrOrderBackoutQueueName, receiveOpenOptions);
			//
			MQGetMessageOptions gmo = new MQGetMessageOptions();
			gmo.options = MQConstants.MQGMO_WAIT
					| MQConstants.MQGMO_FAIL_IF_QUIESCING
					| MQConstants.MQGMO_SYNCPOINT;
			gmo.waitInterval = waitInterval;// 等待时间毫秒
			//
			List<String> msgList = new ArrayList<String>(mqlimit);//mqlimit
			MQMessage message = new MQMessage();
			//
			do{
				message.correlationId = MQConstants.MQCI_NONE;// 清除correlationId
				message.messageId = MQConstants.MQMI_NONE;// 清除messageId
				try
				{
					queue.get(message, gmo);// 获取消息
				}catch(MQException e){
					if (e.reasonCode != 2033) {
						log.error("receive4SolrAddBack read queue error. completionCode:"
								+ e.completionCode + ", reasonCode:"
								+ e.reasonCode + " queue name:"
								+ solrOrderBackoutQueueName, e);
						try
						{
							if(msgList!=null && msgList.size()>0){
								receiveImpl4Solr(msgList);//--将队列为空前收集的数据写入SOLR
								qm.commit();//提交
							}
						}catch(Exception e1){
							log.error("receive4SolrAddBack MQException message into Solr, 之前取出的消息全部回滚到MQ 条数："+msgList.size() ,e);
							try
							{
								qm.backout();//回退到本队列
								log.error("receive4SolrAddBack 3 this message has reached max backoutcout,put it in:"
										+ solrOrderBackoutQueueName);
							}catch(Exception e2){
								log.error("receive4SolrAddBack Error 3 this message has reached max backoutcout,put it in:"
										+ solrOrderBackoutQueueName,e2);
								throw e2;
							}
						}
					} else {// 正常，读取完消息
						log.error("receive4SolrAddBack no message in the queue:" + solrOrderBackoutQueueName);
						try
						{
							if(msgList!=null && msgList.size()>0){
								receiveImpl4Solr(msgList);//--将队列为空前收集的数据写入SOLR
								qm.commit();//提交
							}
						}catch(Exception e1){
							log.error("receive4SolrAddBack when no message into Solr , 之前取出的消息全部回滚到MQ 条数："+msgList.size(),e);
							try
							{
								qm.backout();//回退到本队列
								log.error("receive4SolrAddBack 2 this message has reached max backoutcout,put it in:"
										+ solrOrderBackoutQueueName);
							}catch(Exception e2){
								log.error("receive4SolrAddBack Error 2 this message has reached max backoutcout,put it in:"
										+ solrOrderBackoutQueueName,e2);
								throw e2;
							}
						}
					}
					break;
				}
				MQHeaderIterator it = new MQHeaderIterator(message);
				String result = it.getBodyAsText();
				msgList.add(result);
				try {
					//--批量提交到SOLR
					if(msgList.size()>=mqlimit){
						long end = System.currentTimeMillis();
						log.error("receive4SolrAddBack 从MQ取 "+msgList.size()+"  耗时："+(end-now)+" ms");
						receiveImpl4Solr(msgList);
						msgList.clear();
						now = end;
						qm.commit();
					}
				} catch (Exception e) {
					log.error("receive4SolrAddBack process message error 此次数据全部回滚，回滚条数："+msgList.size(), e);
					try
					{
						qm.backout();//回退到本队列
						log.error("receive4SolrAddBack 1 this message has reached max backoutcout,put it in:"
								+ solrOrderBackoutQueueName);
					}catch(Exception e2){
						log.error("receive4SolrAddBack Error 1 this message has reached max backoutcout,put it in:"
								+ solrOrderBackoutQueueName,e2);
						throw e2;
					}
					break;
				}
			}while (true);
			queue.close();
		}catch (Exception e) {
			throw e;
		} finally {
			disconnectQM(qm);
		}
	}
	
	@Override
	public void receive4Solr(String objType) throws Exception{
		log.error("receive4Solr MQ 开始运行");
		MQQueueManager qm = null;
		try {
			long now = System.currentTimeMillis();
			qm = getQM();
			MQQueue queue = qm
					.accessQueue(solrReceiveQueueName, receiveOpenOptions);
			//
			MQGetMessageOptions gmo = new MQGetMessageOptions();
			gmo.options = MQConstants.MQGMO_WAIT
					| MQConstants.MQGMO_FAIL_IF_QUIESCING
					| MQConstants.MQGMO_SYNCPOINT;

			gmo.waitInterval = waitInterval;// 等待时间毫秒
			//
			List<String> msgList = new ArrayList<String>(mqlimit);//mqlimit
			MQMessage message = new MQMessage();
			//
			do {
				message.correlationId = MQConstants.MQCI_NONE;// 清除correlationId
				message.messageId = MQConstants.MQMI_NONE;// 清除messageId
				try {
					queue.get(message, gmo);// 获取消息
				} catch (MQException e) {
					if (e.reasonCode != 2033) {
						log.error("receive4Solr read queue error. completionCode:"
								+ e.completionCode + ", reasonCode:"
								+ e.reasonCode + " queue name:"
								+ solrReceiveQueueName, e);
						try
						{
							if(msgList!=null && msgList.size()>0){
								receiveImpl4Solr(msgList);//--将队列为空前收集的数据写入SOLR
								qm.commit();//提交
							}
						}catch(Exception e1){
							log.error("receive4Solr MQException message into Solr, 之前取出的消息全部回滚到MQ 条数："+msgList.size() ,e);
							try
							{
								//--写SOLR暂时出现问题，回退到BACK MQ 队列中
								send(msgList,"SOLRADDORDERBACK");
								log.error("receive4Solr 3 this message has reached max backoutcout,put it in:"
										+ solrOrderBackoutQueueName);
							}catch(Exception e2){
								log.error("receive4Solr Error 3 this message has reached max backoutcout,put it in:"
										+ solrOrderBackoutQueueName,e2);
								throw e2;
							}
						}
					} else {// 正常，读取完消息
						log.error("receive4Solr no message in the queue:" + solrReceiveQueueName);
						try
						{
							if(msgList!=null && msgList.size()>0){
								receiveImpl4Solr(msgList);//--将队列为空前收集的数据写入SOLR
								qm.commit();//提交
							}
						}catch(Exception e1){
							log.error("receive4Solr when no message into Solr , 之前取出的消息全部回滚到MQ 条数："+msgList.size(),e);
							try
							{
								send(msgList,"SOLRADDORDERBACK");
								log.error("receive4Solr 2 this message has reached max backoutcout,put it in:"
										+ solrOrderBackoutQueueName);
							}catch(Exception e2){
								log.error("receive4Solr Error 2 this message has reached max backoutcout,put it in:"
										+ solrOrderBackoutQueueName,e2);
								throw e2;
							}
						}
					}
					break;
				}
				MQHeaderIterator it = new MQHeaderIterator(message);
				String result = it.getBodyAsText();
				msgList.add(result);
				try {
					//--批量提交到SOLR
					if(msgList.size()>=mqlimit){
						long end = System.currentTimeMillis();
						log.error("receiveImpl4Solr 从MQ取 "+msgList.size()+"  耗时："+(end-now)+" ms");
						receiveImpl4Solr(msgList);
						msgList.clear();
						now = end;
						qm.commit();
					}
				} catch (Exception e) {
					log.error("receive4Solr process message error 此次数据全部回滚，回滚条数："+msgList.size(), e);
					try
					{
						//--写SOLR暂时出现问题，回退到BACK MQ 队列中
						send(msgList,"SOLRADDORDERBACK");
						log.error("receive4Solr 1 this message has reached max backoutcout,put it in:"
								+ solrOrderBackoutQueueName);
					}catch(Exception e2){
						log.error("receive4Solr Error 1 this message has reached max backoutcout,put it in:"
								+ solrOrderBackoutQueueName,e2);
						throw e2;
					}
					break;
				}
			} while (true);
			queue.close();
		} catch (Exception e) {
			throw e;
		} finally {
			disconnectQM(qm);
		}
	}
	
	@Override
	public void receive4SolrAdd(String objType) throws Exception{
		log.error("receive4SolrAdd MQ 开始运行");
		MQQueueManager qm = null;
		try {
			qm = getQM();
			MQQueue queue = qm 
					.accessQueue(solrAddReceiveQueueName, receiveOpenOptions);
			//
			MQGetMessageOptions gmo = new MQGetMessageOptions();
			gmo.options = MQConstants.MQGMO_WAIT
					| MQConstants.MQGMO_FAIL_IF_QUIESCING
					| MQConstants.MQGMO_SYNCPOINT;

			gmo.waitInterval = waitInterval;// 等待时间毫秒
			//
			List<String> msgList = new ArrayList<String>(mqlimit);
			MQMessage message = new MQMessage();
			//
			do {
				message.correlationId = MQConstants.MQCI_NONE;// 清除correlationId
				message.messageId = MQConstants.MQMI_NONE;// 清除messageId
				try {
					queue.get(message, gmo);// 获取消息
				} catch (MQException e) {
					if (e.reasonCode != 2033) {
						log.error("receive4SolrAdd read queue error. completionCode:"
								+ e.completionCode + ", reasonCode:"
								+ e.reasonCode + " queue name:"
								+ solrReceiveQueueName, e);
						try
						{
							if(msgList!=null && msgList.size()>0){
								//--将队列为空前收集的数据写入SOLR
								receiveImpl4Solr(msgList);
								qm.commit();//提交
							}
						}catch(Exception e1){
							log.error("receive4SolrAdd MQException message into Solr, 之前取出的消息全部回滚到MQ 条数："+msgList.size() ,e);
							try
							{
								send(msgList,"SOLRADDORDERBACK");
								log.error("receive4SolrAdd 1 this message has reached max backoutcout,put it in:"
										+ solrOrderBackoutQueueName);
							}catch(Exception e2){
								log.error("receive4SolrAdd Error 1 this message has reached max backoutcout,put it in:"
										+ solrOrderBackoutQueueName,e2);
								throw e2;
							}
						}
					} else {//正常，读取完消息
						log.error("receive4SolrAdd no message in the queue:" + solrReceiveQueueName);
						try
						{
							if(msgList!=null && msgList.size()>0){
								//--将队列为空前收集的数据写入SOLR
								receiveImpl4Solr(msgList);
								qm.commit();//提交
							}
						}catch(Exception e1){
							log.error("receive4SolrAdd when no message into Solr , 之前取出的消息全部回滚到MQ 条数："+msgList.size(),e);
							//--数据入SOLR异常更新失败返回到新的BACK MQ里
							try
							{
								send(msgList,"SOLRADDORDERBACK");
								log.error("receive4SolrAdd 2 this message has reached max backoutcout,put it in:"
										+ solrOrderBackoutQueueName);
							}catch(Exception e2){
								log.error("receive4SolrAdd Error 2 this message has reached max backoutcout,put it in:"
										+ solrOrderBackoutQueueName,e2);
								throw e2;
							}
						}
					}
					break;
				}
				MQHeaderIterator it = new MQHeaderIterator(message);
				String result = it.getBodyAsText();
				msgList.add(result);
				try {
					//--批量提交到SOLR
					if(msgList.size()>=mqlimit){
						log.error("receive4SolrAdd timer receiveImpl4Solr "+msgList.size());
						receiveImpl4Solr(msgList);
						msgList.clear();
					}
				} catch (Exception e) {
					log.error("receive4SolrAdd process message error", e);
					// 数据入SOLR异常更新失败返回到新的BACK MQ里
					try
					{
						send(msgList,"SOLRADDORDERBACK");
						log.error("receive4SolrAdd 3 this message has reached max backoutcout,put it in:"
								+ solrOrderBackoutQueueName);
					}catch(Exception e2){
						log.error("receive4SolrAdd Error 3 this message has reached max backoutcout,put it in:"
								+ solrOrderBackoutQueueName,e2);
						throw e2;
					}
					break;
				}
				qm.commit();
			} while (true);
			queue.close();
		} catch (Exception e) {
			throw e;
		} finally {
			disconnectQM(qm);
		}
	}

	@Override
	public void receive(String objType) throws Exception {
		MQQueueManager qm = null;
		try {
			qm = getQM();
			String defaultQueueName = offlineReceiveQueueName;
			String defaultBackoutQueueName = offlineBackoutQueueName;
			if (StringUtils.equals(objType, Constants.ONLINE_TYPE)) {
				defaultQueueName = onlineReceiveQueueName;
				defaultBackoutQueueName = onlineBackoutQueueName;
			}else if(StringUtils.equals(objType, Constants.DATA_JINGANG)){
				defaultQueueName = jingangReceiveQueueName;
				defaultBackoutQueueName = jingangBackoutQueueName;
			}
			MQQueue queue = qm
					.accessQueue(defaultQueueName, receiveOpenOptions);
			//
			MQGetMessageOptions gmo = new MQGetMessageOptions();
			gmo.options = MQConstants.MQGMO_WAIT
					| MQConstants.MQGMO_FAIL_IF_QUIESCING
					| MQConstants.MQGMO_SYNCPOINT;

			gmo.waitInterval = waitInterval;// 等待时间毫秒
			//
			MQMessage message = new MQMessage();
			//
			do {
				message.correlationId = MQConstants.MQCI_NONE;// 清除correlationId
				message.messageId = MQConstants.MQMI_NONE;// 清除messageId

				try {
					queue.get(message, gmo);// 获取消息

					if (message.backoutCount >= maxBackoutCount) {// 如果多次处理此消息失败，那么放到backout队列中
						MQQueue backoutq = qm.accessQueue(
								defaultBackoutQueueName, sendOpenOptions);
						MQPutMessageOptions pmo = new MQPutMessageOptions();
						backoutq.put(message, pmo);
						qm.commit();
						backoutq.close();
						log.info("this message has reached max backoutcout,put it in:"
								+ defaultBackoutQueueName);
						continue;
					}
				} catch (MQException e) {
					if (e.reasonCode != 2033) {
						log.error("read queue error. completionCode:"
								+ e.completionCode + ", reasonCode:"
								+ e.reasonCode + " queue name:"
								+ defaultQueueName, e);
						throw e;
					} else {// 正常，读取完消息
						log.info("no message in the queue:" + defaultQueueName);
					}
					break;
				}

				MQHeaderIterator it = new MQHeaderIterator(message);
				String result = it.getBodyAsText();
				try {
					receiveImpl(result);
				} catch (Exception e) {
					log.error("process message error", e);
					qm.backout();// 有异常，回滚消息，继续做下一个消息
					continue;
				}
				qm.commit();
			} while (true);
			queue.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			disconnectQM(qm);
		}
	}

	@Override
	public void receiveFromBackQueue(String objType) throws Exception {
		MQQueueManager qm = null;
		try {
			qm = getQM();
			String defaultQueueName = offlineBackoutQueueName;
			String defaultBackoutQueueName = deadQueueName;
			if (StringUtils.equals(objType, Constants.ONLINE_TYPE)) {
				defaultQueueName = onlineBackoutQueueName;
			}else if(StringUtils.equals(objType, Constants.DATA_JINGANG)){
				defaultQueueName = jingangBackoutQueueName;
				defaultBackoutQueueName = jingangDeadQueueName;
			}
			MQQueue queue = qm
					.accessQueue(defaultQueueName, receiveOpenOptions);
			//
			MQGetMessageOptions gmo = new MQGetMessageOptions();
			gmo.options = MQConstants.MQGMO_WAIT
					| MQConstants.MQGMO_FAIL_IF_QUIESCING
					| MQConstants.MQGMO_SYNCPOINT;

			gmo.waitInterval = waitInterval;// 等待时间毫秒
			//
			MQMessage message = new MQMessage();
			//
			do {
				message.correlationId = MQConstants.MQCI_NONE;// 清除correlationId
				message.messageId = MQConstants.MQMI_NONE;// 清除messageId

				try {
					queue.get(message, gmo);// 获取消息

					if (message.backoutCount >= deadMaxBackoutCount) {// 如果多次处理此消息失败，那么放到backout队列中
						MQQueue backoutq = qm.accessQueue(
								defaultBackoutQueueName, sendOpenOptions);
						MQPutMessageOptions pmo = new MQPutMessageOptions();
						backoutq.put(message, pmo);
						qm.commit();
						backoutq.close();
						log.info("this message has reached max backoutcout,put it in:"
								+ defaultBackoutQueueName);
						continue;
					}
				} catch (MQException e) {
					if (e.reasonCode != 2033) {
						log.error("read queue error. completionCode:"
								+ e.completionCode + ", reasonCode:"
								+ e.reasonCode + " queue name:"
								+ defaultQueueName, e);
						throw e;
					} else {// 正常，读取完消息
						log.info("no message in the queue:" + defaultQueueName);
					}
					break;
				}

				MQHeaderIterator it = new MQHeaderIterator(message);
				String result = it.getBodyAsText();
				try {
					receiveImpl(result);
				} catch (Exception e) {
					log.error("process message error", e);
					qm.backout();// 有异常，回滚消息，继续做下一个消息
					continue;
				}
				qm.commit();
			} while (true);
			queue.close();
		} catch (Exception e) {
			throw e;
		} finally {
			disconnectQM(qm);
		}
	}
	
	@Override
	public int getMQDepth(String objType) throws Exception {
		int result = 0;
		MQQueueManager qm = null;

		try {
			qm = getQM();
			//int openOptions = MQConstants.MQOO_INPUT_AS_Q_DEF|MQConstants.MQOO_OUTPUT|MQConstants.MQOO_INQUIRE; 
			//int openOptions = MQConstants.MQOO_INPUT_AS_Q_DEF;
			int openOptions = MQConstants.MQOO_INPUT_AS_Q_DEF|MQConstants.MQOO_INQUIRE; 
			// 获取当前 MQ
			MQQueue queue = qm.accessQueue(judgeObjectType(objType),
					openOptions, null, null,null);
			
			// 获取当前MQ深度
			result = queue.getCurrentDepth();

			closeQueue(queue);
		} catch (Exception e) {
			result = 0;
			throw e;
		} finally {
			disconnectQM(qm);
		}
		return result;
	}
	

	/**
	 * 
	 * @param objType
	 * @return
	 */
	private String judgeObjectType(String objType) {
		log.info("MQ Send Type :"+objType);
		String defaultQueueName = offlineSendQueueName;
		if (StringUtils.equals(Constants.ONLINE_TYPE, objType) || StringUtils.equals(Constants.UPDATE_TYPE, objType)) {
			defaultQueueName = onlineSendQueueName;
		}else if (StringUtils.equals(Constants.DATA_JINGANG, objType)){
			defaultQueueName = jingangSendQueueName;
		}
		//--4 SOLR
		else if(StringUtils.equals(net.ytoec.kernel.action.common.Constants.JGCOMMAND,objType)){
			defaultQueueName = solrSendQueueName;
		}else if(StringUtils.equals(net.ytoec.kernel.action.common.Constants.SOLR_ADD_ORDER,objType)){
			defaultQueueName = solrAddSendQueueName;
		}else if(StringUtils.equals(net.ytoec.kernel.action.common.Constants.SOLR_ADD_ORDER_BACK,objType)){
			defaultQueueName = solrOrderBackoutQueueName;
		}
		return defaultQueueName;
	}

	/**
	 * 
	 * @param priority
	 * @return
	 */
	private int judgePriority(String objType) {
		int priority = Constants.OFFLINE_AND_UPDATE_PRIORITY;
		if (StringUtils.equals(Constants.ONLINE_TYPE, objType)) {
			priority = Constants.ONLINE_PRIORITY;
		}
		return priority;
	}

	public abstract void receiveImpl(String msg) throws Exception;
	/**
	 * 批量将信息写入Solr，包括订单状态更新和重量更新
	 * */
	public abstract void receiveImpl4Solr(List<String> msgs) throws Exception;

	public void setMaxBackoutCount(int maxBackoutCount) {
		this.maxBackoutCount = maxBackoutCount;
	}

	public void setQmName(String qmName) {
		this.qmName = qmName;
	}

	public void setQmParams(Hashtable<String, Object> qmParams) {
		this.qmParams = qmParams;
	}

	public void setOnlineSendQueueName(String onlineSendQueueName) {
		this.onlineSendQueueName = onlineSendQueueName;
	}

	public void setOfflineSendQueueName(String offlineSendQueueName) {
		this.offlineSendQueueName = offlineSendQueueName;
	}

	public void setOnlineReceiveQueueName(String onlineReceiveQueueName) {
		this.onlineReceiveQueueName = onlineReceiveQueueName;
	}

	public void setOfflineReceiveQueueName(String offlineReceiveQueueName) {
		this.offlineReceiveQueueName = offlineReceiveQueueName;
	}

	public void setOnlineBackoutQueueName(String onlineBackoutQueueName) {
		this.onlineBackoutQueueName = onlineBackoutQueueName;
	}

	public void setOfflineBackoutQueueName(String offlineBackoutQueueName) {
		this.offlineBackoutQueueName = offlineBackoutQueueName;
	}

	public void setDeadQueueName(String deadQueueName) {
		this.deadQueueName = deadQueueName;
	}

	public void setDeadMaxBackoutCount(int deadMaxBackoutCount) {
		this.deadMaxBackoutCount = deadMaxBackoutCount;
	}

	public String getJingangSendQueueName() {
		return jingangSendQueueName;
	}

	public void setJingangSendQueueName(String jingangSendQueueName) {
		this.jingangSendQueueName = jingangSendQueueName;
	}

	public String getJingangReceiveQueueName() {
		return jingangReceiveQueueName;
	}

	public void setJingangReceiveQueueName(String jingangReceiveQueueName) {
		this.jingangReceiveQueueName = jingangReceiveQueueName;
	}

	public String getJingangBackoutQueueName() {
		return jingangBackoutQueueName;
	}

	public void setJingangBackoutQueueName(String jingangBackoutQueueName) {
		this.jingangBackoutQueueName = jingangBackoutQueueName;
	}

	public String getJingangDeadQueueName() {
		return jingangDeadQueueName;
	}

	public void setJingangDeadQueueName(String jingangDeadQueueName) {
		this.jingangDeadQueueName = jingangDeadQueueName;
	}

	public String getSolrReceiveQueueName() {
		return solrReceiveQueueName;
	}

	public void setSolrReceiveQueueName(String solrReceiveQueueName) {
		this.solrReceiveQueueName = solrReceiveQueueName;
	}

	public String getSolrSendQueueName() {
		return solrSendQueueName;
	}

	public void setSolrSendQueueName(String solrSendQueueName) {
		this.solrSendQueueName = solrSendQueueName;
	}

	public int getMqlimit() {
		return mqlimit;
	}

	public void setMqlimit(int mqlimit) {
		this.mqlimit = mqlimit;
	}

	public String getSolrAddReceiveQueueName() {
		return solrAddReceiveQueueName;
	}

	public void setSolrAddReceiveQueueName(String solrAddReceiveQueueName) {
		this.solrAddReceiveQueueName = solrAddReceiveQueueName;
	}

	public String getSolrAddSendQueueName() {
		return solrAddSendQueueName;
	}

	public void setSolrAddSendQueueName(String solrAddSendQueueName) {
		this.solrAddSendQueueName = solrAddSendQueueName;
	}

	public String getSolrOrderBackoutQueueName() {
		return solrOrderBackoutQueueName;
	}

	public void setSolrOrderBackoutQueueName(String solrOrderBackoutQueueName) {
		this.solrOrderBackoutQueueName = solrOrderBackoutQueueName;
	}
}
