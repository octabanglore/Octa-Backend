package com.octa.notification.email.producer;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.octa.notification.email.entiry.MailMessage;

import jakarta.mail.MessagingException;

@Component
@EnableJms
public class MailMessageSender {
	
	@Autowired
	JmsTemplate jmsTemplate;
	
	@Value("${jms.queue.destination}")
	String destinationQueue;
	
	@Value("${jms.queue.misc}")
	String miscQueue;
	
	public void send(MailMessage mailMessage){
		jmsTemplate.convertAndSend(destinationQueue, mailMessage);
	}
	
	public void resendMail(MailMessage mailMessage){
		jmsTemplate.convertAndSend(miscQueue, mailMessage);
	}
	
	public void sendEmailNotification(String toAddr, String fromAddr, String subject, String bodyText) throws MessagingException, IOException {
    	sendEmailNotification(toAddr, fromAddr, "", "", subject, bodyText, "", "", new Date());
    }
    
    public void sendEmailNotification(String toAddr, String fromAddr, String ccAddr, String bccAddr, String subject, String bodyText, String contentType) throws MessagingException, IOException {
    	sendEmailNotification(toAddr, fromAddr, ccAddr, bccAddr, subject, bodyText, contentType, "", new Date());
    }
    
    
    public void sendEmailNotification(String toAddr, String fromAddr, String ccAddr, String bccAddr, String subject, String bodyText, String contentType, String attachmentPath, Date sentDateTime) throws MessagingException, IOException {
    	
    	System.out.println("-----------while creating MailMessage object ---------");
		System.out.println("---------------it goes to ActiveMQ queue--------------");
		System.out.println("toAddr: " + toAddr);
		System.out.println("fromAddr : " + fromAddr);
		System.out.println("ccAddr: " + ccAddr);
		System.out.println("bccAddr: " + bccAddr);
		System.out.println("subject: " + subject);
		System.out.println("contentType: " + contentType);
		System.out.println("fileName: " + attachmentPath);
		System.out.println("----------------------------------------------------");
		
    	MailMessage mailMessage = new MailMessage();
    	mailMessage.setToAddr(toAddr);
    	mailMessage.setFromAddr(fromAddr);
    	mailMessage.setCcAddr(ccAddr!=null?ccAddr:"");
    	mailMessage.setBccAddr(bccAddr!=null?bccAddr:"");
    	mailMessage.setSubject(subject!=null?subject:"");
    	mailMessage.setTextBody(bodyText);
    	mailMessage.setContentType(contentType);
    	mailMessage.setFileName(attachmentPath!=null?attachmentPath:"");
    	mailMessage.setSentDateTime(sentDateTime);
    	send(mailMessage);
    	
    }
}
