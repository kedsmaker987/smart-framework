package org.smart4j.framework.bean;

/**
 * 返回数据对象
 * @author Administrator
 *
 */
public class Data {
	private Object model;
	
	public Data(Object model){
		this.model = model;
	}
	
	public Object getModel(){
		return model;
	}
}
