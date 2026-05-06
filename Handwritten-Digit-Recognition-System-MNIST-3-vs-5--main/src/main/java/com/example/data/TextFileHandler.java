package com.example.data;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import javax.imageio.ImageIO;
import com.example.exceptions.InvalidDimensionsException;
import com.example.exceptions.DataFormatMismatchException;

/**
 * Concrete implementation for handling MNIST data in CSV text format.
 * Provides methods for:
 * - Creating CSV files from binary or pixel data
 * - Reading CSV files back into memory
 * - Converting PNG images to/from CSV
 * - Converting CSV to ARFF format for Weka
 */
public class TextFileHandler extends MNISTProvider {
    
    private String csvPath;
    private static final int EXPECTED_FIELDS = 785; // 784 pixels + 1 label
    
    /**
     * Constructor with CSV file path.
     * 
     * @param csvPath path to the CSV text file
     * @param nbImages number of images expected (used for validation)
     */
    public TextFileHandler(String csvPath, int nbImages) {
        super(nbImages, 28);
        this.csvPath = csvPath;
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
            throw new DataFormatMismatchException("No data to export. Call load() first or populate pixelData/labels.", null);
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(csvPath))) {
            for (int i = 0; i < pixelData.length; i++) {
                StringBuilder line = new StringBuilder();
                for (int j = 0; j < pixelData[i].length; j++) {
                    if (j > 0) line.append(",");
                    line.append(pixelData[i][j]);
                }
                line.append(",").append(labels[i]);
                writer.println(line.toString());
            }
        } catch (IOException e) {
            throw new DataFormatMismatchException("Error writing CSV file: " + e.getMessage(), e);
        }
    }
    
    /**
     * Parse CSV format into pixelData and labels arrays.
     * Format: 784 comma-separated integers (0-255) + label ("trois"/"cinq")
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
     * [Instruction 1.3] createTextFile(int n)
     * Lit les fichiers binaires et génère chiffres.txt contenant les n premiers exemples 
     * de « trois » et les n premiers « cinq ».
     * 
     * @param n number of examples per class
     * @throws Exception if reading or writing fails
     */
    public static void createTextFile(int n) throws Exception {
        String imagesPath = "data/mnist/train-images-idx3-ubyte";
        String labelsPath = "data/mnist/train-labels-idx1-ubyte";
        String outputPath = "data/mnist/chiffres.txt";
        
        BinaryMNISTReader reader = new BinaryMNISTReader(imagesPath, labelsPath, n);
        reader.load();
        
        createFromBinaryReader(reader, outputPath);
    }
    
    /**
     * [Instruction 1.3] imageToFile(String nomImage)
     * Accepte une image PNG 28x28 px et écrit dans un fichier texte (même nom) 
     * une ligne CSV des 784 valeurs d'intensité.
     * 
     * @param nomImage path to the PNG image
     * @throws Exception if conversion fails
     */
    public static void imageToFile(String nomImage) throws Exception {
        String outputPath = nomImage.substring(0, nomImage.lastIndexOf('.')) + ".txt";
        imageToFile(nomImage, outputPath);
    }

    /**
     * Core implementation of image to file conversion.
     */
    public static void imageToFile(String imagePath, String outputPath) 
            throws InvalidDimensionsException, IOException {
        BufferedImage img = ImageIO.read(new File(imagePath));
        if (img == null) {
            throw new IOException("Cannot read image: " + imagePath);
        }
        
        if (img.getWidth() != 28 || img.getHeight() != 28) {
            throw new InvalidDimensionsException(img.getWidth(), img.getHeight());
        }
        
        int[] pixels = new int[784];
        int idx = 0;
        for (int y = 0; y < 28; y++) {
            for (int x = 0; x < 28; x++) {
                int rgb = img.getRGB(x, y);
                int gray = (rgb >> 16) & 0xFF;
                pixels[idx++] = gray;
            }
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath))) {
            for (int i = 0; i < 784; i++) {
                if (i > 0) writer.print(",");
                writer.print(pixels[i]);
            }
            writer.println();
        }
    }
    
    /**
     * [Instruction 1.3] fileToImage(String nomFichier)
     * Opération inverse : recrée l'image PNG 28x28 px à partir du fichier texte CSV.
     * 
     * @param nomFichier path to the input CSV file
     * @throws Exception if reconstruction fails
     */
    public static void fileToImage(String nomFichier) throws Exception {
        String outputPath = nomFichier.substring(0, nomFichier.lastIndexOf('.')) + "_reconstructed.png";
        fileToImage(nomFichier, outputPath);
    }

    /**
     * Core implementation of file to image conversion.
     */
    public static void fileToImage(String csvPath, String outputPath) 
            throws DataFormatMismatchException, IOException {
        List<String> lines = Files.readAllLines(Paths.get(csvPath));
        if (lines.isEmpty()) {
            throw new DataFormatMismatchException("CSV file is empty", null);
        }
        
        String line = lines.get(0);
        String[] fields = line.split(",");
        
        if (fields.length < 784) {
            throw new DataFormatMismatchException(1, 784, fields.length);
        }
        
        int[] pixels = new int[784];
        try {
            for (int i = 0; i < 784; i++) {
                pixels[i] = Integer.parseInt(fields[i].trim());
            }
        } catch (NumberFormatException e) {
            throw new DataFormatMismatchException(1, fields[0]);
        }
        
        BufferedImage img = new BufferedImage(28, 28, BufferedImage.TYPE_BYTE_GRAY);
        int idx = 0;
        for (int y = 0; y < 28; y++) {
            for (int x = 0; x < 28; x++) {
                int gray = pixels[idx++] & 0xFF;
                int rgb = (gray << 16) | (gray << 8) | gray;
                img.setRGB(x, y, rgb);
            }
        }
        
        ImageIO.write(img, "PNG", new File(outputPath));
    }
    
    /**
     * Convert CSV text file to ARFF format for Weka.
     */
    public static void csvToArff(String csvPath, String arffPath) 
            throws DataFormatMismatchException, IOException {
        List<String> csvLines = Files.readAllLines(Paths.get(csvPath));
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(arffPath))) {
            writer.println("@relation mnist_3_vs_5");
            writer.println();
            
            for (int i = 0; i < 784; i++) {
                writer.println("@attribute pixel_" + i + " numeric");
            }
            writer.println("@attribute class {trois,cinq}");
            writer.println();
            
            writer.println("@data");
            for (int lineNum = 0; lineNum < csvLines.size(); lineNum++) {
                String line = csvLines.get(lineNum);
                String[] fields = line.split(",");
                
                if (fields.length != 785) {
                    throw new DataFormatMismatchException(lineNum + 1, 785, fields.length);
                }
                
                StringBuilder arffLine = new StringBuilder();
                try {
                    for (int i = 0; i < 784; i++) {
                        if (i > 0) arffLine.append(",");
                        int pixel = Integer.parseInt(fields[i].trim());
                        arffLine.append(pixel);
                    }
                    arffLine.append(",").append(fields[784].trim());
                } catch (NumberFormatException e) {
                    throw new DataFormatMismatchException(lineNum + 1, fields[784]);
                }
                
                writer.println(arffLine.toString());
            }
        }
    }
    
    /**
     * Create text file from BinaryMNISTReader data.
     */
    public static void createFromBinaryReader(BinaryMNISTReader reader, String outputPath) 
            throws DataFormatMismatchException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath))) {
            int[][] pixels = reader.getPixelData();
            String[] labels = reader.getLabels();
            
            for (int i = 0; i < pixels.length; i++) {
                StringBuilder line = new StringBuilder();
                for (int j = 0; j < pixels[i].length; j++) {
                    if (j > 0) line.append(",");
                    line.append(pixels[i][j]);
                }
                line.append(",").append(labels[i]);
                writer.println(line.toString());
            }
        } catch (IOException e) {
            throw new DataFormatMismatchException("Error writing CSV file: " + e.getMessage(), e);
        }
    }

}
