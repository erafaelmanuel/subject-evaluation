package io.erm.ees.controller;

import io.erm.ees.dao.StudentDao;
import io.erm.ees.dao.impl.StudentDaoImpl;
import io.erm.ees.model.Student;
import io.erm.ees.stage.StudentResultStage;
import io.erm.ees.stage.TeacherStage;
import io.erm.ees.util.ResourceHelper;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StudentFindController implements Initializable, StudentResultStage.OnSelectStudentLister{

    @FXML
    private ImageView imgvLogo;

    @FXML
    private JFXTextField txStudentNumber;

    private TeacherStage teacherStage;

    private final StudentDao studentDao = new StudentDaoImpl();

    @FXML
    protected void onClickEnter() {
        if(!txStudentNumber.getText().trim().isEmpty()) {
            if (txStudentNumber.getText().trim().matches("^[0-9a-zA-Z]+$")) {
                List<Student> studentList = new ArrayList<>();
                if(isNumber(txStudentNumber.getText().trim())) {
                    Student student = studentDao.getStudentById(Long.parseLong(txStudentNumber.getText().trim()));
                    if(student == null) {
                        new Thread(() -> JOptionPane.showMessageDialog(null, "No result found"))
                                .start();
                        return;
                    }
                    studentList.add(student);
                }else {
                    studentList.addAll(studentDao
                            .getStudentList("WHERE firstName='" +txStudentNumber.getText().trim()+ "' " +
                                    "OR lastName='"+txStudentNumber.getText().trim()+"' " +
                                    "OR middleName='"+txStudentNumber.getText().trim()+"'"));
                    if(studentList.size() < 1) {
                        new Thread(() -> JOptionPane.showMessageDialog(null, "No result found"))
                                .start();
                        return;
                    }
                }
                if(studentList.size() > 1) {
                    StudentResultStage studentResultStage = new StudentResultStage();
                    studentResultStage.setOnSelectStudentLister(this);
                    Platform.runLater(()->studentResultStage.showAndWait());
                    studentResultStage.getController().listener(studentList);
                } else {
                    onSelect(studentList.get(0));

                }
                txStudentNumber.setPromptText("Enter a student");
                txStudentNumber.setStyle("-fx-prompt-text-fill:#d35400");
            } else {
                txStudentNumber.setText("");
                txStudentNumber.setPromptText("Invalid input");
                txStudentNumber.setStyle("-fx-prompt-text-fill:#f39c12");
            }
        }
    }


    @FXML
    protected void onActionEnter() {
        onClickEnter();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image imgLogo = new Image(ResourceHelper.resourceWithBasePath("image/studentlogo.png").toString());
        imgvLogo.setImage(imgLogo);
    }

    private boolean isSNvalid() {
        try {Long.parseLong(txStudentNumber.getText()); return true;} catch (NumberFormatException e) {return false;}
    }

    public void listening(TeacherStage teacherStage) {
        this.teacherStage = teacherStage;
    }

    public boolean isNumber(String string) {
        try {
            Long.parseLong(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void onSelect(Student student) {
        teacherStage.getController().listening(student);
    }
}
