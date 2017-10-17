package io.erm.ees.helper;

import io.erm.ees.dao.*;
import io.erm.ees.dao.impl.*;
import io.erm.ees.model.*;
import io.erm.ees.model.v2.AcademicYear;

import java.util.ArrayList;
import java.util.List;

public class EvaluationHelper {

    static final CourseDao courseDao = new CourseDaoImpl();
    static final AcademicYearDao academicYearDao = new AcademicYearDaoImpl();
    static final CurriculumDao curriculumDao = new CurriculumDaoImpl();
    static final SubjectDao subjectDao = new SubjectDaoImpl();
    static final SectionDao sectionDao = new SectionDaoImpl();
    static final CreditSubjectDao creditSubjectDao = new CreditSubjectDaoImpl();

    static final List<Subject> AVAILABLE_LIST = new ArrayList<>();
    static final List<Curriculum> CURRICULUM_LIST = new ArrayList<>();

    static final Student STUDENT = new Student();
    static final Curriculum CURRICULUM = new Curriculum();

    private static EvaluationHelper evaluationHelper;

    public static EvaluationHelper getInstance() {
        if(evaluationHelper == null)
            evaluationHelper = new EvaluationHelper();
        return evaluationHelper;
    }

    @Deprecated
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

    @Deprecated
    public void condition(Student student, int _year, int _semester) {
        Course course = courseDao.getCourseById(student.getCourseId());

        int year = 0;
        int semester = 0;

        year:for (int y = 1; y <= course.getTotalYear(); y++) {
            for (int s = 1; s <= course.getTotalSemester(); s++) {
                List<Subject> list = curriculumDao.getSubjectList(course.getId(), y, s);
                List<Subject> majorList = new MajorSubjectDaoImpl().getSubjectList(course.getId(), y, s);
                System.out.println(list);

                year = y;
                semester = s;

                list:for (Subject subject : list) {
                    if(s!=_semester)
                        continue;

                    //Check if subject is passed
                    if (!new CreditSubjectDaoImpl().isSubjectPassed(subject.getId(), student.getId())) {
                        //Check if subject's prerequisite is passed
                        for(Subject pSubject:new DirtyDaoImpl().getPrerequisiteBySujectId(subject.getId())) {
                            if(!creditSubjectDao.isSubjectPassed(pSubject.getId(), student.getId()))
                                continue list;
                        }
                            AVAILABLE_LIST.add(subject);
                    }
                }
                //major
                for (Subject subject : majorList) {
                    if (!new CreditSubjectDaoImpl().isSubjectPassed(subject.getId(),student.getId())) {
                        break year;
                    }
                }
                //limit
                if (y == _year && s == _semester)
                    break year;

            }
        }
        System.out.println(AVAILABLE_LIST);
    }

    public static void evaluate(Student student, int _year, int _semester, List<Subject> subjectList) {
        final Course course = courseDao.getCourseById(student.getCourseId());
        student.setStatus("REGULAR");

        year:for (int y = 1; y <= course.getTotalYear(); y++) {
            for (int s = 1; s <= course.getTotalSemester(); s++) {

                List<Subject> list = curriculumDao.getSubjectList(course.getId(), y, s);
                list:for (Subject subject : list) {
                    final boolean isPassed = creditSubjectDao.isSubjectPassed(subject.getId(), student.getId());
                    if(s!=_semester) {
                        if(!isPassed)
                            student.setStatus("IRREGULAR");
                        continue;
                    }

                    //Check if subject is passed
                    if (!isPassed) {
                        for(Subject pSubject:new DirtyDaoImpl().getPrerequisiteBySujectId(subject.getId()))
                            //if subject is passed then remove it from the list by skipping the add method
                            if(!creditSubjectDao.isSubjectPassed(pSubject.getId(), student.getId())) continue list;
                        AVAILABLE_LIST.add(subject);
                    }
                }
                //end
                if (y == _year && s == _semester) break year;
            }
        }
        subjectList.addAll(AVAILABLE_LIST);
        clear();
    }

    public static void evaluateWithMajor(Student student, int _year, int _semester, List<Subject> subjectList) {
        Course course = courseDao.getCourseById(student.getCourseId());
        year:for (int y = 1; y <= course.getTotalYear(); y++) {
            for (int s = 1; s <= course.getTotalSemester(); s++) {

                List<Subject> list = curriculumDao.getSubjectList(course.getId(), y, s);
                List<Subject> majorList = new MajorSubjectDaoImpl().getSubjectList(course.getId(), y, s);

                list:for (Subject subject : list) {
                    if(s!=_semester) continue;

                    //Check if subject is passed
                    if (!new CreditSubjectDaoImpl().isSubjectPassed(subject.getId(), student.getId())) {
                        for(Subject pSubject:new DirtyDaoImpl().getPrerequisiteBySujectId(subject.getId()))
                            //if subject is passed then remove it from the list by skipping the add method
                            if(!creditSubjectDao.isSubjectPassed(pSubject.getId(), student.getId())) continue list;
                        AVAILABLE_LIST.add(subject);
                    }
                }
                //major
                for (Subject subject : majorList)
                    if (!new CreditSubjectDaoImpl().isSubjectPassed(subject.getId(),student.getId())) break year;
                //end
                if (y == _year && s == _semester) break year;
            }
        }
        subjectList.addAll(AVAILABLE_LIST);
        System.out.println(AVAILABLE_LIST);
        clear();
    }

    public static void evaluate(Student student, int _year, int _semester) {
        Course course = courseDao.getCourseById(student.getCourseId());
        year:for (int y = 1; y <= course.getTotalYear(); y++) {
            for (int s = 1; s <= course.getTotalSemester(); s++) {

                List<Subject> list = curriculumDao.getSubjectList(course.getId(), y, s);
                List<Subject> majorList = new MajorSubjectDaoImpl().getSubjectList(course.getId(), y, s);
                System.out.println(list);

                list:for (Subject subject : list) {
                    if(s!=_semester) continue;

                    //Check if subject is passed
                    if (!new CreditSubjectDaoImpl().isSubjectPassed(subject.getId(), student.getId())) {
                        for(Subject pSubject:new DirtyDaoImpl().getPrerequisiteBySujectId(subject.getId()))
                            //if subject is passed then remove it from the list by skipping the add method
                            if(!creditSubjectDao.isSubjectPassed(pSubject.getId(), student.getId())) continue list;
                        AVAILABLE_LIST.add(subject);
                    }
                }
                //major
                for (Subject subject : majorList)
                    if (!new CreditSubjectDaoImpl().isSubjectPassed(subject.getId(),student.getId())) break year;
                //end
                if (y == _year && s == _semester) break year;
            }
        }
    }

    public static List<Subject> list() {
        return AVAILABLE_LIST;
    }

    public static void clear() {
       AVAILABLE_LIST.clear();
    }
}
