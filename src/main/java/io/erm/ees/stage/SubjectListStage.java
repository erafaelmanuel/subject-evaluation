package io.erm.ees.stage;

import io.erm.ees.model.Subject;
import io.erm.ees.util.ResourceHelper;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class SubjectListStage extends Stage {

    private Subject subject;
    private OnAddItemListener listener;

    public SubjectListStage() {
        try {
            FXMLLoader loader = new FXMLLoader(ResourceHelper.resourceWithBasePath("fxml/subject_list.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 600, 650);

            initModality(Modality.APPLICATION_MODAL);
            setResizable(false);
            setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void callback(Subject subject) {
        this.subject = subject;
        listener.onAdd(subject);
        Platform.runLater(() -> close());
    }

    public Subject getSelectedItem() {
        return subject;
    }

    public void setListener(OnAddItemListener listener) {
        this.listener = listener;
    }

    @FunctionalInterface
    public interface OnAddItemListener {
        void onAdd(Subject item);
    }
}
