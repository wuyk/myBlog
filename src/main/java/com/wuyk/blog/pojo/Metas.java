package com.wuyk.blog.pojo;

public class Metas {
    private Integer mid;

    private String name;

    private String slug;

    private String type;

    private String description;

    private Integer sort;

    private Integer parent;

    public Metas(Integer mid, String name, String slug, String type, String description, Integer sort, Integer parent) {
        this.mid = mid;
        this.name = name;
        this.slug = slug;
        this.type = type;
        this.description = description;
        this.sort = sort;
        this.parent = parent;
    }

    public Metas() {
        super();
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug == null ? null : slug.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }
}