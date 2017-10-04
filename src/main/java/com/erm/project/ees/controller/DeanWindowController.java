package com.erm.project.ees.controller;

import com.erm.project.ees.dao.*;
import com.erm.project.ees.dao.impl.*;
import com.erm.project.ees.model.Course;
import com.erm.project.ees.model.Curriculum;
import com.erm.project.ees.model.Student;
import com.erm.project.ees.model.StudentSubjectRecord;
import com.erm.project.ees.model.v2.Subject;
import com.erm.project.ees.stage.DeanStage;
import com.erm.project.ees.stage.window.PopOnExitWindow;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DeanWindowController implements Initializable {


    @FXML
    private JFXTextField txSN;

    @FXML
    private JFXComboBox<String> cbYS;

    @FXML
    private TableView<Subject> tblList;

    @FXML
    private Label lbSN;

    @FXML
    private Label lbFName;

    @FXML
    private MenuBar menuBar;

    private Student student;

    private final CourseDao courseDao = new CourseDaoImpl();
    private final StudentDao studentDao = new StudentDaoImpl();
    private final CurriculumDao curriculumDao = new CurriculumDaoImpl();
    private final DirtyDao dirtyDao = new DirtyDaoImpl();
    private final SuggestionDao suggestionDao = new SuggestionDaoImpl();
    private final SubjectDao subjectDao = new SubjectDaoImpl();

    private ObservableList<String> OBSERVABLE_LIST_CURRICULUM = FXCollections.observableArrayList();
    private final ObservableList<Subject> SUBJECT_LIST = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
    }

    @FXML
    protected void onClickAdd() {
        final int index = tblList.getSelectionModel().getSelectedIndex();
        if(index > -1) {
            final long subjectId = SUBJECT_LIST.get(index).getId();
            StudentSubjectRecord record = dirtyDao.getStudentSubjectRecordById(student.getId(), subjectId);

            if(record == null) {
                record = new StudentSubjectRecord();
                record.setMark("");
            }
            if(!(record.getMark().equalsIgnoreCase("PASSED")
                    || record.getMark().equalsIgnoreCase("ONGOING"))) {
                suggestionDao.addSubject(student.getId(), subjectId);

                final List<Subject> subjectList = new ArrayList<>();
                for (com.erm.project.ees.model.Subject subject : curriculumDao.getSubjectList(
                        getCurriculumByYearAndSem(student.getCourseId(),
                                cbYS.getSelectionModel().getSelectedIndex()).getId())) {
                    StudentSubjectRecord remark = dirtyDao.getStudentSubjectRecordById(student.getId(), subject.getId());
                    boolean suggestion = suggestionDao.getSubject(student.getId(), subject.getId()) != null;
                    Subject _subject = new Subject();
                    _subject.setId(subject.getId());
                    _subject.setName(subject.getName());
                    _subject.setDesc(subject.getDesc());
                    _subject.setRemark(remark == null ? "" : remark.getMark());
                    _subject.setSuggest(suggestion ? "YES":"NO");
                    subjectList.add(_subject);
                }
                Platform.runLater(() -> {
                    SUBJECT_LIST.clear();
                    SUBJECT_LIST.addAll(subjectList);
                    tblList.setItems(SUBJECT_LIST);
                });
            } else {
                new Thread(()->
                        JOptionPane.showMessageDialog(null, "Unable to add the subject"))
                        .start();
            }
        }

    }

    @FXML
    protected void onClickRemove() {
        final int index = tblList.getSelectionModel().getSelectedIndex();
        if(index > -1) {
            final long subjectId = SUBJECT_LIST.get(index).getId();
            StudentSubjectRecord record = dirtyDao.getStudentSubjectRecordById(student.getId(), subjectId);
            if(true) {
                suggestionDao.removeSubject(student.getId(), subjectId);

                final List<Subject> subjectList = new ArrayList<>();
                for (com.erm.project.ees.model.Subject subject : curriculumDao.getSubjectList(
                        getCurriculumByYearAndSem(student.getCourseId(),
                                cbYS.getSelectionModel().getSelectedIndex()).getId())) {
                    StudentSubjectRecord remark = dirtyDao.getStudentSubjectRecordById(student.getId(), subject.getId());
                    boolean suggestion = suggestionDao.getSubject(student.getId(), subject.getId()) != null;
                    Subject _subject = new Subject();
                    _subject.setId(subject.getId());
                    _subject.setName(subject.getName());
                    _subject.setDesc(subject.getDesc());
                    _subject.setRemark(remark == null ? "" : remark.getMark());
                    _subject.setSuggest(suggestion ? "YES" : "NO");
                    subjectList.add(_subject);
                }
                Platform.runLater(() -> {
                    SUBJECT_LIST.clear();
                    SUBJECT_LIST.addAll(subjectList);
                    tblList.setItems(SUBJECT_LIST);
                });
            } else {
                new Thread(()->JOptionPane.showMessageDialog(null, "Unable to remove the subject"))
                        .start();
            }
        }
    }

    @FXML
    protected void onClickSignOut() {
        DeanStage stage = (DeanStage) menuBar.getScene().getWindow();
        stage.close();
        stage.callBack();
    }

    @FXML
    protected void onClickExit() {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        if (PopOnExitWindow.display("Are you sure you want to exit?"))
            stage.close();
    }

    @FXML
    protected void onChooseYearAndSem() {
        if (cbYS.getSelectionModel().getSelectedIndex() > -1) {
            final int index = cbYS.getSelectionModel().getSelectedIndex();
            final long curriculumId = getCurriculumByYearAndSem(student.getCourseId(),
                    cbYS.getSelectionModel().getSelectedIndex()).getId();
            final List<Subject> subjectList = new ArrayList<>();

            for (com.erm.project.ees.model.Subject subject : curriculumDao.getSubjectList(curriculumId)) {
                StudentSubjectRecord remark = dirtyDao.getStudentSubjectRecordById(student.getId(), subject.getId());
                boolean suggestion = suggestionDao.getSubject(student.getId(), subject.getId()) != null;
                Subject _subject = new Subject();
                _subject.setId(subject.getId());
                _subject.setName(subject.getName());
                _subject.setDesc(subject.getDesc());
                _subject.setRemark(remark == null ? "" : remark.getMark());
                _subject.setSuggest(suggestion ? "YES":"NO");
                subjectList.add(_subject);
            }
            Platform.runLater(() -> {
                SUBJECT_LIST.clear();
                SUBJECT_LIST.addAll(subjectList);
                tblList.setItems(SUBJECT_LIST);
            });
        }
    }

    @FXML
    protected void onClickSearch() {
        if(!txSN.getText().trim().isEmpty()) {
            if(isNumber(txSN.getText().trim())) {
                student = studentDao.getStudentById(Long.parseLong(txSN.getText().trim()));
                listener(student);
            } else
                new Thread(()->JOptionPane.showMessageDialog(null, "Invalid input"))
                        .start();
        }
    }

    private void initTable() {
        TableColumn<Subject, Long> sId = new TableColumn<>("Subject ID");
        sId.setCellValueFactory(new PropertyValueFactory<>("id"));
        sId.setPrefWidth(80);

        TableColumn<Subject, String> sName = new TableColumn<>("Subject");
        sName.setCellValueFactory(new PropertyValueFactory<>("name"));
        sName.setPrefWidth(180);

        TableColumn<Subject, String> sDesc = new TableColumn<>("Description");
        sDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        sDesc.setPrefWidth(300);

        TableColumn<Subject, String> remark = new TableColumn<>("Remark");
        remark.setCellValueFactory(new PropertyValueFactory<>("remark"));
        remark.setPrefWidth(100);
        remark.setResizable(false);

        TableColumn<Subject, String> isSuggest = new TableColumn<>("Suggest");
        isSuggest.setCellValueFactory(new PropertyValueFactory<>("suggest"));
        isSuggest.setPrefWidth(100);
        isSuggest.setResizable(false);

        tblList.getColumns().add(sId);
        tblList.getColumns().add(sName);
        tblList.getColumns().add(sDesc);
        tblList.getColumns().add(remark);
        tblList.getColumns().add(isSuggest);
    }

    public void listener(Student student) {
        this.student = student;
        final Course course = courseDao.getCourseById(student.getCourseId());

        OBSERVABLE_LIST_CURRICULUM.clear();
        cbYS.setItems(OBSERVABLE_LIST_CURRICULUM);
        for (int year = 1; year <= course.getTotalYear(); year++) {
            for (int sem = 1; sem <= course.getTotalSemester(); sem++) {
                cbYS.getItems().add(format(year) + " YEAR / " + format(sem) + " SEMESTER");
            }
        }
        cbYS.getSelectionModel().select(0);
        lbSN.setText(student.getStudentNumber() + "");
        lbFName.setText(String.format("%s, %s %s", student.getLastName(), student.getFirstName(),
                student.getMiddleName()));
    }

    public Curriculum getCurriculumByYearAndSem(long courseId, int index) {
        switch (index) {
            case 0:
                return curriculumDao.getCurriculumListByCourseId(courseId, 1, 1);
            case 1:
                return curriculumDao.getCurriculumListByCourseId(courseId, 1, 2);
            case 2:
                return curriculumDao.getCurriculumListByCourseId(courseId, 2, 1);
            case 3:
                return curriculumDao.getCurriculumListByCourseId(courseId, 2, 2);
            case 4:
                return curriculumDao.getCurriculumListByCourseId(courseId, 3, 1);
            case 5:
                return curriculumDao.getCurriculumListByCourseId(courseId, 4, 2);
            case 6:
                return curriculumDao.getCurriculumListByCourseId(courseId, 5, 1);
            case 7:
                return curriculumDao.getCurriculumListByCourseId(courseId, 6, 2);
            case 8:
                return curriculumDao.getCurriculumListByCourseId(courseId, 7, 1);
            case 9:
                return curriculumDao.getCurriculumListByCourseId(courseId, 8, 2);
        }
        return new Curriculum();
    }

    public boolean isNumber(String string) {
        try {
            Long.parseLong(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
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
}
