package org.smart4j.framework.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.smart4j.framework.util.ReflectionUtil;

/**
 * Bean 的助手类
 * @author Administrator
 *
 */
public class BeanHelper {
	/**
	 * Bean 类的映射(Bean 和Bean实例之间的映射关系)
	 */
	private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<Class<?>,Object>();
	
	static{
		Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
		for(Class<?> beanClass:beanClassSet){
			Object obj = ReflectionUtil.newInstance(beanClass);
			BEAN_MAP.put(beanClass, obj);
		}
	}
	
	/**
	 * 获取Bean 映射
	 * @return
	 */
	public static Map<Class<?>,Object> getBeanMap(){
		return BEAN_MAP;
	}
	
	/**
	 * 获取Bean的实例
	 * @param cls
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> cls){
		if(!BEAN_MAP.containsKey(cls)){
			throw new RuntimeException("can't not get bean by class: "+cls);
		}
		
		return (T) BEAN_MAP.get(cls);
	}
}
