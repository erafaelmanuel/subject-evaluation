package io.ermdev.ees.legacy.dao;

import io.ermdev.ees.legacy.model.StudentSubjectRecord;
import io.ermdev.ees.legacy.model.Subject;

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

    boolean deletePrerequisite(long subjectId, long toSubjectId);

    boolean deletePrerequisite(long subjectId);

    boolean addPrerequisite(long subjectId, long toSubjectId);

    boolean updateStudentRecord(long subjectId, long studentId, double midterm, double finalterm, String mark);
}
