package com.expensetracker.savings;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavingRepository extends JpaRepository<Saving, Long> {
    List<Saving> findByUsernameOrderByDateDesc(String username);
}
