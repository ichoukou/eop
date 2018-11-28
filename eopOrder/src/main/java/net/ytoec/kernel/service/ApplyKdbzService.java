package net.ytoec.kernel.service;

import net.ytoec.kernel.dataobject.SendTask;
import net.ytoec.kernel.dataobject.SendTaskToTB;

public interface ApplyKdbzService<T> {
	
	public boolean addApplyKdbz(T applyKdbz,SendTask sendTask);
	
	public boolean editApplyKdbz(T applyKdbz,SendTask sendTask);
	
	public boolean applyStatusNodify(T applyKdbz, SendTaskToTB sendTaskToTB);
}
