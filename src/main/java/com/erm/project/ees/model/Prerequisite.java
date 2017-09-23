package com.erm.project.ees.model;

public class Prerequisite {

    private long id;
    private long subjectId;
    private long toSubjectId;

    public Prerequisite() {
        super();
    }

    public Prerequisite(long subjectId, long toSubjectId) {
        this.subjectId = subjectId;
        this.toSubjectId = toSubjectId;
    }

    public Prerequisite(long id, long subjectId, long toSubjectId) {
        this.id = id;
        this.subjectId = subjectId;
        this.toSubjectId = toSubjectId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public long getToSubjectId() {
        return toSubjectId;
    }

    public void setToSubjectId(long toSubjectId) {
        this.toSubjectId = toSubjectId;
    }
}
