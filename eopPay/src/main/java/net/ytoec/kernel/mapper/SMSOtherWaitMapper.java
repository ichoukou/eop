package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * 待发送短信表（供其他模块使用，不受定时器发送时间的限制）
 * @author guolongchao
 * 20120910
 */
public interface SMSOtherWaitMapper<T extends SMSObject> extends BaseSqlMapper<T> {

	  /**
	   * 将临时表ec_core_phone_temp中的数据
	   * 去除重复的手机号后
	   * 导入待发送短信表ec_core_sms_wait 
	   */
      public void insertBatchToSMS();
      /**
       * 直接插入手机号
       */
      public int insertPhone(SMSObject smsObject);
      /**
       * 插入记录后更新序列
       */
      public void updatesequenceID(SMSObject smsObject);
      /**
       * 将处于待发送状态的短信删除掉
       * @param status==8
       */
      public void deleteByStatus(String status);
      
      /**
       * 批量更新ec_core_sms_wait中的数据
       * @param map
       * smsType,pkTotal,messageContent,status
       */
      public void editBatch(Map map);
      
	  /**
	  	 * 根据查询条件查询符合条件的记录
	  	 * @param map
	  	 * @return
	  	 * @throws DataAccessException
	  	 */
	  public List<SMSObject> getList(Map map) throws DataAccessException;
	  
	  /**
	   * 查询id的最大值
	   * return int
	   */
	  public SMSObject searchMaxId();
}
