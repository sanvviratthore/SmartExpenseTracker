package com.expensetracker.savings;

import com.expensetracker.savings.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/savings")
public class SavingsController {

    private final SavingService savingService;

    public SavingsController(SavingService savingService) {
        this.savingService = savingService;
    }

    // ---- wishlist goals ----
    @PostMapping("/goals")
    @ResponseStatus(HttpStatus.CREATED)
    public GoalResponse addGoal(@Valid @RequestBody GoalRequest request, Principal principal) {
        return savingService.setGoal(principal.getName(), request);
    }

    @GetMapping("/goals")
    public List<GoalResponse> getGoals(Principal principal) {
        return savingService.getGoals(principal.getName());
    }

    // ---- daily savings ----
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SavingResponse addSaving(@Valid @RequestBody SavingRequest request, Principal principal) {
        return savingService.addSaving(principal.getName(), request);
    }

    @GetMapping
    public SavingsSummaryResponse getSavings(Principal principal) {
        return savingService.getSavings(principal.getName());
    }

    // ---- purchase advisor ----
    @GetMapping("/insight")
    public List<BuyingInsightResponse> getBuyingInsights(Principal principal) {
        return savingService.getBuyingInsights(principal.getName());
    }
}
