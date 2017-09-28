package com.erm.project.ees.stage;

import com.erm.project.ees.controller.StudentFindController;
import com.erm.project.ees.util.ResourceHelper;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class StudentFindStage extends Stage {

    private StudentFindController controller;
    private OnItemAddLister onItemAddLister;

    public StudentFindStage() {
        try {
            initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(ResourceHelper.resourceWithBasePath("fxml/student_find.fxml"));
            Parent root = loader.load();

            setScene(new Scene(root, 273, 241));
            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public StudentFindController getController() {
        return controller;
    }

    public void setOnItemAddLister(OnItemAddLister onItemAddLister) {
        this.onItemAddLister = onItemAddLister;
    }

    public void callBack() {
        onItemAddLister.onAddCourse();
        Platform.runLater(()->close());
    }

    @FunctionalInterface
    public interface OnItemAddLister {
        void onAddCourse();
    }
}
