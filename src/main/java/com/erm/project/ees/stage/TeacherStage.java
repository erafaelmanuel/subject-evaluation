package com.erm.project.ees.stage;

import com.erm.project.ees.controller.TeacherWindowController;
import com.erm.project.ees.model.Student;
import com.erm.project.ees.util.ResourceHelper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TeacherStage extends Stage {

    TeacherWindowController controller;
    OnSignOutListener listener;

    public TeacherStage() {
        try {
            FXMLLoader loader = new FXMLLoader(ResourceHelper.resourceWithBasePath("fxml/teacher.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 0, 0);
            setScene(scene);
            setMaximized(true);
            setMinWidth(1160);
            setMinHeight(800);
            controller = loader.getController();
            controller.listening(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void callBack(Student student) {
        controller.listening(student);
    }

    public void setListener(OnSignOutListener listener) {
        this.listener = listener;
    }

    public TeacherWindowController getController() {
        return controller;
    }

    public void callBack() {
        listener.onSignout();
    }

    public interface OnSignOutListener {
        void onSignout();
    }
}
