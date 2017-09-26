package com.erm.project.ees.controller;

import com.erm.project.ees.dao.StudentDao;
import com.erm.project.ees.dao.impl.StudentDaoImpl;
import com.erm.project.ees.model.Student;
import com.erm.project.ees.stage.TeacherStage;
import com.erm.project.ees.stage.window.PopOnExitWindow;
import com.erm.project.ees.util.ResourceHelper;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TeacherWindowController implements Initializable {

    @FXML
    private MenuBar menuBar;

    @FXML
    private ImageView imgvLogo;

    @FXML
    private JFXTextField txStudentNumber;

    @FXML
    private BorderPane blSpace;

    @FXML
    protected void onClickExit() {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        if(PopOnExitWindow.display("Are you sure you want to exit?"))
            stage.close();
    }

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
                    TeacherStage teacherStage = (TeacherStage) ((Node) event.getSource()).getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(ResourceHelper.resourceWithBasePath("fxml/student_grade.fxml"));
                    Parent root = loader.load();
                    Platform.runLater(()->blSpace.setCenter(root));

                    StudentGradeController controller = loader.getController();
                    controller.listener(student);
                } else {
                    Platform.runLater(()->JOptionPane.showMessageDialog(null,"No student found"));
                }
            } else
                Platform.runLater(()->JOptionPane.showMessageDialog(null,"No student found"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @FXML
    protected void onClickEvaluate() {
        new Thread(()->{
            Platform.runLater(()->{
                TeacherStage teacherStage = (TeacherStage) menuBar.getScene().getWindow();
                teacherStage.refresh();
            });
        }).start();


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image imgLogo = new Image(ResourceHelper.resourceWithBasePath("image/studentlogo.png").toString());
        imgvLogo.setImage(imgLogo);
    }

    private boolean isSNvalid() {
        try {Long.parseLong(txStudentNumber.getText()); return true;} catch (NumberFormatException e) {return false;}
    }


}
