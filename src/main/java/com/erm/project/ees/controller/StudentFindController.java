package com.erm.project.ees.controller;

import com.erm.project.ees.dao.StudentDao;
import com.erm.project.ees.dao.impl.StudentDaoImpl;
import com.erm.project.ees.model.Student;
import com.erm.project.ees.stage.TeacherStage;
import com.erm.project.ees.util.ResourceHelper;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StudentFindController implements Initializable {

    @FXML
    private ImageView imgvLogo;

    @FXML
    private JFXTextField txStudentNumber;

    private TeacherStage teacherStage;

    @FXML
    protected void onClickEnter(ActionEvent event) {
        try {
            if(isSNvalid()) {
                long sn = Long.parseLong(txStudentNumber.getText());
                final StudentDao studentDao = new StudentDaoImpl();
                Student student = null;
                for(Student stu : studentDao.getStudentList()) {
                    if(stu.getStudentNumber() == sn) {
                        student = stu;
                        break;
                    }
                }

                if(student != null) {
                    teacherStage.callBack(student);
                } else {
                    Platform.runLater(()-> JOptionPane.showMessageDialog(null,"No student found"));
                }
            } else
                Platform.runLater(()->JOptionPane.showMessageDialog(null,"No student found"));
        } catch (Exception e) {
            e.printStackTrace();
        }

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
}
