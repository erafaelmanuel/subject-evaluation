package io.erm.ees.model.v2;

public class Record {

    private long id;
    private double midterm;
    private double finalterm;
    private String date;
    private String remark;
    private long subjectId;
    private long academicId;
    private long studentId;

    public Record() {
        super();
    }

    public Record(long id, double midterm, double finalterm, String date, String remark, long subjectId,
                  long academicId, long studentId) {
        this.id = id;
        this.midterm = midterm;
        this.finalterm = finalterm;
        this.date = date;
        this.remark = remark;
        this.subjectId = subjectId;
        this.academicId = academicId;
        this.studentId = studentId;
    }

    public Record(double midterm, double finalterm, String date, String remark, long subjectId, long academicId,
                  long studentId) {
        this.midterm = midterm;
        this.finalterm = finalterm;
        this.date = date;
        this.remark = remark;
        this.subjectId = subjectId;
        this.academicId = academicId;
        this.studentId = studentId;
    }

    public Record(double midterm, double finalterm, String date, String remark) {
        this.midterm = midterm;
        this.finalterm = finalterm;
        this.date = date;
        this.remark = remark;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getMidterm() {
        return midterm;
    }

    public void setMidterm(double midterm) {
        this.midterm = midterm;
    }

    public double getFinalterm() {
        return finalterm;
    }

    public void setFinalterm(double finalterm) {
        this.finalterm = finalterm;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public long getAcademicId() {
        return academicId;
    }

    public void setAcademicId(long academicId) {
        this.academicId = academicId;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", midterm=" + midterm +
                ", finalterm=" + finalterm +
                ", date='" + date + '\'' +
                ", remark='" + remark + '\'' +
                ", subjectId=" + subjectId +
                ", academicListId=" + academicId +
                ", studentId=" + studentId +
                '}';
    }
}
