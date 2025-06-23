package com.expensetracker.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Utility class for creating and managing modal dialogs.
 */
public class ModalUtils {

    /**
     * Opens a modal dialog with the specified FXML file.
     * @param fxmlPath Path to the FXML file
     * @param title Title of the modal dialog
     * @param owner Parent stage
     * @param controller Controller object to set     * @return The created stage
     * @throws IOException If FXML file cannot be loaded
     */
    public static Stage openModal(String fxmlPath, String title, Stage owner, Object controller) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ModalUtils.class.getResource(fxmlPath));
        if (controller != null) {
            loader.setController(controller);
        }
        
        Parent root = loader.load();
        
        Stage modalStage = new Stage();
        modalStage.setTitle(title);
        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(owner);
        modalStage.initStyle(StageStyle.UTILITY);
        modalStage.setResizable(false);
          Scene scene = new Scene(root);
        try {
            String cssPath = "/css/styles-enhanced.css";
            if (ModalUtils.class.getResource(cssPath) != null) {
                scene.getStylesheets().add(ModalUtils.class.getResource(cssPath).toExternalForm());
            } else {
                System.err.println("Warning: CSS file not found at path: " + cssPath);
            }
        } catch (Exception e) {
            System.err.println("Warning: Could not load CSS file: " + e.getMessage());
            e.printStackTrace();
        }
        modalStage.setScene(scene);
        
        return modalStage;
    }

    /**
     * Opens a modal dialog and waits for it to close.
     * @param fxmlPath Path to the FXML file
     * @param title Title of the modal dialog
     * @param owner Parent stage
     * @param controller Controller object to set
     * @throws IOException If FXML file cannot be loaded
     */
    public static void openModalAndWait(String fxmlPath, String title, Stage owner, Object controller) throws IOException {
        Stage modalStage = openModal(fxmlPath, title, owner, controller);
        modalStage.showAndWait();
    }

    /**
     * Gets the controller from an FXML loader.
     * @param loader The FXMLLoader
     * @param <T> Controller type
     * @return The controller instance
     */
    public static <T> T getController(FXMLLoader loader) {
        return loader.getController();
    }

    /**
     * Shows a confirmation dialog and returns the result.
     * @param title Title of the dialog
     * @param headerText Header text of the dialog
     * @param contentText Content text of the dialog
     * @return true if user clicked OK, false otherwise
     */
    public static boolean showConfirmationDialog(String title, String headerText, String contentText) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        
        java.util.Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK;
    }
    
    /**
     * Shows an error dialog.
     * @param title Title of the dialog
     * @param message Error message to display
     */
    public static void showErrorDialog(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Centers a stage on the screen.
     * @param stage The stage to center
     */
    public static void centerStageOnScreen(Stage stage) {
        javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }
}
