package com.expensetracker.savings.dto;

import java.util.List;

public record SavingsSummaryResponse(double totalSaved, List<SavingResponse> savings) {}
