package org.smart4j.framework.helper;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.annotation.Aspect;
import org.smart4j.framework.annotation.Service;
import org.smart4j.framework.proxy.AspectProxy;
import org.smart4j.framework.proxy.Proxy;
import org.smart4j.framework.proxy.ProxyManager;
import org.smart4j.framework.proxy.TransactionProxy;
import org.smart4j.framework.util.JsonUtil;

public class AopHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(AopHelper.class);
	
	static{
		try{
			Map<Class<?>,Set<Class<?>>> proxyMap = createProxyMap();
			Map<Class<?>,List<Proxy>> targetMap = createTargetMap(proxyMap);
			for(Map.Entry<Class<?>, List<Proxy>> targetEntry:targetMap.entrySet()){
				Class<?> targetClass = targetEntry.getKey();
				List<Proxy> proxyList = targetEntry.getValue();
				Object proxy = ProxyManager.createProxy(targetClass, proxyList);
				BeanHelper.setBean(targetClass, proxy);
			}
		}catch(Exception e){
			logger.error("aop failure ",e);
		}
	}

	/**
	 * 创建 proxyMap 映射关系
	 * @return
	 */
	private static Map<Class<?>, Set<Class<?>>> createProxyMap() {
		// 代理类和@aspect value 对应目标类集合之间的对应关系
		Map<Class<?>,Set<Class<?>>> proxyMap = new HashMap<Class<?>,Set<Class<?>>>();
		// 切面代理类
		addAspectProxy(proxyMap);
		// 事物代理类
		addTransactionProxy(proxyMap);
		return proxyMap;
	}

	private static void addTransactionProxy(Map<Class<?>, Set<Class<?>>> proxyMap) {
		Set<Class<?>> serverClassSet = ClassHelper.getClassSetByAnnotation(Service.class);
		proxyMap.put(TransactionProxy.class, serverClassSet);
	}

	private static void addAspectProxy(Map<Class<?>, Set<Class<?>>> proxyMap) {
		Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);
		for(Class<?> proxyClass:proxyClassSet){
			// 代理类上有aspect 的标签
			if(proxyClass.isAnnotationPresent(Aspect.class)){
				Aspect aspect = proxyClass.getAnnotation(Aspect.class);
				Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
				proxyMap.put(proxyClass, targetClassSet);
			}
		}
	}

	private static Set<Class<?>> createTargetClassSet(Aspect aspect) {
		Set<Class<?>> targetClassSet = new HashSet<Class<?>>();
		Class<? extends Annotation> annotationClass = aspect.value();
		if(annotationClass!=null && !annotationClass.equals(Aspect.class)){
			targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotationClass));
		}
		return targetClassSet;
	}
	
	private static Map<Class<?>,List<Proxy>> createTargetMap(Map<Class<?>,Set<Class<?>>> proxyMap) throws InstantiationException, IllegalAccessException{
		Map<Class<?>,List<Proxy>> targetMap = new HashMap<Class<?>,List<Proxy>>();
		for(Map.Entry<Class<?>, Set<Class<?>>> proxyEntry:proxyMap.entrySet()){
			Class<?> proxyClass = proxyEntry.getKey();
			Set<Class<?>> targetClassSet = proxyEntry.getValue();
			for(Class<?> targetClass:targetClassSet){
				Proxy proxy = (Proxy)proxyClass.newInstance();
				if(targetMap.containsKey(targetClass)){
					targetMap.get(targetClass).add(proxy);
				}else{
					List<Proxy> proxyList = new ArrayList<Proxy>();
					proxyList.add(proxy);
					targetMap.put(targetClass,proxyList);
				}
			}
		}
		return targetMap;
	}
	
}
