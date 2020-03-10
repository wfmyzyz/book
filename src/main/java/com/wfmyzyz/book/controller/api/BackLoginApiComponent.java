package com.wfmyzyz.book.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.wfmyzyz.book.config.ProjectConfig;
import com.wfmyzyz.book.utils.Msg;
import com.wfmyzyz.book.vo.UserBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author admin
 */
@Component
public class BackLoginApiComponent {

    @Autowired
    private RestTemplate restTemplate;

    public UserBo checkUserAuthority(String token, String url, HttpServletResponse response){
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("token",token);
        paramMap.add("url",url);
        ResponseEntity<Msg> msgResponseEntity = restTemplate.postForEntity(ProjectConfig.USER_PATH+"/api/rest/userIsAuthority", paramMap, Msg.class);
        Msg body = msgResponseEntity.getBody();
        if (body.getCode() != 200){
            Msg.needBack(response,body.getCode(),body.getMsg());
            return null;
        }
        Map<String, Object> map = body.getMap();
        UserBo userBo = JSONObject.parseObject(JSONObject.toJSONString(map.get("data")),UserBo.class);
        if (!userBo.isAuthority()){
            Msg.needBack(response,208,"没有权限");
            return null;
        }
        return userBo;
    }
}
