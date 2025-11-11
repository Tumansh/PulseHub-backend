package com.pulsehub.expenseservice.expense_service.repo;

import com.pulsehub.expenseservice.expense_service.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserIdOrderByDateDesc(Long userId);
}
