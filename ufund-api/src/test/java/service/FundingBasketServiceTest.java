package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.service.FundingBasketServiceImplementation;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.persistence.FundingBasketDAO;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;

/**
 * Unit tests for the FundingBasketServiceImplementation
 * 
 * @author Anthony Pelto
 */
public class FundingBasketServiceTest {
    private FundingBasketDAO mockDAO;
    private FundingBasketServiceImplementation service;

    /**
     * Initialize the mock DAO and service before each test
     */
    @BeforeEach
    public void setup() {
        mockDAO = mock(FundingBasketDAO.class);
        service = new FundingBasketServiceImplementation(mockDAO);
    }

    /**
     * Tests that getNeeds() returns all the needs correctly
     */
    @Test
    public void testGetNeeds() throws IOException {
        Need[] expected = {
            new Need(1, "Wires", 5.0, 10, "cord"),
            new Need(2, "Breadboard", 12.0, 3, "board")
        };

        when(mockDAO.getNeeds()).thenReturn(expected);
        Need[] actual = service.getNeeds();

        assertArrayEquals(expected, actual);
        verify(mockDAO, times(1)).getNeeds();
    }

    /**
     * Tests that the FundingBasketService getNeed returns
     * a single need by its ID.
     */
    @Test
    public void testGetNeed() throws IOException {
        Need expected = new Need(1, "Sensor", 3.5, 4, "component");
        when(mockDAO.getNeed(1)).thenReturn(expected);
        Need actual = service.getNeed(1);

        assertEquals(expected, actual);
        verify(mockDAO, times(1)).getNeed(1);
    }

    /**
     * Tests searchNeeds to verify the needs are filtered by name
     */
    @Test
    public void testSearchNeeds() throws IOException {
        String searchTerm = "Lab";
        Need[] expected = {
            new Need(1, "Lab Kit", 20.0, 2, "kit"),
            new Need(2, "Lab Coat", 15.0, 5, "apparel")
        };

        when(mockDAO.searchNeeds(searchTerm)).thenReturn(expected);
        Need[] actual = service.searchNeeds(searchTerm);

        assertArrayEquals(expected,actual);
        verify(mockDAO, times(1)).searchNeeds(searchTerm);
    }

    /**
     * Tests createdNeed to verify that a new need is successfully created
     */
    @Test
    public void testCreateNeed() throws IOException {
        Need newNeed = new Need(3, "Resistor", 0.10, 100, "component");
        when(mockDAO.createdNeed(newNeed)).thenReturn(newNeed);

        Need result = service.createdNeed(newNeed);
        assertEquals(newNeed, result);
        verify(mockDAO, times(1)).createdNeed(newNeed);
    }

    /**
     * Tests that updateNeed() updates an existing need
     */
    @Test
    public void testUpdateNeed() throws IOException {
        Need updated = new Need(2, "Updated Board", 14.0, 2, "board");
        when(mockDAO.updateNeed(updated)).thenReturn(updated);
        Need result = service.updateNeed(updated);

        assertEquals(updated, result);
        verify(mockDAO, times(1)).updateNeed(updated);
    }

    /**
     * Tests that deleteNeed(int) deletes a need by its ID
     */
    @Test
    public void testDeleteNeed() throws IOException {
        when(mockDAO.deleteNeed(1)).thenReturn(true);
        boolean result = service.deleteNeed(1);

        assertTrue(result);
        verify(mockDAO, times(1)).deleteNeed(1);
    }

    /**
     * Tests that deleteNeedType deletes all needs of a given type
     */
    @Test
    public void testDeleteNeedType() throws IOException {
        when(mockDAO.deleteNeedType("board")).thenReturn(true);
        boolean result = service.deleteNeedType("board");

        assertTrue(result);
        verify(mockDAO, times(1)).deleteNeedType("board");
    }

    /**
     * Tests that deleteAll() deletes all Needs
     */
    @Test
    public void testDeleteAll() throws IOException {
        when(mockDAO.deleteAll()).thenReturn(true);
        boolean result = service.deleteAll();

        assertTrue(result);
        verify(mockDAO, times(1)).deleteAll();
    }

    /**
     * Tests that deleteNeedname deletes needs with a matching substring
     */
    @Test
    public void testDeleteNeedName() throws IOException {
        when(mockDAO.deleteNeedName("Wire")).thenReturn(true);
        boolean result = service.deleteNeedName("Wire");

        assertTrue(result);
        verify(mockDAO, times(1)).deleteNeedName("Wire");
    }
}
