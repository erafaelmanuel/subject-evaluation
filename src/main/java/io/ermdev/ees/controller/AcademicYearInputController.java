package io.ermdev.ees.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import io.ermdev.ees.dao.AcademicYearDao;
import io.ermdev.ees.dao.CourseDao;
import io.ermdev.ees.helper.DbFactory;
import io.ermdev.ees.model.Course;
import io.ermdev.ees.model.v2.AcademicYear;
import io.ermdev.ees.stage.AcademicYearInputStage;
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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

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

    @FXML
    private JFXButton bnSave;

    @FXML
    private JFXTextField txYear;

    @FXML
    private CheckBox chbCurrentYear;

    @FXML CheckBox chbFirstSem;

    @FXML CheckBox chbSecondSem;

    @FXML CheckBox chbThirdSem;

    private final ObservableList<String> COURSE_ITEM = FXCollections.observableArrayList();
    private final List<Course> COURSE_LIST = new ArrayList<>();
    private boolean[] SEM_LIST;

    private final CourseDao courseDao = DbFactory.courseFactory();
    private final AcademicYearDao academicYearDao = DbFactory.academicYearFactory();

    private Calendar calendar = Calendar.getInstance();
    private int year = calendar.get(Calendar.YEAR);
    private long code = Long.parseLong(String.format(Locale.ENGLISH, "%d%d", year, year+1));
    private String name = String.format(Locale.ENGLISH, "%d-%d", year, year+1);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
            if (chbAll.isSelected()) {
                for (int y = 1; y<=COURSE_LIST.get(index).getTotalYear(); y++) {
                    for (int s = 1; s<=COURSE_LIST.get(index).getTotalSemester(); s++) {
                        if(SEM_LIST[s-1])
                            continue;
                        AcademicYear academicYear = new AcademicYear(code, name, s, y, false, 0);
                        academicYearDao.addAcademicYear(COURSE_LIST.get(index).getId(), academicYear);
                    }
                }
            }

            //new AcademicYearDaoImpl().statusOpen(code, 1);
            AcademicYearInputStage stage = (AcademicYearInputStage) ((Node) event.getSource()).getScene().getWindow();
            stage.callBack();
        }

    }

    @FXML
    protected void onReleasedYear() {
        if(txYear.getText().trim().matches("^[0-9]+$")) {
            try {
                if (Long.parseLong(txYear.getText().trim()) > 999999) {
                    txYear.setStyle("-fx-text-fill:#c0392b");
                    txName.setText("--");
                    return;
                }
            } catch (NumberFormatException e) {
                txYear.setStyle("-fx-text-fill:#c0392b");
                txName.setText("--");
                return;
            }

            txYear.setStyle("-fx-text-fill:#000000");

            if(Integer.parseInt(txYear.getText().trim()) < 1) {
                txYear.setText("1");
                txYear.end();
            }

            bnSave.setDisable(false);
            year = Integer.parseInt(txYear.getText().trim());
            code = Long.parseLong(String.format(Locale.ENGLISH, "%d%d", year, year+1));
            name = String.format(Locale.ENGLISH, "%d-%d", year, year+1);

            txName.setText(name);

            setup();
        } else {
            txYear.setStyle("-fx-text-fill:#c0392b");
            txName.setText("--");
            bnSave.setDisable(true);
        }
    }

    @FXML
    protected void onClickCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onChooseCurrentYear() {
        if(chbCurrentYear.isSelected()) {
            bnSave.setDisable(false);
            txYear.setVisible(false);
            txYear.setText("");

            year = calendar.get(Calendar.YEAR);
            code = Long.parseLong(String.format(Locale.ENGLISH, "%d%d", year, year+1));
            name = String.format(Locale.ENGLISH, "%d-%d", year, year+1);

            txName.setText(name);

            setup();
        } else {
            txYear.setVisible(true);
            txName.setText("--");
            bnSave.setDisable(true);
        }
    }

    @FXML
    protected void onChooseCourse() {
        setup();
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

    public void listener() {
        Platform.runLater(()->{
            chbCurrentYear.setSelected(true);
            txYear.setVisible(false);
            txYear.setText("");

            chbAll.setVisible(false);
            chbFirst.setVisible(false);
            chbSecond.setVisible(false);
            chbThird.setVisible(false);
            chbForth.setVisible(false);
            chbFifth.setVisible(false);

            chbFirstSem.setDisable(true);
            chbSecondSem.setDisable(true);
            chbThirdSem.setDisable(true);

            chbFirstSem.setSelected(true);
            chbSecondSem.setSelected(true);
            chbThirdSem.setSelected(true);

            chbFirstSem.setIndeterminate(false);
            chbSecondSem.setIndeterminate(false);
            chbThirdSem.setIndeterminate(false);

            chbThirdSem.setVisible(false);

            txName.setText(name);
        });

        COURSE_ITEM.clear();
        for(Course course : courseDao.getCourseList()) {
            COURSE_ITEM.add(course.getName());
            COURSE_LIST.add(course);
        }
        if(COURSE_ITEM.size() > 0) {
            Platform.runLater(()-> {
                cbCourse.setItems(COURSE_ITEM);
                cbCourse.getSelectionModel().select(0);
            });
        }
    }

    public void setup() {
        final int index = cbCourse.getSelectionModel().getSelectedIndex();
        if(index > -1) {
            final int semester = COURSE_LIST.get(index).getTotalSemester();
            setCheckBoxes(index);

            chbFirstSem.setSelected(true);
            chbSecondSem.setSelected(true);
            chbThirdSem.setSelected(true);

            chbFirstSem.setIndeterminate(false);
            chbSecondSem.setIndeterminate(false);
            chbThirdSem.setIndeterminate(false);

            lbStatus.setText("You've already created the academic year on this course");
            hbMessage.setStyle("-fx-background-color:#1abc9c;");
            bnSave.setDisable(true);

            SEM_LIST = new boolean[semester];
            for(int i=1; i<= semester; i++) {
                SEM_LIST[i-1]=true;
                if (!academicYearDao.isAcademicYearIsExist(code, COURSE_LIST.get(index).getId(), i)) {
                    lbStatus.setText("You can still add an academic year on this course");
                    hbMessage.setStyle("-fx-background-color:#95a5a6;");
                    bnSave.setDisable(false);
                    SEM_LIST[i-1]=false;
                }
            }

            if(SEM_LIST[0]) {
                chbFirstSem.setIndeterminate(true);
                chbFirstSem.setSelected(false);
            }
            if(SEM_LIST[1]) {
                chbSecondSem.setIndeterminate(true);
                chbSecondSem.setSelected(false);
            }
            if(semester==3) {
                if (SEM_LIST[2]) {
                    chbThirdSem.setIndeterminate(true);
                    chbThirdSem.setSelected(false);
                }
            }
        }
    }
}
