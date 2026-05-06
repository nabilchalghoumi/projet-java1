"# projet-java1" 
# 🧠 Handwritten Digit Recognition System (MNIST 3 vs 5)

![Java](https://img.shields.io/badge/Java-11%2B-orange)
![Maven](https://img.shields.io/badge/Maven-3.9.5-blue)
![Weka](https://img.shields.io/badge/ML-Weka-yellow)
![Status](https://img.shields.io/badge/Project-Completed-brightgreen)

A complete Java project implementing machine learning-based recognition of handwritten digits (distinguishing "3" from "5") using the MNIST dataset. This project demonstrates advanced Object-Oriented Programming design, exception handling, machine learning integration with Weka, and GUI development using Swing.

**Course**: Programmation Orientée Objet : Java (1TA3)  
**Instructor**: Mohamed Mahmoud Moussa  
**Institution**: ENSTAB (École Nationale des Sciences et Technologies Avancées de Borj-Cédria)  
**Academic Year**: 2025/2026  

---

## 📌 Project Overview (4 Parts)

### 🔹 Part 1: OOP Architecture & File Processing
- Parsing MNIST binary format (IDX3 / IDX1)
- Conversion between multiple formats: binary → CSV → Excel → PNG → ARFF
- Core classes: `DataProcessor`, `MNISTProvider`, `BinaryMNISTReader`, `TextFileHandler`, `ExcelExporter`

### 🔹 Part 2: Custom Exception Handling
- Implementation of 3 checked exceptions
- Clear and detailed error messages
- Classes: `InvalidDimensionsException`, `MNISTFileNotFoundException`, `DataFormatMismatchException`

### 🔹 Part 3: AI & Polymorphism (Weka Integration)
- Machine learning models using Weka:
  - Naïve Bayes
  - Random Forest
- Polymorphic design for model interchangeability
- Classes: `DigitClassifier`, `NaiveBayesClassifier`, `RandomForestClassifier`, `ModelComparator`

### 🔹 Part 4: GUI (Real-time Recognition)
- Interactive drawing canvas (280×280 → 28×28 preprocessing)
- Real-time digit prediction
- Model selection interface
- Classes: `DrawingCanvas`, `RecognitionGUI`

---
TP JAVA PROJECT/
├── src/
│ ├── main/java/com/example/
│ │ ├── data/
│ │ ├── exceptions/
│ │ ├── ml/
│ │ ├── gui/
│ │ ├── Main.java
│ │ └── WekaExample.java
│ └── test/java/com/example/
├── data/mnist/
├── lib/weka-3-8-6/
├── tools/apache-maven-3.9.5/
├── pom.xml
└── README.md

---

## ⚙️ Prerequisites

- Java 11+ (Java 21 compatible)
- Maven 3.6+
- MNIST Dataset (download required)

---

## 🚀 Quick Start

### 1. Build the project
```bash
./tools/apache-maven-3.9.5/bin/mvn clean compile

## 📁 Project Structure
2. Download MNIST dataset
data/mnist/
data/mnist/
./tools/apache-maven-3.9.5/bin/mvn test
4. Run model comparison
./tools/apache-maven-3.9.5/bin/mvn exec:java -Dexec.mainClass="com.example.ml.ModelComparator"
5. Launch GUI
./tools/apache-maven-3.9.5/bin/mvn exec:java -Dexec.mainClass="com.example.gui.RecognitionGUI"

TextFileHandler.imageToFile("digit.png", "output.csv");
NaiveBayesClassifier classifier = new NaiveBayesClassifier("train.arff");
String prediction = classifier.predict(pixelVector);
System.out.println(prediction);
try {
    BinaryMNISTReader reader = new BinaryMNISTReader(imagesPath, labelsPath, 50);
    reader.load();
} catch (MNISTFileNotFoundException e) {
    System.err.println(e.getFilePath());
}

