package com.erm.project.ees.dao;

import com.erm.project.ees.model.StudentSubjectRecord;
import com.erm.project.ees.model.Subject;

import java.util.List;

public interface DirtyDao {

    StudentSubjectRecord getStudentSubjectRecordById(long studentId, long subjectId);

    List<StudentSubjectRecord> getStudentSubjectRecords(long courseId, long studentId, int year, int semester);

    List<Subject> getCurriculumSubjectList(long courseId, int year, int semester);

    List<Subject> getPrerequisiteBySujectId(long subjectId);

    int getCurrentSemByCourseId(long courseId);
}
