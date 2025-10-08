package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the Need class
 * 
 * @author CTRL Team
 */

@Tag("Model-tier")
public class NeedTest {
    @Test
    public void testCtor() {
        // Setup
        int expected_id = 99;
        String expected_name = "Breadboard";
        double expected_value = 30.0;
        int expected_quantity = 6;
        String expected_type = "board";

        // Invoke
        Need need = new Need(expected_id, expected_name, expected_value, expected_quantity, expected_type);

        // Analyze
        assertEquals(expected_id,need.getId());
        assertEquals(expected_name,need.getName());
    }

    @Test
    public void testName() {
        // Setup
        int id = 99;
        String name = "Breadboard";
        double value = 30.0;
        int quantity = 6;
        String type = "board";
        Need need = new Need(id, name, value, quantity, type);

        String expected_name = "Circuit Board";

        // Invoke
        need.setName(expected_name);

        // Analyze
        assertEquals(expected_name,need.getName());
    }

    @Test
    public void testToString() {
        // Setup
        int id = 99;
        String name = "Breadboard";
        double value = 30.0;
        int quantity = 6;
        String type = "board";
        String expected_string = String.format(Need.STRINGIFY,id,name, value, quantity, type);
        Need need = new Need(id, name, value, quantity, type);

        // Invoke
        String actual_string = need.toString();

        // Analyze
        assertEquals(expected_string,actual_string);
    }
}