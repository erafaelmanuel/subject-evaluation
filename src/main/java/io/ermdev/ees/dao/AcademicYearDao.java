package io.ermdev.ees.dao;

import io.ermdev.ees.model.v2.AcademicYear;

import java.util.List;

public interface AcademicYearDao {

    String TABLE_NAME = "tblacademicyear";
    String COL_1 = "id";
    String COL_2 = "code";
    String COL_3 = "name";
    String COL_4 = "semester";
    String COL_5 = "year";
    String COL_6 = "status";
    String COL_7 = "courseId";

    void init();

    AcademicYear getAcademicYearOpen(long courseId);

    AcademicYear getAcademicYearOpen(long courseId, int year);

    List<AcademicYear> getAcademicYearList();

    List<AcademicYear> getAcademicYearList(long studentId);

    List<AcademicYear> getAcademicYearList(long studentId, long courseId);

    List<AcademicYear> getAcademicYearList(long code, int semester);

    List<AcademicYear> getAcademicYearListOpen();

    List<AcademicYear> getAcademicYearListOpen(long courseId);

    AcademicYear addAcademicYear(long courseId, AcademicYear academicYear);

    void deleteAcademicYearById(long id);

    void deleteAcademicYear(long code, long courseId, int semester);

    void statusOpen(long code, long courseId, int semester);

    void statusClose(long courseId);

    void statusClose(long code, long courseId, int semester);

    int currentSemesterOpen(long courseId);

    long currentCodeOpen(long courseId);

    boolean isTaken(long studentId, long code, int semester);

    boolean isAcademicYearIsExist(long code, long courseId);

    boolean isAcademicYearIsExist(long code, long courseId, int semester);
}