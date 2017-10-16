package io.erm.ees.controller;

import io.erm.ees.dao.CourseDao;
import io.erm.ees.dao.CurriculumDao;
import io.erm.ees.dao.SubjectDao;
import io.erm.ees.dao.impl.CourseDaoImpl;
import io.erm.ees.dao.impl.CurriculumDaoImpl;
import io.erm.ees.dao.impl.SubjectDaoImpl;
import io.erm.ees.model.Course;
import io.erm.ees.model.Curriculum;
import io.erm.ees.stage.CurriculumStage;
import io.erm.ees.stage.SubjectInputStage;
import io.erm.ees.stage.SubjectListStage;
import io.erm.ees.util.ResourceHelper;
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
import java.util.List;
import java.util.ResourceBundle;

public class CurriculumController implements Initializable, SubjectListStage.OnAddItemListener,
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
    private ImageView imgLoading;

    @FXML
    private VBox pnScreen;

    @FXML
    private JFXTreeTableView<io.erm.ees.model.recursive.Subject> tblSList;

    private final SubjectDao subjectDao = new SubjectDaoImpl();
    private final CourseDao courseDao = new CourseDaoImpl();
    private final CurriculumDao curriculumDao = new CurriculumDaoImpl();

    private io.erm.ees.model.Subject sCurrent;

    private final ObservableList<io.erm.ees.model.recursive.Subject> SUBJECT_LIST = FXCollections.observableArrayList();
    private final ObservableList<String> YR_SEM_LIST = FXCollections.observableArrayList();
    private final List<Curriculum> CURRICULUM_LIST = new ArrayList<>();

    private final Course COURSE = new Course();
    private final Course TEMP_COURSE = new Course();

    private int totalUnit;
    private boolean unitIsValid[];
    private volatile boolean stop;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();

        Image logoLoading = new Image(ResourceHelper.resourceWithBasePath("image/loading.gif").toString());
        imgLoading.setImage(logoLoading);
    }

    @FXML
    protected void onClickSearch() {
        try {
            long subjectId = Long.parseLong(txCode.getText());
            final io.erm.ees.model.Subject subject = subjectDao.getSubjectById(subjectId);

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

                SUBJECT_LIST.add(new io.erm.ees.model.recursive.Subject(sCurrent.getId(), sCurrent.getName(),
                        sCurrent.getDesc(), sCurrent.getUnit(), sCurrent.getUnitDisplay()));

                TreeItem<io.erm.ees.model.recursive.Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST,
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
            io.erm.ees.model.recursive.Subject subject =
                    SUBJECT_LIST.get(tblSList.getSelectionModel().getSelectedIndex());

            totalUnit -= subject.getUnit();

            curriculumDao.removeSubject(curriculumId, subject.getId());

            SUBJECT_LIST.remove(subject);

            TreeItem<io.erm.ees.model.recursive.Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST,
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
            final List<io.erm.ees.model.recursive.Subject> subjectList = new ArrayList<>();

            totalUnit = 0;

            for (io.erm.ees.model.Subject subject : curriculumDao.getSubjectList(curriculumId)) {
                subjectList.add(new io.erm.ees.model.recursive.Subject(subject.getId(), subject.getName(),
                        subject.getDesc(), subject.getUnit(), subject.getUnitDisplay()));
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

                TreeItem<io.erm.ees.model.recursive.Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST,
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
        TreeItem<io.erm.ees.model.recursive.Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST,
                RecursiveTreeObject::getChildren);

        JFXTreeTableColumn<io.erm.ees.model.recursive.Subject, Long> idCol = new JFXTreeTableColumn<>("ID");
        idCol.setResizable(false);
        idCol.setPrefWidth(80);
        idCol.setCellValueFactory(param -> param.getValue().getValue().idProperty().asObject());

        JFXTreeTableColumn<io.erm.ees.model.recursive.Subject, String> nameCol = new JFXTreeTableColumn<>("Subject");
        nameCol.setResizable(false);
        nameCol.setPrefWidth(130);
        nameCol.setCellValueFactory(param -> param.getValue().getValue().nameProperty());

        JFXTreeTableColumn<io.erm.ees.model.recursive.Subject, String> descCol = new JFXTreeTableColumn<>("Description");
        descCol.setResizable(false);
        descCol.setPrefWidth(210);
        descCol.setCellValueFactory(param -> param.getValue().getValue().descProperty());

        JFXTreeTableColumn<io.erm.ees.model.recursive.Subject, String> unitCol = new JFXTreeTableColumn<>("Unit");
        unitCol.setResizable(false);
        unitCol.setPrefWidth(80);
        unitCol.setCellValueFactory(param -> param.getValue().getValue().unitDisplayProperty());

        tblSList.getColumns().add(idCol);
        tblSList.getColumns().add(nameCol);
        tblSList.getColumns().add(descCol);
        tblSList.getColumns().add(unitCol);

        tblSList.setRoot(root);
        tblSList.setShowRoot(false);
    }

    @Override
    public void onAdd(io.erm.ees.model.Subject item) {
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

                SUBJECT_LIST.add(new io.erm.ees.model.recursive.Subject(sCurrent.getId(), sCurrent.getName(),
                        sCurrent.getDesc(), sCurrent.getUnit(), sCurrent.getUnitDisplay()));

                TreeItem<io.erm.ees.model.recursive.Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST,
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

    public void dispose(State state) {
        if (state.getState() == State.DISCARD.getState()) {
            new Thread(() -> {
                stop = true;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (Curriculum curriculum : CURRICULUM_LIST)
                    curriculumDao.deleteCurriculumById(curriculum.getId());
                courseDao.deleteCourseById(TEMP_COURSE.getId());
            }).start();
        }

        Platform.runLater(() -> {

            lbName.setText("");
            lbDesc.setText("");
            lbStatus.setText("");
            txCode.setText("");
            lbUnit.setText("0");

            bnAdd.setDisable(true);

            SUBJECT_LIST.clear();
            CURRICULUM_LIST.clear();
        });
    }

    public void listener(Course course) {
        showLoading();
        new Thread(() -> {
            stop = false;
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

                        if (stop) {
                            break;
                        }

                        Curriculum cur = curriculumDao.addCurriculum(new Curriculum(year, sem, course.getId()));
                        if (isExist) {
                            for (io.erm.ees.model.Subject subject : curriculumDao.getSubjectList(COURSE.getId(), year, sem)) {
                                curriculumDao.addSubject(cur.getId(), subject.getId());
                            }
                            unitIsValid[counter] = true;
                        }
                        CURRICULUM_LIST.add(cur);
                        counter++;
                    }
                }
            }

            Platform.runLater(() -> {
                lbName.setText(TEMP_COURSE.getName());
                lbDesc.setText(TEMP_COURSE.getDesc());

                YR_SEM_LIST.clear();
                cbYS.setItems(YR_SEM_LIST);
                for (int i = 1; i <= TEMP_COURSE.getTotalYear(); i++) {
                    for (int sem = 1; sem <= TEMP_COURSE.getTotalSemester(); sem++) {
                        cbYS.getItems().add(formatter(i) + " YEAR / " + formatter(sem) + " SEMESTER");
                    }
                }
                cbYS.getSelectionModel().select(0);
            });

            hideLoading();
        }).start();
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
