package com.erm.project.ees.dao;

import com.erm.project.ees.model.Subject;

import java.util.List;

public interface SubjectDao {

    Subject getSubjectById(long id);
    Subject getSubject(String query);
    List<Subject> getSubjectList();
    List<Subject> getSubjectList(String query);
    Subject addSubject(Subject subject);
    boolean updateSubjectById(long id, Subject subject);
    boolean updateSubject(String query, Subject subject);
    boolean deleteSubjectById(long id);
    boolean deleteSubject(String query);
}
