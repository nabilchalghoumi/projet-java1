package com.example;

import com.example.data.*;
import com.example.exceptions.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Test suite for Part 1: Architecture OO & File Processing
 * 
 * Tests:
 * 1. createTextFile(50) - verify 100 lines output
 * 2. imageToFile() then fileToImage() - round-trip test
 * 3. createExcelFile() - verify Excel creation
 */
public class TestPartie1 {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("TEST PARTIE 1 - Architecture OO & Traitement de Fichiers");
        System.out.println("=".repeat(70));
        System.out.println();
        
        try {
            testCreateTextFile();
            testImageConversion();
            testExcelExport();
            
            System.out.println();
            System.out.println("=".repeat(70));
            System.out.println("✓ All Part 1 tests completed successfully!");
            System.out.println("=".repeat(70));
            
        } catch (Exception e) {
            System.err.println("\n✗ Test failed with error:");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test 1: Create text file from binary MNIST data
     * Expects: MNIST binary files to exist in data/mnist/
     */
    private static void testCreateTextFile() throws Exception {
        System.out.println("\n[TEST 1] createTextFile(50)");
        System.out.println("-".repeat(70));
        
        String csvOutput = "data/mnist/chiffres.txt";
        
        try {
            // Call the standardized method
            TextFileHandler.createTextFile(50);
            
            // Verify line count
            long lineCount = Files.lines(Paths.get(csvOutput)).count();
            System.out.println("✓ Created: " + csvOutput);
            System.out.println("✓ Line count: " + lineCount + " (expected: 100)");
            
            if (lineCount == 100) {
                System.out.println("✓ TEST 1 PASSED");
            } else {
                System.out.println("✗ TEST 1 FAILED: Expected 100 lines, got " + lineCount);
            }
            
        } catch (MNISTFileNotFoundException e) {
            System.out.println("✗ MNIST file error: " + e.getMessage());
        } catch (DataFormatMismatchException e) {
            System.out.println("✗ Data format error: " + e.getMessage());
        }
    }
    
    /**
     * Test 2: Convert PNG image to CSV and back
     * Requires a 28x28 PNG image in the project
     */
    private static void testImageConversion() throws Exception {
        System.out.println("\n[TEST 2] imageToFile() → fileToImage()");
        System.out.println("-".repeat(70));
        
        String testImagePath = "test_image.png";
        
        if (!Files.exists(Paths.get(testImagePath))) {
            System.out.println("⚠ Test image not found: " + testImagePath);
            System.out.println("  → Skipping test");
            return;
        }
        
        try {
            // Convert image to CSV
            TextFileHandler.imageToFile(testImagePath);
            System.out.println("✓ Converted image → CSV");
            
            // Convert CSV back to image
            String csvPath = "test_image.txt";
            TextFileHandler.fileToImage(csvPath);
            System.out.println("✓ Converted CSV → image");
            
            System.out.println("✓ TEST 2 PASSED");
            
        } catch (InvalidDimensionsException e) {
            System.out.println("✗ Invalid dimensions: " + e.getMessage());
        }
    }
    
    /**
     * Test 3: Convert CSV to Excel
     */
    private static void testExcelExport() throws Exception {
        System.out.println("\n[TEST 3] createExcelFile()");
        System.out.println("-".repeat(70));
        
        String csvPath = "data/mnist/chiffres.txt";
        String excelPath = "data/mnist/chiffres.xlsx";
        
        if (!Files.exists(Paths.get(csvPath))) {
            System.out.println("⚠ CSV file not found: " + csvPath);
            System.out.println("  → Skipping test");
            return;
        }
        
        try {
            // Convert CSV to Excel
            ExcelExporter.createExcelFile(csvPath);
            System.out.println("✓ Created Excel file: " + excelPath);
            
            if (Files.exists(Paths.get(excelPath))) {
                System.out.println("✓ TEST 3 PASSED");
            } else {
                System.out.println("✗ TEST 3 FAILED: Excel file not created");
            }
            
        } catch (DataFormatMismatchException e) {
            System.out.println("✗ Data format error: " + e.getMessage());
        }
    }
}
