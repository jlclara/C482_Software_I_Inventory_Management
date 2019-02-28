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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 *
 * @author Jenny Nguyen
 */
public class MainController implements Initializable {
    
    @FXML
    private TableView<Part> partsTv;
    @FXML
    private TableColumn<Part, Integer> partIDCol;
    @FXML
    private TableColumn<Part, String> partNameCol;
    @FXML
    private TableColumn<Part, Integer> partInvCol;
    @FXML
    private TableColumn<Part, String> partPriceCol;
    @FXML
    private TableView<Product> productsTv;
    @FXML
    private TableColumn<Product, Integer> productIDCol;
    @FXML
    private TableColumn<Product, String> productNameCol;
    @FXML
    private TableColumn<Product, Integer> productInvCol;
    @FXML
    private TableColumn<Product, Double> productPriceCol;
    @FXML
    private TextField partsSearchTf;
    @FXML
    private TextField productsSearchTf;
    private Part modifyPartSelectedItem;
    private Product modifyProductSelectedItem;
    private static int modItemIndex = -1;
   
    // Load Add Part Scene
    @FXML   
    public void AddPartClicked(ActionEvent e) throws IOException
    {
        Parent addPartScene = FXMLLoader.load(getClass().getResource("AddPart.fxml"));
        Scene scene = new Scene(addPartScene);
        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    
    // Load Mod Part Scene (modified Add Part Scene) if user has selected a part. 
    // Using int modItemIndex to specify which item is being modified.
    @FXML  
    public void ModifyPartClicked(ActionEvent e) throws IOException
    {
        modifyPartSelectedItem = partsTv.getSelectionModel().getSelectedItem();
        modItemIndex = partsTv.getSelectionModel().getSelectedIndex();
        if (modItemIndex == -1)
        {    
             Alert selectAlert = new Alert (Alert.AlertType.WARNING);
             selectAlert.setTitle("No Parts Selected");
             selectAlert.setHeaderText("Error: No Parts Selected");
             selectAlert.setContentText("Please select a part and try again.");
             selectAlert.showAndWait();                       
        }
        else
        {
            Parent addPartScene = FXMLLoader.load(getClass().getResource("AddPart.fxml"));
            Scene scene = new Scene(addPartScene);
            Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
    }
    
    // Delete selected part if valid. 
    @FXML
    public void DeletePartBtnClicked(ActionEvent e)
    {
        modifyPartSelectedItem = partsTv.getSelectionModel().getSelectedItem();
        modItemIndex = partsTv.getSelectionModel().getSelectedIndex();
        if (modItemIndex == -1)
        {    
             Alert selectAlert = new Alert (Alert.AlertType.WARNING);
             selectAlert.setTitle("No Parts Selected");
             selectAlert.setHeaderText("Error: No Parts Selected");
             selectAlert.setContentText("Please select a part and try again.");
             selectAlert.showAndWait();                       
        }
        else
        {

            if (Inventory.isPartFound(modifyPartSelectedItem))
            {
                Alert selectAlert = new Alert (Alert.AlertType.WARNING);
                selectAlert.setTitle("Part cannot be deleted!");
                selectAlert.setHeaderText("Error: Part cannot be deleted!");
                selectAlert.setContentText("The selected part is associated "
                        + "with a product.");
                selectAlert.showAndWait();      
               // break;
            }
            else
            {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Delete?");
                alert.setHeaderText("Are you sure you want to delete Part #" 
                        + modifyPartSelectedItem.getPartID()+ "?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK)
                {
                    Inventory.deletePart(modifyPartSelectedItem);
                    setModIndex();
                }
            }
            
        }
    }
    
    // Search parts inventory by name. If there are matching results, the part 
    // will appear in the table view. 
    @FXML
    public void  SearchPartsBtnClicked(ActionEvent e)
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
   
    
    // Load Add Product Scene
    @FXML
    public void AddProductBtnClicked (ActionEvent e) throws IOException
    {
        Parent addPartScene = FXMLLoader.load(getClass().getResource("AddProduct.fxml"));
        Scene scene = new Scene(addPartScene);
        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    
    // Load Mod Product Scene (modified Add Product Scene) if user has selected a product. 
    @FXML
    public void ModifyProductClicked (ActionEvent e) throws IOException 
    {
        modifyProductSelectedItem = productsTv.getSelectionModel().getSelectedItem();
        modItemIndex = Inventory.getProductsInventory().indexOf(modifyProductSelectedItem);
        if (modItemIndex == -1)
        {    
             Alert selectAlert = new Alert (Alert.AlertType.WARNING);
             selectAlert.setTitle("No Product Selected");
             selectAlert.setHeaderText("Error: No Product Selected");
             selectAlert.setContentText("Please select a product and try again.");
             selectAlert.showAndWait();                       
        }
        else
        {
            Parent addPartScene = FXMLLoader.load(getClass().getResource("AddProduct.fxml"));
            Scene scene = new Scene(addPartScene);
            Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
    }
    // Delete selected Product if valid. 
    @FXML 
    public void DeleteProductBtnClicked(ActionEvent e)
    {
        modifyProductSelectedItem = productsTv.getSelectionModel().getSelectedItem();
        modItemIndex = Inventory.getProductsInventory().indexOf(modifyProductSelectedItem);
        if (modItemIndex == -1)
        {    
             Alert selectAlert = new Alert (Alert.AlertType.WARNING);
             selectAlert.setTitle("No Product Selected");
             selectAlert.setHeaderText("Error: No Product Selected");
             selectAlert.setContentText("Please select a product and try again.");
             selectAlert.showAndWait();                       
        }
        else
        {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Delete?");
            alert.setHeaderText("Are you sure you want to delete Product #" +
                    modifyProductSelectedItem.getProductID() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK)
            {
                Inventory.removeProduct(modItemIndex);
                setModIndex();
            }
        }
    }
    
    // Search products inventory by name. If there are matching results, the 
    // products will appear in the table view.
    @FXML public void SearchProductsBtnClicked(ActionEvent e)
    {
        String searchQuery = productsSearchTf.getText();
        Product placeHolderProduct; 
        ObservableList<Product> productQuery = FXCollections.observableArrayList();
        for (int i = 0; i < Inventory.getProductsInventory().size(); i++)
        {
            if ((Inventory.getProductsInventory().get(i).getName().contains(searchQuery)))
            {
                placeHolderProduct = Inventory.lookupProduct(i);
                productQuery.add(placeHolderProduct);
            }
            productsTv.setItems(productQuery);
        }
    }
    
    // Close program
    @FXML
    public void ExitProgram (ActionEvent e)
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Exit Program?");
        alert.setHeaderText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
            System.exit(0);
    }
    
    // Popuplate the table view with data from parts and products inventory
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partIDCol.setCellValueFactory(new PropertyValueFactory<>("partID"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("partName"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("partInStock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("partPrice"));
        productIDCol.setCellValueFactory(new PropertyValueFactory<>("productID"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvCol.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        updateTableView();
    }

    // Update table view with parts and products inventory.
    public void updateTableView() {
        partsTv.setItems(Inventory.getPartsInventory());
        productsTv.setItems(Inventory.getProductsInventory());
    }    
    
    // for other classes to access the modItemIndex private variable
    public static int getModIndex()
    {
        return modItemIndex;
    }
    
    // for other classes to (re)set the modItemIndex private variable
    public static void setModIndex()
    {
        modItemIndex = -1;
    }
}
