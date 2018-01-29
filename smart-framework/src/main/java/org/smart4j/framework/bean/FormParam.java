package org.smart4j.framework.bean;

/**
 *表单参数
 * @author Administrator
 *
 */
public class FormParam {
	
	private String fieldName;
	private Object fieldValue;
	
	
	public FormParam(String fieldName,Object fieldValue){
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}


	public String getFieldName() {
		return fieldName;
	}


	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}


	public Object getFieldValue() {
		return fieldValue;
	}


	public void setFieldValue(Object fieldValue) {
		this.fieldValue = fieldValue;
	}
	
}
