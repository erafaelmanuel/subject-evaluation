package io.erm.ees.controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import io.erm.ees.dao.CourseDao;
import io.erm.ees.dao.DirtyDao;
import io.erm.ees.dao.SubjectDao;
import io.erm.ees.dao.impl.AcademicYearDaoImpl;
import io.erm.ees.dao.impl.CourseDaoImpl;
import io.erm.ees.dao.impl.DirtyDaoImpl;
import io.erm.ees.dao.impl.SubjectDaoImpl;
import io.erm.ees.model.Course;
import io.erm.ees.model.recursive.Subject;
import io.erm.ees.model.v2.AcademicYear;
import io.erm.ees.stage.SubjectInputStage;
import io.erm.ees.stage.SubjectListStage;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.util.*;

public class AcademicYearInputController implements Initializable {

    @FXML
    private JFXTextField txName;

    @FXML
    private JFXTextField txSemester;

    @FXML
    private JFXComboBox<String> cbCourse;

    @FXML
    private CheckBox chbAll;

    @FXML
    private CheckBox chbFirst;

    @FXML
    private CheckBox chbSecond;

    @FXML
    private CheckBox chbThird;

    @FXML
    private CheckBox chbForth;

    @FXML
    private CheckBox chbFifth;

    @FXML
    private Label lbStatus;

    @FXML
    private HBox hbMessage;


    private final ObservableList<String> COURSE_ITEM = FXCollections.observableArrayList();
    private final List<Course> COURSE_LIST = new ArrayList<>();

    final CourseDao courseDao = new CourseDaoImpl();

    private Calendar calendar = Calendar.getInstance();
    private int year = calendar.get(Calendar.YEAR);
    private long code = Long.parseLong(String.format(Locale.ENGLISH, "%d%d", year, year+1));
    private String name = String.format(Locale.ENGLISH, "%d-%d", year, year+1);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chbAll.setVisible(false);
        chbFirst.setVisible(false);
        chbSecond.setVisible(false);
        chbThird.setVisible(false);
        chbForth.setVisible(false);
        chbFifth.setVisible(false);

        COURSE_ITEM.clear();
        for(Course course : courseDao.getCourseList()) {
            COURSE_ITEM.add(course.getName());
            COURSE_LIST.add(course);
        }
        if(COURSE_ITEM.size() > 0) {
            cbCourse.setItems(COURSE_ITEM);
            cbCourse.getSelectionModel().select(0);
            final int index = cbCourse.getSelectionModel().getSelectedIndex();
            if(index > -1) {
                setCheckBoxes(index);
                txName.setText(name);
                txSemester.setText(COURSE_LIST.get(index).getTotalSemester() + " Semester");

                if(new AcademicYearDaoImpl().isAcademicYearIsExist(code, COURSE_LIST.get(index).getId())) {
                    lbStatus.setText("You've already created the academic year for course " + COURSE_LIST.get(index).getName());
                    hbMessage.setStyle("-fx-background-color:#1abc9c;");
                } else {
                    lbStatus.setText("You've not create an academic year for this course yet");
                    hbMessage.setStyle("-fx-background-color:#95a5a6;");
                }
            }
        }

        chbAll.selectedProperty().addListener((o, _o, _n)->{
            if(_n){
                final int index = cbCourse.getSelectionModel().getSelectedIndex();
                if(index > -1) {
                    setCheckBoxes(index);
                }
            }
        });


        ChangeListener<Boolean> listener = (o, _o, _n) -> chbAll.setSelected(false);
        chbFirst.selectedProperty().addListener(listener);
        chbSecond.selectedProperty().addListener(listener);
        chbThird.selectedProperty().addListener(listener);
        chbForth.selectedProperty().addListener(listener);
        chbFifth.selectedProperty().addListener(listener);
    }

    @FXML
    protected void onClickSave(ActionEvent event) {
        final int index = cbCourse.getSelectionModel().getSelectedIndex();
        if(index > -1) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            long code = Long.parseLong(String.format(Locale.ENGLISH, "%d%d", year, year+1));
            String name = String.format(Locale.ENGLISH, "%d-%d", year, year+1);
            if (chbAll.isSelected()) {
                for (int y = 1; y<=COURSE_LIST.get(index).getTotalYear(); y++) {
                    for (int s = 1; s<=COURSE_LIST.get(index).getTotalSemester(); s++) {
                        AcademicYear academicYear = new AcademicYear(code, name, s, y, false, 0);
                        new AcademicYearDaoImpl().addAcademicYear(COURSE_LIST.get(index).getId(), academicYear);
                    }
                }
            }

            new AcademicYearDaoImpl().statusOpen(code, 1);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }

    }

    @FXML
    protected void onClickCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onChooseCourse() {
        final int index = cbCourse.getSelectionModel().getSelectedIndex();
        if(index > -1) {
            setCheckBoxes(index);

            if(new AcademicYearDaoImpl().isAcademicYearIsExist(code, COURSE_LIST.get(index).getId())) {
                lbStatus.setText("You've already created the academic year for course " + COURSE_LIST.get(index).getName());
                hbMessage.setStyle("-fx-background-color:#1abc9c;");
            } else {
                lbStatus.setText("You've not created the academic year for this course yet");
                lbStatus.setText("You've not create an academic year for this course yet");
            }
        }
    }

    @FXML
    protected void onClickAll() {
        if(!chbAll.isSelected()) {
            chbFirst.setSelected(false);
            chbSecond.setSelected(false);
            chbThird.setSelected(false);
            chbForth.setSelected(false);
            chbFifth.setSelected(false);
        }

    }

    private void setCheckBoxes(int index) {
        switch (COURSE_LIST.get(index).getTotalYear()) {
            case 1:
                chbFirst.setVisible(true);
                chbSecond.setVisible(false);
                chbThird.setVisible(false);
                chbForth.setVisible(false);
                chbFifth.setVisible(false);

                chbFirst.setSelected(true);
                chbSecond.setSelected(false);
                chbThird.setSelected(false);
                chbForth.setSelected(false);
                chbFifth.setSelected(false);
                break;
            case 2:
                chbFirst.setVisible(true);
                chbSecond.setVisible(true);
                chbThird.setVisible(false);
                chbForth.setVisible(false);
                chbFifth.setVisible(false);

                chbFirst.setSelected(true);
                chbSecond.setSelected(true);
                chbThird.setSelected(false);
                chbForth.setSelected(false);
                chbFifth.setSelected(false);
                break;
            case 3:
                chbFirst.setVisible(true);
                chbSecond.setVisible(true);
                chbThird.setVisible(true);
                chbForth.setVisible(false);
                chbFifth.setVisible(false);

                chbFirst.setSelected(true);
                chbSecond.setSelected(true);
                chbThird.setSelected(true);
                chbForth.setSelected(false);
                chbFifth.setSelected(false);
                break;
            case 4:
                chbFirst.setVisible(true);
                chbSecond.setVisible(true);
                chbThird.setVisible(true);
                chbForth.setVisible(true);
                chbFifth.setVisible(false);

                chbFirst.setSelected(true);
                chbSecond.setSelected(true);
                chbThird.setSelected(true);
                chbForth.setSelected(true);
                chbFifth.setSelected(false);
                break;
            case 5:
                chbFirst.setVisible(true);
                chbSecond.setVisible(true);
                chbThird.setVisible(true);
                chbForth.setVisible(true);
                chbFifth.setVisible(true);

                chbFirst.setSelected(true);
                chbSecond.setSelected(true);
                chbThird.setSelected(true);
                chbForth.setSelected(true);
                chbFifth.setSelected(true);
                break;
        }

        chbAll.setVisible(true);
        chbAll.setSelected(true);
    }
}
