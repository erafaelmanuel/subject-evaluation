package com.erm.project.ees.stage.window;

import com.erm.project.ees.util.ResourceHelper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class StudentInputWindow {

    public static boolean display() {
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            Parent root = FXMLLoader.load(ResourceHelper.resourceWithBasePath("fxml/student_input.fxml"));
            Scene scene = new Scene(root, 324, 380);
            stage.setScene(scene);
            stage.showAndWait();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
