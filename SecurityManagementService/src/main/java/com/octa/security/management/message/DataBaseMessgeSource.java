package com.octa.security.management.message;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.stereotype.Component;

import com.octa.security.management.message.entity.Message;

import jakarta.annotation.PostConstruct;

@Component(value="messages")
@DependsOn(value={"emf"})
public class DataBaseMessgeSource extends AbstractMessageSource {
	
	private static final Logger LOG = LoggerFactory.getLogger(DataBaseMessgeSource.class);
	
	private LocMessages locMessages;
	
	@Autowired
	private LocalizationService localeService;

	@Override
	protected MessageFormat resolveCode(String code, Locale locale) {

		String msg = locMessages.getMessage(code, locale);
		LOG.info("Resolved message : {}", msg);
		if (null == msg) {
			throw new NoSuchMessageException("The message for code : " + code + " in locale : " + locale + " is not available.");
		}
		msg = msg.replaceAll("\'", "\'\'");
		return createMessageFormat(msg, locale);
	
	}
	
	@PostConstruct
	public void init() {
		LOG.info("Loading messages from database has been started...");
		locMessages = new LocMessages();
		  List<Message> msgList = localeService.getMessages();
		for (Message label : msgList) {
			String[] localeParams = label.getLocale().getLocaleCode().split("\\_");
			Locale locale = localeParams.length > 1 ? new Locale(localeParams[0], localeParams[1]) : new Locale(localeParams[0]);
			locMessages.addMessage(label.getEntityName() + "." + label.getMsgCode(), locale, label.getMsgValue());
		}
		LOG.info("Loading Labels from database has been completed...");
		LOG.info("Loaded Labels count : {}", locMessages.getMessageCount());
	}
	
	
	protected static final class LocMessages {

		private Map<String, Map<Locale, String>> messages;

		public void addMessage(String code, Locale locale, String msg) {
			messages = messages == null ? new HashMap<>() : messages;
	        messages.computeIfAbsent(code, k -> new HashMap<>()).put(locale, msg);
		}

		public String getMessage(String code, Locale locale) {
			return messages != null
		            ? messages.getOrDefault(code, Collections.emptyMap()).getOrDefault(locale, "")
		            : "";
		}

		public int getMessageCount() {
			return messages != null ? messages.size() : 0;
		}
	}
	

}
