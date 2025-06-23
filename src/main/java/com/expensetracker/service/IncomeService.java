package com.expensetracker.service;

import com.expensetracker.model.Income;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Service interface for income management operations.
 * Defines the contract for income-related business logic.
 */
public interface IncomeService {

    /**
     * Adds a new income to the system.
     * @param income The income to add
     * @return The added income with generated ID
     */
    Income addIncome(Income income);

    /**
     * Updates an existing income.
     * @param income The income to update
     * @return The updated income
     */
    Income updateIncome(Income income);

    /**
     * Deletes an income by ID.
     * @param id The ID of the income to delete
     * @return true if deleted successfully, false otherwise
     */
    boolean deleteIncome(Long id);

    /**
     * Retrieves an income by ID.
     * @param id The ID of the income
     * @return The income if found, null otherwise
     */
    Income getIncomeById(Long id);

    /**
     * Retrieves all incomes.
     * @return List of all incomes
     */
    List<Income> getAllIncomes();

    /**
     * Retrieves incomes filtered by date range.
     * @param startDate The start date (inclusive)
     * @param endDate The end date (inclusive)
     * @return List of incomes within the date range
     */
    List<Income> getIncomesByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Calculates total income for a date range.
     * @param startDate The start date (inclusive)
     * @param endDate The end date (inclusive)
     * @return Total amount of income
     */
    Double getTotalIncome(LocalDate startDate, LocalDate endDate);

    /**
     * Gets recent incomes (last 10).
     * @return List of recent incomes
     */
    List<Income> getRecentIncomes();
    
    /**
     * Groups incomes by source and calculates total for each source within a date range.
     * @param startDate The start date (inclusive)
     * @param endDate The end date (inclusive)
     * @return Map with source as key and total amount as value
     */
    Map<String, Double> getIncomeBySource(LocalDate startDate, LocalDate endDate);
}
