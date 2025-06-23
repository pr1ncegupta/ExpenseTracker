package com.expensetracker.service;

import com.expensetracker.model.BudgetGoal;
import com.expensetracker.model.ExpenseCategory;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * In-memory implementation of BudgetGoalService.
 * Stores data in memory for the application session.
 */
public class BudgetGoalServiceImpl implements BudgetGoalService {

    private final Map<Long, BudgetGoal> budgetGoals = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final ExpenseService expenseService;

    public BudgetGoalServiceImpl(ExpenseService expenseService) {
        this.expenseService = expenseService;
        // Initialize with sample data
        initializeSampleData();
    }

    private void initializeSampleData() {
        // Add sample budget goals for the current month
        YearMonth currentMonth = YearMonth.now();
        
        addBudgetGoal(new BudgetGoal(null, ExpenseCategory.FOOD, 500.0, currentMonth));
        addBudgetGoal(new BudgetGoal(null, ExpenseCategory.TRANSPORTATION, 300.0, currentMonth));
        addBudgetGoal(new BudgetGoal(null, ExpenseCategory.ENTERTAINMENT, 200.0, currentMonth));
        addBudgetGoal(new BudgetGoal(null, ExpenseCategory.UTILITIES, 400.0, currentMonth));
    }

    @Override
    public BudgetGoal addBudgetGoal(BudgetGoal budgetGoal) {
        if (budgetGoal == null) {
            throw new IllegalArgumentException("Budget goal cannot be null");
        }
        
        // Check if a budget goal already exists for this category and month
        Optional<BudgetGoal> existing = budgetGoals.values().stream()
                .filter(bg -> bg.getCategory() == budgetGoal.getCategory() && 
                       bg.getYearMonth().equals(budgetGoal.getYearMonth()))
                .findFirst();
        
        if (existing.isPresent()) {
            throw new IllegalArgumentException("A budget goal already exists for " + 
                    budgetGoal.getCategory().getDisplayName() + " in " + 
                    budgetGoal.getYearMonth().getMonth().toString());
        }
        
        Long id = idGenerator.getAndIncrement();
        budgetGoal.setId(id);
        budgetGoals.put(id, budgetGoal);
        return budgetGoal;
    }

    @Override
    public BudgetGoal updateBudgetGoal(BudgetGoal budgetGoal) {
        if (budgetGoal == null || budgetGoal.getId() == null) {
            throw new IllegalArgumentException("Budget goal and its ID cannot be null");
        }
        
        if (!budgetGoals.containsKey(budgetGoal.getId())) {
            throw new IllegalArgumentException("Budget goal with ID " + budgetGoal.getId() + " not found");
        }
        
        budgetGoals.put(budgetGoal.getId(), budgetGoal);
        return budgetGoal;
    }

    @Override
    public boolean deleteBudgetGoal(Long id) {
        if (id == null) {
            return false;
        }
        return budgetGoals.remove(id) != null;
    }

    @Override
    public BudgetGoal getBudgetGoalById(Long id) {
        return budgetGoals.get(id);
    }

    @Override
    public List<BudgetGoal> getAllBudgetGoals() {
        return new ArrayList<>(budgetGoals.values());
    }

    @Override
    public List<BudgetGoal> getBudgetGoalsByYearMonth(YearMonth yearMonth) {
        return budgetGoals.values().stream()
                .filter(bg -> bg.getYearMonth().equals(yearMonth))
                .collect(Collectors.toList());
    }

    @Override
    public BudgetGoal getBudgetGoalByCategoryAndMonth(ExpenseCategory category, YearMonth yearMonth) {
        return budgetGoals.values().stream()
                .filter(bg -> bg.getCategory() == category && bg.getYearMonth().equals(yearMonth))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Map<ExpenseCategory, Double> getBudgetPerformance(YearMonth yearMonth) {
        Map<ExpenseCategory, Double> result = new HashMap<>();
        
        // Get budget goals for the month
        List<BudgetGoal> monthlyGoals = getBudgetGoalsByYearMonth(yearMonth);
        
        // Get start and end dates for the month
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        
        // Get actual expenses by category
        Map<ExpenseCategory, Double> actualExpenses = expenseService.getExpensesByCategory(startDate, endDate);
        
        // Calculate performance percentages
        for (BudgetGoal goal : monthlyGoals) {
            Double actual = actualExpenses.getOrDefault(goal.getCategory(), 0.0);
            Double budgeted = goal.getAmount();
            
            // Calculate percentage of budget used
            Double percentUsed = (actual / budgeted) * 100.0;
            result.put(goal.getCategory(), percentUsed);
        }
        
        return result;
    }
}
