package com.wfmyzyz.book.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author admin
 */
public class SerialVo implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 章回ID
     */
    private Integer serialId;

    /**
     * 书籍ID
     */
    private Integer bookId;

    /**
     * 章回数
     */
    private Integer serialNum;

    /**
     * 章回名
     */
    private String title;

    /**
     * 章回内容
     */
    private String text;

    /**
     * 浏览人数
     */
    private String browse;

    /**
     * 审核状态:通过，未通过
     */
    private String serialCheck;

    /**
     * 书籍名
     */
    private String bookName;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 状态:正常，删除
     */
    private String tbStatus;

    public Integer getSerialId() {
        return serialId;
    }

    public void setSerialId(Integer serialId) {
        this.serialId = serialId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(Integer serialNum) {
        this.serialNum = serialNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getBrowse() {
        return browse;
    }

    public void setBrowse(String browse) {
        this.browse = browse;
    }

    public String getSerialCheck() {
        return serialCheck;
    }

    public void setSerialCheck(String serialCheck) {
        this.serialCheck = serialCheck;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getTbStatus() {
        return tbStatus;
    }

    public void setTbStatus(String tbStatus) {
        this.tbStatus = tbStatus;
    }
}
