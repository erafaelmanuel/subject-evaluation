package com.erm.project.ees.controller;

import com.erm.project.ees.dao.*;
import com.erm.project.ees.dao.impl.*;
import com.erm.project.ees.model.Course;
import com.erm.project.ees.model.Curriculum;
import com.erm.project.ees.model.Student;
import com.erm.project.ees.model.StudentSubjectRecord;
import com.erm.project.ees.model.recursive.Subject;
import com.erm.project.ees.stage.AdvisingFormStage;
import com.erm.project.ees.util.AssessmentHelper;
import com.erm.project.ees.util.ResourceHelper;
import com.erm.project.ees.util.document.PDF;
import com.jfoenix.controls.*;
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

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class EnrollmentController implements Initializable {

    @FXML
    private JFXTreeTableView<Subject> tblAbSubject;

    @FXML
    private JFXTreeTableView<Subject> tblYeSubject;

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

    private final ObservableList<Subject> ENROLL_SUBJECT_LIST = FXCollections.observableArrayList();
    private final ObservableList<Subject> AVAILABLE_SUBJECT_LIST = FXCollections.observableArrayList();

    private final DirtyDao dirtyDao = new DirtyDaoImpl();
    private final CourseDao courseDao = new CourseDaoImpl();
    private final SectionDao sectionDao = new SectionDaoImpl();
    private final CurriculumDao curriculumDao = new CurriculumDaoImpl();
    private final StudentDao studentDao = new StudentDaoImpl();

    private Student student;
    private Course course;

    private final List<com.erm.project.ees.model.Subject> ENROLLED_LIST_REMOVE = new ArrayList<>();
    private final List<com.erm.project.ees.model.Subject> ENROLLED_LIST = new ArrayList<>();

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
            for (Subject s : ENROLL_SUBJECT_LIST) {
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
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        new Thread(() -> {
            PDF pdf = new PDF();
            pdf.setSubjectList(ENROLL_SUBJECT_LIST);
            pdf.setStudent(student);
            pdf.writeAndClose();
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

            for (com.erm.project.ees.model.Subject subject : ENROLLED_LIST) {
                ENROLL_SUBJECT_LIST.add(new Subject(subject.getId(), subject.getName(), subject.getDesc(), subject.getUnit()));
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
            TreeItem<Subject> root = new RecursiveTreeItem<>(ENROLL_SUBJECT_LIST, RecursiveTreeObject::getChildren);
            Platform.runLater(() -> {
                tblYeSubject.setRoot(root);
                tblYeSubject.setShowRoot(false);
            });

            Platform.runLater(() -> cbAbSubject.getSelectionModel().select("DROP"));
            if (cbAbSubject.getSelectionModel().getSelectedItem().equalsIgnoreCase("DROP")) {
                new Thread(() -> {
                    List<com.erm.project.ees.model.Subject> list = new ArrayList<>();
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
                List<com.erm.project.ees.model.Subject> list = new ArrayList<>();

                //Remove list
                loadRemoveList(list);

                loadAbItem(list);
                hideLoading();
            }).start();
        } else if (cbAbSubject.getSelectionModel().getSelectedIndex() == 0) {
            new Thread(() -> {
                List<com.erm.project.ees.model.Subject> list = AssessmentHelper.getSubjectListWithFilter(student,
                        cbCurSemester.getSelectionModel().getSelectedIndex() + 1, cbMaxYear.getSelectionModel().getSelectedIndex() + 1);
                if (cbCurSemester.getSelectionModel().getSelectedIndex() == 1)
                    list.addAll(AssessmentHelper.getSpecialSubjectListWithFilter(student, "SPECIAL_CLASSES", cbMaxYear.getSelectionModel().getSelectedIndex() + 1));

                loadAbItem(list);
                hideLoading();
            }).start();
        } else if (cbAbSubject.getSelectionModel().getSelectedIndex() <= course.getTotalYear()) {
            new Thread(() -> {
                List<com.erm.project.ees.model.Subject> list = AssessmentHelper.getSubjectListWithFilter(student,
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
                List<com.erm.project.ees.model.Subject> list = AssessmentHelper.getSubjectListWithFilter(student,
                        cbCurSemester.getSelectionModel().getSelectedIndex() + 1, cbMaxYear.getSelectionModel().getSelectedIndex() + 1);
                if (cbCurSemester.getSelectionModel().getSelectedIndex() == 1)
                    list.addAll(AssessmentHelper.getSpecialSubjectListWithFilter(student, "SPECIAL_CLASSES", cbMaxYear.getSelectionModel().getSelectedIndex() + 1));

                loadAbItem(list);
                hideLoading();
            }).start();
        } else if (cbAbSubject.getSelectionModel().getSelectedIndex() <= course.getTotalYear()) {
            new Thread(() -> {
                List<com.erm.project.ees.model.Subject> list = AssessmentHelper.getSubjectListWithFilter(student,
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
                List<com.erm.project.ees.model.Subject> list = new ArrayList<>();

                //Remove list
                loadRemoveList(list);

                loadAbItem(list);
                hideLoading();
            }).start();
        } else if (cbAbSubject.getSelectionModel().getSelectedIndex() == 0) {
            new Thread(() -> {
                List<com.erm.project.ees.model.Subject> list = AssessmentHelper.getSubjectListWithFilter(student,
                        cbCurSemester.getSelectionModel().getSelectedIndex() + 1, cbMaxYear.getSelectionModel().getSelectedIndex() + 1);
                if (cbCurSemester.getSelectionModel().getSelectedIndex() == 1)
                    list.addAll(AssessmentHelper.getSpecialSubjectListWithFilter(student, "SPECIAL_CLASSES", cbMaxYear.getSelectionModel().getSelectedIndex() + 1));

                loadAbItem(list);
                hideLoading();
            }).start();
        } else if (cbAbSubject.getSelectionModel().getSelectedIndex() <= course.getTotalYear()) {
            new Thread(() -> {
                List<com.erm.project.ees.model.Subject> list = AssessmentHelper.getSubjectListWithFilter(student,
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
        if (tblAbSubject.getSelectionModel().getSelectedIndex() > -1) {
            Subject subject = tblAbSubject.getSelectionModel().getSelectedItem().getValue();

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
    private void loadYeSubject(List<com.erm.project.ees.model.Subject> subjectList) {
        for (com.erm.project.ees.model.Subject subject : subjectList) {
            ENROLL_SUBJECT_LIST.add(new Subject(subject.getId(), subject.getName(), subject.getDesc(), subject.getUnit()));
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
        TreeItem<Subject> root = new RecursiveTreeItem<>(ENROLL_SUBJECT_LIST, RecursiveTreeObject::getChildren);
        Platform.runLater(() -> {
            JFXTreeTableColumn<Subject, Long> idCol = new JFXTreeTableColumn<>("ID");
            idCol.setResizable(false);
            idCol.setPrefWidth(80);
            idCol.setCellValueFactory(param -> param.getValue().getValue().idProperty().asObject());

            JFXTreeTableColumn<Subject, String> nameCol = new JFXTreeTableColumn<>("Subject");
            nameCol.setResizable(false);
            nameCol.setPrefWidth(130);
            nameCol.setCellValueFactory(param -> param.getValue().getValue().nameProperty());

            JFXTreeTableColumn<Subject, String> descCol = new JFXTreeTableColumn<>("Description");
            descCol.setResizable(false);
            descCol.setPrefWidth(210);
            descCol.setCellValueFactory(param -> param.getValue().getValue().descProperty());

            JFXTreeTableColumn<Subject, Integer> unitCol = new JFXTreeTableColumn<>("Unit");
            unitCol.setResizable(false);
            unitCol.setPrefWidth(80);
            unitCol.setCellValueFactory(param -> param.getValue().getValue().unitProperty().asObject());

            tblYeSubject.getColumns().add(idCol);
            tblYeSubject.getColumns().add(nameCol);
            tblYeSubject.getColumns().add(descCol);
            tblYeSubject.getColumns().add(unitCol);

            tblYeSubject.setRoot(root);
            tblYeSubject.setShowRoot(false);
        });
    }

    public void addYeSubject(Subject subject) {
        for (Subject s : ENROLL_SUBJECT_LIST) {
            if (s.getId() == subject.getId())
                return;
        }
        ENROLL_SUBJECT_LIST.add(new Subject(subject.getId(), subject.getName(), subject.getDesc(), subject.getUnit()));
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

    public void addYeSubjectAll() {
        int size = AVAILABLE_SUBJECT_LIST.size();
        first:
        for (int i = 0; i < size; i++) {
            Subject subject = AVAILABLE_SUBJECT_LIST.get(i);
            for (Subject s : ENROLL_SUBJECT_LIST) {
                if (s.getId() == subject.getId())
                    continue first;
            }
            ENROLL_SUBJECT_LIST.add(new Subject(subject.getId(), subject.getName(), subject.getDesc(), subject.getUnit()));

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
        TreeItem<Subject> root = new RecursiveTreeItem<Subject>(ENROLL_SUBJECT_LIST, RecursiveTreeObject::getChildren);

        tblYeSubject.setRoot(root);
        tblYeSubject.setShowRoot(false);

        //Remove from the table
        if (ENROLLED_LIST.size() > 0)
            removeToRemoveList(ENROLL_SUBJECT_LIST);
    }

    public void removeYeSubject() {
        Subject subject = ENROLL_SUBJECT_LIST.get(tblYeSubject.getSelectionModel().getSelectedIndex());

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
        TreeItem<Subject> root = new RecursiveTreeItem<>(ENROLL_SUBJECT_LIST, RecursiveTreeObject::getChildren);

        tblYeSubject.setRoot(root);
        tblYeSubject.setShowRoot(false);

        if (ENROLLED_LIST.size() > 0)
            addToRemoveList(subject);
    }

    public void removeAllYeSubject() {

        final List<Subject> templist = new ArrayList<>();

        //reset
        totalYeUnit = 0;
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

            List<Curriculum> curriculumList = curriculumDao.getCurriculumListByCourseId(student.getCourseId());

            student.setStatus("REGULAR");
            studentDao.updateStudentById(student.getId(), student);

            root:
            for (Curriculum curriculum : curriculumList) {
                for (com.erm.project.ees.model.Subject subject : curriculumDao.getSubjectList(curriculum.getId())) {
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
                lbCourse.setText(new CourseDaoImpl().getCourseById(student.getCourseId()).getDesc());
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
                } else if (totalYear == 5) {
                    cbMaxYear.getItems().add("1ST YR");
                    cbMaxYear.getItems().add("2ND YR");
                    cbMaxYear.getItems().add("3RD YR");
                    cbMaxYear.getItems().add("4TH YR");
                    cbMaxYear.getItems().add("5TH YR");
                } else {
                    cbMaxYear.getItems().add("1ST YR");
                    cbMaxYear.getItems().add("2ND YR");
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

            List<com.erm.project.ees.model.Subject> list = AssessmentHelper.getSubjectListWithFilter(student,
                    cbCurSemester.getSelectionModel().getSelectedIndex() + 1, cbMaxYear.getSelectionModel().getSelectedIndex() + 1);
            if (cbCurSemester.getSelectionModel().getSelectedIndex() == 1)
                list.addAll(AssessmentHelper.getSpecialSubjectListWithFilter(student, "SPECIAL_CLASSES", cbMaxYear.getSelectionModel().getSelectedIndex() + 1));

            clearAb();
            initAbSubject(list);

            List<com.erm.project.ees.model.Subject> subjectList = new ArrayList<>();
            List<StudentSubjectRecord> recordList = dirtyDao.getStudentSubjectRecordByMark(student.getId(), "ONGOING");

            for (StudentSubjectRecord record : recordList) {
                com.erm.project.ees.model.Subject subject = new SubjectDaoImpl().getSubjectById(record.getSubjectId());
                subjectList.add(subject);
            }

            if (subjectList.size() > 0) {
                Platform.runLater(() -> {
                    cbAbSubject.getItems().add("DROP");

                    cbCurSemester.getSelectionModel().select(dirtyDao.getStudentSubjectRecordSemester(student.getId(), "ONGOING") - 1);
                    cbCurSemester.setDisable(true);
                });
            }

            ENROLLED_LIST.addAll(subjectList);

            clearYe();
            loadYeSubject(subjectList);
        }).start();
    }

    public void addToRemoveList(Subject subject) {
        final List<StudentSubjectRecord> recordList = dirtyDao.getStudentSubjectRecordByMark(student.getId(), "ONGOING");

        for (StudentSubjectRecord record : recordList) {
            if (subject.getId() == record.getSubjectId()) {
                ENROLLED_LIST_REMOVE.add(new com.erm.project.ees.model.Subject(subject.getId(), subject.getName(),
                        subject.getDesc(), subject.getUnit()));
                break;
            }
        }

        showLoading();
        Platform.runLater(() -> cbAbSubject.getSelectionModel().select("DROP"));
        if (cbAbSubject.getSelectionModel().getSelectedItem().equalsIgnoreCase("DROP")) {
            new Thread(() -> {
                List<com.erm.project.ees.model.Subject> list = new ArrayList<>();

                //Remove list
                loadRemoveList(list);

                loadAbItem(list);
                hideLoading();
            }).start();
        }
    }

    public void addToRemoveList(List<Subject> subjects) {
        final List<StudentSubjectRecord> recordList = dirtyDao.getStudentSubjectRecordByMark(student.getId(), "ONGOING");

        for (Subject subject : subjects) {
            child:
            for (StudentSubjectRecord record : recordList) {
                if (subject.getId() == record.getSubjectId()) {
                    ENROLLED_LIST_REMOVE.add(new com.erm.project.ees.model.Subject(subject.getId(), subject.getName(),
                            subject.getDesc(), subject.getUnit()));
                    break child;
                }
            }
        }

        showLoading();
        Platform.runLater(() -> cbAbSubject.getSelectionModel().select("DROP"));
        if (cbAbSubject.getSelectionModel().getSelectedItem().equalsIgnoreCase("DROP")) {
            new Thread(() -> {
                List<com.erm.project.ees.model.Subject> list = new ArrayList<>();

                //Remove list
                loadRemoveList(list);

                loadAbItem(list);
                hideLoading();
            }).start();
        }
    }

    public void removeToRemoveList(Subject subject) {
        final List<com.erm.project.ees.model.Subject> subjectList = new ArrayList<>();
        final List<StudentSubjectRecord> recordList = dirtyDao.getStudentSubjectRecordByMark(student.getId(), "ONGOING");

        for (StudentSubjectRecord record : recordList) {
            if (subject.getId() == record.getSubjectId()) {
                inner:
                for (com.erm.project.ees.model.Subject s : ENROLLED_LIST_REMOVE) {
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
                List<com.erm.project.ees.model.Subject> list = new ArrayList<>();

                //Remove list
                loadRemoveList(list);

                loadAbItem(list);
                hideLoading();
            }).start();
        }
    }

    public void removeToRemoveList(List<Subject> subjects) {
        final List<com.erm.project.ees.model.Subject> subjectList = new ArrayList<>();
        final List<StudentSubjectRecord> recordList = dirtyDao.getStudentSubjectRecordByMark(student.getId(), "ONGOING");

        for (Subject subject : subjects) {
            child1:
            for (StudentSubjectRecord record : recordList) {
                if (subject.getId() == record.getSubjectId()) {
                    child2:
                    for (com.erm.project.ees.model.Subject s : ENROLLED_LIST_REMOVE) {
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
                List<com.erm.project.ees.model.Subject> list = new ArrayList<>();

                //Remove list
                loadRemoveList(list);

                loadAbItem(list);
                hideLoading();
            }).start();
        }
    }

    public void loadRemoveList(List<com.erm.project.ees.model.Subject> list) {
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

    private void loadAbItem(List<com.erm.project.ees.model.Subject> subjectList) {

        //reset the total unit
        totalAbUnit = 0;

        //reset the list
        while (AVAILABLE_SUBJECT_LIST.size() > 0)
            AVAILABLE_SUBJECT_LIST.remove(0);

        for (com.erm.project.ees.model.Subject subject : subjectList) {
            AVAILABLE_SUBJECT_LIST.add(new Subject(subject.getId(), subject.getName(), subject.getDesc(), subject.getUnit()));
            totalAbUnit += subject.getUnit();
        }
        TreeItem<Subject> root = new RecursiveTreeItem<>(AVAILABLE_SUBJECT_LIST, RecursiveTreeObject::getChildren);

        Platform.runLater(() -> {
            lbAbUnit.setText(totalAbUnit + "");
            tblAbSubject.setRoot(root);
            tblAbSubject.setShowRoot(false);
        });
    }

    private void initAbSubject(List<com.erm.project.ees.model.Subject> subjectList) {

        //reset the total unit
        totalAbUnit = 0;

        //reset the list
        while (AVAILABLE_SUBJECT_LIST.size() > 0)
            AVAILABLE_SUBJECT_LIST.remove(0);

        for (com.erm.project.ees.model.Subject subject : subjectList) {
            AVAILABLE_SUBJECT_LIST.add(new Subject(subject.getId(), subject.getName(), subject.getDesc(), subject.getUnit()));
            totalAbUnit += subject.getUnit();
        }

        Platform.runLater(() -> {
            lbAbUnit.setText(totalAbUnit + "");
            TreeItem<Subject> root = new RecursiveTreeItem<>(AVAILABLE_SUBJECT_LIST, RecursiveTreeObject::getChildren);

            JFXTreeTableColumn<Subject, Long> idCol = new JFXTreeTableColumn<>("ID");
            idCol.setResizable(false);
            idCol.setPrefWidth(80);
            idCol.setCellValueFactory(param -> param.getValue().getValue().idProperty().asObject());

            JFXTreeTableColumn<Subject, String> nameCol = new JFXTreeTableColumn<>("Subject");
            nameCol.setResizable(false);
            nameCol.setPrefWidth(130);
            nameCol.setCellValueFactory(param -> param.getValue().getValue().nameProperty());

            JFXTreeTableColumn<Subject, String> descCol = new JFXTreeTableColumn<>("Description");
            descCol.setResizable(false);
            descCol.setPrefWidth(210);
            descCol.setCellValueFactory(param -> param.getValue().getValue().descProperty());

            JFXTreeTableColumn<Subject, Integer> unitCol = new JFXTreeTableColumn<>("Unit");
            unitCol.setResizable(false);
            unitCol.setPrefWidth(80);
            unitCol.setCellValueFactory(param -> param.getValue().getValue().unitProperty().asObject());

            tblAbSubject.getColumns().add(idCol);
            tblAbSubject.getColumns().add(nameCol);
            tblAbSubject.getColumns().add(descCol);
            tblAbSubject.getColumns().add(unitCol);

            tblAbSubject.setRoot(root);
            tblAbSubject.setShowRoot(false);
        });
    }
}
