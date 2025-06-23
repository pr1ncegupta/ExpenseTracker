module expensetracker {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.graphics;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires java.desktop;

    exports com.expensetracker;
    exports com.expensetracker.controller;
    exports com.expensetracker.model;
    exports com.expensetracker.service;
    exports com.expensetracker.util;
    
    // Open packages to JavaFX for FXML injection
    opens com.expensetracker.controller to javafx.fxml;
    opens com.expensetracker.model to com.fasterxml.jackson.databind;
}
