package org.smart4j.framework.helper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.smart4j.framework.annotation.Action;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Request;

/**
 * 控制器助手类
 * @author Administrator
 *
 */
public final class ControllerHelper {
	
	/**
	 * 存放请求和处理器之间关系
	 */
	private static final Map<Request,Handler> ACTION_MAP = new HashMap<Request,Handler>();
	
	static{
		// 获取所有controller.class 类
		Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
		
		if(CollectionUtils.isNotEmpty(controllerClassSet)){
			//循环 
			for(Class<?> controllerClass:controllerClassSet){
				// 获取 controller 类中所有的方法
				Method[] methods = controllerClass.getDeclaredMethods();
				for(Method method: methods){
					if(method.isAnnotationPresent(Action.class)){
						// 获取action 注解
						Action action = method.getAnnotation(Action.class);
						// 获取 action注解中的url 映射
						String mapping = action.value();
						// 根据对应的规则去区分
						if(mapping.matches("\\w+:/\\w*")){
							String[] array = mapping.split(":");
							if(ArrayUtils.isNotEmpty(array) && array.length==2){
								// 请求method
								String requestMethod = array[0];
								// 请求地址
								String requestPath = array[1];
								
								Request request = new Request(requestMethod,requestPath);
								Handler handler = new Handler(controllerClass,method);
								// 初始化ACTION_MAP
								ACTION_MAP.put(request, handler);
							}
						}
						
					}
				}
			}
		}
	}
}
