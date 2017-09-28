package com.erm.project.ees.controller;

import com.erm.project.ees.dao.CourseDao;
import com.erm.project.ees.dao.SectionDao;
import com.erm.project.ees.dao.StudentDao;
import com.erm.project.ees.dao.impl.CourseDaoImpl;
import com.erm.project.ees.dao.impl.DirtyDaoImpl;
import com.erm.project.ees.dao.impl.SectionDaoImpl;
import com.erm.project.ees.dao.impl.StudentDaoImpl;
import com.erm.project.ees.model.Course;
import com.erm.project.ees.model.Section;
import com.erm.project.ees.model.Student;
import com.erm.project.ees.model.StudentSubjectRecord;
import com.erm.project.ees.stage.EnrollmentStage;
import com.erm.project.ees.util.ResourceHelper;
import com.jfoenix.controls.JFXComboBox;
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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StudentGradeController implements Initializable {

    @FXML
    private ImageView imgvLogo;

    @FXML
    private JFXComboBox<String> cbYearSem;

    @FXML
    private Label lbCourse;

    @FXML
    private Label lbSN;

    @FXML
    private Label lbStudent;

    @FXML
    private Label lbYS;

    @FXML
    private Label lbStatus;

    @FXML
    private JFXComboBox<String> cbCourse;

    @FXML
    private TableView<StudentSubjectRecord> tblRecord;

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

        cbYearSem.setItems(OBSERVABLE_LIST_CURRICULUM);
        cbYearSem.getItems().add("1st yr / 1st sem");
        cbYearSem.getItems().add("1st yr / 2nd sem");
        cbYearSem.getItems().add("2nd yr / 1st sem");
        cbYearSem.getItems().add("2nd yr / 2nd sem");
        cbYearSem.getItems().add("3rd yr / 1st sem");
        cbYearSem.getItems().add("3rd yr / 2nd sem");
        cbYearSem.getItems().add("4th yr / 1st sem");
        cbYearSem.getItems().add("4th yr / 2nd sem");
        cbYearSem.getSelectionModel().select(0);

        for(Course course : courseDao.getCourseList()) {
            cbCourse.getItems().add(course.getName());
            COURSE_LIST.add(course);
        }
        cbCourse.getSelectionModel().select(0);

        tblRecord.setItems(OBSERVABLE_LIST_RECORD);
    }

    @FXML
    protected void onChoose() {
        switch(cbYearSem.getSelectionModel().getSelectedIndex()) {
            case 0 :
                clear();
                loadStudent(student , 1, 1);
                break;
            case 1 :
                clear();
                loadStudent(student , 1, 2);
                break;
            case 2 :
                clear();
                loadStudent(student , 2, 1);
                break;
            case 3 :
                clear();
                loadStudent(student , 2, 2);
                break;
            case 4 :
                clear();
                loadStudent(student , 3, 1);
                break;
            case 5 :
                clear();
                loadStudent(student , 3, 2);
                break;
            case 6 :
                clear();
                loadStudent(student , 4, 1);
                break;
            case 7 :
                clear();
                loadStudent(student , 4, 2);
                break;
            default: break;
        }
    }

    @FXML
    protected void onClickAssessment(ActionEvent event) {
        final EnrollmentStage enrollmentStage = new EnrollmentStage();
        new Thread(()->{
            Platform.runLater(()->enrollmentStage.showAndWait());
            enrollmentStage.getController().listener(student);
        }).start();
    }

    @FXML
    protected void onClickShift() {
        student.setCourseId(COURSE_LIST.get(cbCourse.getSelectionModel().getSelectedIndex()).getId());
        studentDao.updateStudentById(student.getId(), student);
        lbCourse.setText(COURSE_LIST.get(cbCourse.getSelectionModel().getSelectedIndex()).getName());
        onChoose();
    }

    private void loadStudent(Student student, int year, int semester) {
        TableColumn<StudentSubjectRecord, String> sName = new TableColumn<>("Subject");
        sName.setCellValueFactory(new PropertyValueFactory<>("subjectName"));

        TableColumn<StudentSubjectRecord, String> sDesc = new TableColumn<>("Description");
        sDesc.setCellValueFactory(new PropertyValueFactory<>("subjectDesc"));
        sDesc.setResizable(false);
        sDesc.setPrefWidth(200);

        TableColumn<StudentSubjectRecord, String> date = new TableColumn<>("Date");
        date.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<StudentSubjectRecord, String> midterm = new TableColumn<>("Midterm");
        midterm.setCellValueFactory(new PropertyValueFactory<>("midterm"));

        TableColumn<StudentSubjectRecord, String> finalterm = new TableColumn<>("Finalterm");
        finalterm.setCellValueFactory(new PropertyValueFactory<>("finalterm"));

        TableColumn<StudentSubjectRecord, String> mark = new TableColumn<>("Mark");
        mark.setCellValueFactory(new PropertyValueFactory<>("mark"));

        tblRecord.getColumns().add(sName);
        tblRecord.getColumns().add(sDesc);
        tblRecord.getColumns().add(date);
        tblRecord.getColumns().add(midterm);
        tblRecord.getColumns().add(finalterm);
        tblRecord.getColumns().add(mark);

        for(StudentSubjectRecord record : new DirtyDaoImpl().getStudentSubjectRecords(student.getCourseId(),
                student.getId(), year, semester)) {
            tblRecord.getItems().add(record);
        }
    }

    private void clear() {
        tblRecord.getItems().clear();
        tblRecord.getColumns().clear();
    }

    public void listener(Student student) {
        this.student = student;
        final Section section = sectionDao.getSectionById(student.getSectionId());
        lbCourse.setText(courseDao.getCourseById(student.getCourseId()).getName());
        lbSN.setText(student.getStudentNumber() + "");
        lbStudent.setText(student.getFirstName() + " " + student.getLastName());
        lbYS.setText(section.getYear() + "-" +section.getName().toUpperCase());
        lbStatus.setText(student.getStatus());

        for(int i=0; i<COURSE_LIST.size(); i++) {
            if(student.getCourseId() == COURSE_LIST.get(i).getId()) {
                cbCourse.getSelectionModel().select(i);
                break;
            }
        }

        clear();
        loadStudent(student ,1, 1);
    }

    @FXML
    protected void onClickRefresh() {
        onChoose();
    }
}
