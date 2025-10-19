package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufund.api.ufundapi.model.Need;

/*
 * Represents an individual user, each has:
 * the user's username as a String
 * the user's funding basket as a list of needs
 */
public class User {
    @JsonProperty("userName") private String name;
    @JsonProperty("basket") private Need[] needs;

    public String getName() {
        return name;
    }

    public Need[] getBasket() {
        return needs;
    }
}
