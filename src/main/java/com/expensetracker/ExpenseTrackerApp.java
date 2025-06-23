package com.expensetracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class for the ExpenseTracker Desktop Application.
 * Entry point for the JavaFX application.
 */
public class ExpenseTrackerApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the main dashboard FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
        Parent root = loader.load();

        // Create the scene
        Scene scene = new Scene(root, 1000, 700);
          // Add CSS stylesheet (enhanced version for better visuals)
        scene.getStylesheets().add(getClass().getResource("/css/styles-enhanced.css").toExternalForm());

        // Configure the primary stage
        primaryStage.setTitle("ExpenseTracker - Personal Finance Manager");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
