package com.erm.project.ees.dao.impl;

import com.erm.project.ees.dao.StudentDao;
import com.erm.project.ees.dao.conn.DBManager;
import com.erm.project.ees.dao.conn.UserLibrary;
import com.erm.project.ees.dao.exception.NoResultFoundException;
import com.erm.project.ees.model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class StudentDaoImpl implements StudentDao {

    protected static final Logger LOGGER = Logger.getLogger(StudentDaoImpl.class.getSimpleName());
    protected static final String TABLE_NAME = "tblstudent";

    private DBManager dbManager;


    public StudentDaoImpl() {
        dbManager = new DBManager();
        SectionDaoImpl sectionDao = new SectionDaoImpl();
        sectionDao.init();
        init();
    }

    public StudentDaoImpl(DBManager dbManager) {
        this();
        this.dbManager = dbManager;
        init();
    }

    public StudentDaoImpl(UserLibrary userLibrary) {
        this();
        dbManager = new DBManager(userLibrary);
        init();
    }

    private void init() {
        Connection connection = null;
        try {
            if (dbManager.connect()) {
                String sql = "CREATE TABLE IF NOT EXISTS "
                        .concat(TABLE_NAME)
                        .concat("(")
                        .concat("id bigint PRIMARY KEY AUTO_INCREMENT,")
                        .concat("studentNumber bigint,")
                        .concat("firstName varchar(100),")
                        .concat("lastName varchar(100),")
                        .concat("middleName varchar(100),")
                        .concat("age int,")
                        .concat("gender varchar(100),")
                        .concat("contactNumber int,")
                        .concat("sectionId bigint,")
                        .concat("courseId bigint,")
                        .concat("FOREIGN KEY (sectionId) REFERENCES tblsection(id),")
                        .concat("FOREIGN KEY (courseId) REFERENCES tblcourse(id));");


                //SQL INFO
                LOGGER.info("SQL : " + sql);

                connection = dbManager.getConnection();
                PreparedStatement pst = connection.prepareStatement(sql);
                pst.executeUpdate();
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.info("SQLException");
        }
    }

    @Override
    public Student getStudentById(long id) {
        try {
            Student student = null;
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ? LIMIT 1;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, id);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    student = new Student();
                    student.setId(rs.getLong(1));
                    student.setStudentNumber(rs.getLong(2));
                    student.setFirstName(rs.getString(3));
                    student.setLastName(rs.getString(4));
                    student.setMiddleName(rs.getString(5));
                    student.setAge(rs.getInt(6));
                    student.setGender(rs.getString(7));
                    student.setContactNumber(rs.getInt(8));
                    student.setSectionId(rs.getLong(9));
                    student.setCourseId(rs.getLong(10));
                    return student;
                }
            }
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException e) {
            LOGGER.info("Connection error");
            return null;
        } catch (NoResultFoundException e) {
            LOGGER.info("NoResultFoundException");
            return null;
        }
    }

    @Override
    public Student getStudent(String query) {
        try {
            Student student = null;
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT * FROM "
                        .concat(TABLE_NAME)
                        .concat(" ")
                        .concat(query.replace(";", " "))
                        .concat("LIMIT 1;");
                PreparedStatement pst = connection.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    student = new Student();
                    student.setId(rs.getLong(1));
                    student.setStudentNumber(rs.getLong(2));
                    student.setFirstName(rs.getString(3));
                    student.setLastName(rs.getString(4));
                    student.setMiddleName(rs.getString(5));
                    student.setAge(rs.getInt(6));
                    student.setGender(rs.getString(7));
                    student.setContactNumber(rs.getInt(8));
                    student.setSectionId(rs.getLong(9));
                    student.setCourseId(rs.getLong(10));
                    return student;
                }
            }
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.info("Connection error");
            return null;
        } catch (NoResultFoundException e) {
            LOGGER.info("NoResultFoundException");
            return null;
        }
    }

    @Override
    public List<Student> getStudentList() {
        List<Student> studentList = new ArrayList<>();
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT * FROM " + TABLE_NAME + ";";

                PreparedStatement pst = connection.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getLong(1));
                    student.setStudentNumber(rs.getLong(2));
                    student.setFirstName(rs.getString(3));
                    student.setLastName(rs.getString(4));
                    student.setMiddleName(rs.getString(5));
                    student.setAge(rs.getInt(6));
                    student.setGender(rs.getString(7));
                    student.setContactNumber(rs.getInt(8));
                    student.setSectionId(rs.getLong(9));
                    student.setCourseId(rs.getLong(10));
                    studentList.add(student);
                }
                return studentList;
            }
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.info("Connection error");
            return studentList;
        } catch (NoResultFoundException e) {
            LOGGER.info("NoResultFoundException");
            return studentList;
        }
    }

    @Override
    public List<Student> getStudentList(String query) {
        List<Student> studentList = new ArrayList<>();
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT * FROM "
                        .concat(TABLE_NAME)
                        .concat(" ")
                        .concat(query.replace(";", " "));

                PreparedStatement pst = connection.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getLong(1));
                    student.setStudentNumber(rs.getLong(2));
                    student.setFirstName(rs.getString(3));
                    student.setLastName(rs.getString(4));
                    student.setMiddleName(rs.getString(5));
                    student.setAge(rs.getInt(6));
                    student.setGender(rs.getString(7));
                    student.setContactNumber(rs.getInt(8));
                    student.setSectionId(rs.getLong(9));
                    student.setCourseId(rs.getLong(10));
                    studentList.add(student);
                }
                return studentList;
            }
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.info("Connection error");
            return studentList;
        } catch (NoResultFoundException e) {
            LOGGER.info("NoResultFoundException");
            return studentList;
        }
    }

    @Override
    public boolean addStudent(Student student) {
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();

                String sql = "INSERT INTO " + TABLE_NAME + "(id, studentNumber, firstName, lastName, middleName, " +
                        "age, gender, contactNumber, sectionId, courseId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, student.getId());
                pst.setLong(2, student.getStudentNumber());
                pst.setString(3, student.getFirstName());
                pst.setString(4, student.getLastName());
                pst.setString(5, student.getMiddleName());
                pst.setInt(6, student.getAge());
                pst.setString(7, student.getGender());
                pst.setInt(8, student.getContactNumber());
                pst.setLong(9, student.getSectionId());
                pst.setLong(10, student.getCourseId());
                pst.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateStudentById(long id, Student student) {
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "UPDATE " + TABLE_NAME + " SET firstName=?, lastName=?, middleName=?, age = ?, " +
                        "gender = ?, contactNumber=?, sectionId=?, courseId=? WHERE id = ? OR studentNumber = ?";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setString(1, student.getFirstName());
                pst.setString(2, student.getLastName());
                pst.setString(3, student.getMiddleName());
                pst.setInt(4, student.getAge());
                pst.setString(5, student.getGender());
                pst.setInt(6, student.getContactNumber());
                pst.setLong(7, student.getSectionId());
                pst.setLong(8, id);
                pst.setLong(9, student.getStudentNumber());
                pst.setLong(10, student.getCourseId());
                pst.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateStudent(String query, Student student) {
        return false;
    }

    @Override
    public boolean deleteStudentById(long id) {
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();

                String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, id);
                pst.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteStudent(String query) {
        return false;
    }
}
