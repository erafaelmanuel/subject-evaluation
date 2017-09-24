package com.erm.project.ees.util;

import com.erm.project.ees.dao.CurriculumDao;
import com.erm.project.ees.dao.DirtyDao;
import com.erm.project.ees.dao.impl.CourseDaoImpl;
import com.erm.project.ees.dao.impl.CurriculumDaoImpl;
import com.erm.project.ees.dao.impl.DirtyDaoImpl;
import com.erm.project.ees.dao.impl.SectionDaoImpl;
import com.erm.project.ees.model.*;

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

            for(int year=1; year<=section.getYear() + 1; year++){
                for(int sem=1; sem<=course.getTotalSemester(); sem++) {

                    List<Subject> currSubList = dirtyDao.getCurriculumSubjectList(course.getId(), year, sem);
                    for(Subject subject : currSubList) {
                        StudentSubjectRecord subjectRecord = dirtyDao.getStudentSubjectRecordById(student.getId(),
                                subject.getId());
                        //Not
                        if(subjectRecord == null) {
                            //Prerequizite
                            List<Subject> subjectPrerequisite = AssessmentHelper.prerequisiteOf(subject.getId());

                            //Flag
                            boolean isValid = true;

                            //
                            if(subjectPrerequisite.size() > 0) {
                                //Iterate the subjects list of prerequisite
                                for(Subject s : subjectPrerequisite) {
                                    StudentSubjectRecord subRecord = dirtyDao.getStudentSubjectRecordById(student.getId(), s.getId());
                                    if(subRecord == null)
                                        isValid = false;
                                    else if(subRecord.getMark().equalsIgnoreCase("FAILED"))
                                        isValid = false;
                                }
                            }
                            if(isValid)
                                subjectList.add(subject);
                            continue;
                        }
                        //
                        if(subjectRecord.getMark().equalsIgnoreCase("FAILED")) {
                            subjectList.add(subject);
                            continue;
                        }
                    }
                }
            }

            System.out.println(subjectList);

            return subjectList;
        } catch (Exception e) {
            e.printStackTrace();
            return subjectList;
        }
    }

    public static List<Subject> getSubjectList(Student student, int iSem) {
        List<Subject> subjectList = new ArrayList<>();
        try {
            final CurriculumDao curriculumDao = new CurriculumDaoImpl();
            final DirtyDao dirtyDao = new DirtyDaoImpl();
            Course course = new CourseDaoImpl().getCourseById(student.getCourseId());
            Section section = new SectionDaoImpl().getSectionById(student.getSectionId());

            for(int year=1; year<=section.getYear() + 1; year++){
                List<Subject> currSubList = dirtyDao.getCurriculumSubjectList(course.getId(), year, iSem);
                for(Subject subject : currSubList) {
                    StudentSubjectRecord subjectRecord = dirtyDao.getStudentSubjectRecordById(student.getId(),
                            subject.getId());
                    //Not
                    if(subjectRecord == null) {
                        //Prerequizite
                        List<Subject> subjectPrerequisite = AssessmentHelper.prerequisiteOf(subject.getId());

                        //Flag
                        boolean isValid = true;

                        //
                        if(subjectPrerequisite.size() > 0) {
                            //Iterate the subjects list of prerequisite
                            for(Subject s : subjectPrerequisite) {
                                StudentSubjectRecord subRecord = dirtyDao.getStudentSubjectRecordById(student.getId(), s.getId());
                                if(subRecord == null)
                                    isValid = false;
                                else if(subRecord.getMark().equalsIgnoreCase("FAILED"))
                                    isValid = false;
                            }
                        }
                        if(isValid)
                            subjectList.add(subject);
                        continue;
                    }
                    //
                    if(subjectRecord.getMark().equalsIgnoreCase("FAILED")) {
                        subjectList.add(subject);
                        continue;
                    }
                }
            }

            System.out.println(subjectList);

            return subjectList;
        } catch (Exception e) {
            e.printStackTrace();
            return subjectList;
        }
    }

    public static List<Subject> getSubjectList(Student student, int cYear, int iSem) {
        List<Subject> subjectList = new ArrayList<>();
        try {
            final CurriculumDao curriculumDao = new CurriculumDaoImpl();
            final DirtyDao dirtyDao = new DirtyDaoImpl();
            Course course = new CourseDaoImpl().getCourseById(student.getCourseId());
            Section section = new SectionDaoImpl().getSectionById(student.getSectionId());

            List<Subject> currSubList = dirtyDao.getCurriculumSubjectList(course.getId(), cYear, iSem);
            for(Subject subject : currSubList) {
                StudentSubjectRecord subjectRecord = dirtyDao.getStudentSubjectRecordById(student.getId(),
                        subject.getId());
                //Not
                if(subjectRecord == null) {
                    //Prerequizite
                    List<Subject> subjectPrerequisite = AssessmentHelper.prerequisiteOf(subject.getId());

                    //Flag
                    boolean isValid = true;

                    //
                    if(subjectPrerequisite.size() > 0) {
                        //Iterate the subjects list of prerequisite
                        for(Subject s : subjectPrerequisite) {
                            StudentSubjectRecord subRecord = dirtyDao.getStudentSubjectRecordById(student.getId(), s.getId());
                            if(subRecord == null)
                                isValid = false;
                            else if(subRecord.getMark().equalsIgnoreCase("FAILED"))
                                isValid = false;
                        }
                    }
                    if(isValid)
                        subjectList.add(subject);
                    continue;
                }
                //
                if(subjectRecord.getMark().equalsIgnoreCase("FAILED")) {
                    subjectList.add(subject);
                    continue;
                }
            }
            System.out.println(subjectList);

            return subjectList;
        } catch (Exception e) {
            e.printStackTrace();
            return subjectList;
        }
    }

    public static List<Subject> prerequisiteOf(long subjectId) {
        try {
            return new DirtyDaoImpl().getPrerequisiteBySujectId(subjectId);
        } catch (Exception e ) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}