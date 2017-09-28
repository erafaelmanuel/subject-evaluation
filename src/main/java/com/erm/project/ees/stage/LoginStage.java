package com.erm.project.ees.stage;

import com.erm.project.ees.dao.conn.DBManager;
import com.erm.project.ees.model.UserType;
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
    private UserType userType;

    private OnLoginListener onLoginListener;

    public LoginStage() {
        this.dbManager = new DBManager();
        try {
            URL url = ResourceHelper.resourceWithBasePath("fxml/login.fxml");
            Parent root = FXMLLoader.load(url);
            Scene scene = new Scene(root, 520, 390);
            scene.getStylesheets().add(ResourceHelper.resourceWithBasePath("css/login.css").toString());

            this.setTitle("Login");
            this.setScene(scene);
            this.setMinWidth(540);
            this.setMinHeight(420);
            this.setOnCloseRequest((e) -> {
                onLoginListener.onLogin(false, userType);
            });
        } catch (IOException e) {
            logger.info("IOException");
        }
    }


    public LoginStage(DBManager dbManager) {
        this.dbManager = dbManager;
        try {
            URL url = ResourceHelper.resourceWithBasePath("fxml/login.fxml");
            Parent root = FXMLLoader.load(url);
            Scene scene = new Scene(root, 520, 390);
            scene.getStylesheets().add(ResourceHelper.resourceWithBasePath("css/login.css").toString());

            this.setTitle("Login");
            this.setScene(scene);
            this.setMinWidth(540);
            this.setMinHeight(420);
            this.setOnCloseRequest((e) -> {
                onLoginListener.onLogin(false, userType);
            });
        } catch (IOException e) {
            logger.info("IOException");
        }
    }

    public LoginStage(DBManager dbManager, OnLoginListener onLoginListener) {
        this.dbManager = dbManager;
        this.onLoginListener = onLoginListener;
        try {
            URL url = ResourceHelper.resourceWithBasePath("fxml/login.fxml");
            Parent root = FXMLLoader.load(url);
            Scene scene = new Scene(root, 520, 390);
            scene.getStylesheets().add(ResourceHelper.resourceWithBasePath("css/login.css").toString());

            this.setTitle("Login");
            this.setScene(scene);
            this.setMinWidth(540);
            this.setMinHeight(420);
            this.setOnCloseRequest((e) -> {
                onLoginListener.onLogin(false, userType);
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

    public void callBack(boolean status, UserType userType) {
        this.status = status;
        this.userType = userType;
        close();
        onLoginListener.onLogin(status, userType);
    }

    @FunctionalInterface
    public interface OnLoginListener {
        void onLogin(boolean status, UserType userType);
    }
}
