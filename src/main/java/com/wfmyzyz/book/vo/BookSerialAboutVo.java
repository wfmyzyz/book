package com.wfmyzyz.book.vo;

import java.io.Serializable;

/**
 * @author admin
 */
public class BookSerialAboutVo implements Serializable {

    private static final long serialVersionUID=1L;

    private String name;
    private Integer serialNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(Integer serialNum) {
        this.serialNum = serialNum;
    }
}
