package io.ermdev.ees;

import io.ermdev.ees.data.entity.Role;
import io.ermdev.ees.data.entity.User;
import io.ermdev.ees.data.repository.RoleRepository;
import io.ermdev.ees.data.repository.RoleRepositoryImpl;
import io.ermdev.ees.data.repository.UserRepository;
import io.ermdev.ees.ui.login.LoginEvent;
import io.ermdev.ees.ui.login.LoginListener;
import io.ermdev.ees.ui.login.LoginStage;
import javafx.application.Application;
import javafx.stage.Stage;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@SpringBootApplication
@EnableJpaRepositories("io.ermdev.ees.data")
public class EESApplication extends Application implements LoginListener {

    private Stage primaryStage;
    private ApplicationContext applicationContext;

    @Bean
    public CommandLineRunner commandLineRunner(UserRepository userRepository, RoleRepositoryImpl roleRepository) {

        return new CommandLineRunner() {
            @Override
            public void run(String... strings) throws Exception {
//                User user = new User();
//                user.setUsername("hottodogu");
//                user.setPassword("123");
//
                Role role = new Role();
//                role.setName("ADMIN_ROLE");
//                roleRepository.save(role);
////
//                user.getRoles().add(role);
//                userRepository.save(user);
//                System.out.println("test");

//                userRepository.delete((long)10);

                role = roleRepository.findById(new Long(9));
                List<User> users = role.getUsers();
                System.out.println(users.size());
//                //role.getUsers().clear();
////
//                roleRepository.delete(new Long(9));
            }
        };
    }

    public static void main(String args[]) {
        launch();
    }

    @Override
    public void init() throws Exception {
        applicationContext=SpringApplication.run(EESApplication.class);
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
