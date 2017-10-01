package com.erm.project.ees.controller;

import com.erm.project.ees.dao.CourseDao;
import com.erm.project.ees.dao.CurriculumDao;
import com.erm.project.ees.dao.SubjectDao;
import com.erm.project.ees.dao.impl.CourseDaoImpl;
import com.erm.project.ees.dao.impl.CurriculumDaoImpl;
import com.erm.project.ees.dao.impl.SubjectDaoImpl;
import com.erm.project.ees.model.Course;
import com.erm.project.ees.model.Curriculum;
import com.erm.project.ees.model.Subject;
import com.erm.project.ees.stage.CurriculumStage;
import com.erm.project.ees.stage.SubjectInputStage;
import com.erm.project.ees.stage.SubjectListStage;
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
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CurriculumController implements Initializable, SubjectListStage.OnAddItemListener ,
    SubjectInputStage.OnItemAddLister {


    @FXML
    private Label lbName;

    @FXML
    private Label lbDesc;

    @FXML
    private Label lbStatus;

    @FXML
    private JFXTextField txCode;

    @FXML
    private JFXComboBox<String> cbYS;

    @FXML
    private JFXButton bnAdd;

    @FXML
    private JFXButton bnSave;

    @FXML
    private Label lbUnit;

    @FXML
    private JFXTreeTableView<com.erm.project.ees.model.recursive.Subject> tblSList;

    private final SubjectDao subjectDao = new SubjectDaoImpl();
    private final CourseDao courseDao = new CourseDaoImpl();
    private final CurriculumDao curriculumDao = new CurriculumDaoImpl();

    private Subject sCurrent;

    private final ObservableList<com.erm.project.ees.model.recursive.Subject> SUBJECT_LIST = FXCollections.observableArrayList();
    private final ObservableList<String> YR_SEM_LIST = FXCollections.observableArrayList();
    private final List<Curriculum> CURRICULUM_LIST = new ArrayList<>();

    private final Course COURSE = new Course();
    private final Course TEMP_COURSE = new Course();

    private int totalUnit;
    private boolean unitIsValid[];

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
    }

    @FXML
    protected void onClickSearch() {
        try {
            long subjectId = Long.parseLong(txCode.getText());
            final Subject subject = subjectDao.getSubjectById(subjectId);

            if (subject != null) {
                lbStatus.setVisible(true);
                lbStatus.setText("Found");
                lbStatus.setStyle("-fx-text-fill:#1abc9c");
                sCurrent = subject;
                bnAdd.setDisable(false);
            } else {
                lbStatus.setVisible(true);
                lbStatus.setText("Not found");
                lbStatus.setStyle("-fx-text-fill:#c0392b");
                bnAdd.setDisable(true);
            }

        } catch (NumberFormatException e) {
            lbStatus.setVisible(true);
            lbStatus.setText("Not Found");
            lbStatus.setStyle("-fx-text-fill:#c0392b");
            bnAdd.setDisable(true);
        } catch (RuntimeException e) {
            lbStatus.setVisible(true);
            lbStatus.setText("Not found");
            lbStatus.setStyle("-fx-text-fill:#c0392b");
            bnAdd.setDisable(true);
        }
    }

    @FXML
    protected void onClickBrowse() {
        SubjectListStage subjectListStage = new SubjectListStage();
        subjectListStage.setListener(this);
        subjectListStage.showAndWait();
    }

    @FXML
    protected void onClickNew() {
        SubjectInputStage subjectInputStage = new SubjectInputStage();
        subjectInputStage.setOnItemAddLister(this);
        subjectInputStage.showAndWait();
    }

    @FXML
    protected void onClickAdd() {
        if (sCurrent != null) {
            final int index = cbYS.getSelectionModel().getSelectedIndex();
            final long curriculumId = CURRICULUM_LIST.get(index).getId();
            boolean isExist = false;

            for (Curriculum curriculum : CURRICULUM_LIST) {
                if (curriculumDao.isSubjectExist(curriculum.getId(), sCurrent.getId())) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                totalUnit += sCurrent.getUnit();

                curriculumDao.addSubject(curriculumId, sCurrent.getId());

                SUBJECT_LIST.add(new com.erm.project.ees.model.recursive.Subject(sCurrent.getId(), sCurrent.getName(),
                        sCurrent.getDesc(), sCurrent.getUnit()));

                TreeItem<com.erm.project.ees.model.recursive.Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST,
                        RecursiveTreeObject::getChildren);

                tblSList.setRoot(root);
                tblSList.setShowRoot(false);

                sCurrent = null;
                bnAdd.setDisable(true);
                lbStatus.setVisible(false);

                lbUnit.setText(totalUnit + "");
                if (totalUnit > 0 && totalUnit <= 30) {
                    lbUnit.setStyle("-fx-text-fill:#16a085");
                    unitIsValid[index] = true;
                } else {
                    lbUnit.setStyle("-fx-text-fill:#c0392b");
                    unitIsValid[index] = false;
                }
            } else {
                lbStatus.setVisible(true);
                lbStatus.setText("Subject already exist");
                lbStatus.setStyle("-fx-text-fill:#c0392b");
                bnAdd.setDisable(true);
            }
        }
    }

    @FXML
    protected void onClickRemove() {
        final int index = tblSList.getSelectionModel().getSelectedIndex();
        final long curriculumId = CURRICULUM_LIST.get(cbYS.getSelectionModel().getSelectedIndex()).getId();
        if (index >= 0) {
            com.erm.project.ees.model.recursive.Subject subject =
                    SUBJECT_LIST.get(tblSList.getSelectionModel().getSelectedIndex());

            totalUnit -= subject.getUnit();

            curriculumDao.removeSubject(curriculumId, subject.getId());

            SUBJECT_LIST.remove(subject);

            TreeItem<com.erm.project.ees.model.recursive.Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST,
                    RecursiveTreeObject::getChildren);

            tblSList.setRoot(root);
            tblSList.setShowRoot(false);

            lbUnit.setText(totalUnit + "");
            if (totalUnit > 0 && totalUnit <= 30) {
                lbUnit.setStyle("-fx-text-fill:#16a085");
                unitIsValid[cbYS.getSelectionModel().getSelectedIndex()] = true;
            } else {
                lbUnit.setStyle("-fx-text-fill:#c0392b");
                unitIsValid[cbYS.getSelectionModel().getSelectedIndex()] = false;
            }
        }
    }

    @FXML
    protected void onClickSave(ActionEvent event) {
        boolean isValid = true;
        for (Curriculum curriculum : CURRICULUM_LIST) {
            isValid = curriculumDao.getSubjectList(curriculum.getId()).size() > 0;
            if (!isValid)
                break;
        }
        if (isValid) {
            for (boolean valid : unitIsValid) {
                if (!valid)
                    isValid = false;
            }
            if (!isValid) {
                Platform.runLater(() -> new Thread(() -> JOptionPane.showMessageDialog(null,
                        "The maximum units per curriculum is 30")).start());
                return;
            }
            if (courseDao.getCourseById(COURSE.getId()) != null) {
                courseDao.deleteCourseById(COURSE.getId());
                courseDao.updateCourseById(TEMP_COURSE.getId(), COURSE);
            }
            dispose(State.SAVE);
            CurriculumStage stage = (CurriculumStage) ((Node) event.getSource()).getScene().getWindow();
            stage.callBack();
        } else {
            Platform.runLater(() -> new Thread(() -> JOptionPane.showMessageDialog(null,
                    "Make sure to add subject to a curriculum")).start());
        }
    }

    @FXML
    protected void onClickDiscard(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        new Thread(() -> dispose(State.DISCARD)).start();
    }

    @FXML
    protected void onChooseYS(ActionEvent event) {
        if (cbYS.getSelectionModel().getSelectedIndex() > -1) {
            final int index = cbYS.getSelectionModel().getSelectedIndex();
            final long curriculumId = CURRICULUM_LIST.get(index).getId();
            final List<com.erm.project.ees.model.recursive.Subject> subjectList = new ArrayList<>();

            totalUnit = 0;

            for (Subject subject : curriculumDao.getSubjectList(curriculumId)) {
                subjectList.add(new com.erm.project.ees.model.recursive.Subject(subject.getId(), subject.getName(),
                        subject.getDesc(), subject.getUnit()));
                totalUnit += subject.getUnit();
            }
            Platform.runLater(() -> {
                SUBJECT_LIST.clear();
                SUBJECT_LIST.addAll(subjectList);

                lbUnit.setText(totalUnit + "");
                if (totalUnit > 0 && totalUnit <= 30) {
                    lbUnit.setStyle("-fx-text-fill:#16a085");
                    unitIsValid[index] = true;
                } else {
                    lbUnit.setStyle("-fx-text-fill:#c0392b");
                    unitIsValid[index] = false;
                }

                TreeItem<com.erm.project.ees.model.recursive.Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST,
                        RecursiveTreeObject::getChildren);

                tblSList.setRoot(root);
                tblSList.setShowRoot(false);
            });
        }
    }

    @FXML
    protected void onPressedText() {
        lbStatus.setVisible(false);
        bnAdd.setDisable(true);
        sCurrent = null;
    }

    public String formatter(int num) {
        switch (num) {
            case 1:
                return num + "ST";
            case 2:
                return num + "ND";
            case 3:
                return num + "RD";
            default:
                return num + "TH";
        }
    }

    public void initTable() {
        TreeItem<com.erm.project.ees.model.recursive.Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST,
                RecursiveTreeObject::getChildren);

        JFXTreeTableColumn<com.erm.project.ees.model.recursive.Subject, Long> idCol = new JFXTreeTableColumn<>("ID");
        idCol.setResizable(false);
        idCol.setPrefWidth(80);
        idCol.setCellValueFactory(param -> param.getValue().getValue().idProperty().asObject());

        JFXTreeTableColumn<com.erm.project.ees.model.recursive.Subject, String> nameCol = new JFXTreeTableColumn<>("Subject");
        nameCol.setResizable(false);
        nameCol.setPrefWidth(130);
        nameCol.setCellValueFactory(param -> param.getValue().getValue().nameProperty());

        JFXTreeTableColumn<com.erm.project.ees.model.recursive.Subject, String> descCol = new JFXTreeTableColumn<>("Description");
        descCol.setResizable(false);
        descCol.setPrefWidth(210);
        descCol.setCellValueFactory(param -> param.getValue().getValue().descProperty());

        JFXTreeTableColumn<com.erm.project.ees.model.recursive.Subject, Integer> unitCol = new JFXTreeTableColumn<>("Unit");
        unitCol.setResizable(false);
        unitCol.setPrefWidth(80);
        unitCol.setCellValueFactory(param -> param.getValue().getValue().unitProperty().asObject());

        tblSList.getColumns().add(idCol);
        tblSList.getColumns().add(nameCol);
        tblSList.getColumns().add(descCol);
        tblSList.getColumns().add(unitCol);

        tblSList.setRoot(root);
        tblSList.setShowRoot(false);
    }

    public void dispose(State state) {
        if (state.getState() == State.DISCARD.getState()) {
            for (Curriculum curriculum : CURRICULUM_LIST)
                curriculumDao.deleteCurriculumById(curriculum.getId());

            courseDao.deleteCourseById(TEMP_COURSE.getId());
        }

        Platform.runLater(() -> {

            lbName.setText("");
            lbDesc.setText("");
            lbStatus.setText("");
            txCode.setText("");
            lbUnit.setText("0");

            cbYS.getItems().clear();
            cbYS.getSelectionModel().clearSelection();

            bnAdd.setDisable(true);

            SUBJECT_LIST.clear();

            CURRICULUM_LIST.clear();
        });
    }

    @Override
    public void onAdd(Subject item) {
        sCurrent = item;
        if (sCurrent != null) {
            final int index = cbYS.getSelectionModel().getSelectedIndex();
            final long curriculumId = CURRICULUM_LIST.get(index).getId();
            boolean isExist = false;

            for (Curriculum curriculum : CURRICULUM_LIST) {
                if (curriculumDao.isSubjectExist(curriculum.getId(), sCurrent.getId())) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                totalUnit += sCurrent.getUnit();

                curriculumDao.addSubject(curriculumId, sCurrent.getId());

                SUBJECT_LIST.add(new com.erm.project.ees.model.recursive.Subject(sCurrent.getId(), sCurrent.getName(),
                        sCurrent.getDesc(), sCurrent.getUnit()));

                TreeItem<com.erm.project.ees.model.recursive.Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST,
                        RecursiveTreeObject::getChildren);

                tblSList.setRoot(root);
                tblSList.setShowRoot(false);

                sCurrent = null;
                bnAdd.setDisable(true);
                lbStatus.setVisible(false);

                lbUnit.setText(totalUnit + "");
                if (totalUnit > 0 && totalUnit <= 30) {
                    lbUnit.setStyle("-fx-text-fill:#16a085");
                    unitIsValid[index] = true;
                } else {
                    lbUnit.setStyle("-fx-text-fill:#c0392b");
                    unitIsValid[index] = false;
                }
            } else {
                Platform.runLater(() -> new Thread(() -> JOptionPane.showMessageDialog(null,
                        "The subject already exist")).start());
            }
        }
    }

    public void listener(Course course) {

        boolean isExist = false;
        COURSE.setId(course.getId());
        COURSE.setName(course.getName());
        COURSE.setDesc(course.getDesc());
        COURSE.setTotalYear(course.getTotalYear());
        COURSE.setTotalSemester(course.getTotalSemester());

        if (courseDao.getCourseById(COURSE.getId()) != null)
            isExist = true;
        Course c = courseDao.addCourse(course);
        if (c != null) {
            TEMP_COURSE.setId(c.getId());
            TEMP_COURSE.setName(c.getName());
            TEMP_COURSE.setDesc(c.getDesc());
            TEMP_COURSE.setTotalYear(c.getTotalYear());
            TEMP_COURSE.setTotalSemester(c.getTotalSemester());

            unitIsValid = new boolean[TEMP_COURSE.getTotalYear() * TEMP_COURSE.getTotalSemester()];
            int counter = 0;
            for (int year = 1; year <= TEMP_COURSE.getTotalYear(); year++) {
                for (int sem = 1; sem <= TEMP_COURSE.getTotalSemester(); sem++) {
                    Curriculum cur = curriculumDao.addCurriculum(new Curriculum(year, sem, course.getId()));
                    if (isExist) {
                        for (Subject subject : curriculumDao.getSubjectList(COURSE.getId(), year, sem)) {
                            curriculumDao.addSubject(cur.getId(), subject.getId());
                        }
                        unitIsValid[counter] = true;
                    }
                    CURRICULUM_LIST.add(cur);
                    counter ++;
                }
            }
        }

        Platform.runLater(() -> {
            lbName.setText(TEMP_COURSE.getName());
            lbDesc.setText(TEMP_COURSE.getDesc());

            YR_SEM_LIST.clear();

            for (int i = 1; i <= TEMP_COURSE.getTotalYear(); i++) {
                for (int sem = 1; sem <= TEMP_COURSE.getTotalSemester(); sem++) {
                    YR_SEM_LIST.add(formatter(i) + " YEAR / " + formatter(sem) + " SEMESTER");
                }
            }
            cbYS.setItems(YR_SEM_LIST);
            cbYS.getSelectionModel().select(0);
        });
    }

    @Override
    public void onAddSubject() {

    }

    public enum State {
        SAVE(1), DISCARD(2);

        private int state;

        State(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }
    }
}
