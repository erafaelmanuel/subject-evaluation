package com.erm.project.ees;

import com.erm.project.ees.dao.StudentDao;
import com.erm.project.ees.dao.conn.DBManager;
import com.erm.project.ees.dao.impl.StudentDaoImpl;
import com.erm.project.ees.model.Student;
import com.erm.project.ees.stage.ConfigurationStage;
import com.erm.project.ees.stage.LoginStage;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application implements ConfigurationStage.OnFinishListener, LoginStage.OnLoginListener {

    private Stage primaryStage;

    public static void main(String args[]) {
        StudentDao studentDao = new StudentDaoImpl();
        Student student = new Student(1, "ff","l","m",14,
                "male", 12334, 1);
        System.out.println(studentDao.updateStudentById(1, student));
        //launch(args);
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
