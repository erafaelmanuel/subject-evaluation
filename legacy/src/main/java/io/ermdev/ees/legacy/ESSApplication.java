package io.ermdev.ees.legacy;

import io.ermdev.ees.legacy.dao.conn.DbManager;
import io.ermdev.ees.legacy.dao.impl.v2.*;
import io.ermdev.ees.legacy.helper.DbFactory;
import io.ermdev.ees.legacy.model.UserType;
import io.ermdev.ees.legacy.stage.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;

public class ESSApplication extends Application implements ConfigurationStage.OnFinishListener, LoginStage.OnLoginListener,
        TeacherStage.OnSignOutListener, AdminStage.OnSignOutListener, DeanStage.OnSignOutListener {

    private Stage primaryStage;

    private DbSubject dbSubject;
    private DbCreditSubject dbCreditSubject;
    private DbAcademicYear dbAcademicYear;
    private DbCourse dbCourse;
    private DbStudent dbStudent;

    public static void main(String args[]) throws IOException {
        launch();
    }

    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setOnCloseRequest(e -> setOnClose());

        final DbManager dbManager = new DbManager();
        if (!dbManager.connect()) {
            showConfig(dbManager);
        } else {
            dbSubject = new DbSubject(dbManager);
            dbCreditSubject = new DbCreditSubject(dbManager);
            dbAcademicYear = new DbAcademicYear(dbManager);
            dbCourse = new DbCourse(dbManager);
            dbStudent = new DbStudent(dbManager);

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
        if (!status) {
            primaryStage.close();
            setOnClose();
        } else
            showLogin(dbManager);
    }

    @Override
    public void onLogin(boolean status, UserType userType) {
        if (!status)
            primaryStage.close();
        else {
            final DbManager dbManager = new DbManager();
            dbSubject = new DbSubject(dbManager);
            dbCreditSubject = new DbCreditSubject(dbManager);
            dbAcademicYear = new DbAcademicYear(dbManager);
            dbCourse = new DbCourse(dbManager);
            dbStudent = new DbStudent(dbManager);

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

            if (userType.getType().equals(UserType.ADMIN.getType()))
                showAdminWindow();
            else if (userType.getType().equals(UserType.DEAN.getType()))
                showDeanWindow();
            else
                showTeacherWindow();
        }
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
        Platform.runLater(adminStage::show);
        adminStage.getController().listener();
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

    private void setOnClose() {
        dbSubject.close();
        dbCreditSubject.close();
        dbAcademicYear.close();
        dbCourse.close();
        dbStudent.close();
    }
}
