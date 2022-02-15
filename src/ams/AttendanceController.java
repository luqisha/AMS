/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ams;

import ams.utils.DBConnect;
import ams.utils.TableLoader;
import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.javafx.collections.ObservableSequentialListWrapper;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author swapnil
 */
public class AttendanceController implements Initializable {

    @FXML
    private TableView<?> attendancetable;
    @FXML
    private TextField courseNo;
    @FXML
    private TextField classNo;
    @FXML
    private DatePicker date;

    @FXML
    private TextField session;

    private TableLoader tableLoader;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    private Boolean Validate() {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Empty Field");

        if (courseNo.getText().equals("")) {
            alert.setContentText("Enter CourseNo");
            alert.show();
            return Boolean.FALSE;
        }

        if (session.getText().equals("")) {
            alert.setContentText("Enter Session");
            alert.show();
            return Boolean.FALSE;
        }

        if (classNo.getText().equals("")) {
            alert.setContentText("Enter ClassNO");
            alert.show();
            return Boolean.FALSE;
        }

        return Boolean.TRUE;

    }

    private Integer getCourseID() throws ClassNotFoundException, SQLException {
        String query = "Select * from Courses where CourseNo = '"
                + courseNo.getText() + "'AND CourseSession = '" + session.getText() + "';";

        if (!Validate()) {
            return 0;
        }

        DBConnect dbc = new DBConnect();
        dbc.connectToDB();

        ResultSet resultSet = dbc.queryToDB(query); //unahndled exception

        Integer courseId = 0;
        if (resultSet.next()) {
            courseId = resultSet.getInt("CourseID");
        }

        System.out.println(courseId);

        dbc.disconnectFromDB();

        return courseId;

    }

    @FXML
    private void onClickAttendance(ActionEvent event) throws ClassNotFoundException, SQLException {

        DBConnect dbc = new DBConnect();
        dbc.connectToDB();

        Integer courseId = getCourseID();

        String query = "SELECT SelectedStudents.StudentID,\n"
                + "CASE WHEN SelectedAttends.ClassNo IS NULL THEN 0\n"
                + "ELSE 1\n"
                + "END AS Present\n"
                + "FROM (SELECT * FROM StudentCourseJoin WHERE CourseID =" + courseId + ") \n"
                + "AS SelectedStudents\n"
                + "LEFT JOIN (SELECT * FROM Attends WHERE ClassNO = " + Integer.parseInt(classNo.getText()) + ") "
                + "AS SelectedAttends\n"
                + "ON SelectedAttends.StudentID = SelectedStudents.StudentID;";

        tableLoader.loadTable(query, attendancetable);

        dbc.disconnectFromDB();
    }

    @FXML
    private void onClickPresent(ActionEvent event) throws ClassNotFoundException, SQLException {

        if (!Validate()) {
            return;
        }

        ObservableListWrapper obj = (ObservableListWrapper) attendancetable.getSelectionModel().getSelectedItem();

        String studentId = obj.get(0).toString();

        Integer courseId = getCourseID();

        String query = "INSERT INTO Attends(StudentID,CourseID,ClassNo,ClassDate) VALUES('" + studentId + "',"
                + courseId + "," + classNo.getText() + ",GETDATE());";

        System.out.println(query);
        DBConnect dbc = new DBConnect();
        dbc.connectToDB();
        dbc.queryToDB(query);

        dbc.disconnectFromDB();

        onClickAttendance(event);

        attendancetable.getSelectionModel().selectBelowCell();

    }

    @FXML
    private void onClickAbsent(ActionEvent event) throws ClassNotFoundException, SQLException {

        Integer courseId = getCourseID();

        DBConnect dbc = new DBConnect();
        dbc.connectToDB();

        ObservableListWrapper obj = (ObservableListWrapper) attendancetable.getSelectionModel().getSelectedItem();

        String studentId = obj.get(0).toString();

        String query = "SELECT COUNT(StudentId) FROM Attends WHERE CourseId =" + courseId + " AND ClassNo ="
                + Integer.parseInt(classNo.getText()) + " \n"
                + "AND StudentID = '" + studentId + "';";

        ResultSet resultSet = dbc.queryToDB(query);

        Integer entries = 0;

        if (resultSet.next()) {
            entries = resultSet.getInt(1);
        }

        if (entries != 0) {
            query = "DELETE FROM Attends WHERE CourseId =" + courseId + " AND ClassNo ="
                    + Integer.parseInt(classNo.getText()) + " \n"
                    + "AND StudentID = '" + studentId + "';";
            dbc.queryToDB(query);
            onClickAttendance(event);
        }

        attendancetable.getSelectionModel().selectBelowCell();
        dbc.disconnectFromDB();

    }

}
