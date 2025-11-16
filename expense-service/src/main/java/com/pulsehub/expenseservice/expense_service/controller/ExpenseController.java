package com.pulsehub.expenseservice.expense_service.controller;

import com.pulsehub.commonlib.common_lib.security.JwtUtil;
import com.pulsehub.expenseservice.expense_service.model.Expense;
import com.pulsehub.expenseservice.expense_service.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final JwtUtil jwtUtil;

    public ExpenseController(ExpenseService expenseService, JwtUtil jwtUtil) {
        this.expenseService = expenseService;
        this.jwtUtil = jwtUtil;
    }

    private Long getUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        return jwtUtil.extractUserId(authHeader.substring(7));
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getExpenses(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = getUserId(authHeader);
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        List<Expense> expenses = expenseService.getUserExpenses(userId);
        return ResponseEntity.ok(expenses);
    }

    @PostMapping
    public ResponseEntity<Expense> addExpense(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody Expense expense) {

        Long userId = getUserId(authHeader);
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        // Normalize input
        expense.setUserId(userId);
        String type = expense.getType() != null ? expense.getType().trim().toLowerCase() : "expense";
        expense.setType(type.equals("income") ? "income" : "expense");

        Expense saved = expenseService.addExpense(expense);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Double>> getSummary(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        Long userId = getUserId(authHeader);
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Map<String, Double> summary = expenseService.getSummary(userId);
        return ResponseEntity.ok(summary);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long id) {

        Long userId = getUserId(authHeader);
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
