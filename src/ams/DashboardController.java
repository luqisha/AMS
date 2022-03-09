/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ams;

import ams.utils.SceneLoader;
import ams.utils.TableLoader;
import ams.utils.DBConnect;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ashiq
 */
public class DashboardController implements Initializable {

    @FXML
    private TableView coursesTable;
    @FXML
    private TextField courseNoTxt;
    @FXML
    private TextField courseTitleTxt;
    @FXML
    private TextField sessionTxt;
    @FXML
    private Button insertBtn;
    
    private TableLoader tableLoader;
 
    private SceneLoader sceneLoader;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            buildTable();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void handleMouseClicked(MouseEvent event) {
    }



    @FXML
    private void onClickInsert(ActionEvent event) throws ClassNotFoundException, SQLException {
        
        DBConnect dbc = new DBConnect();
        dbc.connectToDB();
        String courseNo= courseNoTxt.getText(),
               courseTitle= courseTitleTxt.getText(),
               courseSession= sessionTxt.getText();

        String query="INSERT INTO Courses (CourseNo, CourseTitle, CourseSession)"
                + "values('"+courseNo+"','"+courseTitle+"','"+courseSession+"');";
        
        if(courseNo.isEmpty() || courseTitle.isEmpty() || courseSession.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty Field");
            alert.setContentText("Fill up all the text fields.");
            alert.show();
        }
        else{
            dbc.insertDataToDB(query);
        }
        
        buildTable();
        
        courseNoTxt.setText("");
        courseTitleTxt.setText("");
        sessionTxt.setText("");
        
    }
    
    public void buildTable() throws ClassNotFoundException, SQLException {
        // fetches data from database);
        tableLoader.loadTable("Select * from Courses", coursesTable);
    }

    private void onClickRefresh(ActionEvent event) {
        try {
            buildTable();
            courseNoTxt.setText("");
            courseTitleTxt.setText("");
            sessionTxt.setText("");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onClickChecker(ActionEvent event) throws IOException {

        //System.out.println(coursesTable.getSelectionModel().getSelectedItem().toString());

        Object s = coursesTable.getSelectionModel().getSelectedItems().get(0);

        //System.out.println(s.toString().split(", ")[1].substring(1));

        String courseID = s.toString().split(", ")[0].substring(1); //got the 1st column of selected row -> first col = course id
        //System.out.println(courseID);
        
        TableLoader.CourseId = courseID;
        
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CourseDetails.fxml"));

        Parent rootPane = (Parent) loader.load();
        CourseDetailsController controller = loader.getController();
        controller.SetHeading(s.toString().split(", ")[2]);
        Scene dialogScene = new Scene(rootPane);
        dialog.setScene(dialogScene);
        dialog.show();
        
        //sceneLoader.loadPage("CourseDetails.fxml", this, event);
    }
        
}

