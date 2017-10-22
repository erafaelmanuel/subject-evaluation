package io.erm.ees.controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import io.erm.ees.dao.CourseDao;
import io.erm.ees.dao.CurriculumDao;
import io.erm.ees.dao.SpecialCurriculumDao;
import io.erm.ees.dao.impl.CurriculumDaoImpl;
import io.erm.ees.dao.impl.SpecialCurriculumDaoImpl;
import io.erm.ees.helper.DbFactory;
import io.erm.ees.model.Course;
import io.erm.ees.model.Curriculum;
import io.erm.ees.model.SpecialCurriculum;
import io.erm.ees.model.recursive.Subject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SpecialCurriculumController implements Initializable {


    @FXML
    private JFXComboBox<String> cbCourse;

    @FXML
    private JFXComboBox<String> cbType;

    @FXML
    private JFXComboBox<String> cbYS;

    @FXML
    private JFXTreeTableView<Subject> tblFrom;

    @FXML
    private JFXTreeTableView<Subject> tblTo;

    @FXML
    private JFXTextField txName;

    private final CourseDao courseDao = DbFactory.courseFactory();
    private final CurriculumDao curriculumDao = new CurriculumDaoImpl();
    private final SpecialCurriculumDao specialCurriculumDao = new SpecialCurriculumDaoImpl();

    private final List<Course> COURSE_LIST = new ArrayList<>();

    private final ObservableList<Subject> SUBJECT_LIST = FXCollections.observableArrayList();
    private final ObservableList<Subject> SUBJECT_LIST_TO = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        for (Course course : courseDao.getCourseList()) {
            cbCourse.getItems().add(course.getName());
            COURSE_LIST.add(course);
        }

        initTable();
        initTable2();

        if(cbCourse.getItems().size() > 0 && COURSE_LIST.size() > 0) {
            cbCourse.getSelectionModel().select(0);
            cbType.getItems().add("SATURDAY_CLASS");
            cbType.getItems().add("SUMMER_CLASS");
            cbType.getSelectionModel().select(0);

            final int index = cbCourse.getSelectionModel().getSelectedIndex();
            cbYS.setItems(FXCollections.observableArrayList());
            for (int year = 1; year <= COURSE_LIST.get(index).getTotalYear(); year++) {
                for (int sem = 1; sem <= COURSE_LIST.get(index).getTotalSemester(); sem++) {
                    cbYS.getItems().add(format(year) + " YEAR / " + format(sem) + " SEMESTER");
                }
            }
            cbYS.getSelectionModel().select(0);

            SUBJECT_LIST.clear();
            Curriculum c = getCurriculumByYearAndSem(COURSE_LIST.get(index).getId(), 0);
            for (io.erm.ees.model.Subject subject : curriculumDao.getSubjectList(c.getId())) {
                SUBJECT_LIST.add(new Subject(subject.getId(), subject.getName(), subject.getDesc(), subject.getUnit()));
            }
            TreeItem<Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST, RecursiveTreeObject::getChildren);
            tblFrom.setRoot(root);
            tblFrom.setShowRoot(false);
        }
    }

    @FXML
    private void onChooseCourse() {
        final int index = cbCourse.getSelectionModel().getSelectedIndex();
        cbYS.setItems(FXCollections.observableArrayList());
        for (int year = 1; year <= COURSE_LIST.get(index).getTotalYear(); year++) {
            for (int sem = 1; sem <= COURSE_LIST.get(index).getTotalSemester(); sem++) {
                cbYS.getItems().add(format(year) + " YEAR / " + format(sem) + " SEMESTER");
            }
        }
        cbYS.getSelectionModel().select(0);

        SUBJECT_LIST.clear();
        Curriculum c = getCurriculumByYearAndSem(COURSE_LIST.get(index).getId(), 0);
        for (io.erm.ees.model.Subject subject : curriculumDao.getSubjectList(c.getId())) {
            SUBJECT_LIST.add(new Subject(subject.getId(), subject.getName(), subject.getDesc(), subject.getUnit()));
        }
        TreeItem<Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST, RecursiveTreeObject::getChildren);
        tblFrom.setRoot(root);
        tblFrom.setShowRoot(false);
    }

    @FXML
    protected void onClickCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

    }

    @FXML
    protected void onClickSave(ActionEvent event) {
        if (SUBJECT_LIST_TO.size() > 0) {
            final int index = cbYS.getSelectionModel().getSelectedIndex();
            SpecialCurriculum curriculum = new SpecialCurriculum();
            curriculum.setYear(getYearByIndex(index));
            curriculum.setSemester(index);
            curriculum.setCourseId(COURSE_LIST.get(cbCourse.getSelectionModel().getSelectedIndex()).getId());
            curriculum.setType(cbType.getSelectionModel().getSelectedItem());
            curriculum.setName(txName.getText());
            specialCurriculumDao.addCurriculum(curriculum);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void onChooseYS() {
        final int index = cbCourse.getSelectionModel().getSelectedIndex();
        SUBJECT_LIST.clear();
        SUBJECT_LIST_TO.clear();
        Curriculum c = getCurriculumByYearAndSem(COURSE_LIST.get(index).getId(), cbYS.getSelectionModel().getSelectedIndex());
        for (io.erm.ees.model.Subject subject : curriculumDao.getSubjectList(c.getId())) {
            SUBJECT_LIST.add(new Subject(subject.getId(), subject.getName(), subject.getDesc(), subject.getUnit()));
        }
        TreeItem<Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST, RecursiveTreeObject::getChildren);
        tblFrom.setRoot(root);
        tblFrom.setShowRoot(false);

        tblTo.setRoot(null);
        tblTo.setShowRoot(false);

    }

    @FXML
    private void onClickAdd() {
        final int index = tblFrom.getSelectionModel().getSelectedIndex();
        if (index > -1) {
            Subject subject = SUBJECT_LIST.get(index);
            for (Subject s : SUBJECT_LIST_TO) {
                if (s.getId() == subject.getId()) {
                    new Thread(() -> JOptionPane.showMessageDialog(null, "Already exist")).start();
                    return;
                }
            }
            SUBJECT_LIST_TO.add(subject);
            TreeItem<Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST_TO, RecursiveTreeObject::getChildren);
            tblTo.setRoot(root);
            tblTo.setShowRoot(false);
        }
    }

    @FXML
    private void onClickRemove() {
        final int index = tblTo.getSelectionModel().getSelectedIndex();
        if (index > -1) {
            Subject subject = SUBJECT_LIST_TO.get(index);
            SUBJECT_LIST_TO.remove(subject);
            TreeItem<Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST_TO, RecursiveTreeObject::getChildren);
            tblTo.setRoot(root);
            tblTo.setShowRoot(false);
        }
    }

    public void initTable() {
        TreeItem<Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST,
                RecursiveTreeObject::getChildren);

        JFXTreeTableColumn<Subject, Long> idCol = new JFXTreeTableColumn<>("ID");
        idCol.setResizable(false);
        idCol.setPrefWidth(80);
        idCol.setCellValueFactory(param -> param.getValue().getValue().idProperty().asObject());
        idCol.setSortable(false);

        JFXTreeTableColumn<Subject, String> nameCol = new JFXTreeTableColumn<>("Subject");
        nameCol.setResizable(false);
        nameCol.setPrefWidth(130);
        nameCol.setCellValueFactory(param -> param.getValue().getValue().nameProperty());
        nameCol.setSortable(false);

        JFXTreeTableColumn<Subject, Integer> unitCol = new JFXTreeTableColumn<>("Unit");
        unitCol.setResizable(false);
        unitCol.setPrefWidth(80);
        unitCol.setCellValueFactory(param -> param.getValue().getValue().unitProperty().asObject());
        unitCol.setSortable(false);

        tblFrom.getColumns().add(idCol);
        tblFrom.getColumns().add(nameCol);
        tblFrom.getColumns().add(unitCol);

        tblFrom.setRoot(root);
        tblFrom.setShowRoot(false);
    }

    public void initTable2() {
        TreeItem<Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST_TO, RecursiveTreeObject::getChildren);

        JFXTreeTableColumn<Subject, Long> idCol = new JFXTreeTableColumn<>("ID");
        idCol.setResizable(false);
        idCol.setPrefWidth(80);
        idCol.setCellValueFactory(param -> param.getValue().getValue().idProperty().asObject());
        idCol.setSortable(false);

        JFXTreeTableColumn<Subject, String> nameCol = new JFXTreeTableColumn<>("Subject");
        nameCol.setResizable(false);
        nameCol.setPrefWidth(130);
        nameCol.setCellValueFactory(param -> param.getValue().getValue().nameProperty());
        nameCol.setSortable(false);

        JFXTreeTableColumn<Subject, Integer> unitCol = new JFXTreeTableColumn<>("Unit");
        unitCol.setResizable(false);
        unitCol.setPrefWidth(80);
        unitCol.setCellValueFactory(param -> param.getValue().getValue().unitProperty().asObject());
        unitCol.setSortable(false);

        tblTo.getColumns().add(idCol);
        tblTo.getColumns().add(nameCol);
        tblTo.getColumns().add(unitCol);

        tblTo.setRoot(root);
        tblTo.setShowRoot(false);
    }

    public String format(int num) {
        switch (num) {
            case 1:
                return "1ST";
            case 2:
                return "2ND";
            case 3:
                return "3RD";
            default:
                return num + "TH";
        }
    }

    public Curriculum getCurriculumByYearAndSem(long courseId, int index) {
        switch (index) {
            case 0:
                return curriculumDao.getCurriculum(courseId, 1, 1);
            case 1:
                return curriculumDao.getCurriculum(courseId, 1, 2);
            case 2:
                return curriculumDao.getCurriculum(courseId, 2, 1);
            case 3:
                return curriculumDao.getCurriculum(courseId, 2, 2);
            case 4:
                return curriculumDao.getCurriculum(courseId, 3, 1);
            case 5:
                return curriculumDao.getCurriculum(courseId, 4, 2);
            case 6:
                return curriculumDao.getCurriculum(courseId, 5, 1);
            case 7:
                return curriculumDao.getCurriculum(courseId, 6, 2);
            case 8:
                return curriculumDao.getCurriculum(courseId, 7, 1);
            case 9:
                return curriculumDao.getCurriculum(courseId, 8, 2);
        }
        return new Curriculum();
    }

    public void courseProperty(Course course) {
        for (int i = 0; i < COURSE_LIST.size(); i++) {
            if (COURSE_LIST.get(i).getId() == course.getId()) {
                cbCourse.getSelectionModel().select(i);
                break;
            }
        }

    }

    public int getYearByIndex(int index) {
        return (index + 2) / 2;
    }

    public int getSemByIndex(int index) {
        return 2 * (index / 2) == index ? 1 : 2;
    }
}
