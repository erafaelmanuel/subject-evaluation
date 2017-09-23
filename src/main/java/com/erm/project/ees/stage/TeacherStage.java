package com.erm.project.ees.stage;

import com.erm.project.ees.model.Student;
import com.erm.project.ees.util.ResourceHelper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TeacherStage extends Stage {

    public TeacherStage() {
        try {
            Parent root = FXMLLoader.load(ResourceHelper.resource("/fxml/teacher.fxml"));
            Scene scene = new Scene(root, 0, 0);
            this.setScene(scene);
            this.setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
