package io.ermdev.ees.legacy.stage;

import io.ermdev.ees.legacy.controller.AdminWindowController;
import io.ermdev.ees.legacy.util.ResourceHelper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminStage extends Stage {

    AdminWindowController controller;
    OnSignOutListener listener;

    public AdminStage() {
        try {
            FXMLLoader loader = new FXMLLoader(ResourceHelper.resourceWithBasePath("fxml/admin.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 0, 0);
            this.setScene(scene);
            this.setMaximized(true);

            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setListener(OnSignOutListener listener) {
        this.listener = listener;
    }

    public AdminWindowController getController() {
        return controller;
    }

    public void callBack() {
        listener.onSignout();
    }

    public interface OnSignOutListener {
        void onSignout();
    }
}
