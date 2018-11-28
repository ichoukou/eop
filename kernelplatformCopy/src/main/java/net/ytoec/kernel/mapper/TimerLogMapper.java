package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.TimerLog;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

public interface TimerLogMapper<T extends TimerLog>  extends BaseSqlMapper<T>{
	
	public int addTimerLog(TimerLog log);
	public List<T> get(Map<String,Object> map);
	public int countNum(Map<String,Object> map);
}
