package com.wfmyzyz.book.vo;

import java.io.Serializable;

public class UserBo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer userId;

    private String username;

    private boolean isAuthority;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAuthority() {
        return isAuthority;
    }

    public void setAuthority(boolean authority) {
        isAuthority = authority;
    }

    @Override
    public String toString() {
        return "UserBo{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", isAuthority=" + isAuthority +
                '}';
    }
}
