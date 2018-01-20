package io.ermdev.ees.legacy.model.v2;

import io.ermdev.ees.legacy.helper.DbFactory;

public class AcademicYear {

    private long id;
    private long code;
    private String name;
    private int semester;
    private int year;
    private boolean status;
    private long courseId;

    public AcademicYear() {
    }

    public AcademicYear(long id, long code, String name, int semester, int year, boolean status, long courseId) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.semester = semester;
        this.year = year;
        this.status = status;
        this.courseId = courseId;
    }

    public AcademicYear(long code, String name, int semester, int year, boolean status, long courseId) {
        this.code = code;
        this.name = name;
        this.semester = semester;
        this.year = year;
        this.status = status;
        this.courseId = courseId;
    }

    public AcademicYear(long code, String name, int semester, int year, boolean status) {
        this.code = code;
        this.name = name;
        this.semester = semester;
        this.year = year;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getDisplayStatus() {
       return status ? "Open":"Close";
    }

    public String getDisplayCourse() {
        return DbFactory.courseFactory().getCourseById(courseId).getName();
    }

    public int getStudents() {
        return 0;
    }

    @Override
    public String toString() {
        return "AcademicYear{" +
                "id=" + id +
                ", code=" + code +
                ", name='" + name + '\'' +
                ", semester=" + semester +
                ", year=" + year +
                ", status=" + status +
                ", courseId=" + courseId +
                '}';
    }
}
