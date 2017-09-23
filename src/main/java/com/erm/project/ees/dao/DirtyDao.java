package com.erm.project.ees.dao;

import com.erm.project.ees.model.StudentSubjectRecord;

import java.util.List;

public interface DirtyDao {

    List<StudentSubjectRecord> getStudentSubjectRecords(long courseId, long studentId, int year, int semester);
}
