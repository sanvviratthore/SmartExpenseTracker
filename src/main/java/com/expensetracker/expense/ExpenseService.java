package com.expensetracker.expense;

import com.expensetracker.expense.dto.ExpenseRequest;
import com.expensetracker.expense.dto.ExpenseResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Expense CRUD. Mirrors the old ExpenseService/ExpenseStorage pair, but persists
 * through the repository instead of reading/writing text files.
 */
@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public ExpenseResponse addExpense(String username, ExpenseRequest request) {
        Expense expense = new Expense(
                username,
                request.title(),
                request.amount(),
                request.category(),
                LocalDate.now());
        return ExpenseResponse.from(expenseRepository.save(expense));
    }

    public List<ExpenseResponse> getExpenses(String username) {
        return expenseRepository.findByUsernameOrderByDateDesc(username)
                .stream()
                .map(ExpenseResponse::from)
                .toList();
    }

    /** Raw entities, used by the insights service. */
    public List<Expense> getRawExpenses(String username) {
        return expenseRepository.findByUsernameOrderByDateDesc(username);
    }
}
