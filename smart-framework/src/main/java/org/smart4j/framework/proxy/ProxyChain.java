package org.smart4j.framework.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sf.cglib.proxy.MethodProxy;

public class ProxyChain {
	
	private final Class<?> targetClass;
	private final Object targetObject;
	private final Method targetMethod;
	private final MethodProxy methodProxy;
	private final Object[] methodParams;
	
	private int proxyIndex = 0;
	private List<Proxy> proxyList = new ArrayList<Proxy>();
	public ProxyChain(Class<?> targetClass, Object targetObject, Method targetMethod, MethodProxy methodProxy,
			Object[] methodParams) {
		super();
		this.targetClass = targetClass;
		this.targetObject = targetObject;
		this.targetMethod = targetMethod;
		this.methodProxy = methodProxy;
		this.methodParams = methodParams;
	}
	public Class<?> getTargetClass() {
		return targetClass;
	}
	public Object getTargetObject() {
		return targetObject;
	}
	public Method getTargetMethod() {
		return targetMethod;
	}
	public MethodProxy getMethodProxy() {
		return methodProxy;
	}
	public Object[] getMethodParams() {
		return methodParams;
	}
	
	public Object doProxyChain() throws Throwable{
		Object methodResult;
		if(proxyIndex<proxyList.size()){
			methodResult = proxyList.get(proxyIndex++).doProxy(this);
		}else{
			methodResult = methodProxy.invokeSuper(targetObject, methodParams);
		}
		return methodResult;
	}
	
	
	
	
}
 