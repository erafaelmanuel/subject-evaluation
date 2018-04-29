package io.ermdev.ees.model.recursive;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Student extends RecursiveTreeObject<Student> {

    private SimpleLongProperty number;
    private SimpleStringProperty fullName;
    private SimpleStringProperty course;
    private SimpleStringProperty section;

    public Student(long number, String fullName, String course, String section) {
        this.number = new SimpleLongProperty(number);
        this.fullName = new SimpleStringProperty(fullName);
        this.course = new SimpleStringProperty(course);
        this.section = new SimpleStringProperty(section);
    }

    public long getNumber() {
        return number.get();
    }

    public SimpleLongProperty numberProperty() {
        return number;
    }

    public void setNumber(long number) {
        this.number.set(number);
    }

    public String getFullName() {
        return fullName.get();
    }

    public SimpleStringProperty fullNameProperty() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName.set(fullName);
    }

    public String getCourse() {
        return course.get();
    }

    public SimpleStringProperty courseProperty() {
        return course;
    }

    public void setCourse(String course) {
        this.course.set(course);
    }

    public String getSection() {
        return section.get();
    }

    public SimpleStringProperty sectionProperty() {
        return section;
    }

    public void setSection(String section) {
        this.section.set(section);
    }
}
