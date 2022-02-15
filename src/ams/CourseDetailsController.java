/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ams;

import Utilities.SceneLoader;
import Utilities.TableLoader;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import ams.utils.DBConnect;
import com.sun.applet2.preloader.event.ConfigEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author swarn
 */
public class CourseDetailsController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private String qry_getAll;
    private String thiscourseid;
    private String searchQuery_;
    private TableLoader tableLoader;
    @FXML
    private TableView courseDetailsTbl;
    @FXML
    private Button addNewStd;
    @FXML
    private Button refresh;
    @FXML
    private MenuButton searchByMenu;
    @FXML
    private MenuItem searchBy_id;
    @FXML
    private MenuItem searchBy_name;
    @FXML
    private MenuItem searchBy_section;
    @FXML
    private MenuItem searchBy_semester;
    @FXML
    private TextField search_tf;
    @FXML
    private Button search_btn;
    
    private SceneLoader sceneLoader;
    @FXML
    private TextField id_tf;
    private TextField name_tf;
    private TextField section_tf;
    private TextField semester_tf;
    @FXML
    private Label headingLabel;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            //
            this.thiscourseid = "1001"; //how to get the course id from previous page?:/
            
            
            
            this.qry_getAll = "Select * from Students where Students.StudentID in (select distinct StudentCourseJoin.StudentID from StudentCourseJoin where CourseID = '" + this.thiscourseid + "')";

            
            buildTable(qry_getAll);
            searchByMenu.setText("Search by ID");
            this.searchQuery_ = qry_getAll + " and StudentId like ";
       
           
            headingLabel.setText(this.thiscourseid);
            headingLabel.setAlignment(Pos.CENTER);
         
        
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void buildTable(String q) throws ClassNotFoundException, SQLException {

        tableLoader.loadTable(q, courseDetailsTbl);
    }

    @FXML
    private void onClickInsert(ActionEvent event) throws ClassNotFoundException, SQLException {

        DBConnect dbc = new DBConnect();
        dbc.connectToDB();
        String id = this.id_tf.getText();
        
        

        String query = "INSERT INTO StudentCourseJoin "
                + "values('" + this.thiscourseid + "', '"+ id + "');";

        if (id.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty Field");
            alert.setContentText("Fill up all the text fields.");
            alert.show();
        } else {
            
            int status = dbc.insertDataToDB(query);
            
            if(status == 1) { //if insert success
                            
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success!");
                alert.setContentText(id +" has been added!");
                alert.show();
            }
            else { //insertion failed
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failed :(");
                alert.setContentText("Id exists?");
                alert.show();
            }

            
        }
        buildTable(qry_getAll);


    }

    @FXML
    private void onClickRefresh(ActionEvent event) {
        try {
            buildTable(qry_getAll);
            //System.out.println(qry_getAll);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onClickSearchById(ActionEvent event) {
        //set query
        this.searchQuery_ = qry_getAll + " and StudentID like ";
        searchByMenu.setText("Search By ID");

    }

    @FXML
    private void onClickSearchBySection(ActionEvent event) {
        //set query for search by seaction
        this.searchQuery_ = qry_getAll + " and section like ";
        searchByMenu.setText("Search By Section");
    }

    @FXML
    private void onClickSearchByName(ActionEvent event) {
        //set query
        this.searchQuery_ = qry_getAll + " and StudentName like ";
        searchByMenu.setText("Search By Name");
    }

    @FXML
    private void onClickSearchBySemester(ActionEvent event) {
        //set query
        this.searchQuery_ = qry_getAll + " and semester like ";
        searchByMenu.setText("Search By Semester");
    }

    @FXML
    private void onClickSearch(ActionEvent event) {
        try {

            if (search_tf.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Empty Search Field");
                alert.setContentText("Fill up search field.");
                alert.show();
            } else {

                buildTable(this.searchQuery_ + "'%" + search_tf.getText() + "%'");
                //System.out.println(this.searchQuery_ + "'%" + search_tf.getText() + "%'");
                
            }
        } 
        catch (Exception e){
            //this try catch not necessary maybe, used for debugging
        } 
    }
    
    
    private void debuglog(ActionEvent event) {
        //set query :: for debugging
        System.out.println(this.searchQuery_);
    }

    @FXML
    private void debuglog(MouseEvent event) {
    }

 


    
 

}
