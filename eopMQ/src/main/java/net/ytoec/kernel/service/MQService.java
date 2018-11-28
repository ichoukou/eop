package net.ytoec.kernel.service;

import java.io.IOException;
import java.util.Collection;

import com.ibm.mq.MQException;
import com.ibm.mq.headers.MQDataException;

public interface MQService {
	/**
	 * 写消息到队列中
	 * 
	 * @param objType消息的业务类型
	 * @param msg
	 * @throws MQException
	 * @throws IOException
	 */
	public boolean send(String msg, String objType) throws Exception;

	/**
	 * 写多个消息到队列中
	 * 
	 * @param objType消息的业务类型
	 * @param msgs
	 * @throws MQException
	 * @throws IOException
	 */
	public void send(Collection<String> msgs, String objType) throws Exception;

	/**
	 * 从队列中获取消息
	 * 
	 * @param objType消息的业务类型
	 * @throws MQException
	 * @throws IOException
	 * @throws MQDataException
	 */
	public void receive(String objType) throws Exception;

	/**
	 * 从回退队列中获取消息
	 * 
	 * @param objType消息的业务类型
	 * @throws MQException
	 * @throws IOException
	 * @throws MQDataException
	 */
	public void receiveFromBackQueue(String objType) throws Exception;
	/**
	 * 读取MQ的订单更新信息写入SOLR
	 * 
	 * @param objType消息的业务类型
	 * @throws MQException
	 * @throws IOException
	 * @throws MQDataException
	 */
	public void receive4Solr(String objType) throws Exception;
	/**
	 * 读取MQ的订单新增信息写入SOLR
	 * @param objType消息的业务类型
	 * */
	public void receive4SolrAdd(String objType) throws Exception;
	/**
	 * 读取暂时写入SOLR失败的信息，再次写入SOLR
	 * @param objType消息的业务类型
	 * */
	public void receive4SolrAddBack(String objType) throws Exception;
	
	/**
	 * 获取MQ深度
	 * @param objType消息的业务类型
	 * */
	public int getMQDepth(String objType) throws Exception;
}
