package ams.utils;

import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javax.swing.JOptionPane;

/**
 *
 * @author Ashiq
 */
public class DBConnect {
    private Statement stmt = null;
    private Connection connection;
    
    public void connectToDB() throws ClassNotFoundException, SQLException{
        System.out.println("DB connecting..............");
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String connectionUrl = "jdbc:sqlserver://localhost:1433;user=sa;password=p@ssword13;" + "databaseName=dbproject;";
        connection = DriverManager.getConnection(connectionUrl);
        System.out.println("Connected to database successfully.");
        java.sql.Statement stmt = connection.createStatement();
    }
    
    public void disconnectFromDB(){

    try{
        if (stmt != null){stmt.close();}
        if (connection != null){connection.close();}
    }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Unable to close connection");
        }
    }

    public int insertDataToDB(String query)
     {
        try
        {
            java.sql.Statement stmt=connection.createStatement();
            int returnValue = stmt.executeUpdate(query);
            return returnValue;
        }
        catch (SQLException ex)
        {
            System.out.println("Update Execution Failed");
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }        
     }
    
    public ResultSet queryToDB(String query)
     {
        try
        {
            java.sql.Statement stmt=connection.createStatement();
            
            ResultSet rs = stmt.executeQuery(query);
            return rs;
            
        }
        catch (SQLException ex)
        {
            System.out.println("Query Failed");
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }        
     }
}
