package com.erm.project.ees.model;

public class Subject {

    private long id;
    private String name;
    private String desc;
    private int unit;

    public Subject() {
        super();
    }

    public Subject(String name, String desc, int unit) {
        this.name = name;
        this.desc = desc;
        this.unit = unit;
    }

    public Subject(long id, String name, String desc, int unit) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.unit = unit;
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

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", unit=" + unit +
                '}';
    }
}
