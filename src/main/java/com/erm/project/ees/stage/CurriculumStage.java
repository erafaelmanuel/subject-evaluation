package com.erm.project.ees.stage;

import com.erm.project.ees.controller.CurriculumController;
import com.erm.project.ees.util.ResourceHelper;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CurriculumStage extends Stage {

    private CurriculumController controller;
    private OnItemAddLister onItemAddLister;

    public CurriculumStage() {
        try {
            initModality(Modality.APPLICATION_MODAL);
            setResizable(false);
            FXMLLoader loader = new FXMLLoader(ResourceHelper.resourceWithBasePath("fxml/course_curriculum.fxml"));
            Parent root = loader.load();
            setScene(new Scene(root, 700, 600));
            setOnCloseRequest(e -> new Thread(() -> controller.dispose(CurriculumController.State.DISCARD)).start());
            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CurriculumController getController() {
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
