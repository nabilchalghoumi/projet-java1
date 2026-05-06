package com.example.ml;

import weka.core.*;
import weka.core.converters.ArffLoader;
import java.io.File;

/**
 * Compares two digit classifiers (Naïve Bayes vs Random Forest) on a test dataset.
 * Loads training and test ARFF files, trains both models, evaluates predictions,
 * and displays accuracy comparison.
 */
public class ModelComparator {
    
    public static void main(String[] args) throws Exception {
        System.out.println("=".repeat(70));
        System.out.println("MODEL COMPARATOR - Naïve Bayes vs Random Forest");
        System.out.println("=".repeat(70));
        System.out.println();
        
        String trainDataPath = "data/mnist/train-data.arff";
        String testDataPath = "data/mnist/test-data.arff";
        String imagesPath = "data/mnist/train-images-idx3-ubyte";
        String labelsPath = "data/mnist/train-labels-idx1-ubyte";
        
        // Ensure training data exists (Step 1 requirement)
        if (!new File(trainDataPath).exists()) {
            System.out.println("Generating training data (800 examples)...");
            BinaryMNISTReader trainReader = new BinaryMNISTReader(imagesPath, labelsPath, 400); // 400 threes + 400 fives
            trainReader.load();
            TextFileHandler.createFromBinaryReader(trainReader, "data/mnist/train-data.csv");
            TextFileHandler.csvToArff("data/mnist/train-data.csv", trainDataPath);
        }
        
        // Step 1: Charge train-data.arff
        System.out.println("[Step 1] Loading training data...");
        ArffLoader trainLoader = new ArffLoader();
        trainLoader.setFile(new File(trainDataPath));
        Instances trainData = trainLoader.getDataSet();
        trainData.setClassIndex(trainData.numAttributes() - 1);
        System.out.println("✓ Training data loaded: " + trainData.numInstances() + " instances");
        System.out.println();
        
        // Step 2: Crée un fichier test-data.arff à partir de 100 exemples supplémentaires
        System.out.println("[Step 2] Creating test data (100 additional examples)...");
        // Skip the first 400 of each used for training
        BinaryMNISTReader testReader = new BinaryMNISTReader(imagesPath, labelsPath, 50, 400); 
        testReader.load();
        String testCsv = "data/mnist/test-data.csv";
        TextFileHandler.createFromBinaryReader(testReader, testCsv);
        TextFileHandler.csvToArff(testCsv, testDataPath);
        System.out.println("✓ Test data created: " + testDataPath);
        System.out.println();
        
        // Step 3: Instancie NaiveBayesClassifier et RandomForestClassifier
        System.out.println("[Step 3] Initializing and training classifiers...");
        NaiveBayesClassifier nbClassifier = new NaiveBayesClassifier(trainDataPath);
        RandomForestClassifier rfClassifier = new RandomForestClassifier(trainDataPath);
        System.out.println("✓ Classifiers trained");
        System.out.println();
        
        // Load test data for evaluation
        ArffLoader testLoader = new ArffLoader();
        testLoader.setFile(new File(testDataPath));
        Instances testData = testLoader.getDataSet();
        testData.setClassIndex(testData.numAttributes() - 1);
        
        // Step 4 & 5: Predict and Display Accuracy
        System.out.println("[Step 4] Predicting test labels...");
        EvaluationResult nbResult = evaluateClassifier(nbClassifier, testData);
        EvaluationResult rfResult = evaluateClassifier(rfClassifier, testData);
        
        System.out.println();
        System.out.println("=".repeat(70));
        System.out.println("RESULTS");
        System.out.println("=".repeat(70));
        System.out.println();
        
        System.out.println(String.format("%-30s | %-15s", "Model", "Accuracy"));
        System.out.println("-".repeat(70));
        System.out.println(String.format("%-30s | %14.2f%%", "Naïve Bayes", nbResult.accuracy * 100));
        System.out.println(String.format("%-30s | %14.2f%%", "Random Forest", rfResult.accuracy * 100));
        System.out.println();
        System.out.println("=".repeat(70));
    }
    
    /**
     * Evaluate a classifier on test data.
     */
    private static EvaluationResult evaluateClassifier(DigitClassifier classifier, Instances testData) 
            throws Exception {
        EvaluationResult result = new EvaluationResult();
        
        for (int i = 0; i < testData.numInstances(); i++) {
            Instance instance = testData.instance(i);
            String trueLabel = instance.stringValue(instance.classAttribute());
            
            // Predict without class value
            int[] pixels = instanceToPixels(instance);
            String predictedLabel = classifier.predict(pixels);
            
            // Update metrics
            if (trueLabel.equals(predictedLabel)) {
                result.correctPredictions++;
                if ("trois".equals(trueLabel)) {
                    result.troisTP++;
                } else {
                    result.cinqTP++;
                }
            } else {
                if ("trois".equals(trueLabel)) {
                    result.troisFN++;
                } else {
                    result.cinqFN++;
                }
            }
        }
        
        result.totalInstances = testData.numInstances();
        result.accuracy = (double) result.correctPredictions / result.totalInstances;
        result.troisRecall = (double) result.troisTP / (result.troisTP + result.troisFN);
        result.cinqRecall = (double) result.cinqTP / (result.cinqTP + result.cinqFN);
        
        return result;
    }
    
    /**
     * Convert Weka Instance to pixel array (first 784 attributes).
     */
    private static int[] instanceToPixels(Instance instance) {
        int[] pixels = new int[784];
        for (int i = 0; i < 784; i++) {
            pixels[i] = (int) instance.value(i);
        }
        return pixels;
    }
    
    /**
     * Helper class to store evaluation results.
     */
    private static class EvaluationResult {
        int correctPredictions = 0;
        int totalInstances = 0;
        double accuracy = 0;
        int troisTP = 0;
        int troisFN = 0;
        int cinqTP = 0;
        int cinqFN = 0;
        double troisRecall = 0;
        double cinqRecall = 0;
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
