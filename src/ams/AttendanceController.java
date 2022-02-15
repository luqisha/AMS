/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ams;

import ams.utils.DBConnect;
import ams.utils.TableLoader;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
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

    private Boolean Validate(){
        
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Empty Field");
            
        if(courseNo.getText().equals("")){
            alert.setContentText("Enter CourseNo");
            alert.show();
            return Boolean.FALSE;
        }
        
        if(session.getText().equals("")){
            alert.setContentText("Enter Session");
            alert.show();
            return Boolean.FALSE;
        }
        
        if(classNo.getText().equals("")){
            alert.setContentText("Enter ClassNO");
            alert.show();
            return Boolean.FALSE;
        }
        
        return Boolean.TRUE;
        
        
    }

    @FXML
    private void onClickAttendance(ActionEvent event) throws ClassNotFoundException, SQLException {
        
        String query = "Select * from Courses where CourseNo = '"+
                courseNo.getText()+"'AND CourseSession = '" + session.getText()+"';";
        
        if(!Validate()){
            return;
        }
       
        DBConnect dbc = new DBConnect();
        dbc.connectToDB();
       
        ResultSet resultSet = dbc.queryToDB(query); //unahndled exception
       
        
        Integer courseId = 0;
        if(resultSet.next()){
            courseId = resultSet.getInt("CourseID");
        }
        
        System.out.println(courseId);
        
        query = "SELECT SelectedStudents.StudentID,\n" +
        "CASE WHEN SelectedAttends.ClassNo IS NULL THEN 0\n" +
        "ELSE 1\n" +
        "END AS Present\n" +
        "FROM (SELECT * FROM StudentCourseJoin WHERE CourseID ="+courseId+") \n" +
        "AS SelectedStudents\n" +
        "LEFT JOIN (SELECT * FROM Attends WHERE ClassNO = " +Integer.parseInt(classNo.getText())+") "
                + "AS SelectedAttends\n" +
        "ON SelectedAttends.StudentID = SelectedStudents.StudentID;";

        tableLoader.loadTable(query, attendancetable);
    }

    @FXML
    private void onClickPresent(ActionEvent event) {
        
    }

    @FXML
    private void onClickAbsent(ActionEvent event) {
    }
    
}
