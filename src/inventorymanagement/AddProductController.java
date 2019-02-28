package inventorymanagement;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * Add Product Controller class
 *
 * @author Jenny Nguyen
 */
public class AddProductController implements Initializable {

    @FXML
    private TableView<Part> partsTv;
    @FXML
    private TableColumn<Part, Integer> partIDCol;
    @FXML
    private TableColumn<Part, String> partNameCol;
    @FXML
    private TableColumn<Part, Integer> partInvCol;
    @FXML
    private TableColumn<Part, Double> partPriceCol;
    @FXML
    private TableView<Part> partsInProductTv;
    @FXML
    private TableColumn<Part, Integer> pipIDCol;
    @FXML
    private TableColumn<Part, String> pipNameCol;
    @FXML
    private TableColumn<Part, Integer> pipInvCol;
    @FXML
    private TableColumn<Part, Double> pipPriceCol;
    @FXML 
    private Label addProductLabel;
    @FXML
    private TextField productNameTf, productMaxTf, productMinTf, 
            productInvTf, productIDtf, productPriceTf, partsSearchTf;
    private int productID, modIndex, partToRemove;
    private Product oProduct =  new Product();
    private String checkValuesMessage = new String();
   
    // add selected part to Product's associated part only if a part is selected
    @FXML
    public void addAssociatedPartClicked(ActionEvent e) {
        
        Part selectedPart = partsTv.getSelectionModel().getSelectedItem();
        if (selectedPart != null)
        {
            oProduct.addAssociatedPart(selectedPart);
            updatePartsInProductsTv();
        }    
    }
    
    @FXML
    public void deletedAssociatedPartClicked(ActionEvent e) {
        Part selectedPart = partsInProductTv.getSelectionModel().getSelectedItem();
        if (selectedPart != null)
        {
           Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Delete?");
            alert.setHeaderText("Are you sure you want to delete Part #" + selectedPart.getPartID() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK)
            {
                partToRemove = oProduct.getPartsInventory().indexOf(selectedPart);
                oProduct.removeAssociatedPart(partToRemove);
                updatePartsInProductsTv();
            }
           
        }
    }
            
    @FXML 
    public void saveProductClicked(ActionEvent e) throws IOException {
        
        // parse and save data inputted from text fields if valid
        try { 
            String productName = productNameTf.getText(); 
            Integer productInv = Integer.parseInt(productInvTf.getText()); 
            Double productPrice = Double.parseDouble(productPriceTf.getText());
            Integer productMin = Integer.parseInt(productMinTf.getText());
            Integer productMax = Integer.parseInt(productMaxTf.getText());
            
            checkValuesMessage = Inventory.isProductValid(productInv,productMin,
                    productMax, productPrice, oProduct.getPartsInventory(), checkValuesMessage);
                        
            // check functional logic
            if (checkValuesMessage.length()>0)
                throw new Exception();
         
            
            // once all the data is validated, set oProduct's attributes
            oProduct.setProductID(productID);
            oProduct.setName(productName);
            oProduct.setInStock(productInv);
            oProduct.setPrice(productPrice);
            oProduct.setMax(productMax);
            oProduct.setMin(productMin);
            
            if (MainController.getModIndex() == -1)  // add product
            {
                oProduct.setAssociatedParts(partsInProductTv.getItems()); 
                Inventory.addProduct(oProduct);
            }
            else // modify product
            {
                Inventory.updateProduct(modIndex, oProduct);  
                MainController.setModIndex();
            }           
            
            // load Main scene
            Parent productsSave = FXMLLoader.load(getClass().getResource("Main.fxml"));
            Scene scene = new Scene(productsSave);
            Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } 
        catch (NumberFormatException ex) {
            Alert inputAlert = new Alert (Alert.AlertType.WARNING);
            inputAlert.setTitle("Invalid Input Detected");
            inputAlert.setHeaderText("Error: Invalid input detected.");
            inputAlert.setContentText("Please check input and try again.");
            inputAlert.showAndWait();
        }    
        catch (Exception exc)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Saving Product");
            alert.setHeaderText("Error Saving Product");
            alert.setContentText(checkValuesMessage);
            alert.showAndWait();
            checkValuesMessage = "";
        }
    }
    
    // search parts by name
    @FXML
    public void SearchPartsBtnClicked(ActionEvent e)
    {
        String searchQuery = partsSearchTf.getText();
        Part placeHolderPart; 
        ObservableList<Part> partsQuery = FXCollections.observableArrayList();
        for (int i = 0; i < Inventory.getPartsInventory().size(); i++)
        {
            if ((Inventory.getPartsInventory().get(i).getPartName().contains(searchQuery)))
            {
                placeHolderPart = Inventory.lookupPart(i);
                partsQuery.add(placeHolderPart);
            }
            partsTv.setItems(partsQuery);
        }
    }
    
    // load Main scene
    @FXML 
    public void cancelBtnPressed(ActionEvent e) throws IOException
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Cancel Adding Products?");
        alert.setHeaderText("Are you sure you want to cancel adding products?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            if (MainController.getModIndex() == -1)
                Inventory.setProductIDCount();

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
        
//      if Modifying Products, set pre-saved data into bottom 
//      table view and property attribute textfields
//      else, set the product ID for the new product

        if (MainController.getModIndex() != -1)
        {
           
            addProductLabel.setText("Modify Product"); 
            modIndex = MainController.getModIndex();
            oProduct = Inventory.getProductsInventory().get(modIndex);
            productID = oProduct.getProductID();
            productNameTf.setText(oProduct.getName());
            productInvTf.setText(Integer.toString(oProduct.getInStock()));
            productMaxTf.setText(Integer.toString(oProduct.getMax()));
            productMinTf.setText(Integer.toString(oProduct.getMin()));
            productPriceTf.setText(Double.toString(oProduct.getPrice()));
            updatePartsInProductsTv();
        }
        else
            productID = Inventory.getProductIDCount();

//      load the 2 tables and the product ID
        
        partIDCol.setCellValueFactory(new PropertyValueFactory<>("partID"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("partName"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("partInStock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("partPrice"));
        pipIDCol.setCellValueFactory(new PropertyValueFactory<>("partID"));
        pipNameCol.setCellValueFactory(new PropertyValueFactory<>("partName"));
        pipInvCol.setCellValueFactory(new PropertyValueFactory<>("partInStock"));
        pipPriceCol.setCellValueFactory(new PropertyValueFactory<>("partPrice"));
        partsTv.setItems(Inventory.getPartsInventory());
        productIDtf.setText("AutoGen: " +Integer.toString(productID));
        
    }    
    
    // update parts in product table view
    public void updatePartsInProductsTv()
    {
        partsInProductTv.setItems(oProduct.getPartsInventory());
    }
}
