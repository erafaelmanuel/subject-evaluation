package io.ermdev.ees.legacy.model;

public class Subject {

    private long id;
    private String name;
    private String desc;
    private int unit;
    private int unitLecture;
    private int unitLaboratory;
    private String unitDisplay;

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

    public Subject(String name, String desc, int unit, int unitLecture, int unitLaboratory, String unitDisplay) {
        this.name = name;
        this.desc = desc;
        this.unit = unit;
        this.unitLecture = unitLecture;
        this.unitLaboratory = unitLaboratory;
        this.unitDisplay = unitDisplay;
    }

    public Subject(long id, String name, String desc, int unit, int unitLecture, int unitLaboratory,
                   String unitDisplay) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.unit = unit;
        this.unitLecture = unitLecture;
        this.unitLaboratory = unitLaboratory;
        this.unitDisplay = unitDisplay;
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
        return unit = unitLecture + unitLaboratory;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public int getUnitLecture() {
        return unitLecture;
    }

    public void setUnitLecture(int unitLecture) {
        this.unitLecture = unitLecture;
    }

    public int getUnitLaboratory() {
        return unitLaboratory;
    }

    public void setUnitLaboratory(int unitLaboratory) {
        this.unitLaboratory = unitLaboratory;
    }

    public String getUnitDisplay() {
        if(unitLecture == 0 && unitLaboratory == 0)
            return unitDisplay = "0";
        if(unitLaboratory == 0)
            return unitDisplay = unitLecture + "";
        unitDisplay = unitLecture + "/" + unitLaboratory;
        return unitDisplay;
    }

    public void setUnitDisplay(String unitDisplay) {
        this.unitDisplay = unitDisplay;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", unit=" + getUnit() +
                ", unitLecture=" + unitLecture +
                ", unitLaboratory=" + unitLaboratory +
                ", unitDisplay='" + getUnitDisplay() + '\'' +
                '}';
    }
}
