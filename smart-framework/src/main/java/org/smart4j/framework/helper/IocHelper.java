package org.smart4j.framework.helper;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.smart4j.framework.annotation.Inject;
import org.smart4j.framework.util.ReflectionUtil;

/**
 * 依赖注入
 * @author Administrator
 *
 */
public class IocHelper {
	static{
		// bean 和对应实例的映射关系
		Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
		if(MapUtils.isNotEmpty(beanMap)){
			// 遍历 beanMap
			for(Map.Entry<Class<?>, Object> beanEntry:beanMap.entrySet()){
				// bean 的类
				Class<?> beanClass = beanEntry.getKey();
				// bean的实例
				Object beanInstance = beanEntry.getValue();
				
				// bean 类中所有的成员变量
				Field[] beanFields = beanClass.getDeclaredFields();
				if(ArrayUtils.isNotEmpty(beanFields)){
					// 遍历 beanField
					for(Field beanField:beanFields){
						// 获取 成员变量中有@inject 注解的类
						Class<?> beanFieldClass = beanField.getType();
						// 获得实例
						Object beanFieldInstance = beanMap.get(beanFieldClass);
						if(beanFieldInstance!=null){
							// 通过反射初始化BeanField 的值
							ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
						}
					}
				}
			}
		}
	}
}
