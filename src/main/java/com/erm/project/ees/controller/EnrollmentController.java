package com.erm.project.ees.controller;

import com.erm.project.ees.dao.impl.*;
import com.erm.project.ees.model.Student;
import com.erm.project.ees.model.StudentSubjectRecord;
import com.erm.project.ees.model.recursive.Subject;
import com.erm.project.ees.util.AssessmentHelper;
import com.erm.project.ees.util.ResourceHelper;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
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
    private JFXComboBox<String> cbClass;

    @FXML
    private JFXComboBox<String> cbMaxYear;

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
    private JFXComboBox<String> cbCurSemester;

    @FXML
    private Label lbAbUnit;

    @FXML
    private Label lbYeUnit;

    @FXML
    private JFXButton btnSave;

    @FXML
    private Label lbexceed;

    private Student student;

    private final ObservableList<Subject> ENROLL_SUBJECT_LIST = FXCollections.observableArrayList();
    private final ObservableList<Subject> AVAILABLE_SUBJECT_LIST = FXCollections.observableArrayList();
    private int totalAbUnit;
    private int totalYeUnit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //student = new StudentDaoImpl().getStudentById(1);

        lbexceed.setVisible(false);

        cbAbSubject.getItems().add("ALL");
        cbAbSubject.getItems().add("1ST YR");
        cbAbSubject.getItems().add("2ND YR");
        cbAbSubject.getItems().add("3RD YR");
        cbAbSubject.getItems().add("4TH YR");
        cbAbSubject.getSelectionModel().select(0);

        cbClass.getItems().add("REGULAR/IRREG CLASSES");
        cbClass.getItems().add("SUMMER CLASSES");
        cbClass.getSelectionModel().select(0);

        cbCurSemester.getItems().add("1ST SEMESTER");
        cbCurSemester.getItems().add("2ND SEMESTER");
        cbCurSemester.getSelectionModel().select(0);

        Image image = new Image(ResourceHelper.resource("/image/studentlogo.png").toString());
        imgLogo.setImage(image);

//        int totalYear = new CourseDaoImpl().getCourseById(student.getCourseId()).getTotalYear();
//        if(totalYear <= 4) {
//            cbMaxYear.getItems().add("1ST YR");
//            cbMaxYear.getItems().add("2ND YR");
//            cbMaxYear.getItems().add("3RD YR");
//            cbMaxYear.getItems().add("4TH YR");
//        }else if(totalYear==5){
//            cbMaxYear.getItems().add("1ST YR");
//            cbMaxYear.getItems().add("2ND YR");
//            cbMaxYear.getItems().add("3RD YR");
//            cbMaxYear.getItems().add("4TH YR");
//            cbMaxYear.getItems().add("5TH YR");
//        }else {
//            cbMaxYear.getItems().add("1ST YR");
//            cbMaxYear.getItems().add("2ND YR");
//        }
//        int year = new SectionDaoImpl().getSectionById(student.getSectionId()).getYear();
//        switch (year) {
//            case 5: cbMaxYear.getSelectionModel().select(4); break;
//            case 4: cbMaxYear.getSelectionModel().select(3); break;
//            case 3: cbMaxYear.getSelectionModel().select(2); break;
//            case 2: cbMaxYear.getSelectionModel().select(1); break;
//            default: cbMaxYear.getSelectionModel().select(0); break;
//        }
//
//        List<com.erm.project.ees.model.Subject> list = AssessmentHelper.getSubjectListWithFilter(student,
//                cbCurSemester.getSelectionModel().getSelectedIndex() + 1, cbMaxYear.getSelectionModel().getSelectedIndex() + 1);
//        if(cbCurSemester.getSelectionModel().getSelectedIndex() == 1)
//            list.addAll(AssessmentHelper.getSpecialSubjectListWithFilter(student, "SPECIAL_CLASSES", cbMaxYear.getSelectionModel().getSelectedIndex() + 1));
//
//        clearAb();
//        loadAbSubject(list);
//
//        clearYe();
//        loadYeSubject();
    }

    @FXML
    protected void onClickSave() {
        if(totalYeUnit >= 1 && totalYeUnit <= 30) {
            for (Subject s : ENROLL_SUBJECT_LIST) {
                StudentSubjectRecord record = new StudentSubjectRecord();
                record.setDate(new Date().toString());
                record.setMark("ONGOING");
                new DirtyDaoImpl().addStudentRecord(record, s.getId(), student.getId());
            }
        }else if(totalYeUnit < 1)
            Platform.runLater(()->JOptionPane.showMessageDialog(null, "Please add a subject to enroll."));
        else
            Platform.runLater(()->JOptionPane.showMessageDialog(null, "The limit of unit is exceeded."));
    }

    @FXML
    protected void onChooseFilter() {
        if(cbAbSubject.getSelectionModel().getSelectedIndex() == 0) {
            List<com.erm.project.ees.model.Subject> list = AssessmentHelper.getSubjectListWithFilter(student,
                    cbCurSemester.getSelectionModel().getSelectedIndex() + 1, cbMaxYear.getSelectionModel().getSelectedIndex() + 1);
            if(cbCurSemester.getSelectionModel().getSelectedIndex() == 1)
                list.addAll(AssessmentHelper.getSpecialSubjectListWithFilter(student, "SPECIAL_CLASSES", cbMaxYear.getSelectionModel().getSelectedIndex() + 1));

            clearAb();
            loadAbSubject(list);
        }else {
            List<com.erm.project.ees.model.Subject> list = AssessmentHelper.getSubjectListWithFilter(student,
                    cbAbSubject.getSelectionModel().getSelectedIndex(), cbCurSemester.getSelectionModel().getSelectedIndex() + 1,
                    cbMaxYear.getSelectionModel().getSelectedIndex() + 1);
            if(cbCurSemester.getSelectionModel().getSelectedIndex() == 1)
                list.addAll(AssessmentHelper.getSpecialSubjectListWithFilter(student, cbAbSubject.getSelectionModel().getSelectedIndex(),"SPECIAL_CLASSES",
                        cbMaxYear.getSelectionModel().getSelectedIndex() + 1));
            clearAb();
            loadAbSubject(list);
        }
    }

    @FXML
    protected void onChooseSemester() {
        if(cbAbSubject.getSelectionModel().getSelectedIndex() == 0) {
            List<com.erm.project.ees.model.Subject> list = AssessmentHelper.getSubjectListWithFilter(student,
                    cbCurSemester.getSelectionModel().getSelectedIndex() + 1, cbMaxYear.getSelectionModel().getSelectedIndex() + 1);
            if(cbCurSemester.getSelectionModel().getSelectedIndex() == 1)
                list.addAll(AssessmentHelper.getSpecialSubjectListWithFilter(student, "SPECIAL_CLASSES", cbMaxYear.getSelectionModel().getSelectedIndex() + 1));

            clearAb();
            loadAbSubject(list);
        }else {
            List<com.erm.project.ees.model.Subject> list = AssessmentHelper.getSubjectListWithFilter(student,
                    cbAbSubject.getSelectionModel().getSelectedIndex(), cbCurSemester.getSelectionModel().getSelectedIndex() + 1,
                    cbMaxYear.getSelectionModel().getSelectedIndex() + 1);
            if(cbCurSemester.getSelectionModel().getSelectedIndex() == 1)
                list.addAll(AssessmentHelper.getSpecialSubjectListWithFilter(student, cbAbSubject.getSelectionModel().getSelectedIndex(),"SPECIAL_CLASSES",
                        cbMaxYear.getSelectionModel().getSelectedIndex() + 1));
            clearAb();
            loadAbSubject(list);
        }
        removeAllYeSubject();
    }

    @FXML
    private void onChooseMaxYear() {
        if(cbAbSubject.getSelectionModel().getSelectedIndex() == 0) {
            List<com.erm.project.ees.model.Subject> list = AssessmentHelper.getSubjectListWithFilter(student,
                    cbCurSemester.getSelectionModel().getSelectedIndex() + 1, cbMaxYear.getSelectionModel().getSelectedIndex() + 1);
            if(cbCurSemester.getSelectionModel().getSelectedIndex() == 1)
                list.addAll(AssessmentHelper.getSpecialSubjectListWithFilter(student, "SPECIAL_CLASSES", cbMaxYear.getSelectionModel().getSelectedIndex() + 1));

            clearAb();
            loadAbSubject(list);
        }else {
            List<com.erm.project.ees.model.Subject> list = AssessmentHelper.getSubjectListWithFilter(student,
                    cbAbSubject.getSelectionModel().getSelectedIndex(), cbCurSemester.getSelectionModel().getSelectedIndex() + 1,
                    cbMaxYear.getSelectionModel().getSelectedIndex() + 1);
            if(cbCurSemester.getSelectionModel().getSelectedIndex() == 1)
                list.addAll(AssessmentHelper.getSpecialSubjectListWithFilter(student, cbAbSubject.getSelectionModel().getSelectedIndex(),"SPECIAL_CLASSES",
                        cbMaxYear.getSelectionModel().getSelectedIndex() + 1));
            clearAb();
            loadAbSubject(list);
        }
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
        //reset the total unit
        totalAbUnit = 0;
        //reset the list
        while(AVAILABLE_SUBJECT_LIST.size() > 0)
            AVAILABLE_SUBJECT_LIST.remove(0);

        for(com.erm.project.ees.model.Subject subject : subjectList) {
            AVAILABLE_SUBJECT_LIST.add(new Subject(subject.getId(), subject.getName(), subject.getDesc(), subject.getUnit()));
            totalAbUnit += subject.getUnit();
        }
        lbAbUnit.setText(totalAbUnit + "");
        TreeItem<Subject> root = new RecursiveTreeItem<>(AVAILABLE_SUBJECT_LIST, RecursiveTreeObject :: getChildren);

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

    private void loadYeSubject(List<com.erm.project.ees.model.Subject> subjectList) {
        for(com.erm.project.ees.model.Subject subject : subjectList) {
            ENROLL_SUBJECT_LIST.add(new Subject(subject.getId(), subject.getName(), subject.getDesc(), subject.getUnit()));
            totalYeUnit += subject.getUnit();
            lbYeUnit.setText(totalYeUnit + "");
            if(totalYeUnit < 1 || totalYeUnit > 30) {
                lbYeUnit.setStyle("-fx-text-fill:#c0392b;");
                if(totalYeUnit > 30)
                    lbexceed.setVisible(true);
            }else {
                lbYeUnit.setStyle("-fx-text-fill:#27ae60;");
                lbexceed.setVisible(false);
            }
        }

        TreeItem<Subject> root = new RecursiveTreeItem<>(ENROLL_SUBJECT_LIST, RecursiveTreeObject :: getChildren);

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

        tblYeSubject.setRoot(root);
        tblYeSubject.setShowRoot(false);
    }

    public void addYeSubject(Subject subject) {
        for(Subject s : ENROLL_SUBJECT_LIST) {
            if(s.getId() == subject.getId())
                return;
        }
        ENROLL_SUBJECT_LIST.add(new Subject(subject.getId(), subject.getName(), subject.getDesc(), subject.getUnit()));
        TreeItem<Subject> root = new RecursiveTreeItem<Subject>(ENROLL_SUBJECT_LIST, RecursiveTreeObject :: getChildren);

        totalYeUnit += subject.getUnit();
        lbYeUnit.setText(totalYeUnit + "");
        if(totalYeUnit < 1 || totalYeUnit > 30) {
            lbYeUnit.setStyle("-fx-text-fill:#c0392b;");
            if(totalYeUnit > 30)
                lbexceed.setVisible(true);

        }else {
            lbYeUnit.setStyle("-fx-text-fill:#27ae60;");
            lbexceed.setVisible(false);
        }

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

            totalYeUnit += subject.getUnit();
            lbYeUnit.setText(totalYeUnit + "");
            if(totalYeUnit < 1 || totalYeUnit > 30) {
                lbYeUnit.setStyle("-fx-text-fill:#c0392b;");
                if(totalYeUnit > 30)
                    lbexceed.setVisible(true);
            }else {
                lbYeUnit.setStyle("-fx-text-fill:#27ae60;");
                lbexceed.setVisible(false);
            }
        }
        TreeItem<Subject> root = new RecursiveTreeItem<Subject>(ENROLL_SUBJECT_LIST, RecursiveTreeObject :: getChildren);

        tblYeSubject.setRoot(root);
        tblYeSubject.setShowRoot(false);
    }

    public void removeYeSubject() {
        Subject subject = ENROLL_SUBJECT_LIST.get(tblYeSubject.getSelectionModel().getSelectedIndex());

        totalYeUnit -= subject.getUnit();
        lbYeUnit.setText(totalYeUnit + "");
        if(totalYeUnit < 1 || totalYeUnit > 30) {
            lbYeUnit.setStyle("-fx-text-fill:#c0392b;");
            if(totalYeUnit > 30)
                lbexceed.setVisible(true);
        }else {
            lbYeUnit.setStyle("-fx-text-fill:#27ae60;");
            lbexceed.setVisible(false);
        }

        ENROLL_SUBJECT_LIST.remove(subject);
        TreeItem<Subject> root = new RecursiveTreeItem<Subject>(ENROLL_SUBJECT_LIST, RecursiveTreeObject :: getChildren);

        tblYeSubject.setRoot(root);
        tblYeSubject.setShowRoot(false);
    }

    public void removeAllYeSubject() {
        //reset
        totalYeUnit = 0;
        lbYeUnit.setText(totalYeUnit + "");
        lbYeUnit.setStyle("-fx-text-fill:#c0392b;");
        lbexceed.setVisible(false);

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

        int totalYear = new CourseDaoImpl().getCourseById(student.getCourseId()).getTotalYear();
        if(totalYear <= 4) {
            cbMaxYear.getItems().add("1ST YR");
            cbMaxYear.getItems().add("2ND YR");
            cbMaxYear.getItems().add("3RD YR");
            cbMaxYear.getItems().add("4TH YR");
        }else if(totalYear==5){
            cbMaxYear.getItems().add("1ST YR");
            cbMaxYear.getItems().add("2ND YR");
            cbMaxYear.getItems().add("3RD YR");
            cbMaxYear.getItems().add("4TH YR");
            cbMaxYear.getItems().add("5TH YR");
        }else {
            cbMaxYear.getItems().add("1ST YR");
            cbMaxYear.getItems().add("2ND YR");
        }
        int year = new SectionDaoImpl().getSectionById(student.getSectionId()).getYear();
        switch (year) {
            case 5: cbMaxYear.getSelectionModel().select(4); break;
            case 4: cbMaxYear.getSelectionModel().select(3); break;
            case 3: cbMaxYear.getSelectionModel().select(2); break;
            case 2: cbMaxYear.getSelectionModel().select(1); break;
            default: cbMaxYear.getSelectionModel().select(0); break;
        }

        List<com.erm.project.ees.model.Subject> list = AssessmentHelper.getSubjectListWithFilter(student,
                cbCurSemester.getSelectionModel().getSelectedIndex() + 1, cbMaxYear.getSelectionModel().getSelectedIndex() + 1);
        if(cbCurSemester.getSelectionModel().getSelectedIndex() == 1)
            list.addAll(AssessmentHelper.getSpecialSubjectListWithFilter(student, "SPECIAL_CLASSES", cbMaxYear.getSelectionModel().getSelectedIndex() + 1));

        clearAb();
        loadAbSubject(list);

        List<com.erm.project.ees.model.Subject> subjectList = new ArrayList<>();
        List<StudentSubjectRecord> recordList = new DirtyDaoImpl().getStudentSubjectRecordByMark(student.getId(), "ONGOING");

        for(StudentSubjectRecord record : recordList) {
            com.erm.project.ees.model.Subject subject = new SubjectDaoImpl().getSubjectById(record.getSubjectId());
            subjectList.add(subject);
        }

        clearYe();
        loadYeSubject(subjectList);
    }
}
