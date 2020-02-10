package Warehouse;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import WarehouseModel.WarehouseModel;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class FXMLDocumentController implements Initializable {
    private WarehouseModel model;
    private String newVariety;
    private String currentVariety;
    private int addNumber;
    private int currentNum;
    
    @FXML
    private ListView choiceList;
    @FXML
    private TextField addNewField;
    @FXML
    private TextField addField;
    @FXML
    private ChoiceBox choice;
    
    @FXML
    private void addNewVariety(ActionEvent event) {
        newVariety = addNewField.getText();
        model.addNewVariety(newVariety);
    }
    
    @FXML
    private void addNew(ActionEvent event) {
        String add= addField.getText();
        addNumber=Integer.parseInt(add);
        model.addStock(newVariety, addNumber);
    }
    
    @FXML
    private void removeStock(ActionEvent event) {
        model.removeStock(newVariety, addNumber);
    }
    
    @FXML
    private void saveAndExit(ActionEvent event) {
        model.save();
        Platform.exit();
    }
    
    private void setVariety(Object newVarieties) {
        FilteredList<WarehouseModel> varietiesList = new FilteredList<WarehouseModel>(model.addNewVariety());
        choiceList.setItems(varietiesList);
    }
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        choice.getItems().addAll(model.getAllVarieties());
        choice.setValue(" ");
        choice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->setVariety(newValue));
        
        model = new WarehouseModel();
        try {
            currentNum = model.getStock(currentVariety);
            currentVariety = choice.getValue().toString();
            System.out.println(currentNum + "cases of " + currentVariety + " in stock.");
        } catch (Exception ex) {
            System.out.println("Can not load data file.");
            ex.printStackTrace();
        }
        
    }    
    
}
