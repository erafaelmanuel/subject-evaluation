package io.ermdev.ees.controller;

import io.ermdev.ees.dao.conn.DbManager;
import io.ermdev.ees.dao.conn.DbUserLibrary;
import io.ermdev.ees.stage.ConfigurationStage;
import io.ermdev.ees.util.ConnectionHelper;
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

    private DbManager dbManager;

    @FXML
    private void onConnectClick(ActionEvent event) {
        dbManager = ((ConfigurationStage) ((Node) event.getSource()).getScene().getWindow()).getDbManager();
        DbUserLibrary dbUserLibrary = ConnectionHelper.getUserLibrary();

        dbUserLibrary.setHost(txHost.getText());
        dbUserLibrary.setPort(txPort.getText());
        dbUserLibrary.setUsername(txUsername.getText());
        dbUserLibrary.setPassword(txPassword.getText());
        dbUserLibrary.setCatalog(txCatalog.getText());

        Platform.runLater(() -> {
            lbLoading.setVisible(true);
            bnConnect.setDisable(true);

            txHost.setDisable(true);
            txPort.setDisable(true);
            txUsername.setDisable(true);
            txPassword.setDisable(true);
            txCatalog.setDisable(true);
        });

        new Thread(() -> {
            dbUserLibrary.setHost(txHost.getText());
            dbUserLibrary.setPort(txPort.getText());
            dbUserLibrary.setUsername(txUsername.getText());
            dbUserLibrary.setPassword(txPassword.getText());
            dbUserLibrary.setCatalog(txCatalog.getText());
            if (!dbManager.connect(dbUserLibrary)) {
                Platform.runLater(() -> {
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
                Platform.runLater(() -> {
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
        ConfigurationStage configurationStage = (ConfigurationStage) ((Node) event.getSource()).getScene().getWindow();
        ConnectionHelper.setUserLibrary(dbManager.getDbUserLibrary());
        configurationStage.callBack(dbManager, true);
    }

    private void init() {
        try {
            DbUserLibrary dbUserLibrary = ConnectionHelper.getUserLibrary();
            if (dbUserLibrary != null) {
                String host = dbUserLibrary.getHost() != null ? dbUserLibrary.getHost() : "";
                String port = dbUserLibrary.getPort() != null ? dbUserLibrary.getPort() : "";
                String username = dbUserLibrary.getUsername() != null ? dbUserLibrary.getUsername() : "";
                String password = dbUserLibrary.getPassword() != null ? dbUserLibrary.getPassword() : "";
                String catalog = dbUserLibrary.getCatalog() != null ? dbUserLibrary.getCatalog() : "";

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