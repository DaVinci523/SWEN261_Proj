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
        int expected_current_quantity = 6;
        int expected_max_quantity = 7;
        String expected_type = "board";
        boolean expected_visible = true;

        // Invoke
        Need need = new Need(expected_id, expected_name, expected_value, expected_current_quantity, expected_max_quantity, expected_type, expected_visible);

        // Analyze
        assertEquals(expected_id,need.getId());
        assertEquals(expected_name,need.getName());
        assertEquals(expected_value,need.getValue());
        assertEquals(expected_current_quantity,need.getCurrentQuantity());
        assertEquals(expected_max_quantity,need.getMaxQuantity());
        assertEquals(expected_type,need.getType());
        assertEquals(expected_visible, need.getVisible());
    }

    @Test
    public void testValue() {
        // Setup
        int id = 99;
        String name = "Breadboard";
        double value = 30.0;
        int currentQuantity = 0;
        int maxQuantity = 6;
        String type = "board";
        boolean visible = true;
        Need need = new Need(id, name, value, currentQuantity, maxQuantity, type, visible);

        Double expected_value = 40.0;

        // Invoke
        need.setValue(expected_value);

        // Analyze
        assertEquals(expected_value,need.getValue());
    }

    @Test
    public void testCurrentQuantity() {
        // Setup
        int id = 99;
        String name = "Breadboard";
        double value = 30.0;
        int currentQuantity = 0;
        int maxQuantity = 6;
        String type = "board";
        boolean visible = true;
        Need need = new Need(id, name, value, currentQuantity, maxQuantity, type, visible);

        int expected_quantity = 7;

        // Invoke
        need.setCurrentQuantity(expected_quantity);

        // Analyze
        assertEquals(expected_quantity,need.getCurrentQuantity());
    }

    @Test
    public void testMaxQuantity() {
        // Setup
        int id = 99;
        String name = "Breadboard";
        double value = 30.0;
        int currentQuantity = 5;
        int maxQuantity = 6;
        String type = "board";
        boolean visible = true;
        Need need = new Need(id, name, value, currentQuantity, maxQuantity, type, visible);

        int expected_quantity = 7;

        // Invoke
        need.setMaxQuantity(expected_quantity);

        // Analyze
        assertEquals(expected_quantity,need.getMaxQuantity());
    }

    @Test
    public void testType() {
        // Setup
        int id = 99;
        String name = "Breadboard";
        double value = 30.0;
        int currentQuantity = 0;
        int maxQuantity = 6;
        String type = "board";
        boolean visible = true;
        Need need = new Need(id, name, value, currentQuantity, maxQuantity, type, visible);

        String expected_type = "breadboard";

        // Invoke
        need.setType(expected_type);

        // Analyze
        assertEquals(expected_type,need.getType());
    }
    
    @Test
    public void testName() {
        // Setup
        int id = 99;
        String name = "Breadboard";
        double value = 30.0;
        int currentQuantity = 0;
        int maxQuantity = 6;
        String type = "board";
        boolean visible = true;
        Need need = new Need(id, name, value, currentQuantity, maxQuantity, type, visible);

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
        int currentQuantity = 0;
        int maxQuantity = 6;
        String type = "board";
        boolean visible = true;
        String expected_string = String.format(Need.STRINGIFY,id,name, value, currentQuantity, maxQuantity, type, visible);
        Need need = new Need(id, name, value, currentQuantity, maxQuantity, type, visible);

        // Invoke
        String actual_string = need.toString();

        // Analyze
        assertEquals(expected_string,actual_string);
    }
}