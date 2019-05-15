package com.wuyk.blog.pojo;

public class RelationshipsDoKey {
    private Integer cid;

    private Integer mid;

    public RelationshipsDoKey(Integer cid, Integer mid) {
        this.cid = cid;
        this.mid = mid;
    }

    public RelationshipsDoKey() {
        super();
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }
}