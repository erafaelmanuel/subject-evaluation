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

    List<AcademicYear> getAcademicYearList(long studentId);

    void addAcademicYear(long courseId, AcademicYear academicYear);

    void deleteAcademicYearById(long id);

    boolean statusOpen(long id);

    boolean statusClose(long id);
}
