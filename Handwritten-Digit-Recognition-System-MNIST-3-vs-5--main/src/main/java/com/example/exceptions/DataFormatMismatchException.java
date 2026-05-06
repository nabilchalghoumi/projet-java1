package com.example.exceptions;

/**
 * Checked exception thrown when CSV/text data format does not match expected structure.
 * Used primarily by TextFileHandler when validating CSV lines.
 * Validates: exactly 785 fields (784 integers + 1 label), all values are valid integers.
 */
public class DataFormatMismatchException extends Exception {
    
    private int lineNumber;
    private String problematicValue;
    
    /**
     * Constructor with line and value information.
     * 
     * @param lineNumber the line number (1-based) where the error occurred
     * @param expectedFields expected number of fields
     * @param actualFields actual number of fields found
     */
    public DataFormatMismatchException(int lineNumber, int expectedFields, int actualFields) {
        super("CSV format mismatch at line " + lineNumber + ": expected " + expectedFields + 
              " fields, but got " + actualFields + " fields.");
        this.lineNumber = lineNumber;
    }
    
    /**
     * Constructor for invalid integer values in CSV.
     * 
     * @param lineNumber the line number (1-based) where the error occurred
     * @param problematicValue the value that could not be parsed as integer
     */
    public DataFormatMismatchException(int lineNumber, String problematicValue) {
        super("CSV format mismatch at line " + lineNumber + ": value '" + problematicValue + 
              "' cannot be converted to integer.");
        this.lineNumber = lineNumber;
        this.problematicValue = problematicValue;
    }
    
    /**
     * Constructor with custom message and cause.
     * 
     * @param message detailed error message
     * @param cause the underlying cause
     */
    public DataFormatMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public int getLineNumber() {
        return lineNumber;
    }
    
    public String getProblematicValue() {
        return problematicValue;
    }
}
