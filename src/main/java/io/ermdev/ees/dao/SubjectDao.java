package io.ermdev.ees.dao;

import io.ermdev.ees.model.Subject;

import java.util.List;

public interface SubjectDao {

    String TABLE_NAME = "tblsubject";

    Subject getSubjectById(long id);

    Subject getSubject(String query);

    List<Subject> getSubjectList();

    List<Subject> getSubjectList(String query);

    List<Subject> getSubjectListBySearch(String query);

    Subject addSubject(Subject subject);

    boolean updateSubjectById(long id, Subject subject);

    boolean updateSubject(String query, Subject subject);

    boolean deleteSubjectById(long id);

    boolean deleteSubject(String query);

    boolean isSubjectNameExist(String name);
}
