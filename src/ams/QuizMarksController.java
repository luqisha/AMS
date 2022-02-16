/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ams;

import ams.utils.DBConnect;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtFieldCourseName.setText("");
        txtFieldQuizNo.setText("");
        txtFieldSID.setText("");
        
        ObservableList<String> data = FXCollections.observableArrayList("CourseNo", "StudentID");
        dropDownFindBy.setItems(data);
    }    

    @FXML
    private void onClickFindMarks(ActionEvent event) throws ClassNotFoundException, SQLException {
        String courseName = txtFieldCourseName.getText();
        String QuizNo = txtFieldQuizNo.getText();
        String SID = txtFieldSID.getText();
        
        DBConnect dbc= new DBConnect();
        dbc.connectToDB();
        
        
        
        if(dropDownFindBy.getSelectionModel().getSelectedItem().equals("CourseNo"))
        {
            
            if(courseName.isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Empty Field");
                alert.setContentText("Write the name of the Course");
                alert.show();
            }
            else if(QuizNo.isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Empty Field");
                alert.setContentText("Write the QuizNo");
                alert.show();
            }
            else
            {
                String query="SELECT StudentID, QuizMarks FROM QuizMark "
                + "WHERE CourseID = (SELECT CourseID FROM courses WHERE CourseNo='"+courseName+"') AND QuizNo ='"+QuizNo+"';";
            
                buildTable(query, quizMarksTableView);
            }
            
        }
        else if(dropDownFindBy.getSelectionModel().getSelectedItem().equals("StudentID"))
        {
            
            if(courseName.isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Empty Field");
                alert.setContentText("Write the name of the Course!");
                alert.show();
            }
            else if(SID.isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Empty Field");
                alert.setContentText("Write the Student ID!");
                alert.show();
            }
            else
            {
                String query="SELECT QuizNo, QuizMarks FROM QuizMark "
                + "WHERE CourseID = (SELECT CourseID FROM courses WHERE CourseNo='"+courseName+"') AND StudentID ='"+SID+"';";
            
                buildTable(query, quizMarksTableView);
            }
            
        }
        else
        {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Option Selected");
                alert.setContentText("Select what you want to find data by! ");
                alert.show();
        }
          
    }
    
    public void buildTable(String query, TableView tView) throws ClassNotFoundException, SQLException {
        tView.getItems().clear();
        tView.getColumns().clear();
        DBConnect dbc = new DBConnect();
        dbc.connectToDB();
        data = FXCollections.observableArrayList();
        try {
            
            //SQL FOR SELECTING ALL OF CUSTOMER
            
            //ResultSet
            ResultSet rs = dbc.queryToDB(query);

            /**
             * ********************************
             * TABLE COLUMN ADDED DYNAMICALLY *
             *********************************
             */
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
 
                tView.getColumns().addAll(col);
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
            tView.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    
    dbc.disconnectFromDB();
    }

    @FXML
    private void onClickReset(ActionEvent event) {
        txtFieldCourseName.setText("");
        txtFieldQuizNo.setText("");
        txtFieldSID.setText("");
        dropDownFindBy.getSelectionModel().clearSelection();
        txtFieldSID.setDisable(false);
        txtFieldQuizNo.setDisable(false);
    }

    @FXML
    private void onClickDropDown(ActionEvent event) {
        if(dropDownFindBy.getSelectionModel().getSelectedItem().equals("CourseNo"))
        {
            txtFieldSID.setText("");
            txtFieldSID.setDisable(true);
            txtFieldQuizNo.setDisable(false);
        }
        else if (dropDownFindBy.getSelectionModel().getSelectedItem().equals("StudentID"))
        {
            txtFieldQuizNo.setText("");
            txtFieldQuizNo.setDisable(true);
            txtFieldSID.setDisable(false);
        }
    }
    
}
