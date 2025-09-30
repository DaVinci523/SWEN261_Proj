package com.ufund.api.ufundapi.model;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents Need Object
 * @author CTRL
 */

public class Need {
    private static final Logger LOG = Logger.getLogger(Need.class.getName());
    
    static final String STRINGIFY = "Need [id=%d, name=%s, value=%f, quantity=%]";

    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("value") private double value;
    @JsonProperty("quantity") private int quantity;
    @JsonProperty("type") private String type;
    /**
     * Create a Need with given id, name, and value
     * @param id Id of the created Need
     * @param name Name of the created Need
     * @param value value of the created Need
     * @JsonProperty used for mapping JSON to Java object
     */
    public Need(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("value") double value,
     @JsonProperty("quantity") int quantity, @JsonProperty("type") String type){
            this.id = id;
            this.name = name;
            this.value = value;
            this.quantity = quantity;
            this.type = type;
    }

    /**
     * Retrevies Needs id
     * @return id of need
     */
    public int getId() {return id;}

    /**
     * set name of need for deserialization
     * @param name
     */
    public void setName(String name) {this.name = name;}
    /**
     * Set value of need for deserialization
     * @return double value, price of need
     */
    public void setValue(double value) {this.value = value;}


    /**
     * Retrieves name of need
     * @return name of need
     */
    public String getName() {return name;}

    /**
     * Retrieves name of need
     * @return name of need
     */
    public double getValue() {return value;}
    /**
    * retrieves quantity of need
    * @return quantity
    */
    public int getQuantity() {return quantity;}
    /**
     * retrieves type of need
     * @return type
     */
    public String getType() {return type;}
    /**
     * Set quantity of this need
     * @param quantity
     */
    public void setQuantity(int quantity) {this.quantity = quantity;}

    /**
     * Set type of this need 
     * @param type
     */
    public void setType(String type) {this.type = type;}

    /**
     * @return String representation of a Need
     */
    @Override
    public String toString(){
        return String.format(STRINGIFY, id, name, value, quantity, type);
    }
}
