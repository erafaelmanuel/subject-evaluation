package io.ermdev.ees.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import io.ermdev.ees.dao.CourseDao;
import io.ermdev.ees.dao.SectionDao;
import io.ermdev.ees.dao.StudentDao;
import io.ermdev.ees.dao.impl.SectionDaoImpl;
import io.ermdev.ees.helper.DbFactory;
import io.ermdev.ees.model.Section;
import io.ermdev.ees.model.recursive.Student;
import io.ermdev.ees.stage.StudentResultStage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StudentResultController implements Initializable {


    @FXML
    private JFXTreeTableView<Student> tblResult;

    @FXML
    private JFXButton btnConfirm;

    @FXML
    private Label lbNResult;

    private final ObservableList<Student> OBV_STUDENT_LIST = FXCollections.observableArrayList();

    private final CourseDao courseDao = DbFactory.courseFactory();
    private final SectionDao sectionDao = new SectionDaoImpl();
    private final StudentDao studentDao = DbFactory.studentFactory();

    @FXML
    protected void onClickItem() {
        if (tblResult.getSelectionModel().getSelectedIndex() > -1)
            btnConfirm.setDisable(false);
        else
            btnConfirm.setDisable(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnConfirm.setDisable(true);
        init();
    }

    public void listener(List<io.ermdev.ees.model.Student> studentList) {
        OBV_STUDENT_LIST.clear();
        loadResult(studentList);
    }

    private void loadResult(List<io.ermdev.ees.model.Student> studentList) {
        for (io.ermdev.ees.model.Student s : studentList) {
            final Section _section = sectionDao.getSectionById(s.getSectionId());
            String fname = String.format("%s, %s %s.",
                    s.getLastName(), s.getFirstName(), s.getMiddleName().substring(0, 1).toUpperCase());
            String course = courseDao.getCourseById(s.getCourseId()).getName();
            String section = _section.getYear() + "-" + _section.getName();
            OBV_STUDENT_LIST.add(new Student(s.getStudentNumber(), fname, course, section));
        }

        lbNResult.setText(OBV_STUDENT_LIST.size() + "");

        TreeItem<Student> root = new RecursiveTreeItem<>(OBV_STUDENT_LIST, RecursiveTreeObject::getChildren);
        tblResult.setRoot(root);
        tblResult.setShowRoot(false);
    }

    public void init() {
        JFXTreeTableColumn<Student, Long> numberCol = new JFXTreeTableColumn<>("#");
        numberCol.setResizable(false);
        numberCol.setPrefWidth(120);
        numberCol.setCellValueFactory(param -> param.getValue().getValue().numberProperty().asObject());
        numberCol.setSortable(false);

        JFXTreeTableColumn<Student, String> nameCol = new JFXTreeTableColumn<>("Name");
        nameCol.setResizable(false);
        nameCol.setPrefWidth(180);
        nameCol.setCellValueFactory(param -> param.getValue().getValue().fullNameProperty());
        nameCol.setSortable(false);

        JFXTreeTableColumn<Student, String> courseCol = new JFXTreeTableColumn<>("Course");
        courseCol.setResizable(false);
        courseCol.setPrefWidth(80);
        courseCol.setCellValueFactory(param -> param.getValue().getValue().courseProperty());
        courseCol.setSortable(false);

        JFXTreeTableColumn<Student, String> sectionCol = new JFXTreeTableColumn<>("Section");
        sectionCol.setResizable(false);
        sectionCol.setPrefWidth(70);
        sectionCol.setCellValueFactory(param -> param.getValue().getValue().sectionProperty());
        sectionCol.setSortable(false);

        tblResult.getColumns().add(numberCol);
        tblResult.getColumns().add(nameCol);
        tblResult.getColumns().add(courseCol);
        tblResult.getColumns().add(sectionCol);
    }

    @FXML
    protected void onClickConfirm(ActionEvent event) {
        StudentResultStage studentResultStage = (StudentResultStage) ((Node) event.getSource()).getScene().getWindow();
        final long id = OBV_STUDENT_LIST.get(tblResult.getSelectionModel().getFocusedIndex()).getNumber();
        io.ermdev.ees.model.Student student = studentDao.getStudentById(id);
        studentResultStage.callBack(student);
    }
}
