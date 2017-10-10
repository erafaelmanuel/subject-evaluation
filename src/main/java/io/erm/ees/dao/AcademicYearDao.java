package io.erm.ees.dao;

import io.erm.ees.model.v2.AcademicYear;

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

    List<AcademicYear> getAcademicYearList();

    List<AcademicYear> getAcademicYearList(long studentId);

    List<AcademicYear> getAcademicYearList(long code, int semester);

    List<AcademicYear> getAcademicYearListOpen(long courseId);

    AcademicYear addAcademicYear(long courseId, AcademicYear academicYear);

    void deleteAcademicYearById(long id);

    void statusOpen(long code, int semester);

    void statusClose(long code, int semester);

    int currentSemesterOpen(long courseId);
}
