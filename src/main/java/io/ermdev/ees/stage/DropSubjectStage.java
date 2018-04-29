package io.ermdev.ees.stage;

import io.ermdev.ees.controller.DropSubjectController;
import io.ermdev.ees.util.ResourceHelper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

@Deprecated
public class DropSubjectStage extends Stage {

    private DropSubjectController controller;
    private OnCloseListener listener;

    public DropSubjectStage() {
        try {
            FXMLLoader loader = new FXMLLoader(ResourceHelper.resourceWithBasePath("fxml/add_drop_subject.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 900, 600);
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

    public DropSubjectController getController() {
        return controller;
    }

    public void setClose() {
        if(listener != null)
            listener.onClose();
    }

    @FunctionalInterface
    public interface OnCloseListener {
        void onClose();
    }
}
