package io.ermdev.ees.legacy.stage;

import io.ermdev.ees.legacy.controller.EvaluationAddController;
import io.ermdev.ees.legacy.util.ResourceHelper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class EvaluationAddStage extends Stage {

    private EvaluationAddController controller;
    private OnCloseListener listener;

    public EvaluationAddStage() {
        try {
            FXMLLoader loader = new FXMLLoader(ResourceHelper.resourceWithBasePath("fxml/evaluation_add.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 940, 670);
            initModality(Modality.APPLICATION_MODAL);
            setScene(scene);
            setResizable(false);

            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setListener(OnCloseListener listener) {
        this.listener = listener;
    }

    public EvaluationAddController getController() {
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
