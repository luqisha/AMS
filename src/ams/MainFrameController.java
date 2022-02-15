/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ams;

import ams.utils.SceneLoader;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author swarn
 */
public class MainFrameController implements Initializable {

    @FXML
    private JFXHamburger hamburger;
    @FXML
    private JFXDrawer drawer;
    private SceneLoader sceneLoader;
    @FXML
    private AnchorPane childAnchor;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("VDrawer.fxml"));
            VBox vbox = loader.load();
            VDrawerController vdc = loader.getController();
            vdc.anchorPane = childAnchor;
            drawer.setSidePane(vbox);
            
        } catch (IOException ex) {
            Logger.getLogger(MainFrameController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamburger);
        transition.setRate(transition.getRate()* -1);
        hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
             
            transition.setRate(transition.getRate()* -1);
            transition.play();
            
            if(drawer.isOpened()) {
                drawer.close();
                childAnchor.setTranslateX(0);
            }
            else {
                
                childAnchor.setTranslateX(200);
                drawer.open();
            }
        });

    }    


}
