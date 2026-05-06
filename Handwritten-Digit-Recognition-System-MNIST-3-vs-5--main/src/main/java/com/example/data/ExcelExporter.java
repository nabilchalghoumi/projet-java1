package com.example.data;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.example.exceptions.DataFormatMismatchException;

/**
 * Concrete implementation for exporting MNIST data to Excel format (.xlsx).
 * Reads CSV format and writes to Excel workbook with one row per image.
 * Each column represents a pixel (784 columns) plus one label column.
 */
public class ExcelExporter extends MNISTProvider {
    
    private String csvPath;
    private String excelPath;
    private static final int EXPECTED_FIELDS = 785; // 784 pixels + 1 label
    
    /**
     * Constructor with CSV and Excel file paths.
     * 
     * @param csvPath path to input CSV file
     * @param excelPath path to output Excel file (.xlsx)
     * @param nbImages number of images expected
     */
    public ExcelExporter(String csvPath, String excelPath, int nbImages) {
        super(nbImages, 28);
        this.csvPath = csvPath;
        this.excelPath = excelPath;
    }
    
    @Override
    public void load() throws DataFormatMismatchException {
        try {
            List<String> lines = Files.readAllLines(Paths.get(csvPath));
            parseCSV(lines);
        } catch (IOException e) {
            throw new DataFormatMismatchException("Error reading CSV file: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void export() throws DataFormatMismatchException {
        if (pixelData == null || labels == null) {
            throw new DataFormatMismatchException("No data to export. Call load() first.", null);
        }
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("MNIST Data");
            
            // Create header row with pixel column names + label
            Row headerRow = sheet.createRow(0);
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);
            
            for (int i = 0; i < 784; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue("Pixel_" + i);
                cell.setCellStyle(headerStyle);
            }
            Cell labelCell = headerRow.createCell(784);
            labelCell.setCellValue("Label");
            labelCell.setCellStyle(headerStyle);
            
            // Write data rows
            for (int i = 0; i < pixelData.length; i++) {
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < pixelData[i].length; j++) {
                    row.createCell(j).setCellValue(pixelData[i][j]);
                }
                row.createCell(784).setCellValue(labels[i]);
            }
            
            // Auto-size columns (optional, for better readability)
            for (int i = 0; i < 785; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Write workbook to file
            try (FileOutputStream fos = new FileOutputStream(excelPath)) {
                workbook.write(fos);
            }
            
        } catch (IOException e) {
            throw new DataFormatMismatchException("Error writing Excel file: " + e.getMessage(), e);
        }
    }
    
    /**
     * Parse CSV format into pixelData and labels arrays.
     */
    private void parseCSV(List<String> lines) throws DataFormatMismatchException {
        List<int[]> pixelsList = new ArrayList<>();
        List<String> labelsList = new ArrayList<>();
        
        for (int lineNum = 0; lineNum < lines.size(); lineNum++) {
            String line = lines.get(lineNum);
            String[] fields = line.split(",");
            
            if (fields.length != EXPECTED_FIELDS) {
                throw new DataFormatMismatchException(lineNum + 1, EXPECTED_FIELDS, fields.length);
            }
            
            int[] pixels = new int[784];
            try {
                for (int i = 0; i < 784; i++) {
                    pixels[i] = Integer.parseInt(fields[i].trim());
                    if (!validatePixelIntensity(pixels[i])) {
                        throw new DataFormatMismatchException(lineNum + 1, fields[i]);
                    }
                }
            } catch (NumberFormatException e) {
                throw new DataFormatMismatchException(lineNum + 1, fields[784]);
            }
            
            pixelsList.add(pixels);
            labelsList.add(fields[784].trim());
        }
        
        pixelData = pixelsList.toArray(new int[0][]);
        labels = labelsList.toArray(new String[0]);
        nbImages = pixelData.length;
    }
    
    /**
     * [Instruction 1.3] createExcelFile(String nomFichier)
     * Convertit un fichier texte (format de createTextFile) en classeur Excel.
     * Chaque colonne correspond à un pixel (784 colonnes) + une dernière colonne label.
     * 
     * @param nomFichier the input CSV/text filename
     * @throws DataFormatMismatchException if conversion fails
     */
    public static void createExcelFile(String nomFichier) throws DataFormatMismatchException {
        String excelPath = nomFichier.substring(0, nomFichier.lastIndexOf('.')) + ".xlsx";
        createFromCSV(nomFichier, excelPath);
    }

    /**
     * Convenience method to convert CSV directly to Excel in one operation.
     * 
     * @param csvPath input CSV file path
     * @param excelPath output Excel file path
     */
    public static void createFromCSV(String csvPath, String excelPath) throws DataFormatMismatchException {
        ExcelExporter exporter = new ExcelExporter(csvPath, excelPath, Integer.MAX_VALUE);
        exporter.load();
        exporter.export();
    }
}
