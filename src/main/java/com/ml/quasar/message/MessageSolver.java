package com.ml.quasar.message;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageSolver implements I_MessageSolver {

	/**
	 * Message Solver
	 */
	@Autowired
    private MessageSource messageSource;

	/**
	 * The locale.
	 */
	private Locale locale;

	/**
	 * Resets the locale, Spanish will be the default language.
	 */
	private void resetLocale() {
		// Here, we can take a language configured in some place, maybe a table with the parameters
		this.setLocale(new Locale("es"));
	}

	/**
	 * Init method.
	 */
	@PostConstruct
	public void init() {
		this.resetLocale();
	}
	
	/**
	 * Gets a i18n message.
	 * 
	 * @param code
	 *            message code
	 * 
	 * @return string
	 */
	@Override
	public String getMessage(String code) {
		return this.getMessageSource().getMessage(code, new String[] {}, this.getLocale());
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return this.locale;
	}

	/**
	 * @param locale
	 *            the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}