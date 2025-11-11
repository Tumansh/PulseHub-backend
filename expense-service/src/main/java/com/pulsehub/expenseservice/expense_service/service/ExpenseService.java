package com.pulsehub.expenseservice.expense_service.service;

import com.pulsehub.expenseservice.expense_service.model.Expense;
import com.pulsehub.expenseservice.expense_service.repo.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ExpenseService {

    private final ExpenseRepository repo;

    public ExpenseService(ExpenseRepository repo) {
        this.repo = repo;
    }

    public List<Expense> getUserExpenses(Long userId) {
        return repo.findByUserIdOrderByDateDesc(userId);
    }

    public Expense addExpense(Expense expense) {
        return repo.save(expense);
    }

    public void deleteExpense(Long id) {
        repo.deleteById(id);
    }

    public Map<String, Double> getSummary(Long userId) {
        List<Expense> expenses = repo.findByUserIdOrderByDateDesc(userId);
        double income = expenses.stream()
                .filter(e -> "income".equalsIgnoreCase(e.getType()))
                .mapToDouble(Expense::getAmount)
                .sum();
        double spent = expenses.stream()
                .filter(e -> "expense".equalsIgnoreCase(e.getType()))
                .mapToDouble(Expense::getAmount)
                .sum();
        double savings = income - spent;
        return Map.of("income", income, "spent", spent, "savings", savings);
    }
}
