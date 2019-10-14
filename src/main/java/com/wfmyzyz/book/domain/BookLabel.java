package com.wfmyzyz.book.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Miss.Mo
 * @since 2019-09-12
 */
public class BookLabel implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 书籍标签ID
     */
    @TableId(value = "book_label_id", type = IdType.AUTO)
    private Integer bookLabelId;

    /**
     * 书籍ID
     */
    private Integer bookId;

    /**
     * 标签ID
     */
    private Integer labelId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 状态：正常，删除
     */
    private String tbStatus;


    public Integer getBookLabelId() {
        return bookLabelId;
    }

    public void setBookLabelId(Integer bookLabelId) {
        this.bookLabelId = bookLabelId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getLabelId() {
        return labelId;
    }

    public void setLabelId(Integer labelId) {
        this.labelId = labelId;
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
        return "BookLabel{" +
        "bookLabelId=" + bookLabelId +
        ", bookId=" + bookId +
        ", labelId=" + labelId +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", tbStatus=" + tbStatus +
        "}";
    }
}
