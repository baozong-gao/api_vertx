package com.shenxin.core.api.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringUtil {
    
    @Autowired
    protected ApplicationContext context;

    public <T> T getBean(String name, Class<T> c) {
        if (context != null && name != null) {
            try {
                return context.getBean(name, c);
            } catch (Exception e) {
            }
        }
        return null;
    }
    
}
