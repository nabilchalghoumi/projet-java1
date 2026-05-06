package com.example.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * Custom drawing canvas for digit recognition.
 * Allows users to draw digits with mouse on a 280x280 px canvas,
 * which is resized to 28x28 for model prediction.
 */
public class DrawingCanvas extends JPanel {
    
    private BufferedImage image;
    private Graphics2D g2d;
    private int canvasSize = 280;  // Display size
    private int modelSize = 28;    // Weka model input size
    
    public DrawingCanvas() {
        // Initialize image with white background
        image = new BufferedImage(canvasSize, canvasSize, BufferedImage.TYPE_INT_RGB);
        g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, canvasSize, canvasSize);
        
        // Setup mouse listener for drawing
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2d.drawOval(e.getX() - 1, e.getY() - 1, 2, 2);
                repaint();
            }
        });
        
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2d.drawLine(e.getX() - 1, e.getY() - 1, e.getX() + 1, e.getY() + 1);
                repaint();
            }
        });
        
        setPreferredSize(new Dimension(canvasSize, canvasSize));
        setBackground(Color.WHITE);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }
    
    /**
     * Clear canvas (reset to white background).
     */
    public void clear() {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, canvasSize, canvasSize);
        repaint();
    }
    
    /**
     * Get pixel data as array of 784 intensities (0-255).
     * Resizes from 280x280 to 28x28 using nearest-neighbor sampling.
     * 
     * @return array of 784 pixel values for Weka prediction
     */
    public int[] getPixelVector() {
        // Create 28x28 resized image
        BufferedImage resized = new BufferedImage(modelSize, modelSize, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = resized.createGraphics();
        g.drawImage(image, 0, 0, modelSize, modelSize, null);
        g.dispose();
        
        // Extract grayscale values
        int[] pixels = new int[784];
        int idx = 0;
        for (int y = 0; y < modelSize; y++) {
            for (int x = 0; x < modelSize; x++) {
                int rgb = resized.getRGB(x, y);
                // Extract red channel as grayscale intensity
                int gray = (rgb >> 16) & 0xFF;
                pixels[idx++] = gray;
            }
        }
        
        return pixels;
    }
}
