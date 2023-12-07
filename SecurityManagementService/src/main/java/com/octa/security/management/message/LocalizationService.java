package com.octa.security.management.message;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.octa.security.management.message.entity.Labels;
import com.octa.security.management.message.entity.Message;
import com.octa.transaction.entity.Tenant;
import com.octa.transaction.platform.OctaTransaction;
import com.octa.transaction.platform.TenantAwareRequestContext;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocalizationService {
	
	private final MessageDAO messageRepository;
	private final LabelsDao labelsRepository;
	

	@OctaTransaction
	public List<Message> getMessages() {
		TenantAwareRequestContext.setTenantScope(TenantAwareRequestContext.DEFAULT_MASTER_CONTEXT.getId());
		return messageRepository.getMessages();
	}
	
	@OctaTransaction
	public List<Labels> getLabels() {
		TenantAwareRequestContext.setTenantScope(TenantAwareRequestContext.DEFAULT_MASTER_CONTEXT.getId());
		return labelsRepository.getLabels();
	}
	
	@OctaTransaction
	public List<Message> getMessages(Tenant t) {
		return messageRepository.getMessages();
	}
	
	@OctaTransaction
	public List<Labels> getLabels(Tenant t) {
		return labelsRepository.getLabels();
	}
	
	@OctaTransaction
	public Map<Locale, Map<String, String>> getOrganisedLabels() {
		List<Labels> lbels = labelsRepository.getLabels();
		Map<Locale, Map<String, String>> mpalabels =  new HashMap<>();
		for (Labels label : lbels) {
			String[] localeParams = label.getLocale().getLocaleCode().split("\\_");
			Locale locale = localeParams.length > 1 ? new Locale(localeParams[0], localeParams[1]) : new Locale(localeParams[0]);
			addLocalization(locale, label.getEntityName() + "." + label.getLabelCode(), label.getLabelValue(), mpalabels);
		}
		return mpalabels;
	}
	private void addLocalization(Locale locale, String code, String msg, Map<Locale, Map<String, String>> mapLocalize) {
		mapLocalize = mapLocalize == null ? new HashMap<>() : mapLocalize;
		mapLocalize.computeIfAbsent(locale, k -> new HashMap<>()).put(code, msg);
		}
}
