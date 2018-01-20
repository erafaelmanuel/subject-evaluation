package io.ermdev.ees.legacy.stage.window;

import io.ermdev.ees.legacy.util.ResourceHelper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AboutWindow {

    public static void display() {
        try {
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("About");

            Parent root = FXMLLoader.load(ResourceHelper.resourceWithBasePath("fxml/about.fxml"));
            stage.setScene(new Scene(root, 576, 385));

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
