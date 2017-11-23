package io.ermdev.ees.dao;

import io.ermdev.ees.model.v2.Record;

import java.util.List;

public interface CreditSubjectDao {

    String TABLE_NAME = "tblcreditsubject";
    String COL_1 = "id";
    String COL_2 = "midterm";
    String COL_3 = "finalterm";
    String COL_4 = "date";
    String COL_5 = "remark";
    String COL_6 = "subjectId";
    String COL_7 = "academicId";
    String COL_8 = "studentId";

    void init();

    Record getRecordById(long id);

    Record getRecordBySubjectId(long subjectId, long studentId);

    List<Record> getRecordList();

    List<Record> getRecordList(long studentId);

    List<Record> getRecordList(long academicId, long studentId);

    List<Record> getRecordListOfSubject(long subjectId, long studentId);

    void addRecord(long subjectId, long academicId, long studentId, Record record);

    void updateRecordById(long id, Record record);

    void deleteRecordById(long id);

    void deleteRecordByRemark(long subjectId, long studentId, String remark);

    boolean isSubjectPassed(long subjectId, long studentId);

    boolean isSubjectFailed(long subjectId, long studentId);

    boolean isSubjectIncomplete(long subjectId, long studentId);

    boolean isSubjectDropped(long subjectId, long studentId);

    boolean isSubjectNotSet(long subjectId, long studentId);

    boolean isSubjectNotPassed(long subjectId, long studentId);

    List<Record> getRecordListByMark(long studentId, String remark);

    boolean isSubjectDuplicated(long subjectId, long academicId, long studentId);

    boolean isTaken(long subjectId, long studentId, long courseId,  int year, int semester);

    boolean isTaken(long subjectId, long studentId, long courseId);

    void setSubject(long courseId);
}
