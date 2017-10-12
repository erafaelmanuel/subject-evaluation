package io.erm.ees.test.util;

import io.erm.ees.dao.impl.StudentDaoImpl;
import io.erm.ees.helper.EvaluationHelper;
import io.erm.ees.stage.AcademicYearInputStage;
import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.Test;

public class SomethingTest extends Application{

    @Test
    public void test() {
        launch();
//        EvaluationHelper evaluationHelper = EvaluationHelper.getInstance();
//        evaluationHelper.condition(new StudentDaoImpl().getStudentById(1), 3, 2);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AcademicYearInputStage academicYearInputStage = new AcademicYearInputStage();
        academicYearInputStage.showAndWait();
    }
}
