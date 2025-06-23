package com.expensetracker.model;

/**
 * Enumeration representing predefined expense categories.
 * Categories are fixed and cannot be modified by users.
 */
public enum ExpenseCategory {
    FOOD("Food"),
    TRANSPORTATION("Transportation"),
    ENTERTAINMENT("Entertainment"),
    UTILITIES("Utilities"),
    HEALTH("Health"),
    SHOPPING("Shopping"),
    EDUCATION("Education"),
    TRAVEL("Travel"),
    OTHER("Other");

    private final String displayName;

    ExpenseCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
