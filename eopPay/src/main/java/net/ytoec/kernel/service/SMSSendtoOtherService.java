package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import net.ytoec.kernel.dataobject.SMSConfigURL;

/**
 * 将已处理的短信发送到其他模块 service
 * @author guolongchao
 * 20120808
 */
@SuppressWarnings("all")
public interface SMSSendtoOtherService extends BaseService{

	public void sendToOther(int limit);
}
