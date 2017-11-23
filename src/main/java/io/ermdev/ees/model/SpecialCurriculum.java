package io.ermdev.ees.model;

public class SpecialCurriculum {

    private long id;
    private int year;
    private int semester;
    private long courseId;
    private String name;
    private String type;

    public SpecialCurriculum() {
        super();
    }

    public SpecialCurriculum(int year, int semester, long courseId) {
        this.year = year;
        this.semester = semester;
        this.courseId = courseId;
    }

    public SpecialCurriculum(long id, int year, int semester, long courseId) {
        this.id = id;
        this.year = year;
        this.semester = semester;
        this.courseId = courseId;
    }

    public SpecialCurriculum(long id, int year, int semester, long courseId, String name, String type) {
        this.id = id;
        this.year = year;
        this.semester = semester;
        this.courseId = courseId;
        this.name = name;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
