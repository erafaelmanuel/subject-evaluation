package com.erm.project.ees.stage;

import com.erm.project.ees.controller.StudentResultController;
import com.erm.project.ees.model.Student;
import com.erm.project.ees.util.ResourceHelper;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class StudentResultStage extends Stage {

    private OnSelectStudentLister onSelectStudentLister;
    private StudentResultController controller;

    public StudentResultStage() {
        try {
            initModality(Modality.APPLICATION_MODAL);
            setResizable(false);
            setTitle("Multiple result");
            FXMLLoader loader = new FXMLLoader(ResourceHelper.resourceWithBasePath("fxml/student_search_list.fxml"));
            Parent root = loader.load();
            setScene(new Scene(root, 440, 220));
            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public StudentResultController getController() {
        return controller;
    }

    public void setOnSelectStudentLister(OnSelectStudentLister onSelectStudentLister) {
        this.onSelectStudentLister = onSelectStudentLister;
    }

    public void callBack(Student student) {
        onSelectStudentLister.onSelect(student);
        Platform.runLater(() -> close());
    }

    @FunctionalInterface
    public interface OnSelectStudentLister {
        void onSelect(Student student);
    }
}
