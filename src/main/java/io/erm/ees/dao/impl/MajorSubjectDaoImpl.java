package io.erm.ees.dao.impl;

import io.erm.ees.dao.MajorSubjectDao;
import io.erm.ees.dao.conn.DBManager;
import io.erm.ees.dao.exception.NoResultFoundException;
import io.erm.ees.dao.exception.SubjectDuplicateException;
import io.erm.ees.dao.exception.SubjectNotBelongException;
import io.erm.ees.helper.IdGenerator;
import io.erm.ees.model.Subject;
import io.erm.ees.model.v2.AcademicYear;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MajorSubjectDaoImpl implements MajorSubjectDao {

    private static final DBManager DB_MANAGER = new DBManager();

    private static final Logger LOGGER = Logger.getLogger(CreditSubjectDaoImpl.class.getSimpleName());

    public MajorSubjectDaoImpl() {
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
                        .concat(COL_3 + " bigint,")
                        .concat("FOREIGN KEY ("+ COL_2 +") REFERENCES tblcurriculum(id) ON DELETE CASCADE ON UPDATE CASCADE,")
                        .concat("FOREIGN KEY ("+ COL_3 +") REFERENCES tblsubject(id) ON DELETE CASCADE ON UPDATE CASCADE)");

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
    public List<Subject> getSubjectList(long courseId, int year, int semester) {
        List<Subject> subjectList = new ArrayList<>();
        try {
            if (DB_MANAGER.connect()) {
                Connection connection = DB_MANAGER.getConnection();
                String sql = "SELECT TBL_SUB.id, TBL_SUB._name, TBL_SUB._desc, TBL_SUB._unit, TBL_SUB._unitLecture, " +
                        "TBL_SUB._unitLaboratory FROM tblmajorsubject AS TBL_MS JOIN tblsubject AS TBL_SUB ON TBL_MS" +
                        ".subjectId=TBL_SUB.id JOIN tblcurriculum AS TBL_CUR ON TBL_MS.curriculumId=TBL_CUR.id WHERE" +
                        " TBL_CUR.course_id=? AND TBL_CUR._year=? AND TBL_CUR._semester=?";

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
                DB_MANAGER.close();
                return subjectList;
            }
            throw new NoResultFoundException("No result found");
        } catch (SQLException | NoResultFoundException e) {
            LOGGER.warning(e.getMessage());
            DB_MANAGER.close();
            return subjectList;
        }
    }

    @Override
    @Deprecated
    public List<Subject> getMinorSubjectList(long courseId, int year, int semester) {
        List<Subject> subjectList = new ArrayList<>();
        try {
            if (DB_MANAGER.connect()) {
                Connection connection = DB_MANAGER.getConnection();
                String sql = "SELECT * FROM tblsubject as s right join tblcurriculumsubjectlist as l on s.id=l.subjectId join tblcurriculum as c on l.curriculumId = c.id where c.course_id=? and c._year=? and c._semester=? and s.id not in (select subjectId from tblmajorsubject as m join tblcurriculum as c2 on m.curriculumId=c2.id and c2.course_id=? and c2._year=? and c2._semester=?)";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, courseId);
                pst.setInt(2, year);
                pst.setInt(3, semester);
                pst.setLong(4, courseId);
                pst.setInt(5, year);
                pst.setInt(6, semester);
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
                DB_MANAGER.close();
                return subjectList;
            }
            throw new NoResultFoundException("No result found");
        } catch (SQLException | NoResultFoundException e) {
            LOGGER.warning(e.getMessage());
            DB_MANAGER.close();
            return subjectList;
        }
    }

    @Override
    public void addSubject(long curriculumId, long subjectId) {
        try {
            if(!isInCurriculum(curriculumId, subjectId))
                throw new SubjectNotBelongException("The subject is not part of curriculum");
            if(isSubjectExist(curriculumId, subjectId))
                throw new SubjectDuplicateException("The subject is duplicated");
            if (DB_MANAGER.connect()) {
                String sql = "INSERT INTO " + TABLE_NAME + "(curriculumId, subjectId) VALUES (?, ?);";
                PreparedStatement pst = DB_MANAGER.getConnection().prepareStatement(sql);
                pst.setLong(1, curriculumId);
                pst.setLong(2, subjectId);
                pst.executeUpdate();
                DB_MANAGER.close();
                return;
            }
            throw new SQLException("Connection Problem");
        } catch (SQLException | SubjectDuplicateException | SubjectNotBelongException e) {
            LOGGER.warning(e.getMessage());
            DB_MANAGER.close();
        }
    }

    @Override
    public void removeSubject(long curriculumId, long subjectId) {
        try {
            if (DB_MANAGER.connect()) {
                Connection connection = DB_MANAGER.getConnection();

                String sql = "DELETE FROM " + TABLE_NAME + " WHERE curriculumId=? AND subjectId=?;";
                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, curriculumId);
                pst.setLong(2, subjectId);
                pst.executeUpdate();
            }
            DB_MANAGER.close();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            DB_MANAGER.close();
        }
    }

    @Override
    public boolean isSubjectExist(long curriculumId, long subjectId) {
        try {
            if (DB_MANAGER.connect()) {
                Connection connection = DB_MANAGER.getConnection();
                String sql = "SELECT * FROM " + TABLE_NAME + " WHERE curriculumId=? AND subjectId=?;";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, curriculumId);
                pst.setLong(2, subjectId);
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
    public boolean isInCurriculum(long curriculumId, long subjectId) {
        try {
            if (DB_MANAGER.connect()) {
                Connection connection = DB_MANAGER.getConnection();
                String sql = "SELECT * FROM tblcurriculumsubjectlist as l join tblcurriculum as c on l.curriculumId" +
                        "=c.id where c.id=? and l.subjectId=?";

                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setLong(1, curriculumId);
                pst.setLong(2, subjectId);
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
}
