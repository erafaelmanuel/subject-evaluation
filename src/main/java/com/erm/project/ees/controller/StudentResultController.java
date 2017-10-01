package com.erm.project.ees.controller;

import com.erm.project.ees.dao.CourseDao;
import com.erm.project.ees.dao.SectionDao;
import com.erm.project.ees.dao.StudentDao;
import com.erm.project.ees.dao.impl.CourseDaoImpl;
import com.erm.project.ees.dao.impl.SectionDaoImpl;
import com.erm.project.ees.dao.impl.StudentDaoImpl;
import com.erm.project.ees.model.Section;
import com.erm.project.ees.model.recursive.Student;
import com.erm.project.ees.model.recursive.Subject;
import com.erm.project.ees.stage.StudentResultStage;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StudentResultController implements Initializable {


    @FXML
    private JFXTreeTableView<Student> tblResult;

    @FXML
    private JFXButton btnConfirm;

    @FXML
    private Label lbNResult;

    private final ObservableList<Student> OBV_STUDENT_LIST = FXCollections.observableArrayList();

    private final CourseDao courseDao = new CourseDaoImpl();
    private final SectionDao sectionDao = new SectionDaoImpl();
    private final StudentDao studentDao = new StudentDaoImpl();

    @FXML
    protected void onClickItem() {
        if(tblResult.getSelectionModel().getSelectedIndex() > -1)
            btnConfirm.setDisable(false);
        else
            btnConfirm.setDisable(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnConfirm.setDisable(true);
        init();
    }

    public void listener(List<com.erm.project.ees.model.Student> studentList) {
        OBV_STUDENT_LIST.clear();
        loadResult(studentList);
    }

    private void loadResult(List<com.erm.project.ees.model.Student> studentList) {
        for(com.erm.project.ees.model.Student s : studentList) {
            final Section _section = sectionDao.getSectionById(s.getSectionId());
            String fname = String.format("%s, %s %s.",
                    s.getLastName(), s.getFirstName(), s.getMiddleName().substring(0, 1).toUpperCase());
            String course = courseDao.getCourseById(s.getCourseId()).getName();
            String section = _section.getYear() + "-" + _section.getName();
            OBV_STUDENT_LIST.add(new Student(s.getStudentNumber(), fname, course, section));
        }

        lbNResult.setText(OBV_STUDENT_LIST.size() + "");

        TreeItem<Student> root = new RecursiveTreeItem<>(OBV_STUDENT_LIST, RecursiveTreeObject::getChildren);
        tblResult.setRoot(root);
        tblResult.setShowRoot(false);
    }

    public void init() {
        JFXTreeTableColumn<Student, Long> numberCol = new JFXTreeTableColumn<>("#");
        numberCol.setResizable(false);
        numberCol.setPrefWidth(120);
        numberCol.setCellValueFactory(param -> param.getValue().getValue().numberProperty().asObject());

        JFXTreeTableColumn<Student, String> nameCol = new JFXTreeTableColumn<>("Name");
        nameCol.setResizable(false);
        nameCol.setPrefWidth(180);
        nameCol.setCellValueFactory(param -> param.getValue().getValue().fullNameProperty());

        JFXTreeTableColumn<Student, String> courseCol = new JFXTreeTableColumn<>("Course");
        courseCol.setResizable(false);
        courseCol.setPrefWidth(80);
        courseCol.setCellValueFactory(param -> param.getValue().getValue().courseProperty());

        JFXTreeTableColumn<Student, String> sectionCol = new JFXTreeTableColumn<>("Section");
        sectionCol.setResizable(false);
        sectionCol.setPrefWidth(70);
        sectionCol.setCellValueFactory(param -> param.getValue().getValue().sectionProperty());

        tblResult.getColumns().add(numberCol);
        tblResult.getColumns().add(nameCol);
        tblResult.getColumns().add(courseCol);
        tblResult.getColumns().add(sectionCol);
    }

    @FXML
    protected void onClickConfirm(ActionEvent event) {
        StudentResultStage studentResultStage = (StudentResultStage) ((Node) event.getSource()).getScene().getWindow();
        final long id = OBV_STUDENT_LIST.get(tblResult.getSelectionModel().getFocusedIndex()).getNumber();
        com.erm.project.ees.model.Student student = studentDao.getStudentById(id);
        studentResultStage.callBack(student);
    }
}
