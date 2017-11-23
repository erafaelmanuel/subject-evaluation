package io.ermdev.ees.controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import io.ermdev.ees.dao.DirtyDao;
import io.ermdev.ees.dao.SubjectDao;
import io.ermdev.ees.dao.impl.DirtyDaoImpl;
import io.ermdev.ees.helper.DbFactory;
import io.ermdev.ees.stage.SubjectInputStage;
import io.ermdev.ees.stage.SubjectListStage;
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
    private JFXComboBox<String> cbUnitLab;

    @FXML
    private JFXTreeTableView<io.ermdev.ees.model.recursive.Subject> tblSubject;

    @FXML
    private Label lbNError;

    @FXML
    private Label lbDError;

    private final ObservableList<io.ermdev.ees.model.recursive.Subject> SUBJECT_LIST = FXCollections.observableArrayList();
    private final io.ermdev.ees.model.Subject SUBJECT = new io.ermdev.ees.model.Subject();

    private final DirtyDao dirtyDao = new DirtyDaoImpl();
    private final SubjectDao subjectDao = DbFactory.subjectFactory();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbUnit.getItems().add("1 UNIT");
        cbUnit.getItems().add("2 UNIT");
        cbUnit.getItems().add("3 UNIT");
        cbUnit.getItems().add("4 UNIT");
        cbUnit.getItems().add("5 UNIT");
        cbUnit.getItems().add("6 UNIT");
        cbUnit.getItems().add("7 UNIT");
        cbUnit.getItems().add("8 UNIT");
        cbUnit.getItems().add("9 UNIT");
        cbUnit.getItems().add("10 UNIT");


        cbUnitLab.getItems().add("0 UNIT");
        cbUnitLab.getItems().add("1 UNIT");
        cbUnitLab.getItems().add("2 UNIT");
        cbUnitLab.getItems().add("3 UNIT");
        cbUnitLab.getItems().add("4 UNIT");
        cbUnitLab.getItems().add("5 UNIT");
        cbUnitLab.getItems().add("6 UNIT");
        cbUnitLab.getItems().add("7 UNIT");
        cbUnitLab.getItems().add("8 UNIT");
        cbUnitLab.getItems().add("9 UNIT");
        cbUnitLab.getItems().add("10 UNIT");

        cbUnit.getSelectionModel().select(0);
        cbUnitLab.getSelectionModel().select(0);
        txName.setText("");
        txDesc.setText("");

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
            io.ermdev.ees.model.recursive.Subject subject = SUBJECT_LIST.get(index);
            SUBJECT_LIST.remove(subject);

            TreeItem<io.ermdev.ees.model.recursive.Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST,
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
            SUBJECT.setUnitLecture(cbUnit.getSelectionModel().getSelectedIndex() + 1);
            SUBJECT.setUnitLaboratory(cbUnitLab.getSelectionModel().getSelectedIndex());

            if (subjectDao.getSubjectById(SUBJECT.getId()) == null) {
                if(subjectDao.isSubjectNameExist(SUBJECT.getName().trim())) {
                    new Thread(()->JOptionPane.showMessageDialog(null, "Subject already exist"))
                            .start();
                    return;
                }
                SUBJECT.setId(subjectDao.addSubject(SUBJECT).getId());
            } else {
                subjectDao.updateSubjectById(SUBJECT.getId(), SUBJECT);
                dirtyDao.deletePrerequisite(SUBJECT.getId());
            }

            for (io.ermdev.ees.model.recursive.Subject s : SUBJECT_LIST) {
                if (SUBJECT.getId() == s.getId())
                    continue;
                dirtyDao.addPrerequisite(SUBJECT.getId(), s.getId());
            }
            SubjectInputStage stage = (SubjectInputStage) ((Node) event.getSource()).getScene().getWindow();
            stage.callBack();
            SUBJECT.setId(0);

            dispose();
        }
    }

    @FXML
    protected void onClickCancel(ActionEvent event) {
        dispose();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void initTable() {

        TreeItem<io.ermdev.ees.model.recursive.Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST,
                RecursiveTreeObject::getChildren);

        Platform.runLater(() -> {
            JFXTreeTableColumn<io.ermdev.ees.model.recursive.Subject, Long> idCol = new JFXTreeTableColumn<>("ID");
            idCol.setResizable(false);
            idCol.setPrefWidth(80);
            idCol.setCellValueFactory(param -> param.getValue().getValue().idProperty().asObject());
            idCol.setSortable(false);

            JFXTreeTableColumn<io.ermdev.ees.model.recursive.Subject, String> nameCol = new JFXTreeTableColumn<>("Subject");
            nameCol.setResizable(false);
            nameCol.setPrefWidth(130);
            nameCol.setCellValueFactory(param -> param.getValue().getValue().nameProperty());
            nameCol.setSortable(false);

            JFXTreeTableColumn<io.ermdev.ees.model.recursive.Subject, String> descCol = new JFXTreeTableColumn<>("Description");
            descCol.setResizable(false);
            descCol.setPrefWidth(210);
            descCol.setCellValueFactory(param -> param.getValue().getValue().descProperty());
            descCol.setSortable(false);

            JFXTreeTableColumn<io.ermdev.ees.model.recursive.Subject, Integer> unitCol = new JFXTreeTableColumn<>("Unit");
            unitCol.setResizable(false);
            unitCol.setPrefWidth(80);
            unitCol.setCellValueFactory(param -> param.getValue().getValue().unitProperty().asObject());
            unitCol.setSortable(false);

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
    public void onAdd(io.ermdev.ees.model.Subject item) {
        if (subjectDao.getSubjectById(SUBJECT.getId()) != null) {
            if (SUBJECT.getId() == item.getId()) {

                new Thread(() ->
                        JOptionPane.showMessageDialog(null,
                                "You can't add subject to itself"))
                        .start();
                return;
            }
        }

        for (io.ermdev.ees.model.recursive.Subject subject : SUBJECT_LIST) {
            if (subject.getId() == item.getId()) {
                new Thread(() ->
                        JOptionPane.showMessageDialog(null,
                                "You can't add subject twice"))
                        .start();
                return;
            }
        }

        SUBJECT_LIST.add(new io.ermdev.ees.model.recursive.Subject(item.getId(), item.getName(), item.getDesc(), item.getUnit()));

        TreeItem<io.ermdev.ees.model.recursive.Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST,
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

    public void listen(io.ermdev.ees.model.Subject subject) {

        SUBJECT.setId(subject.getId());
        SUBJECT.setName(subject.getName());
        SUBJECT.setDesc(subject.getDesc());
        SUBJECT.setUnit(subject.getUnit());
        SUBJECT.setUnitLecture(subject.getUnitLecture());
        SUBJECT.setUnitLaboratory(subject.getUnitLaboratory());

        txName.setText(SUBJECT.getName());
        txDesc.setText(SUBJECT.getDesc());

        cbUnit.getSelectionModel().select((SUBJECT.getUnitLecture()>0 ?SUBJECT.getUnitLecture()-1:0));
        cbUnitLab.getSelectionModel().select(SUBJECT.getUnitLaboratory());

        SUBJECT_LIST.clear();
        for (io.ermdev.ees.model.Subject item : new DirtyDaoImpl().getPrerequisiteBySujectId(SUBJECT.getId())) {
            SUBJECT_LIST.add(new io.ermdev.ees.model.recursive.Subject(item.getId(), item.getName(), item.getDesc(), item.getUnit()));
        }

        TreeItem<io.ermdev.ees.model.recursive.Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST, RecursiveTreeObject::getChildren);

        Platform.runLater(() -> {
            tblSubject.setRoot(root);
            tblSubject.setShowRoot(false);
        });
    }

    public void dispose() {
        cbUnit.getSelectionModel().select(0);
        cbUnitLab.getSelectionModel().select(0);
        txName.setText("");
        txDesc.setText("");
    }
}
