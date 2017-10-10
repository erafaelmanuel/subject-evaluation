package io.erm.ees.controller;

import io.erm.ees.dao.DirtyDao;
import io.erm.ees.dao.impl.DirtyDaoImpl;
import io.erm.ees.model.recursive.Mark;
import io.erm.ees.stage.GradeInputStage;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class GradeInputController implements Initializable {


    @FXML
    private JFXTextField txMidterm;

    @FXML
    private JFXTextField txFinalterm;

    @FXML
    private JFXRadioButton rbPassed;

    @FXML
    private JFXRadioButton rbFailed;

    @FXML
    private JFXRadioButton rbIncomplete;

    @FXML
    private Label lbMError;

    @FXML
    private Label lbFError;

    private final DirtyDao dirtyDao = new DirtyDaoImpl();

    private Mark mark;

    private long studentId;

    private String status = "INCOMPLETE";

    private boolean isValid = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rbPassed.setDisable(true);
        rbFailed.setDisable(true);
        rbIncomplete.setDisable(true);

        txMidterm.focusedProperty().addListener((obj, oldPropertu, newProperty) -> {
            if (newProperty) {
                txMidterm.setText("");

                rbPassed.setDisable(false);
                rbFailed.setDisable(false);
                rbIncomplete.setDisable(false);
            } else {
                rbPassed.setDisable(true);
                rbFailed.setDisable(true);
                rbIncomplete.setDisable(true);
            }
        });

        txFinalterm.focusedProperty().addListener((obj, oldPropertu, newProperty) -> {
            if (newProperty) {
                txFinalterm.setText("");
                rbPassed.setDisable(false);
                rbFailed.setDisable(false);
                rbIncomplete.setDisable(false);
            } else {
                rbPassed.setDisable(true);
                rbFailed.setDisable(true);
                rbIncomplete.setDisable(true);
            }
        });
    }

    public void listening(Mark mark, long studentId) {
        this.mark = mark;
        this.studentId = studentId;

        txMidterm.setText(mark.getMidterm() + "");
        txFinalterm.setText(mark.getFinalterm() + "");
    }

    @FXML
    protected void onClickCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onClickSave(ActionEvent event) {
        if (!txMidterm.getText().trim().matches("^[0-9]+([.][0-9])?$"))
            isValid = false;
        if (!txFinalterm.getText().trim().matches("^[0-9]+([.][0-9])?$")) {
            isValid = false;
        }
        if (txFinalterm.getText().trim().matches("^[0-9]+([.][0-9])?$")) {
            final double grade = Double.parseDouble(txFinalterm.getText().trim());
            if (!(grade <= 5 && grade >= 0))
                isValid = false;
        }

        if (isValid) {
            GradeInputStage stage = (GradeInputStage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

            final double midterm = Double.parseDouble(txMidterm.getText().trim());
            final double finalterm = Double.parseDouble(txFinalterm.getText().trim());
            dirtyDao.updateStudentRecord(mark.getSubjectId(), studentId, midterm, finalterm, status);

            stage.callBack();
        } else {
            isValid = true;
            new Thread(() ->
                    JOptionPane.showMessageDialog(null, "Please enter a valid grade"))
                    .start();
        }
    }

    @FXML
    private void onKeyReleased() {
        if (txMidterm.getText().trim().matches("^[0-9]+([.][0-9])?$")) {
            lbMError.setVisible(false);
        } else {
            lbMError.setVisible(true);
        }
        if (txFinalterm.getText().trim().matches("^[0-9]+([.][0-9])?$")) {
            lbFError.setVisible(false);
            final double grade = Double.parseDouble(txFinalterm.getText().trim());

            if (grade <= 5 && grade >= 0) {
                status = (grade >= 1 && grade <= 3 ? "PASSED" : grade > 3 && grade <= 5 ? "FAILED" : "INCOMPLETE");
                selectStatus(status);
            } else {
                lbFError.setVisible(true);
            }
        } else {
            lbFError.setVisible(true);
        }
    }

    @FXML
    protected void onSelectMark(ActionEvent event) {
        String id = ((Node) event.getSource()).getId();
        switch (id) {
            case "rbPassed":
                rbFailed.setSelected(false);
                rbIncomplete.setSelected(false);
                status = "PASSED";
                break;
            case "rbFailed":
                rbPassed.setSelected(false);
                rbIncomplete.setSelected(false);
                status = "FAILED";
                break;
            case "rbIncomplete":
                rbFailed.setSelected(false);
                rbPassed.setSelected(false);
                status = "INCOMPLETE";
                break;
        }
    }

    public void selectStatus(String status) {
        switch (status) {
            case "PASSED":
                rbPassed.setSelected(true);
                rbFailed.setSelected(false);
                rbIncomplete.setSelected(false);
                break;
            case "FAILED":
                rbPassed.setSelected(false);
                rbFailed.setSelected(true);
                rbIncomplete.setSelected(false);
                break;
            case "INCOMPLETE":
                rbFailed.setSelected(false);
                rbPassed.setSelected(false);
                rbIncomplete.setSelected(true);
                break;
        }
    }

}
