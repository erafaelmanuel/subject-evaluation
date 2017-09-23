package com.erm.project.ees.controller;

import com.erm.project.ees.dao.impl.DirtyDaoImpl;
import com.erm.project.ees.model.Student;
import com.erm.project.ees.model.StudentSubjectRecord;
import com.erm.project.ees.util.ResourceHelper;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class StudentGradeController implements Initializable {

    @FXML
    private ImageView imgvLogo;

    @FXML
    private JFXComboBox<String> cbYearSem;

    @FXML
    private Label lbCourse;

    @FXML
    private Label lbSN;

    @FXML
    private Label lbStudent;

    @FXML
    private Label lbYS;

    @FXML
    private Label lbStatus;

    @FXML
    private TableView<StudentSubjectRecord> tblRecord;

    private Student student;

    private ObservableList<String> OBSERVABLE_LIST_CURRICULUM = FXCollections.observableArrayList();
    private ObservableList<StudentSubjectRecord> OBSERVABLE_LIST_RECORD = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image image = new Image(ResourceHelper.resource("/image/studentlogo.png").toString());
        imgvLogo.setImage(image);

        cbYearSem.setItems(OBSERVABLE_LIST_CURRICULUM);
        cbYearSem.getItems().add("1st yr / 1st sem");
        cbYearSem.getItems().add("1st yr / 2nd sem");
        cbYearSem.getItems().add("2nd yr / 1st sem");
        cbYearSem.getItems().add("2nd yr / 2nd sem");
        cbYearSem.getItems().add("3rd yr / 1st sem");
        cbYearSem.getItems().add("3rd yr / 2nd sem");
        cbYearSem.getItems().add("4th yr / 1st sem");
        cbYearSem.getItems().add("4th yr / 2nd sem");
        cbYearSem.getSelectionModel().select(0);

        tblRecord.setItems(OBSERVABLE_LIST_RECORD);
    }

    @FXML
    protected void onChoose() {
        switch(cbYearSem.getSelectionModel().getSelectedIndex()) {
            case 0 :
                clear();
                loadStudent(student , 1, 1);
                break;
            case 1 :
                clear();
                loadStudent(student , 1, 2);
                break;
            case 2 :
                clear();
                loadStudent(student , 2, 1);
                break;
            case 3 :
                clear();
                loadStudent(student , 2, 2);
                break;
            case 4 :
                clear();
                loadStudent(student , 3, 1);
                break;
            case 5 :
                clear();
                loadStudent(student , 3, 2);
                break;
            case 6 :
                clear();
                loadStudent(student , 4, 1);
                break;
            case 7 :
                clear();
                loadStudent(student , 4, 2);
                break;
            default: break;
        }
    }

    private void loadStudent(Student student, int year, int semester) {
        TableColumn<StudentSubjectRecord, String> sName = new TableColumn<>("Subject");
        sName.setCellValueFactory(new PropertyValueFactory<>("subjectName"));

        TableColumn<StudentSubjectRecord, String> sDesc = new TableColumn<>("Description");
        sDesc.setCellValueFactory(new PropertyValueFactory<>("subjectDesc"));
        sDesc.setResizable(false);
        sDesc.setPrefWidth(200);

        TableColumn<StudentSubjectRecord, String> date = new TableColumn<>("Date");
        date.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<StudentSubjectRecord, String> midterm = new TableColumn<>("Midterm");
        midterm.setCellValueFactory(new PropertyValueFactory<>("midterm"));

        TableColumn<StudentSubjectRecord, String> finalterm = new TableColumn<>("Finalterm");
        finalterm.setCellValueFactory(new PropertyValueFactory<>("finalterm"));

        TableColumn<StudentSubjectRecord, String> mark = new TableColumn<>("Mark");
        mark.setCellValueFactory(new PropertyValueFactory<>("mark"));

        tblRecord.getColumns().add(sName);
        tblRecord.getColumns().add(sDesc);
        tblRecord.getColumns().add(date);
        tblRecord.getColumns().add(midterm);
        tblRecord.getColumns().add(finalterm);
        tblRecord.getColumns().add(mark);

        for(StudentSubjectRecord record : new DirtyDaoImpl().getStudentSubjectRecords(student.getCourseId(),
                student.getId(), year, semester)) {
            tblRecord.getItems().add(record);
        }
    }

    private void clear() {
        tblRecord.getItems().clear();
        tblRecord.getColumns().clear();
    }

    public void listener(Student student) {
        this.student = student;
        lbCourse.setText(student.getCourseId() + "");
        lbSN.setText(student.getStudentNumber() + "");
        lbStudent.setText(student.getFirstName() + " " + student.getLastName());
        lbYS.setText(student.getSectionId() + "");

        clear();
        loadStudent(student ,1, 1);
    }
}
