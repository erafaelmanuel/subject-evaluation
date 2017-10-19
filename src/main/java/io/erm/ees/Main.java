package io.erm.ees;

import io.erm.ees.dao.conn.DbManager;
import io.erm.ees.dao.impl.v2.*;
import io.erm.ees.helper.DbFactory;
import io.erm.ees.model.UserType;
import io.erm.ees.stage.*;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application implements ConfigurationStage.OnFinishListener, LoginStage.OnLoginListener,
        TeacherStage.OnSignOutListener, AdminStage.OnSignOutListener, DeanStage.OnSignOutListener {

    private Stage primaryStage;

    private final DbSubject dbSubject = new DbSubject();
    private final DbCreditSubject dbCreditSubject = new DbCreditSubject();
    private final DbAcademicYear dbAcademicYear = new DbAcademicYear();
    private final DbCourse dbCourse = new DbCourse();
    private final DbStudent dbStudent = new DbStudent();

    public static void main(String args[]) throws IOException {
        launch();
    }

    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        DbManager dbManager = new DbManager();
        if (!dbManager.connect()) {
            showConfig(dbManager);
        } else {
            dbSubject.open();
            dbCreditSubject.open();
            dbAcademicYear.open();
            dbCourse.open();
            dbStudent.open();

            DbFactory.addSubjectFactory(dbSubject);
            DbFactory.addCreditSubjectFactory(dbCreditSubject);
            DbFactory.addAcademicYearFactory(dbAcademicYear);
            DbFactory.addCourseFactory(dbCourse);
            DbFactory.addStudentFactory(dbStudent);

            showLogin(dbManager);
        }
    }

    @Override
    public void onFinish(DbManager dbManager, boolean status) {
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

    private void showConfig(DbManager dbManager) {
        ConfigurationStage configurationStage = new ConfigurationStage(dbManager);
        configurationStage.setOnFinishListener(this);
        configurationStage.showAndWait();
    }

    private void showLogin(DbManager dbManager) {
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

    private void showDeanWindow() {
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
