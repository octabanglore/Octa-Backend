package com.octa.notification.email.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.octa.notification.email.entiry.MailMessage;
import com.octa.notification.email.producer.MailMessageSender;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    
    @Autowired
    private MailMessageSender mailMessageSender;
    
    @Value("${octa.activeMQ.retrycount}")
	private String mqRetryCount;

	public void sendEmail(MailMessage mailMessage){
		
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setTo(mailMessage.getToAddr());
			
			if(StringUtils.isNoneBlank(mailMessage.getFromAddr())) {
				helper.setFrom(mailMessage.getFromAddr());
			}
			
			if(StringUtils.isNoneBlank(mailMessage.getBccAddr())) {
				helper.setBcc(mailMessage.getBccAddr());
			}
			
			if(StringUtils.isNoneBlank(mailMessage.getCcAddr())) {
				helper.setCc(mailMessage.getCcAddr());
			}
			
			if(StringUtils.isNoneBlank(mailMessage.getSubject())) {
				helper.setSubject(mailMessage.getSubject());
			}
			
			if(StringUtils.isNoneBlank(mailMessage.getTextBody())) {
				boolean imgPresent = false;
				String textBody = getEmailTemplate(mailMessage.getTextBody());
				imgPresent = textBody.contains("loginHeaderBg");
				if(imgPresent){
					int startImg = textBody.indexOf("emailImg")+9;
					String hideProp = "style=\"mso-hide:all; display:none; max-height:0px;\" ";
					textBody = textBody.substring(0,startImg+1)+hideProp+textBody.substring(startImg+1);
				}
				helper.setText(textBody, true);
			}
			
			if(StringUtils.isNoneBlank(mailMessage.getFileName())) {
				String filename = mailMessage.getFileName();
				for (String attachmentPath : filename.split("|")) {
					FileSystemResource file = new FileSystemResource(new File(attachmentPath));
			    	helper.addAttachment(file.getFilename(), file);
				}
				
			}
			javaMailSender.send(message);
			
			System.out.println("------------ from EmailService --------------");
			System.out.println("To Address: " + mailMessage.getToAddr());
			System.out.println("fromAddr : " + mailMessage.getFromAddr());
			System.out.println("ccAddr: " + mailMessage.getCcAddr());
			System.out.println("bccAddr: " + mailMessage.getBccAddr());
			System.out.println("subject: " + mailMessage.getSubject());
			System.out.println("contentType: " + mailMessage.getContentType());
			System.out.println("FileName: " + mailMessage.getFileName());
			System.out.println("----------- Mail Handover to SMTP server --------------");
			
		} catch (MailException e) {
			e.printStackTrace();
			messageReSend(mailMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
			messageReSend(mailMessage);
		}
	}
	
	private void messageReSend(MailMessage mailMessage) {
		int retryCount = 5;
    	if(StringUtils.isNotBlank(mqRetryCount))
    		retryCount = Integer.parseInt(mqRetryCount);
    	if (mailMessage.getReTryCount() < retryCount){
    		System.out.println("retry count is " + (mailMessage.getReTryCount()+1));
    		mailMessage.setReTryCount(mailMessage.getReTryCount() + 1);
    		System.out.println("retry count is " + (mailMessage.getReTryCount()));
    		if (mailMessage.getRetryDuration() == 0)
    			mailMessage.setRetryDuration((Long)( 1000l * mailMessage.getReTryCount()));
    		else{
    			mailMessage.setRetryDuration((Long)(mailMessage.getRetryDuration()* mailMessage.getReTryCount()));
    		}
    		mailMessageSender.resendMail(mailMessage);
    	}
	}
	
	public static String getEmailTemplate(String contentBody){
		StringBuilder contentBuilder = new StringBuilder();
		try {
			String filePath = "manthanEmailTemplate.html";
		    BufferedReader in = new BufferedReader(new FileReader("D:\\OCTA\\Workspace\\Octa-Backend\\SecurityManagementService\\src\\main\\resources\\manthanEmailTemplate.html"));
		    String str;
		    while ((str = in.readLine()) != null) {
		        contentBuilder.append(str);
		    }
		    in.close();
		} catch (IOException e) {
			//log.info("Error while preparing template for email :: "+e.getMessage());
		}
		String content = contentBuilder.toString();
		int startEB = content.indexOf("emailBody")+10;
		content = content.substring(0,startEB+1)+contentBody+content.substring(startEB+1);
		return content;
	}
}
