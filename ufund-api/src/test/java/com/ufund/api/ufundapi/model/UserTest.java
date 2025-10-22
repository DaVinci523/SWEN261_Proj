package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit test for the User class
 * 
 * @author: Anthony Pelto
 */

@Tag("Model-tier")
public class UserTest {
    @Test
    public void testToString(){
        // Setup
        Need need1 = new Need(1, "Breadboard", 30.0, 6, "board");
        Need need2 = new Need(2, "Resistor", 5.0, 10, "component");
        Need[] needs = {need1, need2};
        String username = "user";
        User user = new User(username, needs);

        String expected = String.format(User.STRINGIFY, username, needs);

        String actual = user.toString();

        assertEquals(expected, actual);
    }
}
