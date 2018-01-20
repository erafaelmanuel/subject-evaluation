package io.ermdev.ees.legacy.controller;

import io.ermdev.ees.legacy.model.Student;
import io.ermdev.ees.legacy.stage.TeacherStage;
import io.ermdev.ees.legacy.stage.window.AboutWindow;
import io.ermdev.ees.legacy.stage.window.PopOnExitWindow;
import io.ermdev.ees.legacy.util.ResourceHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TeacherWindowController implements Initializable {

    @FXML
    private MenuBar menuBar;

    @FXML
    private BorderPane blSpace;

    private TeacherStage teacherStage;

    @FXML
    protected void onClickExit() {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        if (PopOnExitWindow.display("Are you sure you want to exit?"))
            stage.close();
    }

    @FXML
    protected void onClickAssess() {
        try {
            FXMLLoader loader = new FXMLLoader(ResourceHelper.resourceWithBasePath("fxml/student_find.fxml"));
            Parent root = loader.load();
            Platform.runLater(() -> blSpace.setCenter(root));
            ((StudentFindController) loader.getController()).listening(teacherStage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void listening(TeacherStage teacherStage) {
        try {
            this.teacherStage = teacherStage;
            FXMLLoader loader = new FXMLLoader(ResourceHelper.resourceWithBasePath("fxml/student_find.fxml"));
            Parent root = loader.load();
            Platform.runLater(() -> blSpace.setCenter(root));
            ((StudentFindController) loader.getController()).listening(teacherStage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listening(Student student) {
        try {
            FXMLLoader loader = new FXMLLoader(ResourceHelper.resourceWithBasePath("fxml/student_grade.fxml"));
            Parent root = loader.load();
            Platform.runLater(() -> blSpace.setCenter(root));

            StudentGradeController controller = loader.getController();
            controller.listener(student);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onClickSignout() {
        TeacherStage stage = (TeacherStage) menuBar.getScene().getWindow();
        stage.close();
        stage.callBack();
    }

    @FXML
    protected void onClickAbout() {
        AboutWindow.display();
    }
}
