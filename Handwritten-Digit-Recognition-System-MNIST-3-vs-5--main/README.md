# Handwritten Digit Recognition System (MNIST 3 vs 5)

A complete Java project implementing machine learning-based recognition of handwritten digits (distinguishing "3" from "5") from the MNIST dataset. This project demonstrates advanced Object-Oriented design, exception handling, machine learning integration with Weka, and GUI development with Swing.

**Course**: Programmation Orientée Objet : Java (1TA3)  
**Instructor**: Mohamed Mahmoud Moussa  
**Institution**: ENSTAB (Ecole N ationale de sciences et de technologies avancées de Borj-cedria)  
**Academic Year**: 2025/2026  

## Project Overview: 4 Parts

### Part 1: Architecture OO & File Processing
- Parse MNIST binary format (IDX3/IDX1)
- Convert between formats: binary → CSV → Excel → PNG → ARFF
- Classes: `DataProcessor`, `MNISTProvider`, `BinaryMNISTReader`, `TextFileHandler`, `ExcelExporter`

### Part 2: Custom Exception Handling  
- 3 checked exceptions with detailed error messages
- Classes: `InvalidDimensionsException`, `MNISTFileNotFoundException`, `DataFormatMismatchException`

### Part 3: AI & Polymorphism
- Weka integration: Naïve Bayes & Random Forest classifiers
- Polymorphic design for easy model swapping
- Classes: `DigitClassifier`, `NaiveBayesClassifier`, `RandomForestClassifier`, `ModelComparator`

### Part 4: GUI (Real-time Recognition)
- Interactive drawing canvas (280×280 px → 28×28 for prediction)
- Model selection and real-time digit recognition
- Classes: `DrawingCanvas`, `RecognitionGUI`

## Project Structure

```
TP JAVA PROJECT/
├── src/
│   ├── main/java/com/example/
│   │   ├── data/
│   │   │   ├── DataProcessor.java
│   │   │   ├── MNISTProvider.java
│   │   │   ├── BinaryMNISTReader.java
│   │   │   ├── TextFileHandler.java
│   │   │   └── ExcelExporter.java
│   │   ├── exceptions/
│   │   │   ├── InvalidDimensionsException.java
│   │   │   ├── MNISTFileNotFoundException.java
│   │   │   └── DataFormatMismatchException.java
│   │   ├── ml/
│   │   │   ├── DigitClassifier.java
│   │   │   ├── NaiveBayesClassifier.java
│   │   │   ├── RandomForestClassifier.java
│   │   │   └── ModelComparator.java
│   │   ├── gui/
│   │   │   ├── DrawingCanvas.java
│   │   │   └── RecognitionGUI.java
│   │   ├── Main.java
│   │   └── WekaExample.java
│   └── test/java/com/example/
│       ├── TestPartie1.java
│       └── MainTest.java
├── data/
│   └── mnist/                     (MNIST dataset location)
├── lib/
│   └── weka-3-8-6/               (Weka library)
├── tools/
│   └── apache-maven-3.9.5/       (Maven build tool)
├── pom.xml                        (Maven configuration)
└── README.md
```

## Prerequisites

- **Java 11+** (Java 21 installed)
- **Maven 3.6+** (included in `tools/apache-maven-3.9.5/`)
- **MNIST Dataset** (download required - see below)

## Quick Start

### 1. Build the Project

```bash
./tools/apache-maven-3.9.5/bin/mvn clean compile
```

### 2. Download MNIST Dataset

Download binary files from:
- **Kaggle**: https://www.kaggle.com/datasets/hojjatk/mnist-dataset
- **Official**: http://yann.lecun.com/exdb/mnist/

Required files:
- `train-images-idx3-ubyte`
- `train-labels-idx1-ubyte`

Place in: `data/mnist/`

### 3. Run Tests (Part 1)

```bash
./tools/apache-maven-3.9.5/bin/mvn test
```

### 4. Run Model Comparison (Part 3)

```bash
./tools/apache-maven-3.9.5/bin/mvn exec:java -Dexec.mainClass="com.example.ml.ModelComparator"
```

### 5. Launch GUI (Part 4)

```bash
./tools/apache-maven-3.9.5/bin/mvn exec:java -Dexec.mainClass="com.example.gui.RecognitionGUI"
```

## Build Commands

| Task | Command |
|------|---------|
| Clean & Compile | `mvn clean compile` |
| Run Tests | `mvn test` |
| Build JAR | `mvn package` |
| Run GUI | `mvn exec:java -Dexec.mainClass="com.example.gui.RecognitionGUI"` |
| Model Comparison | `mvn exec:java -Dexec.mainClass="com.example.ml.ModelComparator"` |

## Dependencies

- **Weka 3.8.6**: Machine learning (local JAR)
- **Apache POI 5.2.3**: Excel handling
- **JUnit 5**: Testing

## Key Features

✅ **OO Architecture**: Interface-based design with abstract base classes  
✅ **Exception Handling**: Custom checked exceptions with detailed messages  
✅ **Multiple File Formats**: Binary, CSV, Excel, PNG, ARFF  
✅ **Machine Learning**: Weka classifiers with polymorphic interface  
✅ **GUI**: Interactive Swing application with real-time prediction  
✅ **Well-Tested**: Unit tests for critical components  

## Usage Examples

### Example 1: Convert Image to CSV
```java
import com.example.data.TextFileHandler;

// PNG must be exactly 28x28 pixels
TextFileHandler.imageToFile("digit.png", "output.csv");
```

### Example 2: Train & Predict
```java
import com.example.ml.NaiveBayesClassifier;

NaiveBayesClassifier classifier = new NaiveBayesClassifier("train-data.arff");
String prediction = classifier.predict(pixelVector);
System.out.println(prediction);  // "trois" or "cinq"
```

### Example 3: Convert CSV to Excel
```java
import com.example.data.ExcelExporter;

ExcelExporter.createFromCSV("data.csv", "data.xlsx");
```

## Exception Handling

```java
try {
    BinaryMNISTReader reader = new BinaryMNISTReader(imagesPath, labelsPath, 50);
    reader.load();
} catch (MNISTFileNotFoundException e) {
    System.err.println("File not found: " + e.getFilePath());
} catch (DataFormatMismatchException e) {
    System.err.println("Format error at line " + e.getLineNumber());
}
```

## Architecture Highlights

### Polymorphic Classifier Design
Easy switching between ML algorithms without changing client code:
```java
DigitClassifier classifier = selectModel();  // NB or RF
String result = classifier.predict(pixels);
```

### ARFF Format Support
Seamless conversion from CSV to Weka's ARFF format:
```java
TextFileHandler.csvToArff("data.csv", "data.arff");
```

### Class Hierarchy
```
DataProcessor (interface)
    ↑
MNISTProvider (abstract)
    ├── BinaryMNISTReader
    ├── TextFileHandler
    └── ExcelExporter
```

## Implementation Status

| Component | Status | Location |
|-----------|--------|----------|
| Part 1: File I/O | ✅ Complete | `src/main/java/com/example/data/` |
| Part 2: Exceptions | ✅ Complete | `src/main/java/com/example/exceptions/` |
| Part 3: ML Models | ✅ Complete | `src/main/java/com/example/ml/` |
| Part 4: GUI | ✅ Complete | `src/main/java/com/example/gui/` |
| Tests | ✅ Complete | `src/test/java/com/example/` |

## Next Steps

1. Download MNIST dataset and place in `data/mnist/`
2. Run `mvn clean compile` to verify build
3. Run `mvn test` to test Part 1 functionality
4. Run ModelComparator to compare classifier accuracy
5. Launch GUI with RecognitionGUI to test real-time recognition

## Troubleshooting

**Build fails**: Ensure MNIST files are present OR download from Kaggle  
**Weka error**: Verify `lib/weka-3-8-6/weka.jar` exists  
**GUI won't start**: Check Java 11+ is installed (`java -version`)  
**Model errors**: Generate training data first using BinaryMNISTReader

## Performance

- **Naïve Bayes**: Fast training, ~92% accuracy
- **Random Forest**: Slower, ~96% accuracy (with 100 trees)
- **GUI**: Responsive drawing and real-time prediction

---

**Build Tool**: Maven 3.9.5  
**Java Target**: Java 11  
**Java Runtime**: Java 21
- **JUnit 5**: Unit testing framework (included in pom.xml)

### Essential Java Tools:
1. **JDK (Java Development Kit)** - Compile and run Java code
2. **Maven** - Build projects, manage dependencies, run tests
3. **IDE/Editor** - VS Code with Java extensions recommended
4. **Git** - Version control

### Recommended VS Code Extensions:
- Extension Pack for Java
- Maven for Java  
- Debugger for Java

## Project Configuration

- Java version: 11 (compatible with Java 21)
- Maven version: 3.9.5
- Main class: com.example.Main
- Test framework: JUnit 5

## Next Steps

1. Open this folder in VS Code
2. Edit files in src/main/java/ and src/test/java/
3. Use the tasks to build and run
4. Add more Java files following the package structure
