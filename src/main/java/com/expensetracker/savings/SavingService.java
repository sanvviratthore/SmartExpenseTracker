package com.expensetracker.savings;

import com.expensetracker.savings.dto.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

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
        BigDecimal total = savings.stream()
                .map(Saving::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new SavingsSummaryResponse(
                total,
                savings.stream().map(SavingResponse::from).toList());
    }

    public BigDecimal getTotalSaved(String username) {
        return savingRepository.findByUsernameOrderByDateDesc(username)
                .stream()
                .map(Saving::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<BuyingInsightResponse> getBuyingInsights(String username) {
        BigDecimal totalSaved = getTotalSaved(username);
        return goalRepository.findByUsername(username).stream()
                .map(goal -> {
                    BigDecimal target = goal.getTargetAmount();
                    BigDecimal percent = target.compareTo(BigDecimal.ZERO) > 0
                            ? totalSaved
                                .divide(target, 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100))
                            : BigDecimal.ZERO;

                    if (percent.compareTo(BigDecimal.valueOf(100)) > 0) {
                        percent = BigDecimal.valueOf(100); // cap at 100%
                    }
                    percent = percent.setScale(2, RoundingMode.HALF_UP);

                    boolean canBuy = percent.compareTo(BigDecimal.valueOf(80)) >= 0;

                    return new BuyingInsightResponse(
                            goal.getProductName(),
                            target,
                            goal.getProductLink(),
                            percent,
                            canBuy);
                })
                .toList();
    }
}