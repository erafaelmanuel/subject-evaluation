package io.ermdev.ees.ui.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import io.ermdev.ess.business.Dimension;
import io.ermdev.ess.business.login.LoginEvent;
import io.ermdev.ess.business.login.LoginListener;
import io.ermdev.ess.business.login.LoginService;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

import java.util.logging.Logger;

public class LoginController {

    private final Logger logger = Logger.getLogger(LoginController.class.getSimpleName());

    private Dimension dimension;

    private Stage stage;
    private ApplicationContext applicationContext;
    private LoginListener listener;

    private LoginService loginService;

    @FXML
    private JFXTextField txUsername;
    @FXML
    private JFXPasswordField txPassword;

    public LoginController() {
        dimension = new Dimension();
    }

    protected void setStage(Stage stage) {
        this.stage = stage;
    }

    protected void setApplicationContext(ApplicationContext applicationContext) {
        try {
            this.applicationContext = applicationContext;
            loginService = applicationContext.getBean(LoginService.class);
        } catch (NoSuchBeanDefinitionException e) {
            logger.info(e.getMessage());
        }
    }

    protected void setListener(LoginListener listener) {
        this.listener=listener;
    }

    @FXML
    public void onActionLogin() {
        String username = txUsername.getText().trim();
        String password = txPassword.getText().trim();

        if(loginService.authenticateUser(username, password)) {
            LoginEvent loginEvent = new LoginEvent(loginService.getUser());
            listener.onLoginSuccess(loginEvent);
        } else {
            System.out.println("failed");
            new LoginDialog().display();
        }
    }

    @FXML
    public void onPressedTitle(MouseEvent m) {
        if(m.isPrimaryButtonDown()) {
            dimension.setX(m.getSceneX());
            dimension.setY(m.getSceneY());
        }
    }

    @FXML
    public void onDraggedTitle(MouseEvent m) {
        if(m.isPrimaryButtonDown()) {
            stage.setX(m.getScreenX() - dimension.getX());
            stage.setY(m.getScreenY() - dimension.getY());
        }
    }

    @FXML
    public void onClickedClose() {
        stage.close();
    }

    @FXML
    public void onClickedMinimize() {
        stage.setIconified(true);
    }
}
