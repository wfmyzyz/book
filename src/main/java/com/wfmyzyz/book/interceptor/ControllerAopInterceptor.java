package com.wfmyzyz.book.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Enumeration;

/**
 * 
 * @ClassName: ControllerAopInterceptor
 * @Description: 访问接口的参数以及返回结果的日志
 * @author B450-R5
 * @date 2018年11月23日
 *
 */
@Component // 将对象交由spring进行管理
@Aspect // 代表此类为一个切面类
public class ControllerAopInterceptor {
	/** 初始化日志打印 */
	public static final Logger log = LoggerFactory.getLogger("visitLog");

	// 切入点表达式
//	@Pointcut("execution(public * com.bingo.hkmarksix.*.controller.*.*.*(..))")
	@Pointcut("execution(public * com.wfmyzyz.book.controller.back.administrators.admin.*.*(..))")
	public void privilege() {
	}

	@Around("privilege()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod();
		RequestMapping annotation = method.getAnnotation(RequestMapping.class);

		// 获取类名
		String className = pjp.getTarget().getClass().getName();// pjp.getTarget().getClass().getSimpleName();
		// 获取执行的方法名称
		String methodName = pjp.getSignature().getName();
		// 获取参数名称
		String[] parameterNamesArgs = ((MethodSignature) pjp.getSignature()).getParameterNames();
		// 定义返回参数
		Object result = null;
		// 获取方法参数
		Object[] args = pjp.getArgs();
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		// 请求的URL
		String requestURL = request.getRequestURL().toString();
		String ip = getIpAddr(request);

		StringBuffer paramsBuf = new StringBuffer();
		// 获取请求参数集合并进行遍历拼接
		for (int i = 0; i < args.length; i++) {
			if (paramsBuf.length() > 0) {
				paramsBuf.append("|");
			}
			paramsBuf.append(parameterNamesArgs[i]).append(" = ").append(args[i]);
		}
		StringBuffer headerBuf = new StringBuffer();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			if (headerBuf.length() > 0) {
				headerBuf.append("|");
			}
			headerBuf.append(key).append("=").append(value);
		}

		// 打印请求参数参数
		// 记录开始时间
		long start = System.currentTimeMillis();

		log.info("请求| ip:{} | 请求接口:{} | 请求类:{} | 方法 :{} | 参数:{} | 请求header:{}|请求时间 :{}", ip, requestURL, className, methodName, paramsBuf.toString(),
				headerBuf.toString(), start);
		// 执行目标方法
		result = pjp.proceed();
		// 获取执行完的时间 打印返回报文
		log.info("返回| 请求接口:{} | 请求时间:{} | 处理时间:{} 毫秒 | 返回结果 :{}", requestURL, start, (System.currentTimeMillis() - start), result);
		return result;
	}

	/**
	 * @Title: getIpAddr
	 * @Description: 获取ip
	 * @param request
	 * @return
	 * @return String 返回类型
	 */
	public String getIpAddr(HttpServletRequest request) {
		String ipAddress = null;
		ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
		}

// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
			// = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		// 或者这样也行,对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
//return ipAddress!=null&&!"".equals(ipAddress)?ipAddress.split(",")[0]:null;
		return ipAddress;
	}
}

