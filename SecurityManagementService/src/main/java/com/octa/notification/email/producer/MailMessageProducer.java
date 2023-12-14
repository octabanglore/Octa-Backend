package com.octa.notification.email.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.octa.notification.email.entiry.MailMessage;

@Component
@EnableJms
public class MailMessageProducer {
	@Autowired
	JmsTemplate jmsTemplate;
	
	@Value("${jms.queue.destination}")
	String destinationQueue;
	
	public void send(String msg){
		jmsTemplate.convertAndSend(destinationQueue, msg);
	}
	public void send(MailMessage mailMessage){
		jmsTemplate.convertAndSend(destinationQueue, mailMessage);
	}
}
