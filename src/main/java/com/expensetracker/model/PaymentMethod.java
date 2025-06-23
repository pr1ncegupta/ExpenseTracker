package com.expensetracker.model;

/**
 * Enumeration representing predefined payment methods.
 * Payment methods are fixed and cannot be modified by users.
 */
public enum PaymentMethod {
    CASH("Cash"),
    UPI("UPI"),
    CARD("Card");

    private final String displayName;

    PaymentMethod(String displayName) {
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
