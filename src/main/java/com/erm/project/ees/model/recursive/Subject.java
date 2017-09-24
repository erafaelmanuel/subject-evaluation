package com.erm.project.ees.model.recursive;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Subject extends RecursiveTreeObject<Subject> {

    private SimpleLongProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty desc;
    private SimpleIntegerProperty unit;

    public Subject(long id, String name, String desc, int unit) {
        this.id = new SimpleLongProperty(id);
        this.name = new SimpleStringProperty(name);
        this.desc = new SimpleStringProperty(desc);
        this.unit = new SimpleIntegerProperty(unit);
    }

    public long getId() {
        return id.get();
    }

    public SimpleLongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getDesc() {
        return desc.get();
    }

    public SimpleStringProperty descProperty() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc.set(desc);
    }

    public int getUnit() {
        return unit.get();
    }

    public SimpleIntegerProperty unitProperty() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit.set(unit);
    }
}
