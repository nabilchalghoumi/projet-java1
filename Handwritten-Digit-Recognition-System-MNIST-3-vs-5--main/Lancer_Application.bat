@echo off
setlocal

:: Tentative de détection de JAVA_HOME si non défini
if "%JAVA_HOME%"=="" (
    for /d %%i in ("C:\Program Files\Java\jdk*") do set "JAVA_HOME=%%i"
)

if "%JAVA_HOME%"=="" (
    echo [ERREUR] La variable d'environnement JAVA_HOME n'est pas définie.
    echo Veuillez installer un JDK (Java Development Kit) et définir JAVA_HOME.
    pause
    exit /b
)

echo Java detecte dans : %JAVA_HOME%
echo Lancement de l'application via Maven...
echo.

:: Chemin vers Maven inclus dans le projet
set "MVN_BIN=tools\apache-maven-3.9.5\bin\mvn.cmd"

if not exist "%MVN_BIN%" (
    echo [ERREUR] Maven n'a pas ete trouve dans %MVN_BIN%
    pause
    exit /b
)

call "%MVN_BIN%" exec:java -Dexec.mainClass="com.example.Main"

pause
