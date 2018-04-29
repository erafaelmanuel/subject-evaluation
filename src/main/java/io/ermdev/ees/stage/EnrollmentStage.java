package io.ermdev.ees.stage;

import io.ermdev.ees.controller.EnrollmentController;
import io.ermdev.ees.util.ResourceHelper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

@Deprecated
public class EnrollmentStage extends Stage {

    private EnrollmentController controller;
    private OnCloseListener listener;

    public EnrollmentStage() {
        try {
            FXMLLoader loader = new FXMLLoader(ResourceHelper.resourceWithBasePath("fxml/enrollment.fxml"));
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

    public EnrollmentController getController() {
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
