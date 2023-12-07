package com.octa.security.management.message;

import java.util.Locale;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/i18")
@RequiredArgsConstructor
public class LocalizationController {
	
	private final DataBaseLabelSource labelSource;
	private final LocalizationService localeService;
	
	@GetMapping(value="/labels/{lang}")
	public Map<Locale, Map<String, String>> getLocalization(@PathVariable String lang) {
		return	localeService.getOrganisedLabels();
	}
	
	

}
