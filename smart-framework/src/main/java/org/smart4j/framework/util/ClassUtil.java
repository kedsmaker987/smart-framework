package org.smart4j.framework.util;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * 这是一个类加载器 加载类的作用
 * @author Administrator
 *
 */
public final class ClassUtil {
	private static final Logger logger = LoggerFactory.getLogger(ClassUtil.class);
	
	/**
	 * 获取类加载器
	 * @return
	 */
	public static ClassLoader getClassLoader(){
		return Thread.currentThread().getContextClassLoader();
	}
	
	/**
	 * 加载类  通过 类名 Class.forName 获得所有的
	 * @param className
	 * @param isInitialialized
	 * @return
	 */
	public static Class<?> loadClass(String className,boolean isInitialialized){
		Class<?> cls;
		try{
			cls = Class.forName(className,isInitialialized,getClassLoader());
		}catch(Exception e){
			logger.error("load class failure ",e);
			throw new RuntimeException(e);
		}
		return cls;
	}
	
	
	/**
	 * 加载指定包下的所有类
	 * @param packageName
	 * Set 是没有重复
	 */
	public static Set<Class<?>> getClassSet(String packageName) {
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		try {
			// 类加载器中 url 地址 所有包含 packageName 的地址
			Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				if (url != null) {
					// protocol 文件类型
					String protocol = url.getProtocol();
					// file: 包括 .class 文件 目录
					if (protocol.equals("file")) {
						String packagePath = url.getPath().replaceAll("%20", "");// %20 为空格
						addClass(classSet, packagePath, packageName);
					} else if (protocol.equals("jar")) {// jar
						JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
						if (jarURLConnection != null) {
							JarFile jarFile = jarURLConnection.getJarFile();
							if (jarFile != null) {
								Enumeration<JarEntry> jarEntries = jarFile.entries();
								while (jarEntries.hasMoreElements()) {
									JarEntry jarEntry = jarEntries.nextElement();
									String jarEntryName = jarEntry.getName();
									if (jarEntryName.endsWith(".class")) {
										String className = jarEntryName.substring(0, jarEntryName.lastIndexOf("."))
												.replaceAll("/", ".");
										doAddClass(classSet, className);
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("get class set failure ",e);
			throw new RuntimeException(e);
		}
		logger.info("laad classSet :"+JSONObject.toJSONString(classSet));
		return classSet;
	}
	
	/**
	 * file.getName() 1. 文件 xxx.后缀      2. 目录: 目录名称
	 * @param classSet
	 * @param packagePath 指定包下的文件或者目录 /xxx/xxx/xxx
	 * @param packageName 指定的包 com.kejian.xxx
	 */
	private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
		// 过滤条件 .class 结尾,或者是 目录
		File[] files = new File(packagePath).listFiles(new FileFilter() {
			public boolean accept(File file) {
				return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
			}
		});

		for (File file : files) {
			// fileName ClassUtil.java 或目录名称
			String fileName = file.getName();
			if (file.isFile()) {// 联系上文 如果是.class文件
				// className: ClassUtil
				String className = fileName.substring(0, fileName.lastIndexOf("."));// 获取文件名
				if (StringUtils.isNotEmpty(packageName)) {
					// className : 包+ 名称
					className = packageName + "." + className;
				}
				doAddClass(classSet, className);
			} else {
				// 当前为目录 fileName 就是当前目录
				String subPackagePath = fileName;
				if(StringUtils.isNotEmpty(packagePath)){
					subPackagePath = packagePath + "/"+subPackagePath;
				}
				
				String subPackageName = fileName;
				if(StringUtils.isNotEmpty(packageName)){
					subPackageName = packageName + "."+subPackageName;
				}
				
				addClass(classSet,subPackagePath,subPackageName);
			}
		}
	}
	
	/**
	 * 将全路径添加到 classSet 中
	 * @param classSet
	 * @param className 文件全路径
	 */
	private static void doAddClass(Set<Class<?>> classSet, String className){
		Class<?> cls = loadClass(className,false);
		classSet.add(cls);
	}
}
