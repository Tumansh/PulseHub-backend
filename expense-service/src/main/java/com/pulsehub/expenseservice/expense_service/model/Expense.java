package com.pulsehub.expenseservice.expense_service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String category;
    private Double amount;
    private String type;
    private String note;

    @Column(nullable = false)
    private LocalDateTime date = LocalDateTime.now();

    public Expense() {}

    public Expense(Long userId, String category, Double amount, String type, String note) {
        this.userId = userId;
        this.category = category;
        this.amount = amount;
        this.type = type;
        this.note = note;
        this.date = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getCategory() { return category; }
    public Double getAmount() { return amount; }
    public String getType() { return type; }
    public String getNote() { return note; }
    public LocalDateTime getDate() { return date; }

    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setCategory(String category) { this.category = category; }
    public void setAmount(Double amount) { this.amount = amount; }
    public void setType(String type) { this.type = type; }
    public void setNote(String note) { this.note = note; }
    public void setDate(LocalDateTime date) { this.date = date; }
}
