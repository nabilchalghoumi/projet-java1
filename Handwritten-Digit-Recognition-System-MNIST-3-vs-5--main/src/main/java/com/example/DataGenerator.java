package com.example;

import com.example.data.*;
import com.example.exceptions.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Utility class to generate training and test datasets from MNIST binary files.
 * 
 * This class orchestrates the conversion pipeline:
 * Binary MNIST → CSV → Excel & ARFF
 */
public class DataGenerator {
    
    private static final String MNIST_DIR = "data/mnist";
    private static final String IMAGES_FILE = MNIST_DIR + "/train-images-idx3-ubyte";
    private static final String LABELS_FILE = MNIST_DIR + "/train-labels-idx1-ubyte";
    
    public static void main(String[] args) {
        System.out.println(repeat('=', 70));
        System.out.println("MNIST TRAINING DATA GENERATOR");
        System.out.println(repeat('=', 70));
        System.out.println();
        
        try {
            // Verify MNIST files exist
            if (!Files.exists(Paths.get(IMAGES_FILE)) || !Files.exists(Paths.get(LABELS_FILE))) {
                System.err.println("✗ ERROR: MNIST binary files not found!");
                System.err.println();
                System.err.println("Expected files:");
                System.err.println("  - " + IMAGES_FILE);
                System.err.println("  - " + LABELS_FILE);
                System.err.println();
                System.err.println("Download from:");
                System.err.println("  Kaggle: https://www.kaggle.com/datasets/hojjatk/mnist-dataset");
                System.err.println("  Official: http://yann.lecun.com/exdb/mnist/");
                System.err.println();
                System.err.println("Extract and place files in: " + MNIST_DIR + "/");
                return;
            }
            
            System.out.println("[1/6] Reading MNIST binary files (400 threes + 400 fives for training)...");
            BinaryMNISTReader trainReader = new BinaryMNISTReader(IMAGES_FILE, LABELS_FILE, 400);
            trainReader.load();
            System.out.println("✓ Loaded " + trainReader.getNbImages() + " training samples");
            System.out.println();
            
            System.out.println("[2/6] Exporting training data to CSV...");
            String trainCsv = MNIST_DIR + "/train-data.csv";
            TextFileHandler.createFromBinaryReader(trainReader, trainCsv);
            System.out.println("✓ Created: " + trainCsv);
            System.out.println();
            
            System.out.println("[3/6] Converting training CSV to ARFF format...");
            String trainArff = MNIST_DIR + "/train-data.arff";
            TextFileHandler.csvToArff(trainCsv, trainArff);
            System.out.println("✓ Created: " + trainArff);
            System.out.println();
            
            System.out.println("[4/6] Reading MNIST binary files (50 threes + 50 fives for testing)...");
            BinaryMNISTReader testReader = new BinaryMNISTReader(IMAGES_FILE, LABELS_FILE, 50);
            testReader.load();
            System.out.println("✓ Loaded " + testReader.getNbImages() + " test samples");
            System.out.println();
            
            System.out.println("[5/6] Exporting test data to CSV...");
            String testCsv = MNIST_DIR + "/test-data.csv";
            TextFileHandler.createFromBinaryReader(testReader, testCsv);
            System.out.println("✓ Created: " + testCsv);
            System.out.println();
            
            System.out.println("[6/6] Converting test CSV to ARFF format...");
            String testArff = MNIST_DIR + "/test-data.arff";
            TextFileHandler.csvToArff(testCsv, testArff);
            System.out.println("✓ Created: " + testArff);
            System.out.println();
            
            // Verify all files
            System.out.println(repeat('=', 70));
            System.out.println("✓ GENERATION COMPLETE!");
            System.out.println(repeat('=', 70));
            System.out.println();
            
            printFileStats(trainCsv, "Training CSV");
            printFileStats(trainArff, "Training ARFF");
            printFileStats(testCsv, "Test CSV");
            printFileStats(testArff, "Test ARFF");
            
            System.out.println();
            System.out.println("Next steps:");
            System.out.println("  1. Train classifiers: mvn exec:java -Dexec.mainClass=\"com.example.ml.ModelComparator\"");
            System.out.println("  2. Launch GUI: mvn exec:java -Dexec.mainClass=\"com.example.gui.RecognitionGUI\"");
            System.out.println();
            
        } catch (MNISTFileNotFoundException e) {
            System.err.println("✗ File error: " + e.getMessage());
            System.err.println("  Path: " + e.getFilePath());
        } catch (DataFormatMismatchException e) {
            System.err.println("✗ Format error: " + e.getMessage());
            if (e.getLineNumber() > 0) {
                System.err.println("  Line: " + e.getLineNumber());
            }
        } catch (Exception e) {
            System.err.println("✗ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Print file statistics.
     */
    private static void printFileStats(String filePath, String label) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("⚠ " + label + ": FILE NOT FOUND");
            return;
        }
        
        long lines = Files.lines(Paths.get(filePath)).count();
        long bytes = file.length();
        String size = formatBytes(bytes);
        
        System.out.println("✓ " + label + ": " + lines + " lines, " + size);
    }
    
    /**
     * Format bytes to human-readable size.
     */
    private static String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
    }
    
    /**
     * Java 8-compatible string repetition helper.
     */
    private static String repeat(char c, int count) {
        if (count <= 0) {
            return "";
        }
        char[] chars = new char[count];
        java.util.Arrays.fill(chars, c);
        return new String(chars);
    }
}
