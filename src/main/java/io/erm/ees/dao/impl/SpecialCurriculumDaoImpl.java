package io.erm.ees.dao.impl;

import io.erm.ees.dao.SpecialCurriculumDao;
import io.erm.ees.dao.conn.DbManager;
import io.erm.ees.dao.conn.DbUserLibrary;
import io.erm.ees.dao.exception.NoResultFoundException;
import io.erm.ees.model.SpecialCurriculum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SpecialCurriculumDaoImpl implements SpecialCurriculumDao {

    protected static final Logger LOGGER = Logger.getLogger(SpecialCurriculumDaoImpl.class.getSimpleName());
    protected static final String TABLE_NAME = "tblspecialcurriculum";

    private DbManager dbManager;

    public SpecialCurriculumDaoImpl() {
        dbManager = new DbManager();
        init();
    }

    public SpecialCurriculumDaoImpl(DbManager dbManager) {
        this.dbManager = dbManager;
        init();
    }

    public SpecialCurriculumDaoImpl(DbUserLibrary dbUserLibrary) {
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
                        .concat("_year int,")
                        .concat("_semester int,")
                        .concat("course_id bigint,")
                        .concat("_name varchar(100),")
                        .concat("_type varchar(100),")
                        .concat("FOREIGN KEY (course_id) REFERENCES tblcourse(id));");

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
    public SpecialCurriculum getCurriculumById(long id) {
        try {
            SpecialCurriculum curriculum = null;
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ? LIMIT 1;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, id);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    curriculum = new SpecialCurriculum();
                    curriculum.setId(rs.getLong(1));
                    curriculum.setYear(rs.getInt(2));
                    curriculum.setSemester(rs.getInt(3));
                    curriculum.setCourseId(rs.getLong(4));
                    curriculum.setName(rs.getString(5));
                    curriculum.setType(rs.getString(6));

                    dbManager.close();
                    return curriculum;
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
    public SpecialCurriculum getCurriculum(String query) {
        try {
            SpecialCurriculum curriculum = null;
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
                    curriculum = new SpecialCurriculum();
                    curriculum.setId(rs.getLong(1));
                    curriculum.setYear(rs.getInt(2));
                    curriculum.setSemester(rs.getInt(3));
                    curriculum.setCourseId(rs.getLong(4));
                    curriculum.setName(rs.getString(5));
                    curriculum.setType(rs.getString(6));

                    dbManager.close();
                    return curriculum;
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
    public List<SpecialCurriculum> getCurriculumList() {
        List<SpecialCurriculum> curriculumList = new ArrayList<>();
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT * FROM " + TABLE_NAME + ";";

                PreparedStatement pst = connection.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    SpecialCurriculum curriculum = new SpecialCurriculum();
                    curriculum.setId(rs.getLong(1));
                    curriculum.setYear(rs.getInt(2));
                    curriculum.setSemester(rs.getInt(3));
                    curriculum.setCourseId(rs.getLong(4));
                    curriculum.setName(rs.getString(5));
                    curriculum.setType(rs.getString(6));
                    curriculumList.add(curriculum);
                }
                dbManager.close();
                return curriculumList;
            }
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.info("Connection error");
            dbManager.close();
            return curriculumList;
        } catch (NoResultFoundException e) {
            LOGGER.info("NoResultFoundException");
            dbManager.close();
            return curriculumList;
        }
    }

    @Override
    public List<SpecialCurriculum> getCurriculumList(String query) {
        List<SpecialCurriculum> curriculumList = new ArrayList<>();
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT * FROM "
                        .concat(TABLE_NAME)
                        .concat(" ")
                        .concat(query);

                //SQL INFO
                LOGGER.info("SQL : " + sql);

                PreparedStatement pst = connection.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    SpecialCurriculum curriculum = new SpecialCurriculum();
                    curriculum.setId(rs.getLong(1));
                    curriculum.setYear(rs.getInt(2));
                    curriculum.setSemester(rs.getInt(3));
                    curriculum.setCourseId(rs.getLong(4));
                    curriculum.setName(rs.getString(5));
                    curriculum.setType(rs.getString(6));
                    curriculumList.add(curriculum);
                }
                dbManager.close();
                return curriculumList;
            }
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.info("Connection error");
            dbManager.close();
            return curriculumList;
        } catch (NoResultFoundException e) {
            LOGGER.info("NoResultFoundException");
            dbManager.close();
            return curriculumList;
        }
    }

    @Override
    public boolean addCurriculum(SpecialCurriculum curriculum) {
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();

                String sql = "INSERT INTO " + TABLE_NAME + "(id, _year, _semester, course_id, _name, _type) VALUES (?, ?, ?, ?, ?, ?);";
                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, curriculum.getId());
                pst.setInt(2, curriculum.getYear());
                pst.setInt(3, curriculum.getSemester());
                pst.setLong(4, curriculum.getCourseId());
                pst.setString(5, curriculum.getName());
                pst.setString(6, curriculum.getType());
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
    public boolean updateCurriculumById(long id, SpecialCurriculum curriculum) {
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "UPDATE " + TABLE_NAME + " SET _year=?, _semester=?, course_id=?, _name=?, year=? WHERE id = ?;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setInt(1, curriculum.getYear());
                pst.setInt(2, curriculum.getSemester());
                pst.setLong(3, curriculum.getCourseId());
                pst.setString(4, curriculum.getName());
                pst.setString(5, curriculum.getType());
                pst.setLong(6, curriculum.getId());
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
    public boolean updateCurriculum(String query, SpecialCurriculum curriculum) {
        return false;
    }

    @Override
    public boolean deleteCurriculumById(long id) {
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
    public boolean deleteCurriculum(String query) {
        return false;
    }
}
