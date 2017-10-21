package io.erm.ees.model;

import io.erm.ees.dao.CourseDao;
import io.erm.ees.dao.SectionDao;

public class Student {

    private long id;
    private long studentNumber;
    private String firstName;
    private String lastName;
    private String middleName;
    private int age;
    private String gender;
    private long contactNumber;
    private long sectionId;
    private long courseId;
    private String status;
    private CourseDao courseDao;
    private SectionDao sectionDao;

    public Student() {
        super();
    }

    public Student(long studentNumber, String firstName, String lastName, String middleName, int age, String gender,
                   long contactNumber, long sectionId, long courseId) {
        this.studentNumber = studentNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.age = age;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.sectionId = sectionId;
        this.courseId = courseId;
    }

    public Student(long id, long studentNumber, String firstName, String lastName, String middleName, int age,
                   String gender, int contactNumber, long sectionId, long courseId) {
        this.id = id;
        this.studentNumber = studentNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.age = age;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.sectionId = sectionId;
        this.courseId = courseId;
    }

    public Student(long id, long studentNumber, String firstName, String lastName, String middleName, int age,
                   String gender, int contactNumber, long sectionId, long courseId, String status) {
        this.id = id;
        this.studentNumber = studentNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.age = age;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.sectionId = sectionId;
        this.courseId = courseId;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(long studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public long getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(long contactNumber) {
        this.contactNumber = contactNumber;
    }

    public long getSectionId() {
        return sectionId;
    }

    public void setSectionId(long sectionId) {
        this.sectionId = sectionId;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDisplayCourse() {
        return courseDao.getCourseById(courseId).getName();
    }

    public String getDisplaySection() {
        final Section section = sectionDao.getSectionById(sectionId);
        return section.getYear() + "-" +section.getName();
    }

    public String getDisplayContactNumber() {
        return "+63 " + String.valueOf(contactNumber).substring(0,3)+"-"+
                String.valueOf(contactNumber).substring(3,7)+"-"+
                String.valueOf(contactNumber).substring(7);
    }

    public void setCourseDao(CourseDao courseDao) {
        this.courseDao=courseDao;
    }

    public void setSectionDao(SectionDao sectionDao) {
        this.sectionDao=sectionDao;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", studentNumber=" + studentNumber +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", contactNumber=" + contactNumber +
                ", sectionId=" + sectionId +
                ", courseId=" + courseId +
                ", status='" + status + '\'' +
                '}';
    }

    public enum STATUS {
        REGULAR, IRREGULAR
    }
}
