/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ams;

import ams.utils.DBConnect;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

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
    @FXML
    private Button updateBtn;
    
    private ObservableList<ObservableList> data;
 
    private TableView tableview;
    @FXML
    private Button refrestableBtn;

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
    private void onClickUpdate(ActionEvent event) {
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
        coursesTable.getItems().clear();
        coursesTable.getColumns().clear();
        DBConnect dbc = new DBConnect();
        dbc.connectToDB();
        data = FXCollections.observableArrayList();
        try {
            
            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL = "SELECT * from Courses";
            //ResultSet
            ResultSet rs = dbc.queryToDB(SQL);

            /**
             * ********************************
             * TABLE COLUMN ADDED DYNAMICALLY *
             *********************************
             */
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
 
                coursesTable.getColumns().addAll(col);
                System.out.println("Column [" + i + "] ");
            }
 
            /**
             * ******************************
             * Data added to ObservableList *
             *******************************
             */
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added " + row);
                data.add(row);
 
            }
 
            //FINALLY ADDED TO TableView
            coursesTable.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    
    dbc.disconnectFromDB();
    }

    @FXML
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
        
}
