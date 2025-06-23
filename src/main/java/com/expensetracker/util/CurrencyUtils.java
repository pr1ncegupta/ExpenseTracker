package com.expensetracker.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Utility class for currency formatting and operations.
 */
public class CurrencyUtils {

    private static final String RUPEE_SYMBOL = "₹";
    private static final NumberFormat CURRENCY_FORMAT = new DecimalFormat("#,##0.00");

    /**
     * Formats an amount as Rupees with the ₹ symbol.
     * @param amount The amount to format
     * @return The formatted amount with ₹ symbol
     */
    public static String formatAsRupees(Double amount) {
        if (amount == null) {
            return RUPEE_SYMBOL + "0.00";
        }
        return RUPEE_SYMBOL + CURRENCY_FORMAT.format(amount);
    }
    
    /**
     * Parses a string with Rupee symbol to a double value.
     * @param amountStr The string representation of amount (e.g., "₹100.00")
     * @return The parsed double value
     * @throws NumberFormatException if the string cannot be parsed
     */
    public static Double parseRupees(String amountStr) {
        if (amountStr == null || amountStr.trim().isEmpty()) {
            return 0.0;
        }
        
        // Remove rupee symbol and any commas or whitespace
        String cleaned = amountStr.replace(RUPEE_SYMBOL, "")
                                .replace(",", "")
                                .trim();
        
        return Double.parseDouble(cleaned);
    }
    
    /**
     * Checks if a string represents a valid currency amount.
     * @param input The input string to check
     * @return true if the string is a valid currency amount, false otherwise
     */
    public static boolean isValidCurrencyAmount(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        
        // Remove rupee symbol, commas, and whitespace
        String cleaned = input.replace(RUPEE_SYMBOL, "")
                           .replace(",", "")
                           .trim();
        
        // Check if the string matches a decimal number pattern
        return cleaned.matches("\\d+(\\.\\d{1,2})?");
    }
}
