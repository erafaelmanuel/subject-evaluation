package com.erm.project.ees.controller;

import com.erm.project.ees.dao.CourseDao;
import com.erm.project.ees.dao.StudentDao;
import com.erm.project.ees.dao.SubjectDao;
import com.erm.project.ees.dao.impl.CourseDaoImpl;
import com.erm.project.ees.dao.impl.StudentDaoImpl;
import com.erm.project.ees.dao.impl.SubjectDaoImpl;
import com.erm.project.ees.model.Course;
import com.erm.project.ees.model.Student;
import com.erm.project.ees.model.Subject;
import com.erm.project.ees.stage.*;
import com.erm.project.ees.stage.window.PopOnExitWindow;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminWindowController implements Initializable, StudentInputStage.OnItemAddLister,
        CurriculumStage.OnItemAddLister, SubjectInputStage.OnItemAddLister {

    @FXML
    private Button bnAdd;

    @FXML
    private Button bnRefresh;

    @FXML
    private MenuBar menuBar;

    @FXML
    private TableView<Object> tblData;

    @FXML
    private Label lbTitle;

    @FXML
    private MenuItem miInputGrade;


    private static final ObservableList<Object> OBSERVABLE_LIST = FXCollections.observableArrayList();

    private static final int NO_TABLE = 0;
    private static final int TABLE_STUDENT = 1;
    private static final int TABLE_SUBJECT = 2;
    private static final int TABLE_COURSE = 3;

    private int mCurrent = TABLE_STUDENT;

    private final CourseStage courseStage = new CourseStage();
    private final StudentInputStage studentInputStage = new StudentInputStage();
    private final SubjectInputStage subjectInputStage = new SubjectInputStage();

    private final List<Student> STUDENT_LIST = new ArrayList<>();
    private final List<Course> COURSE_LIST = new ArrayList<>();
    private final List<Subject> SUBJECT_LIST = new ArrayList<>();

    private final CourseDao courseDao = new CourseDaoImpl();
    private final StudentDao studentDao = new StudentDaoImpl();
    private final SubjectDao subjectDao = new SubjectDaoImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tblData.setItems(OBSERVABLE_LIST);
        loadStudent();
        lbTitle.setText("Student List");

        studentInputStage.setOnItemAddLister(this);
        courseStage.getCurriculumStage().setOnItemAddLister(this);
        subjectInputStage.setOnItemAddLister(this);
    }

    @FXML
    protected void onClickExit() {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        if(PopOnExitWindow.display("Are you sure you want to exit?"))
            stage.close();
    }

    @FXML
    protected void onClickAdd() {
        switch (mCurrent) {
            case TABLE_STUDENT: studentInputStage.showAndWait(); break;
            case TABLE_COURSE:
                courseStage.showAndWait();
                break;
            case TABLE_SUBJECT: subjectInputStage.showAndWait(); break;
            default: courseStage.showAndWait(); break;
        }
    }

    @FXML
    protected void onClickSubject() {
        mCurrent = TABLE_SUBJECT;

        clear();
        loadSubject();

        miInputGrade.setVisible(false);
        lbTitle.setText("Subject List");
    }

    @FXML
    protected void onClickDelete() {
        final int index = tblData.getSelectionModel().getSelectedIndex();
        if(index > -1) {
            switch (mCurrent) {
                case TABLE_STUDENT:
                    studentDao.deleteStudentById(STUDENT_LIST.get(index).getId());
                    clear();
                    loadStudent();
                    break;
                case TABLE_COURSE:
                    courseDao.deleteCourseById(COURSE_LIST.get(index).getId());
                    clear();
                    loadCourse();
                    break;
                case TABLE_SUBJECT:
                    subjectDao.deleteSubjectById(SUBJECT_LIST.get(index).getId());
                    clear();
                    loadSubject();
                    break;
            }
        }
    }

    @FXML
    protected void onClickUpdate() {
        final int index = tblData.getSelectionModel().getSelectedIndex();
        if(index > -1) {
            switch (mCurrent) {
                case TABLE_STUDENT:
                    Student student = STUDENT_LIST.get(index);
                    Platform.runLater(()-> studentInputStage.showAndWait());
                    studentInputStage.getController().listen(student);
                    break;
                case TABLE_SUBJECT:
                    new Thread(()-> {
                        Subject subject = SUBJECT_LIST.get(index);
                        Platform.runLater(() -> subjectInputStage.showAndWait());
                        subjectInputStage.getController().listen(subject);
                    }).start();
                    break;
                case TABLE_COURSE:
                        Course course = COURSE_LIST.get(index);
                        Platform.runLater(() -> courseStage.showAndWait());
                        courseStage.getController().listen(course);
                    break;
            }
        }
    }

    @FXML
    protected void onClickInputGrade() {
        final int index = tblData.getSelectionModel().getSelectedIndex();
        if(index > -1) {
            Student student = STUDENT_LIST.get(index);
            StudentGradeInputStage studentGradeInputStage = new StudentGradeInputStage();
            Platform.runLater(() -> studentGradeInputStage.showAndWait());
            studentGradeInputStage.getController().listening(student);
        }
    }


    @FXML
    protected void onClickStudent() {
        mCurrent = TABLE_STUDENT;

        clear();
        loadStudent();

        miInputGrade.setVisible(true);
        lbTitle.setText("Student List");
    }
    @FXML
    protected void onClickCourse() {
        mCurrent = TABLE_COURSE;

        clear();
        loadCourse();

        miInputGrade.setVisible(false);
        lbTitle.setText("Course List");
    }

    private void loadStudent() {
        TableColumn<Object, String> stStudentNumber = new TableColumn<>("Student Number");
        stStudentNumber.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
        stStudentNumber.setPrefWidth(200);

        TableColumn<Object, String> stFirstName = new TableColumn<>("FirstName");
        stFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        stFirstName.setPrefWidth(200);

        TableColumn<Object, String> stLastName = new TableColumn<>("LastName");
        stLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        stLastName.setPrefWidth(200);

        TableColumn<Object, String> stMiddleName = new TableColumn<>("MiddleName");
        stMiddleName.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        stMiddleName.setPrefWidth(200);

        TableColumn<Object, String> stAge = new TableColumn<>("Age");
        stAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        stAge.setPrefWidth(200);

        TableColumn<Object, String> stGender = new TableColumn<>("Gender");
        stGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        stGender.setPrefWidth(200);

        TableColumn<Object, String> stContact = new TableColumn<>("Contact");
        stContact.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        stContact.setPrefWidth(200);

        tblData.getColumns().add(stStudentNumber);
        tblData.getColumns().add(stFirstName);
        tblData.getColumns().add(stLastName);
        tblData.getColumns().add(stMiddleName);
        tblData.getColumns().add(stAge);
        tblData.getColumns().add(stGender);
        tblData.getColumns().add(stContact);

        STUDENT_LIST.clear();
        for(Student student : studentDao.getStudentList()) {
            tblData.getItems().add(student);
            STUDENT_LIST.add(student);
        }
    }

    private void loadSubject() {
        TableColumn<Object, String> suId = new TableColumn<>("Id");
        suId.setCellValueFactory(new PropertyValueFactory<>("id"));
        suId.setPrefWidth(200);

        TableColumn<Object, String> suName = new TableColumn<>("Name");
        suName.setCellValueFactory(new PropertyValueFactory<>("name"));
        suName.setPrefWidth(200);

        TableColumn<Object, String> suDesc = new TableColumn<>("Description");
        suDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        suDesc.setPrefWidth(200);

        TableColumn<Object, String> suUnit = new TableColumn<>("Unit");
        suUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        suUnit.setPrefWidth(200);

        tblData.getColumns().add(suId);
        tblData.getColumns().add(suName);
        tblData.getColumns().add(suDesc);
        tblData.getColumns().add(suUnit);

        SUBJECT_LIST.clear();
        for(Subject subject : subjectDao.getSubjectList()) {
            tblData.getItems().add(subject);
            SUBJECT_LIST.add(subject);
        }
    }

    private void loadCourse() {
        TableColumn<Object, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(200);

        TableColumn<Object, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(200);

        TableColumn<Object, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("desc"));
        descCol.setPrefWidth(400);

        TableColumn<Object, String> tYearCol = new TableColumn<>("Number of Year");
        tYearCol.setCellValueFactory(new PropertyValueFactory<>("totalYear"));
        tYearCol.setPrefWidth(200);

        TableColumn<Object, String> tSemCol = new TableColumn<>("Number of Semester");
        tSemCol.setCellValueFactory(new PropertyValueFactory<>("totalSemester"));
        tSemCol.setPrefWidth(200);

        tblData.getColumns().add(idCol);
        tblData.getColumns().add(nameCol);
        tblData.getColumns().add(descCol);
        tblData.getColumns().add(tYearCol);
        tblData.getColumns().add(tSemCol);

        COURSE_LIST.clear();
        for(Course course : courseDao.getCourseList()) {
            tblData.getItems().add(course);
            COURSE_LIST.add(course);
        }
    }

    private void clear() {
        tblData.getColumns().clear();
        tblData.getItems().clear();
    }


    @Override
    public void onAddStudent() {
        clear();
        loadStudent();
    }

    @Override
    public void onAddCourse() {
        clear();
        loadCourse();
    }

    @Override
    public void onAddSubject() {
        clear();
        loadSubject();
    }
}
