package com.erm.project.ees;

import com.erm.project.ees.dao.conn.DBManager;
import com.erm.project.ees.stage.ConfigurationStage;
import com.erm.project.ees.stage.LoginStage;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application implements ConfigurationStage.OnFinishListener, LoginStage.OnLoginListener {

    private Stage primaryStage;

    public static void main(String args[]) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        DBManager dbManager = new DBManager();
        if (!dbManager.connect()) {
            showConfig(dbManager);
        }
        showLogin(dbManager);
    }

    @Override
    public void onFinish(DBManager dbManager, boolean status) {
        if (!status)
            primaryStage.close();
        showLogin(dbManager);
    }

    @Override
    public void onLogin(boolean status) {
        if (!status)
            primaryStage.close();
    }

    private void showConfig(DBManager dbManager) {
        ConfigurationStage configurationStage = new ConfigurationStage("/fxml/config.fxml", dbManager);
        configurationStage.setOnFinishListener(this);
        configurationStage.showAndWait();
    }

    private void showLogin(DBManager dbManager) {
        LoginStage loginStage = new LoginStage("/fxml/login.fxml", dbManager);
        loginStage.setOnLoginListener(this);
        loginStage.showAndWait();
    }
}
