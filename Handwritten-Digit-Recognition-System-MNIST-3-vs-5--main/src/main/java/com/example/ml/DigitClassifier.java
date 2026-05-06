package com.example.ml;

/**
 * Interface for digit classification using machine learning.
 * Enables polymorphic switching between different classifier implementations
 * (Naïve Bayes, Random Forest, etc.) without changing client code.
 */
public interface DigitClassifier {
    
    /**
     * Predict the class of a handwritten digit based on pixel intensities.
     * 
     * @param pixelVector array of 784 integers (0-255) representing pixel intensities
     * @return predicted label: "trois" or "cinq"
     * @throws Exception if prediction fails (e.g., model not trained, invalid data)
     */
    String predict(int[] pixelVector) throws Exception;
}
