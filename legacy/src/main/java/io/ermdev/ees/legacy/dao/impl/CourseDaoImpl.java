package io.ermdev.ees.legacy.dao.impl;

import io.ermdev.ees.legacy.dao.CourseDao;
import io.ermdev.ees.legacy.dao.conn.DbManager;
import io.ermdev.ees.legacy.dao.conn.DbUserLibrary;
import io.ermdev.ees.legacy.dao.exception.NoResultFoundException;
import io.ermdev.ees.legacy.model.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Deprecated
public class CourseDaoImpl implements CourseDao {

    protected static final Logger LOGGER = Logger.getLogger(CourseDaoImpl.class.getSimpleName());
    private DbManager dbManager;

    public CourseDaoImpl() {
        dbManager = new DbManager();
        init();
    }

    public CourseDaoImpl(DbManager dbManager) {
        this.dbManager = dbManager;
        init();
    }

    public CourseDaoImpl(DbUserLibrary dbUserLibrary) {
        dbManager = new DbManager(dbUserLibrary);
        init();
    }

    public void init() {
        Connection connection = null;
        try {
            if (dbManager.connect()) {
                String sql = "CREATE TABLE IF NOT EXISTS "
                        .concat(TABLE_NAME)
                        .concat("(")
                        .concat("id bigint PRIMARY KEY AUTO_INCREMENT,")
                        .concat("_name varchar(100),")
                        .concat("_desc int,")
                        .concat("_totalYear int,")
                        .concat("_totalSemester int);");

                //SQL INFO
                LOGGER.info("SQL : " + sql);

                connection = dbManager.getConnection();
                PreparedStatement pst = connection.prepareStatement(sql);
                pst.executeUpdate();
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.info("SQLException");
            dbManager.close();
        }
    }

    @Override
    public Course getCourseById(long id) {
        try {
            Course course = null;
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ? LIMIT 1;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, id);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    course = new Course();
                    course.setId(rs.getLong(1));
                    course.setName(rs.getString(2));
                    course.setDesc(rs.getString(3));
                    course.setTotalYear(rs.getInt(4));
                    course.setTotalSemester(rs.getInt(5));

                    dbManager.close();
                    return course;
                }
            }
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException e) {
            LOGGER.info("Connection error");
            dbManager.close();
            return null;
        } catch (NoResultFoundException e) {
            LOGGER.info("NoResultFoundException");
            dbManager.close();
            return null;
        }
    }

    @Override
    public Course getCourse(String query) {
        try {
            Course course = null;
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
                    course = new Course();
                    course.setId(rs.getLong(1));
                    course.setName(rs.getString(2));
                    course.setDesc(rs.getString(3));
                    course.setTotalYear(rs.getInt(4));
                    course.setTotalSemester(rs.getInt(5));

                    dbManager.close();
                    return course;
                }
            }
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.info("Connection error");
            dbManager.close();
            return null;
        } catch (NoResultFoundException e) {
            LOGGER.info("NoResultFoundException");
            dbManager.close();
            return null;
        }
    }

    @Override
    public List<Course> getCourseList() {
        List<Course> courseList = new ArrayList<>();
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT * FROM " + TABLE_NAME + ";";

                PreparedStatement pst = connection.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    Course course = new Course();
                    course.setId(rs.getLong(1));
                    course.setName(rs.getString(2));
                    course.setDesc(rs.getString(3));
                    course.setTotalYear(rs.getInt(4));
                    course.setTotalSemester(rs.getInt(5));
                    courseList.add(course);
                }
                dbManager.close();
                return courseList;
            }
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.info("Connection error");
            dbManager.close();
            return courseList;
        } catch (NoResultFoundException e) {
            LOGGER.info("NoResultFoundException");
            dbManager.close();
            return courseList;
        }
    }

    @Override
    public List<Course> getCourseList(String query) {
        List<Course> courseList = new ArrayList<>();
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
                    Course course = new Course();
                    course.setId(rs.getLong(1));
                    course.setName(rs.getString(2));
                    course.setDesc(rs.getString(3));
                    course.setTotalYear(rs.getInt(4));
                    course.setTotalSemester(rs.getInt(5));
                    courseList.add(course);
                }
                dbManager.close();
                return courseList;
            }
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.info("Connection error");
            dbManager.close();
            return courseList;
        } catch (NoResultFoundException e) {
            LOGGER.info("NoResultFoundException");
            dbManager.close();
            return courseList;
        }
    }

    @Override
    public Course addCourse(Course course) {
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                long id = generate();
                String sql = "INSERT INTO " + TABLE_NAME + "(id, _name, _desc, _totalYear, _totalSemester) VALUES (?, ?, ?, ?, ?);";
                PreparedStatement pst = connection.prepareStatement(sql);

                course.setId(id);
                pst.setLong(1, id);
                pst.setString(2, course.getName());
                pst.setString(3, course.getDesc());
                pst.setInt(4, course.getTotalYear());
                pst.setInt(5, course.getTotalSemester());
                pst.executeUpdate();
            }
            dbManager.close();
            return course;
        } catch (SQLException e) {
            e.printStackTrace();
            dbManager.close();
            return null;
        }
    }

    @Override
    public boolean updateCourseById(long id, Course course) {
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "UPDATE " + TABLE_NAME + " SET id=?, _name=?, _desc=?, _totalYear=?, _totalSemester=? WHERE id = ?;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, course.getId());
                pst.setString(2, course.getName());
                pst.setString(3, course.getDesc());
                pst.setInt(4, course.getTotalYear());
                pst.setInt(5, course.getTotalSemester());
                pst.setLong(6, id);
                pst.executeUpdate();
            }
            dbManager.getConnection().close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            dbManager.close();
            return false;
        }
    }

    @Override
    public boolean updateCourse(String query, Course course) {
        return false;
    }

    @Override
    public boolean deleteCourseById(long id) {
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();

                String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, id);
                pst.executeUpdate();
            }
            dbManager.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            dbManager.close();
            return false;
        }
    }

    @Override
    public boolean deleteCourse(String query) {
        return false;
    }

    long generate() {
        return (long) (Math.random() * Long.MAX_VALUE);
    }
}
