package com.example.ml;

import weka.core.*;
import weka.core.converters.ArffLoader;
import weka.classifiers.trees.RandomForest;
import java.io.File;

/**
 * Concrete implementation of DigitClassifier using Weka's Random Forest classifier.
 * Loads training data from ARFF file and trains the model on instantiation.
 */
public class RandomForestClassifier implements DigitClassifier {
    
    private RandomForest classifier;
    private Instances trainingData;
    
    /**
     * Constructor: loads ARFF data and trains the Random Forest model.
     * 
     * @param arffPath path to ARFF training file
     * @throws Exception if ARFF file not found or model training fails
     */
    public RandomForestClassifier(String arffPath) throws Exception {
        // Load ARFF file
        ArffLoader loader = new ArffLoader();
        loader.setFile(new File(arffPath));
        trainingData = loader.getDataSet();
        
        // Set class attribute (last column)
        if (trainingData.classIndex() == -1) {
            trainingData.setClassIndex(trainingData.numAttributes() - 1);
        }
        
        // Initialize and train classifier
        classifier = new RandomForest();
        classifier.setNumIterations(100);  // Use 100 iterations
        classifier.buildClassifier(trainingData);
    }
    
    @Override
    public String predict(int[] pixelVector) throws Exception {
        if (pixelVector.length != 784) {
            throw new IllegalArgumentException("Pixel vector must have exactly 784 values");
        }
        
        // Convert pixel array to Weka Instance
        Instance instance = pixelsToInstance(pixelVector);
        
        // Predict class
        double predictedClassIndex = classifier.classifyInstance(instance);
        return trainingData.classAttribute().value((int) predictedClassIndex);
    }
    
    /**
     * Get prediction probabilities for both classes.
     * 
     * @param pixelVector array of 784 pixel intensities
     * @return array [prob_trois, prob_cinq]
     */
    public double[] predictProbabilities(int[] pixelVector) throws Exception {
        if (pixelVector.length != 784) {
            throw new IllegalArgumentException("Pixel vector must have exactly 784 values");
        }
        
        Instance instance = pixelsToInstance(pixelVector);
        return classifier.distributionForInstance(instance);
    }
    
    /**
     * Convert pixel array to Weka Instance compatible with training data.
     */
    private Instance pixelsToInstance(int[] pixelVector) throws Exception {
        double[] values = new double[trainingData.numAttributes()];
        
        // Set pixel values
        for (int i = 0; i < 784; i++) {
            values[i] = pixelVector[i];
        }
        
        // Set class as missing (to be predicted)
        values[trainingData.classIndex()] = Double.NaN;
        
        Instance instance = new DenseInstance(1.0, values);
        instance.setDataset(trainingData);
        return instance;
    }
}
