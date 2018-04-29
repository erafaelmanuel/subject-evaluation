package io.ermdev.ees.stage;

import io.ermdev.ees.controller.SubjectInputController;
import io.ermdev.ees.util.ResourceHelper;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class SubjectInputStage extends Stage {

    private OnItemAddLister onItemAddLister;
    private SubjectInputController controller;

    public SubjectInputStage() {
        try {
            initModality(Modality.APPLICATION_MODAL);
            setResizable(false);
            FXMLLoader loader = new FXMLLoader(ResourceHelper.resourceWithBasePath("fxml/subject_input.fxml"));
            Parent root = loader.load();
            setScene(new Scene(root, 700, 600));
            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SubjectInputController getController() {
        return controller;
    }

    public void setOnItemAddLister(OnItemAddLister onItemAddLister) {
        this.onItemAddLister = onItemAddLister;
    }

    public void callBack() {
        onItemAddLister.onAddSubject();
        Platform.runLater(() -> close());
    }

    @FunctionalInterface
    public interface OnItemAddLister {
        void onAddSubject();
    }
}
