package io.ermdev.ees.legacy.stage;

import io.ermdev.ees.legacy.controller.StudentGradeInputController;
import io.ermdev.ees.legacy.util.ResourceHelper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class StudentGradeInputStage extends Stage {

    private StudentGradeInputController controller;

    public StudentGradeInputStage() {
        try {
            FXMLLoader loader = new FXMLLoader(ResourceHelper.resourceWithBasePath("fxml/student_grade_input.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 700, 600);

            initModality(Modality.APPLICATION_MODAL);
            setScene(scene);

            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public StudentGradeInputController getController() {
        return controller;
    }
}
