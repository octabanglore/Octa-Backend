package com.octa.notification.email.entiry;

import java.io.Serializable;
import java.util.Date;

public class MailMessage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String toAddr = null;
	private String fromAddr = null;
	private String ccAddr = null;
	private String bccAddr = null;
	private String subject = null;
	private String textBody = null;
	private String contentType = null;
	private String fileName = null;
	private Date sentDateTime = null;
	private int priority = -1;
	private int reTryCount = 0;
	private long retryDuration = 0;

	private String deliveryAs = null;
	private String type = null;
	private String exceptionOccoured = "";
	private int tenantId = 0;

	public MailMessage() {
	}

	public MailMessage(String toAddr, String fromAddr, String ccAddr, String bccAddr, String subject, String textBody, String contentType, String fileName,Date sentDateTime) {
		this.toAddr = toAddr;
		this.fromAddr = fromAddr;
		this.ccAddr = ccAddr;
		this.bccAddr = bccAddr;
		this.subject = subject;
		this.textBody = textBody;
		this.contentType = contentType;
		this.fileName = fileName;
		this.priority = priority;

		System.out.println("-----------while creating MailMessage object ---------");
		System.out.println("---------------it goes to ActiveMQ queue--------------");
		System.out.println("toAddr: " + toAddr);
		System.out.println("fromAddr : " + fromAddr);
		System.out.println("ccAddr: " + ccAddr);
		System.out.println("bccAddr: " + bccAddr);
		System.out.println("subject: " + subject);
		System.out.println("contentType: " + contentType);
		System.out.println("fileName: " + fileName);
		System.out.println("----------------------------------------------------");
	}

	//To Address
	public void setToAddr(String toAddr)	{
		this.toAddr = toAddr;
	}

	public String getToAddr(){
		return this.toAddr;
	}

	//From Address
	public void setFromAddr(String fromAddr)	{
		this.fromAddr = fromAddr;
	}
	public String getFromAddr(){
		return this.fromAddr;
	}

	//Subject
	public void setSubject(String subject)	{
		this.subject = subject;
	}

	public String getSubject(){
		return this.subject;
	}
	//Body
	public void setTextBody(String textBody)	{
		this.textBody = textBody;
	}

	public String getTextBody(){
		return this.textBody;
	}
	
	//Priority
	public void setPriority(int priority)	{
		this.priority = priority;
	}

	public int getPriority(){
		return this.priority;
	}

	//CC addresses
	public void setCcAddr(String ccAddr)	{
		this.ccAddr = ccAddr;
	}
	public String getCcAddr(){
		return this.ccAddr;
	}

	//BCC Addresses
	public void setBccAddr(String bccAddr)	{
		this.bccAddr = bccAddr;
	}
	public String getBccAddr(){
		return this.bccAddr;
	}

	//FileName
    public void setFileName(String fileName)	{
		this.fileName = fileName;
	}

	public String getFileName(){
		return this.fileName;
	}

	//Sent Time	sentDateTime
	public void setSentDateTime(Date sentDateTime)	{
		this.sentDateTime = sentDateTime;
	}

	public Date getSentDateTime(){
		return this.sentDateTime;
	}

	//Sent Time	sentDateTime
	public void setContentType(String contentType)	{
		this.contentType = contentType;
	}

	public String getContentType(){
		return this.contentType;
	}
	//Sent retryCount
	public void setReTryCount(int reTryCount)	{
		this.reTryCount = reTryCount;
	}

	public int getReTryCount(){
		return this.reTryCount;
	}
	//Sent retryCount
	public void setRetryDuration(long retryDuration)	{
		this.retryDuration = retryDuration;
	}

	public long getRetryDuration() {
		return this.retryDuration;
	}

	public String getDeliveryAs() {
		return deliveryAs;
	}

	public void setDeliveryAs(String deliveryAs) {
		this.deliveryAs = deliveryAs;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getExceptionOccoured() {
		return exceptionOccoured;
	}

	public void setExceptionOccoured(String exceptionOccoured) {
		this.exceptionOccoured = exceptionOccoured;
	}
	
	public int getTenantId() {
		return tenantId;
	}

	public void setTenantId(int tenantId) {
		this.tenantId = tenantId;
	}

}
