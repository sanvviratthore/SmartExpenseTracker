package com.expensetracker.expense;

import com.expensetracker.expense.dto.ExpenseRequest;
import com.expensetracker.expense.dto.ExpenseResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExpenseResponse add(@Valid @RequestBody ExpenseRequest request, Principal principal) {
        return expenseService.addExpense(principal.getName(), request);
    }

    @GetMapping
    public List<ExpenseResponse> list(Principal principal) {
        return expenseService.getExpenses(principal.getName());
    }
}
