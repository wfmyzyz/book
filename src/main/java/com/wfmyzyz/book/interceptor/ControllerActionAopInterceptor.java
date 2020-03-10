package com.wfmyzyz.book.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.wfmyzyz.book.config.ProjectConfig;
import com.wfmyzyz.book.domain.Action;
import com.wfmyzyz.book.service.IActionService;
import com.wfmyzyz.book.utils.RequestUtils;
import com.wfmyzyz.book.vo.UserBo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author admin
 */
@Component
@Aspect
@Slf4j
public class ControllerActionAopInterceptor {

    @Autowired
    private IActionService actionService;

    /**
     * 切入点表达式
     */
    @Pointcut("execution(public * com.wfmyzyz.book.*.controller.back.*.*(..))")
    public void privilege(){
    }

    @Around("privilege()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        log.info("动作日志开始---");
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        ApiOperation annotation = method.getAnnotation(ApiOperation.class);
        String value = annotation.value();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(ProjectConfig.TOKEN_KEY);
        Object[] args = pjp.getArgs();
        String[] parameterNameArgs = ((MethodSignature) pjp.getSignature()).getParameterNames();
        StringBuffer paramsBuf = new StringBuffer();
        for (int i=0;i < args.length; i++){
            if (paramsBuf.length() > 0){
                paramsBuf.append("|");
            }
            paramsBuf.append(parameterNameArgs[i]).append(" = ").append(args[i]);
        }
        UserBo user = (UserBo) request.getAttribute("user");
        Action action = new Action();
        action.setTitle(value);
        action.setToken(token);
        action.setUserId(user.getUserId());
        action.setUsername(user.getUsername());
        action.setParams(paramsBuf.toString());
        action.setIp(RequestUtils.getIpAddr(request));
        Object result = pjp.proceed();
        action.setResult(JSONObject.toJSONString(result));
        actionService.save(action);
        log.info("动作日志结束---");
        return result;
    }
}
