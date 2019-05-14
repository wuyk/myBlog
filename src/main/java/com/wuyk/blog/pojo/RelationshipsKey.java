package com.wuyk.blog.pojo;

public class RelationshipsKey {
    private Integer cid;

    private Integer mid;

    public RelationshipsKey(Integer cid, Integer mid) {
        this.cid = cid;
        this.mid = mid;
    }

    public RelationshipsKey() {
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