package com.erm.project.ees;

import com.erm.project.ees.dao.conn.DBManager;
import com.erm.project.ees.model.UserType;
import com.erm.project.ees.stage.AdminStage;
import com.erm.project.ees.stage.ConfigurationStage;
import com.erm.project.ees.stage.LoginStage;
import com.erm.project.ees.stage.TeacherStage;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application implements ConfigurationStage.OnFinishListener, LoginStage.OnLoginListener,
    TeacherStage.OnSignOutListener, AdminStage.OnSignOutListener {

    private Stage primaryStage;

    public static void main(String args[]) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
        DBManager dbManager = new DBManager();
        if (!dbManager.connect()) {
            showConfig(dbManager);
        } else
            showLogin(dbManager);
    }

    @Override
    public void onFinish(DBManager dbManager, boolean status) {
        if (!status)
            primaryStage.close();
        else
            showLogin(dbManager);
    }

    @Override
    public void onLogin(boolean status, UserType userType) {
        if (!status)
            primaryStage.close();
        else if(userType.getType().equals(UserType.ADMIN.getType()))
            showAdminWindow();
        else
            showTeacherWindow();
    }

    private void showConfig(DBManager dbManager) {
        ConfigurationStage configurationStage = new ConfigurationStage(dbManager);
        configurationStage.setOnFinishListener(this);
        configurationStage.showAndWait();
    }

    private void showLogin(DBManager dbManager) {
        LoginStage loginStage = new LoginStage(dbManager);
        loginStage.setOnLoginListener(this);
        loginStage.showAndWait();
    }

    private void showAdminWindow() {
        AdminStage adminStage = new AdminStage();
        adminStage.setListener(this);
        adminStage.show();
    }

    private void showTeacherWindow() {
        TeacherStage teacherStage = new TeacherStage();
        teacherStage.setListener(this);
        teacherStage.show();
    }

    @Override
    public void onSignout() {
        LoginStage loginStage = new LoginStage();
        loginStage.setOnLoginListener(this);
        loginStage.showAndWait();
    }
}
