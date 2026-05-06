package com.example.data;

/**
 * Abstract base class for MNIST data processing.
 * Implements DataProcessor interface and provides common fields/methods
 * for all MNIST-related data handlers.
 */
public abstract class MNISTProvider implements DataProcessor {
    
    protected int nbImages;
    protected int resolution;  // 28 for 28x28 pixels
    protected int[][] pixelData;     // 2D array: [imageIndex][pixelIndex]
    protected String[] labels;       // Class labels: "trois" or "cinq"
    
    /**
     * Constructor initializing common MNIST parameters.
     * 
     * @param nbImages number of images to process
     * @param resolution pixel dimension (typically 28x28)
     */
    public MNISTProvider(int nbImages, int resolution) {
        this.nbImages = nbImages;
        this.resolution = resolution;
    }
    
    public int getNbImages() {
        return nbImages;
    }
    
    public int getResolution() {
        return resolution;
    }
    
    public int[][] getPixelData() {
        return pixelData;
    }
    
    public String[] getLabels() {
        return labels;
    }
    
    /**
     * Validates pixel vector (should have exactly 784 values for 28x28 image).
     * 
     * @param pixels array of pixel intensities (0-255)
     * @return true if valid (length == 784), false otherwise
     */
    protected boolean validatePixelVector(int[] pixels) {
        return pixels != null && pixels.length == resolution * resolution;
    }
    
    /**
     * Validates pixel intensity value (should be 0-255).
     * 
     * @param intensity the pixel intensity value
     * @return true if valid (0-255), false otherwise
     */
    protected boolean validatePixelIntensity(int intensity) {
        return intensity >= 0 && intensity <= 255;
    }
}
