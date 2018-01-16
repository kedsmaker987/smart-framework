package org.smart4j.framework.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * properties 文件处理类
 * @author Administrator
 *
 */
public class PropsUtil {
	private static final Logger logger = LoggerFactory.getLogger("PropsUtil");
	
	// 返回已经加载的 properties 类
	public static Properties loadProps(String fileName) {
		logger.info("start 加载properties文件: ", fileName);
		Properties props = null;
		InputStream input = null;
		try {
			input = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
			if (input == null) {
				throw new FileNotFoundException(fileName + "file is not found");
			}
			props = new Properties();
			props.load(input);
		} catch (Exception e) {
			logger.error("加载propertis类异常", e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					logger.error("关闭流异常", e);
				}
			}
		}
		return props;
	}
	
	/**
	 * 获取字符类型属性(默认值为空字符串)
	 * @param props
	 * @param key
	 * @return
	 */
	public static String getString(Properties props,String key){
		return getString(props,key,"");
	}
	
	/**
	 * 获取字符串类型 没有可以指定值 defaultValue;
	 * @param props
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getString(Properties props,String key,String defaultValue){
		String value = defaultValue;
		if(props.containsKey(key)){
			value = props.getProperty(key);
		}
		
		return value;
	}
	
	/**
	 * 获取数值型属性(默认为0)
	 * @param props
	 * @param key
	 * @return
	 */
	public static int getInt(Properties props,String key){
		return getInt(props,key,0);
	}
	
	/**
	 * 获取属性值类型()
	 * @param props
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static int getInt(Properties props,String key,int defaultValue){
		int value = defaultValue;
		if(props.containsKey(key)){
			value = CastUtil.castInt(props.getProperty(key));
		}
		return value;
	}
	
	/**
	 * 各種換進666
	 * @param props
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(Properties props,String key){
		return getBoolean(props,key,false);
	}
	
	
	public static boolean getBoolean(Properties props ,String key,boolean defalutValue){
		boolean value = defalutValue;
		if(props.containsKey(key)){
			value = CastUtil.castBoolean(props.getProperty(key));
		}
		return value;
	}
}
