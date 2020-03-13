package com.wfmyzyz.book.vo;

import com.wfmyzyz.book.domain.Book;
import com.wfmyzyz.book.domain.Label;

import java.io.Serializable;
import java.util.List;

/**
 * @author admin
 */
public class BookAboutVo implements Serializable {
    private static final long serialVersionUID=1L;

    private Book book;
    private List<Label> labelList;
    private boolean collect;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public List<Label> getLabelList() {
        return labelList;
    }

    public void setLabelList(List<Label> labelList) {
        this.labelList = labelList;
    }

    public boolean isCollect() {
        return collect;
    }

    public void setCollect(boolean collect) {
        this.collect = collect;
    }
}
