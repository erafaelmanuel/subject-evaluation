package com.erm.project.ees.dao.impl;

import com.erm.project.ees.dao.DirtyDao;
import com.erm.project.ees.dao.conn.DBManager;
import com.erm.project.ees.dao.exception.NoResultFoundException;
import com.erm.project.ees.model.StudentSubjectRecord;
import com.erm.project.ees.model.Subject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DirtyDaoImpl implements DirtyDao {

    public static final Logger LOGGER = Logger.getLogger(DirtyDaoImpl.class.getSimpleName());

    DBManager dbManager;

    public DirtyDaoImpl() {
        dbManager = new DBManager();
    }

    private void init() {

    }

    public StudentSubjectRecord getStudentSubjectRecordById(long studentId, long subjectId) {
        List<StudentSubjectRecord> recordList = new ArrayList<>();
        try {
            if(dbManager.connect()) {
                String sql = "SELECT * FROM tblstudentsubjectlist WHERE studentId = ? AND subjectId = ?";
                PreparedStatement pst = dbManager.getConnection().prepareStatement(sql);
                pst.setLong(1, studentId);
                pst.setLong(2, subjectId);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    StudentSubjectRecord record = new StudentSubjectRecord();
                    record.setMidterm(rs.getDouble(2));
                    record.setFinalterm(rs.getDouble(3));
                    record.setDate(rs.getString(4));
                    record.setMark(rs.getString(5));

                    dbManager.close();
                    return record;
                }
            }
            throw new NoResultFoundException("No result found");
        } catch (SQLException | NoResultFoundException e) {
            LOGGER.warning(e.getMessage());
            dbManager.close();
            return null;
        }
    }

    @Override
    public List<StudentSubjectRecord> getStudentSubjectRecordByMark(long studentId, String mark) {
        List<StudentSubjectRecord> recordList = new ArrayList<>();
        try {
            if(dbManager.connect()) {
                String sql = "SELECT * FROM tblstudentsubjectlist WHERE studentId=? AND _mark=?";
                PreparedStatement pst = dbManager.getConnection().prepareStatement(sql);
                pst.setLong(1, studentId);
                pst.setString(2, mark);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    StudentSubjectRecord record = new StudentSubjectRecord();
                    record.setMidterm(rs.getDouble(2));
                    record.setFinalterm(rs.getDouble(3));
                    record.setDate(rs.getString(4));
                    record.setMark(rs.getString(5));
                    record.setSubjectId(rs.getLong(6));
                    record.setSemester(rs.getInt(8));
                    recordList.add(record);
                }
                dbManager.close();
                return recordList;
            }
            throw new NoResultFoundException("No result found");
        } catch (SQLException | NoResultFoundException e) {
            LOGGER.warning(e.getMessage());
            dbManager.close();
            return recordList;
        }
    }

    @Override
    public int getStudentSubjectRecordSemester(long studentId, String mark) {
        List<StudentSubjectRecord> recordList = new ArrayList<>();
        try {
            if(dbManager.connect()) {
                String sql = "SELECT DISTINCT semester FROM tblstudentsubjectlist WHERE studentId=? AND _mark=?";
                PreparedStatement pst = dbManager.getConnection().prepareStatement(sql);
                pst.setLong(1, studentId);
                pst.setString(2, mark);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    int semester = rs.getInt(1);
                    dbManager.close();
                    return semester;
                }
            }
            throw new NoResultFoundException("No result found");
        } catch (SQLException | NoResultFoundException e) {
            LOGGER.warning(e.getMessage());
            dbManager.close();
            return 1;
        }
    }

    @Override
    public List<StudentSubjectRecord> getStudentSubjectRecords(long courseId, long studentId, int year, int semester) {
        List<StudentSubjectRecord> recordList = new ArrayList<>();
        try {
            if(dbManager.connect()) {
                String sql = "SELECt S._name, S._desc, SL._date, SL._midterm, SL._finalterm, SL._mark from tblcourse as C JOIN tblcurriculum as CU ON C.id = CU.course_id JOIN tblcurriculumsubjectlist as CSL ON CSL.curriculumId = CU.id JOIN tblsubject as S ON S.id = CSL.subjectId JOIN tblstudentsubjectlist as SL ON SL.subjectId = S.id JOIN tblstudent as ST ON ST.id = SL.studentId WHERE CU._year = ? AND CU._semester = ? AND C.id = ? AND ST.id = ?";
                PreparedStatement pst = dbManager.getConnection().prepareStatement(sql);
                pst.setInt(1, year);
                pst.setInt(2, semester);
                pst.setLong(3, courseId);
                pst.setLong(4, studentId);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    StudentSubjectRecord record = new StudentSubjectRecord();

                    record.setSubjectName(rs.getString(1));
                    record.setSubjectDesc(rs.getString(2));
                    record.setDate(rs.getString(3));
                    record.setMidterm(rs.getDouble(4));
                    record.setFinalterm(rs.getDouble(5));
                    record.setMark(rs.getString(6));
                    recordList.add(record);
                }
                dbManager.close();
                return recordList;
            }
            throw new NoResultFoundException("No result found");
        } catch (SQLException e) {
            e.printStackTrace();
            dbManager.close();
            return recordList;
        }
    }

    @Override
    public List<Subject> getCurriculumSubjectList(long courseId, int year, int semester) {
        List<Subject> subjectList = new ArrayList<>();
        try {
            if(dbManager.connect()) {
                String sql = "SELECT S.id, S._name, S._desc, S._unit from tblcourse as C JOIN tblcurriculum as CU ON C.id = CU.course_id JOIN tblcurriculumsubjectlist as CSL ON CSL.curriculumId = CU.id JOIN tblsubject as S ON S.id = CSL.subjectId WHERE CU._year = ? AND CU._semester = ? AND C.id = ?";
                PreparedStatement pst = dbManager.getConnection().prepareStatement(sql);
                pst.setInt(1, year);
                pst.setInt(2, semester);
                pst.setLong(3, courseId);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    Subject subject = new Subject();
                    subject.setId(rs.getLong(1));
                    subject.setName(rs.getString(2));
                    subject.setDesc(rs.getString(3));
                    subject.setUnit(rs.getInt(4));
                    subjectList.add(subject);
                }
                dbManager.close();
                return subjectList;
            }
            throw new NoResultFoundException("No result found");
        }catch (SQLException e) {
            e.printStackTrace();
            dbManager.close();
            return subjectList;
        }
    }

    @Override
    public List<Subject> getSpecialCurriculumSubjectList(long courseId, int year, int semester, String type) {
        List<Subject> subjectList = new ArrayList<>();
        try {
            if(dbManager.connect()) {
                String sql = "SELECT S.id, S._name, S._desc, S._unit from tblcourse as C JOIN tblspecialcurriculum as SCU ON C.id = SCU.course_id JOIN tblspecialcurriculumsubjectlist as SCSL ON SCSL.curriculumId = SCU.id JOIN tblsubject as S ON S.id = SCSL.subjectId WHERE SCU._year = ? AND SCU._semester = ? AND C.id = ? AND SCU._type = ?";
                PreparedStatement pst = dbManager.getConnection().prepareStatement(sql);
                pst.setInt(1, year);
                pst.setInt(2, semester);
                pst.setLong(3, courseId);
                pst.setString(4, type);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    Subject subject = new Subject();
                    subject.setId(rs.getLong(1));
                    subject.setName(rs.getString(2));
                    subject.setDesc(rs.getString(3));
                    subject.setUnit(rs.getInt(4));
                    subjectList.add(subject);
                }
                dbManager.close();
                return subjectList;
            }
            throw new NoResultFoundException("No result found");
        }catch (SQLException e) {
            e.printStackTrace();
            dbManager.close();
            return subjectList;
        }
    }

    @Override
    public List<Subject> getPrerequisiteBySujectId(long subjectId) {
        List<Subject> subjectList = new ArrayList<>();
        try {
            if(dbManager.connect()) {
                String sql = "SELECT toSubjectId FROM tblsubjectprerequisite WHERE subjectId = ?";
                PreparedStatement pst = dbManager.getConnection().prepareStatement(sql);
                pst.setLong(1, subjectId);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    Subject subject = new SubjectDaoImpl().getSubjectById(rs.getLong(1));
                    subjectList.add(subject);
                }
                dbManager.close();
                return subjectList;
            }
            throw new NoResultFoundException("No result found");
        }catch (SQLException | NoResultFoundException e) {
            LOGGER.warning(e.getMessage());
            dbManager.close();
            return subjectList;
        }
    }

    @Override
    public int getCurrentSemByCourseId(long courseId) {
        return 0;
    }

    @Override
    public boolean addStudentRecord(StudentSubjectRecord record, long subjectId, long studentId) {
        try {
            if(dbManager.connect()) {
                String sql = "INSERT INTO tblstudentsubjectlist (_midterm, _finalterm, _date, _mark, subjectId, studentId, semester) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pst = dbManager.getConnection().prepareStatement(sql);
                pst.setDouble(1, record.getMidterm());
                pst.setDouble(2, record.getFinalterm());
                pst.setString(3, record.getDate());
                pst.setString(4, record.getMark());
                pst.setLong(5, subjectId);
                pst.setLong(6, studentId);
                pst.setInt(7, record.getSemester());
                pst.executeUpdate();
            }
            dbManager.close();
            return true;
        } catch (SQLException | NoResultFoundException e) {
            LOGGER.warning(e.getMessage());
            dbManager.close();
            return false;
        }
    }

    @Override
    public boolean deleteStudentRecord(long studentId, String mark) {
        try {
            if(dbManager.connect()) {
                String sql = "DELETE FROM tblstudentsubjectlist WHERE studentId=? AND _mark=?;";
                PreparedStatement pst = dbManager.getConnection().prepareStatement(sql);
                pst.setLong(1, studentId);
                pst.setString(2, mark);
                pst.executeUpdate();
            }
            dbManager.close();
            return true;
        } catch (SQLException | NoResultFoundException e) {
            LOGGER.warning(e.getMessage());
            dbManager.close();
            return false;
        }
    }
}
