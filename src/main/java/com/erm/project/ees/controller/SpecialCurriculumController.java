package com.erm.project.ees.controller;

import com.erm.project.ees.dao.CourseDao;
import com.erm.project.ees.dao.CurriculumDao;
import com.erm.project.ees.dao.impl.CourseDaoImpl;
import com.erm.project.ees.dao.impl.CurriculumDaoImpl;
import com.erm.project.ees.model.Course;
import com.erm.project.ees.model.Curriculum;
import com.erm.project.ees.model.recursive.Subject;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SpecialCurriculumController implements Initializable {


    @FXML
    private JFXComboBox<String> cbCourse;

    @FXML
    private JFXComboBox<String> cbType;

    @FXML
    private JFXComboBox<String> cbYS;

    @FXML
    private JFXTreeTableView<Subject> tblFrom;

    @FXML
    private JFXTreeTableView<Subject> tblTo;

    private final CourseDao courseDao = new CourseDaoImpl();
    private final CurriculumDao curriculumDao = new CurriculumDaoImpl();

    private final List<Course> COURSE_LIST = new ArrayList<>();

    private final ObservableList<Subject> SUBJECT_LIST = FXCollections.observableArrayList();
    private final ObservableList<Subject> SUBJECT_LIST_TO = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        for(Course course : courseDao.getCourseList()) {
            cbCourse.getItems().add(course.getName());
            COURSE_LIST.add(course);
        }

        initTable();
        initTable2();

        cbCourse.getSelectionModel().select(0);
        cbType.getItems().add("SATURDAY CLASS");
        cbType.getItems().add("SUMMER CLASS");
        cbType.getSelectionModel().select(0);

        final int index = cbCourse.getSelectionModel().getSelectedIndex();
        cbYS.setItems(FXCollections.observableArrayList());
        for(int year=1; year<=COURSE_LIST.get(index).getTotalYear(); year++) {
            for(int sem=1; sem<=COURSE_LIST.get(index).getTotalSemester(); sem++) {
                cbYS.getItems().add(format(year) + " YEAR / " + format(sem) + " SEMESTER");
            }
        }
        cbYS.getSelectionModel().select(0);

        SUBJECT_LIST.clear();
        Curriculum c = getCurriculumByYearAndSem(COURSE_LIST.get(index).getId(), 0);
        for (com.erm.project.ees.model.Subject subject : curriculumDao.getSubjectList(c.getId())) {
            SUBJECT_LIST.add(new Subject(subject.getId(), subject.getName(), subject.getDesc(), subject.getUnit()));
        }
        TreeItem<Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST, RecursiveTreeObject::getChildren);
        tblFrom.setRoot(root);
        tblFrom.setShowRoot(false);
    }
    @FXML
    private void onChooseCourse() {
        final int index = cbCourse.getSelectionModel().getSelectedIndex();
        cbYS.setItems(FXCollections.observableArrayList());
        for(int year=1; year<=COURSE_LIST.get(index).getTotalYear(); year++) {
            for(int sem=1; sem<=COURSE_LIST.get(index).getTotalSemester(); sem++) {
                cbYS.getItems().add(format(year) + " YEAR / " + format(sem) + " SEMESTER");
            }
        }
        cbYS.getSelectionModel().select(0);

        SUBJECT_LIST.clear();
        Curriculum c = getCurriculumByYearAndSem(COURSE_LIST.get(index).getId(), 0);
        for (com.erm.project.ees.model.Subject subject : curriculumDao.getSubjectList(c.getId())) {
            SUBJECT_LIST.add(new Subject(subject.getId(), subject.getName(), subject.getDesc(), subject.getUnit()));
        }
        TreeItem<Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST, RecursiveTreeObject::getChildren);
        tblFrom.setRoot(root);
        tblFrom.setShowRoot(false);
    }

    @FXML
    private void onChooseYS() {
        final int index = cbCourse.getSelectionModel().getSelectedIndex();
        SUBJECT_LIST.clear();
        Curriculum c = getCurriculumByYearAndSem(COURSE_LIST.get(index).getId(), cbYS.getSelectionModel().getSelectedIndex());
        for (com.erm.project.ees.model.Subject subject : curriculumDao.getSubjectList(c.getId())) {
            SUBJECT_LIST.add(new Subject(subject.getId(), subject.getName(), subject.getDesc(), subject.getUnit()));
        }
        TreeItem<Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST, RecursiveTreeObject::getChildren);
        tblFrom.setRoot(root);
        tblFrom.setShowRoot(false);
    }

    @FXML
    private void onClickAdd() {
        final int index = tblFrom.getSelectionModel().getSelectedIndex();
        if(index > -1) {
            Subject subject = SUBJECT_LIST.get(index);
            for(Subject s : SUBJECT_LIST_TO) {
                if(s.getId() == subject.getId()) {
                    new Thread(()-> JOptionPane.showMessageDialog(null, "Already exist")).start();
                    return;
                }
            }
            SUBJECT_LIST_TO.add(subject);
            TreeItem<Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST_TO, RecursiveTreeObject::getChildren);
            tblTo.setRoot(root);
            tblTo.setShowRoot(false);
        }
    }

    @FXML
    private void onClickRemove() {
        final int index = tblTo.getSelectionModel().getSelectedIndex();
        if(index > -1) {
            Subject subject = SUBJECT_LIST_TO.get(index);
            SUBJECT_LIST_TO.remove(subject);
            TreeItem<Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST_TO, RecursiveTreeObject::getChildren);
            tblTo.setRoot(root);
            tblTo.setShowRoot(false);
        }
    }

    public void initTable() {
        TreeItem<Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST,
                RecursiveTreeObject::getChildren);

        JFXTreeTableColumn<Subject, Long> idCol = new JFXTreeTableColumn<>("ID");
        idCol.setResizable(false);
        idCol.setPrefWidth(80);
        idCol.setCellValueFactory(param -> param.getValue().getValue().idProperty().asObject());

        JFXTreeTableColumn<com.erm.project.ees.model.recursive.Subject, String> nameCol = new JFXTreeTableColumn<>("Subject");
        nameCol.setResizable(false);
        nameCol.setPrefWidth(130);
        nameCol.setCellValueFactory(param -> param.getValue().getValue().nameProperty());

//        JFXTreeTableColumn<com.erm.project.ees.model.recursive.Subject, String> descCol = new JFXTreeTableColumn<>("Description");
//        descCol.setResizable(false);
//        descCol.setPrefWidth(210);
//        descCol.setCellValueFactory(param -> param.getValue().getValue().descProperty());

        JFXTreeTableColumn<com.erm.project.ees.model.recursive.Subject, Integer> unitCol = new JFXTreeTableColumn<>("Unit");
        unitCol.setResizable(false);
        unitCol.setPrefWidth(80);
        unitCol.setCellValueFactory(param -> param.getValue().getValue().unitProperty().asObject());

        tblFrom.getColumns().add(idCol);
        tblFrom.getColumns().add(nameCol);
        //tblFrom.getColumns().add(descCol);
        tblFrom.getColumns().add(unitCol);

        tblFrom.setRoot(root);
        tblFrom.setShowRoot(false);
    }

    public void initTable2() {
        TreeItem<Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST_TO, RecursiveTreeObject::getChildren);

        JFXTreeTableColumn<Subject, Long> idCol = new JFXTreeTableColumn<>("ID");
        idCol.setResizable(false);
        idCol.setPrefWidth(80);
        idCol.setCellValueFactory(param -> param.getValue().getValue().idProperty().asObject());

        JFXTreeTableColumn<com.erm.project.ees.model.recursive.Subject, String> nameCol = new JFXTreeTableColumn<>("Subject");
        nameCol.setResizable(false);
        nameCol.setPrefWidth(130);
        nameCol.setCellValueFactory(param -> param.getValue().getValue().nameProperty());

//        JFXTreeTableColumn<com.erm.project.ees.model.recursive.Subject, String> descCol = new JFXTreeTableColumn<>("Description");
//        descCol.setResizable(false);
//        descCol.setPrefWidth(210);
//        descCol.setCellValueFactory(param -> param.getValue().getValue().descProperty());

        JFXTreeTableColumn<com.erm.project.ees.model.recursive.Subject, Integer> unitCol = new JFXTreeTableColumn<>("Unit");
        unitCol.setResizable(false);
        unitCol.setPrefWidth(80);
        unitCol.setCellValueFactory(param -> param.getValue().getValue().unitProperty().asObject());

        tblTo.getColumns().add(idCol);
        tblTo.getColumns().add(nameCol);
        //tblFrom.getColumns().add(descCol);
        tblTo.getColumns().add(unitCol);

        tblTo.setRoot(root);
        tblTo.setShowRoot(false);
    }

    public String format(int num) {
        switch (num) {
            case 1: return "1ST";
            case 2: return "2ND";
            case 3: return "3RD";
            default: return num + "TH";
        }
    }

    public Curriculum getCurriculumByYearAndSem(long courseId, int index) {
        switch (index) {
            case 0:
                return curriculumDao.getCurriculumListByCourseId(courseId, 1, 1);
            case 1:
                return curriculumDao.getCurriculumListByCourseId(courseId, 1, 2);
            case 2:
                return curriculumDao.getCurriculumListByCourseId(courseId, 2, 1);
            case 3:
                return curriculumDao.getCurriculumListByCourseId(courseId, 2, 2);
            case 4:
                return curriculumDao.getCurriculumListByCourseId(courseId, 3, 1);
            case 5:
                return curriculumDao.getCurriculumListByCourseId(courseId, 4, 2);
            case 6:
                return curriculumDao.getCurriculumListByCourseId(courseId, 5, 1);
            case 7:
                return curriculumDao.getCurriculumListByCourseId(courseId, 6, 2);
            case 8:
                return curriculumDao.getCurriculumListByCourseId(courseId, 7, 1);
            case 9:
                return curriculumDao.getCurriculumListByCourseId(courseId, 8, 2);
        }
        return new Curriculum();
    }
}
