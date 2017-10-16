package io.erm.ees.controller;

import io.erm.ees.dao.SubjectDao;
import io.erm.ees.dao.impl.SubjectDaoImpl;
import io.erm.ees.model.recursive.Subject;
import io.erm.ees.stage.SubjectListStage;
import com.jfoenix.controls.JFXTextField;
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
import java.util.List;
import java.util.ResourceBundle;

public class SubjectListController implements Initializable {

    @FXML
    private JFXTreeTableView<Subject> tblSubject;

    private final ObservableList<Subject> SUBJECT_LIST = FXCollections.observableArrayList();

    private final SubjectDao subjectDao = new SubjectDaoImpl();

    @FXML
    private JFXTextField txSearch;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
    }

    @FXML
    protected void onClickSearch() {
        loadSubject(subjectDao.getSubjectListBySearch(txSearch.getText().trim()));
    }

    @FXML
    protected void onActionSearch() {
        loadSubject(subjectDao.getSubjectListBySearch(txSearch.getText().trim()));
    }

    @FXML
    protected void onPressedSearch() {
        loadSubject(subjectDao.getSubjectListBySearch(txSearch.getText().trim()));
    }

    @FXML
    protected void onReleasedSearch() {
        loadSubject(subjectDao.getSubjectListBySearch(txSearch.getText().trim()));
    }

    @FXML
    protected void onClickAdd(ActionEvent event) {
        final int index = tblSubject.getSelectionModel().getSelectedIndex();
        if (index > -1) {
            SubjectListStage stage = (SubjectListStage) ((Node) event.getSource()).getScene().getWindow();
            final Subject subject = SUBJECT_LIST.get(index);
            stage.callback(new io.erm.ees.model.Subject(subject.getId(), subject.getName(), subject.getDesc(),
                    subject.getUnit(), subject.getUnitLecture(), subject.getUnitLaboratory(), subject.getUnitDisplay()));
        } else
            new Thread(() -> JOptionPane.showMessageDialog(null,
                    "Select a subject")).start();
    }

    @FXML
    protected void onClickCancel(ActionEvent event) {
        SubjectListStage stage = (SubjectListStage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void initTable() {
        for (io.erm.ees.model.Subject s : subjectDao.getSubjectList())
            SUBJECT_LIST.add(new Subject(s.getId(), s.getName(), s.getDesc(), s.getUnit(), s.getUnitLecture(),
                    s.getUnitLaboratory(), s.getUnitDisplay()));

        TreeItem<Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST,
                RecursiveTreeObject::getChildren);

        Platform.runLater(() -> {
            JFXTreeTableColumn<Subject, Long> idCol = new JFXTreeTableColumn<>("ID");
            idCol.setResizable(false);
            idCol.setPrefWidth(100);
            idCol.setCellValueFactory(param -> param.getValue().getValue().idProperty().asObject());

            JFXTreeTableColumn<Subject, String> nameCol = new JFXTreeTableColumn<>("Subject");
            nameCol.setResizable(false);
            nameCol.setPrefWidth(130);
            nameCol.setCellValueFactory(param -> param.getValue().getValue().nameProperty());

            JFXTreeTableColumn<Subject, String> descCol = new JFXTreeTableColumn<>("Description");
            descCol.setResizable(false);
            descCol.setPrefWidth(250);
            descCol.setCellValueFactory(param -> param.getValue().getValue().descProperty());

            JFXTreeTableColumn<Subject, String> unitCol = new JFXTreeTableColumn<>("Unit");
            unitCol.setResizable(false);
            unitCol.setPrefWidth(100);
            unitCol.setCellValueFactory(param -> param.getValue().getValue().unitDisplayProperty());

            tblSubject.getColumns().add(idCol);
            tblSubject.getColumns().add(nameCol);
            tblSubject.getColumns().add(descCol);
            tblSubject.getColumns().add(unitCol);

            tblSubject.getSelectionModel().select(0);

            tblSubject.setRoot(root);
            tblSubject.setShowRoot(false);
        });
    }

    private void loadSubject(List<io.erm.ees.model.Subject> subjectList) {

        SUBJECT_LIST.clear();
        for (io.erm.ees.model.Subject s : subjectList)
            SUBJECT_LIST.add(new Subject(s.getId(), s.getName(), s.getDesc(), s.getUnit(), s.getUnitDisplay()));

        TreeItem<Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST,
                RecursiveTreeObject::getChildren);

        Platform.runLater(() -> {
            tblSubject.setRoot(root);
            tblSubject.setShowRoot(false);
        });

    }
}
