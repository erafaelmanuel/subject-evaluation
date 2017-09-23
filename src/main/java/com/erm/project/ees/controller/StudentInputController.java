package com.erm.project.ees.controller;

import com.erm.project.ees.dao.StudentDao;
import com.erm.project.ees.dao.impl.StudentDaoImpl;
import com.erm.project.ees.model.Section;
import com.erm.project.ees.model.Student;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class StudentInputController implements Initializable {

    @FXML
    private JFXTextField txFirstName;

    @FXML
    private JFXTextField txLastName;

    @FXML
    private JFXTextField txMiddleName;

    @FXML
    private JFXTextField txAge;

    @FXML
    private JFXComboBox<String> cbGender;

    @FXML
    private JFXTextField txContact;

    @FXML
    private JFXComboBox<String> cbSection;

    @FXML
    private Label lbfn;

    @FXML
    private Label lbln;

    @FXML
    private Label lbmn;

    @FXML
    private Label lbage;

    @FXML
    private Label lbcontact;

    private static final ObservableList<String> OBSERVABLE_LIST_GENDER = FXCollections.observableArrayList();
    private static final ObservableList<String> OBSERVABLE_LIST_SECTION = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        cbGender.setItems(OBSERVABLE_LIST_GENDER);
        cbGender.getItems().add("Female");
        cbGender.getItems().add("Male");

        cbSection.setItems(OBSERVABLE_LIST_SECTION);
    }

    @FXML
    protected void onClickCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onClickSave() {
        if(isInputValid()) {
            StudentDao studentDao = new StudentDaoImpl();
            Student student = new Student();
            student.setFirstName(txFirstName.getText());
            student.setLastName(txLastName.getText());
            student.setMiddleName(txMiddleName.getText());
            student.setAge(Integer.parseInt(txAge.getText()));
            student.setGender(cbGender.getSelectionModel().getSelectedItem());
            student.setContactNumber(Integer.parseInt(txContact.getText()));
            studentDao.addStudent(student);
        }
    }

    private boolean isInputValid() {
        boolean isValid = true;
        if(txFirstName.getText().trim().isEmpty()) {
            lbfn.setVisible(true);
            isValid = false;
        } else
            lbfn.setVisible(false);
        if(txLastName.getText().trim().isEmpty()) {
            lbln.setVisible(true);
            isValid = false;
        } else
            lbln.setVisible(false);
        if(txMiddleName.getText().trim().isEmpty()) {
            lbmn.setVisible(true);
            isValid = false;
        } else
            lbmn.setVisible(false);
        if(txAge.getText().trim().isEmpty()) {
            lbage.setVisible(true);
            isValid = false;
        } else
            lbage.setVisible(false);
        if(txContact.getText().trim().isEmpty()) {
            lbcontact.setVisible(true);
            isValid = false;
        } else
            lbcontact.setVisible(false);
        return isValid;
    }
}
