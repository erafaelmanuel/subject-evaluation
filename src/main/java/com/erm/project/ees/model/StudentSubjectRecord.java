package com.erm.project.ees.model;

public class StudentSubjectRecord {

    private long subjectId;
    private String subjectName;
    private String subjectDesc;
    private String date;
    private double midterm;
    private double finalterm;
    private String mark;

    public StudentSubjectRecord() {
        super();
    }

    public StudentSubjectRecord(String subjectName, String subjectDesc, String date, double midterm, double finalterm,
                                String mark) {
        this.subjectName = subjectName;
        this.subjectDesc = subjectDesc;
        this.date = date;
        this.midterm = midterm;
        this.finalterm = finalterm;
        this.mark = mark;
    }

    public StudentSubjectRecord(long subjectId, String subjectName, String subjectDesc, String date, double midterm,
                                double finalterm, String mark) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.subjectDesc = subjectDesc;
        this.date = date;
        this.midterm = midterm;
        this.finalterm = finalterm;
        this.mark = mark;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectDesc() {
        return subjectDesc;
    }

    public void setSubjectDesc(String subjectDesc) {
        this.subjectDesc = subjectDesc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    @Override
    public String toString() {
        return "StudentSubjectRecord{" +
                "subjectName=" + subjectName +
                ", subjectDesc=" + subjectDesc +
                ", date='" + date + '\'' +
                ", midterm=" + midterm +
                ", finalterm=" + finalterm +
                ", mark='" + mark + '\'' +
                '}';
    }
}
