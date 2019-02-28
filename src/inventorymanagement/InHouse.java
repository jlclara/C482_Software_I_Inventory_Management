/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorymanagement;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Alert;

/**
 *
 * @author Jenny Nguyen
 */
public class InHouse extends Part {
    
private IntegerProperty machineID;

    public InHouse(){
    
        super();
        machineID = new SimpleIntegerProperty();

    }

    public int getMachineID() {
        return this.machineID.get();
    }

    public void setMachineID(int machineID) { 
        this.machineID.set(machineID);
        
    }

}
