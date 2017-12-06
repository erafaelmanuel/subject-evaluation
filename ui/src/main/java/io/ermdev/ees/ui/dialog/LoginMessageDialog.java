package io.ermdev.ees.ui.dialog;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginMessageDialog {

    public void display() {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);

        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-color: transparent");

        Label label = new Label();
        label.setText("sample");

        Button button = new Button("Close");
        button.setOnAction(e->stage.close());

        root.getChildren().add(button);

        Scene scene = new Scene(root, 100, 50);

        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.showAndWait();
    }
}
