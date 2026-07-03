package com.expensetracker.savings;

import com.expensetracker.savings.dto.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Savings and wishlist logic. Mirrors the original SavingService:
 *  - set wishlist goals and record daily savings
 *  - "buying insight" caps progress at 100% and flags a purchase once savings
 *    reach 80% of a goal's price.
 */
@Service
public class SavingService {

    private final GoalRepository goalRepository;
    private final SavingRepository savingRepository;

    public SavingService(GoalRepository goalRepository, SavingRepository savingRepository) {
        this.goalRepository = goalRepository;
        this.savingRepository = savingRepository;
    }

    public GoalResponse setGoal(String username, GoalRequest request) {
        Goal goal = new Goal(username, request.productName(), request.targetAmount(), request.productLink());
        return GoalResponse.from(goalRepository.save(goal));
    }

    public List<GoalResponse> getGoals(String username) {
        return goalRepository.findByUsername(username).stream().map(GoalResponse::from).toList();
    }

    public SavingResponse addSaving(String username, SavingRequest request) {
        Saving saving = new Saving(username, request.amount(), LocalDate.now());
        return SavingResponse.from(savingRepository.save(saving));
    }

    public SavingsSummaryResponse getSavings(String username) {
        List<Saving> savings = savingRepository.findByUsernameOrderByDateDesc(username);
        double total = savings.stream().mapToDouble(Saving::getAmount).sum();
        return new SavingsSummaryResponse(
                total,
                savings.stream().map(SavingResponse::from).toList());
    }

    public double getTotalSaved(String username) {
        return savingRepository.findByUsernameOrderByDateDesc(username)
                .stream().mapToDouble(Saving::getAmount).sum();
    }

    public List<BuyingInsightResponse> getBuyingInsights(String username) {
        double totalSaved = getTotalSaved(username);
        return goalRepository.findByUsername(username).stream()
                .map(goal -> {
                    double percent = goal.getTargetAmount() > 0
                            ? (totalSaved / goal.getTargetAmount()) * 100
                            : 0;
                    if (percent >= 100) {
                        percent = 100; // cap at 100%
                    }
                    boolean canBuy = percent >= 80;
                    return new BuyingInsightResponse(
                            goal.getProductName(),
                            goal.getTargetAmount(),
                            goal.getProductLink(),
                            Math.round(percent * 100.0) / 100.0,
                            canBuy);
                })
                .toList();
    }
}
