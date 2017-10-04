package com.erm.project.ees.controller;

import com.erm.project.ees.dao.DirtyDao;
import com.erm.project.ees.dao.SubjectDao;
import com.erm.project.ees.dao.impl.DirtyDaoImpl;
import com.erm.project.ees.dao.impl.SubjectDaoImpl;
import com.erm.project.ees.model.recursive.Subject;
import com.erm.project.ees.stage.SubjectInputStage;
import com.erm.project.ees.stage.SubjectListStage;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class SubjectInputController implements Initializable, SubjectListStage.OnAddItemListener {

    @FXML
    private JFXTextField txName;

    @FXML
    private JFXTextField txDesc;

    @FXML
    private JFXComboBox<String> cbUnit;

    @FXML
    private JFXTreeTableView<Subject> tblSubject;

    @FXML
    private Label lbNError;

    @FXML
    private Label lbDError;

    private final ObservableList<Subject> SUBJECT_LIST = FXCollections.observableArrayList();
    private final com.erm.project.ees.model.Subject SUBJECT = new com.erm.project.ees.model.Subject();

    final DirtyDao dirtyDao = new DirtyDaoImpl();
    final SubjectDao subjectDao = new SubjectDaoImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbUnit.getItems().add("1 UNIT");
        cbUnit.getItems().add("2 UNIT");
        cbUnit.getItems().add("3 UNIT");
        cbUnit.getItems().add("4 UNIT");
        cbUnit.getItems().add("5 UNIT");

        cbUnit.getSelectionModel().select(0);

        initTable();
    }

    @FXML
    protected void onClickAdd() {
        new Thread(() -> {
            Platform.runLater(() -> {
                SubjectListStage subjectListStage = new SubjectListStage();
                subjectListStage.setListener(this);
                subjectListStage.showAndWait();
            });
        }).start();
    }

    @FXML
    protected void onClickRemove() {
        final int index = tblSubject.getSelectionModel().getSelectedIndex();
        if (index > -1) {
            Subject subject = SUBJECT_LIST.get(index);
            SUBJECT_LIST.remove(subject);

            TreeItem<Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST,
                    RecursiveTreeObject::getChildren);

            Platform.runLater(() -> {
                tblSubject.setRoot(root);
                tblSubject.setShowRoot(false);
            });
        }
    }

    @FXML
    protected void onClickSave(ActionEvent event) {
        if (isValid()) {
            SUBJECT.setName(txName.getText().trim());
            SUBJECT.setDesc(txDesc.getText().trim());
            SUBJECT.setUnit(cbUnit.getSelectionModel().getSelectedIndex() + 1);

            if (subjectDao.getSubjectById(SUBJECT.getId()) == null)
                SUBJECT.setId(subjectDao.addSubject(SUBJECT).getId());
            else {
                subjectDao.updateSubjectById(SUBJECT.getId(), SUBJECT);
                dirtyDao.deletePrerequisite(SUBJECT.getId());
            }

            for (Subject s : SUBJECT_LIST) {
                if (SUBJECT.getId() == s.getId())
                    continue;
                dirtyDao.addPrerequisite(SUBJECT.getId(), s.getId());
            }
            SubjectInputStage stage = (SubjectInputStage) ((Node) event.getSource()).getScene().getWindow();
            stage.callBack();
            SUBJECT.setId(0);
        }
    }

    @FXML
    protected void onClickCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void initTable() {

        TreeItem<Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST,
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

    @Override
    public void onAdd(com.erm.project.ees.model.Subject item) {
        if (subjectDao.getSubjectById(SUBJECT.getId()) != null) {
            if (SUBJECT.getId() == item.getId()) {

                new Thread(() ->
                        JOptionPane.showMessageDialog(null,
                                "You can't add subject to itself"))
                        .start();
                return;
            }
        }

        for (Subject subject : SUBJECT_LIST) {
            if (subject.getId() == item.getId()) {
                new Thread(() ->
                        JOptionPane.showMessageDialog(null,
                                "You can't add subject twice"))
                        .start();
                return;
            }
        }

        SUBJECT_LIST.add(new Subject(item.getId(), item.getName(), item.getDesc(), item.getUnit()));

        TreeItem<Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST,
                RecursiveTreeObject::getChildren);

        Platform.runLater(() -> {
            tblSubject.setRoot(root);
            tblSubject.setShowRoot(false);
        });
    }

    protected boolean isValid() {
        boolean isValid = true;
        if (txName.getText().trim().isEmpty()) {
            isValid = false;
            lbNError.setVisible(true);
        } else {
            lbNError.setVisible(false);
        }

        if (txDesc.getText().trim().isEmpty()) {
            isValid = false;
            lbDError.setVisible(true);
        } else {
            lbDError.setVisible(false);
        }
        return isValid;
    }

    public void listen(com.erm.project.ees.model.Subject subject) {
        SUBJECT.setId(subject.getId());
        SUBJECT.setName(subject.getName());
        SUBJECT.setDesc(subject.getDesc());
        SUBJECT.setUnit(subject.getUnit());

        txName.setText(SUBJECT.getName());
        txDesc.setText(SUBJECT.getDesc());
        cbUnit.getSelectionModel().select(SUBJECT.getUnit() - 1);

        SUBJECT_LIST.clear();
        for (com.erm.project.ees.model.Subject item : new DirtyDaoImpl().getPrerequisiteBySujectId(SUBJECT.getId())) {
            SUBJECT_LIST.add(new Subject(item.getId(), item.getName(), item.getDesc(), item.getUnit()));
        }

        TreeItem<Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST, RecursiveTreeObject::getChildren);

        Platform.runLater(() -> {
            tblSubject.setRoot(root);
            tblSubject.setShowRoot(false);
        });

    }
}
