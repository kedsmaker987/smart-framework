package org.smart4j.framework.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.smart4j.framework.bean.FormParam;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.util.CodecUtil;
import org.smart4j.framework.util.StreamUtil;

public class RequestHelper {
	
	public static Param createParam(HttpServletRequest request) throws IOException{
		List<FormParam> formParamList = new ArrayList<FormParam>();
		formParamList.addAll(parseParameterNames(request));
		formParamList.addAll(parseInputStream(request));
		return new Param(formParamList);
	}

	/**
	 * 创建请求头参数
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	private static Collection<? extends FormParam> parseInputStream(HttpServletRequest request) throws IOException {
		List<FormParam> formParamList  = new ArrayList<FormParam>();
		String body = CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
		if(StringUtils.isNotEmpty(body)){
			String[] kvs = StringUtils.split(body, "&");
			if(ArrayUtils.isNotEmpty(kvs)){
				for(String kv:kvs){
					String[] array = StringUtils.split(kv, "=");
					if(ArrayUtils.isNotEmpty(array) && array.length==2){
						String fieldName = array[0];
						String fieldValue = array[1];
						formParamList.add(new FormParam(fieldName, fieldValue));
					}
				}
			}
		}
		return formParamList;
	}

	/**
	 * 创建表单请求参数
	 * @param request
	 * @return
	 */
	private static Collection<? extends FormParam> parseParameterNames(HttpServletRequest request) {
		List<FormParam> formParamList = new ArrayList<FormParam>();
		Enumeration<String> parameterNames = request.getParameterNames();
		while(parameterNames.hasMoreElements()){
			String fieldName = parameterNames.nextElement();
			String[] fieldValues = request.getParameterValues(fieldName);
			if(ArrayUtils.isNotEmpty(fieldValues)){
				Object fieldValue;
				if(fieldValues.length==1){
					fieldValue = fieldValues[0];
				}else{
					StringBuilder sb = new StringBuilder();
					for(int i=0;i<fieldValues.length;i++){
						sb.append(fieldValues[i]);
						if(i !=fieldValues.length-1){
							sb.append(String.valueOf((char) 29));
						}
					}
					fieldValue = sb.toString();
				}
				formParamList.add(new FormParam(fieldName, fieldValue));
			}
		}
		return formParamList;
		
	}
	
	
	
}
