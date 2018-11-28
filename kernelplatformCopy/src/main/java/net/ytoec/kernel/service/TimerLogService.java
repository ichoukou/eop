package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.TimerLog;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
public interface TimerLogService<T extends TimerLog> {
	public boolean addTimerLog(TimerLog bean);
	public List<T> get(Map<String, String> map,Pagination pagination);
	public int countNum(Map<String, String> map,Pagination pagination);
}
