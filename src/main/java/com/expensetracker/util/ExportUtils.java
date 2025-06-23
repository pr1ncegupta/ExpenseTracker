package com.expensetracker.util;

import com.expensetracker.model.Expense;
import com.expensetracker.model.Income;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Utility class for exporting data to various formats.
 */
public class ExportUtils {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    
    /**
     * Exports expenses to a CSV file.
     * @param expenses List of expenses to export
     * @param stage Parent stage for the file chooser
     * @return true if export was successful, false otherwise
     */
    public static boolean exportExpensesToCSV(List<Expense> expenses, Stage stage) {
        if (expenses == null || expenses.isEmpty()) {
            ModalUtils.showErrorDialog("Export Error", "No expenses to export.");
            return false;
        }
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Expenses");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialFileName("expenses.csv");
        
        File file = fileChooser.showSaveDialog(stage);
        if (file == null) {
            return false; // User cancelled
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Write header
            writer.write("ID,DATE,CATEGORY,PAYMENT_METHOD,DESCRIPTION,AMOUNT");
            writer.newLine();
            
            // Write data
            for (Expense expense : expenses) {
                StringBuilder row = new StringBuilder();
                row.append(expense.getId()).append(",");
                row.append(expense.getDate().format(DATE_FORMATTER)).append(",");
                row.append(expense.getCategory().getDisplayName()).append(",");
                row.append(expense.getPaymentMethod().getDisplayName()).append(",");
                
                // Escape description if it contains commas
                String description = expense.getDescription();
                if (description.contains(",")) {
                    description = "\"" + description + "\"";
                }
                row.append(description).append(",");
                
                row.append(String.format("%.2f", expense.getAmount()));
                writer.write(row.toString());
                writer.newLine();
            }
            
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            ModalUtils.showErrorDialog("Export Error", "Failed to export expenses: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Exports income entries to a CSV file.
     * @param incomes List of income entries to export
     * @param stage Parent stage for the file chooser
     * @return true if export was successful, false otherwise
     */
    public static boolean exportIncomesToCSV(List<Income> incomes, Stage stage) {
        if (incomes == null || incomes.isEmpty()) {
            ModalUtils.showErrorDialog("Export Error", "No income entries to export.");
            return false;
        }
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Income");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialFileName("income.csv");
        
        File file = fileChooser.showSaveDialog(stage);
        if (file == null) {
            return false; // User cancelled
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Write header
            writer.write("ID,DATE,SOURCE,AMOUNT");
            writer.newLine();
            
            // Write data
            for (Income income : incomes) {
                StringBuilder row = new StringBuilder();
                row.append(income.getId()).append(",");
                row.append(income.getDate().format(DATE_FORMATTER)).append(",");
                
                // Escape source if it contains commas
                String source = income.getSource();
                if (source.contains(",")) {
                    source = "\"" + source + "\"";
                }
                row.append(source).append(",");
                
                row.append(String.format("%.2f", income.getAmount()));
                writer.write(row.toString());
                writer.newLine();
            }
            
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            ModalUtils.showErrorDialog("Export Error", "Failed to export income entries: " + e.getMessage());
            return false;
        }
    }
}
