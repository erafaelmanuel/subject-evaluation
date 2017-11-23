package io.ermdev.ees.controller;

import io.ermdev.ees.dao.UserDetailDao;
import io.ermdev.ees.dao.impl.UserDetailDaoImpl;
import io.ermdev.ees.model.UserDetail;
import io.ermdev.ees.stage.LoginStage;
import io.ermdev.ees.util.ResourceHelper;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Label lbTitle;
    @FXML
    private TextField txUsername;
    @FXML
    private PasswordField txPassword;
    @FXML
    private Button bnLogin;
    @FXML
    private VBox plLoading;
    @FXML
    private ImageView imgLoading;
    @FXML
    private VBox plMessage;
    @FXML
    private Label lbMessage;
    @FXML
    private ImageView imgLogo;

    private UserDetail userDetail;

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        try {
            Image image = new Image(ResourceHelper.resourceWithBasePath("image/loading.gif").toString());
            Image logo = new Image(ResourceHelper.resourceWithBasePath("image/ccslogo.png").toString());

            plLoading.setVisible(false);
            imgLoading.setVisible(false);

            imgLoading.setImage(image);
            imgLogo.setImage(logo);
            bnLogin.setDisable(true);

            txUsername.focusedProperty().addListener((ob, pOld, pNew) -> {
                if (pNew) txUsername.setStyle("-fx-border-width:0;");
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onClickLogin(ActionEvent event) throws Exception {
        new Thread(() -> {
            Platform.runLater(loading());

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            LoginStage loginStage = (LoginStage) ((Node) event.getSource()).getScene().getWindow();
            UserDetailDao userDetailDao = new UserDetailDaoImpl(loginStage.getDbManager());

            boolean isValid = false;

            for (UserDetail userDetail : userDetailDao.getUserDetailList()) {
                if (txUsername.getText().trim().equals(userDetail.getUsername()) &&
                        txPassword.getText().trim().equals(userDetail.getPassword()) && userDetail.isActivated()) {
                    isValid = true;
                    this.userDetail = userDetail;
                    break;
                }
            }
            if (!isValid) {
                Platform.runLater(loginFail());
            } else {
                new Thread(() ->
                        Platform.runLater(() ->
                                loginStage.callBack(true, userDetail.getUserType())))
                        .start();
            }

            Platform.runLater(() -> {
                plLoading.setVisible(false);
                imgLoading.setVisible(false);

                txUsername.setDisable(false);
                txPassword.setDisable(false);
                bnLogin.setDisable(false);
            });
        }).start();
    }

    @FXML
    public void onTextChange(KeyEvent event) {
        if (!txUsername.getText().trim().equals("") && !txPassword.getText().trim().equals(""))
            bnLogin.setDisable(false);
        else
            bnLogin.setDisable(true);
    }


    private void fadeIn(Node node) {
        FadeTransition ft = new FadeTransition(Duration.millis(500), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    private Runnable loading() {
        return () -> {
            plMessage.setStyle("-fx-background-color:#2ecc71");
            lbMessage.setText("Loading. . .");

            txUsername.setDisable(true);
            txPassword.setDisable(true);
            bnLogin.setDisable(true);

            plLoading.setVisible(true);
            imgLoading.setVisible(true);
        };
    }

    private Runnable loginFail() {
        return () -> {
            plMessage.setStyle("-fx-background-color:#e74c3c");
            lbMessage.setText("Invalid username or password.");
            fadeIn(plMessage);
        };
    }
}