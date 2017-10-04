package com.erm.project.ees.model.v2;

public class Subject {

    private long id;
    private String name;
    private String desc;
    private String remark;
    private String suggest;

    public Subject() {
    }

    public Subject(String name, String desc, String remark, String suggest) {
        this.name = name;
        this.desc = desc;
        this.remark = remark;
        this.suggest = suggest;
    }

    public Subject(long id, String name, String desc, String remark, String suggest) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.remark = remark;
        this.suggest = suggest;
    }

    public Subject(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", remark='" + remark + '\'' +
                ", suggest='" + suggest + '\'' +
                '}';
    }
}
