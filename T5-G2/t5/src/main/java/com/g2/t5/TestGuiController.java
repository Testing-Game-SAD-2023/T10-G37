

package com.g2.t5;

import java.util.List;

public class TestGuiController {

    public static void main(String[] args) {
        // Crea un'istanza di GuiController
        GuiController guiController = new GuiController(null);

        // Chiama la funzione getLevels con il nome della classe desiderata
        String className = "Calcolatrice"; // Sostituisci con il nome della classe effettivo
        List<String> levels = guiController.getLevels(className);

        // Stampa i livelli ottenuti dalla funzione getLevels
        System.out.println("Livelli per la classe " + className + ":");
        for (String level : levels) {
            System.out.println(level);
        }
    }
}
