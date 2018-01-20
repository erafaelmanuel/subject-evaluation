package io.ermdev.ees.legacy.stage;

import io.ermdev.ees.legacy.controller.AdvisingFormController;
import io.ermdev.ees.legacy.util.ResourceHelper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AdvisingFormStage extends Stage {

    private AdvisingFormController controller;

    public AdvisingFormStage() {
        try {
            FXMLLoader loader = new FXMLLoader(ResourceHelper.resourceWithBasePath("fxml/advising_form.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 600, 650);

            initModality(Modality.APPLICATION_MODAL);
            setResizable(false);
            setScene(scene);

            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AdvisingFormController getController() {
        return controller;
    }

}
