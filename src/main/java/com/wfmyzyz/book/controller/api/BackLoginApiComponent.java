package com.wfmyzyz.book.controller.api;

import com.alibaba.fastjson.JSONObject;
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

@Component
public class BackLoginApiComponent {

    @Autowired
    private RestTemplate restTemplate;

    public boolean checkUserAuthority(String token, String url, HttpServletResponse response){
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("token",token);
        paramMap.add("url",url);
        ResponseEntity<Msg> msgResponseEntity = restTemplate.postForEntity("http://127.0.0.1:9090/user/api/rest/userIsAuthority", paramMap, Msg.class);
        Msg body = msgResponseEntity.getBody();
        if (body.getCode() != 200){
            Msg.needBack(response,body.getCode(),body.getMsg());
            return false;
        }
        Map<String, Object> map = body.getMap();
        UserBo data = JSONObject.parseObject(JSONObject.toJSONString(map.get("data")),UserBo.class);
        if (!data.isAuthority()){
            Msg.needBack(response,208,"没有权限");
            return false;
        }
        return true;
    }
}
