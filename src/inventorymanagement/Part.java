package inventorymanagement;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Jenny Nguyen
 */
public abstract class Part {
    
 private IntegerProperty partID, partInStock, partMin, partMax;
 private StringProperty partName;
 private DoubleProperty partPrice;

public Part(){
    partID = new SimpleIntegerProperty();
    partName = new SimpleStringProperty();
    partInStock = new SimpleIntegerProperty();
    partMax = new SimpleIntegerProperty();
    partMin = new SimpleIntegerProperty();
    partPrice = new SimpleDoubleProperty();
}

public int getPartID() {
        return this.partID.get();
    }

    public String getPartName() {
        return this.partName.get();
    }

    public double getPartPrice() {
        return this.partPrice.get();
    }

    public int getPartInStock() {
        return this.partInStock.get();
    }

    public int getPartMin() {
        return this.partMin.get();
    }

    public int getPartMax() {
        return this.partMax.get();
    }

    //Setters
    
    public void setPartID(int partID) {
        this.partID.set(partID);
    }

    public void setPartName(String name) {
        this.partName.set(name);
    }

    public void setPartPrice(double price) {
        this.partPrice.set(price);
    }

    public void setPartInStock(int inStock) {
        this.partInStock.set(inStock);
    }

    public void setPartMin(int min) {
        this.partMin.set(min);
    }

    public void setPartMax(int max) {
        this.partMax.set(max);
    }
   
}
