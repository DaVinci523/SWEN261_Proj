package com.ufund.api.ufundapi.controller;

import java.io.IOException;
import org.springframework.web.bind.annotation.*;
import com.ufund.api.ufundapi.service.DonationServiceImplementation;
import com.ufund.api.ufundapi.model.Donation;

@RestController
@RequestMapping("/donations")
public class DonationController {
    private final DonationServiceImplementation donationService;
    /**
     * Creates a donationService object for functionality
     * 
     * @param donationService
     */
    public DonationController(DonationServiceImplementation donationService) {
        this.donationService = donationService;
    }
    /**
     * Get the current information in donations
     * @return Status
     * @throws IOException
     */
    @GetMapping("/status")
    public Donation getStatus() throws IOException {
        return donationService.getStatus();
    }
    /**
     * Increment the donations 
     * @param amount
     * @return donation increment
     * @throws IOException
     */
    @PostMapping("/add/{amount}")
    public Donation addDonation(@PathVariable int amount) throws IOException {
        return donationService.addDonation(amount);
    }

    /**
     * Update the donationGoal
     * @param newGoal
     * @return update goal
     * @throws IOException
     */
    @PostMapping("/goal/{newGoal}")
    public Donation setGoal(@PathVariable int newGoal) throws IOException {
        return donationService.setGoal(newGoal);
    }
}
