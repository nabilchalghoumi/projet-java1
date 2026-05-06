package com.example.exceptions;

/**
 * Checked exception thrown when MNIST binary files (train-images or train-labels) 
 * are not found or cannot be read.
 * Used primarily by BinaryMNISTReader.
 */
public class MNISTFileNotFoundException extends Exception {
    
    private String filePath;
    
    /**
     * Constructor with file path information.
     * 
     * @param filePath the path to the missing or unreadable file
     */
    public MNISTFileNotFoundException(String filePath) {
        super("MNIST file not found or unreadable: " + filePath);
        this.filePath = filePath;
    }
    
    /**
     * Constructor with custom message and cause.
     * 
     * @param message detailed error message
     * @param cause the underlying cause (e.g., IOException)
     */
    public MNISTFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public String getFilePath() {
        return filePath;
    }
}
