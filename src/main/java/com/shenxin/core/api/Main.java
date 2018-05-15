package com.shenxin.core.api;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author: gaobaozong
 * @Description: Main入口
 * @Date: Created in 2017/9/15 - 12:36
 * @Version: V1.0
 */
public class Main {

    public static ApplicationContext context;

    public static void main(String[] args) {
        context = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
    }
}
