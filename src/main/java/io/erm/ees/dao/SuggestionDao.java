package io.erm.ees.dao;

import io.erm.ees.model.Subject;

import java.util.List;

public interface SuggestionDao {

    Subject getSubject(long studentId, long subjectId);
    List<Subject> getSubjectListByStudent(long id);
    Subject addSubject(long studentId, long subjectId);
    Subject removeSubject(long studentId, long subjectId);
}