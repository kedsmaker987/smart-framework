package org.smart4j.framework.util;

import java.net.URLDecoder;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 编码解码工具
 * @author Administrator
 *
 */
public class CodecUtil {
	private static final Logger logger = LoggerFactory.getLogger(CodecUtil.class);
	
	/**
	 * URL 编码
	 * @param source
	 * @return
	 */
	public static String encodeURL(String source){
		String target;
		try{
			target = URLEncoder.encode(source,"UTF-8");
		}catch(Exception e){
			logger.error("encode url failure ",e);
			throw new RuntimeException(e);
		}
		
		return target;
	}
	
	/**
	 * url 解码
	 * @param source
	 * @return
	 */
	public static String decodeURL(String source){
		String target;
		
		try{
			target = URLDecoder.decode(source,"UTF-8");
		}catch(Exception e){
			logger.error("decode url failure ",e);
			throw new RuntimeException(e);
		}
		
		return target;
	}
}
