package com.erm.project.ees.dao;

import com.erm.project.ees.model.Curriculum;
import com.erm.project.ees.model.Subject;

import java.util.List;

public interface CurriculumDao {

    Curriculum getCurriculumById(long id);

    Curriculum getCurriculum(String query);

    List<Curriculum> getCurriculumList();

    List<Curriculum> getCurriculumList(String query);

    Curriculum addCurriculum(Curriculum curriculum);

    boolean updateCurriculumById(long id, Curriculum curriculum);

    boolean updateCurriculum(String query, Curriculum curriculum);

    boolean deleteCurriculumById(long id);

    boolean deleteCurriculum(String query);

    Subject addSubject(long curriculumId, long subjectId);

    Subject removeSubject(long curriculumId, long subjectId);

    boolean isSubjectExist(long curriculumId, long subjectId);

    List<Subject> getSubjectList(long curriculumId);

    boolean updateCurriculumCourseId(long id, long courseId);

    boolean deleteCurriculumByCourseId(long courseId);

    List<Curriculum> getCurriculumListByCourseId(long courseId);

    Curriculum getCurriculumListByCourseId(long courseId, int year, int semester);

    List<Subject> getSubjectList(long courseId, int year, int semester);
}
