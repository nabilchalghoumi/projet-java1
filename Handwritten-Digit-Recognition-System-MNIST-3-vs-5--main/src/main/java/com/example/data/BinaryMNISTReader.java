package com.example.data;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.example.exceptions.MNISTFileNotFoundException;
import com.example.exceptions.DataFormatMismatchException;

/**
 * Concrete implementation of MNISTProvider for reading binary MNIST files.
 * Parses IDX3 (images) and IDX1 (labels) formats from the official MNIST dataset.
 * 
 * Format:
 * - IDX3 images: magic=2051, then dimensions, then pixel data (uint8)
 * - IDX1 labels: magic=2049, then count, then label data (uint8)
 */
public class BinaryMNISTReader extends MNISTProvider {
    
    private String imagesPath;
    private String labelsPath;
    private int offset;
    
    /**
     * Constructor with paths to binary MNIST files.
     * 
     * @param imagesPath path to train-images-idx3-ubyte file
     * @param labelsPath path to train-labels-idx1-ubyte file
     * @param nbImages number of images to read (per class)
     */
    public BinaryMNISTReader(String imagesPath, String labelsPath, int nbImages) {
        this(imagesPath, labelsPath, nbImages, 0);
    }

    /**
     * Constructor with paths and offset.
     * 
     * @param imagesPath path to train-images-idx3-ubyte file
     * @param labelsPath path to train-labels-idx1-ubyte file
     * @param nbImages number of images to read (per class)
     * @param offset number of images to skip (per class)
     */
    public BinaryMNISTReader(String imagesPath, String labelsPath, int nbImages, int offset) {
        super(nbImages, 28);
        this.imagesPath = imagesPath;
        this.labelsPath = labelsPath;
        this.offset = offset;
    }
    
    @Override
    public void load() throws MNISTFileNotFoundException, DataFormatMismatchException {
        validateFileExistence();
        
        try {
            byte[] imageBytes = readBinaryFile(imagesPath);
            byte[] labelBytes = readBinaryFile(labelsPath);
            
            parseImages(imageBytes);
            parseLabels(labelBytes);
            filterForClasses3And5();
            
        } catch (IOException e) {
            throw new MNISTFileNotFoundException("Error reading MNIST files: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void export() {
        // Binary reader exports through TextFileHandler or ExcelExporter
        throw new UnsupportedOperationException("BinaryMNISTReader does not export directly. Use TextFileHandler or ExcelExporter.");
    }
    
    /**
     * Verify that both binary files exist and are readable.
     */
    private void validateFileExistence() throws MNISTFileNotFoundException {
        if (!Files.exists(Paths.get(imagesPath))) {
            throw new MNISTFileNotFoundException(imagesPath);
        }
        if (!Files.exists(Paths.get(labelsPath))) {
            throw new MNISTFileNotFoundException(labelsPath);
        }
    }
    
    /**
     * Read entire binary file into byte array.
     */
    private byte[] readBinaryFile(String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }
    
    /**
     * Parse IDX3 format image data.
     * Format: magic (4 bytes) | numImages (4) | numRows (4) | numCols (4) | pixel data
     */
    private void parseImages(byte[] data) throws DataFormatMismatchException {
        if (data.length < 16) {
            throw new DataFormatMismatchException("Invalid IDX3 file: too short", null);
        }
        
        int magic = readInt32BigEndian(data, 0);
        if (magic != 2051) {
            throw new DataFormatMismatchException("Invalid IDX3 magic number: " + magic, null);
        }
        
        int numImages = readInt32BigEndian(data, 4);
        int numRows = readInt32BigEndian(data, 8);
        int numCols = readInt32BigEndian(data, 12);
        
        if (numRows != 28 || numCols != 28) {
            throw new DataFormatMismatchException("Expected 28x28 pixels, got " + numRows + "x" + numCols, null);
        }
        
        // Allocate space for images (we'll filter later)
        pixelData = new int[numImages][784];
        
        int offset = 16;
        for (int i = 0; i < numImages; i++) {
            for (int j = 0; j < 784; j++) {
                pixelData[i][j] = data[offset++] & 0xFF;  // Convert signed byte to unsigned int
            }
        }
    }
    
    /**
     * Parse IDX1 format label data.
     * Format: magic (4 bytes) | numLabels (4) | label data (uint8)
     */
    private void parseLabels(byte[] data) throws DataFormatMismatchException {
        if (data.length < 8) {
            throw new DataFormatMismatchException("Invalid IDX1 file: too short", null);
        }
        
        int magic = readInt32BigEndian(data, 0);
        if (magic != 2049) {
            throw new DataFormatMismatchException("Invalid IDX1 magic number: " + magic, null);
        }
        
        int numLabels = readInt32BigEndian(data, 4);
        labels = new String[numLabels];
        
        int offset = 8;
        for (int i = 0; i < numLabels; i++) {
            int label = data[offset++] & 0xFF;
            labels[i] = String.valueOf(label);
        }
    }
    
    /**
     * Filter loaded data to keep only images labeled "3" or "5".
     * Reorganizes pixelData and labels arrays to contain only these two classes.
     */
    private void filterForClasses3And5() {
        java.util.List<int[]> filtered3 = new java.util.ArrayList<>();
        java.util.List<int[]> filtered5 = new java.util.ArrayList<>();
        
        int skipped3 = 0;
        int skipped5 = 0;
        
        for (int i = 0; i < pixelData.length; i++) {
            if ("3".equals(labels[i])) {
                if (skipped3 < offset) {
                    skipped3++;
                } else if (filtered3.size() < nbImages) {
                    filtered3.add(pixelData[i]);
                }
            } else if ("5".equals(labels[i])) {
                if (skipped5 < offset) {
                    skipped5++;
                } else if (filtered5.size() < nbImages) {
                    filtered5.add(pixelData[i]);
                }
            }
            
            if (filtered3.size() >= nbImages && filtered5.size() >= nbImages) {
                break;
            }
        }
        
        // Rebuild arrays with filtered data
        int totalSize = filtered3.size() + filtered5.size();
        int[][] newPixelData = new int[totalSize][];
        String[] newLabels = new String[totalSize];
        
        int idx = 0;
        for (int[] pixels : filtered3) {
            newPixelData[idx] = pixels;
            newLabels[idx] = "trois";
            idx++;
        }
        for (int[] pixels : filtered5) {
            newPixelData[idx] = pixels;
            newLabels[idx] = "cinq";
            idx++;
        }
        
        pixelData = newPixelData;
        labels = newLabels;
        nbImages = totalSize;
    }
    
    /**
     * Read a 32-bit big-endian integer from byte array.
     */
    private int readInt32BigEndian(byte[] data, int offset) {
        return ((data[offset] & 0xFF) << 24) |
               ((data[offset + 1] & 0xFF) << 16) |
               ((data[offset + 2] & 0xFF) << 8) |
               (data[offset + 3] & 0xFF);
    }
}
