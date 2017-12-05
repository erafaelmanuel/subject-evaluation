package io.ermdev.ees;

import io.ermdev.ees.ui.login.LoginStage;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication(scanBasePackages = "io.ermdev.ees")
public class EESApplication extends Application {

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
        new LoginStage().showAndWait();
    }


}
