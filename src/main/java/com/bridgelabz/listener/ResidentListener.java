package com.bridgelabz.listener;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;

import com.bridgelabz.model.JmsObject;
import com.bridgelabz.utility.ElasticUtility;

public class ResidentListener<T> implements MessageListener {
	
	@Autowired
	RestHighLevelClient client;
	
	@Autowired
	ElasticUtility utility;

	@SuppressWarnings("unchecked")
	@Override
	public void onMessage(Message message) {
		
		try {
			ObjectMessage objectMessage = (ObjectMessage) message;
			JmsObject<T> jmsObject = (JmsObject<T>) objectMessage.getObject();
			System.out.println("Got " + jmsObject.getIndex() + ": " + jmsObject.getObject());
			
			String id = utility.save(jmsObject.getObject(), jmsObject.getIndex(), jmsObject.getType(), jmsObject.getId());
			System.out.println(jmsObject.getIndex() + " added at index: " + id);
			
		} catch (JMSException | IOException e) {
			e.printStackTrace();
		}
	}

}
