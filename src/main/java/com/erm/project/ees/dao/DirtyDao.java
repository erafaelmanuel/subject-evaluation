package com.erm.project.ees.dao;

import com.erm.project.ees.model.StudentSubjectRecord;
import com.erm.project.ees.model.Subject;

import java.util.List;

public interface DirtyDao {

    StudentSubjectRecord getStudentSubjectRecordById(long studentId, long subjectId);

    List<StudentSubjectRecord> getStudentSubjectRecords(long courseId, long studentId, int year, int semester);

    List<StudentSubjectRecord> getStudentSubjectRecordByMark(long studentId, String mark);

    List<Subject> getCurriculumSubjectList(long courseId, int year, int semester);

    List<Subject> getSpecialCurriculumSubjectList(long courseId, int year, int semester, String type);

    List<Subject> getPrerequisiteBySujectId(long subjectId);

    int getStudentSubjectRecordSemester(long studentId, String mark);

    int getCurrentSemByCourseId(long courseId);

    boolean addStudentRecord(StudentSubjectRecord record, long subjectId, long studentId);

    boolean deleteStudentRecord(long studentId, String mark);
}
