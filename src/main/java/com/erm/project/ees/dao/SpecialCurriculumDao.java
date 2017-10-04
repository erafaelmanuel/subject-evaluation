package com.erm.project.ees.dao;

import com.erm.project.ees.model.SpecialCurriculum;

import java.util.List;

public interface SpecialCurriculumDao {

    SpecialCurriculum getCurriculumById(long id);

    SpecialCurriculum getCurriculum(String query);

    List<SpecialCurriculum> getCurriculumList();

    List<SpecialCurriculum> getCurriculumList(String query);

    boolean addCurriculum(SpecialCurriculum curriculum);

    boolean updateCurriculumById(long id, SpecialCurriculum curriculum);

    boolean updateCurriculum(String query, SpecialCurriculum curriculum);

    boolean deleteCurriculumById(long id);

    boolean deleteCurriculum(String query);
}
