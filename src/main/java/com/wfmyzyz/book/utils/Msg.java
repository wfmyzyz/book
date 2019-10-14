package com.wfmyzyz.book.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wfmyzyz.book.domain.Book;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Msg {
    private int code;
    private String msg;
    private Map<String,Object> map = new HashMap<>();

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public static Msg success(){
        Msg msg = new Msg();
        msg.setCode(200);
        msg.setMsg("成功！");
        return msg;
    }

    public static Msg error(){
        Msg msg = new Msg();
        msg.setCode(0);
        msg.setMsg("失败！");
        return msg;
    }

    public Msg add(String key,Object value){
        map.put(key,value);
        return this;
    }

    public static Msg resultError(BindingResult result){
        Msg msg = Msg.error();
        for (FieldError error:result.getFieldErrors()){
            msg.add(error.getField(),error.getDefaultMessage());
        }
        return msg;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", map=" + map +
                '}';
    }

}
