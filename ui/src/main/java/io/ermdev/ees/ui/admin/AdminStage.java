package io.ermdev.ees.ui.admin;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class AdminStage extends Stage {

    public AdminStage() {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(new ClassPathResource("/fxml/admin.fxml").getURL());

            Parent root = loader.load();
            AdminController controller = loader.getController();
            Scene scene = new Scene(root);

            setScene(scene);
            setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
