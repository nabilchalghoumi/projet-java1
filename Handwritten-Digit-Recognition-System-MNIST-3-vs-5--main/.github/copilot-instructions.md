# Java Project - Copilot Instructions

This is a Maven-based Java project configured for building and testing Java applications.

## Key Build Commands

- `./tools/apache-maven-3.9.5/bin/mvn clean compile` - Compile the project
- `./tools/apache-maven-3.9.5/bin/mvn test` - Run unit tests
- `./tools/apache-maven-3.9.5/bin/mvn package` - Build JAR file
- `./tools/apache-maven-3.9.5/bin/mvn exec:java -Dexec.mainClass="com.example.Main"` - Run the application

## Project Structure

- `src/main/java/com/example/` - Application source code
- `src/test/java/com/example/` - Unit tests
- `pom.xml` - Maven configuration with Java 11, JUnit 5, and standard plugins

## Java Environment

- **Java Version**: 21 (compatible with pom.xml target of 11)
- **Build Tool**: Maven 3.9.5 (located in tools/)
- **Testing Framework**: JUnit 5
- **Main Class**: com.example.Main

## VS Code Integration

Configure tasks in .vscode/tasks.json for easy building and running from VS Code.
