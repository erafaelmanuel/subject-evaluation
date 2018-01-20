package io.ermdev.ees.legacy.stage;

import io.ermdev.ees.legacy.controller.AcademicYearInputController;
import io.ermdev.ees.legacy.util.ResourceHelper;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AcademicYearInputStage extends Stage {

    private OnItemAddLister onItemAddLister;
    private AcademicYearInputController controller;

    public AcademicYearInputStage() {
        try {
            FXMLLoader loader = new FXMLLoader(ResourceHelper.resourceWithBasePath("fxml/academic_year_input.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 590, 370);

            initModality(Modality.APPLICATION_MODAL);
            setResizable(false);
            setScene(scene);

            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOnItemAddLister(OnItemAddLister onItemAddLister) {
        this.onItemAddLister = onItemAddLister;
    }

    public void callBack() {
        onItemAddLister.onAddAcademicYear();
        Platform.runLater(() -> close());
    }

    public AcademicYearInputController getController() {
        return controller;
    }

    @FunctionalInterface
    public interface OnItemAddLister {
        void onAddAcademicYear();
    }
}
