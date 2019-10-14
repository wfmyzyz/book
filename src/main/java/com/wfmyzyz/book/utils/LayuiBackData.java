package com.wfmyzyz.book.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;

public class LayuiBackData {
    private int code;
    private String msg;
    private long count;
    private Object data;

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

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static LayuiBackData success(){
        LayuiBackData data = new LayuiBackData();
        data.setCode(200);
        data.setMsg("成功！");
        return data;
    }
    public static LayuiBackData error(){
        LayuiBackData data = new LayuiBackData();
        data.setCode(100);
        data.setMsg("失败！");
        return data;
    }

    public static LayuiBackData bringData(IPage<?> iPage){
        LayuiBackData backdata = new LayuiBackData();
        backdata.setMsg("成功！");
        backdata.setCode(0);
        backdata.setCount(iPage.getTotal());
        backdata.setData(iPage.getRecords());
        return backdata;
    }
}
