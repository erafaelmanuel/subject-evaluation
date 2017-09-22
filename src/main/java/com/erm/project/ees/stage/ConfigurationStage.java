package com.erm.project.ees.stage;

import com.erm.project.ees.dao.conn.DBManager;
import com.erm.project.ees.util.ResourceHelper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

public class ConfigurationStage extends Stage{

    private Logger logger = Logger.getLogger(ConfigurationStage.class.getSimpleName());

    private DBManager dbManager;
    private boolean status;

    private OnFinishListener onFinishListener;

    public ConfigurationStage(final String path, DBManager dbManager) {
        this.dbManager = dbManager;
        display(path);
    }

    public ConfigurationStage(final String path, DBManager dbManager, OnFinishListener onFinishListener) {
        this.dbManager = dbManager;
        this.onFinishListener = onFinishListener;
        display(path);

    }

    private void display(String path) {
        try {
            URL url = ResourceHelper.resource(path);
            Parent root = FXMLLoader.load(url);
            Scene scene = new Scene(root, 365, 345);

            this.setTitle("UserLibrary configuration");
            this.setResizable(false);
            this.setScene(scene);
            this.setOnCloseRequest((e)->{
                onFinishListener.onFinish(dbManager, status);
            });
        } catch (IOException e) {
            logger.info("IOException");
        }
    }

    public DBManager getDbManager() {
        return dbManager;
    }

    public void setOnFinishListener(OnFinishListener onFinishListener) {
        this.onFinishListener = onFinishListener;
    }

    public void callBack(DBManager dbManager, boolean status) {
        this.dbManager = dbManager;
        this.status = status;
        this.close();
        onFinishListener.onFinish(dbManager, status);
    }

    @FunctionalInterface
    public interface OnFinishListener {
        void onFinish(DBManager dbManager, boolean status);
    }
}
