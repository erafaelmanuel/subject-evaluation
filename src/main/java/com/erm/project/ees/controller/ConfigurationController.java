package com.erm.project.ees.controller;

import com.erm.project.ees.dao.conn.DBManager;
import com.erm.project.ees.dao.conn.UserLibrary;
import com.erm.project.ees.stage.ConfigurationStage;
import com.erm.project.ees.util.ConnectionHelper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ConfigurationController extends Stage implements Initializable {

    @FXML
    private Button bnConnect;

    @FXML
    private Button bnSave;

    @FXML
    private TextField txHost;

    @FXML
    private TextField txPort;

    @FXML
    private TextField txUsername;

    @FXML
    private PasswordField txPassword;

    @FXML
    private TextField txCatalog;

    @FXML
    private VBox plMessage;

    @FXML
    private Label lbMessage;

    @FXML
    private Label lbLoading;

    private DBManager dbManager;

    @FXML
    private void onConnectClick(ActionEvent event) {
        dbManager =
                ((ConfigurationStage) ((Stage) ((Node)event.getSource()).getScene().getWindow())).getDbManager();
        UserLibrary userLibrary = ConnectionHelper.getUserLibrary();

        userLibrary.setHost(txHost.getText());
        userLibrary.setPort(txPort.getText());
        userLibrary.setUsername(txUsername.getText());
        userLibrary.setPassword(txPassword.getText());
        userLibrary.setCatalog(txCatalog.getText());

        Platform.runLater(()->{
            lbLoading.setVisible(true);
            bnConnect.setDisable(true);

            txHost.setDisable(true);
            txPort.setDisable(true);
            txUsername.setDisable(true);
            txPassword.setDisable(true);
            txCatalog.setDisable(true);
        });

        new Thread(()->{
            if (!dbManager.connect(userLibrary)) {
                Platform.runLater(()->{
                    plMessage.setStyle("-fx-background-color:#e74c3c");
                    lbMessage.setText("ERROR : Bad connection. Please try again");

                    lbLoading.setVisible(false);
                    bnConnect.setDisable(false);

                    txHost.setDisable(false);
                    txPort.setDisable(false);
                    txUsername.setDisable(false);
                    txPassword.setDisable(false);
                    txCatalog.setDisable(false);
                });
            } else {
                Platform.runLater(()->{
                    plMessage.setStyle("-fx-background-color:#2ecc71");
                    lbMessage.setText("SUCCESS : Database connected.");

                    txHost.setDisable(true);
                    txPort.setDisable(true);
                    txUsername.setDisable(true);
                    txPassword.setDisable(true);
                    txCatalog.setDisable(true);

                    bnSave.setDisable(false);
                    bnConnect.setDisable(true);
                    lbLoading.setVisible(false);
                });
            }
        }).start();
    }

    @FXML
    private void onClickSave(ActionEvent event) {
        ConfigurationStage configurationStage = (ConfigurationStage)((Node) event.getSource()).getScene().getWindow();
        ConnectionHelper.setUserLibrary(dbManager.getUserLibrary());
        configurationStage.callBack(dbManager, true);
    }

    private void init() {
        try {
            UserLibrary userLibrary = ConnectionHelper.getUserLibrary();
            if (userLibrary != null) {
                String host = userLibrary.getHost() != null ? userLibrary.getHost() : "";
                String port = userLibrary.getPort() != null ? userLibrary.getPort() : "";
                String username = userLibrary.getUsername() != null ? userLibrary.getUsername() : "";
                String password = userLibrary.getPassword() != null ? userLibrary.getPassword() : "";
                String catalog = userLibrary.getCatalog() != null ? userLibrary.getCatalog() : "";

                txHost.setText(host);
                txPort.setText(port);
                txUsername.setText(username);
                txPassword.setText(password);
                txCatalog.setText(catalog);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
    }
}