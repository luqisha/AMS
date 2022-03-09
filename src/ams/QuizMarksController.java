/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ams;

import ams.utils.DBConnect;
import ams.utils.TableLoader;
import com.sun.javafx.collections.ObservableListWrapper;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Ashiq
 */
public class QuizMarksController implements Initializable {

    @FXML
    private TableView quizMarksTableView;
    @FXML
    private TextField txtFieldCourseName;
    @FXML
    private TextField txtFieldQuizNo;
    @FXML
    private Button btnFindMarks;
    @FXML
    private TextField txtFieldSID;
    @FXML
    private Button btnReset;

    private ObservableList<ObservableList> data;
    @FXML
    private ComboBox<String> dropDownFindBy;
    @FXML
    private TableView<?> quizMarksTableView1;
    @FXML
    private TextField inputFieldCourse;
    @FXML
    private TextField txtFieldQuizNo1;
    @FXML
    private Button btnSearch;
    @FXML
    private TextField txtFieldSID1;
    @FXML
    private Button btnReset1;
    @FXML
    private ComboBox<String> dropDownFindQuiz;
    @FXML
    private TextField txtFieldSession;
    @FXML
    private TextField txtFieldQuizMarks;
    @FXML
    private Button btnInsert1;

    private TableLoader tableLoader;
    @FXML
    private ImageView successIcon;
    @FXML
    private TextField txtFieldSessionToFindMark;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtFieldCourseName.setText("");
        txtFieldQuizNo.setText("");
        txtFieldSID.setText("");

        dropDownFindBy.setItems(FXCollections.observableArrayList("CourseNo", "StudentID" ));

        dropDownFindQuiz.setItems(FXCollections.observableArrayList("CourseID", "CourseNo"));
    }

    @FXML
    private void onClickFindMarks(ActionEvent event) throws ClassNotFoundException, SQLException {
        String courseName = txtFieldCourseName.getText();
        String QuizNo = txtFieldQuizNo.getText();
        String SID = txtFieldSID.getText();
        String Session = txtFieldSessionToFindMark.getText();

        DBConnect dbc = new DBConnect();
        dbc.connectToDB();

        if (dropDownFindBy.getSelectionModel().getSelectedItem().equals("CourseNo")) {

            if (courseName.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Empty Field");
                alert.setContentText("Write the name of the Course");
                alert.show();
            } else if (QuizNo.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Empty Field");
                alert.setContentText("Write the QuizNo");
                alert.show();
            } else if (Session.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Empty Field");
                alert.setContentText("Write Session");
                alert.show();
            }
            else {
                String query = "SELECT StudentID, QuizMarks FROM QuizMark "
                        + "WHERE CourseID = (SELECT CourseID FROM Courses WHERE CourseNo='" + courseName + "' AND CourseSession='"+Session+"') AND QuizNo ='" + QuizNo + "';";

                tableLoader.loadTable(query, quizMarksTableView);
            }

        } else if (dropDownFindBy.getSelectionModel().getSelectedItem().equals("StudentID")) {

            if (courseName.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Empty Field");
                alert.setContentText("Write the name of the Course!");
                alert.show();
            } else if (SID.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Empty Field");
                alert.setContentText("Write the Student ID!");
                alert.show();
            } else {
                String query = "SELECT QuizNo, QuizMarks FROM QuizMark "
                        + "WHERE CourseID = (SELECT CourseID FROM Courses WHERE CourseNo='" + courseName + "' AND CourseSession='"+Session+ "') AND StudentID ='" + SID + "';";

                tableLoader.loadTable(query, quizMarksTableView);
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Option Selected");
            alert.setContentText("Select what you want to find data by! ");
            alert.show();
        }

    }

    @FXML
    private void onClickReset(ActionEvent event) {
        txtFieldCourseName.setText("");
        txtFieldQuizNo.setText("");
        txtFieldSID.setText("");
        dropDownFindBy.getSelectionModel().clearSelection();
        txtFieldSID.setDisable(false);
        txtFieldQuizNo.setDisable(false);
        txtFieldSessionToFindMark.setText("");
    }

    @FXML
    private void onClickDropDown(ActionEvent event) {
        if (dropDownFindBy.getSelectionModel().getSelectedItem().equals("CourseNo")) {
            txtFieldSID.setText("");
            txtFieldSID.setDisable(true);
            txtFieldQuizNo.setDisable(false);
        } else if (dropDownFindBy.getSelectionModel().getSelectedItem().equals("StudentID")) {
            txtFieldQuizNo.setText("");
            txtFieldQuizNo.setDisable(true);
            txtFieldSID.setDisable(false);
        }
    }

    @FXML
    private void onClickSearch(ActionEvent event) throws ClassNotFoundException, SQLException {
        String course = inputFieldCourse.getText();
        String session = txtFieldSession.getText();
        String qNo = txtFieldQuizNo1.getText();
        String sID = txtFieldSID1.getText();
        
        if (course.isEmpty() || qNo.isEmpty() ){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty");
            alert.setContentText("Fill up the Course and QuizNo ");
            alert.show();
        }

        else if (dropDownFindQuiz.getSelectionModel().getSelectedItem().equals("CourseID")) {

            String query = "SELECT StudentCourseJoin.StudentID as StudentID, QuizMark.QuizMarks as QuizMarks \n"
                    + "FROM StudentCourseJoin \n"
                    + "LEFT JOIN QuizMark \n"
                    + "ON StudentCourseJoin.StudentID = QuizMark.StudentID AND QuizMark.QuizNo=" + qNo + "\n"
                    + "WHERE StudentCourseJoin.CourseID =" + course
                    + (sID.isEmpty() ? "" : (" AND StudentCourseJoin.StudentID like '%" + sID + "%'"));

            System.out.println(query);
            quizMarksTableView1.getItems().clear();
            quizMarksTableView1.getColumns().clear();
            tableLoader.loadTable(query, quizMarksTableView1);
            dropDownFindQuiz.setDisable(true);

        } else if (dropDownFindQuiz.getSelectionModel().getSelectedItem().equals("CourseNo")) {

            String query = "SELECT StudentCourseJoin.StudentID as StudentID, QuizMark.QuizMarks as QuizMarks \n"
                    + "FROM StudentCourseJoin \n"
                    + "LEFT JOIN QuizMark \n"
                    + "ON StudentCourseJoin.StudentID = QuizMark.StudentID AND QuizMark.QuizNo=" + qNo + "\n"
                    + "WHERE StudentCourseJoin.CourseID = (SELECT CourseID FROM Courses WHERE CourseNo= '" + course + "' AND CourseSession= '" + session + "')"
                    + (sID.isEmpty() ? "" : (" AND StudentCourseJoin.StudentID like '%" + sID + "%'"));

            System.out.println(query);
            tableLoader.loadTable(query, quizMarksTableView1);
            dropDownFindQuiz.setDisable(true);

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Option Selected");
            alert.setContentText("Select what you want to find data by! ");
            alert.show();
        }

    }

    @FXML
    private void onClickReset1(ActionEvent event) {
        inputFieldCourse.setText("");
        txtFieldQuizNo1.setText("");
        txtFieldSID1.setText("");
        txtFieldSession.setText("");
        txtFieldQuizMarks.setText("");
        dropDownFindQuiz.setDisable(false);
        successIcon.setVisible(false);

    }

    @FXML
    private void onClickInsert(ActionEvent event) throws SQLException, ClassNotFoundException {
        String course = inputFieldCourse.getText();
        String session = txtFieldSession.getText();
        String qNo = txtFieldQuizNo1.getText();
        String sID = txtFieldSID1.getText();
        String qMarks = txtFieldQuizMarks.getText(); 
        String insertQuery;
        DBConnect dbc = new DBConnect();
        dbc.connectToDB();
        
        ObservableListWrapper row = (ObservableListWrapper) quizMarksTableView1.getSelectionModel().getSelectedItem();
        sID = row.get(0).toString();

        if (dropDownFindQuiz.getSelectionModel().getSelectedItem().equals("CourseNo")) {
           
            String query = "SELECT CourseID FROM Courses WHERE CourseNo= '" + course + "' AND CourseSession= '" + session + "'";
            ResultSet rs = dbc.queryToDB(query);
            course = rs.getString("CourseID");
        }
        
        insertQuery = "IF Not Exists(SELECT * FROM QuizMark WHERE CourseID = "+ course + "AND StudentID='" + sID + "' AND QuizNo=" + qNo + ")\n"
                + "BEGIN\n"
                + "INSERT INTO QuizMark values(" +course+ ", '"  +sID+ "', " +qNo+ "," +qMarks+")\n"
                + "END\n"
                + "ELSE\n"
                + "BEGIN\n"
                + "UPDATE QuizMark\n"
                + "SET QuizMarks = " + qMarks + "\n"
                + "WHERE CourseID = "+ course + "AND StudentID='" + sID + "' AND QuizNo=" + qNo + "\n"
                + "END";
                
        if(dbc.insertDataToDB(insertQuery) != -1){
            successIcon.setVisible(true);
            btnSearch.fire();
            quizMarksTableView1.getSelectionModel().selectNext();
        }
        
    }

    @FXML
    private void onClickDropDown1(ActionEvent event) {

        if (dropDownFindQuiz.getSelectionModel().getSelectedItem().equals("CourseID")) {
            txtFieldSession.setDisable(true);
            inputFieldCourse.setPromptText("CourseID.");
        } else if (dropDownFindQuiz.getSelectionModel().getSelectedItem().equals("CourseNo")) {
            txtFieldSession.setDisable(false);
            inputFieldCourse.setPromptText("CourseNo.");
        }
    }

    @FXML
    private void onClickRow(MouseEvent event) {
        successIcon.setVisible(false);

    }

}
