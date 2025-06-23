package com.expensetracker.service;

import com.expensetracker.model.Income;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * In-memory implementation of IncomeService.
 * Stores data in memory for the application session.
 */
public class IncomeServiceImpl implements IncomeService {

    private final Map<Long, Income> incomes = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public IncomeServiceImpl() {
        // Initialize with sample data
        initializeSampleData();
    }

    private void initializeSampleData() {
        // Add some sample incomes for demonstration
        addIncome(new Income(null, 3000.0, "Salary", LocalDate.now().minusDays(15)));
        addIncome(new Income(null, 500.0, "Freelance Work", LocalDate.now().minusDays(10)));
        addIncome(new Income(null, 100.0, "Interest", LocalDate.now().minusDays(5)));
    }

    @Override
    public Income addIncome(Income income) {
        if (income == null) {
            throw new IllegalArgumentException("Income cannot be null");
        }
        
        Long id = idGenerator.getAndIncrement();
        income.setId(id);
        incomes.put(id, income);
        return income;
    }

    @Override
    public Income updateIncome(Income income) {
        if (income == null || income.getId() == null) {
            throw new IllegalArgumentException("Income and its ID cannot be null");
        }
        
        if (!incomes.containsKey(income.getId())) {
            throw new IllegalArgumentException("Income with ID " + income.getId() + " not found");
        }
        
        incomes.put(income.getId(), income);
        return income;
    }

    @Override
    public boolean deleteIncome(Long id) {
        if (id == null) {
            return false;
        }
        return incomes.remove(id) != null;
    }

    @Override
    public Income getIncomeById(Long id) {
        return incomes.get(id);
    }

    @Override
    public List<Income> getAllIncomes() {
        return new ArrayList<>(incomes.values());
    }

    @Override
    public List<Income> getIncomesByDateRange(LocalDate startDate, LocalDate endDate) {
        return incomes.values().stream()
                .filter(income -> {
                    LocalDate incomeDate = income.getDate();
                    return (startDate == null || !incomeDate.isBefore(startDate)) &&
                           (endDate == null || !incomeDate.isAfter(endDate));
                })
                .collect(Collectors.toList());
    }

    @Override
    public Double getTotalIncome(LocalDate startDate, LocalDate endDate) {
        return getIncomesByDateRange(startDate, endDate).stream()
                .mapToDouble(Income::getAmount)
                .sum();
    }

    @Override
    public List<Income> getRecentIncomes() {
        return incomes.values().stream()
                .sorted((i1, i2) -> i2.getDate().compareTo(i1.getDate()))
                .limit(10)
                .collect(Collectors.toList());
    }
    
    @Override
    public Map<String, Double> getIncomeBySource(LocalDate startDate, LocalDate endDate) {
        return getIncomesByDateRange(startDate, endDate).stream()
                .collect(Collectors.groupingBy(
                    Income::getSource,
                    Collectors.summingDouble(Income::getAmount)
                ));
    }
}
