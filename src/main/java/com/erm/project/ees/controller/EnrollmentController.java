package com.erm.project.ees.controller;

import com.erm.project.ees.dao.impl.CourseDaoImpl;
import com.erm.project.ees.dao.impl.SectionDaoImpl;
import com.erm.project.ees.dao.impl.StudentDaoImpl;
import com.erm.project.ees.model.Student;
import com.erm.project.ees.model.recursive.Subject;
import com.erm.project.ees.util.AssessmentHelper;
import com.erm.project.ees.util.ResourceHelper;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EnrollmentController implements Initializable {

    @FXML
    private JFXTreeTableView<Subject> tblAbSubject;

    @FXML
    private JFXTreeTableView<Subject> tblYeSubject;

    @FXML
    private JFXComboBox<String> cbAbSubject;

    @FXML
    private ImageView imgLogo;

    @FXML
    private Label lbStudentNo;

    @FXML
    private Label lbCourse;

    @FXML
    private Label lbYear;

    @FXML
    private Label lbStatus;

    @FXML
    private Label lbfullName;

    @FXML
    private JFXComboBox<Integer> cbCurSemester;

    private Student student;

    private final ObservableList<Subject> ENROLL_SUBJECT_LIST = FXCollections.observableArrayList();
    private final ObservableList<Subject> AVAILABLE_SUBJECT_LIST = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        cbAbSubject.getItems().add("ALL");
        cbAbSubject.getItems().add("1ST YR");
        cbAbSubject.getItems().add("2ND YR");
        cbAbSubject.getItems().add("3RD YR");
        cbAbSubject.getItems().add("4TH YR");
        cbAbSubject.getSelectionModel().select(0);

        cbCurSemester.getItems().add(1);
        cbCurSemester.getItems().add(2);
        cbCurSemester.getSelectionModel().select(0);

        Image image = new Image(ResourceHelper.resource("/image/studentlogo.png").toString());
        imgLogo.setImage(image);

        clearAb();
        loadAbSubject(AssessmentHelper.getSubjectList(new StudentDaoImpl().getStudentById(1),
                cbCurSemester.getSelectionModel().getSelectedItem()));

        clearYe();
        loadYeSubject();
    }

    @FXML
    protected void onChooseFilter() {
        if(cbAbSubject.getSelectionModel().getSelectedIndex() == 0) {
            clearAb();
            loadAbSubject(AssessmentHelper.getSubjectList(new StudentDaoImpl().getStudentById(1),
                    cbCurSemester.getSelectionModel().getSelectedItem()));
        }else {
            clearAb();
            loadAbSubject(AssessmentHelper.getSubjectList(new StudentDaoImpl().getStudentById(1),
                    cbAbSubject.getSelectionModel().getSelectedIndex(), cbCurSemester.getSelectionModel().getSelectedItem()));
        }
    }

    @FXML
    protected void onChooseSemester() {
        if(cbAbSubject.getSelectionModel().getSelectedIndex() == 0) {
            clearAb();
            loadAbSubject(AssessmentHelper.getSubjectList(new StudentDaoImpl().getStudentById(1),
                    cbCurSemester.getSelectionModel().getSelectedItem()));
        }else {
            clearAb();
            loadAbSubject(AssessmentHelper.getSubjectList(new StudentDaoImpl().getStudentById(1),
                    cbAbSubject.getSelectionModel().getSelectedIndex(), cbCurSemester.getSelectionModel().getSelectedItem()));
        }
        removeAllYeSubject();
    }

    @FXML
    protected void onClickAdd() {
        if(tblAbSubject.getSelectionModel().getSelectedIndex() > -1) {
            Subject subject = tblAbSubject.getSelectionModel().getSelectedItem().getValue();
            addYeSubject(subject);
        }
    }

    @FXML
    protected void onClickAddAll() {
        addYeSubjectAll();
    }

    @FXML
    protected void onClickRemove() {
        if(tblYeSubject.getSelectionModel().getSelectedIndex() > -1) {
            removeYeSubject();
        }
    }

    @FXML
    protected void onClickRemoveAll() {
        removeAllYeSubject();
    }

    private void loadAbSubject(List<com.erm.project.ees.model.Subject> subjectList) {

        while(AVAILABLE_SUBJECT_LIST.size() > 0)
            AVAILABLE_SUBJECT_LIST.remove(0);

        for(com.erm.project.ees.model.Subject subject : subjectList) {
            AVAILABLE_SUBJECT_LIST.add(new Subject(subject.getId(), subject.getName(), subject.getDesc(), subject.getUnit()));
        }
        TreeItem<Subject> root = new RecursiveTreeItem<Subject>(AVAILABLE_SUBJECT_LIST, RecursiveTreeObject :: getChildren);

        JFXTreeTableColumn<Subject, Long> idCol = new JFXTreeTableColumn<>("ID");
        idCol.setResizable(false);
        idCol.setPrefWidth(80);
        idCol.setCellValueFactory(param -> param.getValue().getValue().idProperty().asObject());

        JFXTreeTableColumn<Subject, String> nameCol = new JFXTreeTableColumn<>("Subject");
        nameCol.setResizable(false);
        nameCol.setPrefWidth(130);
        nameCol.setCellValueFactory(param -> param.getValue().getValue().nameProperty());

        JFXTreeTableColumn<Subject, String> descCol = new JFXTreeTableColumn<>("Description");
        descCol.setResizable(false);
        descCol.setPrefWidth(210);
        descCol.setCellValueFactory(param -> param.getValue().getValue().descProperty());

        JFXTreeTableColumn<Subject, Integer> unitCol = new JFXTreeTableColumn<>("Unit");
        unitCol.setResizable(false);
        unitCol.setPrefWidth(80);
        unitCol.setCellValueFactory(param -> param.getValue().getValue().unitProperty().asObject());

        tblAbSubject.getColumns().add(idCol);
        tblAbSubject.getColumns().add(nameCol);
        tblAbSubject.getColumns().add(descCol);
        tblAbSubject.getColumns().add(unitCol);

        tblAbSubject.setRoot(root);
        tblAbSubject.setShowRoot(false);

    }

    private void loadYeSubject() {
        JFXTreeTableColumn<Subject, Long> idCol = new JFXTreeTableColumn<>("ID");
        idCol.setResizable(false);
        idCol.setPrefWidth(80);
        idCol.setCellValueFactory(param -> param.getValue().getValue().idProperty().asObject());

        JFXTreeTableColumn<Subject, String> nameCol = new JFXTreeTableColumn<>("Subject");
        nameCol.setResizable(false);
        nameCol.setPrefWidth(130);
        nameCol.setCellValueFactory(param -> param.getValue().getValue().nameProperty());

        JFXTreeTableColumn<Subject, String> descCol = new JFXTreeTableColumn<>("Description");
        descCol.setResizable(false);
        descCol.setPrefWidth(210);
        descCol.setCellValueFactory(param -> param.getValue().getValue().descProperty());

        JFXTreeTableColumn<Subject, Integer> unitCol = new JFXTreeTableColumn<>("Unit");
        unitCol.setResizable(false);
        unitCol.setPrefWidth(80);
        unitCol.setCellValueFactory(param -> param.getValue().getValue().unitProperty().asObject());

        tblYeSubject.getColumns().add(idCol);
        tblYeSubject.getColumns().add(nameCol);
        tblYeSubject.getColumns().add(descCol);
        tblYeSubject.getColumns().add(unitCol);
    }

    public void addYeSubject(Subject subject) {
        for(Subject s : ENROLL_SUBJECT_LIST) {
            if(s.getId() == subject.getId())
                return;
        }
        ENROLL_SUBJECT_LIST.add(new Subject(subject.getId(), subject.getName(), subject.getDesc(), subject.getUnit()));
        TreeItem<Subject> root = new RecursiveTreeItem<Subject>(ENROLL_SUBJECT_LIST, RecursiveTreeObject :: getChildren);

        tblYeSubject.setRoot(root);
        tblYeSubject.setShowRoot(false);
    }

    public void addYeSubjectAll() {
        int size = AVAILABLE_SUBJECT_LIST.size();
        first:for(int i=0; i<size; i++) {
            Subject subject = AVAILABLE_SUBJECT_LIST.get(i);
            for(Subject s : ENROLL_SUBJECT_LIST) {
                if(s.getId() == subject.getId())
                    continue first;
            }
            ENROLL_SUBJECT_LIST.add(new Subject(subject.getId(), subject.getName(), subject.getDesc(), subject.getUnit()));
        }
        TreeItem<Subject> root = new RecursiveTreeItem<Subject>(ENROLL_SUBJECT_LIST, RecursiveTreeObject :: getChildren);

        tblYeSubject.setRoot(root);
        tblYeSubject.setShowRoot(false);
    }

    public void removeYeSubject() {
        ENROLL_SUBJECT_LIST.remove(tblYeSubject.getSelectionModel().getSelectedIndex());
        TreeItem<Subject> root = new RecursiveTreeItem<Subject>(ENROLL_SUBJECT_LIST, RecursiveTreeObject :: getChildren);

        tblYeSubject.setRoot(root);
        tblYeSubject.setShowRoot(false);
    }

    public void removeAllYeSubject() {
        while(ENROLL_SUBJECT_LIST.size() > 0)
            ENROLL_SUBJECT_LIST.remove(0);
        TreeItem<Subject> root = new RecursiveTreeItem<Subject>(ENROLL_SUBJECT_LIST, RecursiveTreeObject :: getChildren);

        tblYeSubject.setRoot(root);
        tblYeSubject.setShowRoot(false);
    }

    private void clearAb() {
        tblAbSubject.getColumns().clear();
        tblAbSubject.setRoot(null);
    }

    private void clearYe() {
        tblYeSubject.getColumns().clear();
        tblYeSubject.setRoot(null);
    }

    public void listener(Student student) {
        this.student = student;
        lbStudentNo.setText(student.getStudentNumber() + "");
        lbCourse.setText(new CourseDaoImpl().getCourseById(student.getCourseId()).getDesc());
        lbfullName.setText(String.format("%s, %s %s.", student.getLastName(), student.getFirstName(),
                student.getMiddleName().substring(0, 1)).toUpperCase());
        lbYear.setText(new SectionDaoImpl().getSectionById(student.getSectionId()).getYear() + "");
        lbStatus.setText(student.getStatus());
    }
}
