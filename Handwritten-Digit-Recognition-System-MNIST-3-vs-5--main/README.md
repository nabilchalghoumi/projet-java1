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

## 📁 Project Structure
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

##  Quick Start

### 1. Build the project
```bash
./tools/apache-maven-3.9.5/bin/mvn clean compile
1. Download MNIST dataset and place in data/mnist/
 2. Run mvn clean compile to verify build
3. Run mvn test to test Part 1 functionality
 4. Run ModelComparator to compare classifier accuracy
5. Launch GUI with RecognitionGUI to test real-time recognition
1. Open this folder in VS Code
2. Edit files in src/main/java/ and src/test/java/
3. Use the tasks to build and run
 
