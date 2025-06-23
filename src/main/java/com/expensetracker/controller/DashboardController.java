package com.expensetracker.controller;

import com.expensetracker.model.Expense;
import com.expensetracker.model.ExpenseCategory;
import com.expensetracker.model.Income;
import com.expensetracker.service.*;
import com.expensetracker.util.CurrencyUtils;
import com.expensetracker.util.ModalUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Main controller for the Dashboard view.
 * Handles all dashboard operations and modal interactions.
 */
public class DashboardController {    // Dashboard summary labels
    @FXML private Label incomeLabel;
    @FXML private Label expensesLabel;
    @FXML private Label balanceLabel;
    @FXML private ComboBox<String> periodComboBox;    // Charts
    @FXML private PieChart expensesPieChart;
    @FXML private PieChart incomePieChart;
      // Navigation tabs
    @FXML private Label dashboardTab;
    @FXML private Label expensesTab;
    @FXML private Label incomeTab;
    
    // View containers
    @FXML private VBox dashboardView;
    @FXML private VBox expensesView;
    @FXML private VBox incomeView;
    
    // Recent expenses table
    @FXML private TableView<Expense> recentExpensesTable;
    @FXML private TableColumn<Expense, String> dateColumn;
    @FXML private TableColumn<Expense, String> categoryColumn;
    @FXML private TableColumn<Expense, String> descriptionColumn;
    @FXML private TableColumn<Expense, String> amountColumn;
    @FXML private Button removeExpenseBtn;
    
    // Recent income table
    @FXML private TableView<Income> recentIncomeTable;
    @FXML private TableColumn<Income, String> incomeDateColumn;
    @FXML private TableColumn<Income, String> incomeSourceColumn;
    @FXML private TableColumn<Income, String> incomeAmountColumn;
    @FXML private Button removeIncomeBtn;
    
    // Buttons
    @FXML private Button addExpenseButton;
    @FXML private Button addIncomeButton;
    @FXML private Button viewExpensesBtn;
    @FXML private Button viewIncomeBtn;
    
    private ExpenseService expenseService;
    private IncomeService incomeService;
    // Currently initialized but not actively used in the UI
    // Will be used in future budget tracking features
    private BudgetGoalService budgetGoalService;
    private ObservableList<Expense> recentExpenses;
    private ObservableList<Income> recentIncomes;
    
    // Track if user has added transactions
    private boolean hasUserAddedTransactions = false;
      @FXML
    private void initialize() {
        expenseService = new ExpenseServiceImpl();
        incomeService = new IncomeServiceImpl();
        budgetGoalService = new BudgetGoalServiceImpl(expenseService);
        recentExpenses = FXCollections.observableArrayList();
        recentIncomes = FXCollections.observableArrayList();
        
        // Setup initial view (dashboard is visible by default)
        dashboardView.setVisible(true);
        expensesView.setVisible(false);
        incomeView.setVisible(false);
        
        // Setup dashboard components
        setupPeriodComboBox();
        setupRecentExpensesTable();
        setupRecentIncomeTable();
        setupTableContextMenu();
        refreshDashboard();
        
        // Initialize budget tracking (planned feature)
        getBudgetStatus();
    }

    private void setupPeriodComboBox() {
        periodComboBox.getItems().addAll("Month", "Week", "Year");
        periodComboBox.setValue("Month");
        periodComboBox.setOnAction(e -> refreshDashboard());
    }    private void setupRecentExpensesTable() {
        // Configure table columns
        dateColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))));        categoryColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getCategory().getDisplayName()));
        
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        
        amountColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(CurrencyUtils.formatAsRupees(cellData.getValue().getAmount())));

        // Set table data
        recentExpensesTable.setItems(recentExpenses);

        // Handle row double-click for editing
        recentExpensesTable.setRowFactory(tv -> {
            TableRow<Expense> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    editExpense(row.getItem());
                }
            });
            return row;
        });
    }
    
    private void setupRecentIncomeTable() {
        // Configure table columns
        incomeDateColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))));
        
        incomeSourceColumn.setCellValueFactory(new PropertyValueFactory<>("source"));
        
        incomeAmountColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(CurrencyUtils.formatAsRupees(cellData.getValue().getAmount())));

        // Set table data
        recentIncomeTable.setItems(recentIncomes);

        // Handle row double-click for editing
        recentIncomeTable.setRowFactory(tv -> {
            TableRow<Income> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    editIncome(row.getItem());
                }
            });
            return row;        });
    }
    
    private void setupTableContextMenu() {
        // Expense table context menu
        ContextMenu expenseContextMenu = new ContextMenu();
        
        MenuItem editExpenseItem = new MenuItem("Edit Expense");
        editExpenseItem.setOnAction(e -> {
            Expense selectedExpense = recentExpensesTable.getSelectionModel().getSelectedItem();
            if (selectedExpense != null) {
                editExpense(selectedExpense);
            }
        });
        
        MenuItem deleteExpenseItem = new MenuItem("Delete Expense");
        deleteExpenseItem.setOnAction(e -> {
            Expense selectedExpense = recentExpensesTable.getSelectionModel().getSelectedItem();
            if (selectedExpense != null) {
                deleteExpense(selectedExpense);
            }
        });
        
        expenseContextMenu.getItems().addAll(editExpenseItem, deleteExpenseItem);
        recentExpensesTable.setContextMenu(expenseContextMenu);
        
        // Income table context menu
        ContextMenu incomeContextMenu = new ContextMenu();
        
        MenuItem editIncomeItem = new MenuItem("Edit Income");
        editIncomeItem.setOnAction(e -> {
            Income selectedIncome = recentIncomeTable.getSelectionModel().getSelectedItem();
            if (selectedIncome != null) {
                editIncome(selectedIncome);
            }
        });
        
        MenuItem deleteIncomeItem = new MenuItem("Delete Income");
        deleteIncomeItem.setOnAction(e -> {
            Income selectedIncome = recentIncomeTable.getSelectionModel().getSelectedItem();
            if (selectedIncome != null) {
                deleteIncome(selectedIncome);
            }
        });
        
        incomeContextMenu.getItems().addAll(editIncomeItem, deleteIncomeItem);
        recentIncomeTable.setContextMenu(incomeContextMenu);
    }private void refreshDashboard() {
        updateSummaryCards();
        updatePieChart();
        updateIncomePieChart();
        // Tables are now updated only when switching to their specific views
    }
    
    private void updateSummaryCards() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = getStartDateForPeriod(endDate);
        
        Double totalExpenses = expenseService.getTotalExpenses(startDate, endDate);
        Double totalIncome = incomeService.getTotalIncome(startDate, endDate);
        
        // Use total income from income service
        Double balance = totalIncome - totalExpenses;
        
        incomeLabel.setText(CurrencyUtils.formatAsRupees(totalIncome));
        expensesLabel.setText(CurrencyUtils.formatAsRupees(totalExpenses));
        balanceLabel.setText(CurrencyUtils.formatAsRupees(balance));
        
        // Set balance color based on value
        if (balance >= 0) {
            balanceLabel.setStyle("-fx-text-fill: #4CAF50;"); // Green
        } else {
            balanceLabel.setStyle("-fx-text-fill: #F44336;"); // Red
        }
    }    private void updatePieChart() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = getStartDateForPeriod(endDate);
        
        Map<ExpenseCategory, Double> categoryExpenses = expenseService.getExpensesByCategory(startDate, endDate);
        
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        Double total = 0.0;
        
        // Calculate total for percentage display
        for (Double amount : categoryExpenses.values()) {
            if (amount > 0) {
                total += amount;
            }
        }
        
        // Create chart data with category names and amounts
        for (Map.Entry<ExpenseCategory, Double> entry : categoryExpenses.entrySet()) {
            if (entry.getValue() > 0) {
                pieChartData.add(new PieChart.Data(entry.getKey().getDisplayName(), entry.getValue()));
            }
        }
        
        expensesPieChart.setData(pieChartData);
        expensesPieChart.setTitle("Expenses by Category");
        
        // Set animations and slices styling
        expensesPieChart.setAnimated(true);
        expensesPieChart.setLabelLineLength(20);
        expensesPieChart.setLabelsVisible(true);
        expensesPieChart.setStartAngle(90);
        
        // Add tooltips and percentage labels to pie slices
        final Double finalTotal = total;
        pieChartData.forEach(data -> {
            String percentage = String.format("%.1f%%", (data.getPieValue() / finalTotal * 100));
            String amount = CurrencyUtils.formatAsRupees(data.getPieValue());
            String tooltipText = data.getName() + "\n" + amount + " (" + percentage + ")";
            
            // Use labels with percentages
            data.nameProperty().set(data.getName() + " (" + percentage + ")");
            
            // Add hover effect using tooltip
            Tooltip tooltip = new Tooltip(tooltipText);
            Tooltip.install(data.getNode(), tooltip);
            
            // Add hover effect
            data.getNode().setOnMouseEntered(e -> 
                data.getNode().setStyle("-fx-pie-color: derive(" + data.getNode().getStyle() + ", -20%);"));
                
            data.getNode().setOnMouseExited(e ->                data.getNode().setStyle("-fx-pie-color: " + data.getNode().getStyle() + ";"));
        });
    }
    
    private void updateIncomePieChart() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = getStartDateForPeriod(endDate);
        
        // Get income data grouped by source
        Map<String, Double> incomeBySource = incomeService.getIncomeBySource(startDate, endDate);
        
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        Double total = 0.0;
        
        // Calculate total for percentage display
        for (Double amount : incomeBySource.values()) {
            if (amount > 0) {
                total += amount;
            }
        }
        
        // Create chart data with source names and amounts
        for (Map.Entry<String, Double> entry : incomeBySource.entrySet()) {
            if (entry.getValue() > 0) {
                pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }
        }
        
        incomePieChart.setData(pieChartData);
        incomePieChart.setTitle("Income by Source");
        
        // Set animations and slices styling
        incomePieChart.setAnimated(true);
        incomePieChart.setLabelLineLength(20);
        incomePieChart.setLabelsVisible(true);
        incomePieChart.setStartAngle(90);
        
        // Add tooltips and percentage labels to pie slices
        final Double finalTotal = total;
        pieChartData.forEach(data -> {
            String percentage = String.format("%.1f%%", (data.getPieValue() / finalTotal * 100));
            String amount = CurrencyUtils.formatAsRupees(data.getPieValue());
            String tooltipText = data.getName() + "\n" + amount + " (" + percentage + ")";
            
            // Use labels with percentages
            data.nameProperty().set(data.getName() + " (" + percentage + ")");
            
            // Add hover effect using tooltip
            Tooltip tooltip = new Tooltip(tooltipText);
            Tooltip.install(data.getNode(), tooltip);
            
            // Add hover effect
            data.getNode().setOnMouseEntered(e -> 
                data.getNode().setStyle("-fx-pie-color: derive(" + data.getNode().getStyle() + ", -20%);"));
                
            data.getNode().setOnMouseExited(e ->
                data.getNode().setStyle("-fx-pie-color: " + data.getNode().getStyle() + ";"));
        });
    }
    
    private void updateRecentExpensesTable() {
        List<Expense> recent = expenseService.getRecentExpenses();
        recentExpenses.clear();
        recentExpenses.addAll(recent);
    }
    
    private void updateRecentIncomeTable() {
        List<Income> recent = incomeService.getRecentIncomes();
        recentIncomes.clear();        recentIncomes.addAll(recent);
    }
    
    private LocalDate getStartDateForPeriod(LocalDate endDate) {
        String period = periodComboBox.getValue();
        switch (period) {
            case "Week":
                return endDate.minusWeeks(1);
            case "Year":
                return endDate.minusYears(1);
            case "Month":
            default:                return endDate.minusMonths(1);
        }
    }
      @FXML
    private void handleAddExpense() {
        try {
            openExpenseModal(null);
            hasUserAddedTransactions = true;
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Add Expense dialog: " + e.getMessage());
        }
    }
    
    private void editExpense(Expense expense) {
        openExpenseModal(expense);
    }
      private void openExpenseModal(Expense expenseToEdit) {
        try {
            Stage primaryStage = (Stage) addExpenseButton.getScene().getWindow();
            String title = expenseToEdit != null ? "Edit Expense" : "Add New Expense";
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/expense-modal.fxml"));
            Parent root = loader.load();            ExpenseModalController controller = loader.getController();
            controller.setExpenseService(expenseService);
            controller.setOnSaveCallback(() -> {
                updateSummaryCards();
                updatePieChart();
                updateIncomePieChart();
                updateRecentExpensesTable();
            });
            
            if (expenseToEdit != null) {
                controller.setExpenseForEdit(expenseToEdit);
            }
            
            Stage modalStage = new Stage();
            modalStage.setTitle(title);
            modalStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
            modalStage.initOwner(primaryStage);
            modalStage.initStyle(javafx.stage.StageStyle.UTILITY);
            modalStage.setResizable(false);
            
            Scene scene = new Scene(root);
            
            try {
                String cssPath = "/css/styles-enhanced.css";
                if (getClass().getResource(cssPath) != null) {
                    scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
                }
            } catch (Exception e) {
                System.err.println("Warning: Could not load CSS file: " + e.getMessage());
            }
            
            modalStage.setScene(scene);
            modalStage.showAndWait();
            
            // Mark that user has made changes when a modal is successfully shown
            updateTransactionStatus();
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not open expense dialog: " + e.getMessage());
        }
    }    private void deleteExpense(Expense expense) {
        String message = expense.getDescription() + " - " + CurrencyUtils.formatAsRupees(expense.getAmount());
        if (ModalUtils.showConfirmationDialog("Delete Expense", "Are you sure you want to delete this expense?", message)) {
            expenseService.deleteExpense(expense.getId());
            // Update only what's needed based on current view
            updateSummaryCards();
            updatePieChart();
            updateIncomePieChart(); // Update income chart too as balance changed
            updateRecentExpensesTable();
        }
    }
    
    private void deleteIncome(Income income) {
        String message = income.getSource() + " - " + CurrencyUtils.formatAsRupees(income.getAmount());
        if (ModalUtils.showConfirmationDialog("Delete Income", "Are you sure you want to delete this income?", message)) {
            incomeService.deleteIncome(income.getId());
            // Update only what's needed based on current view
            updateSummaryCards();
            updatePieChart();
            updateIncomePieChart();
            updateRecentIncomeTable();
        }
    }
      private void showAlert(String title, String message) {
        ModalUtils.showErrorDialog(title, message);
    }    /**
     * Called after a user adds an expense to update the transaction status
     */
    private void updateTransactionStatus() {
        if (!hasUserAddedTransactions) {
            hasUserAddedTransactions = true;
            updateSummaryCards();
            updatePieChart();
            updateIncomePieChart();
        }
    }
    
    @FXML
    private void handleAddIncome() {
        try {
            openIncomeModal(null);
            hasUserAddedTransactions = true;
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Add Income dialog: " + e.getMessage());
        }
    }
    
    private void editIncome(Income income) {
        openIncomeModal(income);
    }
    
    private void openIncomeModal(Income incomeToEdit) {
        try {
            Stage primaryStage = (Stage) addExpenseButton.getScene().getWindow();
            String title = incomeToEdit != null ? "Edit Income" : "Add New Income";
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/income-modal.fxml"));
            Parent root = loader.load();            IncomeModalController controller = loader.getController();
            controller.setIncomeService(incomeService);
            controller.setOnSaveCallback(() -> {
                updateSummaryCards();
                updatePieChart();
                updateIncomePieChart();
                updateRecentIncomeTable();
            });
            
            if (incomeToEdit != null) {
                controller.setIncomeForEdit(incomeToEdit);
            }
            
            Stage modalStage = new Stage();
            modalStage.setTitle(title);
            modalStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
            modalStage.initOwner(primaryStage);
            modalStage.initStyle(javafx.stage.StageStyle.UTILITY);
            modalStage.setResizable(false);
            
            Scene scene = new Scene(root);
            
            try {
                String cssPath = "/css/styles-enhanced.css";
                if (getClass().getResource(cssPath) != null) {
                    scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
                }
            } catch (Exception e) {
                System.err.println("Warning: Could not load CSS file: " + e.getMessage());
            }
            
            modalStage.setScene(scene);
            modalStage.showAndWait();
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not open income dialog: " + e.getMessage());
        }
    }
      // Export functionality has been removed as per requirements
    
    /**
     * Gets budget status information for the current period.
     * This is an initial implementation for budget tracking feature.
     * Will be expanded in future versions.
     */    private void getBudgetStatus() {
        // This is a placeholder for future budget tracking functionality
        LocalDate endDate = LocalDate.now();
        java.time.YearMonth currentMonth = java.time.YearMonth.from(endDate);
        
        // Validate that service is working but don't use results yet
        // Will be displayed in future UI implementations
        budgetGoalService.getBudgetPerformance(currentMonth);
    }
      @FXML
    private void handleTabClick(javafx.scene.input.MouseEvent event) {
        Label source = (Label) event.getSource();
        
        if (source == dashboardTab) {
            switchToView("dashboard");
        } else if (source == expensesTab) {
            switchToView("expenses");
        } else if (source == incomeTab) {
            switchToView("income");
        }
    }
      private void switchToView(String viewName) {
        // Reset all tabs styling
        dashboardTab.getStyleClass().remove("nav-tab-active");
        expensesTab.getStyleClass().remove("nav-tab-active");
        incomeTab.getStyleClass().remove("nav-tab-active");
        
        // Hide all views
        dashboardView.setVisible(false);
        expensesView.setVisible(false);
        incomeView.setVisible(false);
        
        // Show selected view and activate tab
        switch (viewName) {
            case "expenses":
                expensesView.setVisible(true);
                expensesTab.getStyleClass().add("nav-tab-active");
                updateRecentExpensesTable(); // Refresh the expenses data
                break;
            case "income":
                incomeView.setVisible(true);
                incomeTab.getStyleClass().add("nav-tab-active");
                updateRecentIncomeTable(); // Refresh the income data
                break;
            case "dashboard":
            default:
                dashboardView.setVisible(true);
                dashboardTab.getStyleClass().add("nav-tab-active");
                updateSummaryCards(); // Update just the cards
                updatePieChart(); // And the pie chart
                updateIncomePieChart(); // And the income pie chart
                break;
        }
    }
    
    @FXML
    private void handleRemoveExpense() {
        Expense selectedExpense = recentExpensesTable.getSelectionModel().getSelectedItem();
        if (selectedExpense != null) {
            deleteExpense(selectedExpense);
        } else {
            showAlert("No Selection", "Please select an expense to remove.");
        }
    }
    
    @FXML
    private void handleRemoveIncome() {
        Income selectedIncome = recentIncomeTable.getSelectionModel().getSelectedItem();
        if (selectedIncome != null) {
            deleteIncome(selectedIncome);
        } else {
            showAlert("No Selection", "Please select an income entry to remove.");
        }
    }
    
    @FXML
    private void handleViewExpenses() {
        switchToView("expenses");
    }
    
    @FXML
    private void handleViewIncome() {
        switchToView("income");
    }
}
