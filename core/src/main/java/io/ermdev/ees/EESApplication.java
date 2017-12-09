package io.ermdev.ees;

import io.ermdev.ees.data.entity.Role;
import io.ermdev.ees.data.entity.User;
import io.ermdev.ees.data.repository.RoleRepository;
import io.ermdev.ees.data.repository.UserRepository;
import io.ermdev.ees.ui.login.LoginEvent;
import io.ermdev.ees.ui.login.LoginListener;
import io.ermdev.ees.ui.login.LoginStage;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EESApplication extends Application implements LoginListener {

    private Stage primaryStage;
    private ApplicationContext applicationContext;

    @Bean
    public CommandLineRunner commandLineRunner(UserRepository userRepository, RoleRepository roleRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... strings) throws Exception {
//                User user = new User();
//                user.setUsername("hottodogu");
//                user.setPassword("123");
//
//                Role role = new Role();
//                role.setName("ADMIN_ROLE");
//                roleRepository.save(role);
//
//                user.getRoles().add(role);
//                userRepository.save(user);

                userRepository.delete(7L);

//                Role role = roleRepository.findOne(2L);
//
//                roleRepository.delete(role);
            }
        };
    }

    public static void main(String args[]) {
        launch();
    }

    @Override
    public void init() throws Exception {
        applicationContext=SpringApplication.run(EESApplication.class);
        CommandLineRunner commandLineRunner=applicationContext.getBean(CommandLineRunner.class);
        commandLineRunner.run();
    }

    public void start(Stage primaryStage) throws Exception {
        this.primaryStage=primaryStage;
        LoginStage loginStage = new LoginStage(applicationContext, this);
        loginStage.showAndWait();
    }

    @Override
    public void onLoginSuccess(LoginEvent loginEvent) {
        System.out.println(loginEvent.getUser());
    }
}
