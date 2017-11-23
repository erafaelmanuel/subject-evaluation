package io.ermdev.ees.controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import io.ermdev.ees.dao.*;
import io.ermdev.ees.dao.impl.CurriculumDaoImpl;
import io.ermdev.ees.dao.impl.DirtyDaoImpl;
import io.ermdev.ees.dao.impl.SectionDaoImpl;
import io.ermdev.ees.dao.impl.SuggestionDaoImpl;
import io.ermdev.ees.helper.DbFactory;
import io.ermdev.ees.model.Course;
import io.ermdev.ees.model.Curriculum;
import io.ermdev.ees.model.Student;
import io.ermdev.ees.model.StudentSubjectRecord;
import io.ermdev.ees.stage.AdvisingFormStage;
import io.ermdev.ees.stage.EnrollmentStage;
import io.ermdev.ees.util.AssessmentHelper;
import io.ermdev.ees.util.ResourceHelper;
import io.ermdev.ees.util.document.AdvisingDoc;
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
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

@Deprecated
public class DropSubjectController implements Initializable, AdvisingDoc.CreationListener {

    @FXML
    private JFXTreeTableView<io.ermdev.ees.model.recursive.Subject> tblAbSubject;

    @FXML
    private JFXTreeTableView<io.ermdev.ees.model.recursive.Subject> tblYeSubject;

    @FXML
    private JFXComboBox<String> cbAbSubject;

    @FXML
    private JFXComboBox<String> cbClass;

    @FXML
    private JFXComboBox<String> cbMaxYear;

    @FXML
    private ImageView imgLogo;

    @FXML
    private Label lbStudentNo;

    @FXML
    private Label lbCourse;

    @FXML
    private JFXTextField txYear;

    @FXML
    private JFXTextField txStatus;

    @FXML
    private JFXTextField txFullName;

    @FXML
    private JFXComboBox<String> cbCurSemester;

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

    private final ObservableList<io.ermdev.ees.model.recursive.Subject> ENROLL_SUBJECT_LIST = FXCollections.observableArrayList();
    private final ObservableList<io.ermdev.ees.model.recursive.Subject> AVAILABLE_SUBJECT_LIST = FXCollections.observableArrayList();

    private final DirtyDao dirtyDao = new DirtyDaoImpl();
    private final CourseDao courseDao = DbFactory.courseFactory();
    private final SectionDao sectionDao = new SectionDaoImpl();
    private final CurriculumDao curriculumDao = new CurriculumDaoImpl();
    private final StudentDao studentDao = DbFactory.studentFactory();
    private SuggestionDao suggestionDao = new SuggestionDaoImpl();

    private Student student;
    private Course course;

    private final List<io.ermdev.ees.model.Subject> ENROLLED_LIST_REMOVE = new ArrayList<>();
    private final List<io.ermdev.ees.model.Subject> ENROLLED_LIST = new ArrayList<>();

    private int totalAbUnit;
    private int totalYeUnit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lbexceed.setVisible(false);

        cbClass.getItems().add("REGULAR/IRREG CLASSES");
        cbClass.getItems().add("SUMMER CLASSES");
        cbClass.getSelectionModel().select(0);

        cbCurSemester.getItems().add("1ST SEMESTER");
        cbCurSemester.getItems().add("2ND SEMESTER");
        cbCurSemester.getSelectionModel().select(0);

        Image image = new Image(ResourceHelper.resourceWithBasePath("image/studentlogo.png").toString());
        imgLogo.setImage(image);

        Image logoLoading = new Image(ResourceHelper.resourceWithBasePath("image/loading.gif").toString());
        imgLoading.setImage(logoLoading);
    }

    @FXML
    protected void onClickEnroll(ActionEvent event) {

        //Delete the enrolled subject
        dirtyDao.deleteStudentRecord(student.getId(), "ONGOING");

        if (totalYeUnit >= 1 && totalYeUnit <= 30) {
            for (io.ermdev.ees.model.recursive.Subject s : ENROLL_SUBJECT_LIST) {
                StudentSubjectRecord record = new StudentSubjectRecord();
                record.setDate(new Date().toString());
                record.setMark("ONGOING");
                record.setSemester(cbCurSemester.getSelectionModel().getSelectedIndex() + 1);
                dirtyDao.addStudentRecord(record, s.getId(), student.getId());
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
        EnrollmentStage stage = (EnrollmentStage) ((Node) event.getSource()).getScene().getWindow();
        stage.setClose();

        new Thread(() -> {
            AdvisingDoc advisingDoc = new AdvisingDoc();
            advisingDoc.addSubject(ENROLL_SUBJECT_LIST);
            advisingDoc.setStudent(student);
            advisingDoc.setCreationListener(this);
            advisingDoc.create();
        }).start();

        new Thread(() -> Platform.runLater(() -> {
            AdvisingFormStage advisingFormStage = new AdvisingFormStage();
            Platform.runLater(() -> advisingFormStage.showAndWait());
            advisingFormStage.getController().listener(student, ENROLL_SUBJECT_LIST);
        })).start();
    }

    @FXML
    protected void onClickUndo() {
        showLoading();
        new Thread(() -> {
            while (ENROLL_SUBJECT_LIST.size() > 0)
                ENROLL_SUBJECT_LIST.remove(0);

            totalYeUnit = 0;
            ENROLLED_LIST_REMOVE.clear();

            for (io.ermdev.ees.model.Subject subject : ENROLLED_LIST) {
                ENROLL_SUBJECT_LIST.add(new io.ermdev.ees.model.recursive.Subject(subject.getId(), subject.getName(), subject.getDesc(), subject.getUnit()));
                Platform.runLater(() -> {
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
                });
            }
            TreeItem<io.ermdev.ees.model.recursive.Subject> root = new RecursiveTreeItem<>(ENROLL_SUBJECT_LIST, RecursiveTreeObject::getChildren);
            Platform.runLater(() -> {
                tblYeSubject.setRoot(root);
                tblYeSubject.setShowRoot(false);
            });

            Platform.runLater(() -> cbAbSubject.getSelectionModel().select("DROP"));
            if (cbAbSubject.getSelectionModel().getSelectedItem().equalsIgnoreCase("DROP")) {
                new Thread(() -> {
                    List<io.ermdev.ees.model.Subject> list = new ArrayList<>();
                    loadAbItem(list);
                }).start();
            }
            hideLoading();
        }).start();
    }

    @FXML
    protected void onClickRefresh() {
        /** TODO **/
    }

    @FXML
    protected void onChooseFilter() {
        showLoading();
        if (cbAbSubject.getSelectionModel().getSelectedItem().equalsIgnoreCase("DROP")) {
            new Thread(() -> {
                List<io.ermdev.ees.model.Subject> list = new ArrayList<>();

                //Remove list
                loadRemoveList(list);

                loadAbItem(list);
                hideLoading();
            }).start();
        }else if(cbAbSubject.getSelectionModel().getSelectedItem().equalsIgnoreCase("SUGGEST")) {
            new Thread(() -> {
                List<io.ermdev.ees.model.Subject> list = suggestionDao.getSubjectListByStudent(student.getId());
                loadAbItem(list);
                hideLoading();
            }).start();
        }else if (cbAbSubject.getSelectionModel().getSelectedIndex() == 0) {
            new Thread(() -> {
                List<io.ermdev.ees.model.Subject> list = AssessmentHelper.getSubjectListWithFilter(student,
                        cbCurSemester.getSelectionModel().getSelectedIndex() + 1, cbMaxYear.getSelectionModel().getSelectedIndex() + 1);
                if (cbCurSemester.getSelectionModel().getSelectedIndex() == 1)
                    list.addAll(AssessmentHelper.getSpecialSubjectListWithFilter(student, "SPECIAL_CLASSES", cbMaxYear.getSelectionModel().getSelectedIndex() + 1));

                loadAbItem(list);
                hideLoading();
            }).start();
        } else if (cbAbSubject.getSelectionModel().getSelectedIndex() <= course.getTotalYear()) {
            new Thread(() -> {
                List<io.ermdev.ees.model.Subject> list = AssessmentHelper.getSubjectListWithFilter(student,
                        cbAbSubject.getSelectionModel().getSelectedIndex(), cbCurSemester.getSelectionModel().getSelectedIndex() + 1,
                        cbMaxYear.getSelectionModel().getSelectedIndex() + 1);
                if (cbCurSemester.getSelectionModel().getSelectedIndex() == 1)
                    list.addAll(AssessmentHelper.getSpecialSubjectListWithFilter(student, cbAbSubject.getSelectionModel().getSelectedIndex(), "SPECIAL_CLASSES",
                            cbMaxYear.getSelectionModel().getSelectedIndex() + 1));
                loadAbItem(list);
                hideLoading();
            }).start();
        }
    }

    @FXML
    protected void onChooseSemester() {
        showLoading();
        if (cbAbSubject.getSelectionModel().getSelectedIndex() == 0) {
            new Thread(() -> {
                List<io.ermdev.ees.model.Subject> list = AssessmentHelper.getSubjectListWithFilter(student,
                        cbCurSemester.getSelectionModel().getSelectedIndex() + 1, cbMaxYear.getSelectionModel().getSelectedIndex() + 1);
                if (cbCurSemester.getSelectionModel().getSelectedIndex() == 1)
                    list.addAll(AssessmentHelper.getSpecialSubjectListWithFilter(student, "SPECIAL_CLASSES", cbMaxYear.getSelectionModel().getSelectedIndex() + 1));

                loadAbItem(list);
                hideLoading();
            }).start();
        }else if(cbAbSubject.getSelectionModel().getSelectedItem().equalsIgnoreCase("SUGGEST")) {
            new Thread(() -> {
                List<io.ermdev.ees.model.Subject> list = suggestionDao.getSubjectListByStudent(student.getId());
                loadAbItem(list);
                hideLoading();
            }).start();
        } else if (cbAbSubject.getSelectionModel().getSelectedIndex() <= course.getTotalYear()) {
            new Thread(() -> {
                List<io.ermdev.ees.model.Subject> list = AssessmentHelper.getSubjectListWithFilter(student,
                        cbAbSubject.getSelectionModel().getSelectedIndex(), cbCurSemester.getSelectionModel().getSelectedIndex() + 1,
                        cbMaxYear.getSelectionModel().getSelectedIndex() + 1);
                if (cbCurSemester.getSelectionModel().getSelectedIndex() == 1)
                    list.addAll(AssessmentHelper.getSpecialSubjectListWithFilter(student, cbAbSubject.getSelectionModel().getSelectedIndex(), "SPECIAL_CLASSES",
                            cbMaxYear.getSelectionModel().getSelectedIndex() + 1));
                loadAbItem(list);
                hideLoading();
            }).start();
        }
        if (ENROLLED_LIST.size() < 1) {
            removeAllYeSubject();
        }
    }

    @FXML
    private void onChooseMaxYear() {
        showLoading();
        if (cbAbSubject.getSelectionModel().getSelectedItem().equalsIgnoreCase("DROP")) {
            new Thread(() -> {
                List<io.ermdev.ees.model.Subject> list = new ArrayList<>();

                //Remove list
                loadRemoveList(list);

                loadAbItem(list);
                hideLoading();
            }).start();
        }else if(cbAbSubject.getSelectionModel().getSelectedItem().equalsIgnoreCase("SUGGEST")) {
            new Thread(() -> {
                List<io.ermdev.ees.model.Subject> list = suggestionDao.getSubjectListByStudent(student.getId());
                loadAbItem(list);
                hideLoading();
            }).start();
        } else if (cbAbSubject.getSelectionModel().getSelectedIndex() == 0) {
            new Thread(() -> {
                List<io.ermdev.ees.model.Subject> list = AssessmentHelper.getSubjectListWithFilter(student,
                        cbCurSemester.getSelectionModel().getSelectedIndex() + 1, cbMaxYear.getSelectionModel().getSelectedIndex() + 1);
                if (cbCurSemester.getSelectionModel().getSelectedIndex() == 1)
                    list.addAll(AssessmentHelper.getSpecialSubjectListWithFilter(student, "SPECIAL_CLASSES", cbMaxYear.getSelectionModel().getSelectedIndex() + 1));

                loadAbItem(list);
                hideLoading();
            }).start();
        } else if (cbAbSubject.getSelectionModel().getSelectedIndex() <= course.getTotalYear()) {
            new Thread(() -> {
                List<io.ermdev.ees.model.Subject> list = AssessmentHelper.getSubjectListWithFilter(student,
                        cbAbSubject.getSelectionModel().getSelectedIndex(), cbCurSemester.getSelectionModel().getSelectedIndex() + 1,
                        cbMaxYear.getSelectionModel().getSelectedIndex() + 1);
                if (cbCurSemester.getSelectionModel().getSelectedIndex() == 1)
                    list.addAll(AssessmentHelper.getSpecialSubjectListWithFilter(student, cbAbSubject.getSelectionModel().getSelectedIndex(), "SPECIAL_CLASSES",
                            cbMaxYear.getSelectionModel().getSelectedIndex() + 1));
                loadAbItem(list);
                hideLoading();
            }).start();
        }
    }

    @FXML
    protected void onClickAdd() {
        final int index = tblAbSubject.getSelectionModel().getSelectedIndex();
        if (index > -1) {
            io.ermdev.ees.model.recursive.Subject subject = AVAILABLE_SUBJECT_LIST.get(index);

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
    private void loadYeSubject(List<io.ermdev.ees.model.Subject> subjectList) {
        for (io.ermdev.ees.model.Subject subject : subjectList) {
            ENROLL_SUBJECT_LIST.add(new io.ermdev.ees.model.recursive.Subject(subject.getId(), subject.getName(), subject.getDesc(),
                    subject.getUnit(), subject.getUnitDisplay()));
            Platform.runLater(() -> {
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
            });
        }
        TreeItem<io.ermdev.ees.model.recursive.Subject> root = new RecursiveTreeItem<>(ENROLL_SUBJECT_LIST, RecursiveTreeObject::getChildren);
        Platform.runLater(() -> {
            JFXTreeTableColumn<io.ermdev.ees.model.recursive.Subject, Long> idCol = new JFXTreeTableColumn<>("ID");
            idCol.setResizable(false);
            idCol.setPrefWidth(80);
            idCol.setCellValueFactory(param -> param.getValue().getValue().idProperty().asObject());
            idCol.setSortable(false);

            JFXTreeTableColumn<io.ermdev.ees.model.recursive.Subject, String> nameCol = new JFXTreeTableColumn<>("Subject");
            nameCol.setResizable(false);
            nameCol.setPrefWidth(130);
            nameCol.setCellValueFactory(param -> param.getValue().getValue().nameProperty());
            nameCol.setSortable(false);

            JFXTreeTableColumn<io.ermdev.ees.model.recursive.Subject, String> descCol = new JFXTreeTableColumn<>("Description");
            descCol.setResizable(false);
            descCol.setPrefWidth(210);
            descCol.setCellValueFactory(param -> param.getValue().getValue().descProperty());
            descCol.setSortable(false);

            JFXTreeTableColumn<io.ermdev.ees.model.recursive.Subject, String> unitCol = new JFXTreeTableColumn<>("Unit");
            unitCol.setResizable(false);
            unitCol.setPrefWidth(80);
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

    public void addYeSubject(io.ermdev.ees.model.recursive.Subject subject) {
        for (io.ermdev.ees.model.recursive.Subject s : ENROLL_SUBJECT_LIST) {
            if (s.getId() == subject.getId())
                return;
        }
        ENROLL_SUBJECT_LIST.add(new io.ermdev.ees.model.recursive.Subject(subject.getId(), subject.getName(), subject.getDesc(),
                subject.getUnit(), subject.getUnitDisplay()));
        Platform.runLater(() -> {
            TreeItem<io.ermdev.ees.model.recursive.Subject> root = new RecursiveTreeItem<>(ENROLL_SUBJECT_LIST, RecursiveTreeObject::getChildren);

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

    public void addYeSubjectAll() {
        int size = AVAILABLE_SUBJECT_LIST.size();
        first:
        for (int i = 0; i < size; i++) {
            io.ermdev.ees.model.recursive.Subject subject = AVAILABLE_SUBJECT_LIST.get(i);
            for (io.ermdev.ees.model.recursive.Subject s : ENROLL_SUBJECT_LIST) {
                if (s.getId() == subject.getId())
                    continue first;
            }
            ENROLL_SUBJECT_LIST.add(new io.ermdev.ees.model.recursive.Subject(subject.getId(), subject.getName(), subject.getDesc(),
                    subject.getUnit(), subject.getUnitDisplay()));

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
        }
        TreeItem<io.ermdev.ees.model.recursive.Subject> root = new RecursiveTreeItem<io.ermdev.ees.model.recursive.Subject>(ENROLL_SUBJECT_LIST, RecursiveTreeObject::getChildren);

        tblYeSubject.setRoot(root);
        tblYeSubject.setShowRoot(false);

        //Remove from the table
        if (ENROLLED_LIST.size() > 0)
            removeToRemoveList(ENROLL_SUBJECT_LIST);
    }

    public void removeYeSubject() {
        io.ermdev.ees.model.recursive.Subject subject = ENROLL_SUBJECT_LIST.get(tblYeSubject.getSelectionModel().getSelectedIndex());

        totalYeUnit -= subject.getUnit();
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
        TreeItem<io.ermdev.ees.model.recursive.Subject> root = new RecursiveTreeItem<>(ENROLL_SUBJECT_LIST, RecursiveTreeObject::getChildren);

        tblYeSubject.setRoot(root);
        tblYeSubject.setShowRoot(false);

        if (ENROLLED_LIST.size() > 0)
            addToRemoveList(subject);
    }

    public void removeAllYeSubject() {

        final List<io.ermdev.ees.model.recursive.Subject> templist = new ArrayList<>();

        //reset
        totalYeUnit = 0;
        lbYeUnit.setText(totalYeUnit + "");
        lbYeUnit.setStyle("-fx-text-fill:#c0392b;");
        lbexceed.setVisible(false);

        while (ENROLL_SUBJECT_LIST.size() > 0) {
            templist.add(ENROLL_SUBJECT_LIST.get(0));
            ENROLL_SUBJECT_LIST.remove(0);
        }
        TreeItem<io.ermdev.ees.model.recursive.Subject> root = new RecursiveTreeItem<io.ermdev.ees.model.recursive.Subject>(ENROLL_SUBJECT_LIST, RecursiveTreeObject::getChildren);

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

            List<Curriculum> curriculumList = curriculumDao.getCurriculumListByCourseId(student.getCourseId());

            student.setStatus("REGULAR");
            studentDao.updateStudentById(student.getId(), student);

            root:
            for (Curriculum curriculum : curriculumList) {
                for (io.ermdev.ees.model.Subject subject : curriculumDao.getSubjectList(curriculum.getId())) {
                    StudentSubjectRecord record = dirtyDao.getStudentSubjectRecordById(student.getId(), subject.getId());
                    if (record == null)
                        continue;
                    else {
                        if (record.getMark().equalsIgnoreCase("FAILED") ||
                                record.getMark().equalsIgnoreCase("DROPPED")) {
                            student.setStatus("IRREGULAR");
                            studentDao.updateStudentById(student.getId(), student);
                            break root;
                        }
                    }
                }
            }

            Platform.runLater(() -> {
                lbStudentNo.setText(student.getStudentNumber() + "");
                lbCourse.setText(courseDao.getCourseById(student.getCourseId()).getDesc());
                txFullName.setText(String.format("%s, %s %s.", student.getLastName(), student.getFirstName(),
                        student.getMiddleName().substring(0, 1)).toUpperCase());
                txYear.setText(new SectionDaoImpl().getSectionById(student.getSectionId()).getYear() + "");
                txStatus.setText(student.getStatus());

                int totalYear = course.getTotalYear();

                if (totalYear <= 4) {
                    cbAbSubject.getItems().add("ALL");
                    cbAbSubject.getItems().add("1ST YR");
                    cbAbSubject.getItems().add("2ND YR");
                    cbAbSubject.getItems().add("3RD YR");
                    cbAbSubject.getItems().add("4TH YR");
                } else if (totalYear == 5) {
                    cbAbSubject.getItems().add("ALL");
                    cbAbSubject.getItems().add("1ST YR");
                    cbAbSubject.getItems().add("2ND YR");
                    cbAbSubject.getItems().add("3RD YR");
                    cbAbSubject.getItems().add("4TH YR");
                    cbAbSubject.getItems().add("5TH YR");
                } else {
                    cbAbSubject.getItems().add("ALL");
                    cbAbSubject.getItems().add("1ST YR");
                    cbAbSubject.getItems().add("2ND YR");
                }
                cbAbSubject.getSelectionModel().select(0);

                if (totalYear <= 4) {
                    cbMaxYear.getItems().add("1ST YR");
                    cbMaxYear.getItems().add("2ND YR");
                    cbMaxYear.getItems().add("3RD YR");
                    cbMaxYear.getItems().add("4TH YR");
                    cbMaxYear.getSelectionModel().select(3);
                } else if (totalYear == 5) {
                    cbMaxYear.getItems().add("1ST YR");
                    cbMaxYear.getItems().add("2ND YR");
                    cbMaxYear.getItems().add("3RD YR");
                    cbMaxYear.getItems().add("4TH YR");
                    cbMaxYear.getItems().add("5TH YR");
                    cbMaxYear.getSelectionModel().select(4);
                } else {
                    cbMaxYear.getItems().add("1ST YR");
                    cbMaxYear.getItems().add("2ND YR");
                    cbMaxYear.getSelectionModel().select(1);
                }
                int year = new SectionDaoImpl().getSectionById(student.getSectionId()).getYear();
                switch (year) {
                    case 5:
                        cbMaxYear.getSelectionModel().select(4);
                        break;
                    case 4:
                        cbMaxYear.getSelectionModel().select(3);
                        break;
                    case 3:
                        cbMaxYear.getSelectionModel().select(2);
                        break;
                    case 2:
                        cbMaxYear.getSelectionModel().select(1);
                        break;
                    default:
                        cbMaxYear.getSelectionModel().select(0);
                        break;
                }
            });

            List<io.ermdev.ees.model.Subject> list = AssessmentHelper.getSubjectListWithFilter(student,
                    cbCurSemester.getSelectionModel().getSelectedIndex() + 1, cbMaxYear.getSelectionModel().getSelectedIndex() + 1);
            if (cbCurSemester.getSelectionModel().getSelectedIndex() == 1)
                list.addAll(AssessmentHelper.getSpecialSubjectListWithFilter(student, "SPECIAL_CLASSES", cbMaxYear.getSelectionModel().getSelectedIndex() + 1));

            clearAb();
            initAbSubject(list);

            List<io.ermdev.ees.model.Subject> subjectList = new ArrayList<>();
            List<StudentSubjectRecord> recordList = dirtyDao.getStudentSubjectRecordByMark(student.getId(), "ONGOING");

            for (StudentSubjectRecord record : recordList) {
                io.ermdev.ees.model.Subject subject = DbFactory.subjectFactory().getSubjectById(record.getSubjectId());
                subjectList.add(subject);
            }

            if (subjectList.size() > 0) {
                Platform.runLater(() -> {
                    cbAbSubject.getItems().add("DROP");

                    cbCurSemester.getSelectionModel().select(dirtyDao.getStudentSubjectRecordSemester(student.getId(), "ONGOING") - 1);
                    cbCurSemester.setDisable(true);
                });
            }

            Platform.runLater(()->cbAbSubject.getItems().add("SUGGEST"));

            ENROLLED_LIST.addAll(subjectList);

            clearYe();
            loadYeSubject(subjectList);
        }).start();
    }

    public void addToRemoveList(io.ermdev.ees.model.recursive.Subject subject) {
        final List<StudentSubjectRecord> recordList = dirtyDao.getStudentSubjectRecordByMark(student.getId(), "ONGOING");

        for (StudentSubjectRecord record : recordList) {
            if (subject.getId() == record.getSubjectId()) {
                ENROLLED_LIST_REMOVE.add(new io.ermdev.ees.model.Subject(subject.getId(), subject.getName(),
                        subject.getDesc(), subject.getUnit()));
                break;
            }
        }

        showLoading();
        Platform.runLater(() -> cbAbSubject.getSelectionModel().select("DROP"));
        if (cbAbSubject.getSelectionModel().getSelectedItem().equalsIgnoreCase("DROP")) {
            new Thread(() -> {
                List<io.ermdev.ees.model.Subject> list = new ArrayList<>();

                //Remove list
                loadRemoveList(list);

                loadAbItem(list);
                hideLoading();
            }).start();
        }
    }

    public void addToRemoveList(List<io.ermdev.ees.model.recursive.Subject> subjects) {
        final List<StudentSubjectRecord> recordList = dirtyDao.getStudentSubjectRecordByMark(student.getId(), "ONGOING");

        for (io.ermdev.ees.model.recursive.Subject subject : subjects) {
            child:
            for (StudentSubjectRecord record : recordList) {
                if (subject.getId() == record.getSubjectId()) {
                    ENROLLED_LIST_REMOVE.add(new io.ermdev.ees.model.Subject(subject.getId(), subject.getName(),
                            subject.getDesc(), subject.getUnit()));
                    break child;
                }
            }
        }

        showLoading();
        Platform.runLater(() -> cbAbSubject.getSelectionModel().select("DROP"));
        if (cbAbSubject.getSelectionModel().getSelectedItem().equalsIgnoreCase("DROP")) {
            new Thread(() -> {
                List<io.ermdev.ees.model.Subject> list = new ArrayList<>();

                //Remove list
                loadRemoveList(list);

                loadAbItem(list);
                hideLoading();
            }).start();
        }
    }

    public void removeToRemoveList(io.ermdev.ees.model.recursive.Subject subject) {
        final List<io.ermdev.ees.model.Subject> subjectList = new ArrayList<>();
        final List<StudentSubjectRecord> recordList = dirtyDao.getStudentSubjectRecordByMark(student.getId(), "ONGOING");

        for (StudentSubjectRecord record : recordList) {
            if (subject.getId() == record.getSubjectId()) {
                inner:
                for (io.ermdev.ees.model.Subject s : ENROLLED_LIST_REMOVE) {
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
                List<io.ermdev.ees.model.Subject> list = new ArrayList<>();

                //Remove list
                loadRemoveList(list);

                loadAbItem(list);
                hideLoading();
            }).start();
        }
    }

    public void removeToRemoveList(List<io.ermdev.ees.model.recursive.Subject> subjects) {
        final List<io.ermdev.ees.model.Subject> subjectList = new ArrayList<>();
        final List<StudentSubjectRecord> recordList = dirtyDao.getStudentSubjectRecordByMark(student.getId(), "ONGOING");

        for (io.ermdev.ees.model.recursive.Subject subject : subjects) {
            child1:
            for (StudentSubjectRecord record : recordList) {
                if (subject.getId() == record.getSubjectId()) {
                    child2:
                    for (io.ermdev.ees.model.Subject s : ENROLLED_LIST_REMOVE) {
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
                List<io.ermdev.ees.model.Subject> list = new ArrayList<>();

                //Remove list
                loadRemoveList(list);

                loadAbItem(list);
                hideLoading();
            }).start();
        }
    }

    public void loadRemoveList(List<io.ermdev.ees.model.Subject> list) {
        list.addAll(ENROLLED_LIST_REMOVE);
    }

    public void showLoading() {
        Platform.runLater(() -> pnScreen.setVisible(true));
    }

    public void hideLoading() {
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> pnScreen.setVisible(false));
    }

    private void loadAbItem(List<io.ermdev.ees.model.Subject> subjectList) {

        //reset the total unit
        totalAbUnit = 0;

        //reset the list
        while (AVAILABLE_SUBJECT_LIST.size() > 0)
            AVAILABLE_SUBJECT_LIST.remove(0);

        for (io.ermdev.ees.model.Subject subject : subjectList) {
            AVAILABLE_SUBJECT_LIST.add(new io.ermdev.ees.model.recursive.Subject(subject.getId(), subject.getName(), subject.getDesc(),
                    subject.getUnit(), subject.getUnitDisplay()));
            totalAbUnit += subject.getUnit();
        }
        TreeItem<io.ermdev.ees.model.recursive.Subject> root = new RecursiveTreeItem<>(AVAILABLE_SUBJECT_LIST, RecursiveTreeObject::getChildren);

        Platform.runLater(() -> {
            lbAbUnit.setText(totalAbUnit + "");
            tblAbSubject.setRoot(root);
            tblAbSubject.setShowRoot(false);
        });
    }

    private void initAbSubject(List<io.ermdev.ees.model.Subject> subjectList) {

        //reset the total unit
        totalAbUnit = 0;

        //reset the list
        while (AVAILABLE_SUBJECT_LIST.size() > 0)
            AVAILABLE_SUBJECT_LIST.remove(0);

        for (io.ermdev.ees.model.Subject subject : subjectList) {
            AVAILABLE_SUBJECT_LIST.add(new io.ermdev.ees.model.recursive.Subject(subject.getId(), subject.getName(), subject.getDesc(),
                    subject.getUnit(), subject.getUnitDisplay()));
            totalAbUnit += subject.getUnit();
        }

        Platform.runLater(() -> {
            lbAbUnit.setText(totalAbUnit + "");
            TreeItem<io.ermdev.ees.model.recursive.Subject> root = new RecursiveTreeItem<>(AVAILABLE_SUBJECT_LIST, RecursiveTreeObject::getChildren);

            JFXTreeTableColumn<io.ermdev.ees.model.recursive.Subject, Long> idCol = new JFXTreeTableColumn<>("ID");
            idCol.setResizable(false);
            idCol.setPrefWidth(80);
            idCol.setCellValueFactory(param -> param.getValue().getValue().idProperty().asObject());
            idCol.setSortable(false);

            JFXTreeTableColumn<io.ermdev.ees.model.recursive.Subject, String> nameCol = new JFXTreeTableColumn<>("Subject");
            nameCol.setResizable(false);
            nameCol.setPrefWidth(130);
            nameCol.setCellValueFactory(param -> param.getValue().getValue().nameProperty());
            nameCol.setSortable(false);

            JFXTreeTableColumn<io.ermdev.ees.model.recursive.Subject, String> descCol = new JFXTreeTableColumn<>("Description");
            descCol.setResizable(false);
            descCol.setPrefWidth(210);
            descCol.setCellValueFactory(param -> param.getValue().getValue().descProperty());
            descCol.setSortable(false);

            JFXTreeTableColumn<io.ermdev.ees.model.recursive.Subject, String> unitCol = new JFXTreeTableColumn<>("Unit");
            unitCol.setResizable(false);
            unitCol.setPrefWidth(80);
            unitCol.setCellValueFactory(param -> param.getValue().getValue().unitDisplayProperty());
            unitCol.setSortable(false);

            tblAbSubject.getColumns().add(idCol);
            tblAbSubject.getColumns().add(nameCol);
            tblAbSubject.getColumns().add(descCol);
            tblAbSubject.getColumns().add(unitCol);

            tblAbSubject.setRoot(root);
            tblAbSubject.setShowRoot(false);
        });
    }

    @Override
    public void onError() {
        JOptionPane.showMessageDialog(null, "Failed to create report. " +
                "The file might open on other program");
    }
}