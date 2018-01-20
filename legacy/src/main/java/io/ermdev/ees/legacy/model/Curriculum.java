package io.ermdev.ees.legacy.model;

public class Curriculum {

    private long id;
    private int year;
    private int semester;
    private long courseId;

    public Curriculum() {
        super();
    }

    public Curriculum(int year, int semester, long courseId) {
        this.year = year;
        this.semester = semester;
        this.courseId = courseId;
    }

    public Curriculum(long id, int year, int semester, long courseId) {
        this.id = id;
        this.year = year;
        this.semester = semester;
        this.courseId = courseId;
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
}
