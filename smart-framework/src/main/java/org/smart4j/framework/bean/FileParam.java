package org.smart4j.framework.bean;

import java.io.InputStream;

/**
 * 封装上传文件参数
 * @author Administrator
 *
 */
public class FileParam {
	
	
	public FileParam(String fieldName, String fileName, long fileSize, String contentType, InputStream inputStream) {
		this.fieldName = fieldName;
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.contentType = contentType;
		this.inputStream = inputStream;
	}
	/**
	 * 文件表单字段名称
	 */
	private String fieldName;
	/**
	 * 上传文件名称
	 */
	private String fileName;
	private long fileSize;
	/**
	 * 文件类型
	 */
	private String contentType;
	private InputStream inputStream;
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	
}
