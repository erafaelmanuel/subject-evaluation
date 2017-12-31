package io.ermdev.ees.ui.teacher;

import io.ermdev.ees.business.login.LoginListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.logging.Logger;

public class TeacherStage extends Stage {

    private static final Logger LOGGER = Logger.getLogger(TeacherStage.class.getSimpleName());

    public TeacherStage(ApplicationContext context, LoginListener listener) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(new ClassPathResource("fxml/teacher_menu.fxml").getURL());

            Parent root = loader.load();
            Scene scene = new Scene(root, 750, 500);
            scene.getStylesheets().add(new ClassPathResource("css/teacher_style.css").getURL().toString());

            initStyle(StageStyle.UNDECORATED);
            setMinWidth(750);
            setMinHeight(500);
            setResizable(false);
            setScene(scene);

            TeacherController controller=loader.getController();
            controller.setStage(this);

        } catch (IOException e) {
            LOGGER.info(e.getMessage());
        }
    }
}
