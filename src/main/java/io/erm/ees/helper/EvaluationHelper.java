package io.erm.ees.helper;

import io.erm.ees.dao.*;
import io.erm.ees.dao.impl.*;
import io.erm.ees.model.*;

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
    private static int tempYear=1;
    private static String tempStatus="REGULAR";

    public static EvaluationHelper getInstance() {
        if(evaluationHelper == null)
            evaluationHelper = new EvaluationHelper();
        return evaluationHelper;
    }

    private static boolean isDataClean(Student student, Course course, int year, int semester) {
        List<Subject> list = curriculumDao.getSubjectList(course.getId(), year, semester);
        for (Subject subject : list) {
            boolean isPassed = creditSubjectDao.isSubjectPassed(subject.getId(), student.getId());
            boolean isTaken = creditSubjectDao.isTaken(subject.getId(), student.getId(), course.getId());
            if(isTaken) {
                if(!isPassed) return false;
            }
        }
        return true;
    }

    private static boolean isDataComplete(Student student, Course course, int year, int semester) {
        List<Subject> list = curriculumDao.getSubjectList(course.getId(), year, semester);
        for (Subject subject : list) {
            boolean isPassed = creditSubjectDao.isSubjectPassed(subject.getId(), student.getId());
            boolean isTaken = creditSubjectDao.isTaken(subject.getId(), student.getId(), course.getId());
            if(!(isTaken && isPassed)) return false;
        }
        return true;
    }

    private static boolean isDataNoContent(Student student, Course course, int year, int semester) {
        List<Subject> list = curriculumDao.getSubjectList(course.getId(), year, semester);
        for (Subject subject : list) {
            boolean isTaken = creditSubjectDao.isTaken(subject.getId(), student.getId(), course.getId());
            if(isTaken) return false;
        }
        return true;
    }

    public static void evaluate(Student student, int _year, int _semester, List<Subject> subjectList) {
        clear();

        final Course course = courseDao.getCourseById(student.getCourseId());
        List<Subject> list = curriculumDao.getSubjectList(course.getId(), _year, _semester);
        main:for (Subject subject : list) {
            final boolean isPassed = creditSubjectDao.isSubjectPassed(subject.getId(), student.getId());
            //Check if subject is passed
            if (!isPassed) {
                for(Subject pSubject:new DirtyDaoImpl().getPrerequisiteBySujectId(subject.getId()))
                    //if subject is passed then remove it from the list by skipping the add method
                    if(!creditSubjectDao.isSubjectPassed(pSubject.getId(), student.getId())) continue main;
                AVAILABLE_LIST.add(subject);
            }
        }

        subjectList.addAll(AVAILABLE_LIST);
    }

    public static void evaluateAll(Student student, int _semester, List<Subject> subjectList) {
        clear();

        final Course course = courseDao.getCourseById(student.getCourseId());
        for (int y = 1; y <= course.getTotalYear(); y++) {
            for (int s = 1; s <= course.getTotalSemester(); s++) {
                if(s != _semester) continue;

                List<Subject> list = curriculumDao.getSubjectList(course.getId(), y, s);
                subject:
                for (Subject subject : list) {
                    final boolean isPassed = creditSubjectDao.isSubjectPassed(subject.getId(), student.getId());
                    //Check if subject is passed
                    if (!isPassed) {
                        for(Subject pSubject:new DirtyDaoImpl().getPrerequisiteBySujectId(subject.getId()))
                            //if subject is passed then remove it from the list by skipping the add method
                            if(!creditSubjectDao.isSubjectPassed(pSubject.getId(), student.getId())) continue subject;
                        AVAILABLE_LIST.add(subject);
                    }
                }
            }
        }
        subjectList.addAll(AVAILABLE_LIST);
    }

    public static void init(Student student, Section section, int semester) {
        final Course course = courseDao.getCourseById(student.getCourseId());
        if(semester==1) {
            if(regular1stSemester(student, course)) {
                student.setStatus(tempStatus);
                section.setYear(tempYear);
            } else {
                student.setStatus("IRREGULAR");
            }
        } else if(semester==2) {
            if(regular2ndSemester(student, course)) {
                student.setStatus(tempStatus);
                section.setYear(tempYear);
            } else {
                student.setStatus("IRREGULAR");
            }
        }
    }

    private static boolean regular1stSemester(Student student, Course course) {
        final boolean isValid = false;
        final int total = course.getTotalYear();
        for(int i=1; i<=total; i++) {
            switch(i) {
                case 1:
                    if(isDataNoContent(student, course, 1, 1) && isDataNoContent(student, course, 1, 2)) {
                        if(total==1) {
                            tempYear=1;
                            tempStatus="REGULAR";
                            return true;
                        }
                        if(isDataNoContent(student, course, 2, 1) && isDataNoContent(student, course, 2, 2)) {
                            if(total==2) {
                                tempYear=1;
                                tempStatus="REGULAR";
                                return true;
                            }
                            if(isDataNoContent(student, course, 3, 1) && isDataNoContent(student, course, 3, 2)) {
                                if(total==3) {
                                    tempYear=1;
                                    tempStatus="REGULAR";
                                    return true;
                                }
                                if(isDataNoContent(student, course, 4, 1) && isDataNoContent(student, course, 4, 2)) {
                                    if(total==4) {
                                        tempYear=1;
                                        tempStatus="REGULAR";
                                        return true;
                                    }
                                    if(isDataNoContent(student, course, 5, 1) && isDataNoContent(student, course, 5, 2)) {
                                        if(total==5) {
                                            tempYear=1;
                                            tempStatus="REGULAR";
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 2:
                    if(isDataComplete(student, course, 1, 1) && isDataComplete(student, course, 1, 2)) {
                        if(total==1) {
                            tempYear=2;
                            tempStatus="REGULAR";
                            return true;
                        }
                        if(isDataNoContent(student, course, 2, 1) && isDataNoContent(student, course, 2, 2)) {
                            if(total==2) {
                                tempYear=2;
                                tempStatus="REGULAR";
                                return true;
                            }
                            if(isDataNoContent(student, course, 3, 1) && isDataNoContent(student, course, 3, 2)) {
                                if(total==3) {
                                    tempYear=2;
                                    tempStatus="REGULAR";
                                    return true;
                                }
                                if(isDataNoContent(student, course, 4, 1) && isDataNoContent(student, course, 4, 2)) {
                                    if(total==4) {
                                        tempYear=2;
                                        tempStatus="REGULAR";
                                        return true;
                                    }
                                    if(isDataNoContent(student, course, 5, 1) && isDataNoContent(student, course, 5, 2)) {
                                        if(total==5) {
                                            tempYear=2;
                                            tempStatus="REGULAR";
                                            return true;
                                        }
                                    }
                                }
                            }

                        }
                    }
                    break;
                case 3:
                    if(isDataComplete(student, course, 1, 1) && isDataComplete(student, course, 1, 2)) {
                        if(total==1) {
                            tempYear=3;
                            tempStatus="REGULAR";
                            return true;
                        }
                        if(isDataComplete(student, course, 2, 1) && isDataComplete(student, course, 2, 2)) {
                            if(total==2) {
                                tempYear=3;
                                tempStatus="REGULAR";
                                return true;
                            }
                            if(isDataNoContent(student, course, 3, 1) && isDataNoContent(student, course, 3, 2)) {
                                if(total==3) {
                                    tempYear=3;
                                    tempStatus="REGULAR";
                                    return true;
                                }
                                if(isDataNoContent(student, course, 4, 1) && isDataNoContent(student, course, 4, 2)) {
                                    if(total==4) {
                                        tempYear=3;
                                        tempStatus="REGULAR";
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 4:
                    if(isDataComplete(student, course, 1, 1) && isDataComplete(student, course, 1, 2)) {
                        if(total==1) {
                            tempYear=4;
                            tempStatus="REGULAR";
                            return true;
                        }
                        if(isDataComplete(student, course, 2, 1) && isDataComplete(student, course, 2, 2)) {
                            if(total==2) {
                                tempYear=4;
                                tempStatus="REGULAR";
                                return true;
                            }
                            if(isDataComplete(student, course, 3, 1) && isDataComplete(student, course, 3, 2)) {
                                if(total==3) {
                                    tempYear=4;
                                    tempStatus="REGULAR";
                                    return true;
                                }
                                if(isDataNoContent(student, course, 4, 1) && isDataNoContent(student, course, 4, 2)) {
                                    if(total==4) {
                                        tempYear=4;
                                        tempStatus="REGULAR";
                                        return true;
                                    }
                                    if(isDataNoContent(student, course, 5, 1) && isDataNoContent(student, course, 5, 2)) {
                                        if(total==5) {
                                            tempYear=4;
                                            tempStatus="REGULAR";
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 5:
                    if(isDataComplete(student, course, 1, 1) && isDataComplete(student, course, 1, 2)) {
                        if(total==1) {
                            tempYear=5;
                            tempStatus="REGULAR";
                            return true;
                        }
                        if(isDataComplete(student, course, 2, 1) && isDataComplete(student, course, 2, 2)) {
                            if(total==2) {
                                tempYear=5;
                                tempStatus="REGULAR";
                                return true;
                            }
                            if(isDataComplete(student, course, 3, 1) && isDataComplete(student, course, 3, 2)) {
                                if(total==3) {
                                    tempYear=5;
                                    tempStatus="REGULAR";
                                    return true;
                                }
                                if(isDataComplete(student, course, 4, 1) && isDataComplete(student, course, 4, 2)) {
                                    if(total==4) {
                                        tempYear=5;
                                        tempStatus="REGULAR";
                                        return true;
                                    }
                                    if(isDataNoContent(student, course, 5, 1) && isDataNoContent(student, course, 5, 2)) {
                                        if(total==5) {
                                            tempYear=5;
                                            tempStatus="REGULAR";
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
            }
        }

        return isValid;
    }

    private static boolean regular2ndSemester(Student student, Course course) {
        final boolean isValid = false;
        final int total = course.getTotalYear();
        for(int i=0; i<total; i++) {
            switch(i+1) {
                case 1:
                    if(isDataComplete(student, course, 1, 1) && isDataNoContent(student, course, 1, 2)) {
                        if(total==1) {
                            tempYear=1;
                            tempStatus="REGULAR";
                            return true;
                        }
                        if(isDataNoContent(student, course, 2, 1) && isDataNoContent(student, course, 2, 2)) {
                            if(total==2) {
                                tempYear=1;
                                tempStatus="REGULAR";
                                return true;
                            }
                            if(isDataNoContent(student, course, 3, 1) && isDataNoContent(student, course, 3, 2)) {
                                if(total==3) {
                                    tempYear=1;
                                    tempStatus="REGULAR";
                                    return true;
                                }
                                if(isDataNoContent(student, course, 4, 1) && isDataNoContent(student, course, 4, 2)) {
                                    if(total==4) {
                                        tempYear=1;
                                        tempStatus="REGULAR";
                                        return true;
                                    }
                                    if(isDataNoContent(student, course, 5, 1) && isDataNoContent(student, course, 5, 2)) {
                                        if(total==5) {
                                            tempYear=1;
                                            tempStatus="REGULAR";
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 2:
                    if(isDataComplete(student, course, 1, 1) && isDataComplete(student, course, 1, 2)) {
                        if(total==1) {
                            tempYear=2;
                            tempStatus="REGULAR";
                            return true;
                        }
                        if(isDataComplete(student, course, 2, 1) && isDataNoContent(student, course, 2, 2)) {
                            if(total==2) {
                                tempYear=2;
                                tempStatus="REGULAR";
                                return true;
                            }
                            if(isDataNoContent(student, course, 3, 1) && isDataNoContent(student, course, 3, 2)) {
                                if(total==3) {
                                    tempYear=2;
                                    tempStatus="REGULAR";
                                    return true;
                                }
                                if(isDataNoContent(student, course, 4, 1) && isDataNoContent(student, course, 4, 2)) {
                                    if(total==4) {
                                        tempYear=2;
                                        tempStatus="REGULAR";
                                        return true;
                                    }
                                    if(isDataNoContent(student, course, 5, 1) && isDataNoContent(student, course, 5, 2)) {
                                        if(total==5) {
                                            tempYear=2;
                                            tempStatus="REGULAR";
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 3:
                    if(isDataComplete(student, course, 1, 1) && isDataComplete(student, course, 1, 2)) {
                        if(total==1) {
                            tempYear=3;
                            tempStatus="REGULAR";
                            return true;
                        }
                        if(isDataComplete(student, course, 2, 1) && isDataComplete(student, course, 2, 2)) {
                            if(total==2) {
                                tempYear=3;
                                tempStatus="REGULAR";
                                return true;
                            }
                            if(isDataComplete(student, course, 3, 1) && isDataNoContent(student, course, 3, 2)) {
                                if(total==3) {
                                    tempYear=3;
                                    tempStatus="REGULAR";
                                    return true;
                                }
                                if(isDataNoContent(student, course, 4, 1) && isDataNoContent(student, course, 4, 2)) {
                                    if(total==4) {
                                        tempYear=3;
                                        tempStatus="REGULAR";
                                        return true;
                                    }
                                    if(isDataNoContent(student, course, 5, 1) && isDataNoContent(student, course, 5, 2)) {
                                        if(total==5) {
                                            tempYear=3;
                                            tempStatus="REGULAR";
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 4:
                    if(isDataComplete(student, course, 1, 1) && isDataComplete(student, course, 1, 2)) {
                        if(total==1) {
                            tempYear=4;
                            tempStatus="REGULAR";
                            return true;
                        }
                        if(isDataComplete(student, course, 2, 1) && isDataComplete(student, course, 2, 2)) {
                            if(total==2) {
                                tempYear=4;
                                tempStatus="REGULAR";
                                return true;
                            }
                            if(isDataComplete(student, course, 3, 1) && isDataComplete(student, course, 3, 2)) {
                                if(total==3) {
                                    tempYear=4;
                                    tempStatus="REGULAR";
                                    return true;
                                }
                                if(isDataComplete(student, course, 4, 1) && isDataNoContent(student, course, 4, 2)) {
                                    if(total==4) {
                                        tempYear=4;
                                        tempStatus="REGULAR";
                                        return true;
                                    }
                                    if(isDataNoContent(student, course, 5, 1) && isDataNoContent(student, course, 5, 2)) {
                                        if(total==5) {
                                            tempYear=4;
                                            tempStatus="REGULAR";
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 5:
                    if(isDataComplete(student, course, 1, 1) && isDataComplete(student, course, 1, 2)) {
                        if(total==1) {
                            tempYear=5;
                            tempStatus="REGULAR";
                            return true;
                        }
                        if(isDataComplete(student, course, 2, 1) && isDataComplete(student, course, 2, 2)) {
                            if(total==2) {
                                tempYear=5;
                                tempStatus="REGULAR";
                                return true;
                            }
                            if(isDataComplete(student, course, 3, 1) && isDataComplete(student, course, 3, 2)) {
                                if(total==3) {
                                    tempYear=5;
                                    tempStatus="REGULAR";
                                    return true;
                                }
                                if(isDataComplete(student, course, 4, 1) && isDataComplete(student, course, 4, 2)) {
                                    if(total==4) {
                                        tempYear=5;
                                        tempStatus="REGULAR";
                                        return true;
                                    }
                                    if(isDataComplete(student, course, 5, 1) && isDataNoContent(student, course, 5, 2)) {
                                        if(total==5) {
                                            tempYear=5;
                                            tempStatus="REGULAR";
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
            }
        }

        return isValid;
    }

    public static List<Subject> list() {
        return AVAILABLE_LIST;
    }

    public static void clear() {
        AVAILABLE_LIST.clear();
    }
}
