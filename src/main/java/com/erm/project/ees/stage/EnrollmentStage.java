package com.erm.project.ees.stage;

import com.erm.project.ees.controller.EnrollmentController;
import com.erm.project.ees.util.ResourceHelper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EnrollmentStage extends Stage {

    private static EnrollmentController controller;

    public EnrollmentStage(String path) {
        //super();
        display(path);
    }

    private void display(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(ResourceHelper.resource(path));
            Parent root = loader.load();
            Scene scene = new Scene(root, 900, 600);
            this.setScene(scene);

            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public EnrollmentController getController() {
        return controller;
    }
}
