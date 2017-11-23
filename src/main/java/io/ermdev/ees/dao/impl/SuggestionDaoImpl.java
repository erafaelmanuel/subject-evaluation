package io.ermdev.ees.dao.impl;

import io.ermdev.ees.dao.SubjectDao;
import io.ermdev.ees.dao.SuggestionDao;
import io.ermdev.ees.dao.conn.DbManager;
import io.ermdev.ees.dao.exception.NoResultFoundException;
import io.ermdev.ees.helper.DbFactory;
import io.ermdev.ees.model.Subject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class SuggestionDaoImpl implements SuggestionDao {

    private DbManager dbManager;
    private final SubjectDao subjectDao = DbFactory.subjectFactory();

    public static final String TABLE_NAME = "tblsuggestion";
    public static final Logger LOGGER = Logger.getLogger(SuggestionDaoImpl.class.getSimpleName());

    public SuggestionDaoImpl() {
        dbManager = new DbManager();
    }


    @Override
    public Subject getSubject(long studentId, long subjectId) {
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT * FROM " + TABLE_NAME + " WHERE studentId=? AND subjectId=?;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, studentId);
                pst.setLong(2, subjectId);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    final long id = rs.getLong(3);
                    dbManager.close();

                    return subjectDao.getSubjectById(id);
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
    public List<Subject> getSubjectListByStudent(long studentId) {
        List<Subject> subjectList = new ArrayList<>();
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "SELECT SU.id, SU._name, SU._desc, SU._unit, SU._unitLecture, SU._unitLaboratory FROM " + TABLE_NAME + " " +
                        "as SG JOIN tblsubject as SU ON SG.subjectId=SU.id WHERE studentId=?;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, studentId);
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
    public Subject addSubject(long studentId, long subjectId) {
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();
                String sql = "INSERT INTO " + TABLE_NAME + "(studentId, subjectId, date) VALUES (?, ?, ?);";
                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, studentId);
                pst.setLong(2, subjectId);
                pst.setString(3, new Date().toString());
                pst.executeUpdate();
            }
            dbManager.close();
            return subjectDao.getSubjectById(subjectId);
        } catch (SQLException e) {
            e.printStackTrace();
            dbManager.close();
            return null;
        }
    }

    @Override
    public Subject removeSubject(long studentId, long subjectId) {
        try {
            if (dbManager.connect()) {
                Connection connection = dbManager.getConnection();

                String sql = "DELETE FROM " + TABLE_NAME + " WHERE studentId=? AND subjectId=?";
                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, studentId);
                pst.setLong(2, subjectId);
                pst.executeUpdate();
            }
            dbManager.close();
            return subjectDao.getSubjectById(subjectId);
        } catch (SQLException e) {
            e.printStackTrace();
            dbManager.close();
            return null;
        }
    }
}
