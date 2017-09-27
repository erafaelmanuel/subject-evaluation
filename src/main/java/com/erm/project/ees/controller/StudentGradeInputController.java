package com.erm.project.ees.controller;

import com.erm.project.ees.dao.CourseDao;
import com.erm.project.ees.dao.DirtyDao;
import com.erm.project.ees.dao.SubjectDao;
import com.erm.project.ees.dao.impl.CourseDaoImpl;
import com.erm.project.ees.dao.impl.DirtyDaoImpl;
import com.erm.project.ees.dao.impl.SubjectDaoImpl;
import com.erm.project.ees.model.Student;
import com.erm.project.ees.model.StudentSubjectRecord;
import com.erm.project.ees.model.Subject;
import com.erm.project.ees.model.recursive.Mark;
import com.erm.project.ees.stage.GradeInputStage;
import com.erm.project.ees.util.ResourceHelper;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StudentGradeInputController implements Initializable, GradeInputStage.OnItemAddListener {


    @FXML
    private Label lbSName;

    @FXML
    private Label lbSNumber;

    @FXML
    private Label lbCourse;

    @FXML
    private JFXComboBox<String> cbMark;

    @FXML
    private JFXTreeTableView<Mark> tblMark;

    @FXML
    private ImageView imgLogo;

    @FXML
    private ImageView imgLoading;

    @FXML
    private VBox pnScreen;

    @FXML
    private Label lbUnit;

    private final Student STUDENT = new Student();

    private final CourseDao courseDao = new CourseDaoImpl();
    private final DirtyDao dirtyDao = new DirtyDaoImpl();
    private final SubjectDao subjectDao = new SubjectDaoImpl();

    private final GradeInputStage gradeInputStage = new GradeInputStage();

    private final ObservableList<String> MARK_TYPE = FXCollections.observableArrayList();
    private final ObservableList<Mark> MARK_LIST = FXCollections.observableArrayList();

    private int totalUnit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbMark.setItems(MARK_TYPE);
        cbMark.getItems().add("ONGOING");
        cbMark.getItems().add("INCOMPLETE");
        cbMark.getItems().add("PASSED");
        cbMark.getItems().add("FAILED");
        cbMark.getItems().add("DROPPED");
        cbMark.getSelectionModel().select(0);

        Image image = new Image(ResourceHelper.resourceWithBasePath("image/studentlogo.png").toString());
        imgLogo.setImage(image);

        Image logoLoading = new Image(ResourceHelper.resourceWithBasePath("image/loading.gif").toString());
        imgLoading.setImage(logoLoading);

        initTable();

        gradeInputStage.setListener(this);
    }

    @FXML
    protected void onChooseMark() {
        if(cbMark.getSelectionModel().getSelectedIndex() > -1) {
            totalUnit = 0;
            List<StudentSubjectRecord> recordList = dirtyDao.getStudentSubjectRecordByMark(STUDENT.getId(),
                    cbMark.getSelectionModel().getSelectedItem());

            MARK_LIST.clear();

            for(StudentSubjectRecord record : recordList) {
                Subject subject = subjectDao.getSubjectById(record.getSubjectId());
                Mark mark = new Mark(subject.getId(), subject.getName(), subject.getDesc(), record.getMidterm(),
                        record.getFinalterm(), record.getMark());
                totalUnit += subject.getUnit();
                MARK_LIST.add(mark);
            }

            lbUnit.setText(totalUnit + "");
            if(totalUnit == 0)
                lbUnit.setStyle("-fx-text-fill:#c0392b");
            else
                lbUnit.setStyle("-fx-text-fill:#16a085");

            TreeItem<Mark> root = new RecursiveTreeItem<>(MARK_LIST, RecursiveTreeObject::getChildren);
            tblMark.setRoot(root);
            tblMark.setShowRoot(false);
        }
    }

    @FXML
    protected void onClickInput() {
        final int index = tblMark.getSelectionModel().getSelectedIndex();
        if(index > -1) {
            Mark mark = MARK_LIST.get(index);
            Platform.runLater(()->gradeInputStage.showAndWait());
            gradeInputStage.getController().listening(mark, STUDENT.getId());
        }
    }

    @FXML
    protected void onClickDrop() {
        final int index = tblMark.getSelectionModel().getSelectedIndex();
        if(index > -1) {
            dirtyDao.updateStudentRecord(MARK_LIST.get(index).getSubjectId(),
                    STUDENT.getId(), 0, 0, "DROPPED");

            onAddMark();
        }
    }

    @FXML
    protected void onClickDone(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void initTable() {

        TreeItem<Mark> root = new RecursiveTreeItem<>(MARK_LIST, RecursiveTreeObject::getChildren);

        Platform.runLater(() -> {
            JFXTreeTableColumn<Mark, Long> idCol = new JFXTreeTableColumn<>("ID");
            idCol.setResizable(false);
            idCol.setPrefWidth(80);
            idCol.setCellValueFactory(param -> param.getValue().getValue().subjectIdProperty().asObject());

            JFXTreeTableColumn<Mark, String> nameCol = new JFXTreeTableColumn<>("Subject");
            nameCol.setResizable(false);
            nameCol.setPrefWidth(130);
            nameCol.setCellValueFactory(param -> param.getValue().getValue().subjectNameProperty());

            JFXTreeTableColumn<Mark, String> descCol = new JFXTreeTableColumn<>("Description");
            descCol.setResizable(false);
            descCol.setPrefWidth(210);
            descCol.setCellValueFactory(param -> param.getValue().getValue().subjectDescProperty());

            JFXTreeTableColumn<Mark, Double> midCol = new JFXTreeTableColumn<>("Midterm");
            midCol.setResizable(false);
            midCol.setPrefWidth(80);
            midCol.setCellValueFactory(param -> param.getValue().getValue().midtermProperty().asObject());

            JFXTreeTableColumn<Mark, Double> finalCol = new JFXTreeTableColumn<>("Finalterm");
            finalCol.setResizable(false);
            finalCol.setPrefWidth(80);
            finalCol.setCellValueFactory(param -> param.getValue().getValue().finaltermProperty().asObject());

            JFXTreeTableColumn<Mark, String> markCol = new JFXTreeTableColumn<>("Mark");
            markCol.setResizable(false);
            markCol.setPrefWidth(100);
            markCol.setCellValueFactory(param -> param.getValue().getValue().markProperty());

            tblMark.getColumns().add(idCol);
            tblMark.getColumns().add(nameCol);
            tblMark.getColumns().add(descCol);
            tblMark.getColumns().add(midCol);
            tblMark.getColumns().add(finalCol);
            tblMark.getColumns().add(markCol);

            tblMark.setRoot(root);
            tblMark.setShowRoot(false);
        });
    }

    public void listening(Student student) {
        STUDENT.setId(student.getId());
        STUDENT.setStudentNumber(student.getStudentNumber());
        STUDENT.setFirstName(student.getFirstName());
        STUDENT.setLastName(student.getLastName());
        STUDENT.setMiddleName(student.getMiddleName());
        STUDENT.setAge(student.getAge());
        STUDENT.setGender(student.getGender());
        STUDENT.setContactNumber(student.getContactNumber());
        STUDENT.setCourseId(student.getCourseId());
        STUDENT.setSectionId(student.getSectionId());
        STUDENT.setStatus(student.getStatus());

        lbSName.setText(String.format("%s, %s %s", STUDENT.getLastName(), STUDENT.getFirstName(),
                STUDENT.getMiddleName()));
        lbSNumber.setText(STUDENT.getStudentNumber() +"");
        lbCourse.setText(courseDao.getCourseById(STUDENT.getCourseId()).getDesc());

        List<StudentSubjectRecord> recordList = dirtyDao.getStudentSubjectRecordByMark(STUDENT.getId(),
                "ONGOING");
        MARK_LIST.clear();
        for(StudentSubjectRecord record : recordList) {
            Subject subject = subjectDao.getSubjectById(record.getSubjectId());
            Mark mark = new Mark(subject.getId(), subject.getName(), subject.getDesc(), record.getMidterm(),
                    record.getFinalterm(), record.getMark());
            totalUnit += subject.getUnit();
            MARK_LIST.add(mark);
        }

        lbUnit.setText(totalUnit + "");
        if(totalUnit == 0)
            lbUnit.setStyle("-fx-text-fill:#c0392b");
        else
            lbUnit.setStyle("-fx-text-fill:#16a085");

        TreeItem<Mark> root = new RecursiveTreeItem<>(MARK_LIST, RecursiveTreeObject::getChildren);
        tblMark.setRoot(root);
        tblMark.setShowRoot(false);
    }

    @Override
    public void onAddMark() {
        new Thread(()-> {
            showLoading();
            totalUnit = 0;
            List<StudentSubjectRecord> recordList = dirtyDao.getStudentSubjectRecordByMark(STUDENT.getId(),
                    cbMark.getSelectionModel().getSelectedItem());

            MARK_LIST.clear();

            for (StudentSubjectRecord record : recordList) {
                Subject subject = subjectDao.getSubjectById(record.getSubjectId());
                Mark mark = new Mark(subject.getId(), subject.getName(), subject.getDesc(), record.getMidterm(),
                        record.getFinalterm(), record.getMark());
                totalUnit += subject.getUnit();
                MARK_LIST.add(mark);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            TreeItem<Mark> root = new RecursiveTreeItem<>(MARK_LIST, RecursiveTreeObject::getChildren);
            Platform.runLater(()->{
                lbUnit.setText(totalUnit + "");
                if(totalUnit == 0)
                    lbUnit.setStyle("-fx-text-fill:#c0392b");
                else
                    lbUnit.setStyle("-fx-text-fill:#16a085");

                tblMark.setRoot(root);
                tblMark.setShowRoot(false);
            });
            hideLoading();
        }).start();
    }

    public void showLoading() {
        Platform.runLater(()-> pnScreen.setVisible(true));
    }

    public void hideLoading() {
        Platform.runLater(()-> pnScreen.setVisible(false));
    }
}
