package com.expensetracker.controller;

import com.expensetracker.model.Expense;
import com.expensetracker.model.ExpenseCategory;
import com.expensetracker.model.PaymentMethod;
import com.expensetracker.service.ExpenseService;
import com.expensetracker.util.ModalUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * Controller for the Add/Edit Expense modal dialog.
 */
public class ExpenseModalController {

    @FXML private TextField amountField;
    @FXML private TextField descriptionField;
    @FXML private ComboBox<ExpenseCategory> categoryComboBox;
    @FXML private ComboBox<PaymentMethod> paymentMethodComboBox;
    @FXML private DatePicker datePicker;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Label titleLabel;

    private ExpenseService expenseService;
    private Expense currentExpense;
    private boolean isEditMode = false;
    private Runnable onSaveCallback;

    public void setExpenseService(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }

    public void setExpenseForEdit(Expense expense) {
        this.currentExpense = expense;
        this.isEditMode = true;
        populateFields();
        titleLabel.setText("Edit Expense");
    }

    @FXML
    private void initialize() {
        setupComboBoxes();
        setupValidation();
        
        // Set default date to today
        datePicker.setValue(LocalDate.now());
        
        titleLabel.setText("Add New Expense");
    }    private void setupComboBoxes() {
        // Populate category combo box
        categoryComboBox.getItems().addAll(
            ExpenseCategory.FOOD, 
            ExpenseCategory.TRANSPORTATION,
            ExpenseCategory.ENTERTAINMENT,
            ExpenseCategory.UTILITIES,
            ExpenseCategory.OTHER
        );
        categoryComboBox.setPromptText("Select category");

        // Populate payment method combo box
        paymentMethodComboBox.getItems().addAll(
            PaymentMethod.CASH,
            PaymentMethod.UPI,
            PaymentMethod.CARD
        );
        paymentMethodComboBox.setPromptText("Select payment method");
    }private void setupValidation() {
        // Add input validation for amount field (allow digits, one decimal point, and up to 2 decimal places)
        amountField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !newValue.matches("\\d*(\\.\\d{0,2})?")) {
                amountField.setText(oldValue);
            }
            updateSaveButtonState();
        });

        descriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateSaveButtonState();
        });

        categoryComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateSaveButtonState();
        });

        paymentMethodComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateSaveButtonState();
        });

        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateSaveButtonState();
        });
    }    private void updateSaveButtonState() {
        boolean hasAmount = !amountField.getText().trim().isEmpty();
        boolean hasDescription = !descriptionField.getText().trim().isEmpty();
        boolean hasCategory = categoryComboBox.getValue() != null;
        boolean hasPaymentMethod = paymentMethodComboBox.getValue() != null;
        boolean hasDate = datePicker.getValue() != null;
        
        boolean isValid = hasAmount && hasDescription && hasCategory && 
                          hasPaymentMethod && hasDate;
        
        // Visual feedback for required fields
        amountField.setStyle(hasAmount ? "" : "-fx-border-color: #ff6666;");
        descriptionField.setStyle(hasDescription ? "" : "-fx-border-color: #ff6666;");
        categoryComboBox.setStyle(hasCategory ? "" : "-fx-border-color: #ff6666;");
        paymentMethodComboBox.setStyle(hasPaymentMethod ? "" : "-fx-border-color: #ff6666;");
        datePicker.setStyle(hasDate ? "" : "-fx-border-color: #ff6666;");
        
        saveButton.setDisable(!isValid);
    }private void populateFields() {
        if (currentExpense != null) {
            // Format the amount with proper precision (no currency symbol in edit field)
            amountField.setText(String.format("%.2f", currentExpense.getAmount()));
            descriptionField.setText(currentExpense.getDescription());
            categoryComboBox.setValue(currentExpense.getCategory());
            paymentMethodComboBox.setValue(currentExpense.getPaymentMethod());
            datePicker.setValue(currentExpense.getDate());
        }
    }    @FXML
    private void handleSave() {
        try {
            // Parse and validate form inputs
            String amountText = amountField.getText().trim();
            if (amountText.isEmpty()) {
                showAlert("Invalid Input", "Please enter an amount.");
                return;
            }
            
            Double amount = Double.parseDouble(amountText);
            String description = descriptionField.getText().trim();
            ExpenseCategory category = categoryComboBox.getValue();
            PaymentMethod paymentMethod = paymentMethodComboBox.getValue();
            LocalDate date = datePicker.getValue();

            if (isEditMode && currentExpense != null) {
                // Update existing expense
                currentExpense.setAmount(amount);
                currentExpense.setDescription(description);
                currentExpense.setCategory(category);
                currentExpense.setPaymentMethod(paymentMethod);
                currentExpense.setDate(date);
                expenseService.updateExpense(currentExpense);
            } else {
                // Create new expense
                Expense newExpense = new Expense(null, amount, description, category, paymentMethod, date);
                expenseService.addExpense(newExpense);
            }

            // Call the callback to refresh the dashboard
            if (onSaveCallback != null) {
                onSaveCallback.run();
            }

            // Close the modal
            closeModal();        } catch (NumberFormatException e) {
            showAlert("Invalid Amount", "Please enter a valid number for the amount (e.g., 123.45).");
        } catch (Exception e) {
            e.printStackTrace(); // Log the full stack trace
            showAlert("Error Saving Expense", "An error occurred while saving the expense: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeModal();
    }    private void closeModal() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    
    private void showAlert(String title, String message) {
        ModalUtils.showErrorDialog(title, message);
    }
}
