package com.example.data;

import com.example.exceptions.MNISTFileNotFoundException;
import com.example.exceptions.DataFormatMismatchException;

/**
 * Interface defining the contract for data processing operations
 * on MNIST dataset files (binary, text, Excel, image formats).
 */
public interface DataProcessor {
    /**
     * Load data from source file and parse according to the implementation.
     * 
     * @throws MNISTFileNotFoundException if source file not found or unreadable
     * @throws DataFormatMismatchException if data format is invalid
     */
    void load() throws MNISTFileNotFoundException, DataFormatMismatchException;
    
    /**
     * Export processed data to target file in the specified format.
     * 
     * @throws DataFormatMismatchException if data cannot be exported due to format issues
     */
    void export() throws DataFormatMismatchException;
}
