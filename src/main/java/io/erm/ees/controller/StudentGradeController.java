package io.erm.ees.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import io.erm.ees.dao.*;
import io.erm.ees.dao.impl.DirtyDaoImpl;
import io.erm.ees.dao.impl.SectionDaoImpl;
import io.erm.ees.helper.DbFactory;
import io.erm.ees.model.Course;
import io.erm.ees.model.Section;
import io.erm.ees.model.Student;
import io.erm.ees.model.StudentSubjectRecord;
import io.erm.ees.model.v2.AcademicYear;
import io.erm.ees.model.v2.Record;
import io.erm.ees.stage.DropSubjectStage;
import io.erm.ees.stage.EvaluationStage;
import io.erm.ees.stage.StudentResultStage;
import io.erm.ees.util.ResourceHelper;
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
        EvaluationStage.OnCloseListener {

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
    private TableView<Object> tblRecord;

    @FXML
    private JFXTextField txSearch;

    @FXML
    private JFXButton bnEvaluation;

    @FXML
    private JFXButton bnAD;

    @FXML
    private JFXComboBox<String> cbAY;

    private Student student;

    private Course course;

    private ObservableList<String> OBSERVABLE_LIST_ACADEMIC = FXCollections.observableArrayList();
    private ObservableList<String> OBSERVABLE_LIST_CURRICULUM = FXCollections.observableArrayList();
    private ObservableList<Object> OBSERVABLE_LIST_RECORD = FXCollections.observableArrayList();
    private final List<Course> COURSE_LIST = new ArrayList<>();

    private final CourseDao courseDao = DbFactory.courseFactory();
    private final SectionDao sectionDao = new SectionDaoImpl();
    private final StudentDao studentDao = DbFactory.studentFactory();
    private final AcademicYearDao academicYearDao = DbFactory.academicYearFactory();
    private final SubjectDao subjectDao = DbFactory.subjectFactory();
    private final CreditSubjectDao creditSubjectDao = DbFactory.creditSubjectFactory();
    private final List<Record> RECORD_LIST = new ArrayList<>();
    private final List<AcademicYear> ACADEMIC_YEAR_LIST = new ArrayList<>();



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image image = new Image(ResourceHelper.resourceWithBasePath("image/studentlogo.png").toString());
        imgvLogo.setImage(image);

        initRecord();

//        for (Course course : courseDao.getCourseList()) {
//            cbCourse.getItems().add(course.getName());
//            COURSE_LIST.add(course);
//        }
//        cbCourse.getSelectionModel().select(0);
        //tblRecord.setItems(OBSERVABLE_LIST_RECORD);
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
    protected void onChooseAcademicYear() {
        try {
            final int index = cbAY.getSelectionModel().getSelectedIndex();
            if(index > -1) {
                new Thread(() -> {
                    final long aId = ACADEMIC_YEAR_LIST.get(index).getId();
                    RECORD_LIST.clear();
                    RECORD_LIST.addAll(creditSubjectDao.getRecordList(aId, student.getId()));
                    loadRecord(RECORD_LIST);
                }).start();
            }
        } catch (Exception e) {
            new Thread(()->JOptionPane.showMessageDialog(null, "Please try again"));
        }
    }

    @FXML
    protected void onClickEvaluation(ActionEvent event) {
        final EvaluationStage evaluationStage = new EvaluationStage();
        new Thread(() -> {
            Platform.runLater(() -> evaluationStage.showAndWait());
            evaluationStage.getController().listener(student);
            evaluationStage.setListener(this);
            evaluationStage.setOnCloseRequest(e -> onClose());
        }).start();
    }

    @FXML
    protected void onClickAddDrop(ActionEvent event) {
        final DropSubjectStage dropSubjectStage = new DropSubjectStage();
        new Thread(() -> {
            Platform.runLater(() -> dropSubjectStage.showAndWait());
            dropSubjectStage.getController().listener(student);
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
        TableColumn<Object, String> sName = new TableColumn<>("Subject");
        sName.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        sName.setResizable(false);
        sName.setPrefWidth(200);

        TableColumn<Object, String> sDesc = new TableColumn<>("Description");
        sDesc.setCellValueFactory(new PropertyValueFactory<>("subjectDesc"));
        sDesc.setResizable(false);
        sDesc.setPrefWidth(300);

        TableColumn<Object, String> date = new TableColumn<>("Date");
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        date.setResizable(false);
        date.setPrefWidth(120);

        TableColumn<Object, String> midterm = new TableColumn<>("Midterm");
        midterm.setCellValueFactory(new PropertyValueFactory<>("midterm"));
        midterm.setResizable(false);
        midterm.setPrefWidth(80);

        TableColumn<Object, String> finalterm = new TableColumn<>("Finalterm");
        finalterm.setCellValueFactory(new PropertyValueFactory<>("finalterm"));
        finalterm.setResizable(false);
        finalterm.setPrefWidth(80);

        TableColumn<Object, String> mark = new TableColumn<>("Remarks");
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

    private void initRecord() {
        Platform.runLater(()-> {
            TableColumn<Object, String> sName = new TableColumn<>("Subject");
            sName.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
            sName.setResizable(false);
            sName.setPrefWidth(200);

            TableColumn<Object, String> sDesc = new TableColumn<>("Description");
            sDesc.setCellValueFactory(new PropertyValueFactory<>("subjectDesc"));
            sDesc.setResizable(false);
            sDesc.setPrefWidth(300);

            TableColumn<Object, String> date = new TableColumn<>("Date");
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            date.setResizable(false);
            date.setPrefWidth(120);

            TableColumn<Object, String> midterm = new TableColumn<>("Midterm");
            midterm.setCellValueFactory(new PropertyValueFactory<>("midterm"));
            midterm.setResizable(false);
            midterm.setPrefWidth(80);

            TableColumn<Object, String> finalterm = new TableColumn<>("Finalterm");
            finalterm.setCellValueFactory(new PropertyValueFactory<>("finalterm"));
            finalterm.setResizable(false);
            finalterm.setPrefWidth(80);

            TableColumn<Object, String> mark = new TableColumn<>("Remark");
            mark.setCellValueFactory(new PropertyValueFactory<>("remark"));
            mark.setResizable(false);
            mark.setPrefWidth(80);

            tblRecord.getColumns().add(sName);
            tblRecord.getColumns().add(sDesc);
            tblRecord.getColumns().add(date);
            tblRecord.getColumns().add(midterm);
            tblRecord.getColumns().add(finalterm);
            tblRecord.getColumns().add(mark);
        });
    }

    private void loadRecord(List<Record> recordList) {
        Platform.runLater(()-> {
            OBSERVABLE_LIST_RECORD.clear();
            OBSERVABLE_LIST_RECORD.addAll(recordList);
            for(Record record : recordList) {
                record.setSubjectDao(subjectDao);
                OBSERVABLE_LIST_RECORD.add(record);
            }
            tblRecord.setItems(OBSERVABLE_LIST_RECORD);
        });
    }

    @FXML
    protected void onClickCreditSubject() {
        RECORD_LIST.clear();
        if(cbAY.getItems().size() > 0) {
            cbAY.getSelectionModel().select(0);
            RECORD_LIST.addAll(creditSubjectDao.getRecordList(ACADEMIC_YEAR_LIST.get(0).getId(), student.getId()));
            loadRecord(RECORD_LIST);
        }
    }

    private void clear() {
        Platform.runLater(()->tblRecord.getColumns().clear());
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

//        int index = 0;
//        for (int i = 0; i < COURSE_LIST.size(); i++) {
//            if (student.getCourseId() == COURSE_LIST.get(i).getId()) {
//                cbCourse.getSelectionModel().select(i);
//                index = i;
//                break;
//            }
//        }

//        clear();
//        loadStudent(student, 1, 1);
//
//        cbYearSem.setItems(OBSERVABLE_LIST_CURRICULUM);
//        for (int year = 1; year <= COURSE_LIST.get(index).getTotalYear(); year++) {
//            for (int sem = 1; sem <= COURSE_LIST.get(index).getTotalSemester(); sem++) {
//                cbYearSem.getItems().add(format(year) + " YEAR / " + format(sem) + " SEMESTER");
//            }
//        }
//        cbYearSem.getSelectionModel().select(0);

        final long code = academicYearDao.currentCodeOpen(course.getId());
        final int semester = academicYearDao.currentSemesterOpen(course.getId());

        if(code != 0 && !academicYearDao.isTaken(student.getId(), code, semester)) {
            bnEvaluation.setDisable(false);
            bnAD.setDisable(true);
        } else {
            bnEvaluation.setDisable(true);
            bnAD.setDisable(false);
        }

        OBSERVABLE_LIST_ACADEMIC.clear();
        ACADEMIC_YEAR_LIST.clear();
        RECORD_LIST.clear();

        for(AcademicYear academicYear : academicYearDao.getAcademicYearList(student.getId(), course.getId())) {
            OBSERVABLE_LIST_ACADEMIC.add(academicYear.getName() + " ( "+ format(academicYear.getSemester()) +" SEMESTER )");
            ACADEMIC_YEAR_LIST.add(academicYear);
        }
        cbAY.setItems(OBSERVABLE_LIST_ACADEMIC);
        if(cbAY.getItems().size() > 0) {
            RECORD_LIST.addAll(creditSubjectDao.getRecordList(ACADEMIC_YEAR_LIST.get(0).getId(), student.getId()));
            cbAY.getSelectionModel().select(0);
            loadRecord(RECORD_LIST);
        }
    }

    @FXML
    protected void onClickRefresh() {
        onChooseAcademicYear();
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

        if(code != 0 && !academicYearDao.isTaken(student.getId(), code, semester)) {
            bnEvaluation.setDisable(false);
            bnAD.setDisable(true);
        } else {
            bnEvaluation.setDisable(true);
            bnAD.setDisable(false);
        }
    }
}
