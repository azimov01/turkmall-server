package dev.turkmall.onlineshopserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageConfig {
    @Autowired
    MessageSource messageSource;

    public String getMessageByLanguage(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }
}