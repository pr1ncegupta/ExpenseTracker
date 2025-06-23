package com.expensetracker.service;

import com.expensetracker.model.Expense;
import com.expensetracker.model.ExpenseCategory;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Service interface for expense management operations.
 * Defines the contract for expense-related business logic.
 */
public interface ExpenseService {

    /**
     * Adds a new expense to the system.
     * @param expense The expense to add
     * @return The added expense with generated ID
     */
    Expense addExpense(Expense expense);

    /**
     * Updates an existing expense.
     * @param expense The expense to update
     * @return The updated expense
     */
    Expense updateExpense(Expense expense);

    /**
     * Deletes an expense by ID.
     * @param id The ID of the expense to delete
     * @return true if deleted successfully, false otherwise
     */
    boolean deleteExpense(Long id);

    /**
     * Retrieves an expense by ID.
     * @param id The ID of the expense
     * @return The expense if found, null otherwise
     */
    Expense getExpenseById(Long id);

    /**
     * Retrieves all expenses.
     * @return List of all expenses
     */
    List<Expense> getAllExpenses();

    /**
     * Retrieves expenses filtered by date range.
     * @param startDate The start date (inclusive)
     * @param endDate The end date (inclusive)
     * @return List of expenses within the date range
     */
    List<Expense> getExpensesByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Retrieves expenses filtered by category.
     * @param category The expense category
     * @return List of expenses in the category
     */
    List<Expense> getExpensesByCategory(ExpenseCategory category);

    /**
     * Calculates total expenses for a date range.
     * @param startDate The start date (inclusive)
     * @param endDate The end date (inclusive)
     * @return Total amount of expenses
     */
    Double getTotalExpenses(LocalDate startDate, LocalDate endDate);

    /**
     * Calculates total expenses by category for a date range.
     * @param startDate The start date (inclusive)
     * @param endDate The end date (inclusive)
     * @return Map of category to total amount
     */
    Map<ExpenseCategory, Double> getExpensesByCategory(LocalDate startDate, LocalDate endDate);

    /**
     * Gets recent expenses (last 10).
     * @return List of recent expenses
     */
    List<Expense> getRecentExpenses();

    /**
     * Searches expenses by description.
     * @param keyword The search keyword
     * @return List of matching expenses
     */
    List<Expense> searchExpenses(String keyword);
}
