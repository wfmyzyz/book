package com.wfmyzyz.book.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Miss.Mo
 * @since 2019-11-20
 */
public class BookSerial implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 章回ID
     */
    @TableId(value = "serial_id", type = IdType.AUTO)
    private Integer serialId;

    /**
     * 书籍ID
     */
    @NotNull(message = "书籍ID不可为空！")
    private Integer bookId;

    /**
     * 章回数
     */
    @NotNull(message = "章回数不可为空！")
    private Integer serialNum;

    /**
     * 章回名
     */
    @NotBlank(message = "章回名不可为空！")
    private String title;

    /**
     * 章回内容
     */
    @NotBlank(message = "章回内容不可为空！")
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

    @Override
    public String toString() {
        return "BookSerial{" +
        "serialId=" + serialId +
        ", bookId=" + bookId +
        ", serialNum=" + serialNum +
        ", title=" + title +
        ", text=" + text +
        ", browse=" + browse +
        ", serialCheck=" + serialCheck +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", tbStatus=" + tbStatus +
        "}";
    }
}
