package com.wfmyzyz.book.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

import java.util.Map;

/** 
* 
* @author 吴彬 的  autoWeb 自动代码 网址  www.wubin9.com 
* @data 2019年01月19日 16:05:04  
* 此文件由 www.wubin9.com 网站的自动代码 autoWeb 自动生成。 
* 可以根据需求随意修改，如果需要帮助，请联系 吴彬 
* 联系方式：QQ 810978593  微信  wubin0830bingo 邮箱 wubin5922@dingtalk.com 
*/ 

@Component("jsonResult")
public class JsonResult {
	
	private Integer statusCode;
	private String statusMsg;
	private String token;
	private Object data;
	

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusMsg() {
		return statusMsg;
	}

	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public String ok() {
		return this.ok("");
	}
	
	public String ok(Object data) {
		return this.ok("", data);
	}
	public String ok(String msg, Object data) {
		return this.base(200, msg, data);
	}
	
	public String parameterError(String msg, Map<String, Object> paramMap) {		
		return this.base(203, msg, paramMap);
	}

	public String error(int code, String msg) {
		return this.error(code, msg, "");
	}
	
	public String error(int code, String msg, Object data) {
		return this.base(code, msg, data);
	}
	
	public String noRecord() {
		return this.base(202, "没有记录 !!!");
	}
	
	public String notLoggedIn() {
		return this.base(201, " 请登录 !!!");
	}
	public String notLoggedIn(Object data) {
		return this.base(201, " 请登录 !!!", data);
	}
	
	public String noPower() {
		return this.base(206, "没有权限！！！");
	}
	
	private String base(int code, String msg) {
		return this.base(code, msg, "");
	}
	
	private String base(int code, String msg, Object data) {
		JsonResult jd = new JsonResult();
		jd.setStatusCode(code);
		jd.setStatusMsg(msg);
		jd.setData(data);
		return JSON.toJSONString(jd);
	}
	
}

