package io.ermdev.ees.ui.login;

import io.ermdev.ess.data.repository.UserRepository;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class LoginController implements Initializable {


    @FXML
    private TextField txUsername;
    @FXML
    private PasswordField txPassword;

    @FXML
    public void onActionLogin() {
        System.out.println();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
