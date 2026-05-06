package com.example.exceptions;

/**
 * Checked exception thrown when an image does not have the required 28x28 pixel dimensions.
 * Used primarily by imageToFile() when processing PNG images.
 */
public class InvalidDimensionsException extends Exception {
    
    private int actualWidth;
    private int actualHeight;
    
    /**
     * Constructor with detailed dimension information.
     * 
     * @param actualWidth the actual width of the image in pixels
     * @param actualHeight the actual height of the image in pixels
     */
    public InvalidDimensionsException(int actualWidth, int actualHeight) {
        super("Invalid image dimensions: expected 28x28 pixels, but got " + 
              actualWidth + "x" + actualHeight + " pixels.");
        this.actualWidth = actualWidth;
        this.actualHeight = actualHeight;
    }
    
    /**
     * Constructor with custom message and cause.
     * 
     * @param message detailed error message
     * @param cause the underlying cause
     */
    public InvalidDimensionsException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public int getActualWidth() {
        return actualWidth;
    }
    
    public int getActualHeight() {
        return actualHeight;
    }
}
