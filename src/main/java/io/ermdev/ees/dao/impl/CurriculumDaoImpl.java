package io.ermdev.ees.dao.impl;

import io.ermdev.ees.dao.CurriculumDao;
import io.ermdev.ees.dao.conn.DbManager;
import io.ermdev.ees.dao.conn.DbUserLibrary;
import io.ermdev.ees.dao.exception.NoResultFoundException;
import io.ermdev.ees.model.Curriculum;
import io.ermdev.ees.model.Subject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CurriculumDaoImpl implements CurriculumDao {

    protected static final Logger LOGGER = Logger.getLogger(CurriculumDaoImpl.class.getSimpleName());
    protected static final String TABLE_NAME = "tblcurriculum";

    private DbManager dbManager;

    public CurriculumDaoImpl() {
        dbManager = new DbManager();
        init();
    }

    public CurriculumDaoImpl(DbManager dbManager) {
        this.dbManager = dbManager;
        init();
    }

    public CurriculumDaoImpl(DbUserLibrary dbUserLibrary) {
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
            dbManager.close();
        }
    }

    @Override
    public Curriculum getCurriculumById(long id) {
        try {
            Curriculum curriculum = null;
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ? LIMIT 1;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, id);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    curriculum = new Curriculum();
                    curriculum.setId(rs.getLong(1));
                    curriculum.setYear(rs.getInt(2));
                    curriculum.setSemester(rs.getInt(3));
                    curriculum.setCourseId(rs.getLong(4));

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
    public Curriculum getCurriculum(String query) {
        try {
            Curriculum curriculum = null;
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
                    curriculum = new Curriculum();
                    curriculum.setId(rs.getLong(1));
                    curriculum.setYear(rs.getInt(2));
                    curriculum.setSemester(rs.getInt(3));
                    curriculum.setCourseId(rs.getLong(4));

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
    public List<Curriculum> getCurriculumList() {
        List<Curriculum> curriculumList = new ArrayList<>();
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT * FROM " + TABLE_NAME + ";";

                PreparedStatement pst = connection.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    Curriculum curriculum = new Curriculum();
                    curriculum.setId(rs.getLong(1));
                    curriculum.setYear(rs.getInt(2));
                    curriculum.setSemester(rs.getInt(3));
                    curriculum.setCourseId(rs.getLong(4));
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
    public List<Curriculum> getCurriculumListByCourseId(long courseId) {
        List<Curriculum> curriculumList = new ArrayList<>();
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT * FROM " + TABLE_NAME + " WHERE course_id=?";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, courseId);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    Curriculum curriculum = new Curriculum();
                    curriculum.setId(rs.getLong(1));
                    curriculum.setYear(rs.getInt(2));
                    curriculum.setSemester(rs.getInt(3));
                    curriculum.setCourseId(rs.getLong(4));
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
    public Curriculum getCurriculum(long courseId, int year, int semester) {
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT * FROM " + TABLE_NAME + " WHERE course_id=? AND _year=? AND _semester=? LIMIT 1;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, courseId);
                pst.setInt(2, year);
                pst.setInt(3, semester);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    Curriculum curriculum = new Curriculum();
                    curriculum.setId(rs.getLong(1));
                    curriculum.setYear(rs.getInt(2));
                    curriculum.setSemester(rs.getInt(3));
                    curriculum.setCourseId(rs.getLong(4));

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
    public List<Subject> getSubjectList(long courseId, int year, int semester) {
        List<Subject> subjectList = new ArrayList<>();
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT TBL_SUB.id, TBL_SUB._name, TBL_SUB._desc, TBL_SUB._unit, TBL_SUB._unitLecture, TBL_SUB._unitLaboratory FROM tblcurriculumsubjectlist AS TBL_CCS JOIN tblsubject AS TBL_SUB ON TBL_CCS.subjectId = TBL_SUB.id JOIN tblcurriculum as TBL_C ON TBL_CCS.curriculumId = TBL_C.id WHERE TBL_C.course_id=? AND TBL_C._year=? AND TBL_C._semester=?;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, courseId);
                pst.setInt(2, year);
                pst.setInt(3, semester);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    Subject subject = new Subject();
                    subject.setId(rs.getLong(1));
                    subject.setName(rs.getString(2));
                    subject.setDesc(rs.getString(3));
                    subject.setUnit(rs.getInt(4));
                    subject.setUnitLecture(rs.getInt(5));
                    subject.setUnitLaboratory(rs.getInt(6));
                    subjectList.add(subject);
                }
                dbManager.close();
                return subjectList;
            }
            return new ArrayList<>();
        } catch (SQLException e) {
            e.printStackTrace();
            dbManager.close();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Curriculum> getCurriculumList(String query) {
        List<Curriculum> curriculumList = new ArrayList<>();
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
                    Curriculum curriculum = new Curriculum();
                    curriculum.setId(rs.getLong(1));
                    curriculum.setYear(rs.getInt(2));
                    curriculum.setSemester(rs.getInt(3));
                    curriculum.setCourseId(rs.getLong(4));
                    curriculumList.add(curriculum);
                }
                dbManager.close();
                return curriculumList;
            }
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException e) {
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
    public Curriculum addCurriculum(Curriculum curriculum) {
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                long id = generate();
                String sql = "INSERT INTO " + TABLE_NAME + "(id, _year, _semester, course_id) VALUES (?, ?, ?, ?);";
                PreparedStatement pst = connection.prepareStatement(sql);

                curriculum.setId(id);
                pst.setLong(1, id);
                pst.setInt(2, curriculum.getYear());
                pst.setInt(3, curriculum.getSemester());
                pst.setLong(4, curriculum.getCourseId());
                pst.executeUpdate();
            }
            dbManager.close();
            return curriculum;
        } catch (SQLException e) {
            dbManager.close();
            return null;
        }
    }

    @Override
    public boolean updateCurriculumById(long id, Curriculum curriculum) {
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "UPDATE " + TABLE_NAME + " SET _year=?, _semester=?, course_id=? WHERE id = ?;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setInt(1, curriculum.getYear());
                pst.setInt(2, curriculum.getSemester());
                pst.setLong(3, curriculum.getCourseId());
                pst.setLong(4, curriculum.getId());
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
    public boolean updateCurriculum(String query, Curriculum curriculum) {
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

    @Override
    public Subject addSubject(long curriculumId, long subjectId) {
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "INSERT INTO tblcurriculumsubjectlist (curriculumId, subjectId) VALUES (?, ?);";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, curriculumId);
                pst.setLong(2, subjectId);
                pst.executeUpdate();
                dbManager.close();
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            dbManager.close();
            return null;
        }
    }

    @Override
    public Subject removeSubject(long curriculumId, long subjectId) {
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "DELETE FROM tblcurriculumsubjectlist WHERE curriculumId=? AND subjectId=?;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, curriculumId);
                pst.setLong(2, subjectId);
                pst.executeUpdate();
                dbManager.close();
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            dbManager.close();
            return null;
        }
    }

    @Override
    public boolean isSubjectExist(long curriculumId, long subjectId) {
        try {
            int count = 0;
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT * FROM tblcurriculumsubjectlist WHERE curriculumId=? AND subjectId=?";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, curriculumId);
                pst.setLong(2, subjectId);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    count++;
                }
                dbManager.close();
            }
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            dbManager.close();
            return false;
        }
    }

    @Override
    public List<Subject> getSubjectList(long curriculumId) {
        List<Subject> subjectList = new ArrayList<>();
        try {
            int count = 0;
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT TBL_SUB.id, TBL_SUB._name, TBL_SUB._desc, TBL_SUB._unit, TBL_SUB._unitLecture, " +
                        "TBL_SUB._unitLaboratory FROM tblcurriculumsubjectlist AS TBL_CCS JOIN tblsubject AS TBL_SUB " +
                        "ON TBL_CCS.subjectId = TBL_SUB.id WHERE TBL_CCS.curriculumId=?";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, curriculumId);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    Subject subject = new Subject();
                    subject.setId(rs.getLong(1));
                    subject.setName(rs.getString(2));
                    subject.setDesc(rs.getString(3));
                    subject.setUnit(rs.getInt(4));
                    subject.setUnitLecture(rs.getInt(5));
                    subject.setUnitLaboratory(rs.getInt(6));
                    subjectList.add(subject);
                }
                dbManager.close();
                return subjectList;
            }
            return new ArrayList<>();
        } catch (SQLException e) {
            e.printStackTrace();
            dbManager.close();
            return new ArrayList<>();
        }
    }


    @Override
    public boolean deleteCurriculumByCourseId(long courseId) {
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();

                String sql = "DELETE FROM " + TABLE_NAME + " WHERE course_id=?";
                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, courseId);
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
    public boolean updateCurriculumCourseId(long id, long courseId) {
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "UPDATE " + TABLE_NAME + " SET course_id=? WHERE id=?;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, courseId);
                pst.setLong(2, id);
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

    long generate() {
        return (long) (Math.random() * Long.MAX_VALUE);
    }
}
