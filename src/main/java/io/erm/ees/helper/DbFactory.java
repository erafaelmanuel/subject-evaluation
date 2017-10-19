package io.erm.ees.helper;

import io.erm.ees.dao.impl.v2.DbSubject;

public class DbFactory {

    private static DbSubject dbSubject;

    public static void  addSubjectFactory(DbSubject dbSubject) {
        DbFactory.dbSubject=dbSubject;
    }

    public static DbSubject subjectFactory(){
        return dbSubject;
    }
}
