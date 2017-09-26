package com.erm.project.ees.stage;

import com.erm.project.ees.controller.CourseController;
import com.erm.project.ees.util.ResourceHelper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CourseStage extends Stage {

    public CourseStage() {
        try {
            FXMLLoader loader = new FXMLLoader(ResourceHelper.resourceWithBasePath("fxml/course_input.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 421, 349);

            initModality(Modality.APPLICATION_MODAL);
            setResizable(false);
            setOnCloseRequest((e)->((CourseController) loader.getController()).dispose());
            setScene(scene);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
