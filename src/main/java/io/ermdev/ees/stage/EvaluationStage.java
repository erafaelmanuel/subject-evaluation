package io.ermdev.ees.stage;

import io.ermdev.ees.controller.EvaluationController;
import io.ermdev.ees.util.ResourceHelper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class EvaluationStage extends Stage {

    private EvaluationController controller;
    private OnCloseListener listener;

    public EvaluationStage() {
        try {
            FXMLLoader loader = new FXMLLoader(ResourceHelper.resourceWithBasePath("fxml/evaluation.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1100, 600);
            this.initModality(Modality.APPLICATION_MODAL);
            this.setScene(scene);

            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setListener(OnCloseListener listener) {
        this.listener = listener;
    }

    public EvaluationController getController() {
        return controller;
    }

    public void setClose() {
        if(listener != null)
            listener.onClose();
        close();
    }

    @FunctionalInterface
    public interface OnCloseListener {
        void onClose();
    }
}
