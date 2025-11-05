package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents Need Object
 * @author CTRL
 */

public class Need {    
    static final String STRINGIFY = "Need [id=%d, name=%s, value=%f, currentQuantity=%d, maxQuantity=%d,type=%s, visible=%b]";

    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("value") private double value;
    @JsonProperty("currentQuantity") int currentQuantity;
    @JsonProperty("maxQuantity") int maxQuantity;
    @JsonProperty("type") private String type;
    @JsonProperty("visible") private boolean visible;
    /**
     * Create a Need with given id, name, and value
     * @param id Id of the created Need
     * @param name Name of the created Need
     * @param value value of the created Need
     * {@literal @} used for mapping JSON to Java object. JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public Need(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("value") double value,
     @JsonProperty("currentQuantity") int currentQuantity, @JsonProperty("maxQuantity") int maxQuantity, @JsonProperty("type") String type, @JsonProperty("visible") boolean visible){
            this.id = id;
            this.name = name;
            this.value = value;
            this.currentQuantity = currentQuantity;
            this.maxQuantity = maxQuantity;
            this.type = type;
            this.visible = visible;

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
    * retrieves currentQuantity of need
    * @return currentQuantity
    */
    public int getCurrentQuantity() {return currentQuantity;}

    /**
    * retrieves currentQuantity of need
    * @return maxQuantity
    */
    public int getMaxQuantity() {return maxQuantity;}

    /**
     * retrieves type of need
     * @return type
     */
    public String getType() {return type;}

    /**
     * Set currentQuantity of this need
     * @param currentQuantity
     */
    public void setCurrentQuantity(int currentQuantity) {this.currentQuantity = currentQuantity;}

    /**
     * Set currentQuantity of this need
     * @param maxQuantity
     */
    public void setMaxQuantity(int maxQuantity) {this.maxQuantity = maxQuantity;}

    /**
     * Set type of this need 
     * @param type
     */
    public void setType(String type) {this.type = type;}
    
    /**
     * retrieves need's visibility
     * @return visible
     */
    
    public boolean getVisible() {return visible;}
    /**
     * Set visibility of this need
     * @param visible
     */
    public void setVisible(boolean visible) {this.visible = visible;}

    /**
     * @return String representation of a Need
     */
    @Override
    public String toString(){
        return String.format(STRINGIFY, id, name, value, currentQuantity, maxQuantity, type, visible);
    }
}
