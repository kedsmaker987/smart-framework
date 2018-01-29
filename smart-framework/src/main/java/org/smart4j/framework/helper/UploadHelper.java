package org.smart4j.framework.helper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.bean.FileParam;
import org.smart4j.framework.bean.FormParam;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.util.StreamUtil;

/**
 * 文件上传助手
 * @author Administrator
 *
 */
public class UploadHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(UploadHelper.class);
	
	/**
	 * apache Commons FileUpload 提供 servlet 文件上传对象
	 */
	private static ServletFileUpload servletFileUpload;

	
	/**
	 * 初始化上传工具
	 * @param servletContext
	 */
	public static void init(ServletContext servletContext){
		File repository = (File)servletContext.getAttribute("javax.servlet.context.tempdir");
		servletFileUpload = new ServletFileUpload(new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, repository));
		int uploadLimit = ConfigHelper.getAppUploadLimit();
		if(uploadLimit!=0){
			servletFileUpload.setFileSizeMax(uploadLimit*1024*1024);
		}
	}
	
	/**
	 * 请求是否是multipart 类型
	 * @param request
	 * @return
	 */
	public static boolean isMultipart(HttpServletRequest request){
		return ServletFileUpload.isMultipartContent(request);
	}
	
	/**
	 * 创建请求对象
	 * @param request
	 * @return
	 */
	public static Param createParam(HttpServletRequest request){
		List<FormParam> formParamList = new ArrayList<FormParam>();
		List<FileParam> fileParamList = new ArrayList<FileParam>();
		
		try{
			Map<String, List<FileItem>> fileItemListMap = servletFileUpload.parseParameterMap(request);
			if(MapUtils.isNotEmpty(fileItemListMap)){
				for(Map.Entry<String, List<FileItem>> fileItemListEntry:fileItemListMap.entrySet()){
					String fieldName = fileItemListEntry.getKey();
					List<FileItem> fileItemList = fileItemListEntry.getValue();
					if(CollectionUtils.isNotEmpty(fileItemList)){
						for(FileItem fileItem:fileItemList){
							// 表单元素
							if(fileItem.isFormField()){
								String fieldValue = fileItem.getString("UTF-8");
								formParamList.add(new FormParam(fieldName, fieldValue));
							}else{
								String fileName = FilenameUtils.getName(new String(fileItem.getName().getBytes(),"UTF-8"));
								if(StringUtils.isNotEmpty(fileName)){
									long fileSize = fileItem.getSize();
									String contentType = fileItem.getContentType();
									InputStream inputStream = fileItem.getInputStream();
									fileParamList.add(new FileParam(fieldName,fileName,fileSize,contentType,inputStream));
								}
							}
						}
					}
				}
			}
		}catch(Exception e){
			logger.error("create param failure ",e);
			throw new RuntimeException(e);
		}
		return new Param(formParamList,fileParamList);
	}
	
	/**
	 * 上传文件
	 * @param basePath
	 * @param fileParam
	 */
	public static void upLoadFile(String basePath,FileParam fileParam){
		try{
			if(fileParam !=null){
				String filePath = basePath + fileParam.getFileName();
				// 创建文件和父级目录
				createFile(filePath);
				InputStream inputStream = new BufferedInputStream(fileParam.getInputStream());
				OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
				StreamUtil.copyStream(inputStream, outputStream);
			}
		}catch(Exception e){
			logger.error("upload file failure ",e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 批量上传
	 * @param basePath
	 * @param fileParamList
	 */
	public static void upLoadFile(String basePath,List<FileParam> fileParamList){
		try{
			if(CollectionUtils.isNotEmpty(fileParamList)){
				for(FileParam fileParam:fileParamList){
					upLoadFile(basePath,fileParam);
				}
			}
		}catch(Exception e){
			logger.error("upload file failure ",e);
			throw new RuntimeException(e);
		}
	}
	
	
	private static File createFile(String filePath){
		File file;
		try{
			file = new File(filePath);
			File parentDir = file.getParentFile();
			if(!parentDir.exists()){
				FileUtils.forceMkdir(parentDir);
			}
		}catch(Exception e){
			logger.error("create file failure ",e);
			throw new RuntimeException(e);
		}
		
		return file;
	}
	
	
	
}
