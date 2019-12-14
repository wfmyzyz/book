package com.wfmyzyz.book.vo;

import com.wfmyzyz.book.domain.Label;

import java.io.Serializable;
import java.util.List;

/**
 * @author admin
 */
public class BookListVo implements Serializable {

    private static final long serialVersionUID=1L;

    private Integer bookId;

    private String name;

    private String headImage;

    private String bookExplain;

    private String author;

    private List<Label> labelList;

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

    public List<Label> getLabelList() {
        return labelList;
    }

    public void setLabelList(List<Label> labelList) {
        this.labelList = labelList;
    }

    @Override
    public String toString() {
        return "BookListVo{" +
                "bookId=" + bookId +
                ", name='" + name + '\'' +
                ", headImage='" + headImage + '\'' +
                ", bookExplain='" + bookExplain + '\'' +
                ", author='" + author + '\'' +
                ", labelList=" + labelList +
                '}';
    }
}
