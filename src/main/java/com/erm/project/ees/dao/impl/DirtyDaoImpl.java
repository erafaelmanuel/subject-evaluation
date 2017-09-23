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

public class DirtyDaoImpl implements DirtyDao {

    DBManager dbManager;

    public DirtyDaoImpl() {
        dbManager = new DBManager();
    }

    private void init() {

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
                return recordList;
            }
            throw new NoResultFoundException("No result found");
        } catch (SQLException e) {
            e.printStackTrace();
            return recordList;
        }
    }
}
