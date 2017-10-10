package io.erm.ees.helper;

import io.erm.ees.dao.*;
import io.erm.ees.dao.impl.*;
import io.erm.ees.model.Section;
import io.erm.ees.model.Student;
import io.erm.ees.model.v2.AcademicYear;

import java.util.List;

public class EvaluationHelper {

    static final CourseDao courseDao = new CourseDaoImpl();
    static final AcademicYearDao academicYearDao = new AcademicYearDaoImpl();
    static final CurriculumDao curriculumDao = new CurriculumDaoImpl();
    static final SubjectDao subjectDao = new SubjectDaoImpl();
    static final SectionDao sectionDao = new SectionDaoImpl();

    public boolean evaluate(Student student, int year) {
        try {
            final AcademicYear academicYear = new AcademicYear();
            final List<AcademicYear> academicYearList = academicYearDao.getAcademicYearListOpen(student.getCourseId());

            if(academicYearList.size() < 1)
                return false;
            else {
                for(AcademicYear ac:academicYearList) {
                    if(ac.getYear()==year){
                        academicYear.setId(ac.getId());
                        academicYear.setCode(ac.getCode());
                        academicYear.setName(ac.getName());
                        academicYear.setSemester(ac.getSemester());
                        academicYear.setYear(ac.getYear());
                        academicYear.setStatus(ac.isStatus());
                        academicYear.setCourseId(ac.getCourseId());
                        break;
                    }
                }
            }

            final Section section = sectionDao.getSectionById(student.getSectionId());

            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
