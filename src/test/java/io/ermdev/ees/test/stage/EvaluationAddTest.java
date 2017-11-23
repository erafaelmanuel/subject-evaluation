//package io.erm.ees.test.stage;
//
//import io.erm.ees.dao.impl.v2.*;
//import DbFactory;
//import EvaluationAddStage;
//import javafx.application.Application;
//import javafx.application.Platform;
//import javafx.stage.Stage;
//import org.junit.Test;
//
//import java.io.IOException;
//
//public class EvaluationAddTest extends Application{
//
//    private final DbSubject dbSubject = new DbSubject();
//    private final DbCreditSubject dbCreditSubject = new DbCreditSubject();
//    private final DbAcademicYear dbAcademicYear = new DbAcademicYear();
//    private final DbCourse dbCourse = new DbCourse();
//    private final DbStudent dbStudent = new DbStudent();
//
//    public static void main(String args[]) throws IOException {
//        launch();
//    }
//
//    @Test
//    public void show() {
//        dbSubject.open();
//        dbCreditSubject.open();
//        dbAcademicYear.open();
//        dbCourse.open();
//        dbStudent.open();
//
//        DbFactory.addSubjectFactory(dbSubject);
//        DbFactory.addCreditSubjectFactory(dbCreditSubject);
//        DbFactory.addAcademicYearFactory(dbAcademicYear);
//        DbFactory.addCourseFactory(dbCourse);
//        DbFactory.addStudentFactory(dbStudent);
//
//        launch();
//    }
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        EvaluationAddStage stage = new EvaluationAddStage();
//        Platform.runLater(stage::showAndWait);
//        System.out.println(stage.getController());
//        DbStudent dbStudent = new DbStudent();
//        dbStudent.open();
//        stage.getController().listener(dbStudent.getStudentById(570585593910811648L));
//
//    }
//}
