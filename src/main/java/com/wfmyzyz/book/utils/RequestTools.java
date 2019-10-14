package com.wfmyzyz.book.utils;

import com.wfmyzyz.book.domain.Label;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class RequestTools {
    public String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.contains(",")) {
            return ip.split(",")[0];
        } else {
            return ip;
        }
    }

    public static void main(String[] args) throws Exception {
//        List<Label> labelList = new ArrayList<>();
//        Label label1 = new Label();
//        label1.setName("测试");
//        label1.setIntroduce("123");
//        labelList.add(label1);
//        Label label2 = new Label();
//        label2.setName("测试");
//        label2.setIntroduce("456");
//        labelList.add(label2);
//        Label label3 = new Label();
//        label3.setName("文本");
//        label3.setIntroduce("789");
//        labelList.add(label3);
//        List<String> collect = labelList.stream().map(Label::getName).collect(Collectors.toList());
//        List<Label> asd = labelList.stream().filter(Label -> (Label.getName().equals("123"))).collect(Collectors.toList());
//        collect.forEach(e-> System.out.println(e));
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> amountMap = new HashMap<>();
        amountMap.put("betAmount", BigDecimal.ZERO);
        amountMap.put("wonAwount",BigDecimal.ZERO);
        map.put("wai1",amountMap);
        map.put("wai2",amountMap);
        System.out.println(map);
    }
}
