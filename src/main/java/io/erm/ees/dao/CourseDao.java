package io.erm.ees.dao;

import io.erm.ees.model.Course;

import java.util.List;

public interface CourseDao {

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
