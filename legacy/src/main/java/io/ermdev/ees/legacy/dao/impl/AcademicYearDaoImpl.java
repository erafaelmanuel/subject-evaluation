package io.ermdev.ees.legacy.dao.impl;

import io.ermdev.ees.legacy.dao.AcademicYearDao;
import io.ermdev.ees.legacy.dao.conn.DbManager;
import io.ermdev.ees.legacy.dao.exception.NoResultFoundException;
import io.ermdev.ees.legacy.helper.IdGenerator;
import io.ermdev.ees.legacy.model.v2.AcademicYear;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

@Deprecated
public class AcademicYearDaoImpl implements AcademicYearDao {

    private static final DbManager DB_MANAGER = new DbManager();

    private static final Logger LOGGER = Logger.getLogger(AcademicYearDaoImpl.class.getSimpleName());

    public AcademicYearDaoImpl() {
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
                        .concat(COL_2 + " bigint,")
                        .concat(COL_3 + " varchar(100),")
                        .concat(COL_4 + " int,")
                        .concat(COL_5 + " int,")
                        .concat(COL_6 + " tinyint,")
                        .concat(COL_7 + " bigint,")
                        .concat("FOREIGN KEY ("+ COL_7 +") REFERENCES tblcourse(id) ON DELETE CASCADE ON UPDATE CASCADE);");

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
    public AcademicYear getAcademicYearOpen(long courseId) {
        try {
            if (DB_MANAGER.connect()) {
                String sql = "SELECT * FROM " + TABLE_NAME + " WHERE status=? AND courseId=? LIMIT 1;";

                PreparedStatement pst = DB_MANAGER.getConnection().prepareStatement(sql);
                pst.setBoolean(1, true);
                pst.setLong(2, courseId);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    AcademicYear academicYear = new AcademicYear();
                    academicYear.setId(0);
                    academicYear.setCode(rs.getLong(2));
                    academicYear.setName(rs.getString(3));
                    academicYear.setSemester(rs.getInt(4));
                    academicYear.setYear(rs.getInt(5));
                    academicYear.setStatus(rs.getBoolean(6));
                    academicYear.setCourseId(rs.getLong(7));
                    DB_MANAGER.close();
                    return academicYear;
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
    public AcademicYear getAcademicYearOpen(long courseId, int year) {
        try {
            if (DB_MANAGER.connect()) {
                String sql = "SELECT * FROM " + TABLE_NAME + " WHERE status=? AND courseId=? AND year=? LIMIT 1;";

                PreparedStatement pst = DB_MANAGER.getConnection().prepareStatement(sql);
                pst.setBoolean(1, true);
                pst.setLong(2, courseId);
                pst.setInt(3, year);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    AcademicYear academicYear = new AcademicYear();
                    academicYear.setId(rs.getLong(1));
                    academicYear.setCode(rs.getLong(2));
                    academicYear.setName(rs.getString(3));
                    academicYear.setSemester(rs.getInt(4));
                    academicYear.setYear(rs.getInt(5));
                    academicYear.setStatus(rs.getBoolean(6));
                    academicYear.setCourseId(rs.getLong(7));
                    DB_MANAGER.close();
                    return academicYear;
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
    public List<AcademicYear> getAcademicYearList() {
        List<AcademicYear> academicYearList = new ArrayList<>();
        try {
            if (DB_MANAGER.connect()) {
                String sql = "SELECT * FROM tblacademicyear GROUP BY code, semester, courseId ORDER BY courseId, code, semester ASC";

                PreparedStatement pst = DB_MANAGER.getConnection().prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    AcademicYear academicYear = new AcademicYear();
                    academicYear.setId(rs.getLong(1));
                    academicYear.setCode(rs.getLong(2));
                    academicYear.setName(rs.getString(3));
                    academicYear.setSemester(rs.getInt(4));
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
    public List<AcademicYear> getAcademicYearList(long studentId) {
        List<AcademicYear> academicYearList = new ArrayList<>();
        try {
            if (DB_MANAGER.connect()) {
                String sql = "SELECT TBL_AC.id, TBL_AC.code, TBL_AC.name, TBL_AC.semester, TBL_AC.year, TBL_AC.sta" +
                        "tus, TBL_AC.courseId FROM tblacademicyear AS TBL_AC JOIN tblcreditsubject AS TBL_CS ON TBL_" +
                        "AC.id=TBL_CS.academicId WHERE TBL_CS.studentId=?";

                PreparedStatement pst = DB_MANAGER.getConnection().prepareStatement(sql);
                pst.setLong(1, studentId);

                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    AcademicYear academicYear = new AcademicYear();
                    academicYear.setId(rs.getLong(1));
                    academicYear.setCode(rs.getLong(2));
                    academicYear.setName(rs.getString(3));
                    academicYear.setSemester(rs.getInt(4));
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
    public List<AcademicYear> getAcademicYearList(long studentId, long courseId) {
        List<AcademicYear> academicYearList = new ArrayList<>();
        try {
            if (DB_MANAGER.connect()) {
                String sql = "SELECT TBL_AC.id, TBL_AC.code, TBL_AC.name, TBL_AC.semester, TBL_AC.year, TBL_AC.sta" +
                        "tus, TBL_AC.courseId FROM tblacademicyear AS TBL_AC JOIN tblcreditsubject AS TBL_CS ON TBL" +
                        "_AC.id=TBL_CS.academicId WHERE TBL_CS.studentId=? AND TBL_AC.courseId=? GROUP BY TBL_AC.id" +
                        " ORDER BY TBL_AC.code, TBL_AC.semester;";

                PreparedStatement pst = DB_MANAGER.getConnection().prepareStatement(sql);
                pst.setLong(1, studentId);
                pst.setLong(2, courseId);

                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    AcademicYear academicYear = new AcademicYear();
                    academicYear.setId(rs.getLong(1));
                    academicYear.setCode(rs.getLong(2));
                    academicYear.setName(rs.getString(3));
                    academicYear.setSemester(rs.getInt(4));
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
    public List<AcademicYear> getAcademicYearList(long code, int semester) {
        List<AcademicYear> academicYearList = new ArrayList<>();
        try {
            if (DB_MANAGER.connect()) {
                String sql = "SELECT * FROM " + TABLE_NAME + " WHERE code=? AND semester=?;";

                PreparedStatement pst = DB_MANAGER.getConnection().prepareStatement(sql);
                pst.setLong(1, code);
                pst.setInt(2, semester);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    AcademicYear academicYear = new AcademicYear();
                    academicYear.setId(rs.getLong(1));
                    academicYear.setCode(rs.getLong(2));
                    academicYear.setName(rs.getString(3));
                    academicYear.setSemester(rs.getInt(4));
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
    public List<AcademicYear> getAcademicYearListOpen() {
        return null;
    }

    @Override
    public List<AcademicYear> getAcademicYearListOpen(long courseId) {
        List<AcademicYear> academicYearList = new ArrayList<>();
        try {
            if (DB_MANAGER.connect()) {
                String sql = "SELECT * FROM " + TABLE_NAME + " WHERE status=? AND courseId=?;";

                PreparedStatement pst = DB_MANAGER.getConnection().prepareStatement(sql);
                pst.setBoolean(1, true);
                pst.setLong(2, courseId);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    AcademicYear academicYear = new AcademicYear();
                    academicYear.setId(rs.getLong(1));
                    academicYear.setCode(rs.getLong(2));
                    academicYear.setName(rs.getString(3));
                    academicYear.setSemester(rs.getInt(4));
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
    public AcademicYear addAcademicYear(long courseId, AcademicYear academicYear) {
        try {
            if (DB_MANAGER.connect()) {
                String sql = "INSERT INTO " + TABLE_NAME + "(id, code, name, semester, year, " +
                        "status, courseId) VALUES (?, ?, ?, ?, ?, ?, ?);";
                PreparedStatement pst = DB_MANAGER.getConnection().prepareStatement(sql);

                academicYear.setId(IdGenerator.random(IdGenerator.Range.SMALL, AcademicYearDaoImpl::prefix));
                pst.setLong(1, academicYear.getId());
                pst.setLong(2, academicYear.getCode());
                pst.setString(3, academicYear.getName());
                pst.setInt(4, academicYear.getSemester());
                pst.setInt(5, academicYear.getYear());
                pst.setBoolean(6, academicYear.isStatus());
                pst.setLong(7, courseId);
                pst.executeUpdate();

                DB_MANAGER.close();
                return academicYear;
            }
            throw new SQLException("Connection Problem");
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            DB_MANAGER.close();
            return null;
        }
    }

    @Override
    public void deleteAcademicYearById(long id) {
        try {
            if (DB_MANAGER.connect()) {

                String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?;";
                PreparedStatement pst = DB_MANAGER.getConnection().prepareStatement(sql);
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
    public void deleteAcademicYear(long code, long courseId, int semester) {
        try {
            if (DB_MANAGER.connect()) {
                Connection connection = DB_MANAGER.getConnection();

                String sql = "DELETE FROM "+ TABLE_NAME +" WHERE code=? AND courseId=? AND semester=?;";
                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, code);
                pst.setLong(2, courseId);
                pst.setInt(3, semester);
                pst.executeUpdate();
            }
            DB_MANAGER.close();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            DB_MANAGER.close();
        }
    }

    @Override
    public void statusOpen(long code, long courseId, int semester) {
        try {
            if (DB_MANAGER.connect()) {
                String sql = "UPDATE " + TABLE_NAME + " SET status=? WHERE code=? AND courseId=? AND semester=?;";
                PreparedStatement pst = DB_MANAGER.getConnection().prepareStatement(sql);

                pst.setBoolean(1, true);
                pst.setLong(2, code);
                pst.setLong(3, courseId);
                pst.setInt(4, semester);
                pst.executeUpdate();
            }
            DB_MANAGER.close();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            DB_MANAGER.close();
        }
    }

    @Override
    public void statusClose(long courseId) {
        try {
            if (DB_MANAGER.connect()) {
                String sql = "UPDATE " + TABLE_NAME + " SET status=? WHERE courseId=?";
                PreparedStatement pst = DB_MANAGER.getConnection().prepareStatement(sql);

                pst.setBoolean(1, false);
                pst.setLong(2, courseId);
                pst.executeUpdate();


            }
            DB_MANAGER.close();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            DB_MANAGER.close();
        }
    }

    @Override
    public void statusClose(long code, long courseId, int semester) {
        try {
            if (DB_MANAGER.connect()) {
                String sql = "UPDATE " + TABLE_NAME + " SET status=? WHERE code=? AND courseId=? AND semester=?;";
                PreparedStatement pst = DB_MANAGER.getConnection().prepareStatement(sql);

                pst.setBoolean(1, false);
                pst.setLong(2, code);
                pst.setLong(3, courseId);
                pst.setInt(4, semester);
                pst.executeUpdate();


            }
            DB_MANAGER.close();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            DB_MANAGER.close();
        }
    }



    @Override
    public int currentSemesterOpen(long courseId) {
        try {
            if (DB_MANAGER.connect()) {
                Connection connection = DB_MANAGER.getConnection();
                String sql = "SELECT semester FROM " + TABLE_NAME + " WHERE status=? AND courseId=? LIMIT 1;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setBoolean(1, true);
                pst.setLong(2, courseId);
                ResultSet rs = pst.executeQuery();

                if(rs.next()) {
                    final int result = rs.getInt(1);
                    DB_MANAGER.close();
                    return result;
                }
            }
            throw new NoResultFoundException("No result found");
        } catch (SQLException | NoResultFoundException e) {
            LOGGER.warning(e.getMessage());
            DB_MANAGER.close();
            return 0;
        }
    }

    @Override
    public long currentCodeOpen(long courseId) {
        try {
            if (DB_MANAGER.connect()) {
                Connection connection = DB_MANAGER.getConnection();
                String sql = "SELECT code FROM " + TABLE_NAME + " WHERE status=? AND courseId=? LIMIT 1;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setBoolean(1, true);
                pst.setLong(2, courseId);
                ResultSet rs = pst.executeQuery();

                if(rs.next()) {
                    final long result = rs.getLong(1);
                    DB_MANAGER.close();
                    return result;
                }
            }
            throw new NoResultFoundException("No result found");
        } catch (SQLException | NoResultFoundException e) {
            LOGGER.warning(e.getMessage());
            DB_MANAGER.close();
            return 0;
        }
    }

    @Override
    public boolean isTaken(long studentId, long code, int semester) {
        try {
            if (DB_MANAGER.connect()) {
                Connection connection = DB_MANAGER.getConnection();
                String sql = "SELECT * FROM tblacademicyear AS TBL_AC JOIN tblcreditsubject AS TBL_CS ON TBL_" +
                        "AC.id=TBL_CS.academicId WHERE TBL_CS.studentId=? AND TBL_AC.code=? AND TBL_AC.semester=?";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, studentId);
                pst.setLong(2, code);
                pst.setInt(3, semester);

                ResultSet rs = pst.executeQuery();
                final boolean result = rs.next();
                DB_MANAGER.close();
                return result;
            }
            throw new NoResultFoundException("No result found");
        } catch (SQLException | NoResultFoundException e) {
            LOGGER.warning(e.getMessage());
            DB_MANAGER.close();
            return false;
        }
    }

    @Override
    public boolean isAcademicYearIsExist(long code, long courseId) {
        List<AcademicYear> academicYearList = new ArrayList<>();
        try {
            if (DB_MANAGER.connect()) {
                Connection connection = DB_MANAGER.getConnection();
                String sql = "SELECT * FROM " + TABLE_NAME + " WHERE code=? AND courseId=? LIMIT 1;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, code);
                pst.setLong(2, courseId);
                ResultSet rs = pst.executeQuery();

                final boolean result = rs.next();
                DB_MANAGER.close();
                return result ;
            }
            throw new NoResultFoundException("No result found");
        } catch (SQLException | NoResultFoundException e) {
            LOGGER.warning(e.getMessage());
            DB_MANAGER.close();
            return false;
        }
    }

    @Override
    public boolean isAcademicYearIsExist(long code, long courseId, int semester) {
        List<AcademicYear> academicYearList = new ArrayList<>();
        try {
            if (DB_MANAGER.connect()) {
                Connection connection = DB_MANAGER.getConnection();
                String sql = "SELECT * FROM " + TABLE_NAME + " WHERE code=? AND courseId=? AND semester=? LIMIT 1;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, code);
                pst.setLong(2, courseId);
                pst.setInt(3, semester);
                ResultSet rs = pst.executeQuery();

                final boolean result = rs.next();
                DB_MANAGER.close();
                return result ;
            }
            throw new NoResultFoundException("No result found");
        } catch (SQLException | NoResultFoundException e) {
            LOGGER.warning(e.getMessage());
            DB_MANAGER.close();
            return false;
        }
    }


    private static long prefix() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }
}