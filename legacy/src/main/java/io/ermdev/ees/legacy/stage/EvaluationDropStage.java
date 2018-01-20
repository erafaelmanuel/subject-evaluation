package io.ermdev.ees.legacy.stage;

import io.ermdev.ees.legacy.controller.EvaluationDropController;
import io.ermdev.ees.legacy.util.ResourceHelper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class EvaluationDropStage extends Stage {

    private EvaluationDropController controller;
    private OnCloseListener listener;

    public EvaluationDropStage() {
        try {
            FXMLLoader loader = new FXMLLoader(ResourceHelper.resourceWithBasePath("fxml/evaluation_drop.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 940, 640);
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

    public EvaluationDropController getController() {
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