package org.smart4j.framework.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.smart4j.framework.util.CastUtil;

/**
 * 请求参数对象
 * @author Administrator
 *
 */
public class Param {
	
	private List<FormParam> formParamList;
	private List<FileParam> fileParamList;
	
	public Param(List<FormParam> formParamList){
		this.formParamList = formParamList;
	}
	
	public Param(List<FormParam> formParamList,List<FileParam> fileParamList){
		this.formParamList = formParamList;
		this.fileParamList = fileParamList;
	}
	
	/**
	 * 请求参数的映射
	 * @return
	 */
	public Map<String,Object> getFieldMap(){
		Map<String,Object> fieldMap = new HashMap<String,Object>();
		if(CollectionUtils.isNotEmpty(formParamList)){
			for(FormParam fileParam:formParamList){
				String fieldName = fileParam.getFieldName();
				Object fieldValue = fileParam.getFieldValue();
				if(fieldMap.containsKey(fieldName)){
					fieldValue = fieldMap.get(fieldName)+String.valueOf((char) 29)+fieldValue;
				}
				fieldMap.put(fieldName, fieldValue);
			}
		}
		return fieldMap;
	}
	
	/**
	 * 上传文件的映射
	 * @return
	 */
	public Map<String,List<FileParam>> getFileMap(){
		Map<String,List<FileParam>> fileMap = new HashMap<String,List<FileParam>>();
		if(CollectionUtils.isNotEmpty(fileParamList)){
			for(FileParam fileParam: fileParamList){
				String fieldName = fileParam.getFieldName();
				List<FileParam> fileParamList;
				if(fileMap.containsKey(fieldName)){
					fileParamList = fileMap.get(fieldName);
				}else{
					fileParamList = new ArrayList<FileParam>();
				}
				
				fileParamList.add(fileParam);
				fileMap.put(fieldName, fileParamList);
			}
		}
		return fileMap;
	}
	
	
	/**
	 * 获取上传文件
	 * @param fieldName
	 * @return
	 */
	public List<FileParam> getFileList(String fieldName){
		return getFileMap().get(fieldName);
	}
	
	
	/**
	 * 获取唯一上传文件
	 * @param fieldName
	 * @return
	 */
	public FileParam getFile(String fieldName){
		List<FileParam> fileParamList = getFileList(fieldName);
		if(CollectionUtils.isNotEmpty(fileParamList) && fileParamList.size() == 1){
			return fileParamList.get(0);
		}
		return null;
	}
	
	/**
	 * 验证参数是否非空
	 * @return
	 */
	public boolean isEmpty(){
		return CollectionUtils.isEmpty(formParamList) && CollectionUtils.isEmpty(fileParamList);
	}
	
	/**
	 * 获取string 的参数类型
	 * @param name
	 * @return
	 */
	public String getString(String name){
		return CastUtil.castString(getFieldMap().get(name));
	}
	
	public double getDouble(String name){
		return CastUtil.castDouble(getFieldMap().get(name));
	}
	
	public long getLong(String name){
		return CastUtil.castLong(getFieldMap().get(name));
	}
	
	public int getInt(String name){
		return CastUtil.castInt(name);
	}
	
	public boolean getBoolean(String name){
		return CastUtil.castBoolean(name);
	}
}
