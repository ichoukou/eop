package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.SMSObject;

import org.springframework.dao.DataAccessException;

/**
 *待发送短信表（供其他模块使用，不受定时器发送时间的限制）
 * @author guolongchao
 * @param <T>
 */
public interface SMSOtherWaitDao<T> extends BaseDao<T>{

	/**
	   * 将临时表ec_core_phone_temp中的数据
	   * 去除重复的手机号后
	   * 导入待发送短信表ec_core_sms_wait 
	   */
    public boolean insertBatchToSMS();
    /**
     * 直接插入手机号
     */
    public int insertPhone(SMSObject smsObject);
    /**
     * 插入记录后更新序列
     */
    public boolean updatesequenceID(SMSObject smsObject);
    /**
     * 查询id的最大值
     */
    public SMSObject searchMaxId(); 
    
    /**
     * 将处于待发送状态的短信删除掉
     * @param status==8
     */
    public boolean deleteByStatus(String status);
    
    /**
     * 批量更新ec_core_sms_wait中的数据
     * @param map
     * smsType,pkTotal,messageContent,status
     */
    public boolean editBatch(Map map);
    
    /**
	 * 根据查询条件查询符合条件的记录
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getList(Map map) throws DataAccessException;
}
