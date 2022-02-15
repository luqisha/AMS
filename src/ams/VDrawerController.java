/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ams;

import com.sun.deploy.util.FXLoader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author swarn
 */
public class VDrawerController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    public AnchorPane anchorPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void onClickDashboard(ActionEvent event) throws IOException {
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add((Node)FXMLLoader.load(getClass().getResource("Dashboard.fxml")));
    }

    @FXML
    private void onClickCourseDetails(ActionEvent event) {
        System.out.println("tickled course details");
    }

    @FXML
    private void OnClickAttendance(ActionEvent event) throws IOException {
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add((Node)FXMLLoader.load(getClass().getResource("Attendance.fxml")));
    }
    
}
