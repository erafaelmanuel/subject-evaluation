package io.ermdev.ees;

import io.ermdev.ees.ui.login.LoginEvent;
import io.ermdev.ees.ui.login.LoginListener;
import io.ermdev.ees.ui.login.LoginStage;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class EESApplication extends Application implements LoginListener {

    private Stage primaryStage;
    private ApplicationContext applicationContext;

    public static void main(String args[]) {
        launch();
    }

    @Override
    public void init() throws Exception {
        applicationContext=SpringApplication.run(EESApplication.class);
    }

    public void start(Stage primaryStage) throws Exception {
        LoginStage loginStage = new LoginStage(applicationContext, this);
        loginStage.showAndWait();
    }


    @Override
    public void onLoginSuccess(LoginEvent loginEvent) {
        System.out.println(loginEvent.getUser());
    }
}
