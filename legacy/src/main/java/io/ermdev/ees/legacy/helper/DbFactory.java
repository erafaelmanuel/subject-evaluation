package io.ermdev.ees.legacy.helper;

import io.ermdev.ees.legacy.dao.impl.v2.*;

public class DbFactory {

    private static DbSubject dbSubject;
    private static DbCreditSubject dbCreditSubject;
    private static DbAcademicYear dbAcademicYear;
    private static DbCourse dbCourse;
    private static DbStudent dbStudent;

    public static void  addSubjectFactory(DbSubject dbSubject) {
        DbFactory.dbSubject=dbSubject;
    }

    public static DbSubject subjectFactory(){
        return dbSubject;
    }

    public static void  addCreditSubjectFactory(DbCreditSubject dbCreditSubject) {
        DbFactory.dbCreditSubject=dbCreditSubject;
    }

    public static DbCreditSubject creditSubjectFactory(){
        return dbCreditSubject;
    }

    public static void  addAcademicYearFactory(DbAcademicYear dbAcademicYear) {
        DbFactory.dbAcademicYear=dbAcademicYear;
    }

    public static DbAcademicYear academicYearFactory(){
        return dbAcademicYear;
    }

    public static void  addCourseFactory(DbCourse dbCourse) {
        DbFactory.dbCourse=dbCourse;
    }

    public static DbCourse courseFactory(){
        return dbCourse;
    }

    public static void  addStudentFactory(DbStudent dbStudent) {
        DbFactory.dbStudent=dbStudent;
    }

    public static DbStudent studentFactory(){
        return dbStudent;
    }
}
