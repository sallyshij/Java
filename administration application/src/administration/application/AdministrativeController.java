/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package administration.application;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author macpro
 */
public class AdministrativeController implements Initializable {

    private JDBC jdbc;
    private String itemTitle;
    private double itemCost;
    private String itemType;
    private int itemId;
    private String patronName;
    private String patronEmail;
    private int PatronId;
    private int lostID;
    
    @FXML
    private TextField type; 
    @FXML
    private TextField title;
    @FXML
    private TextField cost;
    
    @FXML
    private TextField name; 
    @FXML
    private TextField email;
    
    @FXML
    private Label id;
    @FXML
    private Label patronId;
    @FXML
    private TextField lostId;
    
    @FXML
    private void createItem(ActionEvent event) {
        itemTitle = title.getText();
        String ItemCost = cost.getText();
        itemCost = Double.parseDouble(ItemCost);
        itemType = type.getText();
        
        jdbc.createNewItem(itemTitle, itemCost, itemType);
        itemId = jdbc.getIDFromTitle(itemTitle);
        
        id.setText("Item ID: " + itemId);  
    }
    
    @FXML
    private void createPatron(ActionEvent event) {
        patronName = name.getText();
        patronEmail = email.getText();
        
        jdbc.createNewPatron(patronName, patronEmail);
        PatronId = jdbc.getIDFromName(patronName);
        
        patronId.setText("Patron ID: " + PatronId);

    }
    
    @FXML
    private void check(ActionEvent event) {
        PrintWriter output = null;
        try { 
            output = new PrintWriter(new File("overdue.txt"));
            jdbc.saveOverDue(output);
            output.close();
        } catch(Exception ex) {
            System.out.println("Error writing data to text file");
        }
    }
    
    @FXML
    private void Remove(ActionEvent event) {
        lostID = Integer.parseInt(lostId.getText());
        jdbc.remove(lostID);
    }
    
    @FXML
    private void ComputeFines(ActionEvent event) {
        jdbc.calculateFines(); 
    }
       
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        jdbc = new JDBC();
        jdbc.connect();
    }    
    
}
