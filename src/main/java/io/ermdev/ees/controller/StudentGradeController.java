package io.ermdev.ees.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import io.ermdev.ees.dao.*;
import io.ermdev.ees.dao.impl.CurriculumDaoImpl;
import io.ermdev.ees.dao.impl.SectionDaoImpl;
import io.ermdev.ees.helper.DbFactory;
import io.ermdev.ees.model.*;
import io.ermdev.ees.model.v2.AcademicYear;
import io.ermdev.ees.model.v2.Record;
import io.ermdev.ees.stage.EvaluationAddStage;
import io.ermdev.ees.stage.EvaluationDropStage;
import io.ermdev.ees.stage.EvaluationStage;
import io.ermdev.ees.stage.StudentResultStage;
import io.ermdev.ees.util.ResourceHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StudentGradeController implements Initializable, StudentResultStage.OnSelectStudentLister,
        EvaluationStage.OnCloseListener, EvaluationAddStage.OnCloseListener, EvaluationDropStage.OnCloseListener {

    @FXML
    private ImageView imgvLogo;

    @FXML
    private JFXTextField txCourse;

    @FXML
    private JFXTextField txSNumber;

    @FXML
    private Label lbStudent;

    @FXML
    private JFXTextField txYS;

    @FXML
    private JFXTextField txStatus;

    @FXML
    private JFXComboBox<String> cbCourse;

    @FXML
    private TableView<Object> tblRecord;

    @FXML
    private JFXTextField txSearch;

    @FXML
    private JFXButton bnEvaluation;

    @FXML
    private JFXButton bnDropSubject;

    @FXML
    private JFXComboBox<String> cbAY;

    @FXML
    private JFXButton bnAddSubject;

    @FXML
    private Label lbInfo;

    @FXML
    private JFXButton bnNext;

    @FXML
    private JFXButton bnPrevious;

    private Student student;
    private Course course;

    private final int CREDIT_SUBJECT = 1;
    private final int GRADE = 2;
    private final int ACADEMIC_YEAR = 3;
    private int cTable = CREDIT_SUBJECT;

    private ObservableList<String> OBSERVABLE_LIST_COURSE = FXCollections.observableArrayList();
    private ObservableList<String> OBSERVABLE_LIST_ACADEMIC = FXCollections.observableArrayList();
    private ObservableList<String> OBSERVABLE_LIST_CURRICULUM = FXCollections.observableArrayList();
    private ObservableList<Object> OBSERVABLE_LIST_RECORD = FXCollections.observableArrayList();

    private final CourseDao courseDao = DbFactory.courseFactory();
    private final SectionDao sectionDao = new SectionDaoImpl();
    private final StudentDao studentDao = DbFactory.studentFactory();
    private final AcademicYearDao academicYearDao = DbFactory.academicYearFactory();
    private final SubjectDao subjectDao = DbFactory.subjectFactory();
    private final CreditSubjectDao creditSubjectDao = DbFactory.creditSubjectFactory();
    private final CurriculumDao curriculumDao = new CurriculumDaoImpl();

    private final List<Course> COURSE_LIST = new ArrayList<>();
    private final List<Record> RECORD_LIST = new ArrayList<>();
    private final List<AcademicYear> ACADEMIC_YEAR_LIST = new ArrayList<>();
    private final List<Curriculum> CURRICULUM_LIST = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image image = new Image(ResourceHelper.resourceWithBasePath("image/studentlogo.png").toString());
        imgvLogo.setImage(image);

        tblRecord.setStyle("-fx-table-cell-border-color: transparent;");

        OBSERVABLE_LIST_COURSE.clear();
        for (Course course : courseDao.getCourseList()) {
            OBSERVABLE_LIST_COURSE.add(course.getName());
            COURSE_LIST.add(course);
        }
        cbCourse.setItems(OBSERVABLE_LIST_COURSE);
        if (OBSERVABLE_LIST_COURSE.size() > 0) cbCourse.getSelectionModel().select(0);
    }

    @FXML
    protected void onChooseAcademicYear() {
        try {
            final int index = cbAY.getSelectionModel().getSelectedIndex();
            if (index > -1) {
                if (cTable == CREDIT_SUBJECT) {
                    final long academicId = ACADEMIC_YEAR_LIST.get(index).getId();
                    RECORD_LIST.clear();
                    RECORD_LIST.addAll(creditSubjectDao.getRecordList(academicId, student.getId()));
                    loadRecord(RECORD_LIST);
                } else if (cTable == GRADE) {
                    final long curriculumId = CURRICULUM_LIST.get(index).getId();
                    RECORD_LIST.clear();

                    for (Subject subject : curriculumDao.getSubjectList(curriculumId)) {
                        if (creditSubjectDao.isSubjectPassed(subject.getId(), student.getId())) {
                            RECORD_LIST.add(creditSubjectDao.getRecordBySubjectId(subject.getId(), student.getId()));
                        } else if (creditSubjectDao.isSubjectIncomplete(subject.getId(), student.getId())) {
                            RECORD_LIST.add(creditSubjectDao.getRecordBySubjectId(subject.getId(), student.getId()));
                        } else if (creditSubjectDao.isSubjectFailed(subject.getId(), student.getId())) {
                            RECORD_LIST.add(creditSubjectDao.getRecordBySubjectId(subject.getId(), student.getId()));
                        } else if (creditSubjectDao.isSubjectDropped(subject.getId(), student.getId())) {
                            RECORD_LIST.add(creditSubjectDao.getRecordBySubjectId(subject.getId(), student.getId()));
                        } else {
                            RECORD_LIST.add(new Record(0.0, 0.0, "--/--/--", "--", subject.getId(), student.getId(), 0));
                        }
                    }
                    loadGrade(RECORD_LIST);
                }
            }
        } catch (Exception e) {
            new Thread(() -> JOptionPane.showMessageDialog(null, "Please try again"));
        }
    }

    @FXML
    private void onClickNext() {
        final int index = cbAY.getSelectionModel().getSelectedIndex();
        int max = 0;
        switch (cTable) {
            case CREDIT_SUBJECT:
                max = OBSERVABLE_LIST_ACADEMIC.size();
                break;
            case GRADE:
                max = OBSERVABLE_LIST_CURRICULUM.size();
                break;
        }
        if (index < max - 1 && index > -1) {
            cbAY.getSelectionModel().select(index + 1);
        }
    }

    @FXML
    private void onClickPrevious() {
        final int index = cbAY.getSelectionModel().getSelectedIndex();
        if (index > 0) {
            cbAY.getSelectionModel().select(index - 1);
        }
    }

    @FXML
    protected void onClickEvaluation(ActionEvent event) {
        final EvaluationStage evaluationStage = new EvaluationStage();
        new Thread(() -> {
            Platform.runLater(evaluationStage::showAndWait);
            evaluationStage.getController().listener(student);
            evaluationStage.setListener(this);
            evaluationStage.setOnCloseRequest(e -> onClose());
        }).start();
    }

    @FXML
    protected void onClickDropSubject() {
        final EvaluationDropStage evaluationDropStage = new EvaluationDropStage();
        new Thread(() -> {
            Platform.runLater(evaluationDropStage::showAndWait);
            evaluationDropStage.getController().listener(student);
            evaluationDropStage.setListener(this);
            evaluationDropStage.setOnCloseRequest(e -> onClose());
        }).start();
    }

    @FXML
    protected void onClickAddSubject() {
        final EvaluationAddStage evaluationAddStage = new EvaluationAddStage();
        new Thread(() -> {
            Platform.runLater(evaluationAddStage::showAndWait);
            evaluationAddStage.getController().listener(student);
            evaluationAddStage.setListener(this);
            evaluationAddStage.setOnCloseRequest(e -> onClose());
        }).start();
    }

    @FXML
    protected void onClickCreditSubject() {
        cTable = CREDIT_SUBJECT;
        lbInfo.setText("Academic Year : ");
        bnNext.setDisable(false);
        bnPrevious.setDisable(false);

        lbInfo.setVisible(true);
        cbAY.setVisible(true);

        OBSERVABLE_LIST_ACADEMIC.clear();
        ACADEMIC_YEAR_LIST.clear();
        RECORD_LIST.clear();

        for (AcademicYear academicYear : academicYearDao.getAcademicYearList(student.getId(), course.getId())) {
            OBSERVABLE_LIST_ACADEMIC.add(academicYear.getName() + " ( " + format(academicYear.getSemester()) + " SEMESTER )");
            ACADEMIC_YEAR_LIST.add(academicYear);
        }
        cbAY.setItems(OBSERVABLE_LIST_ACADEMIC);
        if (cbAY.getItems().size() > 0) {
            clear();
            initRecord();

            RECORD_LIST.addAll(creditSubjectDao.getRecordList(ACADEMIC_YEAR_LIST.get(0).getId(), student.getId()));
            cbAY.getSelectionModel().select(0);
        }
    }

    @FXML
    protected void onClickAcademicYear() {
        cTable = ACADEMIC_YEAR;

        bnNext.setDisable(true);
        bnPrevious.setDisable(true);

        lbInfo.setVisible(false);
        cbAY.setVisible(false);

        ACADEMIC_YEAR_LIST.clear();
        ACADEMIC_YEAR_LIST.addAll(academicYearDao.getAcademicYearListOpen());

        clear();
        initAcademicYear();

        loadAcademicYear(ACADEMIC_YEAR_LIST);
    }

    @FXML
    protected void onClickGrade() {
        cTable = GRADE;
        lbInfo.setText("Year / Semester : ");
        bnNext.setDisable(false);
        bnPrevious.setDisable(false);

        lbInfo.setVisible(true);
        cbAY.setVisible(true);

        OBSERVABLE_LIST_CURRICULUM.clear();
        CURRICULUM_LIST.clear();
        RECORD_LIST.clear();

        for (Curriculum curriculum : curriculumDao.getCurriculumListByCourseId(course.getId())) {
            CURRICULUM_LIST.add(curriculum);
            OBSERVABLE_LIST_CURRICULUM.add(format(curriculum.getYear()) + " YEAR - " + format(curriculum.getSemester()) + " SEMESTER");
        }
        cbAY.setItems(OBSERVABLE_LIST_CURRICULUM);
        if (cbAY.getItems().size() > 0) {
            clear();
            initGrade();

            for (Subject subject : curriculumDao.getSubjectList(CURRICULUM_LIST.get(0).getId())) {
                if (creditSubjectDao.isSubjectPassed(subject.getId(), student.getId())) {
                    RECORD_LIST.add(creditSubjectDao.getRecordBySubjectId(subject.getId(), student.getId()));
                } else if (creditSubjectDao.isSubjectIncomplete(subject.getId(), student.getId())) {
                    RECORD_LIST.add(creditSubjectDao.getRecordBySubjectId(subject.getId(), student.getId()));
                } else if (creditSubjectDao.isSubjectFailed(subject.getId(), student.getId())) {
                    RECORD_LIST.add(creditSubjectDao.getRecordBySubjectId(subject.getId(), student.getId()));
                } else if (creditSubjectDao.isSubjectDropped(subject.getId(), student.getId())) {
                    RECORD_LIST.add(creditSubjectDao.getRecordBySubjectId(subject.getId(), student.getId()));
                } else {
                    RECORD_LIST.add(new Record(0.0, 0.0, "--/--/--", "--", subject.getId(), student.getId(), 0));
                }
            }
            cbAY.getSelectionModel().select(0);
        }
    }

    @FXML
    protected void onClickShift() {
        student.setCourseId(COURSE_LIST.get(cbCourse.getSelectionModel().getSelectedIndex()).getId());
        studentDao.updateStudentById(student.getId(), student);
        txCourse.setText(COURSE_LIST.get(cbCourse.getSelectionModel().getSelectedIndex()).getName());
    }

    private void initGrade() {
        Platform.runLater(() -> {
            TableColumn<Object, String> sName = new TableColumn<>("Subject");
            sName.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
            sName.setResizable(false);
            sName.setPrefWidth(200);
            sName.setSortable(false);

            TableColumn<Object, String> sDesc = new TableColumn<>("Description");
            sDesc.setCellValueFactory(new PropertyValueFactory<>("subjectDesc"));
            sDesc.setResizable(false);
            sDesc.setPrefWidth(290);
            sDesc.setSortable(false);

            TableColumn<Object, String> date = new TableColumn<>("Date");
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            date.setResizable(false);
            date.setPrefWidth(120);
            date.setSortable(false);

            TableColumn<Object, String> midterm = new TableColumn<>("Midterm");
            midterm.setCellValueFactory(new PropertyValueFactory<>("midterm"));
            midterm.setResizable(false);
            midterm.setPrefWidth(80);
            midterm.setSortable(false);

            TableColumn<Object, String> finalterm = new TableColumn<>("Finalterm");
            finalterm.setCellValueFactory(new PropertyValueFactory<>("finalterm"));
            finalterm.setResizable(false);
            finalterm.setPrefWidth(80);
            finalterm.setSortable(false);

            TableColumn<Object, String> mark = new TableColumn<>("Remarks");
            mark.setCellValueFactory(new PropertyValueFactory<>("remark"));
            mark.setResizable(false);
            mark.setPrefWidth(80);
            mark.setSortable(false);

            tblRecord.getColumns().add(sName);
            tblRecord.getColumns().add(sDesc);
            tblRecord.getColumns().add(date);
            tblRecord.getColumns().add(midterm);
            tblRecord.getColumns().add(finalterm);
            tblRecord.getColumns().add(mark);
        });
    }

    private void initRecord() {
        Platform.runLater(() -> {
            TableColumn<Object, String> sName = new TableColumn<>("Subject");
            sName.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
            sName.setResizable(false);
            sName.setPrefWidth(200);
            sName.setSortable(false);

            TableColumn<Object, String> sDesc = new TableColumn<>("Description");
            sDesc.setCellValueFactory(new PropertyValueFactory<>("subjectDesc"));
            sDesc.setResizable(false);
            sDesc.setPrefWidth(290);
            sDesc.setSortable(false);

            TableColumn<Object, String> date = new TableColumn<>("Date");
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            date.setResizable(false);
            date.setPrefWidth(120);
            date.setSortable(false);

            TableColumn<Object, String> midterm = new TableColumn<>("Midterm");
            midterm.setCellValueFactory(new PropertyValueFactory<>("midterm"));
            midterm.setResizable(false);
            midterm.setPrefWidth(80);
            midterm.setSortable(false);

            TableColumn<Object, String> finalterm = new TableColumn<>("Finalterm");
            finalterm.setCellValueFactory(new PropertyValueFactory<>("finalterm"));
            finalterm.setResizable(false);
            finalterm.setPrefWidth(80);
            finalterm.setSortable(false);

            TableColumn<Object, String> mark = new TableColumn<>("Remark");
            mark.setCellValueFactory(new PropertyValueFactory<>("remark"));
            mark.setResizable(false);
            mark.setPrefWidth(80);
            mark.setSortable(false);

            tblRecord.getColumns().add(sName);
            tblRecord.getColumns().add(sDesc);
            tblRecord.getColumns().add(date);
            tblRecord.getColumns().add(midterm);
            tblRecord.getColumns().add(finalterm);
            tblRecord.getColumns().add(mark);
        });
    }

    private void initAcademicYear() {
        Platform.runLater(() -> {
            TableColumn<Object, String> ayId = new TableColumn<>("Id");
            ayId.setCellValueFactory(new PropertyValueFactory<>("id"));
            ayId.setPrefWidth(200);
            ayId.setSortable(false);

            TableColumn<Object, String> ayCode = new TableColumn<>("Code");
            ayCode.setCellValueFactory(new PropertyValueFactory<>("code"));
            ayCode.setPrefWidth(200);
            ayCode.setSortable(false);

            TableColumn<Object, String> ayYear = new TableColumn<>("Year");
            ayYear.setCellValueFactory(new PropertyValueFactory<>("name"));
            ayYear.setPrefWidth(200);
            ayYear.setSortable(false);

            TableColumn<Object, String> aySem = new TableColumn<>("Semester");
            aySem.setCellValueFactory(new PropertyValueFactory<>("semester"));
            aySem.setPrefWidth(200);
            aySem.setSortable(false);

            TableColumn<Object, String> ayStatus = new TableColumn<>("Status");
            ayStatus.setCellValueFactory(new PropertyValueFactory<>("displayStatus"));
            ayStatus.setPrefWidth(200);
            ayStatus.setSortable(false);

            TableColumn<Object, String> ayCourse = new TableColumn<>("Course");
            ayCourse.setCellValueFactory(new PropertyValueFactory<>("displayCourse"));
            ayCourse.setPrefWidth(200);
            ayCourse.setSortable(false);

            TableColumn<Object, String> ayStudents = new TableColumn<>("Total Student");
            ayStudents.setCellValueFactory(new PropertyValueFactory<>("students"));
            ayStudents.setPrefWidth(120);
            ayStudents.setSortable(false);

            tblRecord.getColumns().add(ayId);
            tblRecord.getColumns().add(ayCode);
            tblRecord.getColumns().add(ayYear);
            tblRecord.getColumns().add(aySem);
            tblRecord.getColumns().add(ayStatus);
            tblRecord.getColumns().add(ayCourse);
            tblRecord.getColumns().add(ayStudents);
        });
    }

    private void loadRecord(List<Record> recordList) {
        tblRecord.scrollTo(0);
        OBSERVABLE_LIST_RECORD.clear();
        for (Record record : recordList) {
            record.setSubjectDao(subjectDao);
            OBSERVABLE_LIST_RECORD.add(record);
        }
        Platform.runLater(() -> tblRecord.setItems(OBSERVABLE_LIST_RECORD));
    }

    private void loadGrade(List<Record> recordList) {
        tblRecord.scrollTo(0);
        OBSERVABLE_LIST_RECORD.clear();
        for (Record record : recordList) {
            record.setSubjectDao(subjectDao);
            OBSERVABLE_LIST_RECORD.add(record);
        }
        Platform.runLater(() -> tblRecord.setItems(OBSERVABLE_LIST_RECORD));
    }

    private void loadAcademicYear(List<AcademicYear> academicYearList) {
        tblRecord.scrollTo(0);
        OBSERVABLE_LIST_RECORD.clear();
        OBSERVABLE_LIST_RECORD.addAll(academicYearList);
        Platform.runLater(() -> tblRecord.setItems(OBSERVABLE_LIST_RECORD));
    }

    private void clear() {
        Platform.runLater(() -> tblRecord.getColumns().clear());
    }

    public void listener(Student student) {
        this.student = student;
        this.course = courseDao.getCourseById(student.getCourseId());
        final Section section = sectionDao.getSectionById(student.getSectionId());

        txCourse.setText(course.getName());
        txSNumber.setText(student.getStudentNumber() + "");
        lbStudent.setText(student.getFirstName() + " " + student.getLastName());
        txYS.setText(section.getYear() + "-" + section.getName().toUpperCase());
        txStatus.setText(student.getStatus());
        lbInfo.setText("Academic Year : ");

        lbInfo.setText("Year / Semester : ");
        bnNext.setDisable(false);
        bnPrevious.setDisable(false);

        lbInfo.setVisible(true);
        cbAY.setVisible(true);

        cTable = CREDIT_SUBJECT;

        final long code = academicYearDao.currentCodeOpen(course.getId());
        final int semester = academicYearDao.currentSemesterOpen(course.getId());

        if (code != 0) {
            if (!academicYearDao.isTaken(student.getId(), code, semester)) {
                bnEvaluation.setDisable(false);
                bnDropSubject.setDisable(true);
                bnAddSubject.setDisable(true);
            } else {
                bnEvaluation.setDisable(true);
                bnDropSubject.setDisable(false);
                bnAddSubject.setDisable(false);
            }
        } else {
            bnEvaluation.setDisable(true);
            bnDropSubject.setDisable(true);
            bnAddSubject.setDisable(true);
        }

        OBSERVABLE_LIST_ACADEMIC.clear();
        ACADEMIC_YEAR_LIST.clear();
        RECORD_LIST.clear();

        for (AcademicYear academicYear : academicYearDao.getAcademicYearList(student.getId(), course.getId())) {
            OBSERVABLE_LIST_ACADEMIC.add(academicYear.getName() + " ( " + format(academicYear.getSemester()) + " SEMESTER )");
            ACADEMIC_YEAR_LIST.add(academicYear);
        }

        clear();
        initRecord();

        cbAY.setItems(OBSERVABLE_LIST_ACADEMIC);
        if (cbAY.getItems().size() > 0) {
            RECORD_LIST.addAll(creditSubjectDao.getRecordList(ACADEMIC_YEAR_LIST.get(0).getId(), student.getId()));
            cbAY.getSelectionModel().select(0);
            loadRecord(RECORD_LIST);
        }
    }

    @FXML
    protected void onClickRefresh() {
        switch (cTable) {
            case CREDIT_SUBJECT:
                onClickCreditSubject();
                break;
            case GRADE:
                onClickGrade();
                break;
            case ACADEMIC_YEAR:
                onChooseAcademicYear();
                break;
        }
    }

    @FXML
    protected void onClickSearch() {
        if (!txSearch.getText().trim().isEmpty()) {
            if (txSearch.getText().trim().matches("^[0-9a-zA-Z]+$")) {
                List<Student> studentList = new ArrayList<>();
                if (isNumber(txSearch.getText().trim())) {
                    Student student = studentDao.getStudentById(Long.parseLong(txSearch.getText().trim()));
                    if (student == null) {
                        new Thread(() -> JOptionPane.showMessageDialog(null, "No result found"))
                                .start();
                        return;
                    }
                    studentList.add(student);
                } else {
                    studentList.addAll(studentDao
                            .getStudentList("WHERE firstName='" + txSearch.getText().trim() + "' " +
                                    "OR lastName='" + txSearch.getText().trim() + "' " +
                                    "OR middleName='" + txSearch.getText().trim() + "'"));
                    if (studentList.size() < 1) {
                        new Thread(() -> JOptionPane.showMessageDialog(null, "No result found"))
                                .start();
                        return;
                    }
                }
                if (studentList.size() > 1) {
                    StudentResultStage studentResultStage = new StudentResultStage();
                    studentResultStage.setOnSelectStudentLister(this);
                    Platform.runLater(studentResultStage::showAndWait);
                    studentResultStage.getController().listener(studentList);
                } else {
                    onSelect(studentList.get(0));

                }
                txSearch.setPromptText("Enter a student name, number");
                txSearch.setStyle("-fx-prompt-text-fill:#000000");
            } else {
                txSearch.setText("");
                txSearch.setPromptText("Invalid input");
                txSearch.setStyle("-fx-prompt-text-fill:#d35400");
            }
        }
    }

    @FXML
    protected void onActionSearch() {
        onClickSearch();
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

    @FXML
    private void onClickAS() {
        try {
            final String path = "pdf/"
                    .concat(student.getStudentNumber() + "")
                    .concat(".pdf");
            final File file = new File(path);
            if (file.exists()) {
                Desktop.getDesktop().open(file);
            } else {
                new Thread(() ->
                        JOptionPane.showMessageDialog(null, "No file exist"))
                        .start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isNumber(String string) {
        try {
            Long.parseLong(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void onSelect(Student student) {
        listener(student);
    }

    @Override
    public void onClose() {
        final long code = academicYearDao.currentCodeOpen(course.getId());
        final int semester = academicYearDao.currentSemesterOpen(course.getId());

        if (code != 0) {
            if (!academicYearDao.isTaken(student.getId(), code, semester)) {
                bnEvaluation.setDisable(false);
                bnDropSubject.setDisable(true);
                bnAddSubject.setDisable(true);
            } else {
                bnEvaluation.setDisable(true);
                bnDropSubject.setDisable(false);
                bnAddSubject.setDisable(false);
            }
        } else {
            bnEvaluation.setDisable(true);
            bnDropSubject.setDisable(true);
            bnAddSubject.setDisable(true);
        }
    }
}
