package com.erm.project.ees.controller;

import com.erm.project.ees.dao.SubjectDao;
import com.erm.project.ees.dao.impl.SubjectDaoImpl;
import com.erm.project.ees.model.recursive.Subject;
import com.erm.project.ees.stage.SubjectListStage;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class SubjectListController implements Initializable {

    @FXML
    private JFXTreeTableView<Subject> tblSubject;

    private final ObservableList<Subject> SUBJECT_LIST = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
    }

    @FXML
    protected void onClickAdd(ActionEvent event) {
        final int index = tblSubject.getSelectionModel().getSelectedIndex();
        if(index > -1) {
            SubjectListStage stage = (SubjectListStage) ((Node) event.getSource()).getScene().getWindow();
            final Subject subject = SUBJECT_LIST.get(index);
            stage.callback(new com.erm.project.ees.model.Subject(subject.getId(), subject.getName(), subject.getDesc(),
                    subject.getUnit()));
        }else
            Platform.runLater(()->new Thread(()-> JOptionPane.showMessageDialog(null,
                    "Select a subject")).start());
    }

    @FXML
    protected void onClickCancel(ActionEvent event) {
        SubjectListStage stage = (SubjectListStage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void initTable() {
        final SubjectDao subjectDao = new SubjectDaoImpl();

        for(com.erm.project.ees.model.Subject s : subjectDao.getSubjectList())
            SUBJECT_LIST.add(new Subject(s.getId(), s.getName(), s.getDesc(), s.getUnit()));

        TreeItem<com.erm.project.ees.model.recursive.Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST,
                RecursiveTreeObject::getChildren);

        Platform.runLater(() -> {
            JFXTreeTableColumn<Subject, Long> idCol = new JFXTreeTableColumn<>("ID");
            idCol.setResizable(false);
            idCol.setPrefWidth(80);
            idCol.setCellValueFactory(param -> param.getValue().getValue().idProperty().asObject());

            JFXTreeTableColumn<com.erm.project.ees.model.recursive.Subject, String> nameCol = new JFXTreeTableColumn<>("Subject");
            nameCol.setResizable(false);
            nameCol.setPrefWidth(130);
            nameCol.setCellValueFactory(param -> param.getValue().getValue().nameProperty());

            JFXTreeTableColumn<com.erm.project.ees.model.recursive.Subject, String> descCol = new JFXTreeTableColumn<>("Description");
            descCol.setResizable(false);
            descCol.setPrefWidth(210);
            descCol.setCellValueFactory(param -> param.getValue().getValue().descProperty());

            JFXTreeTableColumn<com.erm.project.ees.model.recursive.Subject, Integer> unitCol = new JFXTreeTableColumn<>("Unit");
            unitCol.setResizable(false);
            unitCol.setPrefWidth(80);
            unitCol.setCellValueFactory(param -> param.getValue().getValue().unitProperty().asObject());

            tblSubject.getColumns().add(idCol);
            tblSubject.getColumns().add(nameCol);
            tblSubject.getColumns().add(descCol);
            tblSubject.getColumns().add(unitCol);

            tblSubject.getSelectionModel().select(0);

            tblSubject.setRoot(root);
            tblSubject.setShowRoot(false);
        });
    }

}
