package io.erm.ees.util;

import io.erm.ees.dao.CurriculumDao;
import io.erm.ees.dao.DirtyDao;
import io.erm.ees.dao.impl.CourseDaoImpl;
import io.erm.ees.dao.impl.CurriculumDaoImpl;
import io.erm.ees.dao.impl.DirtyDaoImpl;
import io.erm.ees.dao.impl.SectionDaoImpl;
import io.erm.ees.model.*;

import java.util.ArrayList;
import java.util.List;

public class AssessmentHelper {

    @Deprecated
    public static List<Subject> getSubjectList(Student student) {
        List<Subject> subjectList = new ArrayList<>();
        try {
            final CurriculumDao curriculumDao = new CurriculumDaoImpl();
            final DirtyDao dirtyDao = new DirtyDaoImpl();
            Course course = new CourseDaoImpl().getCourseById(student.getCourseId());
            Section section = new SectionDaoImpl().getSectionById(student.getSectionId());

            for (int year = 1; year <= section.getYear() + 1; year++) {
                for (int sem = 1; sem <= course.getTotalSemester(); sem++) {

                    List<Subject> currSubList = dirtyDao.getCurriculumSubjectList(course.getId(), year, sem);
                    for (Subject subject : currSubList) {
                        StudentSubjectRecord subjectRecord = dirtyDao.getStudentSubjectRecordById(student.getId(),
                                subject.getId());
                        //Not
                        if (subjectRecord == null) {
                            //Prerequizite
                            List<Subject> subjectPrerequisite = AssessmentHelper.prerequisiteOf(subject.getId());

                            //Flag
                            boolean isValid = true;

                            //
                            if (subjectPrerequisite.size() > 0) {
                                //Iterate the subjects list of prerequisite
                                for (Subject s : subjectPrerequisite) {
                                    StudentSubjectRecord subRecord = dirtyDao.getStudentSubjectRecordById(student.getId(), s.getId());
                                    if (subRecord == null)
                                        isValid = false;
                                    else if (subRecord.getMark().equalsIgnoreCase("FAILED"))
                                        isValid = false;
                                }
                            }
                            if (isValid)
                                subjectList.add(subject);
                            continue;
                        }
                        //
                        if (subjectRecord.getMark().equalsIgnoreCase("FAILED")) {
                            subjectList.add(subject);
                            continue;
                        }
                    }
                }
            }

            return subjectList;
        } catch (Exception e) {
            e.printStackTrace();
            return subjectList;
        }
    }

    @Deprecated
    public static List<Subject> getSpecialSubjectList(Student student, String type) {
        List<Subject> subjectList = new ArrayList<>();
        try {
            final CurriculumDao curriculumDao = new CurriculumDaoImpl();
            final DirtyDao dirtyDao = new DirtyDaoImpl();
            Course course = new CourseDaoImpl().getCourseById(student.getCourseId());
            Section section = new SectionDaoImpl().getSectionById(student.getSectionId());

            for (int year = 1; year <= course.getTotalYear(); year++) {
                List<Subject> currSubList = dirtyDao.getSpecialCurriculumSubjectList(course.getId(), year, 1, type);
                for (Subject subject : currSubList) {
                    StudentSubjectRecord subjectRecord = dirtyDao.getStudentSubjectRecordById(student.getId(),
                            subject.getId());
                    //Not
                    if (subjectRecord == null) {
                        //Prerequizite
                        List<Subject> subjectPrerequisite = AssessmentHelper.prerequisiteOf(subject.getId());

                        //Flag
                        boolean isValid = true;

                        //
                        if (subjectPrerequisite.size() > 0) {
                            //Iterate the subjects list of prerequisite
                            for (Subject s : subjectPrerequisite) {
                                StudentSubjectRecord subRecord = dirtyDao.getStudentSubjectRecordById(student.getId(), s.getId());
                                if (subRecord == null)
                                    isValid = false;
                                else if (subRecord.getMark().equalsIgnoreCase("FAILED"))
                                    isValid = false;
                            }
                        }
                        if (isValid)
                            subjectList.add(subject);
                        continue;
                    }
                    //
                    if (subjectRecord.getMark().equalsIgnoreCase("FAILED")) {
                        subjectList.add(subject);
                        continue;
                    }
                }
            }

            return subjectList;
        } catch (Exception e) {
            e.printStackTrace();
            return subjectList;
        }
    }

    public static List<Subject> getSpecialSubjectListWithFilter(Student student, String type, int filterYear) {
        List<Subject> subjectList = new ArrayList<>();
        try {
            final CurriculumDao curriculumDao = new CurriculumDaoImpl();
            final DirtyDao dirtyDao = new DirtyDaoImpl();
            Course course = new CourseDaoImpl().getCourseById(student.getCourseId());
            Section section = new SectionDaoImpl().getSectionById(student.getSectionId());

            for (int year = 1; year <= course.getTotalYear(); year++) {
                if (year > filterYear)
                    continue;
                List<Subject> currSubList = dirtyDao.getSpecialCurriculumSubjectList(course.getId(), year, 1, type);
                for (Subject subject : currSubList) {
                    StudentSubjectRecord subjectRecord = dirtyDao.getStudentSubjectRecordById(student.getId(),
                            subject.getId());
                    //Not
                    if (subjectRecord == null) {
                        //Prerequizite
                        List<Subject> subjectPrerequisite = AssessmentHelper.prerequisiteOf(subject.getId());

                        //Flag
                        boolean isValid = true;

                        //
                        if (subjectPrerequisite.size() > 0) {
                            //Iterate the subjects list of prerequisite
                            for (Subject s : subjectPrerequisite) {
                                StudentSubjectRecord subRecord = dirtyDao.getStudentSubjectRecordById(student.getId(), s.getId());
                                if (subRecord == null)
                                    isValid = false;
                                else if (subRecord.getMark().equalsIgnoreCase("FAILED"))
                                    isValid = false;
                            }
                        }
                        if (isValid)
                            subjectList.add(subject);
                        continue;
                    }
                    //
                    if (subjectRecord.getMark().equalsIgnoreCase("FAILED")) {
                        subjectList.add(subject);
                        continue;
                    }
                }
            }

            return subjectList;
        } catch (Exception e) {
            e.printStackTrace();
            return subjectList;
        }
    }


    @Deprecated
    public static List<Subject> getSubjectList(Student student, int iSem) {
        List<Subject> subjectList = new ArrayList<>();
        try {
            final CurriculumDao curriculumDao = new CurriculumDaoImpl();
            final DirtyDao dirtyDao = new DirtyDaoImpl();
            Course course = new CourseDaoImpl().getCourseById(student.getCourseId());
            Section section = new SectionDaoImpl().getSectionById(student.getSectionId());

            for (int year = 1; year <= course.getTotalYear(); year++) {
                List<Subject> currSubList = dirtyDao.getCurriculumSubjectList(course.getId(), year, iSem);
                for (Subject subject : currSubList) {
                    StudentSubjectRecord subjectRecord = dirtyDao.getStudentSubjectRecordById(student.getId(),
                            subject.getId());
                    //Not
                    if (subjectRecord == null) {
                        //Prerequizite
                        List<Subject> subjectPrerequisite = AssessmentHelper.prerequisiteOf(subject.getId());

                        //Flag
                        boolean isValid = true;

                        //
                        if (subjectPrerequisite.size() > 0) {
                            //Iterate the subjects list of prerequisite
                            for (Subject s : subjectPrerequisite) {
                                StudentSubjectRecord subRecord = dirtyDao.getStudentSubjectRecordById(student.getId(), s.getId());
                                if (subRecord == null)
                                    isValid = false;
                                else if (subRecord.getMark().equalsIgnoreCase("FAILED"))
                                    isValid = false;
                            }
                        }
                        if (isValid)
                            subjectList.add(subject);
                        continue;
                    }
                    //
                    if (subjectRecord.getMark().equalsIgnoreCase("FAILED")) {
                        subjectList.add(subject);
                        continue;
                    }
                }
            }

            return subjectList;
        } catch (Exception e) {
            e.printStackTrace();
            return subjectList;
        }
    }

    public static List<Subject> getSubjectListWithFilter(Student student, int iSem, int filterYear) {
        List<Subject> subjectList = new ArrayList<>();
        try {
            final CurriculumDao curriculumDao = new CurriculumDaoImpl();
            final DirtyDao dirtyDao = new DirtyDaoImpl();
            Course course = new CourseDaoImpl().getCourseById(student.getCourseId());
            Section section = new SectionDaoImpl().getSectionById(student.getSectionId());

            for (int year = 1; year <= course.getTotalYear(); year++) {
                if (year > filterYear)
                    break;
                List<Subject> currSubList = dirtyDao.getCurriculumSubjectList(course.getId(), year, iSem);
                for (Subject subject : currSubList) {
                    StudentSubjectRecord subjectRecord = dirtyDao.getStudentSubjectRecordById(student.getId(),
                            subject.getId());
                    //Not
                    if (subjectRecord == null) {
                        //Prerequizite
                        List<Subject> subjectPrerequisite = AssessmentHelper.prerequisiteOf(subject.getId());

                        //Flag
                        boolean isValid = true;

                        //
                        if (subjectPrerequisite.size() > 0) {
                            //Iterate the subjects list of prerequisite
                            for (Subject s : subjectPrerequisite) {
                                StudentSubjectRecord subRecord = dirtyDao.getStudentSubjectRecordById(student.getId(), s.getId());
                                if (subRecord == null)
                                    isValid = false;
                                    //Remove from list if not pass
                                else if (!subRecord.getMark().equalsIgnoreCase("PASSED"))
                                    isValid = false;
                            }
                        }
                        if (isValid)
                            subjectList.add(subject);
                        continue;
                    }
                    //If passed and ongoing shoud'not add to the available list
                    if (!subjectRecord.getMark().equalsIgnoreCase("PASSED") &&
                            !subjectRecord.getMark().equalsIgnoreCase("ONGOING")) {
                        subjectList.add(subject);
                        continue;
                    }
                }
            }

            return subjectList;
        } catch (Exception e) {
            e.printStackTrace();
            return subjectList;
        }
    }

    @Deprecated
    public static List<Subject> getSpecialSubjectList(Student student, int year, String type) {
        List<Subject> subjectList = new ArrayList<>();
        try {
            final CurriculumDao curriculumDao = new CurriculumDaoImpl();
            final DirtyDao dirtyDao = new DirtyDaoImpl();
            Course course = new CourseDaoImpl().getCourseById(student.getCourseId());
            Section section = new SectionDaoImpl().getSectionById(student.getSectionId());

            List<Subject> currSubList = dirtyDao.getSpecialCurriculumSubjectList(course.getId(), year, 1, type);
            for (Subject subject : currSubList) {
                StudentSubjectRecord subjectRecord = dirtyDao.getStudentSubjectRecordById(student.getId(),
                        subject.getId());
                //Not
                if (subjectRecord == null) {
                    //Prerequizite
                    List<Subject> subjectPrerequisite = AssessmentHelper.prerequisiteOf(subject.getId());

                    //Flag
                    boolean isValid = true;

                    //
                    if (subjectPrerequisite.size() > 0) {
                        //Iterate the subjects list of prerequisite
                        for (Subject s : subjectPrerequisite) {
                            StudentSubjectRecord subRecord = dirtyDao.getStudentSubjectRecordById(student.getId(), s.getId());
                            if (subRecord == null)
                                isValid = false;
                            else if (subRecord.getMark().equalsIgnoreCase("FAILED"))
                                isValid = false;
                        }
                    }
                    if (isValid)
                        subjectList.add(subject);
                    continue;
                }
                //
                if (subjectRecord.getMark().equalsIgnoreCase("FAILED")) {
                    subjectList.add(subject);
                    continue;
                }
            }

            return subjectList;
        } catch (Exception e) {
            e.printStackTrace();
            return subjectList;
        }
    }

    public static List<Subject> getSpecialSubjectListWithFilter(Student student, int year, String type, int filterYear) {
        List<Subject> subjectList = new ArrayList<>();
        try {
            final CurriculumDao curriculumDao = new CurriculumDaoImpl();
            final DirtyDao dirtyDao = new DirtyDaoImpl();
            Course course = new CourseDaoImpl().getCourseById(student.getCourseId());
            Section section = new SectionDaoImpl().getSectionById(student.getSectionId());

            if (year > filterYear)
                return new ArrayList<>();

            List<Subject> currSubList = dirtyDao.getSpecialCurriculumSubjectList(course.getId(), year, 1, type);
            for (Subject subject : currSubList) {
                StudentSubjectRecord subjectRecord = dirtyDao.getStudentSubjectRecordById(student.getId(),
                        subject.getId());
                //Not
                if (subjectRecord == null) {
                    //Prerequizite
                    List<Subject> subjectPrerequisite = AssessmentHelper.prerequisiteOf(subject.getId());

                    //Flag
                    boolean isValid = true;

                    //
                    if (subjectPrerequisite.size() > 0) {
                        //Iterate the subjects list of prerequisite
                        for (Subject s : subjectPrerequisite) {
                            StudentSubjectRecord subRecord = dirtyDao.getStudentSubjectRecordById(student.getId(), s.getId());
                            if (subRecord == null)
                                isValid = false;
                                //Remove from list if not pass
                            else if (!subRecord.getMark().equalsIgnoreCase("PASSED"))
                                isValid = false;
                        }
                    }
                    if (isValid)
                        subjectList.add(subject);
                    continue;
                }
                //
                if (!subjectRecord.getMark().equalsIgnoreCase("PASSED") &&
                        !subjectRecord.getMark().equalsIgnoreCase("ONGOING")) {
                    subjectList.add(subject);
                    continue;
                }
            }

            return subjectList;
        } catch (Exception e) {
            e.printStackTrace();
            return subjectList;
        }
    }

    @Deprecated
    public static List<Subject> getSubjectList(Student student, int cYear, int iSem) {
        List<Subject> subjectList = new ArrayList<>();
        try {
            final CurriculumDao curriculumDao = new CurriculumDaoImpl();
            final DirtyDao dirtyDao = new DirtyDaoImpl();
            Course course = new CourseDaoImpl().getCourseById(student.getCourseId());
            Section section = new SectionDaoImpl().getSectionById(student.getSectionId());

            List<Subject> currSubList = dirtyDao.getCurriculumSubjectList(course.getId(), cYear, iSem);
            for (Subject subject : currSubList) {
                StudentSubjectRecord subjectRecord = dirtyDao.getStudentSubjectRecordById(student.getId(),
                        subject.getId());
                //Not
                if (subjectRecord == null) {
                    //Prerequizite
                    List<Subject> subjectPrerequisite = AssessmentHelper.prerequisiteOf(subject.getId());

                    //Flag
                    boolean isValid = true;

                    //
                    if (subjectPrerequisite.size() > 0) {
                        //Iterate the subjects list of prerequisite
                        for (Subject s : subjectPrerequisite) {
                            StudentSubjectRecord subRecord = dirtyDao.getStudentSubjectRecordById(student.getId(), s.getId());
                            if (subRecord == null)
                                isValid = false;
                            else if (subRecord.getMark().equalsIgnoreCase("FAILED"))
                                isValid = false;
                        }
                    }
                    if (isValid)
                        subjectList.add(subject);
                    continue;
                }
                //
                if (subjectRecord.getMark().equalsIgnoreCase("FAILED")) {
                    subjectList.add(subject);
                    continue;
                }
            }

            return subjectList;
        } catch (Exception e) {
            e.printStackTrace();
            return subjectList;
        }
    }

    public static List<Subject> getSubjectListWithFilter(Student student, int cYear, int iSem, int filterYear) {
        List<Subject> subjectList = new ArrayList<>();
        try {
            final CurriculumDao curriculumDao = new CurriculumDaoImpl();
            final DirtyDao dirtyDao = new DirtyDaoImpl();
            Course course = new CourseDaoImpl().getCourseById(student.getCourseId());
            Section section = new SectionDaoImpl().getSectionById(student.getSectionId());

            if (cYear > filterYear)
                return new ArrayList<>();

            List<Subject> currSubList = dirtyDao.getCurriculumSubjectList(course.getId(), cYear, iSem);
            for (Subject subject : currSubList) {
                StudentSubjectRecord subjectRecord = dirtyDao.getStudentSubjectRecordById(student.getId(),
                        subject.getId());
                //Not
                if (subjectRecord == null) {
                    //Prerequizite
                    List<Subject> subjectPrerequisite = AssessmentHelper.prerequisiteOf(subject.getId());

                    //Flag
                    boolean isValid = true;

                    //
                    if (subjectPrerequisite.size() > 0) {
                        //Iterate the subjects list of prerequisite
                        for (Subject s : subjectPrerequisite) {
                            StudentSubjectRecord subRecord = dirtyDao.getStudentSubjectRecordById(student.getId(), s.getId());
                            if (subRecord == null)
                                isValid = false;
                            else if (subRecord.getMark().equalsIgnoreCase("FAILED"))
                                isValid = false;
                        }
                    }
                    if (isValid)
                        subjectList.add(subject);
                    continue;
                }
                //
                if (subjectRecord.getMark().equalsIgnoreCase("FAILED")) {
                    subjectList.add(subject);
                    continue;
                }
            }

            return subjectList;
        } catch (Exception e) {
            e.printStackTrace();
            return subjectList;
        }
    }

    public static List<Subject> prerequisiteOf(long subjectId) {
        try {
            return new DirtyDaoImpl().getPrerequisiteBySujectId(subjectId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
