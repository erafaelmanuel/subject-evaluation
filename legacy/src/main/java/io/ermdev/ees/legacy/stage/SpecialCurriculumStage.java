package io.ermdev.ees.legacy.stage;

import io.ermdev.ees.legacy.controller.SpecialCurriculumController;
import io.ermdev.ees.legacy.util.ResourceHelper;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class SpecialCurriculumStage extends Stage {

    private SpecialCurriculumController controller;
    private OnItemAddLister onItemAddLister;

    public SpecialCurriculumStage() {
        try {
            initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(ResourceHelper.resourceWithBasePath("fxml/course_special_curriculum.fxml"));
            Parent root = loader.load();

            setScene(new Scene(root, 600, 500));
            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SpecialCurriculumController getController() {
        return controller;
    }

    public void setOnItemAddLister(OnItemAddLister onItemAddLister) {
        this.onItemAddLister = onItemAddLister;
    }

    public void callBack() {
        onItemAddLister.onAddCourse();
        Platform.runLater(() -> close());
    }

    @FunctionalInterface
    public interface OnItemAddLister {
        void onAddCourse();
    }
}
