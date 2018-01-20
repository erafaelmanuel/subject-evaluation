package io.ermdev.ees.legacy.dao;

import io.ermdev.ees.legacy.model.Student;

import java.util.List;

public interface StudentDao {

    String TABLE_NAME = "tblstudent";

    Student getStudentById(long id);

    Student getStudent(String query);

    List<Student> getStudentList();

    List<Student> getStudentList(String query);

    boolean addStudent(Student student);

    boolean updateStudentById(long id, Student student);

    boolean updateStudent(String query, Student student);

    boolean deleteStudentById(long id);

    boolean deleteStudent(String query);

}
