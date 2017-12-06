package io.ermdev.ees.ui.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import io.ermdev.ees.commons.util.Dimension;
import io.ermdev.ees.data.repository.UserRepository;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class LoginController implements Initializable {

    private final Logger logger = Logger.getLogger(LoginController.class.getSimpleName());

    private Dimension initDimension;
    private Dimension dimension;

    private Stage stage;

    private ApplicationContext applicationContext;
    private UserRepository userRepository;

    @FXML
    private JFXTextField txUsername;
    @FXML
    private JFXPasswordField txPassword;
    @FXML
    private Label lbTitle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initDimension = new Dimension();
        dimension = new Dimension();
    }

    protected void setStage(Stage stage) {
        this.stage = stage;
    }

    protected void setApplicationContext(ApplicationContext applicationContext) {
        try {
            this.applicationContext = applicationContext;
            userRepository = applicationContext.getBean(UserRepository.class);
        } catch (NoSuchBeanDefinitionException e) {
            logger.info(e.getMessage());
        }
    }

    @FXML
    public void onActionLogin() {
        System.out.println();
    }

    @FXML
    public void onPressedTitle(MouseEvent m) {
        if(m.isPrimaryButtonDown()) {
            dimension.setX(m.getSceneX());
            dimension.setY(m.getSceneY());

            initDimension.setX(stage.getX());
            initDimension.setY(stage.getY());
        }
    }

    @FXML
    public void onDraggedTitle(MouseEvent m) {
        if(m.isPrimaryButtonDown()) {
            stage.setX(m.getScreenX() - dimension.getX());
            stage.setY(m.getScreenY() - dimension.getY());
        }
    }
}
