package io.erm.ees.dao.impl;

import io.erm.ees.dao.SubjectDao;
import io.erm.ees.dao.conn.DBManager;
import io.erm.ees.dao.conn.UserLibrary;
import io.erm.ees.dao.exception.NoResultFoundException;
import io.erm.ees.model.Subject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SubjectDaoImpl implements SubjectDao {

    protected static final Logger LOGGER = Logger.getLogger(SubjectDaoImpl.class.getSimpleName());
    protected static final String TABLE_NAME = "tblsubject";

    private DBManager dbManager;

    public SubjectDaoImpl() {
        dbManager = new DBManager();
        init();
    }

    public SubjectDaoImpl(DBManager dbManager) {
        this.dbManager = dbManager;
        init();
    }

    public SubjectDaoImpl(UserLibrary userLibrary) {
        dbManager = new DBManager(userLibrary);
        init();
    }

    private void init() {
        try {
            if (dbManager.connect()) {
                String sql = "CREATE TABLE IF NOT EXISTS "
                        .concat(TABLE_NAME)
                        .concat("(")
                        .concat("id bigint PRIMARY KEY AUTO_INCREMENT,")
                        .concat("_name varchar(100),")
                        .concat("_desc varchar(200),")
                        .concat("_unit int")
                        .concat("_unitLecture int")
                        .concat("_unitLaboratory int);");

                PreparedStatement pst = dbManager.getConnection().prepareStatement(sql);
                pst.executeUpdate();

                dbManager.close();
            }
        } catch (SQLException e) {
            LOGGER.info("SQLException");
        }
    }

    @Override
    public Subject getSubjectById(long id) {
        try {
            Subject subject = null;
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ? LIMIT 1;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, id);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    subject = new Subject();
                    subject.setId(rs.getLong(1));
                    subject.setName(rs.getString(2));
                    subject.setDesc(rs.getString(3));
                    subject.setUnit(rs.getInt(4));
                    subject.setUnitLecture(rs.getInt(5));
                    subject.setUnitLaboratory(rs.getInt(6));
                    return subject;
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
    public Subject getSubject(String query) {
        try {
            Subject subject = null;
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
                    subject = new Subject();
                    subject.setId(rs.getLong(1));
                    subject.setName(rs.getString(2));
                    subject.setDesc(rs.getString(3));
                    subject.setUnit(rs.getInt(4));
                    subject.setUnitLecture(rs.getInt(5));
                    subject.setUnitLaboratory(rs.getInt(6));

                    dbManager.close();
                    return subject;
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
    public List<Subject> getSubjectList() {
        List<Subject> subjectList = new ArrayList<>();
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT * FROM " + TABLE_NAME + ";";

                PreparedStatement pst = connection.prepareStatement(sql);
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
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException e) {
            LOGGER.info("Connection error");
            dbManager.close();
            return subjectList;
        } catch (NoResultFoundException e) {
            LOGGER.info("NoResultFoundException");
            dbManager.close();
            return subjectList;
        }
    }

    @Override
    public List<Subject> getSubjectList(String query) {
        List<Subject> subjectList = new ArrayList<>();
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
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.info("Connection error");
            dbManager.close();
            return subjectList;
        } catch (NoResultFoundException e) {
            LOGGER.info("NoResultFoundException");
            dbManager.close();
            return subjectList;
        }
    }

    @Override
    public List<Subject> getSubjectListBySearch(String query) {
        List<Subject> subjectList = new ArrayList<>();
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id=? OR _unit=? OR _name LIKE ?;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, -1L);
                pst.setInt(2, -1);
                pst.setString(3, query + "%");

                if(query.matches("^[0-9]+$") && !query.matches("^[a-zA-Z]+$")) {
                    pst.setLong(1, Long.parseLong(query));
                    pst.setInt(2, Integer.parseInt(query));
                }
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
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException e) {
            LOGGER.info("Connection error");
            dbManager.close();
            return subjectList;
        } catch (NoResultFoundException e) {
            LOGGER.info("NoResultFoundException");
            dbManager.close();
            return subjectList;
        }
    }

    @Override
    public Subject addSubject(Subject subject) {
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                final long id = generate();
                String sql = "INSERT INTO " + TABLE_NAME + "(id, _name, _desc, _unit, _unitLecture, _unitLaboratory) " +
                        "VALUES (?, ?, ?, ?, ?, ?);";
                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, id);
                pst.setString(2, subject.getName());
                pst.setString(3, subject.getDesc());
                pst.setInt(4, subject.getUnit());
                pst.setInt(5, subject.getUnitLecture());
                pst.setInt(6, subject.getUnitLaboratory());
                pst.executeUpdate();

                dbManager.close();

                subject.setId(id);

                return subject;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            dbManager.close();
            return null;
        }
    }

    @Override
    public boolean updateSubjectById(long id, Subject subject) {
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "UPDATE " + TABLE_NAME + " SET _name=?, _desc=?, _unit=?, _unitLecture=?, _unitLaboratory=? " +
                        "WHERE id = ?;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setString(1, subject.getName());
                pst.setString(2, subject.getDesc());
                pst.setInt(3, subject.getUnit());
                pst.setInt(4, subject.getUnitLecture());
                pst.setInt(5, subject.getUnitLaboratory());
                pst.setLong(6, subject.getId());

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
    public boolean updateSubject(String query, Subject subject) {
        return false;
    }

    @Override
    public boolean deleteSubjectById(long id) {
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
            dbManager.close();
            return false;
        }
    }

    @Override
    public boolean deleteSubject(String query) {
        return false;
    }

    @Override
    public boolean isSubjectNameExist(String name) {
        try {
            Subject subject = null;
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT * FROM "+ TABLE_NAME +" WHERE _name=? LIMIT 1;";
                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setString(1, name);
                ResultSet rs = pst.executeQuery();

                final boolean result = rs.next();
                dbManager.close();
                return result;
            }
            throw new NoResultFoundException("No result found");
        } catch (SQLException | NoResultFoundException e) {
            LOGGER.info(e.getMessage());
            dbManager.close();
            return false;
        }
    }

    public long generate2() {
        return (long) (Math.random() * Long.MAX_VALUE);
    }

    public long generate() {
        String string = String.format("2017%05d", (short) (Math.random() * Short.MAX_VALUE));
        return Long.parseLong(string);
    }
}
