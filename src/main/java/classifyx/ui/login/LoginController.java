package classifyx.ui.login;

import classifyx.data.service.LoginService;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

public class LoginController {

    private Stage stage;

    private ApplicationContext context;

    private LoginListener listener;

    private LoginService loginService;

    @FXML
    private JFXTextField txUsername;

    @FXML
    private JFXPasswordField txPassword;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setApplicationContext(ApplicationContext context) {
        try {
            this.context = context;
            loginService = context.getBean(LoginService.class);
        } catch (NoSuchBeanDefinitionException e) {
            e.printStackTrace();
        }
    }

    public void setListener(LoginListener listener) {
        this.listener = listener;
    }

    @FXML
    public void onLogin(ActionEvent event) {
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
}
