/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorymanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Jenny Nguyen
 */
public class Inventory {
    
private static ObservableList<Product> productsInventory = FXCollections.observableArrayList();
private static ObservableList<Part> partsInventory = FXCollections.observableArrayList();
private static int partIDCount;
private static int productIDCount;

public Inventory() {}

public  static ObservableList<Part> getPartsInventory(){
    return partsInventory;
}
public static void addPart(Part p)
{
    partsInventory.add(p);
}
public static boolean deletePart(Part p)
{
    boolean removedSuccessful = partsInventory.remove(p);
    return removedSuccessful; 
}
public static Part  lookupPart(int iPart)
{
    Part targetPart = null;
    
    if (iPart < partsInventory.size()) 
    {
        targetPart = partsInventory.get(iPart);
    }
    else
    {
        System.out.println("No parts found!");
    }
    return targetPart;
    
}
public static void updatePart(int iPart, Part oPart)
{
    partsInventory.set(iPart, oPart);
}

public static Integer getPartIDCount()
{   
    partIDCount++;
    return partIDCount;
}

// to correct the count if the cancel button is clicked (nothing saved/created)
public static void setPartIDCount()
{
    partIDCount--;
}

// Products

public static Integer getProductIDCount()
{
    
    return ++productIDCount;
}

// to correct the count if the cancel button is clicked (nothing saved/created)
public static void setProductIDCount()
{
    productIDCount--;
}
public static void addProduct(Product p)
{
       productsInventory.add(p);
}
public static boolean removeProduct(int i)
{
    boolean removedSuccessful = false;
    if (i < productsInventory.size())
    {
        productsInventory.remove(i);
        removedSuccessful = true;
    }
    return removedSuccessful;
       
}
public static Product lookupProduct(int p)
{
    Product productSearch = null;
    if (p <productsInventory.size())
        productSearch = productsInventory.get(p);
    else
        System.out.println("No products found by product ID");
  
    return productSearch;
    
}

public static void updateProduct(int i, Product p)
{
    productsInventory.set(i, p);
}   

public static ObservableList<Product> getProductsInventory()
{
    return productsInventory;
}

public static boolean isPartFound(Part part)
{
    
    if (productsInventory.isEmpty())
            return false;
    else
    {
        for (int i = 0; i < productsInventory.size(); i++)
        {
            if (productsInventory.get(i).getPartsInventory().
                    contains(part))
                return true;
        }
    }
    return false;
}
public static String checkValues(int inv, int min, int max, double price)
{
    String message = new String();
    if (inv <1)
        message+= "Inventory level must be greater than 0. ";
    if (min > max)
        message+= "Minimum inventory must be less than maximum. ";
    if (inv < min || inv > max)
        message+= "Inventory level must be between maximum and minimum. ";
    if (price < 0)
        message+= "Price must be greater than 0.";

    return message;

}

public static String isProductValid(int inv, int min, int max, double price, 
        ObservableList<Part> parts, String message)
{
    double sumPartsPrice = 0.00;
    for (int i = 0; i < parts.size(); i++)
        sumPartsPrice += parts.get(i).getPartPrice();
    if (inv <1)
        message+= "Inventory level must be greater than 0. ";
    if (min > max)
        message+= "Minimum inventory must be less than maximum. ";
    if (inv < min || inv > max)
        message+= "Inventory level must be between maximum and minimum. ";
    if (parts.size() < 1)
        message+= "Product must contain at least 1  part. ";
    if (sumPartsPrice > price)
        message+= "Product price must be greater than cost of associated parts. ";
    
    return message;
}
}


