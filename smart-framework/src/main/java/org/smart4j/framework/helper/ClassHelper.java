package org.smart4j.framework.helper;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.smart4j.framework.annotation.Controller;
import org.smart4j.framework.annotation.Service;
import org.smart4j.framework.util.ClassUtil;


/**
 * 类操作助手
 * @author Administrator
 *
 */
public final class ClassHelper {
	
	/**
	 * 存放加载的所有类
	 */
	private static final Set<Class<?>> CLASS_SET; 
	
	static{
		String basePackage = ConfigHelper.getAppBasePackage();
		CLASS_SET = ClassUtil.getClassSet(basePackage);
	}
	
	/**
	 * 获得应用包下所有的类
	 * @return
	 */
	public static Set<Class<?>> getClassSet(){
		return CLASS_SET;
	}
	
	/**
	 * 获得应用吧包下所有@service 的类
	 * @return
	 */
	public static Set<Class<?>> getServiceClassSet(){
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		for(Class<?> cls: CLASS_SET){
			if(cls.isAnnotationPresent(Service.class)){
				classSet.add(cls);
			}
		}
		return classSet;
	}
	
	/**
	 * 获得应用包下所有的@controller 类
	 * @return
	 */
	public static Set<Class<?>> getControllerClassSet(){
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		for(Class<?> cls: CLASS_SET){
			if(cls.isAnnotationPresent(Controller.class)){
				classSet.add(cls);
			}
		}
		return classSet;
	}
	
	/**
	 * 获得应用包下所有Bean类(@controller @service)
	 * @return
	 */
	public static Set<Class<?>> getBeanClassSet(){
		Set<Class<?>> beanClassSet = new HashSet<Class<?>>();
		beanClassSet.addAll(getControllerClassSet());
		beanClassSet.addAll(getServiceClassSet());
		return beanClassSet;
	}
	
	/**
	 * 获取应用包下父类的所有子接口
	 * @param superClass
	 * @return
	 */
	public static Set<Class<?>> getClassSetBySuper(Class<?> superClass){
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		for(Class<?> cls: CLASS_SET){
			// 是否是此类注解
			if(superClass.isAssignableFrom(cls) && !superClass.equals(cls)){
				classSet.add(cls);
			}
		}
		return classSet;
	}
	
	/**
	 * 应用包下所有带有注解的所有类
	 * @param annotationClass
	 * @return
	 */
	public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotationClass){
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		for(Class<?> cls: CLASS_SET){
			if(cls.isAnnotationPresent(annotationClass)){
				classSet.add(cls);
			}
		}
		return classSet;
	}
}
