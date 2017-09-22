package com.erm.project.ees.stage;

import com.erm.project.ees.dao.conn.DBManager;
import com.erm.project.ees.util.ResourceHelper;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

public class LoginStage extends Stage {

    private Logger logger = Logger.getLogger(LoginStage.class.getSimpleName());

    private DBManager dbManager;
    private boolean status;

    private OnLoginListener onLoginListener;

    public LoginStage(final String path, DBManager dbManager) {
        this.dbManager = dbManager;
        display(path);
    }

    public LoginStage(final String path, DBManager dbManager, OnLoginListener onLoginListener) {
        this.dbManager = dbManager;
        this.onLoginListener = onLoginListener;
        display(path);
    }

    private void display(String path) {
        try {
            URL url = ResourceHelper.resource(path);
            Parent root = FXMLLoader.load(url);
            Scene scene = new Scene(root, 520, 390);
            scene.getStylesheets().add(ResourceHelper.resource("/css/login.css").toString());

            this.setTitle("Login");
            this.setScene(scene);
            this.setMinWidth(540);
            this.setMinHeight(420);
            this.setOnCloseRequest((e) -> {
                onLoginListener.onLogin(false);
            });
        } catch (IOException e) {
            logger.info("IOException");
        }
    }

    public DBManager getDbManager() {
        return dbManager;
    }

    public void setOnLoginListener(OnLoginListener onLoginListener) {
        this.onLoginListener = onLoginListener;
    }

    public void callBack(boolean status) {
        this.status = status;
        Platform.runLater(() -> {
            this.close();
        });
        onLoginListener.onLogin(status);
    }

    @FunctionalInterface
    public interface OnLoginListener {
        void onLogin(boolean status);
    }
}
