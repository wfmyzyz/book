package com.wfmyzyz.book.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Miss.Mo
 * @since 2019-10-08
 */
public class Book implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 书籍ID
     */
    @TableId(value = "book_id", type = IdType.AUTO)
    private Integer bookId;

    /**
     * 书籍名称
     */
    @NotBlank(message = "书籍名称不能为空")
    private String name;

    /**
     * 书籍封面
     */
    private String headImage;

    /**
     * 简介
     */
    @NotBlank(message = "书籍简介不能为空")
    private String introduce;

    /**
     * 说明
     */
    @NotBlank(message = "书籍说明不能为空")
    private String bookExplain;

    /**
     * 作者
     */
    private String author;

    /**
     * 发布人
     */
    private String publiser;

    /**
     * 浏览人数
     */
    private String browse;

    /**
     * 点赞人数
     */
    private String praise;

    /**
     * 审核状态
     */
    private String bookCheck;

    /**
     * 类型
     */
    private String bookType;

    /**
     * 上传地址
     */
    private String uploadUrl;

    /**
     * 书籍状态
     */
    private String bookStatus;

    /**
     * 书籍状态
     */
    private Integer serialNum;

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
     * 状态：正常，删除
     */
    private String tbStatus;


    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getBookExplain() {
        return bookExplain;
    }

    public void setBookExplain(String bookExplain) {
        this.bookExplain = bookExplain;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPubliser() {
        return publiser;
    }

    public void setPubliser(String publiser) {
        this.publiser = publiser;
    }

    public String getBrowse() {
        return browse;
    }

    public void setBrowse(String browse) {
        this.browse = browse;
    }

    public String getPraise() {
        return praise;
    }

    public void setPraise(String praise) {
        this.praise = praise;
    }

    public String getBookCheck() {
        return bookCheck;
    }

    public void setBookCheck(String bookCheck) {
        this.bookCheck = bookCheck;
    }

    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(String bookStatus) {
        this.bookStatus = bookStatus;
    }

    public Integer getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(Integer serialNum) {
        this.serialNum = serialNum;
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
        return "Book{" +
        "bookId=" + bookId +
        ", name=" + name +
        ", headImage=" + headImage +
        ", introduce=" + introduce +
        ", bookExplain=" + bookExplain +
        ", author=" + author +
        ", publiser=" + publiser +
        ", browse=" + browse +
        ", praise=" + praise +
        ", bookCheck=" + bookCheck +
        ", bookType=" + bookType +
        ", uploadUrl=" + uploadUrl +
        ", bookStatus=" + bookStatus +
        ", serialNum=" + serialNum +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", tbStatus=" + tbStatus +
        "}";
    }
}
