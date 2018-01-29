package org.smart4j.framework;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.smart4j.framework.bean.Data;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.bean.View;
import org.smart4j.framework.helper.BeanHelper;
import org.smart4j.framework.helper.ConfigHelper;
import org.smart4j.framework.helper.ControllerHelper;
import org.smart4j.framework.helper.HelperLoader;
import org.smart4j.framework.helper.RequestHelper;
import org.smart4j.framework.helper.UploadHelper;
import org.smart4j.framework.util.CodecUtil;
import org.smart4j.framework.util.JsonUtil;
import org.smart4j.framework.util.ReflectionUtil;
import org.smart4j.framework.util.StreamUtil;


@WebServlet(urlPatterns="/*",loadOnStartup=0)
public class DispatcherServlet extends HttpServlet{
	
	@Override
	public void init(ServletConfig servletConfig){
		// 初始化相关类
		HelperLoader.init();
		// 获取 servletContext 用于注册 servlet
		ServletContext servletContext = servletConfig.getServletContext();
		// 注册 jsp servlet
		ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
		jspServlet.addMapping(ConfigHelper.getApppJspPath()+"*");
		// 注册静态资源默认servlet
		ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
		defaultServlet.addMapping(ConfigHelper.getAppAssetPath()+"*");
		
		UploadHelper.init(servletContext);
	}
	
	
	public void service(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException{
		// 获取请求方法和路径
		String requestMethod = request.getMethod().toLowerCase();
		String requestPath = request.getPathInfo();
		
		if(requestPath.equals("/favicon.ico")){
			return;
		}
		
		// 获取Action  处理器
		Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
		if(handler!=null){
			Class<?> controllerClass = handler.getControllerClass();
			// 获取 controller的实例对象
			Object controllerBean = BeanHelper.getBean(controllerClass);
			
			Param param;
			if(UploadHelper.isMultipart(request)){
				param = UploadHelper.createParam(request);
			}else{
				param = RequestHelper.createParam(request);
			}
			// 调用action 方法
			Object result;
			Method actionMethod = handler.getActionMethod();
			if(param.isEmpty()){
				result = ReflectionUtil.invokeMethod(controllerBean, actionMethod);
			}else{
				result= ReflectionUtil.invokeMethod(controllerBean, actionMethod, param);
			}
			
			// 处理action 的返回值
			if(result instanceof View){// 返回视图
				handleViewResult(request, response, (View)result);
			}else if(result instanceof Data){// 返回json 数据
				handleDataResult(response, (Data)result);
			}
		}
	}


	private void handleDataResult(HttpServletResponse response, Object result) throws IOException {
		Data data = (Data) result;
		Object model = data.getModel();
		if(model !=null){
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter writer = response.getWriter();
			String json = JsonUtil.toJson(model);
			writer.write(json);
			writer.flush();
			writer.close();
		}
	}


	private void handleViewResult(HttpServletRequest request, HttpServletResponse response, Object result)
			throws IOException, ServletException {
		View view = (View) result;
		String path = view.getPath();// jsp页面地址
		if(StringUtils.isNotEmpty(path)){
			if(path.startsWith("/")){
				// 相对路径且没有带参数
				response.sendRedirect(request.getContextPath()+path);
			}else{
				// 带参数的处理方法
				Map<String,Object> model = view.getModel();
				for(Map.Entry<String, Object> entry: model.entrySet()){
					request.setAttribute(entry.getKey(), entry.getValue());
				}
				
				request.getRequestDispatcher(ConfigHelper.getApppJspPath()+path).forward(request, response);
			}
		}
	}
}
