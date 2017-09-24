package com.erm.project.ees.dao;

import com.erm.project.ees.model.Course;

import java.util.List;

public interface CourseDao {

    Course getCourseById(long id);
    Course getCourse(String query);
    List<Course> getCourseList();
    List<Course> getCourseList(String query);
    boolean addCourse(Course course);
    boolean updateCourseById(long id, Course course);
    boolean updateCourse(String query, Course course);
    boolean deleteCourseById(long id);
    boolean deleteCourse(String query);
}
