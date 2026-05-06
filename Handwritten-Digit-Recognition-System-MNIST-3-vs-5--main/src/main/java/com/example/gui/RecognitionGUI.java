package com.example.gui;

import com.example.ml.DigitClassifier;
import com.example.ml.NaiveBayesClassifier;
import com.example.ml.RandomForestClassifier;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Main GUI window for real-time handwritten digit recognition.
 * Features:
 * - 280x280 px drawing canvas
 * - Clear button to reset canvas
 * - Model selector (Naïve Bayes / Random Forest)
 * - Recognize button to predict drawn digit
 * - Result display with predicted label and confidence
 */
public class RecognitionGUI extends JFrame {
    
    private DrawingCanvas canvas;
    private JComboBox<String> modelSelector;
    private JButton recognizeButton;
    private JButton clearButton;
    private JLabel resultLabel;
    private JLabel confidenceLabel;
    private DigitClassifier currentClassifier;
    private String currentModelName;
    
    public RecognitionGUI() throws Exception {
        setTitle("Système de Reconnaissance de Chiffres (3 vs 5)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // Initialize main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Left: Drawing canvas
        canvas = new DrawingCanvas();
        JPanel canvasPanel = new JPanel();
        canvasPanel.add(canvas);
        canvasPanel.setBorder(BorderFactory.createTitledBorder("Dessinez un chiffre (3 ou 5)"));
        mainPanel.add(canvasPanel, BorderLayout.CENTER);
        
        // Right: Controls panel
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
        controlsPanel.setPreferredSize(new Dimension(220, 300));
        controlsPanel.setBorder(BorderFactory.createTitledBorder("Contrôles"));
        
        // Model selector
        JPanel modelPanel = new JPanel();
        modelPanel.setLayout(new BoxLayout(modelPanel, BoxLayout.Y_AXIS));
        JLabel modelLabel = new JLabel("Sélectionner le modèle :");
        modelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        modelSelector = new JComboBox<>(new String[]{"Naïve Bayes", "Random Forest"});
        modelSelector.setMaximumSize(new Dimension(180, 30));
        modelSelector.addActionListener(e -> switchModel());
        modelSelector.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        modelPanel.add(modelLabel);
        modelPanel.add(Box.createVerticalStrut(10));
        modelPanel.add(modelSelector);
        controlsPanel.add(modelPanel);
        controlsPanel.add(Box.createVerticalStrut(20));
        
        // Clear button
        clearButton = new JButton("Effacer");
        clearButton.setMaximumSize(new Dimension(180, 40));
        clearButton.addActionListener(e -> {
            canvas.clear();
            resultLabel.setText("Résultat : -");
            confidenceLabel.setText("Probabilité : -");
        });
        clearButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlsPanel.add(clearButton);
        controlsPanel.add(Box.createVerticalStrut(10));
        
        // Recognize button
        recognizeButton = new JButton("Reconnaître");
        recognizeButton.setMaximumSize(new Dimension(180, 50));
        recognizeButton.setFont(new Font("Arial", Font.BOLD, 14));
        recognizeButton.setBackground(new Color(50, 150, 50));
        recognizeButton.setForeground(Color.WHITE);
        recognizeButton.setFocusPainted(false);
        recognizeButton.addActionListener(e -> recognize());
        recognizeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlsPanel.add(recognizeButton);
        controlsPanel.add(Box.createVerticalStrut(30));
        
        // Result display
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        
        resultLabel = new JLabel("Résultat : -");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 20));
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultLabel.setForeground(new Color(0, 50, 150));
        
        confidenceLabel = new JLabel("Probabilité : -");
        confidenceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        confidenceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        resultPanel.add(Box.createVerticalStrut(10));
        resultPanel.add(resultLabel);
        resultPanel.add(Box.createVerticalStrut(10));
        resultPanel.add(confidenceLabel);
        resultPanel.add(Box.createVerticalStrut(10));
        
        controlsPanel.add(resultPanel);
        controlsPanel.add(Box.createVerticalGlue());
        
        mainPanel.add(controlsPanel, BorderLayout.EAST);
        
        // Initialize first model
        switchModel();
        
        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }
    
    /**
     * Switch between classifier implementations.
     */
    private void switchModel() {
        try {
            String selected = (String) modelSelector.getSelectedItem();
            String trainPath = "data/mnist/train-data.arff";
            
            if (!new File(trainPath).exists()) {
                JOptionPane.showMessageDialog(this, 
                    "Les données d'entraînement (train-data.arff) sont absentes.\n" +
                    "Veuillez exécuter ModelComparator d'abord.", 
                    "Fichier Manquant", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if ("Naïve Bayes".equals(selected)) {
                currentClassifier = new NaiveBayesClassifier(trainPath);
                currentModelName = "Naïve Bayes";
            } else {
                currentClassifier = new RandomForestClassifier(trainPath);
                currentModelName = "Random Forest";
            }
            
            resultLabel.setText("Résultat : -");
            confidenceLabel.setText("Probabilité : -");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors du chargement du modèle : " + e.getMessage(), 
                "Erreur de Chargement", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Perform digit recognition on drawn image.
     */
    private void recognize() {
        if (currentClassifier == null) {
            switchModel();
            if (currentClassifier == null) return;
        }
        
        try {
            // Get pixel data from canvas
            int[] pixelVector = canvas.getPixelVector();
            
            // Get prediction
            String predicted = currentClassifier.predict(pixelVector);
            resultLabel.setText("Résultat : " + predicted.toUpperCase());
            
            // Try to get confidence (if available)
            double confidence = 0;
            if (currentClassifier instanceof com.example.ml.NaiveBayesClassifier) {
                double[] probs = ((com.example.ml.NaiveBayesClassifier) currentClassifier)
                    .predictProbabilities(pixelVector);
                confidence = Math.max(probs[0], probs[1]) * 100;
            } else if (currentClassifier instanceof com.example.ml.RandomForestClassifier) {
                double[] probs = ((com.example.ml.RandomForestClassifier) currentClassifier)
                    .predictProbabilities(pixelVector);
                confidence = Math.max(probs[0], probs[1]) * 100;
            }
            
            if (confidence > 0) {
                confidenceLabel.setText(String.format("Probabilité : %.1f%%", confidence));
            } else {
                confidenceLabel.setText("Probabilité : N/A");
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors de la reconnaissance : " + e.getMessage(), 
                "Erreur de Reconnaissance", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                RecognitionGUI gui = new RecognitionGUI();
                gui.setVisible(true);
            } catch (Exception e) {
                System.err.println("Impossible de lancer l'interface : " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
