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

import com.octa.security.management.message.entity.Labels;

import jakarta.annotation.PostConstruct;

@Component(value="labels")
@DependsOn(value={"emf"})
public class DataBaseLabelSource extends AbstractMessageSource {
	private static final Logger LOG = LoggerFactory.getLogger(DataBaseLabelSource.class);
	
	private LocLabels locLabels;
	
	@Autowired
	private LocalizationService localeService;
	
	@Override
	protected MessageFormat resolveCode(String code, Locale locale) {
		String msg = locLabels.getLabels(code, locale);
		LOG.info("Resolved labels : {}", msg);
		if (null == msg) {
			throw new NoSuchMessageException("The labels for code : " + code + " in locale : " + locale + " is not available.");
		}
	
		msg = msg.replaceAll("\'", "\'\'");
		return createMessageFormat(msg, locale);
	}
	
	@PostConstruct
	public void init() {
		LOG.info("Loading labels from database has been started...");
		locLabels = new LocLabels();
		 List<Labels> lblList = localeService.getLabels();
		for (Labels label : lblList) {
			String[] localeParams = label.getLocale().getLocaleCode().split("\\_");
			Locale locale = localeParams.length > 1 ? new Locale(localeParams[0], localeParams[1]) : new Locale(localeParams[0]);
			locLabels.addMessage(label.getEntityName() + "." + label.getLabelCode(), locale, label.getLabelValue());
		}
		LOG.info("Loading Labels from database has been completed...");
		LOG.info("Loaded Labels count : {}", locLabels.getLabelCount());
	}
	
	public Map<String, Map<Locale, String>> getAllLabels() {
		return locLabels.getAllLabels();
	}
	
	protected static final class LocLabels {

		private Map<String, Map<Locale, String>> mpalabels;

		public void addMessage(String code, Locale locale, String msg) {
			mpalabels = mpalabels == null ? new HashMap<>() : mpalabels;
			mpalabels.computeIfAbsent(code, k -> new HashMap<>()).put(locale, msg);
		}

		public String getLabels(String code, Locale locale) {
			return mpalabels != null
		            ? mpalabels.getOrDefault(code, Collections.emptyMap()).getOrDefault(locale, "")
		            : "";
		}

		public int getLabelCount() {
			return mpalabels != null ? mpalabels.size() : 0;
		}
		public Map<String, Map<Locale, String>> getAllLabels() {
			return mpalabels;
		}
	}

}
