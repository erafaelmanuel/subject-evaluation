package classifyx.ui.login;

import classifyx.data.service.LoginService;
import classifyx.event.LoginEvent;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import classifyx.commons.Dimension;
import classifyx.listener.LoginListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
        this.listener = listener;
    }

    @FXML
    public void onActionLogin(ActionEvent event) {
        String username = txUsername.getText().trim();
        String password = txPassword.getText().trim();

        if (loginService.authenticateUser(username, password)) {
            if (stage != null) {
                stage.close();
            } else {
                ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
            }
            LoginEvent loginEvent = new LoginEvent(loginService.getUser());
            listener.onLoginSuccess(loginEvent);
        } else {
            LoginDialog dialog = new LoginDialog();
            dialog.displayError();
        }
    }

    @FXML
    public void onPressedTitle(MouseEvent m) {
        if (m.isPrimaryButtonDown()) {
            dimension.setX(m.getSceneX());
            dimension.setY(m.getSceneY());
        }
    }

    @FXML
    public void onDraggedTitle(MouseEvent m) {
        if (m.isPrimaryButtonDown()) {
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
