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

    private CurriculumStage curriculumStage = new CurriculumStage();

    private final Course COURSE = new Course();

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
        dispose();
        stage.close();
    }

    @FXML
    protected void onClickNext(ActionEvent event) {
        Platform.runLater(() -> {
            CourseStage stage = (CourseStage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        });

        new Thread(() -> {
            Platform.runLater(() -> curriculumStage.showAndWait());

            COURSE.setName(txName.getText());
            COURSE.setDesc(txDesc.getText());
            COURSE.setTotalYear(cbYear.getSelectionModel().getSelectedIndex() + 1);
            COURSE.setTotalSemester(cbSemester.getSelectionModel().getSelectedIndex() + 2);
            curriculumStage.getController().listener(COURSE);
            dispose();
        }).start();

    }

    public void dispose() {
        Platform.runLater(() -> {
            txName.setText("");
            txDesc.setText("");
            cbYear.getSelectionModel().select(0);
            cbSemester.getSelectionModel().select(0);
        });
    }

    public void listen(CurriculumStage curriculumStage) {
        this.curriculumStage = curriculumStage;
    }

    public void listen(Course course) {
        COURSE.setId(course.getId());
        COURSE.setName(course.getName());
        COURSE.setDesc(course.getDesc());
        COURSE.setTotalYear(course.getTotalYear());
        COURSE.setTotalSemester(course.getTotalSemester());

        txName.setText(COURSE.getName());
        txDesc.setText(COURSE.getDesc());

        cbYear.getSelectionModel().select(COURSE.getTotalYear() - 1);
        cbSemester.getSelectionModel().select(COURSE.getTotalSemester() - 2);
    }
}
