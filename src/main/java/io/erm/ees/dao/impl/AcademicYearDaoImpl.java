package io.erm.ees.dao.impl;

import io.erm.ees.dao.AcademicYearDao;
import io.erm.ees.dao.conn.DBManager;
import io.erm.ees.dao.exception.NoResultFoundException;
import io.erm.ees.model.v2.AcademicYear;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AcademicYearDaoImpl implements AcademicYearDao {

    private static final DBManager DB_MANAGER = new DBManager();

    private static final Logger LOGGER = Logger.getLogger(CreditSubjectDaoImpl.class.getSimpleName());

    @Override
    public void init() {
        Connection connection = null;
        try {
            if (DB_MANAGER.connect()) {
                String sql = "CREATE TABLE IF NOT EXISTS "
                        .concat(TABLE_NAME)
                        .concat("(")
                        .concat(COL_1 + " bigint PRIMARY KEY AUTO_INCREMENT,")
                        .concat(COL_2 + " bigint,")
                        .concat(COL_3 + " varchar(100),")
                        .concat(COL_4 + " int,")
                        .concat(COL_5 + " int,")
                        .concat(COL_6 + " tinyint,")
                        .concat(COL_7 + " bigint,")
                        .concat("FOREIGN KEY ("+ COL_7 +") REFERENCES tblcourse(id) ON DELETE CASCADE ON UPDATE CASCADE)");

                LOGGER.info("SQL : " + sql);

                connection = DB_MANAGER.getConnection();
                PreparedStatement pst = connection.prepareStatement(sql);
                pst.executeUpdate();
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.info(e.getMessage());
            DB_MANAGER.close();
        }
    }

    @Override
    public List<AcademicYear> getAcademicYearList(long studentId) {
        List<AcademicYear> academicYearList = new ArrayList<>();
        try {
            if (DB_MANAGER.connect()) {
                Connection connection = DB_MANAGER.getConnection();
                String sql = "SELECT * FROM " + TABLE_NAME + ";";

                PreparedStatement pst = connection.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    AcademicYear academicYear = new AcademicYear();
                    academicYear.setId(rs.getLong(1));
                    academicYear.setCode(rs.getLong(2));
                    academicYear.setName(rs.getString(3));
                    academicYear.setStatus(rs.getBoolean(4));
                    academicYear.setYear(rs.getInt(5));
                    academicYear.setStatus(rs.getBoolean(6));
                    academicYear.setCourseId(rs.getLong(7));
                    academicYearList.add(academicYear);
                }
                DB_MANAGER.close();
                return academicYearList;
            }
            throw new NoResultFoundException("No result found");
        } catch (SQLException | NoResultFoundException e) {
            LOGGER.warning(e.getMessage());
            DB_MANAGER.close();
            return academicYearList;
        }
    }

    @Override
    public void addAcademicYear(long courseId, AcademicYear academicYear) {
        try {
            if (DB_MANAGER.connect()) {
                String sql = "INSERT INTO " + TABLE_NAME + "(midterm, finalterm, date, remark, subjectId, " +
                        "academicId, studentId) VALUES (?, ?, ?, ?, ?, ?, ?);";
                PreparedStatement pst = DB_MANAGER.getConnection().prepareStatement(sql);

//                pst.setDouble(1, record.getMidterm());
//                pst.setDouble(2, record.getFinalterm());
//                pst.setString(3, record.getDate());
//                pst.setString(4, record.getRemark());
//                pst.setLong(5, subjectId);
//                pst.setLong(6, academicId);
//                pst.setLong(7, studentId);
                pst.executeUpdate();
            }
            DB_MANAGER.close();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            DB_MANAGER.close();
        }
    }

    @Override
    public void deleteAcademicYearById(long id) {

    }

    @Override
    public boolean statusOpen(long id) {
        return false;
    }

    @Override
    public boolean statusClose(long id) {
        return false;
    }
}
