package io.erm.ees;

import io.erm.ees.dao.conn.DBManager;
import io.erm.ees.model.UserType;
import io.erm.ees.stage.*;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application implements ConfigurationStage.OnFinishListener, LoginStage.OnLoginListener,
        TeacherStage.OnSignOutListener, AdminStage.OnSignOutListener, DeanStage.OnSignOutListener {

    private Stage primaryStage;

    public static void main(String args[]) throws IOException {
        launch();
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
        else if(userType.getType().equals(UserType.DEAN.getType()))
            showDeanWindow();
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

    protected void showDeanWindow() {
        DeanStage deanStage = new DeanStage();
        deanStage.setListener(this);
        deanStage.show();
    }

    @Override
    public void onSignout() {
        LoginStage loginStage = new LoginStage();
        loginStage.setOnLoginListener(this);
        loginStage.showAndWait();
    }
}
