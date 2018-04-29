package classifyx.ui.login;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.net.URL;

public class LoginStage extends Stage {

    public LoginStage(ApplicationContext context, LoginListener listener) {
        final ClassLoader classLoader = getClass().getClassLoader();
        try {
            final URL fxml = classLoader.getResource("fxml/_login.fxml");
            final URL style = classLoader.getResource("css/_login.css");
            final FXMLLoader loader = new FXMLLoader();

            if (fxml != null) {
                loader.setLocation(fxml);
            }

            final Parent root = loader.load();
            final Scene scene = new Scene(root, 592, 390);

            if (style != null) {
                scene.getStylesheets().add(style.toString());
            }
            setMinWidth(592);
            setMinHeight(390);
            setResizable(false);
            setScene(scene);
            setResizable(false);

            LoginController controller = loader.getController();
            controller.setStage(this);
            controller.setApplicationContext(context);
            controller.setListener(listener);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
