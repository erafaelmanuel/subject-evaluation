package io.erm.ees.dao.impl.v2;

import io.erm.ees.dao.StudentDao;
import io.erm.ees.dao.conn.DbManager;
import io.erm.ees.dao.exception.NoResultFoundException;
import io.erm.ees.model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

public class DbStudent implements StudentDao {
    public static final Logger LOGGER = Logger.getLogger(DbStudent.class.getSimpleName());
    private final DbManager dbManager = new DbManager();

    private boolean isConnectable = false;

    public void open() {
        isConnectable=dbManager.connect();
    }

    public void close() {
        dbManager.close();
        isConnectable=false;
    }

    public void init() {
        try {
            if (isConnectable) {
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
                        .concat("contactNumber bigint,")
                        .concat("sectionId bigint,")
                        .concat("courseId bigint,")
                        .concat("status varchar(100),")
                        .concat("FOREIGN KEY (sectionId) REFERENCES tblsection(id),")
                        .concat("FOREIGN KEY (courseId) REFERENCES tblcourse(id));");

                //SQL INFO
                LOGGER.info("SQL : " + sql);

                PreparedStatement pst = dbManager.getConnection().prepareStatement(sql);
                pst.executeUpdate();
            }
        } catch (SQLException e) {
            LOGGER.info("SQLException");
        }
    }

    @Override
    public Student getStudentById(long id) {
        try {
            Student student = null;
            if (isConnectable) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ? OR studentNumber=? LIMIT 1;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, id);
                pst.setLong(2, id);
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
                    student.setContactNumber(rs.getLong(8));
                    student.setSectionId(rs.getLong(9));
                    student.setCourseId(rs.getLong(10));
                    student.setStatus(rs.getString(11));
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
            if (isConnectable) {
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
                    student.setContactNumber(rs.getLong(8));
                    student.setSectionId(rs.getLong(9));
                    student.setCourseId(rs.getLong(10));
                    student.setStatus(rs.getString(11));
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
            if (isConnectable) {
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
                    student.setContactNumber(rs.getLong(8));
                    student.setSectionId(rs.getLong(9));
                    student.setCourseId(rs.getLong(10));
                    student.setStatus(rs.getString(11));
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
            if (isConnectable) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT * FROM "
                        .concat(TABLE_NAME)
                        .concat(" ")
                        .concat(query);

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
                    student.setContactNumber(rs.getLong(8));
                    student.setSectionId(rs.getLong(9));
                    student.setCourseId(rs.getLong(10));
                    student.setStatus(rs.getString(11));
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
            if (isConnectable) {
                Connection connection = dbManager.getConnection();

                String sql =
                        "INSERT INTO " + TABLE_NAME + "(id, studentNumber, firstName, lastName, middleName, age, " +
                                "gender, contactNumber, sectionId, courseId, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, generate());
                pst.setLong(2, generateStudentNumber());
                pst.setString(3, student.getFirstName());
                pst.setString(4, student.getLastName());
                pst.setString(5, student.getMiddleName());
                pst.setInt(6, student.getAge());
                pst.setString(7, student.getGender());
                pst.setLong(8, student.getContactNumber());
                pst.setLong(9, student.getSectionId());
                pst.setLong(10, student.getCourseId());
                pst.setString(11, student.getStatus());
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
            if (isConnectable) {
                Connection connection = dbManager.getConnection();
                String sql = "UPDATE " + TABLE_NAME + " SET firstName=?, lastName=?, middleName=?, age = ?, " +
                        "gender = ?, contactNumber=?, sectionId=?, courseId=?, status=? WHERE id = ? OR studentNumber = ?";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setString(1, student.getFirstName());
                pst.setString(2, student.getLastName());
                pst.setString(3, student.getMiddleName());
                pst.setInt(4, student.getAge());
                pst.setString(5, student.getGender());
                pst.setLong(6, student.getContactNumber());
                pst.setLong(7, student.getSectionId());
                pst.setLong(8, student.getCourseId());
                pst.setString(9, student.getStatus());
                pst.setLong(10, id);
                pst.setLong(11, student.getStudentNumber());
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
            if (isConnectable) {
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

    public long generateStudentNumber() {
        final int YEAR = Calendar.getInstance().get(Calendar.YEAR);
        final int MAX = 9999999;

        final long id = Long.parseLong(String.format(Locale.ENGLISH, "%d%07d", YEAR, (int) (Math.random() * MAX)));
        return id;
    }

    public long generate() {
        return (long) (Math.random() * Long.MAX_VALUE);
    }
}
