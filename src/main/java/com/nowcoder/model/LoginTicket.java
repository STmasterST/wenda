package com.nowcoder.model;

import java.util.Date;

/**
 * 用来保存用户登录过的信息
 * 用来关联一个用户
 * 必须设置有效期,隔一段时间必须重新登录
 */
public class LoginTicket {

    private int id;
    private int userId;
    private Date expired;//过期时间,有效期,默认设置为100天
    private int status;
    private String ticket;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}
