package io.erm.ees.dao;

import io.erm.ees.model.Subject;

import java.util.List;

public interface MajorSubjectDao {

    String TABLE_NAME = "tblmajorsubject";
    String COL_1 = "id";
    String COL_2 = "curriculumId";
    String COL_3 = "subjectId";

    void init();
    List<Subject> getSubjectList(long courseId, int year, int semester);
    List<Subject> getMinorSubjectList(long courseId, int year, int semester);
    void addSubject(long curriculumId, long subjectId);
    void removeSubject(long curriculumId, long subjectId);
    boolean isSubjectExist(long curriculumId, long subjectId);
    boolean isInCurriculum(long curriculumId, long subjectId);
}
