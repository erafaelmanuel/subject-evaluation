package io.ermdev.ees.dao;

import io.ermdev.ees.model.Section;

import java.util.List;

public interface SectionDao {

    Section getSectionById(long id);

    Section getSection(String query);

    List<Section> getSectionList();

    List<Section> getSectionList(String query);

    Section addSection(Section section);

    boolean updateSectionById(long id, Section section);

    boolean updateSection(String query, Section section);

    boolean deleteSectionById(long id);

    boolean deleteSection(String query);
}