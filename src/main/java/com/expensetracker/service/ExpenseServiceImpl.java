package com.expensetracker.service;

import com.expensetracker.model.Expense;
import com.expensetracker.model.ExpenseCategory;
import com.expensetracker.model.PaymentMethod;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * In-memory implementation of ExpenseService.
 * Stores data in memory for the application session.
 */
public class ExpenseServiceImpl implements ExpenseService {

    private final Map<Long, Expense> expenses = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public ExpenseServiceImpl() {
        // Initialize with sample data
        initializeSampleData();
    }

    private void initializeSampleData() {
        // Add some sample expenses for demonstration
        addExpense(new Expense(null, 25.50, "Lunch at restaurant", ExpenseCategory.FOOD, PaymentMethod.CARD, LocalDate.now().minusDays(1)));
        addExpense(new Expense(null, 15.00, "Bus fare", ExpenseCategory.TRANSPORTATION, PaymentMethod.UPI, LocalDate.now().minusDays(2)));
        addExpense(new Expense(null, 120.00, "Grocery shopping", ExpenseCategory.FOOD, PaymentMethod.CARD, LocalDate.now().minusDays(3)));
        addExpense(new Expense(null, 50.00, "Movie tickets", ExpenseCategory.ENTERTAINMENT, PaymentMethod.CASH, LocalDate.now().minusDays(4)));
        addExpense(new Expense(null, 80.00, "Electric bill", ExpenseCategory.UTILITIES, PaymentMethod.UPI, LocalDate.now().minusDays(5)));
        addExpense(new Expense(null, 200.00, "Clothes shopping", ExpenseCategory.SHOPPING, PaymentMethod.CARD, LocalDate.now().minusDays(6)));
        addExpense(new Expense(null, 30.00, "Coffee with friends", ExpenseCategory.FOOD, PaymentMethod.CASH, LocalDate.now().minusDays(7)));
        addExpense(new Expense(null, 60.00, "Taxi ride", ExpenseCategory.TRANSPORTATION, PaymentMethod.UPI, LocalDate.now().minusDays(8)));
    }

    @Override
    public Expense addExpense(Expense expense) {
        if (expense == null) {
            throw new IllegalArgumentException("Expense cannot be null");
        }
        
        Long id = idGenerator.getAndIncrement();
        expense.setId(id);
        expenses.put(id, expense);
        return expense;
    }

    @Override
    public Expense updateExpense(Expense expense) {
        if (expense == null || expense.getId() == null) {
            throw new IllegalArgumentException("Expense and its ID cannot be null");
        }
        
        if (!expenses.containsKey(expense.getId())) {
            throw new IllegalArgumentException("Expense with ID " + expense.getId() + " not found");
        }
        
        expenses.put(expense.getId(), expense);
        return expense;
    }

    @Override
    public boolean deleteExpense(Long id) {
        if (id == null) {
            return false;
        }
        return expenses.remove(id) != null;
    }

    @Override
    public Expense getExpenseById(Long id) {
        return expenses.get(id);
    }

    @Override
    public List<Expense> getAllExpenses() {
        return new ArrayList<>(expenses.values());
    }

    @Override
    public List<Expense> getExpensesByDateRange(LocalDate startDate, LocalDate endDate) {
        return expenses.values().stream()
                .filter(expense -> {
                    LocalDate expenseDate = expense.getDate();
                    return (startDate == null || !expenseDate.isBefore(startDate)) &&
                           (endDate == null || !expenseDate.isAfter(endDate));
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Expense> getExpensesByCategory(ExpenseCategory category) {
        return expenses.values().stream()
                .filter(expense -> expense.getCategory() == category)
                .collect(Collectors.toList());
    }

    @Override
    public Double getTotalExpenses(LocalDate startDate, LocalDate endDate) {
        return getExpensesByDateRange(startDate, endDate).stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    @Override
    public Map<ExpenseCategory, Double> getExpensesByCategory(LocalDate startDate, LocalDate endDate) {
        return getExpensesByDateRange(startDate, endDate).stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(Expense::getAmount)
                ));
    }

    @Override
    public List<Expense> getRecentExpenses() {
        return expenses.values().stream()
                .sorted((e1, e2) -> e2.getDate().compareTo(e1.getDate()))
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public List<Expense> searchExpenses(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllExpenses();
        }
        
        String lowerKeyword = keyword.toLowerCase();
        return expenses.values().stream()
                .filter(expense -> expense.getDescription().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }
}
