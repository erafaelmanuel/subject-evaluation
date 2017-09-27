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

    private CurriculumStage curriculumStage;
    private CourseController controller;

    public CourseStage() {
        try {
            FXMLLoader loader = new FXMLLoader(ResourceHelper.resourceWithBasePath("fxml/course_input.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 421, 349);

            initModality(Modality.APPLICATION_MODAL);
            setResizable(false);
            setScene(scene);
            setOnCloseRequest((e)->controller.dispose());

            controller = loader.getController();
            curriculumStage = new CurriculumStage();
            controller.listen(curriculumStage);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CourseController getController() {
        return controller;
    }

    public CurriculumStage getCurriculumStage() {
        return curriculumStage;
    }
}
