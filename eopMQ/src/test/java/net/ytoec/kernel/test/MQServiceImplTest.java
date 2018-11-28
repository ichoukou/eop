package net.ytoec.kernel.test;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.ytoec.kernel.service.impl.AbstractMQServiceImpl;

public class MQServiceImplTest extends AbstractMQServiceImpl {
	private static Logger log = LoggerFactory
	.getLogger(MQServiceImplTest.class);

	@Override
	public void receiveImpl(String msg) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receiveImpl4Solr(List<String> msgs) throws Exception {
		// TODO Auto-generated method stub
		
	}
	

}
