package io.erm.ees.stage;

import io.erm.ees.controller.EvaluationController;
import io.erm.ees.util.ResourceHelper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class EvaluationStage extends Stage {

    private EvaluationController controller;

    public EvaluationStage() {
        try {
            FXMLLoader loader = new FXMLLoader(ResourceHelper.resourceWithBasePath("fxml/evaluation.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 900, 600);
            this.initModality(Modality.APPLICATION_MODAL);
            this.setScene(scene);

            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public EvaluationController getController() {
        return controller;
    }
}
