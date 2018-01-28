package org.smart4j.framework.proxy;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 切面代理
 * @author Administrator
 *
 */
public abstract class AspectProxy implements Proxy{
	private static final Logger logger = LoggerFactory.getLogger(AspectProxy.class);
	
	public final Object doProxy(ProxyChain proxyChain) throws Throwable{
		Object result = null;
		Class<?> cls = proxyChain.getTargetClass();
		Method method = proxyChain.getTargetMethod();
		Object[] params = proxyChain.getMethodParams();
		
		begin();
		
		try{
			if(intercept(cls,method,params)){
				before(cls,method,params);
				result = proxyChain.doProxyChain();
				after(cls,method,params);
			}else{
				result = proxyChain.doProxyChain();
			}
		}catch(Exception e){
			logger.error("proxy failure ",e);
			error(cls,method,params,e);
			throw e;
		}finally{
			end();
		}
		return result;
	}

	private void end() {
		// TODO Auto-generated method stub
		
	}

	private void error(Class<?> cls, Method method, Object[] params, Exception e) {
		// TODO Auto-generated method stub
		
	}

	public  abstract void after(Class<?> cls, Method method, Object[] params);

	public abstract void before(Class<?> cls, Method method, Object[] params) ;
	
	private boolean intercept(Class<?> cls, Method method, Object[] params) {
		return true;
	}

	private void begin() {
		
	}
}
