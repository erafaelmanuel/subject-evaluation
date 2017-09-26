package com.erm.project.ees.controller;

import com.erm.project.ees.model.Course;
import com.erm.project.ees.stage.CourseStage;
import com.erm.project.ees.stage.CurriculumStage;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import java.net.URL;
import java.util.ResourceBundle;

public class CourseController implements Initializable {

    @FXML
    private JFXTextField txName;

    @FXML
    private JFXTextField txDesc;

    @FXML
    private JFXComboBox<String> cbYear;

    @FXML
    private JFXComboBox<String> cbSemester;

    final CurriculumStage curriculumStage = new CurriculumStage();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbYear.getItems().add("1 YEARS");
        cbYear.getItems().add("2 YEARS");
        cbYear.getItems().add("3 YEARS");
        cbYear.getItems().add("4 YEARS");
        cbYear.getItems().add("5 YEARS");
        cbYear.getSelectionModel().select(0);

        cbSemester.getItems().add("2 SEMESTER");
        cbSemester.getItems().add("3 SEMESTER");
        cbSemester.getSelectionModel().select(0);
    }

    @FXML
    protected void onClickCancel(ActionEvent event) {
        CourseStage stage = (CourseStage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onClickNext(ActionEvent event) {
        Platform.runLater(()->{
            CourseStage stage = (CourseStage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        });

        new Thread(()->{
            Platform.runLater(()->curriculumStage.showAndWait());

            Course course = new Course();
            course.setName(txName.getText());
            course.setDesc(txDesc.getText());
            course.setTotalYear(cbYear.getSelectionModel().getSelectedIndex() + 1);
            course.setTotalSemester(cbSemester.getSelectionModel().getSelectedIndex() + 2);
            curriculumStage.getController().listener(course);
        }).start();

    }

    public void dispose() {
        txName.setText("");
        txDesc.setText("");
        cbYear.getSelectionModel().select(0);
        cbSemester.getSelectionModel().select(0);
    }
}
