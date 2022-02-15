/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Ineffable
 */

public class SceneLoader {
    
    
    public static boolean loadPage(String pageName, Object obj, ActionEvent event){
        /*
        Arguments pagename example -> "Dashboard.fxml"
        obj -> this
        event -> event
        */
        
        try {
            
                Parent root = FXMLLoader.load(obj.getClass().getResource(pageName));
                Scene currentScr = new Scene(root);
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.setScene(currentScr);
                currentStage.setMaximized(true);
                currentStage.show();
                return true;
        } catch (Exception e) {
                System.out.println("Cant Load Page: " + pageName +" Exception: " + e.toString());
                return false;
        }
    }
    
}
