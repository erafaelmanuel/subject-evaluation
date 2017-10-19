package io.erm.ees.dao.impl;

import io.erm.ees.dao.CreditSubjectDao;
import io.erm.ees.dao.conn.DBManager;
import io.erm.ees.dao.exception.NoResultFoundException;
import io.erm.ees.dao.exception.SubjectAlreadyPassedException;
import io.erm.ees.dao.exception.SubjectDuplicateException;
import io.erm.ees.model.v2.Record;
import io.erm.ees.model.v2.Remark;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CreditSubjectDaoImpl implements CreditSubjectDao {

    private final DBManager DB_MANAGER = new DBManager();

    private static final Logger LOGGER = Logger.getLogger(CreditSubjectDaoImpl.class.getSimpleName());

    public CreditSubjectDaoImpl() {
        init();
    }

    @Override
    public void init() {
        try {
            if (DB_MANAGER.connect()) {
                String sql = "CREATE TABLE IF NOT EXISTS "
                        .concat(TABLE_NAME)
                        .concat("(")
                        .concat(COL_1 + " bigint PRIMARY KEY AUTO_INCREMENT,")
                        .concat(COL_2 + " decimal(4,1),")
                        .concat(COL_3 + " decimal(4,1),")
                        .concat(COL_4 + " varchar(100),")
                        .concat(COL_5 + " varchar(100),")
                        .concat(COL_6 + " bigint,")
                        .concat(COL_7 + " bigint,")
                        .concat(COL_8 + " bigint,")
                        .concat("FOREIGN KEY ("+ COL_6 +") REFERENCES tblsubject(id) ON DELETE CASCADE ON UPDATE CASCADE,")
                        .concat("FOREIGN KEY ("+ COL_7 +") REFERENCES tblacademicyear(id) ON DELETE CASCADE ON UPDATE CASCADE,")
                        .concat("FOREIGN KEY ("+ COL_8 +") REFERENCES tblstudent(id) ON DELETE CASCADE ON UPDATE CASCADE);");

                LOGGER.info("SQL : " + sql);
                PreparedStatement pst = DB_MANAGER.getConnection().prepareStatement(sql);
                pst.executeUpdate();
                DB_MANAGER.close();
            }
        } catch (SQLException e) {
            LOGGER.info(e.getMessage());
            DB_MANAGER.close();
        }
    }

    @Override
    public Record getRecordById(long id) {
        try {
            if (DB_MANAGER.connect()) {
                String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ? LIMIT 1;";

                PreparedStatement pst = DB_MANAGER.getConnection().prepareStatement(sql);
                pst.setLong(1, id);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    Record record = new Record();
                    record.setId(rs.getLong(1));
                    record.setMidterm(rs.getDouble(2));
                    record.setFinalterm(rs.getDouble(3));
                    record.setDate(rs.getString(4));
                    record.setRemark(rs.getString(5));
                    record.setSubjectId(rs.getLong(6));
                    record.setAcademicId(rs.getLong(7));
                    record.setStudentId(rs.getLong(8));

                    DB_MANAGER.close();
                    return record;
                }
            }
            throw new NoResultFoundException("No result found");
        } catch (SQLException | NoResultFoundException e) {
            LOGGER.warning(e.getMessage());
            DB_MANAGER.close();
            return null;
        }
    }

    @Override
    public List<Record> getRecordList() {
        List<Record> recordList = new ArrayList<>();
        try {
            if (DB_MANAGER.connect()) {
                String sql = "SELECT * FROM " + TABLE_NAME + ";";

                PreparedStatement pst = DB_MANAGER.getConnection().prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    Record record = new Record();
                    record.setId(rs.getLong(1));
                    record.setMidterm(rs.getDouble(2));
                    record.setFinalterm(rs.getDouble(3));
                    record.setDate(rs.getString(4));
                    record.setRemark(rs.getString(5));
                    record.setSubjectId(rs.getLong(6));
                    record.setAcademicId(rs.getLong(7));
                    record.setStudentId(rs.getLong(8));
                    recordList.add(record);
                }
                DB_MANAGER.close();
                return recordList;
            }
            throw new NoResultFoundException("No result found");
        } catch (SQLException | NoResultFoundException e) {
            LOGGER.warning(e.getMessage());
            DB_MANAGER.close();
            return recordList;
        }
    }

    @Override
    public List<Record> getRecordList(long studentId) {
        List<Record> recordList = new ArrayList<>();
        try {
            if (DB_MANAGER.connect()) {
                Connection connection = DB_MANAGER.getConnection();
                String sql = "SELECT * FROM tblcreditsubject WHERE studentId=?;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, studentId);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    Record record = new Record();
                    record.setId(rs.getLong(1));
                    record.setMidterm(rs.getDouble(2));
                    record.setFinalterm(rs.getDouble(3));
                    record.setDate(rs.getString(4));
                    record.setRemark(rs.getString(5));
                    record.setSubjectId(rs.getLong(6));
                    record.setAcademicId(rs.getLong(7));
                    record.setStudentId(rs.getLong(8));
                    recordList.add(record);
                }
                DB_MANAGER.close();
                return recordList;
            }
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException | NoResultFoundException e) {
            LOGGER.warning(e.getMessage());
            DB_MANAGER.close();
            return recordList;
        }
    }

    @Override
    public List<Record> getRecordList(long academicId, long studentId) {
        List<Record> recordList = new ArrayList<>();
        try {
            if (DB_MANAGER.connect()) {
                Connection connection = DB_MANAGER.getConnection();
                String sql = "SELECT * FROM tblcreditsubject WHERE academicId=? AND studentId=?;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, academicId);
                pst.setLong(2, studentId);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    Record record = new Record();
                    record.setId(rs.getLong(1));
                    record.setMidterm(rs.getDouble(2));
                    record.setFinalterm(rs.getDouble(3));
                    record.setDate(rs.getString(4));
                    record.setRemark(rs.getString(5));
                    record.setSubjectId(rs.getLong(6));
                    record.setAcademicId(rs.getLong(7));
                    record.setStudentId(rs.getLong(8));
                    recordList.add(record);
                }
                DB_MANAGER.close();
                return recordList;
            }
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException | NoResultFoundException e) {
            LOGGER.warning(e.getMessage());
            DB_MANAGER.close();
            return recordList;
        }
    }

    @Override
    public List<Record> getRecordListOfSubject(long subjectId, long studentId) {
        List<Record> recordList = new ArrayList<>();
        try {
            if (DB_MANAGER.connect()) {
                Connection connection = DB_MANAGER.getConnection();
                String sql = "SELECT * FROM tblcreditsubject WHERE subjectId=? AND studentId=?;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, subjectId);
                pst.setLong(2, studentId);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    Record record = new Record();
                    record.setId(rs.getLong(1));
                    record.setMidterm(rs.getDouble(2));
                    record.setFinalterm(rs.getDouble(3));
                    record.setDate(rs.getString(4));
                    record.setRemark(rs.getString(5));
                    record.setSubjectId(rs.getLong(6));
                    record.setAcademicId(rs.getLong(7));
                    record.setStudentId(rs.getLong(8));
                    recordList.add(record);
                }
                DB_MANAGER.close();
                return recordList;
            }
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException | NoResultFoundException e) {
            LOGGER.warning(e.getMessage());
            DB_MANAGER.close();
            return recordList;
        }
    }

    @Override
    public void addRecord(long subjectId, long academicId, long studentId, Record record) {
        try {
            if(isSubjectPassed(subjectId, studentId))
                throw new SubjectAlreadyPassedException("The subject is already passed");
            if(isSubjectDuplicated(subjectId, academicId, studentId))
                throw new SubjectDuplicateException("The subject is duplicated");
            if (DB_MANAGER.connect()) {
                String sql = "INSERT INTO " + TABLE_NAME + "(midterm, finalterm, date, remark, subjectId, " +
                        "academicId, studentId) VALUES (?, ?, ?, ?, ?, ?, ?);";
                PreparedStatement pst = DB_MANAGER.getConnection().prepareStatement(sql);

                pst.setDouble(1, record.getMidterm());
                pst.setDouble(2, record.getFinalterm());
                pst.setString(3, record.getDate());
                pst.setString(4, record.getRemark());
                pst.setLong(5, subjectId);
                pst.setLong(6, academicId);
                pst.setLong(7, studentId);
                pst.executeUpdate();
            }
            DB_MANAGER.close();
        } catch (SQLException | SubjectAlreadyPassedException | SubjectDuplicateException e) {
            LOGGER.warning(e.getMessage());
            DB_MANAGER.close();
        }
    }

    @Override
    public void updateRecordById(long id, Record record) {
        try {
            if (DB_MANAGER.connect()) {
                String sql = "UPDATE " + TABLE_NAME + " SET midterm=?, finalterm=?, date=?, remark=? WHERE id = ?;";

                PreparedStatement pst = DB_MANAGER.getConnection().prepareStatement(sql);
                pst.setDouble(1, record.getMidterm());
                pst.setDouble(2, record.getFinalterm());
                pst.setString(3, record.getDate());
                pst.setString(4, record.getRemark());
                pst.setLong(5, id);
                pst.executeUpdate();
            }
            DB_MANAGER.close();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            DB_MANAGER.close();
        }
    }

    @Override
    public void deleteRecordById(long id) {
        try {
            if (DB_MANAGER.connect()) {
                Connection connection = DB_MANAGER.getConnection();

                String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, id);
                pst.executeUpdate();
            }
            DB_MANAGER.close();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            DB_MANAGER.close();
        }
    }

    @Override
    public List<Record> getRecordListByMark(long studentId, String remark) {
        List<Record> recordList = new ArrayList<>();
        try {
            if (DB_MANAGER.connect()) {
                Connection connection = DB_MANAGER.getConnection();
                String sql = "SELECT * FROM " + TABLE_NAME + " WHERE studentId=? AND remark=?";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, studentId);
                pst.setString(2, remark);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    Record record = new Record();
                    record.setId(rs.getLong(1));
                    record.setMidterm(rs.getDouble(2));
                    record.setFinalterm(rs.getDouble(3));
                    record.setDate(rs.getString(4));
                    record.setRemark(rs.getString(5));
                    record.setSubjectId(rs.getLong(6));
                    record.setAcademicId(rs.getLong(7));
                    record.setStudentId(rs.getLong(8));
                    recordList.add(record);
                }
                DB_MANAGER.close();
                return recordList;
            }
            throw new NoResultFoundException("No result found on the user detail table");
        } catch (SQLException | NoResultFoundException e) {
            LOGGER.warning(e.getMessage());
            DB_MANAGER.close();
            return recordList;
        }
    }

    @Override
    public boolean isSubjectPassed(long subjectId, long studentId) {
        try {
            final Remark remark = Remark.PASSED;
            if (DB_MANAGER.connect()) {
                Connection connection = DB_MANAGER.getConnection();
                String sql = "SELECT * FROM " + TABLE_NAME + " WHERE subjectId=? AND studentId=? AND remark=? LIMIT 1;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, subjectId);
                pst.setLong(2, studentId);
                pst.setString(3, remark.getCode());
                ResultSet rs = pst.executeQuery();

                final boolean result = rs.next();
                DB_MANAGER.close();
                return result;
            }
            throw new SQLException("Connection Problem");
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            DB_MANAGER.close();
            return false;
        }
    }

    @Override
    public boolean isSubjectNotPassed(long subjectId, long studentId) {
        try {
            if (DB_MANAGER.connect()) {
                Connection connection = DB_MANAGER.getConnection();
                String sql = "SELECT * FROM tblcreditsubject WHERE subjectId=? AND studentId=? AND remark=(? OR ? OR ?) LIMIT 1";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, subjectId);
                pst.setLong(2, studentId);
                pst.setString(3, Remark.FAILED.getCode());
                pst.setString(4, Remark.DROPPED.getCode());
                pst.setString(5, Remark.INCOMPLETE.getCode());
                ResultSet rs = pst.executeQuery();

                final boolean result = rs.next();
                DB_MANAGER.close();
                return result;
            }
            throw new SQLException("Connection Problem");
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            DB_MANAGER.close();
            return false;
        }
    }

    @Override
    public boolean isSubjectDuplicated(long subjectId, long academicId, long studentId) {
        try {
            if (DB_MANAGER.connect()) {
                Connection connection = DB_MANAGER.getConnection();
                String sql = "SELECT * FROM " + TABLE_NAME + " WHERE subjectId=? AND academicId=? AND studentId=? LIMIT 1;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, subjectId);
                pst.setLong(2, academicId);
                pst.setLong(3, studentId);
                ResultSet rs = pst.executeQuery();

                final boolean result = rs.next();
                DB_MANAGER.close();
                return result;
            }
            throw new SQLException("Connection Problem");
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            DB_MANAGER.close();
            return false;
        }
    }

    @Override
    public boolean isTaken(long subjectId, long studentId, long courseId,  int year, int semester) {
        try {
            if (DB_MANAGER.connect()) {
                Connection connection = DB_MANAGER.getConnection();
                String sql = "SELECT * FROM tblcreditsubject as c join tblacademicyear as a on c.academicId=a.id " +
                        "WHERE c.subjectId=? AND c.studentId=? and a.courseId=? and a.year=? and a.semester=?;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, subjectId);
                pst.setLong(2, studentId);
                pst.setLong(3, courseId);
                pst.setInt(4, year);
                pst.setInt(5, semester);
                ResultSet rs = pst.executeQuery();

                final boolean result = rs.next();
                DB_MANAGER.close();
                return result;
            }
            throw new SQLException("Connection Problem");
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            DB_MANAGER.close();
            return false;
        }
    }

    @Override
    public boolean isTaken(long subjectId, long studentId, long courseId) {
        try {
            if (DB_MANAGER.connect()) {
                Connection connection = DB_MANAGER.getConnection();
                String sql = "SELECT * FROM tblcreditsubject as c join tblacademicyear as a on c.academicId=a.id " +
                        "WHERE c.subjectId=? AND c.studentId=? and a.courseId=?";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, subjectId);
                pst.setLong(2, studentId);
                pst.setLong(3, courseId);
                ResultSet rs = pst.executeQuery();

                final boolean result = rs.next();
                DB_MANAGER.close();
                return result;
            }
            throw new SQLException("Connection Problem");
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            DB_MANAGER.close();
            return false;
        }
    }

    @Override
    public void setSubject(long courseId) {
        try {
            if (DB_MANAGER.connect()) {
                String sql = "UPDATE tblcreditsubject AS TBL_CS JOIN tblacademicyear AS TBL_AY ON TBL_CS.academicId" +
                        "=TBL_AY.id SET TBL_CS.remark=? WHERE TBL_CS.remark=? AND TBL_AY.courseId=?;";

                PreparedStatement pst = DB_MANAGER.getConnection().prepareStatement(sql);
                pst.setString(1, Remark.INCOMPLETE.getCode());
                pst.setString(2, Remark.NOTSET.getCode());
                pst.setLong(3, courseId);
                pst.executeUpdate();
            }
            DB_MANAGER.close();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            DB_MANAGER.close();
        }
    }
}
