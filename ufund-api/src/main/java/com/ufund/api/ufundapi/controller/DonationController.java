package com.ufund.api.ufundapi.controller;

import java.io.IOException;
import org.springframework.web.bind.annotation.*;
import com.ufund.api.ufundapi.service.DonationServiceImplementation;
import com.ufund.api.ufundapi.model.Donation;

@RestController
@RequestMapping("/donations")
public class DonationController {
    private final DonationServiceImplementation donationService;

    public DonationController(DonationServiceImplementation donationService) {
        this.donationService = donationService;
    }

    @GetMapping("/status")
    public Donation getStatus() throws IOException {
        return donationService.getStatus();
    }

    @PostMapping("/add/{amount}")
    public Donation addDonation(@PathVariable int amount) throws IOException {
        return donationService.addDonation(amount);
    }

    @PostMapping("/goal/{newGoal}")
    public Donation setGoal(@PathVariable int newGoal) throws IOException {
        return donationService.setGoal(newGoal);
    }
}
