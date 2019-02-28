/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorymanagement;

/**
 *
 * @author Jenny Nguyen
 */
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {
    
 private IntegerProperty productID;
 private StringProperty name;
 private IntegerProperty inStock;
 private DoubleProperty price;
 private IntegerProperty min;
 private IntegerProperty max;
 private ObservableList<Part> associatedParts = FXCollections.
         observableArrayList();

    public Product() 
    {
        productID = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
        inStock = new SimpleIntegerProperty();
        price = new SimpleDoubleProperty();
        min = new SimpleIntegerProperty();
        max = new SimpleIntegerProperty();
    }
    public ObservableList<Part> getPartsInventory()
    {
        return associatedParts;
    }
    
    public void setAssociatedParts(ObservableList<Part> oLP) 
    {
        associatedParts = oLP;
    }
    
    public String getName() {
        return name.get();
    }
 
    public void setName(String pName) {
        name.set(pName);
    }
        
    public int getProductID(){
        return productID.get();
    }
    public void setProductID(int pId){
        productID.set(pId);
    }
    
    public int getInStock(){
        return inStock.get();
    }
    
    public void setInStock(int iLvl){
        inStock.set(iLvl);
    }    
    
    public double getPrice(){
        return price.get();
    }
    
    public void setPrice(double pCost){
        price.set(pCost);
    }  
      public void setMin(int minPrice)
    {
        min.set(minPrice);
    }
    
    public int getMin()
    {
        return min.get();
    }
    
    public void setMax(int maxPrice)
    {
        max.set(maxPrice);
    }
    
    public int getMax()
    {
        return max.get();
    }
    
    public void addAssociatedPart(Part p)
    {
        associatedParts.add(p);
    }
    
    public boolean removeAssociatedPart(int i)
    {
        boolean removedSuccessful = false;
        if (i <= associatedParts.size())
        {
            associatedParts.remove(i);
            removedSuccessful = true;
        }
        
        return removedSuccessful;
        
    }    
    
    public void updateAssociatedPart (int i, Part p)
    {
        associatedParts.set(i, p);
    }
    
    public Part lookupAssociatedPart(int i)
    {
        Part part = associatedParts.get(i);
        return part;
    }
}