package io.erm.ees.model;

public class CurriculumSubjectList {

    private long id;
    private long curriculumId;
    private long subjectId;

    public CurriculumSubjectList() {
        super();
    }

    public CurriculumSubjectList(long curriculumId, long subjectId) {
        this.curriculumId = curriculumId;
        this.subjectId = subjectId;
    }

    public CurriculumSubjectList(long id, long curriculumId, long subjectId) {
        this.id = id;
        this.curriculumId = curriculumId;
        this.subjectId = subjectId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCurriculumId() {
        return curriculumId;
    }

    public void setCurriculumId(long curriculumId) {
        this.curriculumId = curriculumId;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }
}
