package com.example;

import com.example.gui.RecognitionGUI;
import com.example.ml.ModelComparator;
import java.util.Scanner;

/**
 * Point d'entrée principal du projet.
 * Permet de lancer les différents composants du système.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("SYSTÈME DE RECONNAISSANCE DE CHIFFRES MNIST (3 vs 5)");
            System.out.println("=".repeat(50));
            System.out.println("1. Générer/Comparer les modèles (ModelComparator)");
            System.out.println("2. Lancer l'interface graphique (RecognitionGUI)");
            System.out.println("3. Lancer l'exemple Weka (WekaExample)");
            System.out.println("0. Quitter");
            System.out.print("\nVotre choix : ");
            
            String choice = scanner.nextLine();
            
            try {
                switch (choice) {
                    case "1":
                        ModelComparator.main(new String[0]);
                        break;
                    case "2":
                        System.out.println("Lancement de l'interface graphique...");
                        RecognitionGUI.main(new String[0]);
                        return; // GUI runs in its own thread, but main can exit or wait
                    case "3":
                        WekaExample.main(new String[0]);
                        break;
                    case "0":
                        System.out.println("Au revoir !");
                        return;
                    default:
                        System.out.println("Choix invalide.");
                }
            } catch (Exception e) {
                System.err.println("Erreur : " + e.getMessage());
                e.printStackTrace();
            }
            
            System.out.println("\nAppuyez sur Entrée pour continuer...");
            scanner.nextLine();
        }
    }
}
