package io.erm.ees.controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import io.erm.ees.dao.*;
import io.erm.ees.dao.impl.CurriculumDaoImpl;
import io.erm.ees.dao.impl.DirtyDaoImpl;
import io.erm.ees.dao.impl.SectionDaoImpl;
import io.erm.ees.dao.impl.SuggestionDaoImpl;
import io.erm.ees.helper.DbFactory;
import io.erm.ees.helper.EvaluationHelper;
import io.erm.ees.helper.SectionHelper;
import io.erm.ees.model.Course;
import io.erm.ees.model.Section;
import io.erm.ees.model.Student;
import io.erm.ees.model.StudentSubjectRecord;
import io.erm.ees.model.recursive.Subject;
import io.erm.ees.model.v2.AcademicYear;
import io.erm.ees.model.v2.Record;
import io.erm.ees.model.v2.Remark;
import io.erm.ees.stage.EvaluationDropStage;
import io.erm.ees.util.ResourceHelper;
import io.erm.ees.util.document.AdvisingDoc;
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

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EvaluationDropController implements Initializable, AdvisingDoc.CreationListener {

    @FXML
    private JFXTreeTableView<Subject> tblAbSubject;

    @FXML
    private JFXTreeTableView<Subject> tblYeSubject;

    @FXML
    private JFXComboBox<String> cbAbSubject;

    @FXML
    private JFXComboBox<String> cbClass;

    @FXML
    private JFXTextField txStudentNo;

    @FXML
    private JFXTextField txCourse;

    @FXML
    private JFXTextField txAYS;

    @FXML
    private JFXTextField txYear;

    @FXML
    private JFXTextField txStatus;

    @FXML
    private JFXTextField txFullName;

    @FXML
    private Label lbAbUnit;

    @FXML
    private Label lbYeUnit;

    @FXML
    private Label lbexceed;

    @FXML
    private ImageView imgLoading;

    @FXML
    private VBox pnScreen;

    @FXML
    private Label lbEnrolledUnit;

    private final ObservableList<Subject> ENROLL_SUBJECT_LIST = FXCollections.observableArrayList();
    private final ObservableList<Subject> AVAILABLE_SUBJECT_LIST = FXCollections.observableArrayList();

    private final DirtyDao dirtyDao = new DirtyDaoImpl();
    private final CourseDao courseDao = DbFactory.courseFactory();
    private final SectionDao sectionDao = new SectionDaoImpl();
    private final CurriculumDao curriculumDao = new CurriculumDaoImpl();
    private final StudentDao studentDao = DbFactory.studentFactory();
    private final SuggestionDao suggestionDao = new SuggestionDaoImpl();
    private final AcademicYearDao academicYearDao = DbFactory.academicYearFactory();
    private final CreditSubjectDao creditSubjectDao = DbFactory.creditSubjectFactory();
    private final SubjectDao subjectDao = DbFactory.subjectFactory();

    private Student student;
    private Course course;
    private Section section;
    private AcademicYear academicYear;

    private final List<io.erm.ees.model.Subject> ENROLLED_LIST_REMOVE = new ArrayList<>();
    private final List<io.erm.ees.model.Subject> ENROLLED_LIST = new ArrayList<>();
    private final List<io.erm.ees.model.Subject> NOTSET_LIST = new ArrayList<>();

    private int totalAbUnit;
    private int totalYeUnit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lbexceed.setVisible(false);

        Image logoLoading = new Image(ResourceHelper.resourceWithBasePath("image/loading.gif").toString());
        imgLoading.setImage(logoLoading);
    }

    @FXML
    protected void onClickSave(ActionEvent event) {
        if (totalYeUnit >= 1 && totalYeUnit <= 30) {
            final int calYear = sectionDao.getSectionById(student.getSectionId()).getYear();
            final AcademicYear academicYear = academicYearDao.getAcademicYearOpen(course.getId(), calYear);

            for (Subject s : ENROLL_SUBJECT_LIST) {
                creditSubjectDao.deleteRecordByRemark(s.getId(), student.getId(), Remark.NOTSET.getCode());
            }
        } else if (totalYeUnit < 1) {
            Platform.runLater(() ->
                    JOptionPane.showMessageDialog(null, "Please add a subject to enroll."));
            return;
        } else {
            Platform.runLater(() ->
                    JOptionPane.showMessageDialog(null, "The limit of unit is exceeded."));
            return;
        }

        EvaluationDropStage stage = (EvaluationDropStage) ((Node) event.getSource()).getScene().getWindow();
        Platform.runLater(stage::close);
    }

    @FXML
    protected void onClickClose(ActionEvent event) {
        EvaluationDropStage stage = (EvaluationDropStage) ((Node) event.getSource()).getScene().getWindow();
        Platform.runLater(stage::close);
    }

    @FXML
    protected void onClickAdd() {
        final int index = tblAbSubject.getSelectionModel().getSelectedIndex();
        if (index > -1) {
            Subject subject = AVAILABLE_SUBJECT_LIST.get(index);

            //add to remove table
            addYeSubject(subject);

            //remove to drop subject list
            if (ENROLLED_LIST.size() > 0)
                removeToRemoveList(subject);
        }
    }

    @FXML
    protected void onClickAddAll() {
        if (AVAILABLE_SUBJECT_LIST.size() > 0)
            addYeSubjectAll();
    }

    @FXML
    protected void onClickRemove() {
        if (tblYeSubject.getSelectionModel().getSelectedIndex() > -1) {
            removeYeSubject();
        }
    }

    @FXML
    protected void onClickRemoveAll() {
        if (ENROLL_SUBJECT_LIST.size() > 0)
            removeAllYeSubject();
    }

    @Deprecated
    private void loadYeSubject(List<io.erm.ees.model.Subject> subjectList) {
        for (io.erm.ees.model.Subject subject : subjectList) {
            ENROLL_SUBJECT_LIST.add(new Subject(subject.getId(), subject.getName(), subject.getDesc(),
                    subject.getUnit(), subject.getUnitDisplay()));
            Platform.runLater(() -> {
                totalYeUnit+=subject.getUnit();
                lbYeUnit.setText(totalYeUnit + "");

                if (totalYeUnit < 1 || totalYeUnit > 30) {
                    lbYeUnit.setStyle("-fx-text-fill:#c0392b;");
                    if (totalYeUnit > 30)
                        lbexceed.setVisible(true);
                } else {
                    lbYeUnit.setStyle("-fx-text-fill:#27ae60;");
                    lbexceed.setVisible(false);
                }
            });
        }
        TreeItem<Subject> root = new RecursiveTreeItem<>(ENROLL_SUBJECT_LIST, RecursiveTreeObject::getChildren);
        Platform.runLater(() -> {
            JFXTreeTableColumn<Subject, Long> idCol = new JFXTreeTableColumn<>("ID");
            idCol.setResizable(false);
            idCol.setPrefWidth(75);
            idCol.setCellValueFactory(param -> param.getValue().getValue().idProperty().asObject());
            idCol.setSortable(false);

            JFXTreeTableColumn<Subject, String> nameCol = new JFXTreeTableColumn<>("Subject");
            nameCol.setResizable(false);
            nameCol.setPrefWidth(105);
            nameCol.setCellValueFactory(param -> param.getValue().getValue().nameProperty());
            nameCol.setSortable(false);

            JFXTreeTableColumn<Subject, String> descCol = new JFXTreeTableColumn<>("Description");
            descCol.setResizable(false);
            descCol.setPrefWidth(195);
            descCol.setCellValueFactory(param -> param.getValue().getValue().descProperty());
            descCol.setSortable(false);

            JFXTreeTableColumn<Subject, String> unitCol = new JFXTreeTableColumn<>("Unit");
            unitCol.setResizable(false);
            unitCol.setPrefWidth(75);
            unitCol.setCellValueFactory(param -> param.getValue().getValue().unitDisplayProperty());
            unitCol.setSortable(false);

            tblYeSubject.getColumns().add(idCol);
            tblYeSubject.getColumns().add(nameCol);
            tblYeSubject.getColumns().add(descCol);
            tblYeSubject.getColumns().add(unitCol);

            tblYeSubject.setRoot(root);
            tblYeSubject.setShowRoot(false);
        });
    }

    private void addYeSubject(Subject subject) {
        for (Subject s : ENROLL_SUBJECT_LIST) {
            if (s.getId() == subject.getId())
                return;
        }
        ENROLL_SUBJECT_LIST.add(new Subject(subject.getId(), subject.getName(), subject.getDesc(),
                subject.getUnit(), subject.getUnitDisplay()));
        Platform.runLater(() -> {
            TreeItem<Subject> root = new RecursiveTreeItem<>(ENROLL_SUBJECT_LIST, RecursiveTreeObject::getChildren);

            totalYeUnit += subject.getUnit();
            lbYeUnit.setText(totalYeUnit + "");

            if (totalYeUnit < 1 || totalYeUnit > 30) {
                lbYeUnit.setStyle("-fx-text-fill:#c0392b;");
                if (totalYeUnit > 30)
                    lbexceed.setVisible(true);

            } else {
                lbYeUnit.setStyle("-fx-text-fill:#27ae60;");
                lbexceed.setVisible(false);
            }

            tblYeSubject.setRoot(root);
            tblYeSubject.setShowRoot(false);
        });
    }

    private void addYeSubjectAll() {
        int size = AVAILABLE_SUBJECT_LIST.size();
        first:
        for (int i = 0; i < size; i++) {
            Subject subject = AVAILABLE_SUBJECT_LIST.get(i);
            for (Subject s : ENROLL_SUBJECT_LIST) {
                if (s.getId() == subject.getId())
                    continue first;
            }
            ENROLL_SUBJECT_LIST.add(new Subject(subject.getId(), subject.getName(), subject.getDesc(),
                    subject.getUnit(), subject.getUnitDisplay()));

            totalYeUnit+=subject.getUnit();
            lbYeUnit.setText(totalYeUnit + "");

            if (totalYeUnit < 1 || totalYeUnit > 30) {
                lbYeUnit.setStyle("-fx-text-fill:#c0392b;");
                if (totalYeUnit > 30)
                    lbexceed.setVisible(true);
            } else {
                lbYeUnit.setStyle("-fx-text-fill:#27ae60;");
                lbexceed.setVisible(false);
            }
        }
        TreeItem<Subject> root = new RecursiveTreeItem<Subject>(ENROLL_SUBJECT_LIST, RecursiveTreeObject::getChildren);

        tblYeSubject.setRoot(root);
        tblYeSubject.setShowRoot(false);

        //Remove from the table
        if (ENROLLED_LIST.size() > 0)
            removeToRemoveList(ENROLL_SUBJECT_LIST);
    }

    private void removeYeSubject() {
        Subject subject = ENROLL_SUBJECT_LIST.get(tblYeSubject.getSelectionModel().getSelectedIndex());

        totalYeUnit-=subject.getUnit();
        lbYeUnit.setText(totalYeUnit + "");

        if (totalYeUnit < 1 || totalYeUnit > 30) {
            lbYeUnit.setStyle("-fx-text-fill:#c0392b;");
            if (totalYeUnit > 30)
                lbexceed.setVisible(true);
        } else {
            lbYeUnit.setStyle("-fx-text-fill:#27ae60;");
            lbexceed.setVisible(false);
        }

        ENROLL_SUBJECT_LIST.remove(subject);
        TreeItem<Subject> root = new RecursiveTreeItem<>(ENROLL_SUBJECT_LIST, RecursiveTreeObject::getChildren);

        tblYeSubject.setRoot(root);
        tblYeSubject.setShowRoot(false);

        if (ENROLLED_LIST.size() > 0)
            addToRemoveList(subject);
    }

    private void removeAllYeSubject() {

        final List<Subject> templist = new ArrayList<>();

        //reset
        totalYeUnit=0;
        lbYeUnit.setText(totalYeUnit + "");

        lbYeUnit.setStyle("-fx-text-fill:#c0392b;");
        lbexceed.setVisible(false);

        while (ENROLL_SUBJECT_LIST.size() > 0) {
            templist.add(ENROLL_SUBJECT_LIST.get(0));
            ENROLL_SUBJECT_LIST.remove(0);
        }
        TreeItem<Subject> root = new RecursiveTreeItem<Subject>(ENROLL_SUBJECT_LIST, RecursiveTreeObject::getChildren);

        tblYeSubject.setRoot(root);
        tblYeSubject.setShowRoot(false);

        if (ENROLLED_LIST.size() > 0)
            addToRemoveList(templist);
    }

    private void clearAb() {
        Platform.runLater(() -> {
            tblAbSubject.getColumns().clear();
            tblAbSubject.setRoot(null);
        });
    }

    private void clearYe() {
        Platform.runLater(() -> {
            tblYeSubject.getColumns().clear();
            tblYeSubject.setRoot(null);
        });
    }

    public void listener(Student student) {
        new Thread(() -> {
            this.student = student;
            course = courseDao.getCourseById(student.getCourseId());
            section = sectionDao.getSectionById(student.getSectionId());
            academicYear = academicYearDao.getAcademicYearOpen(course.getId());

            final int semester = academicYear.getSemester();
            final int currentYear=section.getYear();
            NOTSET_LIST.clear();
            for(Record record : creditSubjectDao.getRecordListByMark(student.getId(), Remark.NOTSET.getCode())) {
                io.erm.ees.model.Subject subject = subjectDao.getSubjectById(record.getSubjectId());
                NOTSET_LIST.add(subject);
            }

            Platform.runLater(() -> {
                clearAb();
                initAbSubject();

                lbYeUnit.setText(totalYeUnit+"");
                txAYS.setText(academicYear.getName() + "  ( " + (academicYear.getSemester() == 1 ? "1ST SEMESTER )" :
                        academicYear.getSemester() == 2 ? "2ND SEMESTER )" : "0/3RD SEMESTER )"));

                txStudentNo.setText(student.getStudentNumber() + "");
                txCourse.setText(courseDao.getCourseById(student.getCourseId()).getDesc());
                txFullName.setText(String.format("%s, %s %s.", student.getLastName(), student.getFirstName(),
                        student.getMiddleName().substring(0, 1)).toUpperCase());
                txYear.setText(SectionHelper.format(currentYear));
                txStatus.setText(student.getStatus());


                loadYeSubject(new ArrayList<>());
                loadAbItem(NOTSET_LIST);
            });
        }).start();
    }

    private void addToRemoveList(Subject subject) {
        final List<StudentSubjectRecord> recordList = dirtyDao.getStudentSubjectRecordByMark(student.getId(), "ONGOING");

        for (StudentSubjectRecord record : recordList) {
            if (subject.getId() == record.getSubjectId()) {
                ENROLLED_LIST_REMOVE.add(new io.erm.ees.model.Subject(subject.getId(), subject.getName(),
                        subject.getDesc(), subject.getUnit()));
                break;
            }
        }

        showLoading();
        Platform.runLater(() -> cbAbSubject.getSelectionModel().select("DROP"));
        if (cbAbSubject.getSelectionModel().getSelectedItem().equalsIgnoreCase("DROP")) {
            new Thread(() -> {
                List<io.erm.ees.model.Subject> list = new ArrayList<>();

                //Remove list
                loadRemoveList(list);

                loadAbItem(list);
                hideLoading();
            }).start();
        }
    }

    private void addToRemoveList(List<Subject> subjects) {
        final List<StudentSubjectRecord> recordList = dirtyDao.getStudentSubjectRecordByMark(student.getId(), "ONGOING");

        for (Subject subject : subjects) {
            child:
            for (StudentSubjectRecord record : recordList) {
                if (subject.getId() == record.getSubjectId()) {
                    ENROLLED_LIST_REMOVE.add(new io.erm.ees.model.Subject(subject.getId(), subject.getName(),
                            subject.getDesc(), subject.getUnit()));
                    break child;
                }
            }
        }

        showLoading();
        Platform.runLater(() -> cbAbSubject.getSelectionModel().select("DROP"));
        if (cbAbSubject.getSelectionModel().getSelectedItem().equalsIgnoreCase("DROP")) {
            new Thread(() -> {
                List<io.erm.ees.model.Subject> list = new ArrayList<>();

                //Remove list
                loadRemoveList(list);

                loadAbItem(list);
                hideLoading();
            }).start();
        }
    }

    private void removeToRemoveList(Subject subject) {
        final List<io.erm.ees.model.Subject> subjectList = new ArrayList<>();
        final List<StudentSubjectRecord> recordList = dirtyDao.getStudentSubjectRecordByMark(student.getId(), "ONGOING");

        for (StudentSubjectRecord record : recordList) {
            if (subject.getId() == record.getSubjectId()) {
                inner:
                for (io.erm.ees.model.Subject s : ENROLLED_LIST_REMOVE) {
                    if (s.getId() == subject.getId()) {
                        ENROLLED_LIST_REMOVE.remove(s);
                        break inner;
                    }
                }
                break;
            }
        }

        if (cbAbSubject.getSelectionModel().getSelectedItem().equalsIgnoreCase("DROP")) {
            showLoading();
            new Thread(() -> {
                List<io.erm.ees.model.Subject> list = new ArrayList<>();

                //Remove list
                loadRemoveList(list);

                loadAbItem(list);
                hideLoading();
            }).start();
        }
    }

    private void removeToRemoveList(List<Subject> subjects) {
        final List<io.erm.ees.model.Subject> subjectList = new ArrayList<>();
        final List<StudentSubjectRecord> recordList = dirtyDao.getStudentSubjectRecordByMark(student.getId(), "ONGOING");

        for (Subject subject : subjects) {
            child1:
            for (StudentSubjectRecord record : recordList) {
                if (subject.getId() == record.getSubjectId()) {
                    child2:
                    for (io.erm.ees.model.Subject s : ENROLLED_LIST_REMOVE) {
                        if (s.getId() == subject.getId()) {
                            ENROLLED_LIST_REMOVE.remove(s);
                            break child2;
                        }
                    }
                    break child1;
                }
            }
        }

        if (cbAbSubject.getSelectionModel().getSelectedItem().equalsIgnoreCase("DROP")) {
            showLoading();
            new Thread(() -> {
                List<io.erm.ees.model.Subject> list = new ArrayList<>();

                //Remove list
                loadRemoveList(list);

                loadAbItem(list);
                hideLoading();
            }).start();
        }
    }

    private void loadRemoveList(List<io.erm.ees.model.Subject> list) {
        list.addAll(ENROLLED_LIST_REMOVE);
    }

    private void showLoading() {
        Platform.runLater(() -> pnScreen.setVisible(true));
    }

    private void hideLoading() {
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> pnScreen.setVisible(false));
    }

    private void loadAbItem(List<io.erm.ees.model.Subject> subjectList) {
        //reset the total unit
        totalAbUnit = 0;

        //reset the list
        while (AVAILABLE_SUBJECT_LIST.size() > 0)
            AVAILABLE_SUBJECT_LIST.remove(0);

        for (io.erm.ees.model.Subject subject : subjectList) {
            AVAILABLE_SUBJECT_LIST.add(new Subject(subject.getId(), subject.getName(), subject.getDesc(),
                    subject.getUnit(), subject.getUnitDisplay()));
            totalAbUnit += subject.getUnit();
        }
        Platform.runLater(() -> {
            TreeItem<Subject> root = new RecursiveTreeItem<>(AVAILABLE_SUBJECT_LIST, RecursiveTreeObject::getChildren);
            lbAbUnit.setText(totalAbUnit + "");
            tblAbSubject.setRoot(root);
            tblAbSubject.setShowRoot(false);
        });
    }

    private void initAbSubject() {
        Platform.runLater(() -> {
            JFXTreeTableColumn<Subject, Long> idCol = new JFXTreeTableColumn<>("ID");
            idCol.setResizable(false);
            idCol.setPrefWidth(75);
            idCol.setCellValueFactory(param -> param.getValue().getValue().idProperty().asObject());
            idCol.setSortable(false);

            JFXTreeTableColumn<Subject, String> nameCol = new JFXTreeTableColumn<>("Subject");
            nameCol.setResizable(false);
            nameCol.setPrefWidth(105);
            nameCol.setCellValueFactory(param -> param.getValue().getValue().nameProperty());
            nameCol.setSortable(false);

            JFXTreeTableColumn<Subject, String> descCol = new JFXTreeTableColumn<>("Description");
            descCol.setResizable(false);
            descCol.setPrefWidth(195);
            descCol.setCellValueFactory(param -> param.getValue().getValue().descProperty());
            descCol.setSortable(false);

            JFXTreeTableColumn<Subject, String> unitCol = new JFXTreeTableColumn<>("Unit");
            unitCol.setResizable(false);
            unitCol.setPrefWidth(75);
            unitCol.setCellValueFactory(param -> param.getValue().getValue().unitDisplayProperty());
            unitCol.setSortable(false);

            tblAbSubject.getColumns().add(idCol);
            tblAbSubject.getColumns().add(nameCol);
            tblAbSubject.getColumns().add(descCol);
            tblAbSubject.getColumns().add(unitCol);

            tblAbSubject.setRoot(null);
            tblAbSubject.setShowRoot(false);
        });
    }

    @Override
    public void onError() {
        JOptionPane.showMessageDialog(null, "Failed to create report. " +
                "The file might open on other program");
    }
}
