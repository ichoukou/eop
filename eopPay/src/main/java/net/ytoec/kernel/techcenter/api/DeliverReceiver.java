package net.ytoec.kernel.techcenter.api;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techcenter.client.api.IDeliverReceiver;
import com.techcenter.protocol.standard12.Standard_Deliver;

public class DeliverReceiver implements IDeliverReceiver {
	private static final Logger logger = LoggerFactory.getLogger(DeliverReceiver.class);
	@Override
	public void receive(Standard_Deliver obj) {
		logger.error("mobile:" + obj.getSrcMobile() + " context:"
				+ obj.getContentString() + " number:" + obj.getDestNumber());
	}
}
