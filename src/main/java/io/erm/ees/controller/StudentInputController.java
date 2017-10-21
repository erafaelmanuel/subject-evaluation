package io.erm.ees.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import io.erm.ees.dao.CourseDao;
import io.erm.ees.dao.SectionDao;
import io.erm.ees.dao.StudentDao;
import io.erm.ees.dao.impl.SectionDaoImpl;
import io.erm.ees.helper.DbFactory;
import io.erm.ees.model.Course;
import io.erm.ees.model.Section;
import io.erm.ees.model.Student;
import io.erm.ees.stage.StudentInputStage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StudentInputController implements Initializable {

    @FXML
    private JFXTextField txFName;

    @FXML
    private JFXTextField txLName;

    @FXML
    private JFXTextField txMName;

    @FXML
    private JFXTextField txAge;

    @FXML
    private JFXComboBox<String> cbGender;

    @FXML
    private JFXComboBox<String> cbCourse;

    @FXML
    private JFXTextField txCNumber;

    @FXML
    private JFXComboBox<String> cbSection;

    private String tempHolder = "+63 ";

    private static final ObservableList<String> OBSERVABLE_LIST_GENDER = FXCollections.observableArrayList();
    private static final ObservableList<String> OBSERVABLE_LIST_SECTION = FXCollections.observableArrayList();

    private final CourseDao courseDao = DbFactory.courseFactory();
    private final SectionDao sectionDao = new SectionDaoImpl();
    private final StudentDao studentDao = DbFactory.studentFactory();

    private final List<Course> COURSE_LIST = new ArrayList<>();
    private final List<Section> SECTION_LIST = new ArrayList<>();

    private final Student STUDENT = new Student();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        cbGender.setItems(OBSERVABLE_LIST_GENDER);
        cbGender.getItems().add("Female");
        cbGender.getItems().add("Male");
        cbGender.getSelectionModel().select(0);

        for (Course c : courseDao.getCourseList()) {
            cbCourse.getItems().add(c.getName());
            COURSE_LIST.add(c);
        }

        cbCourse.getSelectionModel().select(0);

        for (Section s : sectionDao.getSectionList("WHERE _year=1 GROUP BY _name")) {
            cbSection.getItems().add(s.getYear() + "-" + s.getName());
            SECTION_LIST.add(s);
        }

        cbSection.getSelectionModel().select(0);
        txCNumber.focusedProperty().addListener((observableValue, focusOut, focusIn) -> {
            if (focusIn) {
                if (txCNumber.getText().trim().length() <= 3) {
                    txCNumber.setText(tempHolder);
                } else {

                }
            } else {
                if (txCNumber.getText().trim().length() <= 3) {
                    txCNumber.setText("");
                }
            }
        });
    }

    @FXML
    protected void onClickCancel(ActionEvent event) {
        dispose();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onClickSave(ActionEvent event) {
        if (isInputValid()) {
            STUDENT.setFirstName(txFName.getText());
            STUDENT.setLastName(txLName.getText());
            STUDENT.setMiddleName(txMName.getText());
            STUDENT.setAge(Integer.parseInt(txAge.getText()));
            STUDENT.setGender(cbGender.getSelectionModel().getSelectedItem());
            STUDENT.setContactNumber(Long.parseLong(txCNumber.getText()));
            STUDENT.setCourseId(COURSE_LIST.get(cbCourse.getSelectionModel().getSelectedIndex()).getId());
            STUDENT.setSectionId(SECTION_LIST.get(cbSection.getSelectionModel().getSelectedIndex()).getId());
            STUDENT.setStatus("REGULAR");

            if (studentDao.getStudentById(STUDENT.getId()) == null)
                studentDao.addStudent(STUDENT);
            else
                studentDao.updateStudentById(STUDENT.getId(), STUDENT);

            dispose();

            StudentInputStage stage = (StudentInputStage) ((Node) event.getSource()).getScene().getWindow();
            stage.callBack();
        }
    }

    private boolean isInputValid() {
        boolean isValid = true;
        //First name
        if (txFName.getText().trim().isEmpty()) {
            txFName.setText("");
            txFName.setPromptText("Please enter a name");
            txFName.setStyle("-fx-prompt-text-fill:#c0392b");
            isValid = false;
        } else if (!txFName.getText().trim().matches("^([a-zA-Z]+ ?)+$")) {
            txFName.setText("");
            txFName.setPromptText("Please Enter a valid name");
            txFName.setStyle("-fx-prompt-text-fill:#c0392b");
            isValid = false;
        } else if (txFName.getText().trim().toCharArray().length < 3) {
            txFName.setText("");
            txFName.setPromptText("The name is too short");
            txFName.setStyle("-fx-prompt-text-fill:#c0392b");
            isValid = false;
        } else {
            txFName.setPromptText("First Name");
            txFName.setStyle("-fx-prompt-text-fill:#000");
        }
        //Last name
        if (txLName.getText().trim().isEmpty()) {
            txLName.setText("");
            txLName.setPromptText("Please enter a name");
            txLName.setStyle("-fx-prompt-text-fill:#c0392b");
            isValid = false;
        } else if (!txLName.getText().trim().matches("^([a-zA-Z]+ ?)+$")) {
            txLName.setText("");
            txLName.setPromptText("Please Enter a valid name");
            txLName.setStyle("-fx-prompt-text-fill:#c0392b");
            isValid = false;
        } else if (txLName.getText().trim().toCharArray().length < 3) {
            txLName.setText("");
            txLName.setPromptText("The name is too short");
            txLName.setStyle("-fx-prompt-text-fill:#c0392b");
            isValid = false;
        } else {
            txLName.setPromptText("Last Name");
            txLName.setStyle("-fx-prompt-text-fill:#000");
        }
        //Middle name
        if (txMName.getText().trim().isEmpty()) {
            txMName.setText("");
            txMName.setPromptText("Please enter a name");
            txMName.setStyle("-fx-prompt-text-fill:#c0392b");
            isValid = false;
        } else if (!txMName.getText().trim().matches("^([a-zA-Z]+ ?)+$")) {
            txMName.setText("");
            txMName.setPromptText("Please Enter a valid name");
            txMName.setStyle("-fx-prompt-text-fill:#c0392b");
            isValid = false;
        } else if (txMName.getText().trim().toCharArray().length < 3) {
            txMName.setText("");
            txMName.setPromptText("The name is too short");
            txMName.setStyle("-fx-prompt-text-fill:#c0392b");
            isValid = false;
        } else {
            txMName.setPromptText("Middle Name");
            txMName.setStyle("-fx-prompt-text-fill:#000");
        }
        //age
        if (txAge.getText().trim().isEmpty()) {
            txAge.setText("");
            txAge.setPromptText("Please enter an age");
            txAge.setStyle("-fx-prompt-text-fill:#c0392b");
            isValid = false;
        } else if (!txAge.getText().trim().matches("^[0-9]+$")) {
            txAge.setText("");
            txAge.setPromptText("Enter only a numeric character");
            txAge.setStyle("-fx-prompt-text-fill:#c0392b");
            isValid = false;
        } else if (Integer.parseInt(txAge.getText().trim()) < 10 && Integer.parseInt(txAge.getText().trim()) > 75) {
            txAge.setText("");
            txAge.setPromptText("Please enter a valid age");
            txAge.setStyle("-fx-prompt-text-fill:#c0392b");
            isValid = false;
        } else {
            txAge.setPromptText("Age");
            txAge.setStyle("-fx-prompt-text-fill:#000");
        }
        //contact
        txCNumber.setText(txCNumber.getText().replace("+63 ", "0"));
        txCNumber.setText(txCNumber.getText().replace("+63", "0"));
        txCNumber.setText(txCNumber.getText().replace("+6", "0"));
        txCNumber.setText(txCNumber.getText().replace(" ", "0"));
        txCNumber.setText(txCNumber.getText().replace("+", "0"));

        if (txCNumber.getText().trim().isEmpty()) {
            txCNumber.setText("");
            txCNumber.setPromptText("Please enter a contact number");
            txCNumber.setStyle("-fx-prompt-text-fill:#c0392b");
            isValid = false;
        } else if (!txCNumber.getText().trim().matches("^[0-9]+$")) {
            txCNumber.setText("");
            txCNumber.setPromptText("Enter only a numeric character");
            txCNumber.setStyle("-fx-prompt-text-fill:#c0392b");
            isValid = false;
        } else if (txCNumber.getText().trim().length() != 11) {
            new Thread(() -> JOptionPane.showMessageDialog(null, "Invalid contact number")).start();
            isValid = false;
        } else if (!(txCNumber.getText().trim().length() == 11 && txCNumber.getText().charAt(1) == '9')) {
            new Thread(() -> JOptionPane.showMessageDialog(null, "Invalid contact number")).start();
            isValid = false;
        } else {
            txCNumber.setPromptText("Contact Numner");
            txCNumber.setStyle("-fx-prompt-text-fill:#000");
        }

        return isValid;
    }

    @FXML
    protected void onKeyCN(KeyEvent event) {
        if (event.getText().equals("0") && (txCNumber.getText().length() == 4 || txCNumber.getText().length() == 0))
            txCNumber.setText("");
        else if ((!event.getText().matches("^[0-9]$")) && (txCNumber.getText().length() == 3))
            txCNumber.setText("");
        else if (txCNumber.getText().trim().isEmpty())
            txCNumber.setText("+63 ");

        txCNumber.end();
    }

    public void dispose() {
        txFName.setText("");
        txLName.setText("");
        txMName.setText("");
        txAge.setText("");
        txCNumber.setText("");

        cbCourse.getSelectionModel().select(0);
        cbSection.getSelectionModel().select(0);
        cbGender.getSelectionModel().select(0);

    }

    public void listen(Student student) {
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

        txFName.setText(STUDENT.getFirstName());
        txLName.setText(STUDENT.getLastName());
        txMName.setText(STUDENT.getMiddleName());
        txAge.setText(STUDENT.getAge() + "");
        txCNumber.setText("0" + STUDENT.getContactNumber() + "");

        for (int i = 0; i < COURSE_LIST.size(); i++) {
            if (COURSE_LIST.get(i).getId() == STUDENT.getCourseId()) {
                cbCourse.getSelectionModel().select(i);
                break;
            }
        }

        for (int i = 0; i < SECTION_LIST.size(); i++) {
            if (SECTION_LIST.get(i).getId() == STUDENT.getSectionId()) {
                cbCourse.getSelectionModel().select(i);
                break;
            }
        }
        if (STUDENT.getGender().equalsIgnoreCase("MALE"))
            cbGender.getSelectionModel().select(1);
        else
            cbGender.getSelectionModel().select(0);
    }
}
