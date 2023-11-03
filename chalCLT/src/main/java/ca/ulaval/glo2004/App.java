package ca.ulaval.glo2004;

import ca.ulaval.glo2004.domaine.Controleur;
import ca.ulaval.glo2004.gui.MainWindow;

import javax.swing.*;

public class App {

    public static void main(String[] args) {
        Controleur controleur = Controleur.getInstance();

        MainWindow mainWindow = new MainWindow(controleur);
        
        // JFrame configuration
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.pack();
        mainWindow.setSize(800, 600);
        mainWindow.setVisible(true);
    }
}
