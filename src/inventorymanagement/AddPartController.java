package inventorymanagement;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;

/**
 * Add Part Controller class
 *
 * @author Jenny Nguyen
 */
public class AddPartController implements Initializable {
   
    @FXML
    private Label inOutLabel, partTitle;
    @FXML
    private TextField partPriceTf, partMaxTf, partMinTf, 
            partInvTf, partNameTf, partIDtf, inOutTf;
    @FXML 
    private RadioButton inHouseRb, outsourcedRb;
    private boolean isOutsourced;
    private int partID, modIndex;
    private Part modPart;
    
    // Switch between outsourced and inhouse
    @FXML
    public void switchToOutsourcedView(ActionEvent e) {
        inOutTf.setPromptText("Comp Nm");
        inOutLabel.setText("Company Name");
        isOutsourced = true;
    }
    @FXML
    public void switchToInHouseView(ActionEvent e) {
        inOutTf.setPromptText("Mach ID");
        inOutLabel.setText("Machine ID");
        isOutsourced = false;
    }

    @FXML
    public  void savePartBtnClicked(ActionEvent e) throws IOException {  

         String checkValuesMessage = new String(); 
         
         try 
         { 
            // parse and get data from text fields. If there is an issue
            // an exception is handled below. 
            String partName = partNameTf.getText(); 
            Integer partInv = Integer.parseInt(partInvTf.getText()); 
            Double partPrice = Double.parseDouble(partPriceTf.getText());
            Integer partMin = Integer.parseInt(partMinTf.getText());
            Integer partMax = Integer.parseInt(partMaxTf.getText());
            String companyName = inOutTf.getText();
            Integer machID = null;
            if (!isOutsourced)
                machID = Integer.parseInt(inOutTf.getText());
            
            // check the inputted values to make sure they fall within functional logic
            checkValuesMessage = Inventory.checkValues(partInv,partMin,partMax, partPrice);
            
            // exception is thrown with issues encountered with inputted data
            if (checkValuesMessage.length()>0)
                throw new Exception();
          
            // using placeholder part to store data, then add or update part
            if (isOutsourced)  // for outsourced parts
            { 
                Outsourced oPart =  new Outsourced();
                oPart.setPartID(partID);
                oPart.setPartName(partName);
                oPart.setPartInStock(partInv);
                oPart.setPartPrice(partPrice);
                oPart.setPartMax(partMax);
                oPart.setPartMin(partMin);
                oPart.setCompanyName(companyName);
                if (MainController.getModIndex() == -1)  // add outsourced part
                    Inventory.addPart(oPart);
                
                else  // modify outsourced part        
                    Inventory.updatePart(modIndex, oPart);          
            }
            else  // for in-house parts
            {
                InHouse oPart = new InHouse();
                oPart.setPartID(partID);
                oPart.setPartName(partName);
                oPart.setPartInStock(partInv);
                oPart.setPartPrice(partPrice);
                oPart.setPartMax(partMax);
                oPart.setPartMin(partMin);
                oPart.setMachineID(machID);
                if (MainController.getModIndex() == -1) // add in-house part
                    Inventory.addPart(oPart);
                else // modify in-house part
                    Inventory.updatePart(modIndex, oPart); 
            }

             MainController.setModIndex();  // reset modItemIndex int
             
             // go back to main scene when complete
             Parent partsSave = FXMLLoader.load(getClass().getResource("Main.fxml"));
             Scene scene = new Scene(partsSave);
             Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
             window.setScene(scene);
             window.show();
         } 
         catch (NumberFormatException ex) {
             Alert inputAlert = new Alert (AlertType.WARNING);
             inputAlert.setTitle("Invalid Input Detected");
             inputAlert.setHeaderText("Error: Invalid input detected.");
             inputAlert.setContentText("Please check input and try again.");
             inputAlert.showAndWait();
         }
         catch (Exception exc)
         {
             Alert alert = new Alert(Alert.AlertType.INFORMATION);
             alert.setTitle("Error Saving Part");
             alert.setHeaderText("Error Saving Part");
             alert.setContentText(checkValuesMessage);
             alert.showAndWait();
         }
    }

    // go back to main scene 
    @FXML
    public void cancelPartBtnClicked(ActionEvent e) throws IOException {
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Cancel Adding Parts?");
        alert.setHeaderText("Are you sure you want to cancel adding parts?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            if (MainController.getModIndex() == -1)
                Inventory.setPartIDCount();

            MainController.setModIndex();       
            Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
            Scene scene = new Scene(root);
            Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
       

    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        
        if (MainController.getModIndex() != -1) // modify part; load pre-saved data
         {
             partTitle.setText("Modify Parts"); 
             modIndex = MainController.getModIndex();
             modPart = Inventory.getPartsInventory().get(modIndex);
             partID = modPart.getPartID();
             partIDtf.setText("Auto Gen: " + Integer.toString(partID));
             partNameTf.setText(modPart.getPartName());
             partInvTf.setText(Integer.toString(modPart.getPartInStock()));
             partMaxTf.setText(Integer.toString(modPart.getPartMax()));
             partMinTf.setText(Integer.toString(modPart.getPartMin()));
             partPriceTf.setText(Double.toString(modPart.getPartPrice()));

             if (modPart instanceof InHouse)
             {
                 inOutLabel.setText("Machine ID");
                 inOutTf.setText(Integer.toString(((InHouse) modPart).getMachineID()));
                 inHouseRb.setSelected(true);
                 isOutsourced = false;
             }
             else if (modPart instanceof Outsourced)
             {
                 inOutLabel.setText("Company Name");
                 inOutTf.setText(((Outsourced) modPart).getCompanyName());
                 outsourcedRb.setSelected(true);
                 isOutsourced = true;
             }

         }
        else  // add part; no pre-saved data, auto generate part ID
         {
             partID = Inventory.getPartIDCount();
             partIDtf.setText("Auto Gen: " + Integer.toString(partID));
         }
     }        

}
