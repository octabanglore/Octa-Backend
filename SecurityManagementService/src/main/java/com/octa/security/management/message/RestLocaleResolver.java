package com.octa.security.management.message;

import java.util.Locale;

import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import jakarta.servlet.http.HttpServletRequest;

public class RestLocaleResolver extends AcceptHeaderLocaleResolver {
	
	@Override
    public Locale resolveLocale(HttpServletRequest request) {
        String lang = request.getHeader("Accept-Language");
        if (lang == null || lang.isEmpty()) {
            return Locale.getDefault();
        }
        return Locale.forLanguageTag(lang);
    }

}
