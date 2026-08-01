package com.expensetracker.savings.dto;

import java.math.BigDecimal;
import java.util.List;

public record SavingsSummaryResponse(BigDecimal totalSaved, List<SavingResponse> savings) {}