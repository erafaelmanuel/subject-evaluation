package io.ermdev.ees.legacy.stage;

import io.ermdev.ees.legacy.controller.GradeInputController;
import io.ermdev.ees.legacy.util.ResourceHelper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class GradeInputStage extends Stage {

    private GradeInputController controller;
    private OnItemAddListener listener;
    Parent root = null;

    public GradeInputStage() {
        try {
            FXMLLoader loader = new FXMLLoader(ResourceHelper.resourceWithBasePath("fxml/grade_input.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 525, 250);

            initModality(Modality.APPLICATION_MODAL);
            setResizable(false);
            setScene(scene);

            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void callBack() {
        listener.onAddMark();
    }

    public GradeInputController getController() {
        return controller;
    }

    public void setListener(OnItemAddListener listener) {
        this.listener = listener;
    }

    public interface OnItemAddListener {
        void onAddMark();
    }

    @Override
    public void showAndWait() {
        super.showAndWait();
        if(root != null)
            root.requestFocus();
    }
}
