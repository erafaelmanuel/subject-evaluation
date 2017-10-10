package io.erm.ees.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import io.erm.ees.dao.*;
import io.erm.ees.dao.impl.*;
import io.erm.ees.model.Course;
import io.erm.ees.model.Student;
import io.erm.ees.model.Subject;
import io.erm.ees.model.UserDetail;
import io.erm.ees.stage.*;
import io.erm.ees.stage.window.PopOnExitWindow;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminWindowController implements Initializable, StudentInputStage.OnItemAddLister,
        CurriculumStage.OnItemAddLister, SubjectInputStage.OnItemAddLister, UserInputStage.OnItemAddLister {

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

    @FXML
    private MenuItem miSpecial;

    @FXML
    private MenuItem miUpdate;

    @FXML
    private MenuItem miDelete;

    @FXML
    private MenuItem miActivate;

    @FXML
    private MenuItem miDeactivate;

    @FXML
    private JFXButton bnSearch;

    @FXML
    private JFXTextField txSearch;

    @FXML
    private TableView<Object> tblSubData;

    private static final ObservableList<Object> OBSERVABLE_LIST = FXCollections.observableArrayList();

    private static final int NO_TABLE = 0;
    private static final int TABLE_STUDENT = 1;
    private static final int TABLE_SUBJECT = 2;
    private static final int TABLE_COURSE = 3;
    private static final int TABLE_USER = 4;

    private int mCurrent = TABLE_STUDENT;

    private final CourseStage courseStage = new CourseStage();
    private final StudentInputStage studentInputStage = new StudentInputStage();
    private final SubjectInputStage subjectInputStage = new SubjectInputStage();
    private final SpecialCurriculumStage specialCurriculumStage = new SpecialCurriculumStage();
    private final UserInputStage userInputStage = new UserInputStage();

    private final List<Student> STUDENT_LIST = new ArrayList<>();
    private final List<Course> COURSE_LIST = new ArrayList<>();
    private final List<Subject> SUBJECT_LIST = new ArrayList<>();
    private final List<Subject> SUBJECT_PREREQUISITE_LIST = new ArrayList<>();
    private final List<UserDetail> USER_LIST = new ArrayList<>();

    private final CourseDao courseDao = new CourseDaoImpl();
    private final StudentDao studentDao = new StudentDaoImpl();
    private final SubjectDao subjectDao = new SubjectDaoImpl();
    private final DirtyDao dirtyDao = new DirtyDaoImpl();
    private final UserDetailDao userDetailDao = new UserDetailDaoImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tblData.setItems(OBSERVABLE_LIST);

        loadStudent();

        lbTitle.setText("Student List");

        miSpecial.setVisible(false);

        miActivate.setVisible(false);
        miDeactivate.setVisible(false);

        bnSearch.setVisible(false);
        txSearch.setVisible(false);

        tblSubData.setPlaceholder(new Label("No Subject Prerequisite"));
        tblSubData.setVisible(false);
        tblSubData.setPrefWidth(0);

        studentInputStage.setOnItemAddLister(this);
        courseStage.getCurriculumStage().setOnItemAddLister(this);
        subjectInputStage.setOnItemAddLister(this);
        userInputStage.setOnItemAddLister(this);
    }

    @FXML
    protected void onClickSearch() {
        clear();
        switch (mCurrent) {
            case TABLE_STUDENT:
                loadStudent();
                break;
            case TABLE_COURSE:
                loadCourse();
                break;
            case TABLE_SUBJECT:
                loadSubject(subjectDao.getSubjectListBySearch(txSearch.getText().trim()));
                break;
            case TABLE_USER:
                loadUser();
                break;
        }
    }

    @FXML
    protected void onActionSearch() {
        clear();
        switch (mCurrent) {
            case TABLE_STUDENT:
                loadStudent();
                break;
            case TABLE_COURSE:
                loadCourse();
                break;
            case TABLE_SUBJECT:
                loadSubject(subjectDao.getSubjectListBySearch(txSearch.getText().trim()));
                break;
            case TABLE_USER:
                loadUser();
                break;
        }
    }

    @FXML
    protected void onSearchPressed() {
        clear();
        switch (mCurrent) {
            case TABLE_STUDENT:
                loadStudent();
                break;
            case TABLE_COURSE:
                loadCourse();
                break;
            case TABLE_SUBJECT:
                loadSubject(subjectDao.getSubjectListBySearch(txSearch.getText().trim()));
                break;
            case TABLE_USER:
                loadUser();
                break;
        }
    }

    @FXML
    protected void onSearchReleased() {
        clear();
        switch (mCurrent) {
            case TABLE_STUDENT:
                loadStudent();
                break;
            case TABLE_COURSE:
                loadCourse();
                break;
            case TABLE_SUBJECT:
                loadSubject(subjectDao.getSubjectListBySearch(txSearch.getText().trim()));
                break;
            case TABLE_USER:
                loadUser();
                break;
        }
    }

    @FXML
    protected void onClickRefresh() {
        clear();
        switch (mCurrent) {
            case TABLE_STUDENT:
                loadStudent();
                break;
            case TABLE_COURSE:
                loadCourse();
                break;
            case TABLE_SUBJECT:
                loadSubject();
                break;
            case TABLE_USER:
                loadUser();
                break;
        }
    }

    @FXML
    protected void onClickItem() {
        final int index = tblData.getSelectionModel().getSelectedIndex();
        switch (mCurrent) {
            case TABLE_STUDENT:
                break;
            case TABLE_COURSE:
                break;
            case TABLE_SUBJECT:
                if(index > -1) {
                    subClear();
                    loadSubjectPrerequisite(dirtyDao.getPrerequisiteBySujectId(SUBJECT_LIST.get(index).getId()));
                }
                break;
            case TABLE_USER:
                break;
        }
    }

    @FXML
    protected void onPressedItem() {
        final int index = tblData.getSelectionModel().getSelectedIndex();
        switch (mCurrent) {
            case TABLE_STUDENT:
                break;
            case TABLE_COURSE:
                break;
            case TABLE_SUBJECT:
                if(index > -1) {
                    subClear();
                    loadSubjectPrerequisite(dirtyDao.getPrerequisiteBySujectId(SUBJECT_LIST.get(index).getId()));
                }
                break;
            case TABLE_USER:
                break;
        }
    }

    @FXML
    protected void onClickExit() {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        if (PopOnExitWindow.display("Are you sure you want to exit?"))
            stage.close();
    }

    @FXML
    protected void onClickAdd() {
        switch (mCurrent) {
            case TABLE_STUDENT:
                studentInputStage.showAndWait();
                break;
            case TABLE_COURSE:
                courseStage.showAndWait();
                break;
            case TABLE_SUBJECT:
                subjectInputStage.showAndWait();
                break;
            case TABLE_USER:
                userInputStage.showAndWait();
                break;
            default:
                courseStage.showAndWait();
                break;
        }
    }

    @FXML
    protected void onClickActivate() {
        final int index = tblData.getSelectionModel().getSelectedIndex();
        if(index > -1) {
            UserDetail userDetail = USER_LIST.get(index);
            if(!userDetail.getUserType().getType().equalsIgnoreCase("admin/admin")) {
                userDetail.setActivated(true);
                userDetailDao.updateUserDetailById(userDetail.getId(), userDetail);

                clear();
                loadUser();
            } else {
                new Thread(()-> JOptionPane.showMessageDialog(null,
                        "You don't have permission to update/delete this account"))
                        .start();
            }
        }
    }

    @FXML
    protected void onClickDeactivate() {
        final int index = tblData.getSelectionModel().getSelectedIndex();
        if(index > -1) {
            UserDetail userDetail = USER_LIST.get(index);
            if(!userDetail.getUserType().getType().equalsIgnoreCase("admin/admin")) {
                userDetail.setActivated(false);
                userDetailDao.updateUserDetailById(userDetail.getId(), userDetail);

                clear();
                loadUser();
            } else {
                new Thread(()-> JOptionPane.showMessageDialog(null,
                        "You don't have permission to update/delete this account"))
                        .start();
            }
        }
    }

    @FXML
    protected void onClickSpecial() {
        final int index = tblData.getSelectionModel().getSelectedIndex();
        if (index > -1) {
            Platform.runLater(() -> specialCurriculumStage.showAndWait());
            specialCurriculumStage.getController().courseProperty(COURSE_LIST.get(index));
        }

    }

    @FXML
    protected void onClickDelete() {
        final int index = tblData.getSelectionModel().getSelectedIndex();
        if (index > -1) {
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
                case TABLE_USER:
                    UserDetail userDetail = USER_LIST.get(index);
                    if(!userDetail.getUserType().getType().equalsIgnoreCase("admin/admin")) {
                        userDetail.setActivated(true);
                        userDetailDao.deleteUserDetailById(userDetail.getId());

                        clear();
                        loadUser();
                    } else {
                        new Thread(()-> JOptionPane.showMessageDialog(null,
                                "You don't have permission to update/delete this account"))
                                .start();
                    }
                    break;
            }
        }
    }

    @FXML
    protected void onClickUpdate() {
        final int index = tblData.getSelectionModel().getSelectedIndex();
        if (index > -1) {
            switch (mCurrent) {
                case TABLE_STUDENT:
                    Student student = STUDENT_LIST.get(index);
                    Platform.runLater(() -> studentInputStage.showAndWait());
                    studentInputStage.getController().listen(student);
                    break;
                case TABLE_SUBJECT:
                    new Thread(() -> {
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
        if (index > -1) {
            Student student = STUDENT_LIST.get(index);
            StudentGradeInputStage studentGradeInputStage = new StudentGradeInputStage();
            Platform.runLater(() -> studentGradeInputStage.showAndWait());
            studentGradeInputStage.getController().listening(student);
        }
    }

    @FXML
    protected void onClickSubject() {
        mCurrent = TABLE_SUBJECT;

        clear();
        loadSubject();

        miInputGrade.setVisible(false);
        miSpecial.setVisible(false);

        miUpdate.setVisible(true);
        miDelete.setVisible(true);

        miActivate.setVisible(false);
        miDeactivate.setVisible(false);

        lbTitle.setText("Subject List");

        bnSearch.setVisible(true);
        txSearch.setVisible(true);

        tblSubData.setVisible(true);
        tblSubData.setPrefWidth(400);

        loadSubjectPrerequisite(new ArrayList<>());
    }

    @FXML
    protected void onClickStudent() {
        mCurrent = TABLE_STUDENT;

        clear();
        loadStudent();

        miInputGrade.setVisible(true);
        miSpecial.setVisible(false);

        miUpdate.setVisible(true);
        miDelete.setVisible(true);

        miActivate.setVisible(false);
        miDeactivate.setVisible(false);

        lbTitle.setText("Student List");

        bnSearch.setVisible(false);
        txSearch.setVisible(false);

        tblSubData.setVisible(false);
        tblSubData.setPrefWidth(0);
    }

    @FXML
    protected void onClickCourse() {
        mCurrent = TABLE_COURSE;

        clear();
        loadCourse();

        miInputGrade.setVisible(false);
        miSpecial.setVisible(true);

        miUpdate.setVisible(true);
        miDelete.setVisible(true);

        miActivate.setVisible(false);
        miDeactivate.setVisible(false);

        lbTitle.setText("Course List");

        bnSearch.setVisible(false);
        txSearch.setVisible(false);

        tblSubData.setVisible(false);
        tblSubData.setPrefWidth(0);
    }

    @FXML
    protected void onClickUser() {
        mCurrent = TABLE_USER;

        clear();
        loadUser();

        miInputGrade.setVisible(false);
        miSpecial.setVisible(false);

        miUpdate.setVisible(false);
        miDelete.setVisible(true);

        miActivate.setVisible(true);
        miDeactivate.setVisible(true);

        lbTitle.setText("User List");

        bnSearch.setVisible(false);
        txSearch.setVisible(false);

        tblSubData.setVisible(false);
        tblSubData.setPrefWidth(0);
    }
    @FXML
    protected void onClickSignout() {
        AdminStage stage = (AdminStage) menuBar.getScene().getWindow();
        stage.close();
        stage.callBack();
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
        for (Student student : studentDao.getStudentList()) {
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
        suUnit.setCellValueFactory(new PropertyValueFactory<>("unitDisplay"));
        suUnit.setPrefWidth(200);

        tblData.getColumns().add(suId);
        tblData.getColumns().add(suName);
        tblData.getColumns().add(suDesc);
        tblData.getColumns().add(suUnit);

        SUBJECT_LIST.clear();
        for (Subject subject : subjectDao.getSubjectList()) {
            tblData.getItems().add(subject);
            SUBJECT_LIST.add(subject);
        }
    }

    private void loadSubject(List<Subject> subjectList) {
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
        suUnit.setCellValueFactory(new PropertyValueFactory<>("unitDisplay"));
        suUnit.setPrefWidth(200);

        tblData.getColumns().add(suId);
        tblData.getColumns().add(suName);
        tblData.getColumns().add(suDesc);
        tblData.getColumns().add(suUnit);

        SUBJECT_LIST.clear();
        for (Subject subject : subjectList) {
            tblData.getItems().add(subject);
            SUBJECT_LIST.add(subject);
        }
    }

    private void loadSubjectPrerequisite(List<Subject> subjectList) {
        TableColumn<Object, String> suId = new TableColumn<>("Id");
        suId.setCellValueFactory(new PropertyValueFactory<>("id"));
        suId.setPrefWidth(100);
        suId.setResizable(false);

        TableColumn<Object, String> suName = new TableColumn<>("Name");
        suName.setCellValueFactory(new PropertyValueFactory<>("name"));
        suName.setPrefWidth(190);
        suName.setResizable(false);

        TableColumn<Object, String> suUnit = new TableColumn<>("Unit");
        suUnit.setCellValueFactory(new PropertyValueFactory<>("unitDisplay"));
        suUnit.setPrefWidth(80);
        suUnit.setResizable(false);

        tblSubData.getColumns().add(suId);
        tblSubData.getColumns().add(suName);
        tblSubData.getColumns().add(suUnit);

        SUBJECT_PREREQUISITE_LIST.clear();
        for (Subject subject : subjectList) {
            tblSubData.getItems().add(subject);
            SUBJECT_PREREQUISITE_LIST.add(subject);
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
        for (Course course : courseDao.getCourseList()) {
            tblData.getItems().add(course);
            COURSE_LIST.add(course);
        }
    }

    private void loadUser() {
        TableColumn<Object, Long> uId = new TableColumn<>("ID");
        uId.setCellValueFactory(new PropertyValueFactory<>("id"));
        uId.setPrefWidth(200);

        TableColumn<Object, String> uUser = new TableColumn<>("Username");
        uUser.setCellValueFactory(new PropertyValueFactory<>("username"));
        uUser.setPrefWidth(200);

        TableColumn<Object, String> uPass = new TableColumn<>("Password");
        uPass.setCellValueFactory(new PropertyValueFactory<>("password"));
        uPass.setPrefWidth(200);

        TableColumn<Object, String> uType = new TableColumn<>("Type");
        uType.setCellValueFactory(new PropertyValueFactory<>("userType"));
        uType.setPrefWidth(200);

        TableColumn<Object, String> uIA = new TableColumn<>("Activated");
        uIA.setCellValueFactory(new PropertyValueFactory<>("activated"));
        uIA.setPrefWidth(200);

        TableColumn<Object, String> uDate = new TableColumn<>("Registration Date");
        uDate.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        uDate.setPrefWidth(200);


        tblData.getColumns().add(uId);
        tblData.getColumns().add(uUser);
        tblData.getColumns().add(uPass);
        tblData.getColumns().add(uType);
        tblData.getColumns().add(uIA);
        tblData.getColumns().add(uDate);

        USER_LIST.clear();
        for (UserDetail userDetail : userDetailDao.getUserDetailList()) {
            userDetail.setActivated(userDetail.isActivated() ? "YES":"NO");
            tblData.getItems().add(userDetail);
            USER_LIST.add(userDetail);
        }
    }

    private void clear() {
        tblData.getColumns().clear();
        tblData.getItems().clear();
    }

    private void subClear() {
        tblSubData.getColumns().clear();
        tblSubData.getItems().clear();
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

    @Override
    public void onAddUser() {
        clear();
        loadUser();
    }
}
