package com.octa.notification.email.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.octa.notification.email.entiry.MailMessage;
import com.octa.notification.email.service.EmailService;

@Component
public class MailMessageConsumer {
	
	@Autowired
	EmailService emailService;
	
	@JmsListener(destination = "${jms.queue.destination}")
	public void receive(Message message){
		if(message.getPayload() instanceof String){
			System.out.println("Recieved Message: " + message.getPayload().toString());
		}else if (message.getPayload() instanceof MailMessage){
			MailMessage mailMessage = (MailMessage) message.getPayload();
			System.out.println("Recieved Message: " + message.getPayload().toString());
			emailService.sendEmail(mailMessage);
		}else {
			System.err.println("Message Type Unkown !");
		}
	}
	
	@JmsListener(destination = "${jms.queue.misc}")
	public void receiveMiscMessage(Message message){
		if(message.getPayload() instanceof String){
			System.out.println("Recieved Misc Message: " + message.getPayload().toString());
		}else if (message.getPayload() instanceof MailMessage){
			MailMessage mailMessage = (MailMessage) message.getPayload();
			System.out.println("Recieved Misc Message: " + message.getPayload().toString());
			emailService.sendEmail(mailMessage);
		}else {
			System.err.println("Message Type Unkown !");
		}
	}


}
