package org.smart4j.framework.bean;

import java.util.Map;

/**
 * 返回的视图对象
 * @author Administrator
 *
 */
public class View {
	
	private String path;
	
	private Map<String,Object> model;
	
	public View(String path,Map<String,Object> model){
		this.path = path;
		this.model = model;
	}
	
	public View addModel(String key,Object value){
		model.put(key, value);
		return this;
	}
	
	public String getPath(){
		return path;
	}
	
	public Map<String,Object> getModel(){
		return model;
	}
}
