/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ams;

import ams.utils.DBConnect;
import ams.utils.SceneLoader;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author swapnil
 */
public class AddStudentController implements Initializable {

    @FXML
    private TextField ID;
    @FXML
    private TextField Name;
    @FXML
    private TextField Section;
    @FXML
    private TextField Semester;
    @FXML
    private Button AddStudentBtn;
    
    private SceneLoader sceneLoader;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    
    Boolean isValid(){
        if(ID.getText().equals("")){
            return false;
        }
        if(Name.getText().equals("")){
            return false;
        }
        if(Section.getText().equals("")){
            return false;
        }
        if(Semester.getText().equals("")){
            return false;
        }
        return true;
    }

    @FXML
    private void OnClickAddStudent(ActionEvent event) throws Exception {
        
        if(isValid()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed :(");
            alert.setContentText("Empty Field");
            alert.show();
        }
        
        String query=  "INSERT INTO Students VALUES('" +ID.getText()+"','"+Name.getText()+"',"
                + "'"+Section.getText()+"','"+Semester.getText()+"');";
        
        DBConnect dbc = new DBConnect();
        dbc.connectToDB();
        dbc.queryToDB(query);
        dbc.disconnectFromDB();
        
        sceneLoader.loadPage("Dashboard.fxml", this, event);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success!");
        alert.setContentText(ID.getText() +" has been added!");
        alert.show();
        
        
    }
    
}
