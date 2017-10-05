package com.erm.project.ees.stage;

import com.erm.project.ees.controller.UserInputController;
import com.erm.project.ees.util.ResourceHelper;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class UserInputStage extends Stage {

    private OnItemAddLister onItemAddLister;
    private UserInputController controller;

    public UserInputStage() {
        try {
            initModality(Modality.APPLICATION_MODAL);
            setResizable(false);
            FXMLLoader loader = new FXMLLoader(ResourceHelper.resourceWithBasePath("fxml/user_input.fxml"));
            Parent root = loader.load();
            setScene(new Scene(root, 580, 290));
            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UserInputController getController() {
        return controller;
    }

    public void setOnItemAddLister(OnItemAddLister onItemAddLister) {
        this.onItemAddLister = onItemAddLister;
    }

    public void callBack() {
        onItemAddLister.onAddUser();
        Platform.runLater(() -> close());
    }

    @FunctionalInterface
    public interface OnItemAddLister {
        void onAddUser();
    }
}
