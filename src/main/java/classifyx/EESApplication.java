package classifyx;

import classifyx.ui.login.LoginEvent;
import classifyx.ui.login.LoginListener;
import classifyx.ui.login.LoginStage;
import classifyx.ui.teacher.TeacherStage;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class EESApplication extends Application implements LoginListener {

    private ApplicationContext applicationContext;

    @Override
    public void init() {
        applicationContext = SpringApplication.run(EESApplication.class);
    }

    @Override
    public void start(Stage primaryStage) {
        LoginStage loginStage = new LoginStage(applicationContext, this);
        loginStage.show();
    }

    @Override
    public void onLoginSuccess(LoginEvent loginEvent) {
        TeacherStage teacherStage = new TeacherStage(applicationContext, this);
        teacherStage.show();
    }

    public static void main(String args[]) {
        launch();
    }
}
