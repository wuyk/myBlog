package com.wuyk.blog.pojo;

public class LogsDo {
    private Integer id;

    private String action;

    private String data;

    private Integer authorId;

    private String ip;

    private Integer created;

    public LogsDo(Integer id, String action, String data, Integer authorId, String ip, Integer created) {
        this.id = id;
        this.action = action;
        this.data = data;
        this.authorId = authorId;
        this.ip = ip;
        this.created = created;
    }

    public LogsDo() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action == null ? null : action.trim();
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data == null ? null : data.trim();
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }
}