package io.erm.ees.dao;

import io.erm.ees.model.SpecialCurriculum;

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
