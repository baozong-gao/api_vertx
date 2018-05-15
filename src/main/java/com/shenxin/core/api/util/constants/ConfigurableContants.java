package com.shenxin.core.api.util.constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * 常量配置类，从指定文件读取常量
 * 
 */
public class ConfigurableContants {

	private static final Logger logger = LoggerFactory.getLogger(ConfigurableContants.class);

	protected static Properties p = new Properties();

	protected static void init(String propertyFileName) {
		InputStream in = null;
		try {
			in = ConfigurableContants.class.getResourceAsStream(propertyFileName);
			if (in != null)
				p.load(in);
		} catch (IOException e) {
			logger.error("加载配置文件【{}】失败。{}", propertyFileName, e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
	}

	protected static String getProperty(String key, String defaultValue) {
		return p.getProperty(key, defaultValue);
	}

	protected static String getProperty(String key) {
		return getProperty(key, "");
	}

	/**
	 * 取以","分割的集合属性
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	protected static Set<String> getSetProperty(String key, String defaultValue) {
		String property = p.getProperty(key, defaultValue);
		if (property == null) {
			return null;
		}
		String[] strings = property.split(",");
		HashSet<String> hashSet = new HashSet<String>(strings.length);
		for (String string : strings) {
			hashSet.add(string.trim());
		}
		return hashSet;
	}

	protected static int getIntProperty(String key, int defaultValue) {
		String property = getProperty(key);
		int value = defaultValue;
		try {
			value = Integer.parseInt(property);
		} catch (Exception e) {}
		
		return value;
	}
	
	protected static long getLongProperty(String key, long defaultValue) {
		String property = getProperty(key);
		long value = defaultValue;
		try {
			value = Long.parseLong(property);
		} catch (Exception e) {}
		
		return value;
	}

	protected static Set<String> getSetProperty(String key) {
		return getSetProperty(key, "");
	}
	
	protected static boolean getBooleanProperty(String key, boolean b) {
		String property = getProperty(key, b?"yes":"no");
		if("yes".equals(property)){
			return true;
		}
		return false;
	}

	protected static Date getPropertyWithDateFormate(String key, String dateFormate) {
		String str = null;
		Date date = null;
		try {
			str = getProperty(key, "");
			date = new SimpleDateFormat(dateFormate).parse(str);
		} catch (ParseException e) {
			logger.error("Date [" + str + "] Error in Contants");
		}
		return date;
	}
}
