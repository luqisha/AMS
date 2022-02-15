/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ams;

import ams.utils.SceneLoader;
import ams.utils.TableLoader;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import animatefx.animation.*;
import ams.utils.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ashiq
 */
public class LoginController implements Initializable {

    @FXML
    private Pane panelSignUp;
    @FXML
    private TextField signUpName;
    @FXML
    private TextField signUpUsername;
    @FXML
    private TextField signUpEmail;
    @FXML
    private PasswordField signUpPassword;
    @FXML
    private Button signUpBtn;
    @FXML
    private ImageView backBtnToSignIn;
    @FXML
    private Pane panelSignIn;
    @FXML
    private Button signInBtn;
    @FXML
    private Text errTextSignIn;
    @FXML
    private Button createAccountBtn;
    @FXML
    private TextField signInUsername;
    @FXML
    private PasswordField signInPassword;
    @FXML
    private Text confirmSignUpTxt;
    
    private SceneLoader sceneLoader;
    public LoginController() {

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handleMouseClicked(MouseEvent event) {
        if (event.getSource() == backBtnToSignIn) {
            new LightSpeedIn(panelSignIn).play();
            panelSignIn.toFront();
        }
    }

    @FXML
    private void onClickCreateAccount(ActionEvent event) {
        new Bounce(panelSignUp).play();
        panelSignUp.toFront();
    }

    @FXML
    private void onClickSignIn(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
        DBConnect dbc = new DBConnect();
        String username = signInUsername.getText();
        String password = signInPassword.getText();

        dbc.connectToDB();
        if (username.isEmpty() || password.isEmpty()) {
            errTextSignIn.setText("Username/Password can't be empty!");
        } else {
            ResultSet rs = dbc.queryToDB("select * from Users where username='" + username + "' and password='" + password + "';");
            sceneLoader.loadPage("MainFrame.fxml", this, event);
            
            if (rs == null || !rs.next()) {
                errTextSignIn.setText("Enter Valid Username/Password");
                new Shake(panelSignIn).play();
            } else {
                System.out.println("Signed In Successfully.");
            }
        }

        dbc.disconnectFromDB();
    }
    

    @FXML
    private void onClickSignUp(ActionEvent event) throws ClassNotFoundException, SQLException {
        String name = signUpName.getText(),
                username = signUpUsername.getText(),
                email = signUpEmail.getText(),
                password = signUpPassword.getText();

        DBConnect dbc = new DBConnect();
        dbc.connectToDB();

        String query = "INSERT INTO Users (username, name, email, password)"
                + "values('" + username + "','" + name + "','" + email + "','" + password + "');";

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty Field");
            alert.setContentText("Fill up all the text fields.");
            alert.show();
        } else if (dbc.insertDataToDB(query) == 1) {

            confirmSignUpTxt.setText("Signed Up Successfully!");
            confirmSignUpTxt.setFill(Color.GREEN);

            signUpName.setText("");
            signUpUsername.setText("");
            signUpEmail.setText("");
            signUpPassword.setText("");
        } else {
            new Shake(panelSignUp).play();
            confirmSignUpTxt.setText("Signed Up Failed!");
            confirmSignUpTxt.setFill(Color.RED);
        }
        
        

        dbc.disconnectFromDB();
    }

}
