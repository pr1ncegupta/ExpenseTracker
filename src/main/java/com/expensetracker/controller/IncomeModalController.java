package com.expensetracker.controller;

import com.expensetracker.model.Income;
import com.expensetracker.service.IncomeService;
import com.expensetracker.util.ModalUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * Controller for the Add/Edit Income modal dialog.
 */
public class IncomeModalController {

    @FXML private TextField amountField;
    @FXML private ComboBox<String> sourceComboBox;
    @FXML private DatePicker datePicker;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Label titleLabel;

    private IncomeService incomeService;
    private Income currentIncome;
    private boolean isEditMode = false;
    private Runnable onSaveCallback;

    public void setIncomeService(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }

    public void setIncomeForEdit(Income income) {
        this.currentIncome = income;
        this.isEditMode = true;
        populateFields();
        titleLabel.setText("Edit Income");
    }    @FXML
    private void initialize() {
        // Setup income source options
        sourceComboBox.getItems().addAll("Salary", "Freelance", "Borrow", "Gift");
        
        setupValidation();
        
        // Set default date to today
        datePicker.setValue(LocalDate.now());
        
        titleLabel.setText("Add New Income");
    }    private void setupValidation() {
        // Add input validation for amount field (allow digits, one decimal point, and up to 2 decimal places)
        amountField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !newValue.matches("\\d*(\\.\\d{0,2})?")) {
                amountField.setText(oldValue);
            }
            updateSaveButtonState();
        });

        sourceComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateSaveButtonState();
        });

        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateSaveButtonState();
        });
    }    private void updateSaveButtonState() {
        boolean hasAmount = !amountField.getText().trim().isEmpty();
        boolean hasSource = sourceComboBox.getValue() != null;
        boolean hasDate = datePicker.getValue() != null;
        
        boolean isValid = hasAmount && hasSource && hasDate;
        
        // Visual feedback for required fields
        amountField.setStyle(hasAmount ? "" : "-fx-border-color: #ff6666;");
        sourceComboBox.setStyle(hasSource ? "" : "-fx-border-color: #ff6666;");
        datePicker.setStyle(hasDate ? "" : "-fx-border-color: #ff6666;");
        
        saveButton.setDisable(!isValid);
    }    private void populateFields() {
        if (currentIncome != null) {
            // Format the amount with proper precision (no currency symbol in edit field)
            amountField.setText(String.format("%.2f", currentIncome.getAmount()));
            sourceComboBox.setValue(currentIncome.getSource());
            datePicker.setValue(currentIncome.getDate());
        }
    }

    @FXML
    private void handleSave() {
        try {
            // Parse and validate form inputs
            String amountText = amountField.getText().trim();
            if (amountText.isEmpty()) {
                showAlert("Invalid Input", "Please enter an amount.");
                return;
            }
            
            Double amount = Double.parseDouble(amountText);            String source = sourceComboBox.getValue();
            LocalDate date = datePicker.getValue();

            if (isEditMode && currentIncome != null) {
                // Update existing income
                currentIncome.setAmount(amount);
                currentIncome.setSource(source);
                currentIncome.setDate(date);
                incomeService.updateIncome(currentIncome);
            } else {
                // Create new income
                Income newIncome = new Income(null, amount, source, date);
                incomeService.addIncome(newIncome);
            }

            // Call the callback to refresh the dashboard
            if (onSaveCallback != null) {
                onSaveCallback.run();
            }

            // Close the modal
            closeModal();
        } catch (NumberFormatException e) {
            showAlert("Invalid Amount", "Please enter a valid number for the amount (e.g., 123.45).");
        } catch (Exception e) {
            e.printStackTrace(); // Log the full stack trace
            showAlert("Error Saving Income", "An error occurred while saving the income: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeModal();
    }

    private void closeModal() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    
    private void showAlert(String title, String message) {
        ModalUtils.showErrorDialog(title, message);
    }
}
