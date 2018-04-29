package io.ermdev.ees.model.recursive;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Mark extends RecursiveTreeObject<Mark> {

    private SimpleLongProperty subjectId;
    private SimpleStringProperty subjectName;
    private SimpleStringProperty subjectDesc;
    private SimpleDoubleProperty midterm;
    private SimpleDoubleProperty finalterm;
    private SimpleStringProperty mark;

    public Mark(long subjectId, String subjectName, String subjectDesc, double midterm, double finalterm, String mark) {
        this.subjectId = new SimpleLongProperty(subjectId);
        this.subjectName = new SimpleStringProperty(subjectName);
        this.subjectDesc = new SimpleStringProperty(subjectDesc);
        this.midterm = new SimpleDoubleProperty(midterm);
        this.finalterm = new SimpleDoubleProperty(finalterm);
        this.mark = new SimpleStringProperty(mark);
    }

    public long getSubjectId() {
        return subjectId.get();
    }

    public SimpleLongProperty subjectIdProperty() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId.set(subjectId);
    }

    public String getSubjectName() {
        return subjectName.get();
    }

    public SimpleStringProperty subjectNameProperty() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName.set(subjectName);
    }

    public String getSubjectDesc() {
        return subjectDesc.get();
    }

    public SimpleStringProperty subjectDescProperty() {
        return subjectDesc;
    }

    public void setSubjectDesc(String subjectDesc) {
        this.subjectDesc.set(subjectDesc);
    }

    public double getMidterm() {
        return midterm.get();
    }

    public SimpleDoubleProperty midtermProperty() {
        return midterm;
    }

    public void setMidterm(double midterm) {
        this.midterm.set(midterm);
    }

    public double getFinalterm() {
        return finalterm.get();
    }

    public SimpleDoubleProperty finaltermProperty() {
        return finalterm;
    }

    public void setFinalterm(double finalterm) {
        this.finalterm.set(finalterm);
    }

    public String getMark() {
        return mark.get();
    }

    public SimpleStringProperty markProperty() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark.set(mark);
    }
}
