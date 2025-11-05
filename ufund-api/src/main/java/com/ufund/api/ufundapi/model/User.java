package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an individual user, each has:
 * the user's username as a String
 * the user's funding basket as a list of needs
 * @author CTRL
 */
public class User {
    static final String STRINGIFY = "User [name=%s, needs=%s]";

    @JsonProperty("userName") private String name;
    @JsonProperty("basket") private Need[] needs;
    /**
     * Create a User with given, name
     * @param name Name of the created User
     * @param needs values of the User's Needs
     * {@literal @} used for mapping JSON to Java object. JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public User(@JsonProperty("userName") String name, @JsonProperty("basket") Need[] needs){
        this.name = name;
        this.needs = needs;
    }

    // Getters //
    public String getName() {
        return name;
    }

    public Need[] getBasket() {
        return needs;
    }

    /**
     * @return String representation of a User
     */
    @Override
    public String toString(){
        return String.format(STRINGIFY, name, needs);
    }
}
