package com.erm.project.ees.dao;

import com.erm.project.ees.model.Curriculum;

import java.util.List;

public interface CurriculumDao {

    Curriculum getCurriculumById(long id);
    Curriculum getCurriculum(String query);
    List<Curriculum> getCurriculumList();
    List<Curriculum> getCurriculumList(String query);
    boolean addCurriculum(Curriculum curriculum);
    boolean updateCurriculumById(long id, Curriculum curriculum);
    boolean updateCurriculum(String query, Curriculum curriculum);
    boolean deleteCurriculumById(long id);
    boolean deleteCurriculum(String query);
}
