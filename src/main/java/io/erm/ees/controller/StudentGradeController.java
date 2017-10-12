package io.erm.ees.controller;

import io.erm.ees.dao.CourseDao;
import io.erm.ees.dao.SectionDao;
import io.erm.ees.dao.StudentDao;
import io.erm.ees.dao.impl.CourseDaoImpl;
import io.erm.ees.dao.impl.DirtyDaoImpl;
import io.erm.ees.dao.impl.SectionDaoImpl;
import io.erm.ees.dao.impl.StudentDaoImpl;
import io.erm.ees.model.Course;
import io.erm.ees.model.Section;
import io.erm.ees.model.Student;
import io.erm.ees.model.StudentSubjectRecord;
import io.erm.ees.stage.EnrollmentStage;
import io.erm.ees.stage.EvaluationStage;
import io.erm.ees.stage.StudentResultStage;
import io.erm.ees.util.ResourceHelper;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
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

public class StudentGradeController implements Initializable, StudentResultStage.OnSelectStudentLister {

    @FXML
    private ImageView imgvLogo;

    @FXML
    private JFXComboBox<String> cbYearSem;

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
    private TableView<StudentSubjectRecord> tblRecord;

    @FXML
    private JFXTextField txSearch;

    private Student student;

    private ObservableList<String> OBSERVABLE_LIST_CURRICULUM = FXCollections.observableArrayList();
    private ObservableList<StudentSubjectRecord> OBSERVABLE_LIST_RECORD = FXCollections.observableArrayList();
    private final List<Course> COURSE_LIST = new ArrayList<>();

    private final CourseDao courseDao = new CourseDaoImpl();
    private final SectionDao sectionDao = new SectionDaoImpl();
    private final StudentDao studentDao = new StudentDaoImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image image = new Image(ResourceHelper.resourceWithBasePath("image/studentlogo.png").toString());
        imgvLogo.setImage(image);

        for (Course course : courseDao.getCourseList()) {
            cbCourse.getItems().add(course.getName());
            COURSE_LIST.add(course);
        }
        cbCourse.getSelectionModel().select(0);

        tblRecord.setItems(OBSERVABLE_LIST_RECORD);
    }

    @FXML
    protected void onChoose() {
        switch (cbYearSem.getSelectionModel().getSelectedIndex()) {
            case 0:
                clear();
                loadStudent(student, 1, 1);
                break;
            case 1:
                clear();
                loadStudent(student, 1, 2);
                break;
            case 2:
                clear();
                loadStudent(student, 2, 1);
                break;
            case 3:
                clear();
                loadStudent(student, 2, 2);
                break;
            case 4:
                clear();
                loadStudent(student, 3, 1);
                break;
            case 5:
                clear();
                loadStudent(student, 3, 2);
                break;
            case 6:
                clear();
                loadStudent(student, 4, 1);
                break;
            case 7:
                clear();
                loadStudent(student, 4, 2);
                break;
            default:
                break;
        }
    }

    @FXML
    protected void onClickEvaluation(ActionEvent event) {
        final EvaluationStage evaluationStage = new EvaluationStage();
        new Thread(() -> {
            Platform.runLater(() -> evaluationStage.showAndWait());
            evaluationStage.getController().listener(student);
        }).start();
    }

    @FXML
    protected void onClickShift() {
        student.setCourseId(COURSE_LIST.get(cbCourse.getSelectionModel().getSelectedIndex()).getId());
        studentDao.updateStudentById(student.getId(), student);
        txCourse.setText(COURSE_LIST.get(cbCourse.getSelectionModel().getSelectedIndex()).getName());
        onChoose();
    }

    private void loadStudent(Student student, int year, int semester) {
        TableColumn<StudentSubjectRecord, String> sName = new TableColumn<>("Subject");
        sName.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        sName.setResizable(false);
        sName.setPrefWidth(200);

        TableColumn<StudentSubjectRecord, String> sDesc = new TableColumn<>("Description");
        sDesc.setCellValueFactory(new PropertyValueFactory<>("subjectDesc"));
        sDesc.setResizable(false);
        sDesc.setPrefWidth(300);

        TableColumn<StudentSubjectRecord, String> date = new TableColumn<>("Date");
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        date.setResizable(false);
        date.setPrefWidth(120);

        TableColumn<StudentSubjectRecord, String> midterm = new TableColumn<>("Midterm");
        midterm.setCellValueFactory(new PropertyValueFactory<>("midterm"));
        midterm.setResizable(false);
        midterm.setPrefWidth(80);

        TableColumn<StudentSubjectRecord, String> finalterm = new TableColumn<>("Finalterm");
        finalterm.setCellValueFactory(new PropertyValueFactory<>("finalterm"));
        finalterm.setResizable(false);
        finalterm.setPrefWidth(80);

        TableColumn<StudentSubjectRecord, String> mark = new TableColumn<>("Remarks");
        mark.setCellValueFactory(new PropertyValueFactory<>("mark"));
        mark.setResizable(false);
        mark.setPrefWidth(80);

        tblRecord.getColumns().add(sName);
        tblRecord.getColumns().add(sDesc);
        tblRecord.getColumns().add(date);
        tblRecord.getColumns().add(midterm);
        tblRecord.getColumns().add(finalterm);
        tblRecord.getColumns().add(mark);

        for (StudentSubjectRecord record : new DirtyDaoImpl().getStudentSubjectRecords(student.getCourseId(),
                student.getId(), year, semester)) {
            tblRecord.getItems().add(record);
        }
    }

    @FXML
    protected void onClickCreditSubject() {
        clear();
        loadStudent(student, 1, 1);
    }

    private void clear() {
        tblRecord.getItems().clear();
        tblRecord.getColumns().clear();
    }

    public void listener(Student student) {
        this.student = student;
        final Section section = sectionDao.getSectionById(student.getSectionId());
        txCourse.setText(courseDao.getCourseById(student.getCourseId()).getName());
        txSNumber.setText(student.getStudentNumber() + "");
        lbStudent.setText(student.getFirstName() + " " + student.getLastName());
        txYS.setText(section.getYear() + "-" + section.getName().toUpperCase());
        txStatus.setText(student.getStatus());

        int index = 0;
        for (int i = 0; i < COURSE_LIST.size(); i++) {
            if (student.getCourseId() == COURSE_LIST.get(i).getId()) {
                cbCourse.getSelectionModel().select(i);
                index = i;
                break;
            }
        }

        clear();
        loadStudent(student, 1, 1);

        cbYearSem.setItems(OBSERVABLE_LIST_CURRICULUM);
        for (int year = 1; year <= COURSE_LIST.get(index).getTotalYear(); year++) {
            for (int sem = 1; sem <= COURSE_LIST.get(index).getTotalSemester(); sem++) {
                cbYearSem.getItems().add(format(year) + " YEAR / " + format(sem) + " SEMESTER");
            }
        }
        cbYearSem.getSelectionModel().select(0);
    }

    @FXML
    protected void onClickRefresh() {
        onChoose();
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
                    Platform.runLater(() -> studentResultStage.showAndWait());
                    studentResultStage.getController().listener(studentList);
                } else {
                    onSelect(studentList.get(0));

                }
                cbYearSem.getSelectionModel().select(0);
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
                    Platform.runLater(() -> studentResultStage.showAndWait());
                    studentResultStage.getController().listener(studentList);
                } else {
                    onSelect(studentList.get(0));

                }
                cbYearSem.getSelectionModel().select(0);
                txSearch.setPromptText("Enter a student name, number");
                txSearch.setStyle("-fx-prompt-text-fill:#000000");
            } else {
                txSearch.setText("");
                txSearch.setPromptText("Invalid input");
                txSearch.setStyle("-fx-prompt-text-fill:#d35400");
            }
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

    @FXML
    private void onClickAS() {
        try {
            final String path = "pdf/"
            .concat(student.getStudentNumber() + "")
            .concat(".pdf");
            final File file = new File(path);
            if(file.exists()) {
                Desktop.getDesktop().open(file);
            } else {
                new Thread(()->
                        JOptionPane.showMessageDialog(null, "No file exist"))
                .start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isNumber(String string) {
        try {
            Long.parseLong(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void onSelect(Student student) {
        this.student = student;
        final Section section = sectionDao.getSectionById(student.getSectionId());
        txCourse.setText(courseDao.getCourseById(student.getCourseId()).getName());
        txSNumber.setText(student.getStudentNumber() + "");
        lbStudent.setText(student.getFirstName() + " " + student.getLastName());
        txYS.setText(section.getYear() + "-" + section.getName().toUpperCase());
        txStatus.setText(student.getStatus());

        int index = 0;
        for (int i = 0; i < COURSE_LIST.size(); i++) {
            if (student.getCourseId() == COURSE_LIST.get(i).getId()) {
                cbCourse.getSelectionModel().select(i);
                index = i;
                break;
            }
        }

        clear();
        loadStudent(student, 1, 1);

        cbYearSem.setItems(OBSERVABLE_LIST_CURRICULUM);
        for (int year = 1; year <= COURSE_LIST.get(index).getTotalYear(); year++) {
            for (int sem = 1; sem <= COURSE_LIST.get(index).getTotalSemester(); sem++) {
                cbYearSem.getItems().add(format(year) + " YEAR / " + format(sem) + " SEMESTER");
            }
        }
    }
}
