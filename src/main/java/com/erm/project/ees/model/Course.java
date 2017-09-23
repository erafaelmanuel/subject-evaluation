package com.erm.project.ees.model;

public class Course {

    private long id;
    private String name;
    private String desc;

    public Course() {
        super();
    }

    public Course(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public Course(long id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
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

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
