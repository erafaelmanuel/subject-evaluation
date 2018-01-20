package io.ermdev.ees.legacy.dao;

import io.ermdev.ees.legacy.model.Course;

import java.util.List;

public interface CourseDao {

    String TABLE_NAME = "tblcourse";

    Course getCourseById(long id);

    Course getCourse(String query);

    List<Course> getCourseList();

    List<Course> getCourseList(String query);

    Course addCourse(Course course);

    boolean updateCourseById(long id, Course course);

    boolean updateCourse(String query, Course course);

    boolean deleteCourseById(long id);

    boolean deleteCourse(String query);
}
