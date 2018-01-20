package io.ermdev.ees.legacy.stage;

import io.ermdev.ees.legacy.controller.DeanWindowController;
import io.ermdev.ees.legacy.util.ResourceHelper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DeanStage extends Stage {

    private DeanWindowController controller;
    private AdminStage.OnSignOutListener listener;
    public DeanStage() {
        try {
            FXMLLoader loader = new FXMLLoader(ResourceHelper.resourceWithBasePath("fxml/dean.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 0, 0);
            setScene(scene);
            setMaximized(true);
            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DeanWindowController getController() {
        return controller;
    }

    public void setListener(AdminStage.OnSignOutListener listener) {
        this.listener = listener;
    }

    public void callBack() {
        listener.onSignout();
    }

    public interface OnSignOutListener {
        void onSignout();
    }
}
