package com.wfmyzyz.book.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.sun.deploy.net.HttpUtils;
import com.wfmyzyz.book.config.ProjectConfig;
import com.wfmyzyz.book.controller.api.BackLoginApiComponent;
import com.wfmyzyz.book.utils.Msg;
import com.wfmyzyz.book.utils.RequestUtils;
import com.wfmyzyz.book.vo.UserBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import sun.net.www.http.HttpClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class ControllerBackInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ControllerBackInterceptor.class);

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private BackLoginApiComponent backLoginApiComponent;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        String requestURI = request.getRequestURI();
        logger.info("Back访问:IP地址["+ RequestUtils.getIpAddr(request)+"],访问路径["+request.getRequestURL()+"],匹配的路径["+requestURI+"]");
        if (request.getMethod().equals("OPTIONS")){
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        String token = request.getHeader(ProjectConfig.TOKEN_KEY);
        return backLoginApiComponent.checkUserAuthority(token,requestURI,response);
    }
}
