package com.expensetracker.service;

import com.expensetracker.model.BudgetGoal;
import com.expensetracker.model.ExpenseCategory;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

/**
 * Service interface for budget goal management operations.
 * Defines the contract for budget goal-related business logic.
 */
public interface BudgetGoalService {

    /**
     * Adds a new budget goal.
     * @param budgetGoal The budget goal to add
     * @return The added budget goal with generated ID
     */
    BudgetGoal addBudgetGoal(BudgetGoal budgetGoal);

    /**
     * Updates an existing budget goal.
     * @param budgetGoal The budget goal to update
     * @return The updated budget goal
     */
    BudgetGoal updateBudgetGoal(BudgetGoal budgetGoal);

    /**
     * Deletes a budget goal by ID.
     * @param id The ID of the budget goal to delete
     * @return true if deleted successfully, false otherwise
     */
    boolean deleteBudgetGoal(Long id);

    /**
     * Retrieves a budget goal by ID.
     * @param id The ID of the budget goal
     * @return The budget goal if found, null otherwise
     */
    BudgetGoal getBudgetGoalById(Long id);

    /**
     * Retrieves all budget goals.
     * @return List of all budget goals
     */
    List<BudgetGoal> getAllBudgetGoals();

    /**
     * Retrieves budget goals for a specific year and month.
     * @param yearMonth The year and month
     * @return List of budget goals for that month
     */
    List<BudgetGoal> getBudgetGoalsByYearMonth(YearMonth yearMonth);

    /**
     * Gets a budget goal for a specific category and month.
     * @param category The expense category
     * @param yearMonth The year and month
     * @return The budget goal if found, null otherwise
     */
    BudgetGoal getBudgetGoalByCategoryAndMonth(ExpenseCategory category, YearMonth yearMonth);

    /**
     * Gets budget performance - comparing actual expenses to budget goals.
     * @param yearMonth The year and month
     * @return Map of category to percentage of budget used (>100% means over budget)
     */
    Map<ExpenseCategory, Double> getBudgetPerformance(YearMonth yearMonth);
}
