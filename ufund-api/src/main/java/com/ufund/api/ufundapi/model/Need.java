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
    
    static final String STRINGIFY = "Need [id=%d, name=%s, cost=%f, quantity=%]";

    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("cost") private double cost;
    @JsonProperty("quantity") private int quantity;
    @JsonProperty("type") private String type;
    /**
     * Create a Need with given id, name, and cost
     * @param id Id of the created Need
     * @param name Name of the created Need
     * @param cost Cost of the created Need
     * @JsonProperty used for mapping JSON to Java object
     */
    public Need(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("cost") double cost,
     @JsonProperty("quantity") int quantity, @JsonProperty("type") String type){
            this.id = id;
            this.name = name;
            this.cost = cost;
            this.quantity = quantity;
            
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
     * Set Cost of need for deserialization
     * @return double Cost, price of need
     */
    public void setCost(double cost) {this.cost = cost;}


    /**
     * Retrieves name of need
     * @return name of need
     */
    public String getName() {return name;}

    /**
     * Retrieves name of need
     * @return name of need
     */
    public double getCost() {return cost;}

    /**
     * @return String representation of a Need
     */
    @Override
    public String toString(){
        return String.format(STRINGIFY, id, name, cost, quantity, type);
    }
}
