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
//Example1: convert image to csv
import com.example.data.TextFileHandler;
TextFileHandler.imageToFile("digit.png", "output.csv");
//Example 2: Train & predict
import com.example.ml.NaiveBayesClassifier;
NaiveBayesClassifier classifier = new NaiveBayesClassifier("train.arff");
String prediction = classifier.predict(pixelVector);
System.out.println(prediction);
//Example 3: convert CSV to EXCEL
import com.example.data.ExcelExporter;
ExcelExporter.createFromCSV("data.csv", "data.xlsx");
//Exception Handling
try {
    BinaryMNISTReader reader = new BinaryMNISTReader(imagesPath, labelsPath, 50);
    reader.load();
} catch (MNISTFileNotFoundException e) {
    System.err.println(e.getFilePath());
}

Architecture Highlights
Polymorphic Classifier Design
Easy switching between ML algorithms without changing client code:

DigitClassifier classifier = selectModel();  // NB or RF
String result = classifier.predict(pixels);
ARFF Format Support
Seamless conversion from CSV to Weka's ARFF format:

TextFileHandler.csvToArff("data.csv", "data.arff");
Class Hierarchy
DataProcessor (interface)
    ↑
MNISTProvider (abstract)
    ├── BinaryMNISTReader
    ├── TextFileHandler
    └── ExcelExporter
Implementation Status
Component	Status	Location
Part 1: File I/O	✅ Complete	src/main/java/com/example/data/
Part 2: Exceptions	✅ Complete	src/main/java/com/example/exceptions/
Part 3: ML Models	✅ Complete	src/main/java/com/example/ml/
Part 4: GUI	✅ Complete	src/main/java/com/example/gui/
Tests	✅ Complete	src/test/java/com/example/
Next Steps
Download MNIST dataset and place in data/mnist/
Run mvn clean compile to verify build
Run mvn test to test Part 1 functionality
Run ModelComparator to compare classifier accuracy
Launch GUI with RecognitionGUI to test real-time recognition
Troubleshooting
Build fails: Ensure MNIST files are present OR download from Kaggle
Weka error: Verify lib/weka-3-8-6/weka.jar exists
GUI won't start: Check Java 11+ is installed (java -version)
Model errors: Generate training data first using BinaryMNISTReader

Performance
Naïve Bayes: Fast training, ~92% accuracy
Random Forest: Slower, ~96% accuracy (with 100 trees)
GUI: Responsive drawing and real-time prediction
Build Tool: Maven 3.9.5
Java Target: Java 11
Java Runtime: Java 21

JUnit 5: Unit testing framework (included in pom.xml)
Essential Java Tools:
JDK (Java Development Kit) - Compile and run Java code
Maven - Build projects, manage dependencies, run tests
IDE/Editor - VS Code with Java extensions recommended
Git - Version control
Recommended VS Code Extensions:
Extension Pack for Java
Maven for Java
Debugger for Java
Project Configuration
Java version: 11 (compatible with Java 21)
Maven version: 3.9.5
Main class: com.example.Main
Test framework: JUnit 5
Next Steps
1-Open this folder in VS Code
2-Edit files in src/main/java/ and src/test/java/
3-Use the tasks to build and run
4-Add more Java files following the package structure
