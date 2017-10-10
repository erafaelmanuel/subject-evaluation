package io.erm.ees.dao;

import io.erm.ees.model.v2.Record;

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

    List<Record> getRecordList();

    List<Record> getRecordList(long academicId, long studentId);

    void addRecord(long subjectId, long academicId, long studentId, Record record);

    void updateRecordById(long id, Record record);

    void deleteRecordById(long id);

    boolean isSubjectPassed(long subjectId);

}
