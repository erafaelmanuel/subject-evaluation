package io.ermdev.ees.legacy.dao.impl.v2;

import io.ermdev.ees.legacy.dao.CourseDao;
import io.ermdev.ees.legacy.dao.conn.DbManager;
import io.ermdev.ees.legacy.dao.exception.NoResultFoundException;
import io.ermdev.ees.legacy.model.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DbCourse implements CourseDao {

    protected static final Logger LOGGER = Logger.getLogger(DbCourse.class.getSimpleName());
    private DbManager dbManager;

    private boolean isConnectable = false;

    public DbCourse(DbManager dbManager) {
        this.dbManager=dbManager;
    }

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
                        .concat("_name varchar(100),")
                        .concat("_desc int,")
                        .concat("_totalYear int,")
                        .concat("_totalSemester int);");

                LOGGER.info("SQL : " + sql);

                PreparedStatement pst = dbManager.getConnection().prepareStatement(sql);
                pst.executeUpdate();
            }
        } catch (SQLException e) {
            LOGGER.info("SQLException");
        }
    }

    @Override
    public Course getCourseById(long id) {
        try {
            if (isConnectable) {
                String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ? LIMIT 1;";

                PreparedStatement pst = dbManager.getConnection().prepareStatement(sql);
                pst.setLong(1, id);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    Course course = new Course();
                    course.setId(rs.getLong(1));
                    course.setName(rs.getString(2));
                    course.setDesc(rs.getString(3));
                    course.setTotalYear(rs.getInt(4));
                    course.setTotalSemester(rs.getInt(5));
                    return course;
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
    public Course getCourse(String query) {
        try {
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
                    Course course = new Course();
                    course.setId(rs.getLong(1));
                    course.setName(rs.getString(2));
                    course.setDesc(rs.getString(3));
                    course.setTotalYear(rs.getInt(4));
                    course.setTotalSemester(rs.getInt(5));
                    return course;
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
    public List<Course> getCourseList() {
        List<Course> courseList = new ArrayList<>();
        try {
            if (isConnectable) {
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
                return courseList;
            }
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.info("Connection error");
            return courseList;
        } catch (NoResultFoundException e) {
            LOGGER.info("NoResultFoundException");
            return courseList;
        }
    }

    @Override
    public List<Course> getCourseList(String query) {
        List<Course> courseList = new ArrayList<>();
        try {
            if (isConnectable) {
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
                return courseList;
            }
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.info("Connection error");
            return courseList;
        } catch (NoResultFoundException e) {
            LOGGER.info("NoResultFoundException");
            return courseList;
        }
    }

    @Override
    public Course addCourse(Course course) {
        try {
            if (isConnectable) {
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
            return course;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean updateCourseById(long id, Course course) {
        try {
            if (isConnectable) {
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
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
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
    public boolean deleteCourse(String query) {
        return false;
    }

    long generate() {
        return (long) (Math.random() * Long.MAX_VALUE);
    }
}
