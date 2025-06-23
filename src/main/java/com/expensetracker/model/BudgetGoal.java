package com.expensetracker.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.YearMonth;

/**
 * Represents a monthly budget goal for an expense category.
 * This class is used to track spending targets and analyze budget performance.
 */
public class BudgetGoal {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("category")
    private ExpenseCategory category;
    
    @JsonProperty("amount")
    private Double amount;
    
    @JsonProperty("yearMonth")
    private YearMonth yearMonth;
    
    // Default constructor for JSON deserialization
    public BudgetGoal() {}

    public BudgetGoal(Long id, ExpenseCategory category, Double amount, YearMonth yearMonth) {
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.yearMonth = yearMonth;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public YearMonth getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(YearMonth yearMonth) {
        this.yearMonth = yearMonth;
    }

    @Override
    public String toString() {
        return "BudgetGoal{" +
                "id=" + id +
                ", category=" + category +
                ", amount=" + amount +
                ", yearMonth=" + yearMonth +
                '}';
    }
}
