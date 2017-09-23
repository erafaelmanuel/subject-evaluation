package com.erm.project.ees.controller;

import com.erm.project.ees.dao.impl.StudentDaoImpl;
import com.erm.project.ees.dao.impl.SubjectDaoImpl;
import com.erm.project.ees.model.Student;
import com.erm.project.ees.model.Subject;
import com.erm.project.ees.model.UserType;
import com.erm.project.ees.stage.window.PopOnExitWindow;
import com.erm.project.ees.stage.window.StudentInputWindow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminWindowController implements Initializable {

    @FXML
    private Button bnAdd;

    @FXML
    private Button bnRefresh;

    @FXML
    private MenuBar menuBar;

    @FXML
    private TableView<Object> tblData;

    private static final ObservableList<Object> OBSERVABLE_LIST = FXCollections.observableArrayList();

    private static final int NO_TABLE = 0;
    private static final int TABLE_STUDENT = 1;
    private static final int TABLE_SUBJECT = 2;

    private int cTable = TABLE_STUDENT;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tblData.setItems(OBSERVABLE_LIST);
    }

    @FXML
    protected void onClickExit() {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        if(PopOnExitWindow.display("Are you sure you want to exit?"))
            stage.close();
    }

    @FXML
    protected void onClickAdd() {
        if(cTable == TABLE_STUDENT)
            StudentInputWindow.display();
    }

    @FXML
    protected void onClickSubject() {
        Stage stage = (Stage) menuBar.getScene().getWindow();

        clear();

        loadSubject();
    }

    @FXML
    protected void onClickStudent() {

        //Get the stage of the node
        Stage stage = (Stage) menuBar.getScene().getWindow();

        //Clear the table column and items
        clear();

        //Load the student list
        loadStudent();
    }

    private void loadStudent() {
        TableColumn<Object, String> stStudentNumber = new TableColumn<>("Student Number");
        stStudentNumber.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
        stStudentNumber.setPrefWidth(200);

        TableColumn<Object, String> stFirstName = new TableColumn<>("FirstName");
        stFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        stFirstName.setPrefWidth(200);

        TableColumn<Object, String> stLastName = new TableColumn<>("LastName");
        stLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        stLastName.setPrefWidth(200);

        TableColumn<Object, String> stMiddleName = new TableColumn<>("MiddleName");
        stMiddleName.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        stMiddleName.setPrefWidth(200);

        TableColumn<Object, String> stAge = new TableColumn<>("Age");
        stAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        stAge.setPrefWidth(200);

        TableColumn<Object, String> stGender = new TableColumn<>("Gender");
        stGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        stGender.setPrefWidth(200);

        TableColumn<Object, String> stContact = new TableColumn<>("Contact");
        stContact.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        stContact.setPrefWidth(200);

        tblData.getColumns().add(stStudentNumber);
        tblData.getColumns().add(stFirstName);
        tblData.getColumns().add(stLastName);
        tblData.getColumns().add(stMiddleName);
        tblData.getColumns().add(stAge);
        tblData.getColumns().add(stGender);
        tblData.getColumns().add(stContact);

        for(Student student : new StudentDaoImpl().getStudentList()) {
            tblData.getItems().add(student);
        }
    }

    private void loadSubject() {
        TableColumn<Object, String> suId = new TableColumn<>("Id");
        suId.setCellValueFactory(new PropertyValueFactory<>("id"));
        suId.setPrefWidth(200);

        TableColumn<Object, String> suName = new TableColumn<>("Name");
        suName.setCellValueFactory(new PropertyValueFactory<>("name"));
        suName.setPrefWidth(200);

        TableColumn<Object, String> suDesc = new TableColumn<>("Description");
        suDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        suDesc.setPrefWidth(200);

        TableColumn<Object, String> suUnit = new TableColumn<>("Unit");
        suUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        suUnit.setPrefWidth(200);

        tblData.getColumns().add(suId);
        tblData.getColumns().add(suName);
        tblData.getColumns().add(suDesc);
        tblData.getColumns().add(suUnit);

        for(Subject subject : new SubjectDaoImpl().getSubjectList()) {
            tblData.getItems().add(subject);
        }
    }

    private void clear() {
        tblData.getColumns().clear();
        tblData.getItems().clear();
    }
}
